package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;

public class LimitOperator extends UnaryOperator 
{
    
    protected long mLimit;

    public LimitOperator(String limit)
    {
        mID = OperatorID.LIMIT;
        mLimit = Long.parseLong(limit);
    }

    @Override
    public void accept(OperatorVisitor visitor) 
    {
    }

    public long getLimit() 
    {
        return mLimit;
    }

}
