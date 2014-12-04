package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class CardinalityEstimatorOptimiser implements Optimiser
{
    private CardinalityEstimator mCardinalityEstimator;

    public CardinalityEstimatorOptimiser()
    {
    }
    
    @Override
    public Operator optimise(Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) throws LQPException
    {
        mCardinalityEstimator.setDataDictionary(
                requestFederation.getDataDictionary());
        
        QueryPlanLeftChildrenFirstWalk walker = 
            new QueryPlanLeftChildrenFirstWalk(mCardinalityEstimator);

        walker.walk(lqpRoot);
        
        return lqpRoot;
    }
    
    public void setCardinalityEstimator(CardinalityEstimator cardinalityEstimator)
    {
        mCardinalityEstimator = cardinalityEstimator;
    }
   
}
