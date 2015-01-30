// Copyright (c) The University of Edinburgh, 2008-2012.
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

package uk.org.ogsadai.dqp.lqp.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrRenameToPhysicalAttrArithmeticExprVisitor;
import uk.org.ogsadai.expression.visitors.AttrRenameToPhysicalAttrExpressionVisitor;
import uk.org.ogsadai.resource.dataresource.dqp.DQPInternalException;

/**
 * Simple implementation of a SELECT, PROJECT, JOIN table scan query.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleSelectProjectJoinTableScanQuery 
    implements SelectProjectJoinTableScanQuery 
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";
    
    /** Logger for this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleSelectProjectJoinTableScanQuery.class);

    /** Table names used in a query. */
    private List<String> mTableNames = new ArrayList<String>();
    
    private List<ProjectExpression> mProjectExpressions = 
        new ArrayList<ProjectExpression>();

    /** A list of predicates. */
    private List<Predicate> mPredicateList = new ArrayList<Predicate>();
    
    /** Data dictionary. */
    private DataDictionary mDataDictionary;
    
    /** Attribute to use in the ORDER BY clause. */
    private Attribute mSortAttribute;
    
    @Override
    public void setDataDictionary(DataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
    }
    
    @Override
    public void setTableName(String tableName)
    {
        mTableNames.add(tableName);
    }
    
    @Override
    public String getTableName()
    {
        return mTableNames.get(0);
    }

    @Override
    public void addPredicate(Predicate predicate)
    {
        mPredicateList.add(predicate);
    }
    
    @Override
    public void setProjectExpressions(List<ProjectExpression> expressions)
    {
        mProjectExpressions = expressions;
    }
    
    @Override
    public void setProjectAttributes(List<Attribute> attributes)
    {
        mProjectExpressions.clear();
        for (Attribute attr : attributes)
        {
            mProjectExpressions.add(new AttributeProjectExpression(attr));
        }
    }
    
    @Override
    public void merge(SelectProjectJoinTableScanQuery spj) 
        throws TableNotFoundException, AttributeNotFoundException
    {
        SimpleSelectProjectJoinTableScanQuery sspj = 
            (SimpleSelectProjectJoinTableScanQuery) spj;
        
        mTableNames.addAll(sspj.mTableNames);
        mPredicateList.addAll(sspj.mPredicateList);
        
        if (mProjectExpressions.isEmpty())
        {
            addAllAttributes(getHeading().getAttributes());
        }
        addAllAttributes(spj.getHeading().getAttributes());
    }

    @Override
    public Heading getHeading() 
        throws TableNotFoundException, AttributeNotFoundException
    {
        if (mProjectExpressions.size() == 0)
        {
            Heading physicalHead = 
                mDataDictionary.getHeading(mTableNames.get(0));
            return new HeadingImpl(physicalHead);
        }
        else
        {
            List<Attribute> heading = new LinkedList<Attribute>();
            
            for (ProjectExpression pe : mProjectExpressions)
            {
                if (pe instanceof ArithmeticExpressionProjectExpression)
                {
                    ArithmeticExpressionProjectExpression aepe = 
                        (ArithmeticExpressionProjectExpression) pe;
                    heading.add(aepe.getHeading());
                }
                else
                {
                    AttributeProjectExpression ape =
                        (AttributeProjectExpression) pe;
                    Attribute attr = ape.getAttribute();
                    heading.add(mDataDictionary.getAttribute(attr));
                }
            }
            return new HeadingImpl(heading);
        }
    }

    @Override
    public String getPhysicalDatabaseSQLQuery(DataNode dataNode)
    {
        try
        {
            
            List<String> localTableNames = new ArrayList<String>();
            
            // collect all physical tables from the query 
            for (String name : mTableNames)
            {
                String localName = 
                    mDataDictionary.getOriginalTableName(name, dataNode);
                localTableNames.add(localName);
            }
            
            List<ProjectExpression> localProjectExpressions = 
                new ArrayList<ProjectExpression>(mProjectExpressions.size());
            
            // If we have no project expressions add all the local attributes
            Iterator<String> iterator = localTableNames.iterator();
            if (mProjectExpressions.isEmpty())
            {
                for (String name : mTableNames)
                {
                    String localName = iterator.next();
                    for (Attribute attr : mDataDictionary.getHeading(name).getAttributes())
                    {
                        Attribute localAttr = 
                            attr.getCloneNewSource(localName, false);
                        localProjectExpressions.add(
                                new AttributeProjectExpression(localAttr));
                    }
                }
            }
            
            // Set source table names of used attributes to the 
            // physical database table in each project expression
            for (ProjectExpression pe : mProjectExpressions)
            {
                if (pe instanceof ArithmeticExpressionProjectExpression)
                {
                    ArithmeticExpressionProjectExpression aepe = 
                        (ArithmeticExpressionProjectExpression) pe;
                    
                    ArithmeticExpression exprClone = 
                        ExpressionUtils.getClone(aepe.getExpression());
                    localiseAttributes(exprClone, dataNode);
                    localProjectExpressions.add(
                        new ArithmeticExpressionProjectExpression(
                            exprClone, 
                            aepe.getAlias(),
                            aepe.getHeading()));
                }
                else
                {
                    AttributeProjectExpression ape =
                        (AttributeProjectExpression) pe;
                    
                    Attribute physicalAttr = 
                        getPhysicalAttribute(ape.getAttribute(), dataNode);
                    localProjectExpressions.add(
                            new AttributeProjectExpression(physicalAttr));
                }
            }
            
            // Same for predicates
            List<Predicate> localPredicateList = new ArrayList<Predicate>();
            for(Predicate p : mPredicateList)
            {
                Predicate predClone = p.getClone();
                localiseAttributes(predClone.getExpression(), dataNode);
                localPredicateList.add(predClone);
            }

            return generateSQL(
                localTableNames, 
                localProjectExpressions,
                localPredicateList, 
                mSortAttribute);
        }
        catch(TableNotFoundException e)
        {
            // This should not occur.
            throw new DQPInternalException(e);
        }
    }

    @Override
    public Attribute getPhysicalAttribute(Attribute attribute, DataNode dataNode)
    {
        Attribute result = null;
        
        try
        {
            String localName = 
                mDataDictionary.getOriginalTableName(
                        attribute.getSource(), dataNode);
            
            result = new AttributeImpl(
                attribute.getName(),
                attribute.getType(),
                localName,
                attribute.isKey());
        }
        catch (Exception e)
        {
            // Ignore it. Will return null.
        }
        return result;
    }
    
    @Override
    public boolean supportsFilteredTableScan()
    {
        return true;
    }

    @Override
    public String getSQLQuery() 
    {
        return generateSQL(
            mTableNames, mProjectExpressions, mPredicateList, mSortAttribute);
    }

    @Override
    public void setSort(Attribute sortAttribute)
        throws UnsupportedOperationException 
    {
        mSortAttribute = sortAttribute;
    }
    
    /**
     * Generates the SQL query.
     * 
     * @param tableNames
     *    names of the tables in include in the query.  This list must include
     *    at least one table name.
     *    
     * @param expressions
     *    the expressions to project.  If the list is empty then the returned
     *    query will use '*' for the attribute list.
     *    
     * @param predicateList
     *    the SELECT predicates.
     *    
     * @return SQL query generated.
     */
    private static String generateSQL(
        List<String> tableNames,
        List<ProjectExpression> expressions,
        List<Predicate> predicateList,
        Attribute sortAttribute)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        if (expressions.size() == 0)
        {
            sb.append("*");
        }
        else
        {
            boolean firstAttr = true;
            
            for (ProjectExpression pe : expressions)
            {
                if (pe instanceof ArithmeticExpressionProjectExpression)
                {
                    ArithmeticExpressionProjectExpression aepe = 
                        (ArithmeticExpressionProjectExpression) pe;
                    
                    if (!firstAttr) sb.append(", ");
                    firstAttr = false;
                    sb.append(
                        ExpressionUtils.generateSQL(aepe.getExpression()));
                    
                    String alias = aepe.getAlias();
                    if (alias != null)
                    {
                        sb.append(" AS ");
                        sb.append(alias);
                    }
                }
                else
                {
                    AttributeProjectExpression ape =
                        (AttributeProjectExpression) pe;
                    if (!firstAttr) sb.append(", ");
                    firstAttr = false;
                    sb.append(ape.getAttribute());
                }
            }
        }

        sb.append(" FROM ");
        for (int i = 0; i < tableNames.size(); i++)
        {
            sb.append(tableNames.get(i));
            if (i < tableNames.size() - 1)
            {
                sb.append(", ");
            }
        }

        if (predicateList.size() > 0)
        {
            sb.append(" WHERE ");
            
            for (int i = 0; i < predicateList.size(); i++)
            {
                sb.append(" ( ");
                sb.append(predicateList.get(i).toString());
                sb.append(" ) ");
                if (i < predicateList.size() - 1)
                {
                    sb.append(" AND ");
                }
            }
        }
        if (sortAttribute != null) {
            sb.append(" ORDER BY " + sortAttribute.getName());
        }
        return sb.toString();
    }
    
    private void addAllAttributes(List<Attribute> attrs)
    {
        for( Attribute attr : attrs)
        {
            mProjectExpressions.add(new AttributeProjectExpression(attr));
        }
    }
    
    private void localiseAttributes(
            ArithmeticExpression expression,
            DataNode dataNode)
    {
        AttrRenameToPhysicalAttrArithmeticExprVisitor v = 
            new AttrRenameToPhysicalAttrArithmeticExprVisitor(
                    mDataDictionary, dataNode);
        expression.accept(v);
    }
    
    private void localiseAttributes(
            Expression expression,
            DataNode dataNode)
    {
        AttrRenameToPhysicalAttrExpressionVisitor v = 
            new AttrRenameToPhysicalAttrExpressionVisitor(
                    mDataDictionary, dataNode);
        expression.accept(v);
    }
}
