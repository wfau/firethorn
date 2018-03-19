package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class InMemoryJoin implements JoinImplementation
{
    
    private long mMaxStoredRows = 10000;

    public InMemoryJoin() 
    {
    }
    
    public void setMaximumStoredRows(long max)
    {
        mMaxStoredRows = max;
    }
    

    @Override
    public JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator operator) 
    {

        Double leftCard = Annotation.getCardinalityAnnotation(operator.getChild(0));
        Double rightCard = Annotation.getCardinalityAnnotation(operator.getChild(1));
        
        if (Math.min(leftCard, rightCard) > mMaxStoredRows) return null;

        Expression expression = operator.getPredicate().getExpression();

        PrimaryExpressionValidator extractor = new PrimaryExpressionValidator();
        Heading left = operator.getChild(0).getHeading();
        Heading right = operator.getChild(1).getHeading();
        boolean primaryExpressionExists = 
            extractor.validate(
                    expression, left.getAttributes(), right.getAttributes());
        
        // this join only applies if the condition has a primary expression
        if (!primaryExpressionExists) return null;
        
        Cost cost = new Cost();
        cost.setReads(leftCard + rightCard);
        String readFirst;
        if (leftCard < rightCard)
        {
            cost.setLookups(rightCard);
            readFirst = "left";
        }
        else
        {
            cost.setLookups(leftCard);
            readFirst = "right";
        }
        
        InMemoryJoinPlan plan = 
            new InMemoryJoinPlan(operator, readFirst, cost.getCost());
        return plan;
    }

}
