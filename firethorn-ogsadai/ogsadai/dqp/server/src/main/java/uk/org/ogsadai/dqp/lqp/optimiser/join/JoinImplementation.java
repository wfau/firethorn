package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public interface JoinImplementation 
{
    /**
     * Returns a query plan or <code>null</code> if the join implementation
     * doesn't apply to this join.
     * 
     * @param joinOperator
     *            root of the tree
     * @return join plan or <code>null</code>
     */
    JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator joinOperator);
}
