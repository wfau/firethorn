// Copyright (c) The University of Edinburgh, 2008.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.common;

import java.util.List;
import java.util.Map;

import uk.org.ogsadai.dqp.execute.CoordinatorExtension;
import uk.org.ogsadai.dqp.execute.QueryPlanBuilder;
import uk.org.ogsadai.dqp.execute.workflow.ActivityPipelineBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQueryFactory;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;

/**
 * Configuration for the query compiler.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface CompilerConfiguration
{
    /** Default builder key. */
    public static final String DEFAULT_BUILDER = "DEFAULT";

    /**
     * Returns the operator class corresponding to a function name.
     * 
     * @param functionName
     *            function name
     * @return class of the operator, or <code>null</code> if no operator is
     *         registered for this function name.
     */
    public Class<? extends Operator> getFunctionOperatorClass(
        String functionName);
    
    /**
     * Gets the activity pipeline builders corresponding to an operator ID.
     * An operator ID may have one or more activity pipeline builders associated
     * with it.  There must be a default activity pipeline builder.  This
     * default builder can be accessed from the map returned using the string
     * <tt>"DEFAULT"</tt> as the key.  Alternative activity pipeline builders
     * map be accessed using other keys.  
     * 
     * @param operatorID
     *            operator ID
     * @return a mapping from a string to an activity pipeline builder.  The
     *         mapping will have an entry with the key <tt>"DEFAULT"</tt> and
     *         may also have alternative activity pipeline builders with other
     *         keys.
     */
    public Map<String, ActivityPipelineBuilder> getBuilders(
        OperatorID operatorID);
    
    /**
     * Returns an optimisation chain - an ordered list of Optimiser objects
     * that will be used to transform LQP.
     * 
     * @return a list of optimiser objects
     */
    public List<Optimiser> getOptimisationChain();
    
    /**
     * Returns cardinality estimator object.
     * 
     * @return cardinality estimator
     */
    public CardinalityEstimator getCardinalityEstimator();
    
    /**
     * Returns the table scan query factory.  This is a factory that is used
     * to create that implement the <code>TableScanQuery</code> interface.  
     * These objects represent the query that will be sent to a database to 
     * implement a table scan operation.  Some advanced optimisers will need 
     * more functionality for this table scan query object so the having a
     * factory supports configuration of a factory that provides the desired
     * concrete implementation.
     * 
     * @return table scan query factory.
     */
    public TableScanQueryFactory getTableScanQueryFactory();
    
    /**
     * Returns the query plan builder that will be used to build query plans
     * corresponding to user queries.
     * 
     * @return query plan builder
     */
    public QueryPlanBuilder getQueryPlanBuilder();
    
    /**
     * Returns a list of coordinator extension objects or the empty list if none
     * were defined.
     * 
     * @return a list of coordinator extension objects
     */
    public List<CoordinatorExtension> getCoordinatorExtensions();

}
