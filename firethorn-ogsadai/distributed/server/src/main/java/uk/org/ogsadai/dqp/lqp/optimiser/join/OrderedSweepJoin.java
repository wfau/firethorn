package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.arithmetic.visitors.AnyArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.ConstantArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.FunctionArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.TableColumnArithmeticExpressionValidatingVisitor;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class OrderedSweepJoin implements JoinImplementation
{
    @Override
    public JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator joinOperator) 
    {
        OrderedSweepJoinPlan plan = findSweepPredicate(joinOperator);
        if (plan == null) return null;

        // Check that the two attributes are in different children
        boolean matched = false;
        int attr0ChildIndex = -1;
        for (int i=0; i<2; ++i)
        {
            Operator child0 = joinOperator.getChild(i);
            Operator child1 = joinOperator.getChild(1-i);
            
            if (AttributeUtils.containsMatching(
                    plan.mAttr0,
                    child0.getHeading().getAttributes(),
                    AttributeMatchMode.NAME_AND_NULL_SOURCE) &&
                    AttributeUtils.containsMatching(
                            plan.mAttr1,
                            child1.getHeading().getAttributes(),
                            AttributeMatchMode.NAME_AND_NULL_SOURCE))
            {
                matched = true;
                attr0ChildIndex = i;
                break;
            }
        }
        if (!matched) return null;

        // Find if children can be sorted by the attributes
        if (!(OptimiserUtils.canQueryPlanBeOrdered(
                joinOperator.getChild(attr0ChildIndex), plan.mAttr0) &&
            OptimiserUtils.canQueryPlanBeOrdered(
                joinOperator.getChild(1-attr0ChildIndex), plan.mAttr1)))
        {
            return null;
        }

        Double[] card = {
                Annotation.getCardinalityAnnotation(joinOperator.getChild(0)),
                Annotation.getCardinalityAnnotation(joinOperator.getChild(1)) };
        
        // cost is a simple read of both sides 
        // with a sliding window of values being stored
        Cost cost = new Cost();
        cost.setReads(card[0] + card[1]);

        plan.setJoinOperator(joinOperator);
        plan.setAttr0ChildIndex(attr0ChildIndex);
        plan.setCost(cost.getCost());
        return plan;
    }

    /**
     * Returns a predicate that specifies that a plane-sweep join algorithm can
     * be use to the given join. Such a predicate consists of the pattern
     * 
     * <pre>
     * Contains( Point(string, x1, y1), Box(string, x2, y2, xSize, ySize).
     * </pre>
     * 
     * where y1 and y2 are from different children of the join.
     * <p>
     * This predicate need not necessarily be the predicate of the join
     * operator. It could be a predicate of a select operator above the join
     * operator, so long as it does join attributes from both sides of the join.
     * 
     * @param op
     *            join operator
     * 
     * @return the plane-sweep predicate, or <code>null</code> if no appropriate
     *         predicate is found.
     */
    private OrderedSweepJoinPlan findSweepPredicate(InnerThetaJoinOperator op)
    {
        // First check the predicate attached to the op
        Predicate pred = op.getPredicate();
        OrderedSweepJoinPlan plan = isSweepPredicate(pred);
        if (plan != null) return plan;

        // look for sweeps possibilities in SELECTs above
        Operator operator = op.getParent();
        while (operator.getID() != OperatorID.NIL
                && operator.getID() == OperatorID.SELECT) 
        {
            SelectOperator select = (SelectOperator) operator;
            pred = select.getPredicate();
            plan = isSweepPredicate(pred);
            if (plan != null)
            {
                return plan;
            }
            else 
            {
                operator = operator.getParent();
            }
        }

        return null;
    }

    /**
     * Checks if given predicate is a sweep predicate.
     * 
     * @param pred
     *            predicate
     * 
     * @return <tt>true</tt> if predicate is a sweep predicate, <tt>false</tt>
     *         otherwise.
     */
    private OrderedSweepJoinPlan isSweepPredicate(Predicate pred) 
    {
        if (!(pred instanceof CommonPredicate)) return null;
        Expression expr = ((CommonPredicate) pred).getExpression();
        
        TableColumnArithmeticExpressionValidatingVisitor attr0 = 
            new TableColumnArithmeticExpressionValidatingVisitor();
        TableColumnArithmeticExpressionValidatingVisitor attr1 = 
            new TableColumnArithmeticExpressionValidatingVisitor();
        ConstantArithmeticExpressionValidatingVisitor size = 
            new ConstantArithmeticExpressionValidatingVisitor();

        FunctionArithmeticExpressionValidatingVisitor boxValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "BOX", 
                new AnyArithmeticExpressionValidatingVisitor(),
                new AnyArithmeticExpressionValidatingVisitor(),
                attr1,
                new AnyArithmeticExpressionValidatingVisitor(),
                size);

        FunctionArithmeticExpressionValidatingVisitor pointValidator = 
            new FunctionArithmeticExpressionValidatingVisitor(
                "POINT", 
                new AnyArithmeticExpressionValidatingVisitor(),
                new AnyArithmeticExpressionValidatingVisitor(), 
                attr0);

        EqualsValidatingExpressionVisitor equalsVisitor = 
            new EqualsValidatingExpressionVisitor(
                new ConstantArithmeticExpressionValidatingVisitor(),
                new FunctionArithmeticExpressionValidatingVisitor("CONTAINS",
                        pointValidator, boxValidator));
        expr.accept(equalsVisitor);

        if (equalsVisitor.isValid())
        {
            return new OrderedSweepJoinPlan(
                    new AttributeImpl(attr0.getTableColumn()), 
                    new AttributeImpl(attr1.getTableColumn()), 
                    size.getConstant());
        }
        else
        {
            return null;
        }
    }
}
