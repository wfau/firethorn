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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.LQPBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotPartOfAggregationException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.util.ASTConstants;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrRenameArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrReplaceArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * The PROJECT operator.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ProjectOperator extends UnaryOperator
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2008-2012";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ProjectOperator.class);

    /** Holds rename map defined by aliases. */
    private RenameMap mRenameMap;

    /** A list of expressions defining projected attributes. */
    private List<ArithmeticExpression> mAttributeExprList;
    
    /** Names of derived attributes (or other renamed attributes). */
    private List<String> mDerivedAttrAliases; 
    
    /**
     * Constructor.
     */
    private ProjectOperator()
    {
        mID = OperatorID.PROJECT;
    }

    /**
     * Constructs a PROJECT operator connected to a child.
     * 
     * @param childOperator
     *            child operator
     * @param selectListAST
     *            select list abstract syntax tree node (SQL)
     * @param functionRepository
     *            function repository, or <tt>null</tt> if functions are not
     *            permitted.
     * @throws LQPException
     *             if there is a problem with operator building
     */
    public ProjectOperator(
        Operator childOperator, 
        CommonTree selectListAST,
        FunctionRepository functionRepository)
        throws LQPException
    {
        this();
        setChild(0, childOperator);

        mAttributeExprList = new ArrayList<ArithmeticExpression>();
        mDerivedAttrAliases = new ArrayList<String>();
        
        mRenameMap = new SimpleRenameMap();

        SQLQueryParser qp = SQLQueryParser.getInstance();

        for (int i = 0; i < selectListAST.getChildCount(); i++)
        {
            String alias = null;
            CommonTree column = (CommonTree) selectListAST.getChild(i);

            // Skip DISTINCT and ALL tokens
            if (column.getText().toUpperCase().equals(ASTConstants.DISTINCT_TOKEN)
                || column.getText().equals(ASTConstants.ALL_TOKEN))
            {
                continue;
            }

            // Get alias if exists
            if (column.getChildCount() > 1)
            {
                alias = column.getChild(1).getText();
            }

            // Move one level down
            CommonTree child = (CommonTree) column.getChild(0);

            // Deal with SELECT *
            if (child.getText().equals(ASTConstants.STAR_TOKEN))
            {
                for (Attribute attrib : mChildOperator.getHeading()
                    .getAttributes())
                {
                    if (!attrib.isTemporary())
                    {
                        try
                        {
                            mAttributeExprList.add(ArithmeticExpressionFactory
                                .buildArithmeticExpression(
                                    qp.parseSQLForDerivedColumn(
                                        attrib.toString()),
                                    functionRepository));
                            mDerivedAttrAliases.add(null);
                        }
                        // ExpressionException, SQLParserException
                        catch (Exception e)
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("Trouble with: " + attrib.toString());
                            }
                            throw new LQPException(e);
                        }
                    }
                }

                // that's it
                break;
            }
            else if (child.getText().equals(ASTConstants.TABLECOLUMN_TOKEN))
            {
                // we have direct table column access
                try
                {
                    ArithmeticExpression expr = 
                        ArithmeticExpressionFactory.buildArithmeticExpression(
                            column, functionRepository);

                    if (mAttributeExprList.contains(expr))
                    {
                        // Multiple selections of the same attribute, treat
                        // all other than the first as derived attributes
                        addDerivedAttribute(expr, alias);
                    }
                    else
                    {
                        mAttributeExprList.add(expr);
                        mDerivedAttrAliases.add(null);
                    }
                    
                    String tableName;
                    String tableSource = null;
                    if (child.getChildCount() == 2)
                    {
                        tableSource = child.getChild(0).getText();
                        tableName = child.getChild(1).getText();
                    }
                    else
                    {
                        tableName = child.getChild(0).getText();
                    }
                    Attribute originalAttr =
                        new AttributeImpl(tableName, -1, tableSource);
                    if (alias != null)
                    {
                        mRenameMap.add(originalAttr, 
                                new AttributeImpl(alias, -1, tableSource));
                    }
                    else
                    {
                        mRenameMap.add(originalAttr, originalAttr);
                    }
                }
                catch (ExpressionException e)
                {
                    throw new LQPException(e);
                }
            }
            else
            {
                // we have derived column
                try
                {
                    ArithmeticExpression expr = 
                        ArithmeticExpressionFactory.buildArithmeticExpression(
                                column, functionRepository);
                    addDerivedAttribute(expr, alias);
                }
                catch (ExpressionException e)
                {
                    throw new LQPException(e);
                }
            }
        }

        // If there was no renaming - set to null
        if (mRenameMap.getRenamedAttributeList().equals(
            mRenameMap.getOriginalAttributeList()))
        {
            mRenameMap = null;
        }

        localUpdate();
    }
    
    /**
     * Adds a derived attribute as the new attribute in the project.
     * 
     * @param expr   expression for the derived attribute
     * @param alias  alias for the derived attribute, or <tt>null</tt> if none 
     *               is specified.
     */
    private void addDerivedAttribute(ArithmeticExpression expr, String alias)
    {
        if (alias == null)
        {
            alias = LQPBuilder.getNextID();
        }

        mAttributeExprList.add(expr);
        mDerivedAttrAliases.add(alias);
        mRenameMap.add(
            new AttributeImpl(alias), new AttributeImpl(alias));
    }

    /**
     * Constructs a PROJECT operator connected to a child.
     * 
     * @param childOperator
     *            child operator
     * @param attributes
     *            an ordered collection of attributes
     * @throws LQPException
     *             if the given attributes are not all contained in the child
     *             operator's head.
     */
    public ProjectOperator(
        Operator childOperator, Collection<Attribute> attributes) 
        throws LQPException
    {
        this();
        setChild(0, childOperator);
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("In ProjectOperator contructor that takes attributes");
            LOG.debug("Num attributes: " + attributes.size());
        }

        mAttributeExprList = new ArrayList<ArithmeticExpression>();
        mDerivedAttrAliases = new  ArrayList<String>();
        mRenameMap = null;
        
        for (Attribute attr : attributes)
        {
            mAttributeExprList.add(new TableColumn(attr));
            mDerivedAttrAliases.add(null);
        }

        localUpdate();
    }

    /**
     * Constructs a PROJECT with the given expressions (and aliases) and the
     * given child.
     * 
     * @param childOperator  child operator of the PROJECT
     * @param expressions    expressions to be projected
     * @param aliases        aliases for the expressions. This list must be
     *                       the same size as the expressions list. Any 
     *                       expression that does not have an alias must have
     *                       a <tt>null<tt> in its corresponding entry in the
     *                       aliases list
     *                        
     * @throws LQPException if logical query plan rooted at this PROJECT is
     *                      is invalid.
     */
    public ProjectOperator(
        Operator childOperator,
        List<ArithmeticExpression> expressions,
        List<String> aliases)
            throws LQPException
    {
        this();
        setChild(0, childOperator);
        
        if (expressions.size() != aliases.size())
        {
            throw new IllegalArgumentException(
                "expressions list and aliases list must be the same size");
        }
        
        mAttributeExprList = new ArrayList<ArithmeticExpression>();
        mDerivedAttrAliases = new ArrayList<String>();
        mRenameMap = new SimpleRenameMap();

        for (int i=0; i<expressions.size(); ++i)
        {
            String alias = aliases.get(i);
            if (alias != null)
            {
                addDerivedAttribute(expressions.get(i), alias);
            }
            else
            {
                mAttributeExprList.add(expressions.get(i));
                mDerivedAttrAliases.add(alias);
            }
        } 

        // If there was no renaming - set to null
        if (mRenameMap.getRenamedAttributeList().equals(
            mRenameMap.getOriginalAttributeList()))
        {
            mRenameMap = null;
        }

        update();
    }
    
    
    /**
     * Gets the ordered list of attribute expressions.
     * 
     * @return attribute expressions
     */
    public List<ArithmeticExpression> getAttributeExpressions()
    {
        return mAttributeExprList;
    }
    
    public List<String> getAttributeAliases()
    {
        return mDerivedAttrAliases;
    }
    
    /**
     * Gets an ordered list of expressions defining all attributes.
     * 
     * @return ordered list of expressions.
     */
    public List<String> getAttributeDefs()
    {
        List<String> attributeDefList = new ArrayList<String>();
        SQLGenArithmeticExpressionVisitor visitor = 
            new SQLGenArithmeticExpressionVisitor();
        
        for (ArithmeticExpression expr : mAttributeExprList)
        {
            expr.accept(visitor);
            attributeDefList.add(visitor.getSQLString());
            visitor.reset();
        }
        return attributeDefList;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();
        localUpdate();
    }

    /**
     * Performs update without calling update on a child.
     * 
     * @throws LQPException
     */
    private void localUpdate() throws LQPException
    {
        List<Attribute> headAttributes = new ArrayList<Attribute>();
        int i=0;
        
        for (ArithmeticExpression expr : mAttributeExprList)
        {
            String derivedAttrAlias = mDerivedAttrAliases.get(i++);
            if (derivedAttrAlias == null)
            {
                // A non-derived attribute
                
                TableColumn tc = (TableColumn) expr;
                
                Attribute headMatchAttr = null;
                
                try
                {
                    headMatchAttr = 
                        mChildOperator.getHeading().getMatchingAttribute(
                            new AttributeImpl(tc.getName(), tc.getSource()));
                }
                catch(AttributeNotFoundException e)
                {
                    throw improveAttributeNotFoundException(e);
                }

                headAttributes.add(headMatchAttr);
            }
            else
            {
                // Figure out the type this also does type validation
                try
                {
                    expr.configure(mChildOperator.getHeading().getTupleMetadata());
                }
                catch (TypeMismatchException e)
                {
                    e.setExpressionString(expr.toString());
                    throw new LQPException(e);
                }
                
                int type = expr.getMetadata().getType();                
                headAttributes.add(
                    new AttributeImpl(derivedAttrAlias, type, null));
            }
            
            // use fully qualified attribute names in expressions
            ExpressionUtils.renameAttrWithMatching(expr,
                    mChildOperator.getHeading().getAttributes());
        }
        mOperatorHeading = new HeadingImpl(headAttributes);

        // update rename map
        if (mRenameMap != null)
        {
            List<Attribute> renamedAttrList = mRenameMap
                .getRenamedAttributeList();
            mRenameMap = new SimpleRenameMap();
            Attribute headAttr;
            for (i = 0; i < headAttributes.size(); i++)
            {
                headAttr = headAttributes.get(i);
                mRenameMap.add(headAttr, headAttr.getCloneNewName(
                    renamedAttrList.get(i).getName(), false));
            }
        }
    }

    /**
     * Gets the rename map. The rename map will be constructed after the call to
     * update().
     * 
     * @return the rename map or <tt>null</tt> if there is none.
     */
    public RenameMap getRenameMap()
    {
        return mRenameMap;
    }

    /**
     * Gets a list of the used attributes in the order they appear in the
     * project list. Duplicate attributes will not appear in the list.
     * 
     * @return the used attributes in the order they first appear in the
     *         project list.
     */
    public List<Attribute> getOrderedUsedAttributes()
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        
        for(ArithmeticExpression expr : mAttributeExprList)
        {
            expr.accept(visitor);
        }
        
        return visitor.getOrderedAttributes();
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        
        for(ArithmeticExpression expr : mAttributeExprList)
        {
            expr.accept(visitor);
        }
        Set<Attribute> usedAttrs = visitor.getAttributes();

        // The default equals() for Attributes does strict matching. Duplicate
        // in this context calls for relaxed matching.
        return AttributeUtils.removeDuplicates(usedAttrs,
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }

    /**
     * Creates a new PROJECT operator by merging with a child.
     * 
     * @return new merged PROJECT
     * @throws LQPException
     *             if there is a problem with merging operators
     */
    public ProjectOperator createMegredWithChild() throws LQPException
    {
        ProjectOperator projectChild = (ProjectOperator) mChildOperator;
        ProjectOperator mergedProject = new ProjectOperator();
        mergedProject.mAttributeExprList = 
            new ArrayList<ArithmeticExpression>();
        mergedProject.mDerivedAttrAliases = 
            new ArrayList<String>();

        // Visitor used to obtain the used expressions.
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();

        // Process each attribute in turn
        int i = 0;
        for (ArithmeticExpression expr : mAttributeExprList)
        {
            
            // Getting the user attributes of this expression
            visitor.reset();
            expr.accept(visitor);
            Set<Attribute> usedAttrSet = visitor.getAttributes();

            ArithmeticExpression newExpression = null;
            if (usedAttrSet.size() > 0)
            {
                // Need to check is these are derived in the child and if
                // so then merge that function into the expression at this
                // level
                Map<Attribute, ArithmeticExpression> attrToExprMap = 
                    new HashMap<Attribute, ArithmeticExpression>();
                
                for (Attribute attr : usedAttrSet)
                {
                    // we only need to rewrite attributes that are being
                    // derived by the child project
                    if (projectChild.isAttributeDerived(attr))
                    {
                        attrToExprMap.put(
                            attr, 
                            projectChild.getAttributeExpr(attr));
                    }
                }

                AttrReplaceArithmeticExprVisitor v = 
                    new AttrReplaceArithmeticExprVisitor(attrToExprMap);
                expr.accept(v);
                
                if(v.isResultNew())
                {
                    newExpression = v.getResult();
                }
            }
            
            boolean haveAlteredExpression;
            if (newExpression == null)
            {
                haveAlteredExpression = false;
                newExpression = 
                    CloneArithmeticExprVisitor.cloneExpression(expr);
            }
            else
            {
                haveAlteredExpression = true;
            }
            
            mergedProject.mAttributeExprList.add(newExpression);
            
            if (mDerivedAttrAliases.get(i) != null)
            {
                // This was already derived.  Keep the same name.
                mergedProject.mDerivedAttrAliases.add(
                    mDerivedAttrAliases.get(i));
            }
            else if (haveAlteredExpression)
            {
                // Did not previously have an entry in the deriverd attr list
                // but we now have a derived attr so we need to add one.
                // The original expression must have been a TableColumn
                TableColumn tc = (TableColumn) expr;
                mergedProject.mDerivedAttrAliases.add(tc.getName());
            }
            else
            {
                mergedProject.mDerivedAttrAliases.add(null);
                
            }
            
            ++i;
        }

        mergedProject.setChild(0, projectChild.getChild(0));
        mergedProject.localUpdate();
        return mergedProject;
    }

    /**
     * Returns the expression associated with a projected attribute.
     * 
     * @param attribute
     *            projected attribute
     * @return attribute expression
     */
    private ArithmeticExpression getAttributeExpr(Attribute attribute)
    {
        int attrIdx = AttributeUtils.getMatchingIndex(attribute,
                mOperatorHeading.getAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
        return mAttributeExprList.get(attrIdx);
    }
    
    /**
     * Checks if attribute is derived (is a result of arithmetic expression).
     * 
     * @param attribute
     *            attribute to test
     * @return <code>true</code> is attribute is derived, <code>false</code>
     *         otherwise
     */
    protected boolean isAttributeDerived(Attribute attribute)
    {
        return !AttributeUtils.containsMatching(attribute, mChildOperator
                .getHeading().getAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }

    /**
     * Gets a list of derived attributes.
     * 
     * @return list of derived attributes
     */
    public List<Attribute> getDerivedAttributes()
    {
        // This will need changed - it should be based on the derivedAttrAliases
        // list instead
        List<Attribute> derivedAttr = new ArrayList<Attribute>();

        for (int i=0; i<mAttributeExprList.size(); i++)
        {
            if (mDerivedAttrAliases.get(i) != null)
            {
                derivedAttr.add(mOperatorHeading.getAttributes().get(i));
            }
        }

        return derivedAttr;
    }
    
    /**
     * Creates new PROJECT operator with attributes appended to the existing
     * attributes.
     * 
     * @param attributeList
     *            list of attributes to be appended
     * @param sideIndex
     *            <code>0</code> for left, <code>1</code> for right
     * @return new PROJECT operator
     */
    private ProjectOperator createCopyAppend(
        List<Attribute> attributeList,
        int sideIndex)
    {
        ProjectOperator projectOperator = new ProjectOperator();
        
        // Make expressions for the given attributes
        List<ArithmeticExpression> attrExprList = 
            new ArrayList<ArithmeticExpression>(attributeList.size());
        List<String> derivedAttrAliasesList = 
            new ArrayList<String>(attributeList.size());

        for(Attribute attr : attributeList)
        {
            attrExprList.add(new TableColumn(attr.getName(), attr.getSource()));
            derivedAttrAliasesList.add(null);    
        }

        // Now add these expressions to the new project operator
        if (sideIndex == 0)
        {
            projectOperator.mAttributeExprList = attrExprList;
            projectOperator.mAttributeExprList.addAll(mAttributeExprList);
            
            projectOperator.mDerivedAttrAliases = derivedAttrAliasesList;
            projectOperator.mDerivedAttrAliases.addAll(mDerivedAttrAliases);
        }
        else
        {
            projectOperator.mAttributeExprList = this.mAttributeExprList;
            projectOperator.mAttributeExprList.addAll(attrExprList);
            
            projectOperator.mDerivedAttrAliases = mDerivedAttrAliases;
            projectOperator.mDerivedAttrAliases.addAll(derivedAttrAliasesList);
        }

        return projectOperator;
    }

    /**
     * Creates a disconnected copy with attributes appended on the left side of
     * a heading.
     * 
     * @param attributeList
     *            list of attributes to be appended
     * @return new PROJECT operator with appended attributes
     */
    public ProjectOperator getDisconnectedCopyAppendLeft(
        List<Attribute> attributeList)
    {
        return createCopyAppend(attributeList, 0);
    }

    /**
     * Creates a disconnected copy with attributes appended on the right side of
     * a heading.
     * 
     * @param attributeList
     *            list of attributes to be appended
     * @return new PROJECT operator with appended attributes
     */
    public ProjectOperator getDisconnectedCopyAppendRight(
        List<Attribute> attributeList)
    {
        return createCopyAppend(attributeList, 1);
    }

    @Override
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        AttrRenameArithmeticExprVisitor v = new AttrRenameArithmeticExprVisitor(
                renameMap);

        for (ArithmeticExpression e : mAttributeExprList)
        {
            e.accept(v);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();

        mChildOperator.getHeading().containsAll(getUsedAttributes());

    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }
    
    /**
     * Checks if this operator derives attributes (performs any arithmetic
     * expressions).
     * 
     * @return <tt>true</tt> if arithmetic expressions are specified,
     *         <tt>false</tt> otherwise.
     */
    public boolean hasDerivedAttributes()
    {
        return !getDerivedAttributes().isEmpty();
    }
    

    /**
     * Adds a new attribute to the project operator.
     * 
     * @param attr attribute to add.
     */
    public void addAttribute(Attribute attr)
    {
        mAttributeExprList.add(new TableColumn(attr));
        mDerivedAttrAliases.add(null);
        
        List<Attribute> newHeading = new LinkedList<Attribute>();
        newHeading.addAll(mOperatorHeading.getAttributes());
        newHeading.add(attr);
        mOperatorHeading = new HeadingImpl(newHeading);
    }
    
    /**
     * Improves an attribute not found exception by possibly replacing it
     * with an attribute not part of aggregation exception when the missing
     * attribute is hidden by a GROUP_BY operator.
     * 
     * @param exception attribute not found exception
     * 
     * @return the given exception or an AttributeNotPartOfAggregationException
     *   if the attribute not found is hidden by a GROUP_BY operator.
     */
    private LQPException improveAttributeNotFoundException(
        AttributeNotFoundException exception)
    {
        Attribute attr = exception.getAttribute();
        
        if (mChildOperator.getID() == OperatorID.GROUP_BY &&
            AttributeUtils.containsMatching(
                attr,
                mChildOperator.getChild(0).getHeading().getAttributes(), 
                AttributeMatchMode.NAME_AND_NULL_SOURCE))
        {
            // The group by aggregation has hidden the not found attribute
            return new AttributeNotPartOfAggregationException(attr);
        }
        return exception;
    }
}
