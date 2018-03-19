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
import java.util.List;
import java.util.Set;

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
 * Scalar group by operator. Used when query calculates aggregates but there is
 * no GROUP BY clause.
 * 
 * TODO: This class shares a lot of its implementation with GROUP_BY - would be
 * nice to factor out similarities.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ScalarGroupByOperator extends UnaryOperator
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh,  2008-2012";

    /** List of aggregates. */
    private List<Function> mAggregateFunctions;
    
    /**
     * Constructor.
     */
    private ScalarGroupByOperator()
    {
        mID = OperatorID.SCALAR_GROUP_BY;
    }

    /**
     * Constructs SCALAR_GROUP_BY operator, connects child and updates.
     * 
     * @param childOperator
     *            child operator
     * @param selectAggregates
     *            a list of aggregates from select list
     * @param havingAggregates
     *            a list of aggregates from having clause
     * @throws LQPException
     *             if update fails
     */
    public ScalarGroupByOperator(final Operator childOperator,
        final List<Function> selectAggregates,
        final List<Function> havingAggregates) throws LQPException
    {
        this();
        setChild(0, childOperator);

        // Having aggregates are temporary and need to be projected out - tag
        // with annotation
        for (Function funct : havingAggregates)
        {
            Annotation.addTempAttrAnnotation(funct, true);
        }

        mAggregateFunctions = selectAggregates;
        mAggregateFunctions.addAll(havingAggregates);
        
        update();
    }

    /**
     * Gets a list of aggregates.
     * 
     * @return list of aggregates.
     */
    public List<Function> getAggregates()
    {
        return mAggregateFunctions;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        
        for(Function f : mAggregateFunctions)
        {
            for(ArithmeticExpression e : f.getParameters())
            {
                e.accept(visitor);
            }
        }
        
        Set<Attribute> attributes = visitor.getAttributes();
        
        // if there are no used attributes then * is the only parameter 
        // hence the function must be COUNT(*) so we just pick any attribute
        if (visitor.getAttributes().isEmpty())
        {
            // add the first attribute of child - could be any of them
            attributes.add(getChild(0).getHeading().getAttributes().get(0));
        }
        
        // The default equals() for Attributes does strict matching. Duplicate
        // in this context calls for relaxed matching.
        return AttributeUtils.removeDuplicates(
                attributes,
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }

    @Override
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        for (Function f : mAggregateFunctions)
        {
            AttrRenameArithmeticExprVisitor visitor = 
                new AttrRenameArithmeticExprVisitor(renameMap);
    
            for (ArithmeticExpression e : f.getParameters())
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
