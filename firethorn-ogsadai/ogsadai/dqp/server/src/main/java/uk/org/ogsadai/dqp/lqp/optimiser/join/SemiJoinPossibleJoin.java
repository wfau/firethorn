package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.NilOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;

public class SemiJoinPossibleJoin implements PossibleJoin
{
    private final Operator mLhsChild;
    private final SemiJoinOperator mSemiJoinOperator;
    
    public SemiJoinPossibleJoin(
        Operator lhsChild, SemiJoinOperator semiJoinOperator)
    {
        mLhsChild = lhsChild;
        mSemiJoinOperator = semiJoinOperator;
    }

    @Override
    public JoinRank getRank(CardinalityEstimator cardinalityEstimator,
        PartitioningOptimiser partitioner)
    {
//        Operator oldLHSChild = mSemiJoinOperator.getChild(0);
        Operator oldParent = mSemiJoinOperator.getParent();

        try
        {
            // the query plan above the semi join operator might contain loops 
            // or may be invalid in some other way after setting the new child 
            // so we just cut it off at the semi join 
            Operator root = new NilOperator();
            root.setChild(0, mSemiJoinOperator);
            mSemiJoinOperator.setParent(root);
            mSemiJoinOperator.setChild(0, mLhsChild);
            mSemiJoinOperator.update();
            mSemiJoinOperator.accept(cardinalityEstimator);
        }
        catch(LQPException e)
        {
            throw new RuntimeException(e);
        }
        
        double selectivity = 
            Annotation.getCardinalityAnnotation(mSemiJoinOperator) / 
            (Annotation.getCardinalityAnnotation(mSemiJoinOperator.getChild(0)) *
             Annotation.getCardinalityAnnotation(mSemiJoinOperator.getChild(1)));
        
        partitioner.annotateWithEvalNodes(mSemiJoinOperator);

        Operator rhsChild = mSemiJoinOperator.getChild(1);
        
        boolean tableScanJoin = 
            mLhsChild instanceof ScanOperator && 
            rhsChild instanceof ScanOperator;

        DataNode firstOpDataNode = 
            Annotation.getDataNodeAnnotation(mLhsChild);
        DataNode secondOpDataNode = 
            Annotation.getDataNodeAnnotation(rhsChild);
        EvaluationNode firstOpEvaluationNode = 
            Annotation.getEvaluationNodeAnnotation(mLhsChild);
        EvaluationNode secondOpEvaluationNode = 
            Annotation.getEvaluationNodeAnnotation(rhsChild);
        
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
        
        // return semi join operator to old state
//        mSemiJoinOperator.setChild(0, oldLHSChild);
        mSemiJoinOperator.setParent(oldParent);

        return rank;
    }

    @Override
    public Operator apply(JoinGroup joinGroup)
    {
        try
        {
            mSemiJoinOperator.setChild(0, mLhsChild);
            mSemiJoinOperator.update();
    
            joinGroup.addRelation(mSemiJoinOperator);
            joinGroup.removeSemiJoinPredicate(mSemiJoinOperator.getPredicate());
            joinGroup.removeRelation(mLhsChild);
            return mSemiJoinOperator;
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
        sb.append("SemiJoin(").append(mSemiJoinOperator.getPredicate());
        sb.append(", ").append(mLhsChild);
        sb.append(")");
        return sb.toString();
    }
}
