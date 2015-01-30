package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

public class TopOperator extends UnaryOperator 
{
    
    protected long mTop;

    public TopOperator(String limit)
    {
        mID = OperatorID.TOP;
        mTop = Long.parseLong(limit);
    }
  
    public TopOperator(String limit, Operator child)
    {
        this(limit);
        setChild(0, child);
    }
    
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();

        mOperatorHeading = new HeadingImpl(mChildOperator.getHeading());
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
    }

    public long getLimit() 
    {
        return mTop;
    }

}
