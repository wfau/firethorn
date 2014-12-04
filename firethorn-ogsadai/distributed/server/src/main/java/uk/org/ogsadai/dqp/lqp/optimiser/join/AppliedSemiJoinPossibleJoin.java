package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;

public class AppliedSemiJoinPossibleJoin implements PossibleJoin
{
    private final Operator mLhsChild;
    private final ApplyOperator mApplyOperator;
    
    public AppliedSemiJoinPossibleJoin(
        Operator lhsChild, ApplyOperator applyOperator)
    {
        mLhsChild = lhsChild;
        mApplyOperator = applyOperator;
    }

    @Override
    public JoinRank getRank(CardinalityEstimator cardinalityEstimator,
        PartitioningOptimiser partitioner)
    {
        try
        {
            mApplyOperator.setChild(0, mLhsChild);
            mApplyOperator.update();
            mApplyOperator.accept(cardinalityEstimator);
        }
        catch(LQPException e)
        {
            throw new RuntimeException(e);
        }
        
        double selectivity = 
            Annotation.getCardinalityAnnotation(mApplyOperator) / 
            (Annotation.getCardinalityAnnotation(mApplyOperator.getChild(0)) *
             Annotation.getCardinalityAnnotation(mApplyOperator.getChild(1)));

        partitioner.annotateWithEvalNodes(mApplyOperator);

        Operator rhsChild = mApplyOperator.getChild(1);
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
        mLhsChild.setParent(null);

        return rank;
    }

    @Override
    public Operator apply(JoinGroup joinGroup)
    {
        try
        {
            mApplyOperator.setChild(0, mLhsChild);
            mApplyOperator.update();
    
            joinGroup.addRelation(mApplyOperator);
            joinGroup.removeSemiJoinPredicate(
                ((SemiJoinOperator)mApplyOperator.getOperator()).getPredicate());
            joinGroup.removeRelation(mLhsChild);
    
            return mApplyOperator;
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
        sb.append("AppliedSemiJoin(").append(
            ((SemiJoinOperator)mApplyOperator.getOperator()).getPredicate());
        sb.append(",").append(mLhsChild);
        sb.append(")");
        return sb.toString();
    }
}
