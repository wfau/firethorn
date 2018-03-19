// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.operators.AbstractJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.AbstractOperator;
import uk.org.ogsadai.dqp.lqp.operators.GroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.LeftOuterJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScalarGroupByOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQuery;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpOptimiser;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.util.ASTUtil;
import uk.org.ogsadai.dqp.lqp.util.SQLStatementType;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;

/**
 * Extended table scan query data structure. It allows for imploding complex
 * query plans using derived tables.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ExtendedTableScanQuery implements TableScanQuery
{
    /** Initial table scan query. */
    final TableScanQuery mOriginalScanQuery;
    /** Data dictionary. */
    DataDictionary mDataDictionary;
    /** Data node. */
    DataNode mDataNode;
    /** Current query. */
    String mQueryStatement;
    /** Current heading. */
    Heading mHeading;
    /** Attribute to use in the ORDER BY clause. */
    Attribute mSortAttribute;
    
    /** 
     * Does this table scan query support filtered table scan inserting a
     * where clause by parsing the query string?
     */
    boolean mSupportsFilteredTableScan = true;
    
    /**
     * If filtered table scan is supported this is the mapping from heading
     * attributes to logical attributes. If an attribute is not in this
     * mapping it can be mapped to a physical attribute directly.
     */
    Map<Attribute,Attribute> mHeadingToLogicalAttribute =
        new HashMap<Attribute,Attribute>();
    

    /**
     * Constructor.
     * 
     * @param scanQuery
     *            initial table scan query
     * @param dataNode
     *            data node exposing scanned data source
     */
    public ExtendedTableScanQuery(TableScanQuery scanQuery, DataNode dataNode)
    {
        mOriginalScanQuery = scanQuery;
        mDataNode = dataNode;
        
        mQueryStatement = scanQuery.getSQLQuery();
    }

    /**
     * Add duplicate elimination (DISTINCT clause).
     * 
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addDuplicateElim(Heading heading)
    {
        mSupportsFilteredTableScan = false;
        
        List<Attribute> headingAttrs = heading.getAttributes();
    
        // Get query type
        //
        int queryType = SQLStatementType.getStatementType(mQueryStatement);
        boolean sfw = SQLStatementType.isSelectFrom(queryType)
            || SQLStatementType.isSelectFromWhere(queryType);
    
        // If we have SFW query then we can just modify SELECT else we need to
        // use a derived table.
        //
        if (sfw)
        {
            mQueryStatement = mQueryStatement.replaceAll("^SELECT",
                "SELECT DISTINCT");
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT DISTINCT ");
            for (int i = 0; i < headingAttrs.size(); i++)
            {
            sb.append(headingAttrs.get(i).getName());
            if (i < headingAttrs.size() - 1)
            {
                sb.append(", ");
            }
            }
    
            mQueryStatement = sb.toString() + " FROM (" + mQueryStatement
                + ") AS "
                + ExtendedTableScanImplosionOptimiser.nextID("table");
        }
        mHeading = new HeadingImpl(headingAttrs);
    }
    
    /**
     * Add set clause (UNION, INTERSECT, EXCEPT).
     * 
     * @param operatorID
     *            ID of the set operator
     * @param bag
     *            should ALL be used
     * @param scanQuery
     *            query to add on the RHS
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addSetOp(OperatorID operatorID, boolean bag, TableScanQuery scanQuery,
        Heading heading)
    {
        mSupportsFilteredTableScan = false;
        
        String setOp;
        if (operatorID == OperatorID.UNION)
        {
            setOp = "UNION";
        }
        else if (operatorID == OperatorID.INTERSECTION)
        {
            setOp = "INTERSECT";
        }
        else if (operatorID == OperatorID.DIFFERENCE)
        {
            setOp = "EXCEPT";
        }
        else
        {
            throw new IllegalArgumentException("Expected set opeartor");
        }
    
        mQueryStatement = mQueryStatement + ' ' + setOp + (bag ? " ALL" : "")
            + ' ' + scanQuery.getSQLQuery();
    }

    /**
     * Add subquery using EXIST clause.
     * 
     * @param operatorID
     *            ID of the operator
     * @param existsQuery
     *            subquery
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addExists(AbstractJoinOperator join, TableScanQuery existsQuery)
    {
        mSupportsFilteredTableScan = false;
        
        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        String predSource = ExtendedTableScanImplosionOptimiser.nextID("table");
        List<Attribute> headingAttrs = join.getHeading().getAttributes();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(source);
            sb.append(".");
            sb.append(headingAttrs.get(i).getName());
            if (i < headingAttrs.size() - 1)
                sb.append(", ");
        }
        // Do we have EXIST or NOT EXIST?
        //
        String existsString = 
            (join.getID() == OperatorID.SEMI_JOIN) ? " WHERE EXISTS ("
                : " WHERE NOT EXISTS (";

        sb.append(" FROM (").append(mQueryStatement).append(") AS ");
        sb.append(source);
        sb.append(existsString);
        sb.append("SELECT * FROM (");
        sb.append(existsQuery.getSQLQuery());
        sb.append(") AS ");
        sb.append(predSource);
        sb.append(" WHERE ");
        // set the source for the predicate attributes
        join.getPredicate().getExpression().accept(
                new SetSourceExpressionVisitor(
                        new SetJoinSourceVisitor(source, predSource, headingAttrs)));
        sb.append(join.getPredicate());
        sb.append(")");
        mQueryStatement = sb.toString();
    }

    /**
     * Add correlated subquery using EXIST clause.
     * 
     * @param operatorID
     *            ID of the operator
     * @param corrQuery
     *            correlated subquery
     * @param renameMap
     *            rename map
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addCorrelatedExists(OperatorID operatorID, TableScanQuery corrQuery,
        Map<String, String> renameMap, Predicate predicate, Heading heading)
    {
        mSupportsFilteredTableScan = false;

        List<Attribute> headingAttrs = heading.getAttributes();
    
        String corrStmnt = corrQuery.getSQLQuery();
        String corrPred = predicate.toString();
        
        // Rename correlated attributes.
        //
        if (renameMap != null)
        {
            for (String s : renameMap.keySet())
            {
                corrStmnt = corrStmnt.replaceAll(
                        "\\b" + s + "\\b", renameMap.get(s));
                corrPred = corrPred.replaceAll(
                        "\\b" + s + "\\b", renameMap.get(s));
            }
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(headingAttrs.get(i).getName());
            if (i < headingAttrs.size() - 1)
            sb.append(", ");
        }
    
        String existsString = (operatorID == OperatorID.SEMI_JOIN) ? " WHERE EXISTS ("
            : " WHERE NOT EXISTS (";
    
        mQueryStatement = sb.toString() + " FROM (" + mQueryStatement + ") AS "
            + ExtendedTableScanImplosionOptimiser.nextID("table")
            + existsString + "SELECT * FROM (" + corrStmnt + ") AS "
            + ExtendedTableScanImplosionOptimiser.nextID("table")
            + " WHERE " + corrPred + ")";
    
        mHeading = new HeadingImpl(headingAttrs);
    }

    /**
     * Add subquery to the select list.
     * 
     * @param subQuery
     *            subquery
     * @param attrDefs
     *            attribute definitions of the select list
     * @param renameMap
     *            rename map - may be needed for correlated subqueries where
     *            there is rename operator on the non-correlated side. Should be
     *            set to <code>null</code> where no renames are required.
     * @param subqAttr
     *            attribute to be replaced by a subquery
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addSelectListSubquery(
            TableScanQuery subQuery,
            ProjectOperator project,
            LeftOuterJoinOperator loj,
            Map<String, String> renameMap,
            Attribute subqAttr,
            Operator thisScan)
    {
        mSupportsFilteredTableScan = false;

        String outerSource = ExtendedTableScanImplosionOptimiser.nextID("table");

        // construct the correlation statement in the select list
        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        String corrStmnt = subQuery.getSQLQuery();    
        if (renameMap != null)
        {
            for (String s : renameMap.keySet())
            {
                corrStmnt = corrStmnt.replaceAll(
                        "\\b" + s + "\\b", renameMap.get(s));
            }
        }
    
        corrStmnt = "SELECT " + source + "." + subqAttr.getName()
                        + " FROM (" + corrStmnt
                        + ") AS " + source;
    
        // construct the attribute definitions
        List<String> attrDefs;
        RenameMap rename;

        if (project != null)
        {
            // add the source and replace project with this table scan
            List<Attribute> renamed = new ArrayList<Attribute>();
            attrDefs = new ArrayList<String>();
            for (Attribute att : mHeading.getAttributes())
            {
                Attribute n = att.getCloneNewSource(outerSource, true);
                renamed.add(n);
                attrDefs.add(n.toString());
            }
            mHeading = new HeadingImpl(renamed);
            rename = new SimpleRenameMap(renamed, mHeading.getAttributes());
            project.getParent().replaceChild(project, thisScan);
            project.disconnect();
        }
        else
        {
            // need to add the correlation statement for one of the attributes
            List<Attribute> renamed = new ArrayList<Attribute>();
            attrDefs = new ArrayList<String>();
            for (Attribute a : loj.getHeading().getAttributes())
            {
                Attribute n = a.getCloneNewSource(outerSource, true);
                renamed.add(n);
                if (a.equals(subqAttr))
                {
                    attrDefs.add("(" + corrStmnt + ")");
                }
                else
                {
                    attrDefs.add(n.toString());
                }
            }
            // add the new source name
            mHeading = new HeadingImpl(renamed);
            rename = new SimpleRenameMap(renamed, loj.getHeading().getAttributes());
            loj.getParent().replaceChild(loj, thisScan);
        }
        rename.setAttributeMatchMode(AttributeMatchMode.NO_TYPE);
        if (loj != null) loj.disconnect();
        insertRename(rename, thisScan);
        pullUpRename(thisScan);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<Attribute> headingAttrs = mHeading.getAttributes();
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(attrDefs.get(i));
            sb.append(" AS ");
            Attribute att = headingAttrs.get(i);
            sb.append(att.getName());
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
        sb.append(" FROM (");
        sb.append(mQueryStatement); 
        sb.append(") AS ");
        sb.append(outerSource);

        mQueryStatement = sb.toString();
    }

    /**
     * Add subquery in the WHERE clause.
     * 
     * @param subQuery
     *            subquery
     * @param thisScan
     *            scan operator of this query
     * @param predicate
     *            predicate with an attribute to be replaced by a subquery
     * @param renameMap
     *            rename map - may be needed for correlated subqueries where
     *            there is rename operator on the non-correlated side. Should be
     *            set to <code>null</code> where no renames are required.
     * @param subqAttr
     *            attribute to be replaced by a subquery
     */
    void addWhereSubquery(
            TableScanQuery subQuery,
            Operator thisScan,
            Predicate predicate, 
            Map<String, String> renameMap,
            Attribute subqAttr)
    {
        mSupportsFilteredTableScan = false;

        String outerSource = ExtendedTableScanImplosionOptimiser.nextID("table");
        List<Attribute> headingAttrs = mHeading.getAttributes();
        List<String> oldAttrs = new ArrayList<String>(headingAttrs.size());
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<Attribute> renamed = new ArrayList<Attribute>(headingAttrs.size());
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            // add the table source to the attributes
            Attribute newAttr = headingAttrs.get(i).getCloneNewSource(outerSource, true);
            sb.append(newAttr);
            renamed.add(newAttr);
            // keep track of the original attribute list because we need it later 
            oldAttrs.add(headingAttrs.get(i).toString());
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
        mHeading = new HeadingImpl(renamed);
    
        // add the correlated table scan
        String corrStmnt = subQuery.getSQLQuery();
        if (renameMap != null)
        {
            for (String s : renameMap.keySet())
            {
                String ren = renameMap.get(s);
                // look for the match in the old attribute list 
                // but use the renamed attribute in the replacement
                int index = oldAttrs.indexOf(ren);
                if (index >= 0)
                {
                    ren = renamed.get(index).toString();
                }
                corrStmnt = corrStmnt.replaceAll("\\b" + s + "\\b", ren);
            }
        }
        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        corrStmnt = "SELECT " + source + "." + subqAttr.toString()
            + " FROM (" + corrStmnt + ") AS " + source;
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(outerSource);
        
        // add the source in the WHERE clause predicate
        ExpressionUtils.renameUsedAttributes(
                predicate.getExpression(), 
                new SimpleRenameMap(headingAttrs, renamed));
        String wherePred = predicate.toString().replace(
                subqAttr.toString(), "(" + corrStmnt + ")");
    
        sb.append(" WHERE ");
        sb.append(wherePred);
        mQueryStatement = sb.toString();
        
        // insert a rename operator as a parent of this table scan
        // so that the heading (with the new source) is translated into what the 
        // parent is expecting
        RenameMap rename = new SimpleRenameMap(renamed, headingAttrs);
        insertRename(rename, thisScan);
    }

    /**
     * Adds outer join clause.
     * 
     * @param joinType
     *            type of outer join
     * @param joinPredicate
     *            join predicate
     * @param rightQuery
     *            right query
     * @param heading
     *            result heading
     */
    void addOuterJoin(OperatorID joinType, Predicate joinPredicate,
        TableScanQuery rightQuery, Heading heading)
    {
        mSupportsFilteredTableScan = false;

        String left = ExtendedTableScanImplosionOptimiser.nextID("table");
        String right = ExtendedTableScanImplosionOptimiser.nextID("table");
        List<Attribute> headingAttrs = heading.getAttributes();
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(headingAttrs.get(i).getName());
            if (i < headingAttrs.size() - 1)
            sb.append(", ");
        }
    
        String outerJoin = "";
        if (joinType == OperatorID.LEFT_OUTER_JOIN)
            outerJoin = " LEFT OUTER JOIN ";
        else if (joinType == OperatorID.RIGHT_OUTER_JOIN)
            outerJoin = " RIGHT OUTER JOIN ";
        else if (joinType == OperatorID.FULL_OUTER_JOIN)
            outerJoin = " FULL OUTER JOIN ";
        else
            throw new IllegalArgumentException(
                "Expected join type to be one of: " +
                "LEFT_OUTER_JOIN, RIGHT_OUTER_JOIN, FULL_OUTER_JOIN");
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(left);
        sb.append(outerJoin);
        sb.append("(");
        sb.append(rightQuery.getSQLQuery());
        sb.append(") AS ");
        sb.append(right);
        sb.append(" ON ");
        // insert correct table names in the join predicate
        joinPredicate.getExpression().accept(
                new SetSourceExpressionVisitor(
                        new SetJoinSourceVisitor(left, right, mHeading.getAttributes())));
        sb.append(joinPredicate.toString());
        mQueryStatement = sb.toString();
    
        mHeading = new HeadingImpl(headingAttrs);
    }
    
    /**
     * Add aggregate function without GROUP BY clause.
     * 
     * @param aggregates
     *            a list of aggregate function
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addScalarGroupBy(ScalarGroupByOperator scalarGB)
    {
        mSupportsFilteredTableScan = false;

        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        addSourceToHeading(source, scalarGB.getChild(0));
        List<Function> aggregates = scalarGB.getAggregates();
        Heading heading = scalarGB.getHeading();
        List<Attribute> headingAttrs = heading.getAttributes();
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(aggregates.get(i).toSQL());
            sb.append(" AS ");
            sb.append(headingAttrs.get(i).getName());
            if (i < headingAttrs.size() - 1)
            sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(source);
        mQueryStatement = sb.toString();
        mHeading = new HeadingImpl(scalarGB.getHeading());
    }

    /**
     * Add filter using WHERE clause.
     * 
     * @param predicate
     *            predicate to add
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addSelect(SelectOperator select)
    {
        mSupportsFilteredTableScan = false;

        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        addSourceToHeading(source, select.getChild(0));
        Predicate predicate = select.getPredicate();
        List<Attribute> headingAttrs = select.getHeading().getAttributes();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(headingAttrs.get(i));
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(source);
        sb.append(" WHERE ");
        sb.append(predicate.toString());
        mQueryStatement = sb.toString();
        mHeading = new HeadingImpl(select.getHeading());
    }

    /**
     * Add as product.
     * 
     * @param query
     *            query to add
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addProduct(ProductOperator product, TableScanOperator otherTS)
    {
        mSupportsFilteredTableScan = false;

        String left = ExtendedTableScanImplosionOptimiser.nextID("table");
        String right = ExtendedTableScanImplosionOptimiser.nextID("table");
        String subquery = otherTS.getQuery().getSQLQuery();
        
        // replace the product operator with this table scan
        addSourceToHeading(left, right, product);
        otherTS.disconnect();
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");

        List<Attribute> headingAttrs = mHeading.getAttributes();
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(headingAttrs.get(i));
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(left);
        sb.append(", (");
        sb.append(subquery);
        sb.append(") AS ");
        sb.append(right);
        mQueryStatement = sb.toString();
    }

    /**
     * Add join.
     * 
     * @param predicate
     *            join predicate
     * @param query
     *            query to join with
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addJoin(InnerThetaJoinOperator join, TableScanOperator otherTS)
    {
        mSupportsFilteredTableScan = false;

        String left = ExtendedTableScanImplosionOptimiser.nextID("table");
        String right = ExtendedTableScanImplosionOptimiser.nextID("table");
        String subquery = otherTS.getQuery().getSQLQuery();
        
        // change the source in the join predicate expression
        join.getPredicate().getExpression().accept(
                new SetSourceExpressionVisitor(
                        new SetJoinSourceVisitor(
                                left, right, mHeading.getAttributes())));
        String predicate = join.getPredicate().toString();

        // replace the join operator with this table scan operator
        addSourceToHeading(left, right, join);
        otherTS.disconnect();
        
        List<Attribute> headingAttrs = mHeading.getAttributes();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            sb.append(headingAttrs.get(i));
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(left);
        sb.append(" JOIN (");
        sb.append(subquery);
        sb.append(") AS ");
        sb.append(right);
        sb.append(" ON ");
        sb.append(predicate);
        mQueryStatement = sb.toString();
    }

    /**
     * Add GROUP BY clause.
     * 
     * @param groupingAttributes
     *            list of grouping attributes
     * @param aggregates
     *            list of aggreates
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addGroupBy(GroupByOperator groupBy)
    {
        mSupportsFilteredTableScan = false;

        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        addSourceToHeading(source, groupBy.getChild(0));
        List<Attribute> groupingAttributes = groupBy.getGroupingAttributes();
        List<Function> aggregates = groupBy.getAggregates();
        List<Attribute> headingAttrs = groupBy.getHeading().getAttributes();
        int grpAttrCnt = groupingAttributes.size();
    
        StringBuilder sb = new StringBuilder();
        StringBuilder gbsb = new StringBuilder();
        sb.append("SELECT ");
    
        // This depends on grouping attrs being followed by aggregates
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            if (i < grpAttrCnt)
            {
                String fullName = groupingAttributes.get(i).toString();
                sb.append(fullName);
                sb.append(" AS ");
                sb.append(headingAttrs.get(i).getName());
                gbsb.append(fullName);
                if (i < grpAttrCnt - 1)
                    gbsb.append(", ");
            }
            else
            {
                sb.append(aggregates.get(i - grpAttrCnt).toSQL());
                sb.append(" AS ");
                sb.append(headingAttrs.get(i).getName());
            }
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(source);
        sb.append(" GROUP BY ");
        sb.append(gbsb);
        mHeading = groupBy.getHeading();
        mQueryStatement = sb.toString();
    }

    /**
     * Add projection.
     * 
     * @param attrDefs
     *            select list attribute definitions
     * @param heading
     *            expected TABLE_SCAN heading
     */
    void addProject(ProjectOperator project)
    {
        mSupportsFilteredTableScan = false;

        String source = ExtendedTableScanImplosionOptimiser.nextID("table");
        addSourceToHeading(source, project.getChild(0));
        List<String> attrDefs = project.getAttributeDefs();
        List<Attribute> headingAttrs = project.getHeading().getAttributes();
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < headingAttrs.size(); i++)
        {
            Attribute att = headingAttrs.get(i);
            String def = attrDefs.get(i);
            sb.append(def);
            sb.append(" AS ");
            sb.append(att.getName());
            if (i < headingAttrs.size() - 1) sb.append(", ");
        }
    
        sb.append(" FROM (");
        sb.append(mQueryStatement);
        sb.append(") AS ");
        sb.append(source);
        mQueryStatement = sb.toString();
        mHeading = new HeadingImpl(project.getHeading());
    }
    
    private void addSourceToHeading(
            String left, String right, 
            AbstractOperator operator)
    {
        List<Attribute> orig = new ArrayList<Attribute>(operator.getHeading().getAttributes());        
        List<Attribute> renamed = new ArrayList<Attribute>(orig.size());
        
        // find the correct table for each attribute
        for (Attribute att : operator.getHeading().getAttributes())
        {
            if (mHeading.contains(att))
            {
                Attribute newAtt = att.getCloneNewSource(left, true);
                renamed.add(newAtt);
            }
            else
            {
                Attribute newAtt = att.getCloneNewSource(right, true);
                renamed.add(newAtt);
            }
        }
        
        // now rename the attributes of this table scan operator
        RenameMap rename = new SimpleRenameMap(renamed, orig);
        mHeading = new HeadingImpl(renamed);
        
        // swap this table scan with the join operator and disconnect the join
        Operator thisScan = operator.getChild(0);
        operator.getParent().replaceChild(operator, thisScan);
        operator.disconnect();
        
        // insert the rename operator above this table scan and pull up
        insertRename(rename, thisScan);
        pullUpRename(thisScan);
    }

    private void addSourceToHeading(String source, Operator operator)
    {
        List<Attribute> renamed = new ArrayList<Attribute>();
        for (Attribute att : mHeading.getAttributes())
        {
            renamed.add(att.getCloneNewSource(source, true));
        }
        RenameMap rename = new SimpleRenameMap(renamed, mHeading.getAttributes());
        rename.setAttributeMatchMode(AttributeMatchMode.NO_TYPE);
        mHeading = new HeadingImpl(renamed);
        insertRename(rename, operator);
        pullUpRename(operator);
    }
    
    private void insertRename(RenameMap rename, Operator operator)
    {
        RenameOperator renameOp = new RenameOperator(rename);
        renameOp.setParent(operator.getParent());
        operator.getParent().replaceChild(operator, renameOp);
        operator.setParent(renameOp);
        renameOp.setChild(0, operator);
        try
        {
            renameOp.getChild(0).update();
            renameOp.update();
        }
        catch (LQPException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void pullUpRename(Operator operator)
    {
        try
        {
            // pull up the rename
            Operator root = operator;
            while (root.getParent() != null)
            {
                root = root.getParent();
            }
            RenamePullUpOptimiser pullUpOptimiser = new RenamePullUpOptimiser();
            pullUpOptimiser.optimise(root, null, null, null);
        }
        catch (LQPException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * Add disambiguating rename.
     * 
     * @param renameMap
     *            rename map
     */
    void addRename(RenameMap renameMap)
    {
        List<Attribute> orig = renameMap.getOriginalAttributeList();
        List<Attribute> renm = renameMap.getRenamedAttributeList();
        List<Attribute> newHeading = new ArrayList<Attribute>();
        
        for (int i=0; i<orig.size(); ++i)
        {
            mHeadingToLogicalAttribute.put(orig.get(i), renm.get(i));
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
    
        // Check query type.
        //
        int queryType = SQLStatementType.getStatementType(mQueryStatement);
        boolean sfw = SQLStatementType.isSelectFromWhere(queryType)
            || SQLStatementType.isSelectFrom(queryType);
        boolean sfw_star = SQLStatementType.isSelectStar(queryType);
        
        Pattern p = Pattern.compile("SELECT (.*) (FROM .*)");
        Matcher m = p.matcher(mQueryStatement);
    
        // If we have SFW query then we can add to current select list else
        // we need to use a derived table.
        //
        if (sfw & m.matches())
        {
            String[] cols = m.group(1).split(",");
            String fromList = m.group(2);
    
            p = Pattern.compile("(.*) AS .*");
            
            for (int i = 0; i < renm.size(); i++)
            {
                if (sfw_star)
                {
                    sb.append(renm.get(i).getSource()).append(".");
                    sb.append(renm.get(i).getName());
                }
                else
                {
                    String c = cols[i].trim();
                    m = p.matcher(c);
                    if (m.matches())
                    {
                        sb.append(m.group(1));
                    }
                    else
                    {
                        sb.append(c);
                    }
                }
                sb.append(" AS ");
                sb.append(orig.get(i).getName());
        
                if (i < renm.size() - 1) sb.append(", ");
                newHeading.add(orig.get(i));
            }
            mQueryStatement = sb.toString() + ' ' + fromList;  
        }
        else
        {
            String table = ExtendedTableScanImplosionOptimiser.nextID("table");
            for (int i = 0; i < renm.size(); i++)
            {
                sb.append(table).append(".");
                sb.append(renm.get(i).getName());
                sb.append(" AS ");
                sb.append(orig.get(i).getName());
                if (i < renm.size() - 1)
                {
                    sb.append(", ");
                }
                Attribute o = orig.get(i);
                newHeading.add(new AttributeImpl(o.getName(), o.getType(), table));
            }
    
            sb.append(" FROM (");
            sb.append(mQueryStatement);
            sb.append(") AS ");
            sb.append(table);
            mQueryStatement = sb.toString();
        }
        mHeading = new HeadingImpl(newHeading);
    }

    @Override
    public Heading getHeading() throws TableNotFoundException
    {
        return mHeading;
    }

    @Override
    public String getPhysicalDatabaseSQLQuery(DataNode dataNode)
    {
        // This is a bit brute force - but we should be OK as we have unique
        // attribute names except for the deepest "initial queries".
        //
        // TODO: Think if this is sufficiently safe.
        //
        try
        {
            String pQuery = getSQLQuery();
            Set<String> tableNames = ASTUtil.extractTableNames(
                    SQLQueryParser.getInstance().parseSQL(mQueryStatement));
    
            for (String name : tableNames)
            {
                String pName = mDataDictionary.getOriginalTableName(name, dataNode);
                pQuery = pQuery.replaceAll("\\b" + name + "\\b", pName);
                pQuery = pQuery.replaceAll("\\b" + name + "\\.", pName);
            }
            return pQuery;
        }
        catch (TableNotFoundException e)
        {
            // Can not recover from this
            throw new RuntimeException(e);
        }
        catch (SQLParserException e)
        {
            // Can not recover from this
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLQuery()
    {
        String sql = mQueryStatement;
        if (mSortAttribute != null) {
            sql = sql + " ORDER BY " + mSortAttribute.getName();
        }
        return sql;
    }

    @Override
    public String getTableName()
    {
        // TODO: This is a bit misleading as we may not have a single table after
        // implosion - we use this name to find all table schemas exposed by a
        // DataNode. We could just ask for the data node.
        //
        return mOriginalScanQuery.getTableName();
    }

    @Override
    public void setDataDictionary(DataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
    }

    @Override
    public void setTableName(String tableName)
    {
        // TODO: Consider removing from the interface
        //
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPredicate(Predicate predicate)
    {
        // TODO: Consider removing from the interface
        //
        throw new UnsupportedOperationException();
    }

    public void setSort(Attribute sortAttr) throws UnsupportedOperationException {
        mSortAttribute = sortAttr;
     }

    @Override
    public Attribute getPhysicalAttribute(
        Attribute attribute, DataNode dataNode)
    {
        // Hash mapping to find attribute is too strict
        Attribute logicalAttribute = null;
        for (Attribute a : mHeadingToLogicalAttribute.keySet())
        {
            if (a.equals(attribute,AttributeMatchMode.NO_TYPE))
            {
                logicalAttribute = mHeadingToLogicalAttribute.get(a);
            }
        }
        if (logicalAttribute == null)
        {
            logicalAttribute = attribute;
        }
        return mOriginalScanQuery.getPhysicalAttribute(
            logicalAttribute, dataNode);
    }
    
    public boolean supportsFilteredTableScan()
    {
        return mSupportsFilteredTableScan;
    }
}
