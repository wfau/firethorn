package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;

public class InnerThetaJoinPossibleJoin implements PossibleJoin
{
    private final Operator mLhsChild;
    private final Operator mRhsChild;
    private final InnerThetaJoinOperator mJoinOperator;
    
    public InnerThetaJoinPossibleJoin(
        Operator lhsChild, Operator rhsChild, Predicate predicate)
    {
        mLhsChild = lhsChild;
        mRhsChild = rhsChild;
        mJoinOperator = new InnerThetaJoinOperator(predicate);
    }

    @Override
    public JoinRank getRank(
        CardinalityEstimator cardinalityEstimator,
        PartitioningOptimiser partitioner)
    {
        try
        {
            mJoinOperator.setChild(0, mLhsChild);
            mJoinOperator.setChild(1, mRhsChild);
            mJoinOperator.update();
            mJoinOperator.accept(cardinalityEstimator);
        }
        catch(LQPException e)
        {
            throw new RuntimeException(e);
        }
        
        double product = 
            Annotation.getCardinalityAnnotation(mJoinOperator.getChild(0)) *
            Annotation.getCardinalityAnnotation(mJoinOperator.getChild(1));
        
        double selectivity = 1.0;
        if (product != 0)
        {
            selectivity = 
                Annotation.getCardinalityAnnotation(mJoinOperator) / product; 
        }
        
        partitioner.annotateWithEvalNodes(mJoinOperator);

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
        mJoinOperator.disconnect();

        return rank;
    }

    @Override
    public Operator apply(JoinGroup joinGroup)
    {
        try
        {
            mJoinOperator.setChild(0, mLhsChild);
            mJoinOperator.setChild(1, mRhsChild);
            mJoinOperator.update();
    
            joinGroup.addRelation(mJoinOperator);
            joinGroup.removePredicate(mJoinOperator.getPredicate());
            joinGroup.removeRelation(mLhsChild);
            joinGroup.removeRelation(mRhsChild);
    
            return mJoinOperator;
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
        sb.append("InnerThetaJoin(").append(mJoinOperator.getPredicate());
        sb.append(",").append(mLhsChild).append(",").append(mRhsChild);
        sb.append(")");
        return sb.toString();
    }
}
