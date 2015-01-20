package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;

public class TopOperator extends UnaryOperator 
{
    
    protected long mTop;

    public TopOperator(String limit)
    {
        mID = OperatorID.TOP;
        mTop = Long.parseLong(limit);
    }

    @Override
    public void accept(OperatorVisitor visitor) 
    {
    }

    public long getLimit() 
    {
        return mTop;
    }

}
