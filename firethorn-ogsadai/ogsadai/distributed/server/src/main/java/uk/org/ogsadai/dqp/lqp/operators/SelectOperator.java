// Copyright (c) The University of Edinburgh,  2002 - 2008.
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

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.OperatorVisitor;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Operator SELECT.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectOperator extends UnaryOperator
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2002 - 2007";

    /** Condition predicate */
    private Predicate mPredicate;

    /** Used attributes */
    private Set<Attribute> mUsedAttributes;

    /**
     * Constructor.
     */
    protected SelectOperator()
    {
        mID = OperatorID.SELECT;
    }

    /**
     * Constructor.
     * 
     * @param predicate
     *            select predicate
     */
    public SelectOperator(final Predicate predicate)
    {
        this();
        mPredicate = predicate;

        mUsedAttributes = ExpressionUtils.getUsedAttributes(mPredicate
            .getExpression());
    }

    /**
     * Constructor. Creates operator, connects child and updates.
     * 
     * @param childOperator
     *            child operator
     * @param predicate
     *            select predicate
     */
    public SelectOperator(
            final Operator childOperator, final Predicate predicate)
    {
        this(predicate);
        setChild(0, childOperator);
        try
        {
            update();
        }
        catch (LQPException e)
        {
            e.printStackTrace();
            throw new IllegalStateException(
                "Operation produces corrupted query plan.");
        }
    }

    /**
     * Creates disconnected deep copy of the SELECT operator.
     * 
     * @return disconnected copy
     */
    public SelectOperator createCopy()
    {
        SelectOperator returnOperator = 
            new SelectOperator(mPredicate.getClone());

        return returnOperator;
    }

    /**
     * Returns condition predicate.
     * 
     * @return predicate
     */
    public Predicate getPredicate()
    {
        return mPredicate;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        return mUsedAttributes;
    }

    @Override
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        ExpressionUtils.renameUsedAttributes(
                mPredicate.getExpression(), renameMap);

        mUsedAttributes = ExpressionUtils.getUsedAttributes(
                mPredicate.getExpression());
    }
    
    @Override
    public void update() throws LQPException
    {
        super.update();
        
        // Rename used attributes to fully qualified names
        mPredicate.updateAttributes(
                mChildOperator.getHeading().getAttributes());

        // The default equals() for Attributes does strict matching. Duplicate
        // in this context calls for relaxed matching.
        mUsedAttributes = AttributeUtils.removeDuplicates(
                ExpressionUtils.getUsedAttributes(mPredicate.getExpression()),
        AttributeMatchMode.NAME_AND_NULL_SOURCE);
        
        mOperatorHeading = new HeadingImpl(mChildOperator.getHeading());
    }

    @Override
    public void validate() throws LQPException
    {
        super.validate();
        mChildOperator.getHeading().containsAll(getUsedAttributes());

        // Try to configure to catch problems with types
        List<Attribute> attrList = new ArrayList<Attribute>(
                mChildOperator.getHeading().getAttributes());

        try
        {
            // Add correlated attributes to a list of child attributes            
            for (Attribute a : getUsedAttributes())
            {
                if (a.isCorrelated())
                {
                    attrList.add(a);
                }
            }
            mPredicate.getExpression().configure(
                new HeadingImpl(attrList).getTupleMetadata());
        }
        // ColumnNotFoundException, TypeMismatchException
        catch (Exception e)
        {
            throw new LQPException(e);
        }
    }

    /**
     * Splits selects with conjunctive predicate into a chain of SELECT
     * operators with atomic predicates.
     * 
     * @return root operator of the select chain
     * @throws LQPException
     *             if update after explosion fails
     */
    public SelectOperator expolde() throws LQPException
    {
        List<Predicate> splitPredicates = mPredicate
            .splitConjunction();

        if (splitPredicates.size() > 1)
        {
            SelectOperator returnRoot = this;

            for (int i = 0; i < splitPredicates.size(); i++)
            {
                SelectOperator newSelect = new SelectOperator(splitPredicates
                    .get(i));

                if (i == 0)
                {
                    mParentOperator.replaceChild(this, newSelect);
                    returnRoot = newSelect;
                }
                else
                {
                    mParentOperator.setChild(0, newSelect);
                }
                mParentOperator = newSelect;
            }
            mParentOperator.setChild(0, mChildOperator);

            disconnect();

            returnRoot.getParent().update();
            return returnRoot;
        }
        else
        {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void accept(OperatorVisitor visitor)
    {
        visitor.visit(this);
    }

}
