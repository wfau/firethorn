package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class JoinImplementationOptimiser implements Optimiser 
{
    
    public static final DAILogger LOG = 
        DAILogger.getLogger(JoinImplementationOptimiser.class);
    
    private List<JoinImplementation> mJoinImpls =
        new LinkedList<JoinImplementation>();

    private RequestDQPFederation mRequestFederation;

    @Override
    public Operator optimise(
            Operator lqpRoot,
            RequestDQPFederation requestFederation,
            CompilerConfiguration compilerConfiguration,
            RequestDetails requestDetails)
    throws LQPException 
    {
        mRequestFederation = requestFederation;
        walkLQP(lqpRoot);
        return lqpRoot;
    }
    
    public void setJoinImplementations(List<JoinImplementation> joinImplementations)
    {
        mJoinImpls.clear();
        mJoinImpls.addAll(joinImplementations);
    }

    private void walkLQP(Operator operator) throws LQPException 
    {
        for (int i=0; i<operator.getChildCount(); i++)
        {
            walkLQP(operator.getChild(i));
        }
        processOperator(operator);
    }

    private void processOperator(Operator operator) throws LQPException 
    {
        if (operator.getID() == OperatorID.INNER_THETA_JOIN)
        {
            processInnerThetaJoin((InnerThetaJoinOperator)operator);
        }
        // TODO handle products and other joins
        else if (operator.getID() == OperatorID.LEFT_OUTER_JOIN || 
                operator.getID() == OperatorID.FULL_OUTER_JOIN ||
                operator.getID() == OperatorID.ANTI_SEMI_JOIN ||
                operator.getID() == OperatorID.PRODUCT ||
                operator.getID() == OperatorID.RIGHT_OUTER_JOIN ||
                operator.getID() == OperatorID.SEMI_JOIN)
        {
            processDefault(operator);
        }
    }

    private void processInnerThetaJoin(InnerThetaJoinOperator operator)
        throws LQPException 
    {
        List<JoinPlan> options = new LinkedList<JoinPlan>();
        for (JoinImplementation joinImpl : mJoinImpls)
        {
            LOG.debug("Processing option " + joinImpl.getClass().getName());
            JoinPlan option = joinImpl.process(mRequestFederation, operator);
            if (option != null)
            {
                options.add(option);
            }
        }
        
        JoinPlan choice = getBestOption(options);
        LOG.debug("Best option: " + choice);
        if (choice != null)
        {
            choice.apply();
        }
    }

    private JoinPlan getBestOption(List<JoinPlan> options)
    {
        JoinPlan bestOption = null;
        double bestCost = Double.MAX_VALUE;
        for (JoinPlan option : options)
        {
            double cost = option.getCost();
            LOG.debug("Possible option: " + option + ", cost = " + cost);
            if (cost < bestCost)
            {
                bestCost = cost;
                bestOption = option;
            }
        }
        return bestOption;
    }
    
    private void processDefault(Operator operator)
    {
        // TODO choose the best join plan for semi join, anti join, outer join.
        LOG.debug("Default implementation for " + operator.getID());
    }

}
