// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.execute;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;

import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.LQPBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Builds query plans for queries expressed in DQP's SQL grammar.
 *
 * @author The OGSA-DAI Project Team
 */
public class SQLQueryPlanBuilder implements QueryPlanBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SQLQueryPlanBuilder.class);


    /**
     * {@inheritDoc}
     */
    public Operator buildQueryPlan(
        String query, 
        DQPResourceAccessor dqpResourceAccessor,
        RequestDetails requestDetails) 
        throws ActivityUserException
    {
        // get function repository from the context 
        FunctionRepository functionRepository = 
            (FunctionRepository) OGSADAIContext.getInstance().get(
                FunctionRepository.FUNCTION_REPOSITORY_KEY);

        CommonTree ast = parseSQL(query);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Parsed query");
            LOG.debug((new DOTTreeGenerator()).toDOT(ast).toString());
        }
        return buildValidatedLQP(
            ast, dqpResourceAccessor, requestDetails, functionRepository);
    }
    
    
    /**
     * Parses an SQL query and returns the abstract syntax tree.
     * 
     * @param sql
     *            SQL expression
     * @return abstract syntax tree
     * @throws ActivityUserException
     *             if parsing failed
     */
    private CommonTree parseSQL(String sql) throws ActivityUserException
    {
        try
        {
            return SQLQueryParser.getInstance().parseSQL(sql);
        }
        catch (SQLParserException e)
        {
            throw new ActivityUserException(e);
        }

    }
    
    /**
     * Builds and validates a logical query plan from a given abstract syntax
     * tree.
     * 
     * @param ast
     *            abstract syntax tree
     * @param dqpResourceAccessor 
     *            DQP resource accessor giving access to the resource's 
     *            configuration.
     * @param requestDetails 
     *             details of the user's request
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     *             
     * @return validated logical query plan
     * @throws ActivityUserException
     */
    private Operator buildValidatedLQP(
        CommonTree ast, 
        DQPResourceAccessor dqpResourceAccessor,
        RequestDetails requestDetails,
        FunctionRepository functionRepository)
        throws ActivityUserException
    {
        try
        {
            LQPBuilder builder = new LQPBuilder(
                dqpResourceAccessor.getFederation().getDataDictionary(
                    requestDetails),
                dqpResourceAccessor.getCompilerConfiguration());

            Operator root = builder.buildLQP(ast, functionRepository);
            root.validate();
            return root;
        }
        catch (LQPException e)
        {
            throw new ActivityUserException(e);
        }
    }
}
