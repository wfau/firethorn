package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;

public interface PossibleJoin
{
    /** 
     * Gets the join ranking for this possible join.
     * 
     * @param cardinalityEstimator  
     *   cardinality estimator. Used to estimate the cardinality of the
     *   of a join
     *   
     * @param partitioner
     *    partitioner, used to determine what data node and evaluation node
     *    the join operator will be executioned on.
     *    
     * @return join ranking
     */
    JoinRank getRank(
        CardinalityEstimator cardinalityEstimator,
        PartitioningOptimiser partitioner);

    Operator apply(JoinGroup joinGroup);
    
}
