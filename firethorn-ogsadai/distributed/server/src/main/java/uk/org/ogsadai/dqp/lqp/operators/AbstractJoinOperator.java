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

import java.util.HashSet;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Abstract operator JOIN.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class AbstractJoinOperator extends BinaryOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Used attributes */
    protected Set<Attribute> mUsedAttributes;
    /** Join predicate. */
    protected Predicate mPredicate;

    /**
     * Gets join predicate.
     * 
     * @return join predicate
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

    /**
     * {@inheritDoc}
     */
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        ExpressionUtils.renameUsedAttributes(mPredicate
            .getExpression(), renameMap);
        
        // The default equals() for Attributes does strict matching. Duplicate
        // in this context calls for relaxed matching.
        mUsedAttributes = AttributeUtils.removeDuplicates(
            ExpressionUtils.getUsedAttributes(mPredicate.getExpression()),
            AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        super.update();

        mOperatorHeading = mLeftChildOperator.getHeading().createMerged(
            mRightChildOperator.getHeading());

        // Rename used attributes with fully qualified names
        mPredicate.updateAttributes(mOperatorHeading.getAttributes());

        mUsedAttributes = ExpressionUtils.getUsedAttributes(
            mPredicate.getExpression());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        super.validate();

        // Validate the attributes, this validation uses isCorrelated 
        // annotations to handle correlated attributes.
        mLeftChildOperator.getHeading().getClone().createMerged(
            mRightChildOperator.getHeading()).containsAll(mUsedAttributes);
        
        // Find set of correlated attributes
        Set<Attribute> correlatedAttributes = new HashSet<Attribute>();
        for( Attribute attr : mUsedAttributes)
        {
            if (attr.isCorrelated())
            {
                correlatedAttributes.add(attr);
            }
        }
        
        // Try to configure predicate expression to catch any type related
        // problems.
        try
        {
            // Find set of correlated Attributes
            mPredicate.getExpression().configure(
                mLeftChildOperator.getHeading().createMerged(
                    mRightChildOperator.getHeading()).getTupleMetadata(),
                correlatedAttributes);
        }
        // ColumnNotFoundException, TypeMismatchException
        catch (Exception e)
        {
            throw new LQPException(e);
        }
    }
}
