package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

public class DerbyJoinPlan implements JoinPlan 
{

    private InnerThetaJoinOperator mJoinOperator;
    private double mCost;

    public DerbyJoinPlan(InnerThetaJoinOperator joinOperator) 
    {
        mJoinOperator = joinOperator;
    }
    
    public void setCost(double cost)
    {
        mCost = cost;
    }

    @Override
    public double getCost() 
    {
        return mCost;
    }

    @Override
    public void apply() throws LQPException 
    {
        Annotation.addImplementationAnnotation(mJoinOperator, "DERBY_JOIN");
    }

}
