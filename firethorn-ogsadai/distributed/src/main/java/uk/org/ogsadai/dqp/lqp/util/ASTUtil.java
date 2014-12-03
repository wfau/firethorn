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

package uk.org.ogsadai.dqp.lqp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.BaseTreeAdaptor;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.BindingPredicate;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.LQPBuilder;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.exceptions.UnsupportedTokenException;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQL92QueryParser;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import antlr.Token;

/**
 * Utility class for processing abstract syntax trees.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ASTUtil
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * List of supported non-terminals that can appear on left or right of an 
     * OR expression.
     */
    private static final ArrayList<String> SUPPORTED_OR_NON_TERMINALS;

    /** 
     * List of supported terminals that can appear on left or right of an 
     * OR expression.
     */
    private static final ArrayList<String> SUPPORTED_OR_TERMINALS;
    
    /**
     * Initialization. At the moment comparison operators and AND operator
     * are supported in an OR expression.
     */
    static 
    {
        SUPPORTED_OR_NON_TERMINALS = new ArrayList<String>();
        SUPPORTED_OR_NON_TERMINALS.add("AND");
        SUPPORTED_OR_NON_TERMINALS.add("OR");

        SUPPORTED_OR_TERMINALS = new ArrayList<String>();
        SUPPORTED_OR_TERMINALS.add("=");
        SUPPORTED_OR_TERMINALS.add(">");
        SUPPORTED_OR_TERMINALS.add(">=");
        SUPPORTED_OR_TERMINALS.add("<");
        SUPPORTED_OR_TERMINALS.add("<=");
        SUPPORTED_OR_TERMINALS.add("!=");
        SUPPORTED_OR_TERMINALS.add("<>");
    }

    // These Token objects below as used just now in place of constants for
    // these tokens.  When we have constants in ASTConstants for these tokens
    // then the constants should be used instead.

    /** ALL token. */
    private static CommonToken mAllToken = null;
    /** ANY token. */
    private static CommonToken mAnyToken = null;
    /** AND token. */
    private static CommonToken mAndToken = null;
    /** OR token. */
    private static CommonToken mOrToken = null;
    /** = token. */
    private static CommonToken mEqualsToken = null;
    /** != token. */
    private static CommonToken mNotEqualsToken = null;
    /** >= token. */
    private static CommonToken mGreaterThanEqualsToken = null;
    /** > token. */
    private static CommonToken mGreaterThanToken = null;
    /** < token. */
    private static CommonToken mLessThanEqualsToken = null;
    /** <= token */
    private static CommonToken mLessThanToken = null;
    /** literal token */
    private static CommonToken mLiteralToken = null;
    
    /**
     * Gets the fully qualified column name from a TABLECOLUMN node.
     * 
     * @param tableColumnNode TABLECOLUMN node
     * 
     * @return the table name with dots to separate the components of the
     *         name if there are multiple components, for example 
     *         <tt>"a.b"</tt>.
     */
    public static String getTableColumnName(CommonTree tableColumnNode)
    {
        if (tableColumnNode.getText().equals("TABLECOLUMN"))
        {
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<tableColumnNode.getChildCount(); ++i)
            {
                if (i>0)
                {
                    sb.append(".");
                }
                sb.append(tableColumnNode.getChild(i).getText());
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Creates attribute from a TABLECOLUMN node.
     * 
     * @param tableColumnNode
     * @return attribute
     */
    public static Attribute getAttribute(CommonTree tableColumnNode)
    {
        return new AttributeImpl(getTableColumnName(tableColumnNode));
    }

    /**
     * Creates a new literal node containing the given text.
     * 
     * @param text text the literal node should contain
     * 
     * @return new literal node
     */
    public static synchronized CommonTree getLiteralNode(String text)
    {
        if (mLiteralToken == null)
        {
            String condition = "x=2";
            try
            {
                CommonTree literal = (CommonTree) SQLQueryParser.getInstance().
                    parseSQLForCondition(condition).getChild(1);
               
                mLiteralToken = (CommonToken) literal.getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
            }
        }
        return new CommonTree(new CommonToken(mLiteralToken.getType(), text));
    }

    /**
     * Creates a new ALL node.
     * 
     * @param child
     *            ALL node child
     * @return new ALL node
     */
    public static synchronized CommonTree getAllNode(CommonTree child)
    {
        if (mAllToken == null)
        {
            String condition = "a > ALL (SELECT * FROM t)";
            try
            {
                mAllToken = (CommonToken) SQLQueryParser.getInstance()
                        .parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mAllToken);
        result.addChild(child);
        return result;
    }

    /**
     * Creates a new ANY node.
     * 
     * @param child
     *            ANY node child
     * @return new ANY node
     */
    public static synchronized CommonTree getAnyNode(CommonTree child)
    {
        if (mAnyToken == null)
        {
            String condition = "a > ANY (SELECT * FROM t)";
            try
            {
                mAnyToken = (CommonToken) SQLQueryParser.getInstance()
                        .parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mAnyToken);
        result.addChild(child);
        return result;
    }

    /**
     * Gets a new greater than or equals (>=) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new greater than or equals node containing the given children.
     */
    public static synchronized CommonTree getGreaterThanEqualsNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mGreaterThanEqualsToken == null)
        {
            String condition = "x>=y";
            try
            {
                mGreaterThanEqualsToken = (CommonToken) SQLQueryParser.
                    getInstance().parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mGreaterThanEqualsToken);
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }

    /**
     * Gets a new greater than (>) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new greater than node containing the given children.
     */
    public static synchronized CommonTree getGreaterThanNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mGreaterThanToken == null)
        {
            String condition = "x>y";
            try
            {
                mGreaterThanToken = (CommonToken) SQLQueryParser.
                    getInstance().parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mGreaterThanToken);
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }
    
    /**
     * Gets a new less than or equals (<=) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new less than or equals node containing the given children.
     */
    public static synchronized CommonTree getLessThanEqualsNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mLessThanEqualsToken == null)
        {
            String condition = "x<=y";
            try
            {
                mLessThanEqualsToken = (CommonToken) SQLQueryParser.
                    getInstance().parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mLessThanEqualsToken);
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }

    /**
     * Gets a new less than (<) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new less than node containing the given children.
     */
    public static synchronized CommonTree getLessThanNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mLessThanToken == null)
        {
            String condition = "x<y";
            try
            {
                mLessThanToken = (CommonToken) SQLQueryParser.
                    getInstance().parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mLessThanToken);
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }    
    
    /**
     * Gets a new equals (=) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new equals node containing the given children.
     */
    public static synchronized CommonTree getEqualsNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mEqualsToken == null)
        {
            String condition = "x=y";
            try
            {
                CommonToken token = (CommonToken) SQLQueryParser.getInstance().
                    parseSQLForCondition(condition).getToken();
                mEqualsToken = new CommonToken(token.getType(), token.getText());
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(new CommonToken(mEqualsToken.getType(), mEqualsToken.getText()));
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }

    /**
     * Gets a new not equals (!=) node.
     * 
     * @param lhsChild left hand side child
     * @param rhsChild right hand side child
     * 
     * @return new equals node containing the given children.
     */
    public static synchronized CommonTree getNotEqualsNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        if (mNotEqualsToken == null)
        {
            String condition = "x!=y";
            try
            {
                mNotEqualsToken = (CommonToken) SQLQueryParser.getInstance().
                    parseSQLForCondition(condition).getToken();
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        CommonTree result = new CommonTree(mNotEqualsToken);
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }
    
    /**
     * Gets a new AND node with no children.
     * 
     * @return a new AND node.  
     */
    public static synchronized CommonTree getAndNode()
    {
        if (mAndToken == null)
        {
            String condition = "x=y AND a=b";
            try
            {
                CommonToken token = 
                    (CommonToken) SQLQueryParser.getInstance().parseSQLForCondition(condition).getToken();
                
                mAndToken = new CommonToken(token.getType(), token.getText());
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        return new CommonTree(mAndToken);
    }
    
    /**
     * Creates a new AND node.
     * 
     * @param lhsChild left hand size child
     * @param rhsChild right hand size child
     * 
     * @return a new AND node with the specified children.
     */
    public static CommonTree getAndNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        CommonTree result = getAndNode();
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }

    /**
     * Gets a new OR node with no children.
     * 
     * @return a new OR node, caller should add two children.
     */
    public static synchronized CommonTree getOrNode()
    {
        if (mOrToken == null)
        {
            String condition = "x=y OR a=b";
            try
            {
                CommonToken token = (CommonToken) SQLQueryParser.getInstance().
                    parseSQLForCondition(condition).getToken();
                mOrToken = new CommonToken(token.getType(), token.getText());
            }
            catch (SQLParserException e)
            {
                // Should not occur
                throw new RuntimeException(
                    "Failed to parse " + condition + " : should not happen", e);
            }
        }
        return new CommonTree(new CommonToken(mOrToken.getType(), mOrToken.getText()));
    }
    
    /**
     * Gets a new OR node.
     * 
     * @param lhsChild left hand size child
     * @param rhsChild right hand size child
     * 
     * @return a new OR node with the specified children.
     */
    public static CommonTree getOrNode(
        CommonTree lhsChild,
        CommonTree rhsChild)
    {
        CommonTree result = getOrNode();
        result.addChild(lhsChild);
        result.addChild(rhsChild);
        return result;
    }

    /**
     * Gets a new where node with no children.
     * 
     * @return a new where node.
     */
    public static CommonTree getWhereNode()
    {
        return new CommonTree(
            new CommonToken(SQL92QueryParser.WHERE, ASTConstants.WHERE_TOKEN));
    }
    
    /**
     * Is the given node a where node.
     * 
     * @param node node to check
     * 
     * @return <tt>true</tt> if the node is a where node, <tt>false</tt> 
     *         otherwise.
     */
    public static boolean isWhereNode(CommonTree node)
    {
        return node.getText().equals(ASTConstants.WHERE_TOKEN);
    }
    
    /**
     * Is the given node an AND node.
     * 
     * @param node node to check
     * 
     * @return <tt>true</tt> if the node is an AND node, <tt>false</tt> 
     *         otherwise.
     */
    public static boolean isAndNode(CommonTree node)
    {
        return node.getText().equals(ASTConstants.AND_TOKEN);
    }
    
    /**
     * Is the given node an OR node.
     * 
     * @param node node to check
     * 
     * @return <tt>true</tt> if the node is an OR node, <tt>false</tt> 
     *         otherwise.
     */
    public static boolean isOrNode(CommonTree node)
    {
        return node.getText().equals(ASTConstants.OR_TOKEN);
    }
    
    /**
     * Takes QUERY branch of the SQL AST and returns a map that maps string
     * representations of QUERY node direct children to their ASTs.
     * 
     * @param queryAST
     *            QUERY AST node
     * @return map of child ASTs
     */
    public static Map<String, CommonTree> getQueryStructure(CommonTree queryAST) 
    {
        Map<String, CommonTree> queryStructure = 
            new HashMap<String, CommonTree>();

        for (int i = 0; i < queryAST.getChildCount(); i++) 
        {
            CommonTree child = (CommonTree) queryAST.getChild(i);
            queryStructure.put(child.getText(), child);
        }
        return queryStructure;
    }

    /**
     * Checks if a given AST contains QUERY nodes.
     * 
     * @param ast
     *            abstract syntax tree
     * @return <code>true</code> if AST contains subqueries
     */
    public static boolean containsSubqueries(CommonTree ast)
    {
        for (int i = 0; i < ast.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) ast.getChild(i);

            // We are interested in the top level query only.
            if (child.getText().equals(ASTConstants.QUERY_TOKEN))
            {
                return true;
            }
            else
            {
                containsSubqueries(child);
            }
        }
        return false;
    }

    /**
     * Extracts aggregate functions. (FUNCTION) branches of the AST are replaced
     * by (TABLECOLUMN bindingID) branches, where bindingID is automatically
     * generated, unique ID.
     * 
     * User provided list is populated with Function objects representing
     * extracted aggregates. Each function object stores associated bindingID.
     * 
     * @param ast
     *            ast - usually WHERE or SELECT_LIST branches.
     * @param aggregates
     *            a List to be populated with Function objects.
     * @param functionRepository
     *            function repository
     * @throws ExpressionException
     *             when there were problems with parsing parameter list.
     * @throws NoSuchFunctionException
     *             when aggregate function name could not be found in the
     *             repository
     * @throws UnsupportedTokenException 
     */
    public static void extractAggregates(CommonTree ast,
        List<Function> aggregates, FunctionRepository functionRepository)
        throws ExpressionException, NoSuchFunctionException
    {
        for (int i = 0; i < ast.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) ast.getChild(i);

            // We are interested in the top level query only.
            if (child.getText().equals(ASTConstants.FUNCTION_TOKEN))
            {
                // Get function name and retrieve function object.
                Function function = functionRepository
                    .getFunctionInstanceByName(extractFunctionName(child));
                function.initialise(
                    getParameters(child, 1, functionRepository));

                if (function.getType() == FunctionType.SQL_AGGREGATE
                    || function.getType() == FunctionType.UDF_AGGREGATE)
                {
                    // Check if function already exists
                    Function existingAggregate = null;
                    for (Function f : aggregates)
                    {
                        if (function.equals(f))
                        {
                            existingAggregate = f;
                        }
                    }
                    // If it does not then generate new bindingID and add new
                    // function
                    if (existingAggregate == null)
                    {
                        Annotation.addResultNameAnnotation(function,
                            LQPBuilder.getNextID());
                        aggregates.add(function);
                    }
                    else
                    {
                        function = existingAggregate;
                    }

                    TreeAdaptor adaptor = new CommonTreeAdaptor();
                    CommonTree tableColumn = (CommonTree) adaptor
                        .create(
                            SQL92QueryParser.TABLECOLUMN,
                            SQL92QueryParser.tokenNames[SQL92QueryParser.TABLECOLUMN]);
                    tableColumn.addChild((CommonTree) adaptor.create(
                        SQL92QueryParser.ID,
                        Annotation.getResultNameAnnotation(function)));
                    ast.setChild(i, tableColumn);
                }
            }
            // Skip sub queries
            else if (!child.getText().equals(ASTConstants.QUERY_TOKEN))
            {
                extractAggregates(child, aggregates, functionRepository);
            }
        }
    }

    /**
     * Extracts subqueries from the AST and replaces QUERY branches with
     * TABLECOLUMN nodes having automatically generated names. A mapping from
     * the generated table column name to the QUERY branch it replaced is added
     * to a map.
     * 
     * @param astWithSubqueries
     *            abstract syntax tree with subqueries
     * @param subqueries
     *            map of extracted subqueries
     */
    public static void extractReplaceSubqueries(CommonTree astWithSubqueries,
        Map<String, CommonTree> subqueries)
    {
        for (int i = 0; i < astWithSubqueries.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) astWithSubqueries.getChild(i);

            if (child.getText().equals(ASTConstants.QUERY_TOKEN))
            {
                // Assign variable name and modify AST
                String bindingID = LQPBuilder.getNextID();

                TreeAdaptor adaptor = new CommonTreeAdaptor();
                CommonTree tableColumn = (CommonTree) adaptor.create(
                    SQL92QueryParser.TABLECOLUMN,
                    SQL92QueryParser.tokenNames[SQL92QueryParser.TABLECOLUMN]);
                tableColumn.addChild((CommonTree) adaptor.create(
                    SQL92QueryParser.ID, bindingID));
                astWithSubqueries.setChild(i, tableColumn);

                subqueries.put(bindingID, child);
            }
            else
            {
                extractReplaceSubqueries(child, subqueries);
            }
        }
    }

    /**
     * Extracts relations from SQL AST. Adds extracted RELATION nodes to a list.
     * 
     * @param astWithRelationChildren
     *            abstract syntax tree with RELATION nodes
     * @param relationASTs
     *            container list for nodes
     */
    public static void extractRelations(CommonTree astWithRelationChildren,
        List<CommonTree> relationASTs)
    {
        for (int i = 0; i < astWithRelationChildren.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) astWithRelationChildren.getChild(i);
            if (child.getText().equals(ASTConstants.RELATION_TOKEN))
            {
                relationASTs.add(child);
            }
            extractRelations(child, relationASTs);
        }
    }
    
    /**
     * Extracts child relations from SQL AST. All RELATIONs that are a child
     * of the given node will be added to the given list.  This method does
     * not add all the descendant RELATIONs, only those that are children.
     * 
     * @param astWithRelationChildren
     *            abstract syntax tree with RELATION nodes
     * @param relationASTs
     *            container list for nodes
     */
    public static void extractChildRelations(CommonTree astWithRelationChildren,
        List<CommonTree> relationASTs)
    {
        for (int i = 0; i < astWithRelationChildren.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) astWithRelationChildren.getChild(i);
            if (child.getText().equals(ASTConstants.RELATION_TOKEN))
            {
                relationASTs.add(child);
            }
        }
    }

    public static Set<String> extractTableNames(CommonTree tree)
    {
        List<CommonTree> relationASTs = new LinkedList<CommonTree>();
        extractRelations(tree, relationASTs);

        Set<String> tableNames = new HashSet<String>();
        for (CommonTree t : relationASTs)
        {
            if (t.getChild(0).getChildCount() == 0)
                tableNames.add(t.getChild(0).getText());
        }
        
        return tableNames;
    }
    

    /**
     * Extracts existential predicates and replaces them with predicates that
     * are always TRUE. Adds extracted EXISTS nodes to the list.
     * 
     * @param astWithExistentialPredicates
     *            abstract syntax tree with EXISTS nodes
     * @param existPredASTs
     *            container list for extracted predicates
     */
    public static void extractReplaceExistentialPredicates(
            CommonTree astWithExistentialPredicates,
            List<CommonTree> existPredASTs)    
    {
        for (int i = 0; i < astWithExistentialPredicates.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) astWithExistentialPredicates.getChild(i);

            if (child.getText().equals(ASTConstants.NOT_TOKEN))
            {
                CommonTree notChild = (CommonTree) child.getChild(0);

                if (notChild.getText().equals(ASTConstants.NOT_TOKEN))
                {
                    // We have double negation NOT(NOT p) -> p
                    astWithExistentialPredicates.setChild(i, (CommonTree) notChild.getChild(0));
                    // We still need to parse the collapsed branch
                    i--;
                }
                else if (notChild.getText().equals(ASTConstants.EXISTS_TOKEN))
                {
                    // We have NOT EXISTS
                    existPredASTs.add(child);
                    astWithExistentialPredicates.setChild(i, getTrueAST());
                }
            }
            else if (child.getText().equals(ASTConstants.EXISTS_TOKEN))
            {
                // We have EXISTS
                existPredASTs.add(child);
                astWithExistentialPredicates.setChild(i, getTrueAST());
            }
            else if (!child.getText().equals(ASTConstants.QUERY_TOKEN))
            {
                extractReplaceExistentialPredicates(child, existPredASTs);
            }
        }
    }
    
    // Extract ALL ANY for outer query only     
    public static void extractUniversalPredicates(CommonTree whereAST,
            List<CommonTree> universalPredASTs)
    {
        for (int i = 0; i < whereAST.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) whereAST.getChild(i);
            
            if (child.getText().equals(ASTConstants.ALL_TOKEN)
                    || child.getText().equals(ASTConstants.ANY_TOKEN))
            {
                // We have ALL/ANY
                universalPredASTs.add(child);
                whereAST.setChild(i, getTrueAST());
            }
            else if (!child.getText().equals(ASTConstants.QUERY_TOKEN))
            {
                extractUniversalPredicates(child, universalPredASTs);
            }
        }
    }

    /**
     * Gets a predicate that always returns TRUE.
     * 
     * @return
     *      tautology AST
     */
    public static CommonTree getTrueAST()
    {
        SQLQueryParser parser = SQLQueryParser.getInstance();
        try
        {
            return parser.parseSQLForCondition("1=1");
        }
        catch (SQLParserException e)
        {
            // IMPOSSIBLE
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Walks AST and checks if unsupported tokens are present.
     * 
     * @param ast
     *            SQL abstract syntax tree
     * @throws LQPException
     *             when unsupported token is found
     */
    public static void checkForUnsupportedConditionTokens(CommonTree ast)
        throws LQPException
    {
        String[] unsupportedComparisonTokens = new String[] { 
            ASTConstants.SET_TOKEN};

        for (int i = 0; i < ast.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) ast.getChild(i);
            for (String token : unsupportedComparisonTokens)
            {
                if (child.getText().toUpperCase().equals(token))
                {
                    throw new LQPException(
                            new UnsupportedTokenException(token));
                }
            }
            checkForUnsupportedConditionTokens(child);
        }
        
        if(ast.getText().toUpperCase().equals("OR")) {
            if (!checkSupportedOR(ast)) {
                throw new LQPException(new UnsupportedTokenException("OR"));
            }
        }
        
    }

    /**
     * Walks function AST and returns function name.
     * 
     * @param functionAST
     *            function AST
     * @return function name
     */
    public static String extractFunctionName(CommonTree functionAST)
    {
        return functionAST.getChild(0).getText();
    }

    /**
     * Gets parameters from FUNCTION ast.
     * 
     * @param functionAST
     *            FUNCTION branch of SQL AST
     * @param startIdx
     *            node index of the first parameter
     * @param functionRepository
     *            function repository
     * @return a list of parameters represented as arithmetic expressions
     * @throws ExpressionException
     *             when there is a problem building expression
     * @throws UnsupportedTokenException 
     */
    public static List<ArithmeticExpression> getParameters(
            CommonTree functionAST, int startIdx,
            FunctionRepository functionRepository) 
        throws ExpressionException 
    {
        List<ArithmeticExpression> parameters = 
            new ArrayList<ArithmeticExpression>();

        for (int i = startIdx; i < functionAST.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) functionAST.getChild(i);
            parameters.add(ArithmeticExpressionFactory
                .buildArithmeticExpression(child, functionRepository));
        }
        return parameters;
    }

    /**
     * Extracts predicated from the condition AST. If AST is a conjunction - it
     * will be split to several predicates.
     * 
     * @param predicateList
     *            list to which extracted predicates will be added
     * @param conditionAST
     *            condition AST
     * @param functionRepository
     * @throws ExpressionException
     */
    public static void extractPredicates(List<Predicate> predicateList,
        CommonTree conditionAST, FunctionRepository functionRepository)
        throws ExpressionException
    {
        if (conditionAST.getText().toUpperCase().equals(ASTConstants.AND_TOKEN))
        {
            for (int i = 0; i < conditionAST.getChildCount(); i++)
            {
                extractPredicates(predicateList,
                    (CommonTree) conditionAST.getChild(i), functionRepository);
            }
        }
        else
        {
            List<Attribute> boundAttr = new ArrayList<Attribute>();
            extractBoundAttributes(boundAttr, conditionAST);

            Predicate p =
                new CommonPredicate(ExpressionFactory.buildExpression(conditionAST,
                    functionRepository));

            if (boundAttr.size() > 0)
            {
                if (boundAttr.size() > 1)
                {
                    throw new IllegalStateException(
                        "Multiple attributes that need to be bound are not " +
                        "allowed in a single predicate.");
                }
                predicateList.add(new BindingPredicate(p, boundAttr.get(0)));
            }
            else
            {
                predicateList.add(p);
            }
        }
    }

    /**
     * Extracts attributes to be bound from AST and rewrites AST to a
     * conditionAST with BOUND tokens removed.
     * 
     * @param boundAttributes
     *            a list to which attributes to be bound will be added
     * @param conditionAST
     *            condition AST
     */
    private static void extractBoundAttributes(List<Attribute> boundAttributes,
        CommonTree conditionAST)
    {
        for (int i = 0; i < conditionAST.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) conditionAST.getChild(i);
            if (child.getType() == SQL92QueryParser.BOUND)
            {
                CommonTree tableColumnAST = (CommonTree) child.getChild(0);
                boundAttributes.add(new AttributeImpl(
                    tableColumnAST.getChild(1).getText(),
                    tableColumnAST.getChild(0).getText()));
                conditionAST.setChild(i, tableColumnAST);
            }
            else
            {
                extractBoundAttributes(boundAttributes, child);
            }
        }
    }
    
    /**
     * Checks if an OR expression is supported. It goes down the tree of 
     * expression to check if child expressions are supported as well.
     * 
     * @param ast
     *           an expression at the root of which is an OR
     * @return
     *           true of expression is supported, false otherwise
     */
    private static boolean checkSupportedOR(CommonTree ast) {
        for (int i = 0; i < ast.getChildCount(); i++)
        {
            CommonTree child = (CommonTree) ast.getChild(i);
            if (SUPPORTED_OR_TERMINALS.contains(child.getText().toUpperCase())) {
                continue;
            }else if (SUPPORTED_OR_NON_TERMINALS.contains(child.getText().toUpperCase())) {
                checkSupportedOR(child);
                continue;
            }else
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Rewrites <code>a IN QUERY</code> predicate to <code>a = ANY QUERY</code>
     * predicate and <code>a NOT IN QUERY</code> to <code>a != ALL QUERY</code>.
     * Rewrite happens "in-place".
     * 
     * @param ast
     *            AST to rewrite
     */
    public static void rewriteIN(CommonTree ast)
    {
        for(int i=0; i<ast.getChildCount(); i++)
        {
            rewriteIN((CommonTree) ast.getChild(i), i, ast);
        }
    }

    /**
     * Rewrites IN predicates.
     * 
     * @param ast
     *            AST to rewrite
     * @param idx
     *            parent child index (parent -> ast)
     * @param parent
     *            ast node parent
     */
    private static void rewriteIN(CommonTree ast, int idx,
            CommonTree parent)
    {
        if (ast.getText().toUpperCase().equals("NOT")
                && ast.getChild(0).getText().toUpperCase().equals("IN"))
        {
            // NOT IN
            CommonTree inNode = (CommonTree) ast.getChild(0);

            BaseTreeAdaptor adaptor = new CommonTreeAdaptor();
            CommonTree allNode = getAllNode(getNotEqualsNode(
                    (CommonTree) adaptor.dupTree(inNode.getChild(0)),
                    (CommonTree) adaptor.dupTree(inNode.getChild(1))));

            parent.setChild(idx, allNode);
            ast = allNode;
        }
        else if (ast.getText().toUpperCase().equals("IN"))
        {
            // IN
            BaseTreeAdaptor adaptor = new CommonTreeAdaptor();
            CommonTree anyNode = getAnyNode(getEqualsNode(
                (CommonTree) adaptor.dupTree(ast.getChild(0)),
                (CommonTree) adaptor.dupTree(ast.getChild(1))));

            parent.setChild(idx, anyNode);
            ast = anyNode;
        }
        for (int i = 0; i < ast.getChildCount(); i++)
        {
            rewriteIN((CommonTree) ast.getChild(i), i, ast);
        }
    }
}
