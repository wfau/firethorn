package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.expression.arithmetic.Constant;

public class OrderedSweepJoinPlan implements JoinPlan 
{

    private InnerThetaJoinOperator mJoinOperator;
    private Constant mSize;
    private int mAttr0ChildIndex;
    protected Attribute mAttr0;
    protected Attribute mAttr1;
    private double mCost;

    public OrderedSweepJoinPlan(Attribute attr0, Attribute attr1, Constant size) 
    {
        mAttr0 = attr0;
        mAttr1 =  attr1;
        mSize = size;
    }
    
    public void setJoinOperator(InnerThetaJoinOperator operator)
    {
        mJoinOperator = operator;
    }

    public void setAttr0ChildIndex(int attr0ChildIndex) 
    {
        mAttr0ChildIndex = attr0ChildIndex;
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
        OptimiserUtils.orderQueryPlan(
                mJoinOperator.getChild(mAttr0ChildIndex), mAttr0);
        OptimiserUtils.orderQueryPlan(
                mJoinOperator.getChild(1-mAttr0ChildIndex), mAttr1);
        
        // Move the join predicate to a select above the join
        Operator joinParent = mJoinOperator.getParent();
        SelectOperator select = new SelectOperator(
                mJoinOperator.getPredicate());
        OptimiserUtils.copyPlacementAnnotations(
                mJoinOperator,select);
            
        joinParent.replaceChild(mJoinOperator, select);
        mJoinOperator.setParent(select);
        select.setChild(0, mJoinOperator);
        joinParent.update();
            
        // Annotate to say we are using the PLANE_SWEEP implementation
        Annotation.addImplementationAnnotation(mJoinOperator, "PLANE_SWEEP");

        // Add annotations to pass the parameters required by the builder
        mJoinOperator.addAnnotation("PLANE_SWEEP_size", mSize.getResult());
        mJoinOperator.addAnnotation("PLANE_SWEEP_attr0", mAttr0);
        mJoinOperator.addAnnotation("PLANE_SWEEP_attr1", mAttr1);
        mJoinOperator.addAnnotation("PLANE_SWEEP_attr0ChildIndex", 
                mAttr0ChildIndex);
    }
   
}
