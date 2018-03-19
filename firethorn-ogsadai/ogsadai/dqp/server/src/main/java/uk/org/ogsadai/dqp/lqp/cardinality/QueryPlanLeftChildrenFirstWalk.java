package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;

public class QueryPlanLeftChildrenFirstWalk
{
    private OperatorVisitor mVisitor;
    
    public QueryPlanLeftChildrenFirstWalk(OperatorVisitor visitor)
    {
        mVisitor = visitor;
    }
    
    public void walk(Operator operator)
    {
        if (operator.getID() == OperatorID.APPLY)
        {
            // Ensure we visit the children in the correct order
            int first = ((ApplyOperator) operator).getOuterRelationChildIndex();
            walk(operator.getChild(first));
            walk(operator.getChild(1-first));
        }
        else
        {
            for (int i=0; i<operator.getChildCount(); ++i)
            {
                walk(operator.getChild(i));
            }
        }
        if (operator.getID() != OperatorID.NIL) operator.accept(mVisitor);
    }
}
