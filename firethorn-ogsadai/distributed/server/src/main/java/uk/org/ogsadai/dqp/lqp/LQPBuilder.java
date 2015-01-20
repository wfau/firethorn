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

package uk.org.ogsadai.dqp.lqp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.AntiSemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.DifferenceOperator;
import uk.org.ogsadai.dqp.lqp.operators.DuplicateEliminationOperator;
import uk.org.ogsadai.dqp.lqp.operators.FullOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.FunctionOperatorBuilder;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.IntersectionOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.LimitOperator;
import uk.org.ogsadai.dqp.lqp.operators.TopOperator;
import uk.org.ogsadai.dqp.lqp.operators.NilOperator;
import uk.org.ogsadai.dqp.lqp.operators.OneRowOnlyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.RightOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScalarGroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SortOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQuery;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQueryFactory;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.QueryNormaliser;
import uk.org.ogsadai.dqp.lqp.optimiser.implosion.ExtendedTableScanImplosionOptimiser;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;
import uk.org.ogsadai.dqp.lqp.util.ASTConstants;
import uk.org.ogsadai.dqp.lqp.util.ASTUtil;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;

/**
 * Builds logical query plan from an abstract syntax tree generated from an SQL
 * query.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class LQPBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** The data dictionary. */
    private final DataDictionary mDataDictionary;

    /** The operator builder. */
    private final FunctionOperatorBuilder mOperatorBuilder;
    
    /** Table scan query factory. */
    private final TableScanQueryFactory mTableScanQueryFactory;

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(LQPBuilder.class);

    /**
     * Constructor.
     * 
     * @param dataDictionary
     *            data dictionary
     * @param compilerConfiguration
     *            compiler configuration
     */
    public LQPBuilder(
        DataDictionary dataDictionary,
        CompilerConfiguration compilerConfiguration)
    {
        mDataDictionary = dataDictionary;
        mOperatorBuilder = new FunctionOperatorBuilder(compilerConfiguration,
            dataDictionary.getFunctionRepository());
        mTableScanQueryFactory = 
            compilerConfiguration.getTableScanQueryFactory();
    }

    /**
     * Builds a query plan for a query.
     * 
     * @param ast
     *            abstract syntax tree
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return root operator of the query plan
     * @throws LQPException
     *             if an error occurred
     */
    public Operator buildLQP(
        CommonTree ast, 
        FunctionRepository functionRepository) 
        throws LQPException
    {
        Operator nilOperator = new NilOperator();

        CommonTree child = (CommonTree) ast.getChild(0);
        Operator statementRoot;
        try
        {
            statementRoot = buildStatement(child, functionRepository);
        }
        catch (ExpressionException e)
        {
            throw new LQPException(e);
        }

        // Check if we have ORDER_BY or LIMIT
        Operator sortOperator = null;
        Operator limitOperator = null;
        Operator topOperator = null;
        for (int i=1; i<ast.getChildCount(); i++)
        {
            String astText = ast.getChild(i).getText();
            if (ASTConstants.ORDER_TOKEN.equals(astText))
            {
                sortOperator = new SortOperator((CommonTree) ast.getChild(i));
            }
            else if (ASTConstants.LIMIT_TOKEN.equals(astText))
            {
                limitOperator = new LimitOperator(
                        ast.getChild(i).getChild(0).getText());
            } 
            else  if (ASTConstants.TOP_TOKEN.equals(astText))
            {
                topOperator = new TopOperator(
                        ast.getChild(i).getChild(0).getText());
            }
        }
        if (limitOperator != null)
        {
            connectUnary(nilOperator, limitOperator);
            if (sortOperator != null)
            {
                limitOperator.setChild(0, sortOperator);
                sortOperator.setChild(0, statementRoot);
            }
            else
            {
                limitOperator.setChild(0, statementRoot);
            }
        } 
        else  if (topOperator != null)
        {
            connectUnary(nilOperator, topOperator);
            if (sortOperator != null)
            {
            	topOperator.setChild(0, sortOperator);
                sortOperator.setChild(0, statementRoot);
            }
            else
            {
            	topOperator.setChild(0, statementRoot);
            }
        }
        else if (sortOperator != null)
        {
            connectUnary(nilOperator, sortOperator);
            sortOperator.setChild(0, statementRoot);
        }
        else
        {
            connectUnary(nilOperator, statementRoot);
        }
        
        // We need to validate pre-normalised LQP as the query normaliser
        // expects that we have already detected all the correlated attributes
        nilOperator.validate();
        
        // Query normaliser will turn 
        Operator root = null;
        QueryNormaliser qn = new QueryNormaliser();
        root = qn.optimise(nilOperator, null, null, null);

        return root;
    }

    /**
     * Builds tree from statement AST.
     * 
     * @param statementAST
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return tree root operator
     * @throws LQPException
     */
    private Operator buildStatement(
        CommonTree statementAST,
        FunctionRepository functionRepository)
        throws LQPException, ExpressionException
    {
        String astText = statementAST.getText();
        Operator statementRoot;

        if (astText.equals(ASTConstants.QUERY_TOKEN))
        {
            statementRoot = buildQuery(statementAST, functionRepository);
        }
        else
        {
            statementRoot =
                buildUniIntExc(statementAST, astText, functionRepository);
        }

        return statementRoot;
    }

    /**
     * Build set operator.
     * 
     * @param ast
     * @param opToken
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return
     * @throws LQPException
     */
    private Operator buildUniIntExc(
        CommonTree ast, 
        String opToken,
        FunctionRepository functionRepository)
        throws LQPException, ExpressionException
    {
        // Create operator for union intersect or except
        Operator uniIntExcOperator = null;

        if (opToken.equals(ASTConstants.UNION_TOKEN))
        {
            uniIntExcOperator = new UnionOperator(false);
        }
        if (opToken.equals(ASTConstants.UNION_ALL_TOKEN))
        {
            uniIntExcOperator = new UnionOperator(true);
        }
        else if (opToken.equals(ASTConstants.EXCEPT_TOKEN))
        {
            uniIntExcOperator = new DifferenceOperator(false);
        }
        else if (opToken.equals(ASTConstants.EXCEPT_ALL_TOKEN))
        {
            uniIntExcOperator = new DifferenceOperator(true);
        }
        else if (opToken.equals(ASTConstants.INTERSECT_TOKEN))
        {
            uniIntExcOperator = new IntersectionOperator(false);
        }

        CommonTree leftChild = (CommonTree) ast.getChild(0);
        CommonTree rightChild = (CommonTree) ast.getChild(1);
        
        String leftTokenText = leftChild.getText();
        String rightTokenText = rightChild.getText();

        Operator leftOperator;
        Operator rightOperator;
        
        if (leftTokenText.equals(ASTConstants.QUERY_TOKEN))
        {
            leftOperator = buildQuery(leftChild, functionRepository);
        }
        else
        {
            leftOperator =
                buildUniIntExc(leftChild, leftTokenText, functionRepository);
        }

        if (rightTokenText.equals(ASTConstants.QUERY_TOKEN))
        {
            rightOperator = buildQuery(rightChild, functionRepository);
        }
        else
        {
            rightOperator =
                buildUniIntExc(rightChild, rightTokenText, functionRepository);
        }

        // check for union compatibility and add explicit rename if required -
        // we are only concerned with names here - type validation will happen
        // in validate()
        List<Attribute> leftHeadingAttr = leftOperator.getHeading()
            .getAttributes();
        List<Attribute> rightHeadingAttr = rightOperator.getHeading()
            .getAttributes();

        if (leftHeadingAttr.size() != rightHeadingAttr.size())
        {
            throw new LQPException("Relations are not union compatibile.");
        }
        else if (leftHeadingAttr.equals(rightHeadingAttr))
        {
            connectBinary(uniIntExcOperator, leftOperator, rightOperator);
        }
        else
        {
            RenameMap renameMap = new SimpleRenameMap(rightHeadingAttr,
                leftHeadingAttr);
            Operator renameOpertaor = new RenameOperator(rightOperator,
                renameMap);
            connectBinary(uniIntExcOperator, leftOperator, renameOpertaor);
        }
        uniIntExcOperator.update();

        return uniIntExcOperator;
    }

    /**
     * Build query.
     * 
     * @param ast
     * @param functionRepository
     *            function repository, or <tt>null</tt> if functions are not
     *            supported.
     * @return
     * @throws LQPException
     */
    private Operator buildQuery(
        CommonTree ast, FunctionRepository functionRepository) 
        throws LQPException, ExpressionException
    {
        Operator queryRoot;
        Map<String, CommonTree> queryStructure;
        queryStructure = ASTUtil.getQueryStructure(ast);

        // Split the tree into branches
        CommonTree selectListAST = queryStructure
            .get(ASTConstants.SELECT_LIST_TOKEN);
        CommonTree fromListAST = queryStructure
            .get(ASTConstants.FROM_LIST_TOKEN);
        CommonTree groupByAST = queryStructure.get(ASTConstants.GROUP_BY_TOKEN);
        CommonTree havingAST = queryStructure.get(ASTConstants.HAVING_TOKEN);
        CommonTree whereAST = queryStructure.get(ASTConstants.WHERE_TOKEN);

        // build from list - a list of root operators for each comma separated
        // entry from the query
        List<Operator> fromList = buildFromList(fromListAST, functionRepository);

        // create a query graph and get the result root operator - all operators
        // in the from list will be cross joined at this stage
        QueryGraph queryGraph = new QueryGraph();
        queryGraph.addOperators(fromList);
        
        // this temporary operator will be later replaced
        Operator tempQueryRoot = queryGraph.getQueryPlan();
        
        // getting a query plan cleans the graph - we need to rebuild it
        queryGraph.addOperators(fromList);

        // Process WHERE
        if (whereAST != null)
        {
            // we deal with subqueries in the condition - tempQueryRoot is used
            // to represent from list relation - this call will rewrite the
            // whereAST so it can be used to construct a predicate
            queryRoot =
                processSubqueriesInWhere(whereAST, tempQueryRoot, functionRepository);
            
            // extract predicates - conjunctive predicates will be split and
            // form a list of predicates
            List<Predicate> predicateList = new LinkedList<Predicate>();
            ASTUtil.extractPredicates(predicateList,
                (CommonTree) whereAST.getChild(0), functionRepository);
                        
            // Save tempQueryRoot parent - it may change after adding predicates
            Operator tempQueryRootParent = tempQueryRoot.getParent();
            
            // add predicates to the query graph - only equijoin predicates can
            // be used as edges - all the remaining predicates will be rejected
            List<Predicate> rejectedPredicates = 
                queryGraph.addPredicates(predicateList);

            // build a query plan
            Operator newRoot = queryGraph.getQueryPlan();
            
            // Add project to deal with possible select *
            // This uglyfies the code but is required by SQL semantics
            List<Attribute> projectAttrs = new LinkedList<Attribute>();
            for (Operator o : fromList)
                for (Attribute a : o.getHeading().getAttributes())
                    projectAttrs.add(a.getClone(false));            
            ProjectOperator projectOperator = new ProjectOperator(newRoot, projectAttrs);
            projectOperator.update();
            newRoot = projectOperator;
            
            // if there were subqueries we need to replace tempQueryRoot with
            // newRoot
            if(tempQueryRoot != queryRoot)
            {
                tempQueryRootParent.replaceChild(tempQueryRoot, newRoot);
            }
            // otherwise we just set queryRoot to newRoot
            else
            {
                queryRoot = newRoot;
            }
            
            // if we have rejected predicates we need to add these to the plan
            // encapsulated in the SELECT operator
            if(rejectedPredicates.size() > 0)
            {
                Predicate p = new CommonPredicate(rejectedPredicates);
                Operator selectOperator = new SelectOperator(queryRoot, p);
                queryRoot = selectOperator;
            }
        }
        // If there was no WHERE clause - tempQueryRoot becomes queryRoot
        else
        {
            queryRoot = tempQueryRoot;
        }
        queryRoot.update();
        
        OptimiserUtils.checkTreeLinks(queryRoot);

        // Deal with scalar subqueries in the SELECT_LIST
        Map<String, CommonTree> subqueries = new HashMap<String, CommonTree>();
        ASTUtil.extractReplaceSubqueries(selectListAST, subqueries);

        // Get trees for subqueries and add to the LQP using outer join
        for (String queryBindingID : subqueries.keySet())
        {
            Operator subqueryRoot = buildQuery(
                subqueries.get(queryBindingID), functionRepository);

            // Rename attribute to bindingID
            Attribute subqueryAttr = subqueryRoot.getHeading().getAttributes()
                .get(0);
            RenameMap renameMap = new SimpleRenameMap();
            renameMap.add(subqueryAttr, subqueryAttr.getCloneNewName(
                queryBindingID, true));
            Operator renameOperator = new RenameOperator(subqueryRoot,
                renameMap);

            // Create one row only operator
            Operator oneRowOnly = new OneRowOnlyOperator(renameOperator, false);

            // Left outer join - if the result of the sub query is an empty set
            // then we use null
            Operator outerJoin = new LeftOuterJoinOperator(queryRoot,
                oneRowOnly, new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        ASTUtil.getTrueAST(), functionRepository)));

            outerJoin.update();
            queryRoot = outerJoin;
        }

        // Extract any aggregates
        List<Function> selectAggregates = new ArrayList<Function>();
        List<Function> havingAggregates = new ArrayList<Function>();

        try
        {
            ASTUtil.extractAggregates(selectListAST, selectAggregates,
                mDataDictionary.getFunctionRepository());
            if (havingAST != null)
            {
                ASTUtil.extractAggregates(havingAST, havingAggregates,
                    mDataDictionary.getFunctionRepository());
            }
        }
        catch (NoSuchFunctionException e)
        {
            throw new LQPException(e);
        }

        // Check if we have GROUP_BY -> expect group aggregates
        if (groupByAST != null)
        {
            // TODO: Semantic check on SELECT_LIST
            Operator groupByOperator = new GroupByOperator(queryRoot,
                groupByAST, selectAggregates, havingAggregates);

            queryRoot = groupByOperator;
        }
        // We may still have scalar aggregates
        else
        {
            if (havingAggregates.size() > 0 || selectAggregates.size() > 0)
            {
                Operator groupByOperator = new ScalarGroupByOperator(queryRoot,
                    selectAggregates, havingAggregates);

                queryRoot = groupByOperator;
            }
        }

        // Process HAVING
        if (havingAST != null)
        {
            queryRoot =
                processSubqueriesInWhere(havingAST, queryRoot, functionRepository);

            Operator selectOperator =
                new SelectOperator(queryRoot, new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        (CommonTree) havingAST.getChild(0), functionRepository)));
            queryRoot = selectOperator;
        }

        // Finally project
        ProjectOperator project = 
            new ProjectOperator(queryRoot, selectListAST, functionRepository);
        queryRoot = project;

        // If we have aliases for column names add rename operator
        RenameMap renameMap = project.getRenameMap();

        if (renameMap != null)
        {
            Operator renameOperator = new RenameOperator(queryRoot, renameMap);

            renameOperator.update();
            queryRoot = renameOperator;
        }

        // Deal with DISTINCT
        if (selectListAST.getChild(0).getText().toUpperCase().equals(
            ASTConstants.DISTINCT_TOKEN))
        {
            Operator duplElimOperator = new DuplicateEliminationOperator(
                queryRoot);

            duplElimOperator.update();
            queryRoot = duplElimOperator;
        }

        return queryRoot;
    }

    /**
     * Walks the FROM_LIST AST and builds query plan branches for each comma
     * separated relation in the from list. For any of the derived relations
     * (subqueries, joins, etc.) the root operator is added to the result list.
     * <p>
     * For example a FROM_LIST build for: <br />
     * <code>SELECT * FROM a, b JOIN c ON b.x=c.x, (SELECT * FROM d) e</code>
     * <br />
     * will result in a list of 3 operators.
     * </p>
     * 
     * @param fromListAST
     *            a FROM_LIST AST branch
     * @param functionRepository
     * @return a list of relations in the from list
     * @throws LQPException
     * @throws ExpressionException
     */
    private List<Operator> buildFromList(
        CommonTree fromListAST, FunctionRepository functionRepository ) 
        throws LQPException, ExpressionException
    {
        List<Operator> fromList = new LinkedList<Operator>();

        for (int i = 0; i < fromListAST.getChildCount(); i++)
        {
            Operator currentOperator;

            CommonTree child = (CommonTree) fromListAST.getChild(i);
            String childText = child.getText();

            if (childText.equals(ASTConstants.RELATION_TOKEN))
            {
                currentOperator = buildRelation(child, functionRepository);
            }
            else if (childText.equals(ASTConstants.JOIN_TOKEN)
                || childText.equals(ASTConstants.LEFT_OUTER_JOIN_TOKEN)
                || childText.equals(ASTConstants.RIGHT_OUTER_JOIN_TOKEN)
                || childText.equals(ASTConstants.FULL_OUTER_JOIN_TOKEN))
            {
                currentOperator = buildJoin(child, functionRepository);
            }
            else
            {
                throw new LQPException("UNEXPECTED TOKEN: " + childText);
            }
            
            fromList.add(currentOperator);
        }
        return fromList;
    }

    /**
     * Build relation.
     * 
     * @param ast
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return
     * @throws LQPException
     */
    private Operator buildRelation(
        CommonTree ast, FunctionRepository functionRepository) 
        throws LQPException, ExpressionException
    {
        Operator relationRoot;

        CommonTree child = (CommonTree) ast.getChild(0);
        String childText = child.getText();

        if (childText.equals(ASTConstants.UNION_TOKEN)
            || childText.equals(ASTConstants.UNION_ALL_TOKEN)
            || childText.equals(ASTConstants.EXCEPT_TOKEN)
            || childText.equals(ASTConstants.EXCEPT_ALL_TOKEN)
            || childText.equals(ASTConstants.INTERSECT_TOKEN)
            || childText.equals(ASTConstants.QUERY_TOKEN))
        {
            // We have a derived table
            relationRoot = buildStatement(child, functionRepository);
        }
        else if (childText.equals(ASTConstants.FUNCTION_TOKEN))
        {
            // We have a relation function, extract what we can
            String functionName;
            List<CommonTree> relationASTs;
            List<ArithmeticExpression> parameterList;

            functionName = ASTUtil.extractFunctionName(child);
            relationASTs = new ArrayList<CommonTree>();
            ASTUtil.extractChildRelations(child, relationASTs);

            int parametersStartIdx = relationASTs.size() + 1;
            try
            {
                parameterList = ASTUtil.getParameters(
                    child,
                    parametersStartIdx,
                    functionRepository);
            }
            catch (ExpressionException e)
            {
                e.printStackTrace();
                throw new LQPException(e);
            }

            Operator relOperator = mOperatorBuilder.buildRelFunctionOperator(
                functionName, parameterList, relationASTs.size());
            
            Operator[] childOperators = new Operator[relationASTs.size()];
            for (int i = 0; i < relationASTs.size(); i++)
                childOperators[i] = 
                    buildRelation(relationASTs.get(i), functionRepository);

            if (childOperators.length > 0)
            {
                for (int i = 0; i < childOperators.length; i++) 
                {
                    relOperator.setChild(i, childOperators[i]);
                }
            }
            else
            {
                // We have a tuple producing, SCAN type operator
                ((ScanOperator) relOperator).setDataDictionary(mDataDictionary);
            }
            relOperator.update();

            relationRoot = relOperator;
        }
        else
        {
            // We have a table name
            TableScanQuery tableScanQuery = 
                mTableScanQueryFactory.createTableScanQuery();
            tableScanQuery.setTableName(childText);
            ScanOperator tableScanOperator = 
                new TableScanOperator(tableScanQuery);
            tableScanOperator.setDataDictionary(mDataDictionary);
            tableScanOperator.update();

            relationRoot = tableScanOperator;
        }

        // Lets check if we have aliases and act appropriately
        if (ast.getChildCount() > 1)
        {
            // Get alias
            childText = ast.getChild(1).getText();

            Operator renameOperator = new RenameOperator(relationRoot,
                childText);

            renameOperator.update();
            relationRoot = renameOperator;
        }

        return relationRoot;
    }

    /**
     * Build explicit join.
     * 
     * @param ast
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return
     * @throws LQPException
     */
    private Operator buildJoin(
        CommonTree ast,
        FunctionRepository functionRepository) 
        throws LQPException, ExpressionException
    {
        CommonTree leftRelation = (CommonTree) ast.getChild(0);
        CommonTree rightRelation = (CommonTree) ast.getChild(1);

        // All explicit joins have ON followed by condition
        CommonTree joinCondition = (CommonTree) ast.getChild(2);

        // It gets a bit more complex when we have subqueries
        if (ASTUtil.containsSubqueries(joinCondition))
        {
            throw new LQPException(
                "Subqueries in join conditions are not supported.");
        }
        else
        {
            Operator leftOperator;
            if (leftRelation.getText().equals(ASTConstants.RELATION_TOKEN))
            {
                leftOperator = buildRelation(leftRelation, functionRepository);
            }
            else
            {
                leftOperator = buildJoin(leftRelation, functionRepository);
            }

            Operator rightOperator = 
                buildRelation(rightRelation, functionRepository);

            String joinText = ast.getText();
            // Check join type
            Operator joinOperator = null;
            if (joinText.equals(ASTConstants.JOIN_TOKEN))
            {
                joinOperator = new InnerThetaJoinOperator(new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        joinCondition, functionRepository)));
            }
            else if (joinText.equals(ASTConstants.FULL_OUTER_JOIN_TOKEN))
            {
                joinOperator = new FullOuterJoinOperator(new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        joinCondition, functionRepository)));
            }
            else if (joinText.equals(ASTConstants.RIGHT_OUTER_JOIN_TOKEN))
            {
                joinOperator = new RightOuterJoinOperator(new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        joinCondition, functionRepository)));
            }
            else if (joinText.equals(ASTConstants.LEFT_OUTER_JOIN_TOKEN))
            {
                joinOperator = new LeftOuterJoinOperator(new CommonPredicate(
                    ExpressionFactory.buildExpression(
                        joinCondition, functionRepository)));
            }
            else
            {
                throw new LQPException("Unsuppoted join " + joinText);
            }

            connectBinary(joinOperator, leftOperator, rightOperator);
            joinOperator.update();
            
            return joinOperator;
        }
    }

    /**
     * Processes subqueries in the WHERE clause.
     * 
     * @param whereAST
     * @param root
     * @param functionRepository
     *            function repository or <tt>null</tt> if functions are not
     *            supported.
     * @return
     * @throws LQPException
     */
    private Operator processSubqueriesInWhere(
        CommonTree whereAST, 
        Operator root,
        FunctionRepository functionRepository)
        throws LQPException, ExpressionException
    {
        ASTUtil.checkForUnsupportedConditionTokens(whereAST);
        Operator currentRoot = root;

        // Existential predicates
        List<CommonTree> existPredASTs = new ArrayList<CommonTree>();        
        ASTUtil.extractReplaceExistentialPredicates(whereAST, existPredASTs);
        for (CommonTree existPred : existPredASTs)
        {
            CommonTree queryAST;
            Operator semiJoinOperator;

            if (existPred.getText().equals(ASTConstants.NOT_TOKEN))
            {
                semiJoinOperator = new AntiSemiJoinOperator(getTruePredicate());
                queryAST = (CommonTree) existPred.getChild(0).getChild(0);
            }
            else
            {
                semiJoinOperator = new SemiJoinOperator(getTruePredicate());
                queryAST = (CommonTree) existPred.getChild(0);
            }

            Operator queryRoot = buildStatement(queryAST, functionRepository);

            connectBinary(semiJoinOperator, currentRoot, queryRoot);
            semiJoinOperator.update();

            currentRoot = semiJoinOperator;
        }
        
    // Universal predicates
    ASTUtil.rewriteIN(whereAST);
    List<CommonTree> uniPredASTs = new ArrayList<CommonTree>();
    ASTUtil.extractUniversalPredicates(whereAST, uniPredASTs);
    
    for (CommonTree uniPred : uniPredASTs)
    {
        String cmp = uniPred.getChild(0).getText();
        Operator queryRoot = buildStatement(
                (CommonTree) uniPred.getChild(0).getChild(1), 
                functionRepository);
        Attribute attr = ASTUtil.getAttribute(
                (CommonTree) uniPred.getChild(0).getChild(0));
        Attribute qAttr = queryRoot.getHeading().getAttributes().get(0);
        
        // we may have name clashes - rename
        if (attr.equals(qAttr, AttributeMatchMode.NAME_AND_NULL_SOURCE)
            || (currentRoot.getHeading().contains(qAttr) && queryRoot
                .getHeading().contains(qAttr)))
        {
            RenameOperator rename = new RenameOperator(
                ExtendedTableScanImplosionOptimiser.nextID("SUBQ"));
            rename.setChild(0, queryRoot);
            rename.update();
            queryRoot = rename;
            
            // use rednamed attr on the right side
            qAttr = queryRoot.getHeading().getAttributes().get(0);
            
            // use fully qualified attr on the left side
            attr = currentRoot.getHeading().getMatchingAttribute(attr);
        }        

        // single attribute expected in the heading
        if (queryRoot.getHeading().getAttributes().size() != 1)
        {
            throw new LQPException(
                "Only a single expression can be specified in the select "
                    + "list of a subquery when not using EXISTS");
        }
        else
        {
            Operator semiJoinOperator;
            if (uniPred.getText().equals(ASTConstants.ANY_TOKEN))
            {
                Predicate pred = new CommonPredicate(attr.toString() + cmp
                    + qAttr.toString(), functionRepository);
    
                semiJoinOperator = new SemiJoinOperator(pred);
    
                connectBinary(semiJoinOperator, currentRoot, queryRoot);
                semiJoinOperator.update();
                currentRoot = semiJoinOperator;
    
            }
            else if (uniPred.getText().equals(ASTConstants.ALL_TOKEN))
            {
                Predicate pred = new CommonPredicate("( " + attr
                    + CommonPredicate.getNegation(cmp) + qAttr
                    + " AND " + qAttr + " IS NOT NULL ) OR " + attr + " IS NULL",
                    functionRepository);
                semiJoinOperator = new AntiSemiJoinOperator(pred);
    
                connectBinary(semiJoinOperator, currentRoot, queryRoot);
                semiJoinOperator.update();
                currentRoot = semiJoinOperator;
            }
            else
            {
                throw new RuntimeException(
                    "Expected either ALL or ANY token");
            }

        }
    }

        Map<String, CommonTree> subqueries = new HashMap<String, CommonTree>();
        ASTUtil.extractReplaceSubqueries(whereAST, subqueries);
        // TODO: This is basically the same stuff that happens for SELECT_LIST
        for (String queryBindingID : subqueries.keySet())
        {
            Operator subqueryRoot = buildQuery(
                subqueries.get(queryBindingID), functionRepository);

            // Rename attribute to bindingID
            Attribute subqueryAttr = subqueryRoot.getHeading().getAttributes()
                .get(0);
            RenameMap renameMap = new SimpleRenameMap();
            renameMap.add(subqueryAttr, subqueryAttr.getCloneNewName(
                queryBindingID, false));

            Operator renameOperator = new RenameOperator(subqueryRoot,
                renameMap);

            // Create one row only operator
            Operator oneRowOnly = new OneRowOnlyOperator(renameOperator, true);

            // Outer Join - we need a pass for null? TODO: Sanity check
            Operator outerJoin = new LeftOuterJoinOperator(currentRoot,
                oneRowOnly, getTruePredicate());
            outerJoin.update();

            currentRoot = outerJoin;
        }
        return currentRoot;
    }

    /**
     * Returns a predicate that always evaluates to TRUE.
     * 
     * @return true predicate
     */
    private static Predicate getTruePredicate()
    {
        try
        {
            return new CommonPredicate(ExpressionFactory.buildExpression(
                ASTUtil.getTrueAST(), null));
        }
        catch (ExpressionException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Connect child to a unary operator.
     * 
     * @param operator
     *            parent operator
     * @param childOperator
     *            child operator
     */
    private void connectUnary(Operator operator, Operator childOperator)
    {
        operator.setChild(0, childOperator);
    }

    /**
     * Connects children to a binary operator.
     * 
     * @param operator
     *            parent operator
     * @param leftChildOperator
     *            left child operator
     * @param rightChildOperator
     *            right child operator
     */
    private void connectBinary(Operator operator, Operator leftChildOperator,
        Operator rightChildOperator)
    {
        operator.setChild(0, leftChildOperator);
        operator.setChild(1, rightChildOperator);
    }

    private static int mIdCounter = 0;

    /**
     * Returns the next unique identifier.
     * 
     * @return unique identifier
     */
    public static synchronized String getNextID()
    {
        // Reset counter
        if (mIdCounter == Integer.MAX_VALUE)
        {
            mIdCounter = 0;
        }
        mIdCounter++;

        return "ID__" + mIdCounter;
    }

}
