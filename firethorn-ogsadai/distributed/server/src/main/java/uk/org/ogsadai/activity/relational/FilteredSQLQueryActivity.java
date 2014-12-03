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

package uk.org.ogsadai.activity.relational;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.BaseTreeAdaptor;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.util.ASTUtil;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.tuple.Tuple;

/**
 * An activity that adds additional filters to an SQL query expression and
 * outputs a new SQL query expression.  The output expression when executed
 * does not remove the need for a subsequent join or selection using the 
 * condition in the filter predicate but the output expression may reduce the 
 * number of tuples passed to the join or selection by filtering out some 
 * tuples that will not alter the result of the join or selection.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>expression</code>. Type: {@link java.lang.String}. SQL query 
 * expression to which additional filters (conditions in the where clause) 
 * are added.
 * </li>
 * <li>
 * <code>filterPredicate</code>. Type: {@link java.lang.String}. The predicate
 * than can be used to filter the data.   This must be a single equality
 * predicate where one side is an attribute name that refers to an attribute
 * that can be referred to in the where clause of the expression, and the
 * order side in an attribute that appears in the filterStats input. For 
 * example, <tt>"a.b = x.y"</tt>
 * </li>
 * <li>
 * <code>filterStats</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.  Statistics relating to
 * attribute values that can be used to control filter the data.  The tuple
 * must have three string attributes, the first must contain attribute names,
 * the second must contain minimum attribute values for one bin range, the
 * third must contain maximum attribute values for one bin range.  If the
 * third attribute is null then a single value is being referred to.  If the
 * second attribute is null then this indicates there are no input values.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>. Type: {@link java.lang.String}.  The new SQL
 * query expression with the filters added.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: 
 * </p>
 * <ul>
 * <li>
 * The inputs are read in the following order: <code>expression</code>,
 * <code>filterPredicate</code> and finally <code>filterStats</code>.
 * </li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * <ul>
 * <li>
 * This activity must be targeted at a relational data resource but this
 * implementation does not use the resource.  Other implementations of the
 * same functionality may output resource specific queries so it is useful to
 * include the resource in the contract for this activity.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Takes the SQL query expression and statistics describing bound values for
 * it and outputs a new SQL query expression adds clauses relating to the
 * bound values.  For example, if the input expression is<br>
 * <tt>SELECT * FROM myTable</tt><br>
 * the filter predicate is<br>
 * <tt>myTable.x = otherTable.y</tt>
 * and the statistics contains two absolute values (2 and 4) for attribute 
 * <tt>otherTable.y</tt> then the output expression will be:
 * <tt>SELECT * FROM myTable WHERE myTable.x = 2 OR myTable.x = 4</tt><br>
 * </li>
 * <li>
 * The following operators are supported in the filter predicate: 
 * =, <, >, <=, >=, AND, OR.
 * </li>
 * </ul> 
 *
 * @author The OGSA-DAI Project Team
 */
