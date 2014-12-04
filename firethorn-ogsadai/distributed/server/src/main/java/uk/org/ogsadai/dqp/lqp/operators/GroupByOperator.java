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

package uk.org.ogsadai.dqp.lqp.operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.ExecutableFunction;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.LogicalFunction;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrRenameArithmeticExprVisitor;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Operator GROUP_BY.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class GroupByOperator extends UnaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Grouping attributes. */
    protected List<Attribute> mGroupingAttributes;

    /** Aggregate functions. */
    protected List<Function> mAggregateFunctions;
    
    /**
     * Default constructor.
     */
    protected GroupByOperator()
    {
        mID = OperatorID.GROUP_BY;
    }

    /**
     * Creates GROUP_BY operator, connects child and updates.
     * 
     * @param childOperator
     *            child operator
     * @param groupByAST
     *            GROUP_BY ast
     * @param selectAggregates
     *            a list of aggregates from select list
     * @param havingAggregates
     *            a list of aggregates from having clause
     * @throws LQPException
     *             if there is a problem creating the operator
     */
    public GroupByOperator(final Operator childOperator,
        final CommonTree groupByAST, final List<Function> selectAggregates,
        final List<Function> havingAggregates) throws LQPException
    {
        this();

        List<Attribute> groupingAttributes = new ArrayList<Attribute>();

        // build grouping attributes
        try
        {
            for (int i = 0; i < groupByAST.getChildCount(); i++)
            {
                CommonTree column = (CommonTree) groupByAST.getChild(i);
                String source;
                String name;
                if (column.getChildCount() == 1)
                {
                    name = column.getChild(0).getText();
                    source = null;
                    groupingAttributes.add(new AttributeImpl(name));
                }
                else
                {
                    name = column.getChild(1).getText();
                    source = column.getChild(0).getText();
                    groupingAttributes.add(new AttributeImpl(name, -1, source));
                }
            }
        }
        // AmbiguousAttributeException, AttributeNotFoundException
        catch (Exception e)
        {
            throw new LQPException(e);
        }
        
        initialise(childOperator, groupingAttributes, 
            selectAggregates, havingAggregates);
    }

    public GroupByOperator(
        final Operator childOperator,
        final List<Attribute> groupingAttributes,
        final List<Function> selectAggregates,
        final List<Function> havingAggregates) throws LQPException
    {
        this();
        initialise(childOperator, groupingAttributes, 
                   selectAggregates, havingAggregates);
    }
    
    private void initialise(
        final Operator childOperator,
        final List<Attribute> groupingAttributes,
        final List<Function> selectAggregates,
        final List<Function> havingAggregates) throws LQPException
    {
        setChild(0, childOperator);
        
        mGroupingAttributes = new ArrayList<Attribute>();
        mGroupingAttributes.addAll(groupingAttributes);
        
        // Having aggregates are temporary and need to be projected out - tag
        // with annotation
        for (Function funct : havingAggregates)
        {
            Annotation.addTempAttrAnnotation(funct, true);
        }

        mAggregateFunctions = new ArrayList<Function>();
        mAggregateFunctions.addAll(selectAggregates);
        mAggregateFunctions.addAll(havingAggregates);
        
        update();
    }
    
    
    /**
     * Returns the aggregate functions in this GroupBy operator.
     * 
     * @return aggregate functions
     */
    public List<Function> getAggregates()
    {
        return mAggregateFunctions;
    }

    /**
     * Returns the grouping attributes.
     * 
     * @return grouping attributes
     */
    public List<Attribute> getGroupingAttributes()
    {
        return mGroupingAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        // used attributes are both grouping attributes and attributes used by
        // functions
        List<Attribute> usedAttrList = new LinkedList<Attribute>(
            mGroupingAttributes);
    
        AttrExtrArithmeticExprVisitor visitor = new AttrExtrArithmeticExprVisitor();
    
        for (Function f : mAggregateFunctions)
        {
            for (ArithmeticExpression e : f.getParameters())
            {
                e.accept(visitor);
            }
        }
        usedAttrList.addAll(visitor.getAttributes());
        AttributeUtils.removeDuplicates(usedAttrList,
            AttributeMatchMode.NAME_AND_NULL_SOURCE);
    
        return new HashSet<Attribute>(usedAttrList);
    }
    
    @Override
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        // rename grouping attributes
        for (int i = 0; i < mGroupingAttributes.size(); i++)
        {
            Attribute attr;
            if ((attr = (Attribute) renameMap
                    .getRenamedAttribute(mGroupingAttributes.get(i))) != null)
            {
                mGroupingAttributes.set(i, attr);
            }
        }
        
        for(Function f : mAggregateFunctions)
        {
            AttrRenameArithmeticExprVisitor visitor =
                new AttrRenameArithmeticExprVisitor(renameMap);
            
            for(ArithmeticExpression e : f.getParameters())
            {
                e.accept(visitor);
            }
        }        
    }
    
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();

        List<Attribute> headAttributes = new ArrayList<Attribute>();
        
        for(Attribute attr : mGroupingAttributes)
        {
            headAttributes.add(mChildOperator.getHeading()
                .getMatchingAttribute(attr));
        }
        
        for(Function func : mAggregateFunctions)
        {
            String bindName = Annotation.getResultNameAnnotation(func);
            boolean temporary = false;
            if (Annotation.getTempAttrAnnotation(func) != null
                && Annotation.getTempAttrAnnotation(func))
            {
                temporary = true;
            }
            
            // Configure function. This also validates types.
            ExecutableFunctionExpression fExpr = new ExecutableFunctionExpression(
                (ExecutableFunction) func);
            try
            {
                fExpr.configure(mChildOperator.getHeading().getTupleMetadata());
            }
            catch (TypeMismatchException e)
            {
                throw new LQPException(e);
            }

            // TODO: we need to get rid of this by merging with
            // ExecutableFunction interface - some code still relies on setting
            // the input heading.
            ((LogicalFunction) func).setInputHeading(0, mChildOperator
                .getHeading());
            
            // Configured function is able to provide the result type.
            Attribute attr = new AttributeImpl(bindName, fExpr.getMetadata()
                .getType(), null);

            if (temporary)
            {
                Annotation.addTempAttrAnnotation(attr, true);
            }
            headAttributes.add(attr);
        }
        mOperatorHeading = new HeadingImpl(headAttributes);
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

}
