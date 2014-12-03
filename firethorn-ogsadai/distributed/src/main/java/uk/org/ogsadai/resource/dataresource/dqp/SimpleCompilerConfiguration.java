package uk.org.ogsadai.resource.dataresource.dqp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.execute.CoordinatorExtension;
import uk.org.ogsadai.dqp.execute.QueryPlanBuilder;
import uk.org.ogsadai.dqp.execute.workflow.ActivityPipelineBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQueryFactory;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;

public class SimpleCompilerConfiguration implements CompilerConfiguration {

    private static final Logger LOG = 
        Logger.getLogger(SimpleCompilerConfiguration.class);
    
    /** Optimiser chain */
    private List<Optimiser> mOptimisers = new ArrayList<Optimiser>();

    /** LQP builder */
    private QueryPlanBuilder mQueryPlanBuilder;

    /** Cardinality estimator -- is this really necessary at the top level? */
    private CardinalityEstimator mCardinalityEstimator;

    private Map<String, Class<? extends Operator>> mFunctionOperators = 
        new HashMap<String, Class<? extends Operator>>();

    /** Coordinator extensions. */
    private List<CoordinatorExtension> mCoordinatorExtensions =
        new ArrayList<CoordinatorExtension>();

    private TableScanQueryFactory mTableScanQueryFactory;

    /** Operator builders. */
    private Map<OperatorID, Map<String, ActivityPipelineBuilder>> mOperatorBuilders = 
        new HashMap<OperatorID, Map<String,ActivityPipelineBuilder>>();

    @Override
    public Class<? extends Operator> getFunctionOperatorClass(
            String functionName) 
    {
        return mFunctionOperators.get(functionName);
    }

    @Override
    public Map<String, ActivityPipelineBuilder> getBuilders(
            OperatorID operatorID) 
    {
        return mOperatorBuilders.get(operatorID);
    }

    @Override
    public List<Optimiser> getOptimisationChain() 
    {
        return mOptimisers;
    }

    @Override
    public CardinalityEstimator getCardinalityEstimator()
    {
        return mCardinalityEstimator;
    }

    @Override
    public TableScanQueryFactory getTableScanQueryFactory() 
    {
        return mTableScanQueryFactory;
    }

    @Override
    public QueryPlanBuilder getQueryPlanBuilder()
    {
        return mQueryPlanBuilder;
    }

    @Override
    public List<CoordinatorExtension> getCoordinatorExtensions()
    {
        return mCoordinatorExtensions;
    }
    
    public void setFunctionOperators(Map<String, Class<? extends Operator>> functions)
        throws CompilerConfigurationException
    {
        mFunctionOperators.clear();
        mFunctionOperators.putAll(functions);
    }
    
    public void setOptimisationChain(List<Optimiser> optimisers)
    {
        mOptimisers.clear();
        mOptimisers.addAll(optimisers);
    }
    
    public void setCardinalityEstimator(CardinalityEstimator cardinalityEstimator)
    {
        mCardinalityEstimator = cardinalityEstimator;
    }

    public void setQueryPlanBuilder(QueryPlanBuilder queryPlanBuilder)
    {
        mQueryPlanBuilder = queryPlanBuilder;
    }
    
    public void setCoordinatorExtensions(
            List<? extends CoordinatorExtension> coordinatorExtensions)
    {
        mCoordinatorExtensions.clear();
        mCoordinatorExtensions.addAll(coordinatorExtensions);
    }
    
    public void setTableScanQueryFactory(TableScanQueryFactory factory)
    {
        mTableScanQueryFactory = factory;
    }
    
    public void setOperatorBuilders(Map<String, Map<String, ActivityPipelineBuilder>> builders)
    {
        mOperatorBuilders.clear();
        for (Entry<String, Map<String, ActivityPipelineBuilder>> entry : builders.entrySet())
        {
            mOperatorBuilders.put(
                    OperatorID.getInstance(entry.getKey()), 
                    entry.getValue());
        }
    }

}
