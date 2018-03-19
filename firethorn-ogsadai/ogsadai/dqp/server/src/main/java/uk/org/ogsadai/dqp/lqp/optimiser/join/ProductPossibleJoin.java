package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;

public class ProductPossibleJoin implements PossibleJoin
{
    private final Operator mLhsChild;
    private final Operator mRhsChild;
    private final ProductOperator mProductOperator;

    public ProductPossibleJoin(Operator leftOp, Operator rightOp)
    {
        mLhsChild = leftOp;
        mRhsChild = rightOp;
        mProductOperator = new ProductOperator();
    }

    @Override
    public JoinRank getRank(CardinalityEstimator cardinalityEstimator,
        PartitioningOptimiser partitioner)
    {
        try
        {
            mProductOperator.setChild(0, mLhsChild);
            mProductOperator.setChild(1, mRhsChild);
            mProductOperator.update();
            mProductOperator.accept(cardinalityEstimator);
        }
        catch(LQPException e)
        {
            throw new RuntimeException(e);
        }

        double selectivity = 
            Annotation.getCardinalityAnnotation(mProductOperator) / 
            (Annotation.getCardinalityAnnotation(mProductOperator.getChild(0)) *
             Annotation.getCardinalityAnnotation(mProductOperator.getChild(1)));

        partitioner.annotateWithEvalNodes(mProductOperator);

        boolean tableScanJoin = 
            mLhsChild instanceof ScanOperator && 
            mRhsChild instanceof ScanOperator;

        DataNode firstOpDataNode = 
            Annotation.getDataNodeAnnotation(mLhsChild);
        DataNode secondOpDataNode = 
            Annotation.getDataNodeAnnotation(mRhsChild);
        EvaluationNode firstOpEvaluationNode = 
            Annotation.getEvaluationNodeAnnotation(mLhsChild);
        EvaluationNode secondOpEvaluationNode = 
            Annotation.getEvaluationNodeAnnotation(mRhsChild);
        
        boolean sameDataNode = 
            firstOpDataNode != null && 
            secondOpDataNode != null &&
            firstOpDataNode.equals(secondOpDataNode);
        
        boolean sameEvaluationNode = 
            firstOpEvaluationNode != null && 
            secondOpEvaluationNode != null &&
            firstOpEvaluationNode.equals(secondOpEvaluationNode);
        
        JoinRank rank = new JoinRank(
            selectivity,
            sameDataNode,
            sameEvaluationNode,
            tableScanJoin);
        mProductOperator.disconnect();

        return rank;
    }

    
    @Override
    public Operator apply(JoinGroup joinGroup)
    {
        try
        {
            // this is a proper join
            mProductOperator.setChild(0, mLhsChild);
            mProductOperator.setChild(1, mRhsChild);
            mProductOperator.update();
    
            joinGroup.addRelation(mProductOperator);
            joinGroup.removeRelation(mLhsChild);
            joinGroup.removeRelation(mRhsChild);
    
            return mProductOperator;
        }
        catch(LQPException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Product(").append(mLhsChild).append(",").append(mRhsChild);
        sb.append(")");
        return sb.toString();
    }

}
