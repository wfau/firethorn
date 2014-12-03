package uk.org.ogsadai.dqp.lqp.optimiser.join;

import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

public interface JoinPlan
{
    
    double getCost();
    
    void apply() throws LQPException;

}
