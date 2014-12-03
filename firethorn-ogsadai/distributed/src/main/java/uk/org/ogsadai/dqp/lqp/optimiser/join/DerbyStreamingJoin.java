package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class DerbyStreamingJoin implements JoinImplementation
{

    @Override
    public JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator joinOperator)
    {
        DerbyStreamingJoinPlan plan = new DerbyStreamingJoinPlan(joinOperator);
        
        DataNode dataNode = Annotation.getDataNodeAnnotation(joinOperator);
        Expression joinCondition = joinOperator.getPredicate().getExpression();
        if (!dataNode.supportsExpression(joinCondition))
        {
            return null;
        }

        double leftCard = 
            Annotation.getCardinalityAnnotation(joinOperator.getChild(0));
        double rightCard = 
            Annotation.getCardinalityAnnotation(joinOperator.getChild(1));
        Cost cost = new Cost();
        double streamedCard, storedCard;
        if (leftCard > rightCard)
        {
            streamedCard = leftCard;
            storedCard = rightCard;
            plan.setReadFirst("data2");
        }
        else
        {
            streamedCard = rightCard;
            storedCard = leftCard;
            plan.setReadFirst("data1");
        }
        cost.setReads(streamedCard + storedCard);
        cost.setQueries(streamedCard);
        cost.setMaterialise(storedCard);
        
        plan.setCost(cost.getCost());
        return plan;
    }

}