public class FilteredSQLQueryActivity
    extends MatchedIterativeActivity 
    implements ResourceActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(FilteredSQLQueryActivity.class);

    /** Activity input name - expression. */
    public static final String INPUT_EXPRESSION = "expression";
    
    /** Activity input name - filterPredicates. */
    public static final String INPUT_FILTER_PREDICATES = "filterPredicates";

    /** Activity input name - filterStats . */
    public static final String INPUT_FILTER_STATS = "filterStats";

    /** Activity output name - new expression. */
    public static final String OUTPUT_RESULT = "result";

    /**
     * Constructor.
     */
    public FilteredSQLQueryActivity()
    {
        super();
    }
    
    /**
     * {@inheritDoc}
     */
    public Class getTargetResourceAccessorClass()
    {
        return JDBCConnectionProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor)
    {
        // We don't need this in this implementation
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new TypedActivityInput(INPUT_EXPRESSION, String.class),
            new TypedActivityInput(INPUT_FILTER_PREDICATES, String.class),
            new TupleListActivityInput(INPUT_FILTER_STATS)};
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        String expression = (String) iterationData[0];
        String filterPredicate = (String) iterationData[1];
        TupleListIterator statsTuples = (TupleListIterator) iterationData[2];

        expression = buildExpression(expression, filterPredicate, statsTuples);
        
        try
        {
            getOutput().write(expression);
        }
        catch (PipeClosedException e)
        {
            // no more output wanted so we can finish processing
            iterativeStageComplete();
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        //no post-processing
    }

    /**
     * Builds the new expression.
     * 
     * @param expression
     *   expression to add filters to
     * @param filterPredicate
     *   filter predicate
     * @param statsTuples
     *   statistics tuples
     *   
     * @return the new filtered expression
     * 
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    protected static String buildExpression(
        String expression, 
        String filterPredicate, 
        TupleListIterator statsTuples) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        try
        {
            /** Attribute statistics mapped from attribute name. */
            Map<String, AttributeStatistics> attributeStatistics = 
                new HashMap<String, AttributeStatistics>();

            Tuple tuple;
            while ((tuple = (Tuple)statsTuples.nextValue()) != null)
            {
                String attrName = tuple.getString(0);
                String min = tuple.getString(1);
                String max = tuple.getString(2);

                if (!attributeStatistics.containsKey(attrName))
                {
                    attributeStatistics.put(attrName, new AttributeStatistics());
                }
                attributeStatistics.get(attrName).addData(min, max);
            }

            SQLQueryParser queryParser = SQLQueryParser.getInstance();
            CommonTree queryNode = queryParser.parseSQLForQuery(expression);
            CommonTree whereNode = getWhereNode(queryNode);
            CommonTree newCondition = 
                buildCondition(filterPredicate, attributeStatistics);
            addConditionToWhereNode(
                whereNode, 
                newCondition);

            return queryParser.generateSQLForQuery(queryNode);
        }
        catch (SQLParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the where node to the given query node.  If the node has no where
     * node a new one is added with no children.
     * 
     * @param queryNode query node
     * 
     * @return the where node of the query.
     */
    protected static CommonTree getWhereNode(CommonTree queryNode)
    {
        // Look at children for a where node
        for (int i=0; i<queryNode.getChildCount(); ++i)
        {
            CommonTree child = (CommonTree) queryNode.getChild(i);
            if (ASTUtil.isWhereNode(child))
            {
                return child;
            }
        }
        
        // Have not found the where node, we need to add one.  This assumes
        // we can add a where node at the end.
        CommonTree whereNode = ASTUtil.getWhereNode();
        queryNode.addChild(whereNode);
        return whereNode;
    }

    /**
     * Adds a condition to a where node.  If the where node already contains
     * a condition the new condition is ANDed to the existing condition.
     * 
     * @param whereNode where node 
     * @param condition condition
     */
    protected static void addConditionToWhereNode(
        CommonTree whereNode, CommonTree condition)
    {
        if (whereNode.getChildCount() == 0)
        {
            whereNode.addChild(condition);
        }
        else
        {
            CommonTree child = (CommonTree) whereNode.getChild(0);
            CommonTree andNode = ASTUtil.getAndNode();
            andNode.addChild(child);
            andNode.addChild(condition);
            whereNode.deleteChild(0);
            whereNode.addChild(andNode);
        }
    }

    /**
     * Builds a condition from the filter predicate and values.
     * 
     * @param filterPredicate
     *            filter predicate
     * @param attributeStatistics
     *            attribute statistics
     * 
     * @return the new filtered expression
     * 
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     * @throws SQLParserException
     */
    protected static CommonTree buildCondition(
        String filterPredicate,
        Map<String, AttributeStatistics> attributeStatistics) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException, SQLParserException
    {
        SQLQueryParser queryParser = SQLQueryParser.getInstance();
        CommonTree conditionNode = queryParser.parseSQLForCondition(
            filterPredicate);

        // Process each operator
        return processConditionOperator(conditionNode, attributeStatistics);
    }
    
    /**
     * Processes a condition operator to apply any binding.
     * 
     * @param conditionNode  root node of the condition to apply the binding to
     * 
     * @return new bound condition node
     * 
     */
    protected static CommonTree processConditionOperator(
        CommonTree conditionNode,
        Map<String, AttributeStatistics> attributeStatistics)
    {
        if (conditionNode.getText().equals("="))
        {
            return processEqualsOperator(conditionNode, attributeStatistics);
        }
        else if (conditionNode.getText().equals("<"))
        {
            return processLessThanOperator(
                conditionNode, attributeStatistics, false);
        }
        else if (conditionNode.getText().equals(">"))
        {
            return processGreaterThanOperator(
                conditionNode, attributeStatistics, false);
        }
        else if (conditionNode.getText().equals(">="))
        {
            return processGreaterThanOperator(
                conditionNode, attributeStatistics, true);
        }
        else if (conditionNode.getText().equals("<="))
        {
            return processLessThanOperator(
                conditionNode, attributeStatistics, true);
        }
        else if (ASTUtil.isAndNode(conditionNode))
        {
            return processAndOperator(
                conditionNode, attributeStatistics);
        }
        else if (ASTUtil.isOrNode(conditionNode))
        {
            return processOrOperator(
                conditionNode, attributeStatistics);
        }
        else
        {
            throw new RuntimeException(
                "Don't support operator: " + conditionNode.getText());
        }
    }
    
    /**
     * Processes an AND operator.
     * 
     * @param andNode 
     *   AND node
     * 
     * @param attributeStatistics
     *   statistics relating to the attributes to be bound
     *   
     * @return an AND node with the children bound.
     */
    protected static CommonTree processAndOperator(
        CommonTree andNode,
        Map<String, AttributeStatistics> attributeStatistics)
    {
        CommonTree lhsChild = processConditionOperator(
            (CommonTree) andNode.getChild(0), attributeStatistics);
        CommonTree rhsChild = processConditionOperator(
            (CommonTree) andNode.getChild(1), attributeStatistics);
        
        return ASTUtil.getAndNode(lhsChild, rhsChild);
    }
    
    /**
     * Processes an OR operator.
     * 
     * @param orNode 
     *   OR node
     * 
     * @param attributeStatistics
     *   statistics relating to the attributes to be bound
     *   
     * @return an OR node with the children bound.
     */
    protected static CommonTree processOrOperator(
        CommonTree orNode,
        Map<String, AttributeStatistics> attributeStatistics)
    {
        CommonTree lhsChild = processConditionOperator(
            (CommonTree) orNode.getChild(0), attributeStatistics);
        CommonTree rhsChild = processConditionOperator(
            (CommonTree) orNode.getChild(1), attributeStatistics);
        
        return ASTUtil.getOrNode(lhsChild, rhsChild);
    }
    
    /**
     * Processes an equals (=) operator.
     * 
     * @param equalsNode 
     *   equals node
     * 
     * @param attributeStatistics
     *   statistics relating to the attributes to be bound
     *   
     * @return a new bound node possibly the root of a complex tree build using
     *         using AND, OR and equals operators.
     */
    protected static CommonTree processEqualsOperator(
        CommonTree equalsNode,
        Map<String, AttributeStatistics> attributeStatistics)
    {
        int substituteIndex = 0;
        AttributeStatistics stats = null;
        
        // Find which side of the operator is to be substituted
        for (int i=0; i<2; ++i)
        {
            substituteIndex = i;
            CommonTree substituteNode = (CommonTree) equalsNode.getChild(i);
            String columnName = ASTUtil.getTableColumnName(substituteNode);
            
            stats = attributeStatistics.get(columnName);
            if (stats != null)
            {
                break;
            }
        }
        int fixedIndex = substituteIndex == 0 ? 1 : 0; 
        
        // If condition does not refer to something we have statistics for
        // then leave the condition unaltered
        if (stats == null)
        {
            return equalsNode;
        }
        
        List<String> values = stats.getValues();
        List<AttributeValueRange> ranges = stats.getRanges();

        if( values.size() == 0 && ranges.size() == 0)
        {
            // No values so we have an empty condition
            return ASTUtil.getEqualsNode(
                ASTUtil.getLiteralNode("0"),
                ASTUtil.getLiteralNode("1"));
        }
        else
        {
            BaseTreeAdaptor adaptor = new CommonTreeAdaptor();
            List<CommonTree> newNodes = new LinkedList<CommonTree>();
            
            for(  String value : values)
            {
                CommonTree newEqualsNode = ASTUtil.getEqualsNode(
                    ASTUtil.getLiteralNode(value),
                    (CommonTree) 
                        adaptor.dupTree(equalsNode.getChild(fixedIndex)));
                
                newNodes.add(newEqualsNode);
            }
            
            for (AttributeValueRange range : ranges)
            {
                CommonTree node1 = ASTUtil.getGreaterThanEqualsNode(
                    (CommonTree) 
                        adaptor.dupTree(equalsNode.getChild(fixedIndex)),
                    ASTUtil.getLiteralNode(range.getMin()));

                CommonTree node2 = ASTUtil.getLessThanEqualsNode(
                    (CommonTree) 
                        adaptor.dupTree(equalsNode.getChild(fixedIndex)),
                    ASTUtil.getLiteralNode(range.getMax()));
                CommonTree andNode = ASTUtil.getAndNode(node1, node2);
                newNodes.add(andNode);
            }
            
            // Now we need to put all of these nodes into a big chain of
            // OR
            CommonTree resultNode = null;
            
            for (CommonTree node : newNodes)
            {
                if (resultNode == null)
                {
                    resultNode = node;
                }
                else
                {
                    resultNode = ASTUtil.getOrNode(
                        resultNode, 
                        node);
                }
            }
            
            return resultNode;
        }
    }
        
    /**
     * Processes a less than (<), or less than or equals (<=) operator.
     * 
     * @param lessThanNode 
     *   less than node
     * 
     * @param attributeStatistics
     *   statistics relating to the attributes to be bound
     *   
     * @param includeEquals
     *   <tt>true</tt> if the operator is less than or equals, <tt>false</tt>
     *   if it is less than.
     *   
     * @return a new bound node.
     */
    protected static CommonTree processLessThanOperator(
        CommonTree lessThanNode,
        Map<String, AttributeStatistics> attributeStatistics,
        boolean includeEquals)
    {
        int substituteIndex = 0;
        AttributeStatistics stats = null;
        
        // Find which side of the operator is to be substituted
        for (int i=0; i<2; ++i)
        {
            substituteIndex = i;
            CommonTree substituteNode = (CommonTree) lessThanNode.getChild(i);
            String columnName = ASTUtil.getTableColumnName(substituteNode);

            stats = attributeStatistics.get(columnName);
            if (stats != null)
            {
                break;
            }
        }
        int fixedIndex = substituteIndex == 0 ? 1 : 0;
        
        // If condition does not refer to something we have statistics for
        // then leave the condition unaltered
        if (stats == null)
        {
            return lessThanNode;
        }
    
        if (fixedIndex == 0)
        {
            return processLessThanOperator(
                (CommonTree) lessThanNode.getChild(fixedIndex), 
                stats,
                includeEquals);
        }
        else
        {
            return processGreaterThanOperator(
                (CommonTree) lessThanNode.getChild(fixedIndex), 
                stats,
                includeEquals);
        }
    }
    
    /**
     * Processes a greater than (>), or greater than or equals (>=) operator.
     * 
     * @param greaterThanNode 
     *   greater than node
     * 
     * @param attributeStatistics
     *   statistics relating to the attributes to be bound
     *   
     * @param includeEquals
     *   <tt>true</tt> if the operator is greater than or equals, <tt>false</tt>
     *   if it is greater than.
     *   
     * @return a new bound node.
     */
    protected static CommonTree processGreaterThanOperator(
        CommonTree greaterThanNode,
        Map<String, AttributeStatistics> attributeStatistics,
        boolean includeEquals)
    {
        int substituteIndex = 0;
        AttributeStatistics stats = null;
        
        // Find which side of the operator is to be substituted
        for (int i=0; i<2; ++i)
        {
            substituteIndex = i;
            CommonTree substituteNode = (CommonTree) greaterThanNode.getChild(i);
            String columnName = ASTUtil.getTableColumnName(substituteNode);

            stats = attributeStatistics.get(columnName);
            if (stats != null)
            {
                break;
            }
        }
        int fixedIndex = substituteIndex == 0 ? 1 : 0;
        
        // If condition does not refer to something we have statistics for
        // then leave the condition unaltered
        if (stats == null)
        {
            return greaterThanNode;
        }
    
        if (fixedIndex == 0)
        {
            return processGreaterThanOperator(
                (CommonTree) greaterThanNode.getChild(fixedIndex), 
                stats,
                includeEquals);
        }
        else
        {
            return processLessThanOperator(
                (CommonTree) greaterThanNode.getChild(fixedIndex), 
                stats,
                includeEquals);
        }
    }
            
    /**
     * Processes a less than or, less than or equals operator.
     * 
     * @param fixedLHSOperand
     *    fixed left hand side operand
     *    
     * @param statistics
     *    statistics relating to the attribute to be bound
     *    
     * @param includeEquals
     *   <tt>true</tt> if the operator is less than or equals, <tt>false</tt>
     *   if it is less than.

     * @return a new bound node.
     */
    protected static CommonTree processLessThanOperator(
        CommonTree fixedLHSOperand,
        AttributeStatistics statistics,
        boolean includeEquals)
    {
        List<String> values = statistics.getValues();
        List<AttributeValueRange> ranges = statistics.getRanges();

        if( values.size() == 0 && ranges.size() == 0)
        {
            // No values so we have an empty condition
            return ASTUtil.getEqualsNode(
                ASTUtil.getLiteralNode("0"),
                ASTUtil.getLiteralNode("1"));
        }
        else
        {
            String max;
            
            if (values.size() != 0)
            {
                max = values.get(values.size()-1);
            }
            else
            {
                max = ranges.get(ranges.size()-1).getMax();
            }
            
            if (includeEquals)
            {
                return ASTUtil.getLessThanEqualsNode(
                    fixedLHSOperand,
                    ASTUtil.getLiteralNode(max));
            }
            else
            {
                return ASTUtil.getLessThanNode(
                    fixedLHSOperand,
                    ASTUtil.getLiteralNode(max));
            }
        }          
    }        
    
    /**
     * Processes a greater than or, greater than or equals operator.
     * 
     * @param fixedLHSOperand
     *    fixed left hand side operand
     *    
     * @param statistics
     *    statistics relating to the attribute to be bound
     *    
     * @param includeEquals
     *   <tt>true</tt> if the operator is greater than or equals, <tt>false</tt>
     *   if it is greater than.

     * @return a new bound node.
     */
    protected static CommonTree processGreaterThanOperator(
        CommonTree fixedLHSOperand,
        AttributeStatistics statistics,
        boolean includeEquals)
    {
        List<String> values = statistics.getValues();
        List<AttributeValueRange> ranges = statistics.getRanges();

        if( values.size() == 0 && ranges.size() == 0)
        {
            // No values so we have an empty condition
            return ASTUtil.getEqualsNode(
                ASTUtil.getLiteralNode("0"),
                ASTUtil.getLiteralNode("1"));
        }
        else
        {
            String min;
            
            if (values.size() != 0)
            {
                min = values.get(0);
            }
            else
            {
                min = ranges.get(0).getMin();
            }
            
            if (includeEquals)
            {
                return ASTUtil.getGreaterThanEqualsNode(
                    fixedLHSOperand,
                    ASTUtil.getLiteralNode(min));
            }
            else
            {
                return ASTUtil.getGreaterThanNode(
                    fixedLHSOperand,
                    ASTUtil.getLiteralNode(min));
            }
        }
    }


   /**
     * Gets the attribute names from the filter predicates.  This method 
     * assumes the filter predicate is a single equality such as 
     * <tt>"x.y = a.c"</tt>
     * 
     * @param filterPredicate  filter predicate
     * 
     * @return array of the two predicate names.
     */
    protected static  String[] getAttributeNames(String filterPredicate)
    {
        String[] result = filterPredicate.split("=");
        
        for (int i=0; i<result.length; ++i)
        {
            result[i] = result[i].trim();
        }
        
        return result;
    }

}
