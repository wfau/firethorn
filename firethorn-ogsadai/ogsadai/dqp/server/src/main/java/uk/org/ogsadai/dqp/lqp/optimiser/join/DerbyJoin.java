package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class DerbyJoin implements JoinImplementation
{

    @Override
    public JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator joinOperator)
    {
        DerbyJoinPlan plan = new DerbyJoinPlan(joinOperator);

        double joinCard = Annotation.getCardinalityAnnotation(joinOperator);
        double leftCard = 
            Annotation.getCardinalityAnnotation(joinOperator.getChild(0));
        double rightCard = 
            Annotation.getCardinalityAnnotation(joinOperator.getChild(1));
        Cost cost = new Cost();
        // reading inputs in parallel 
        // plus reading results from internal Derby query
        cost.setReads(Math.max(leftCard, rightCard) + joinCard);
        // writing both inputs to the database
        cost.setMaterialise(leftCard + rightCard);
        // executing the internal query? 
        
        plan.setCost(cost.getCost());
        return plan;
    }

}
