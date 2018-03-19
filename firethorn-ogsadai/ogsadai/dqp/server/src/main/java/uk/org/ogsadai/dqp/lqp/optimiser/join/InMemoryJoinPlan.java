package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

public class InMemoryJoinPlan implements JoinPlan 
{
    
    /** There is a primary expression in the join condition. */
    public static final String PRIMARY_EXPRESSION = "PRIMARY_EXPRESSION";
    
    private InnerThetaJoinOperator mOperator;

    private double mCost;

    private String mReadFirst;

    public InMemoryJoinPlan(
            InnerThetaJoinOperator operator, String readFirst, double cost) 
    {
        mOperator = operator;
        mReadFirst = readFirst;
        mCost = cost;
    }

    @Override
    public double getCost()
    {
        return mCost;
    }

    @Override
    public void apply()
    {
        // primary expression exists so choose a theta join
        Annotation.addImplementationAnnotation(mOperator, PRIMARY_EXPRESSION);
        Annotation.addReadFirstAnnotation(mOperator, mReadFirst);
    }

}
    