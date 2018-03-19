package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.Collections;
import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.JoinGroupExtractor;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Join ordering optimiser.
 * <p>
 * Chooses the order of the joins in the query plan. A greedy algorithm is used
 * to select join order. The output execution chain starts with inner theta
 * joins, then contains projects and finally any remaining select predicates.
 * The output join order will be a bushy tree (rather then left-deep or 
 * right-deep).
 * <p>
 * The cost of each possible join is determined the following information:
 * <ol>
 * <li>Are the two sides of the join on the same data node (i.e. same database).
 * True is higher ranked.</li>
 * <li>Are the two sides of the join on the evaluation node (i.e. exposed by the
 * same OGSA-DAI server. True is higher ranked.</li>
 * <li>The join selectivity, which is defined as (join cardinality)/((left child
 * cardinality)*(right child cardinality)). Lower values are higher ranked.
 * </ol>
 * Inner theta joins are ranked according to the above criteria in the order
 * specified. The data nodes and evaluation nodes are part of the criteria to
 * support later implosion where the whole join can be sent to the data source.
 * Even when this cannot be done, prioritising the join of to branches on the
 * same evaluation may reduce the amount of data transfer to be performed.
 * <p>
 * Cross joins (products) can be performed in any order and this optimiser
 * makes not effort to optimise that ordering.
 * <p>
 * There are a few limitation of this algorithm and implementation:
 * <ul>
 *   <li>
 *   The greedy algorithm is not optimal. But according to Database System
 *   Implementation by Garcia-Molina, Ullman and Widom it is a commonly used
 *   heuristic (section 7.6.6, page 403)
 *   </li>
 *   <li>
 *   Although we prioritise the possibility of implosion by preferring the
 *   case where both sides of the join are on the same data node we do not 
 *   check to confirm the join (and the child query plan) can actually be 
 *   imploded.
 *   </li>
 *   <li>
 *   Highly ranking joins where both sides are on the same evaluation node may
 *   not be the best approach. This is left over from an initial attempt at
 *   prioritising same data node joins that mistakenly used the evaluation node
 *   rather than the data node (see ticket #327).
 *   </li>
 *   <li>
 *   No optimisation is carried out for the ordering of products.
 *   </li>
 *   <li>
 *   The ranking assumes that imploding the join is always best.  In some 
 *   cases this may lead to more data being transferred than if the join
 *   was executed by OGSA-DAI.
 *   </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class JoinOrderingOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    /** Cardinality estimator. */
    private CardinalityEstimator mCardinalityEstimator;

    /** 
     * Partitioner. Used to decide where the potential join operators would 
     * be executed. 
     */
    private PartitioningOptimiser mPartitioner;
    
    /**
     * Sets the cardinality estimator.
     * 
     * @param cardinalityEstimator  cardinality estimator.
     */
    public void setCardinalityEstimator(
        CardinalityEstimator cardinalityEstimator)
    {
        mCardinalityEstimator = cardinalityEstimator;
    }
    
    /**
     * Sets the partitioner used to decide when join operators would be 
     * executed in order to prioritise joins on the same data node so they
     * can be imploded.
     * 
     * @param partitioner  partitioner
     */
    public void setPartitioner(
        PartitioningOptimiser partitioner)
    {
        mPartitioner = partitioner;
    }

    @Override
    public Operator optimise(Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) throws LQPException
    {
        mCardinalityEstimator.setDataDictionary(
            requestFederation.getDataDictionary());

        List<JoinGroup> joinGroups = JoinGroupExtractor.getJoinGroups(lqpRoot);
        // We want to process join groups starting from the "leaf groups"
        Collections.reverse(joinGroups);

        for (JoinGroup joinGroup : joinGroups)
        {
//            System.out.println("\n\n\n----------Join group: " + joinGroup);
            processJoinGroup(joinGroup);
        }
        System.out.println("No more join groups");
        return lqpRoot;
    }
    
    private void processJoinGroup(JoinGroup joinGroup)
    {
        Operator currentRoot = null;
        
        List<PossibleJoin> possibleJoins = joinGroup.getPossibleJoins(); 
        while (possibleJoins.size() > 0)
        {
            System.out.println("\n**** New iteration (size=" + possibleJoins.size() + ")");
            PossibleJoin bestPossibleJoin = null;
            JoinRank bestRank = new JoinRank(0, false, false, false);

            for (PossibleJoin pj : possibleJoins)
            {
                System.out.println("Possible Join: " + pj);
                JoinRank rank = pj.getRank(mCardinalityEstimator, mPartitioner);
                System.out.println(" ->   Rank: " + rank);
                if (rank.higherThan(bestRank) || bestPossibleJoin == null)
                {
                    bestRank = rank;
                    bestPossibleJoin = pj;
                }
                
            }
            
            System.out.println("Applying possible join: " + bestPossibleJoin);
            currentRoot = bestPossibleJoin.apply(joinGroup);

            // Update the possible joins
            possibleJoins = joinGroup.getPossibleJoins();
        }

        // It may happen that we will have some predicates left
        for (Predicate p : joinGroup.getPredicates())
        {
            try
            {
                SelectOperator selOp = new SelectOperator(currentRoot, p);
                selOp.update();
                selOp.accept(mCardinalityEstimator);
                currentRoot = selOp;
            }
            catch(LQPException e)
            {
                throw new RuntimeException(e);
            }
        }

        joinGroup.reconnectWithParent(currentRoot);
    }
}
