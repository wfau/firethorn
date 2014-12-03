package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

public class DerbyStreamingJoinPlan implements JoinPlan 
{

    private InnerThetaJoinOperator mJoinOperator;
    private double mCost;
    private String mReadFirst;

    public DerbyStreamingJoinPlan(InnerThetaJoinOperator joinOperator) 
    {
        mJoinOperator = joinOperator;
    }
    
    public void setCost(double cost)
    {
        mCost = cost;
    }

    public void setReadFirst(String readFirst) 
    {
        mReadFirst = readFirst;
    }

    @Override
    public double getCost() 
    {
        return mCost;
    }

    @Override
    public void apply() throws LQPException 
    {
        Annotation.addImplementationAnnotation(
                mJoinOperator, "DERBY_STREAMING_JOIN");
        Annotation.addReadFirstAnnotation(mJoinOperator, mReadFirst);
    }

}
