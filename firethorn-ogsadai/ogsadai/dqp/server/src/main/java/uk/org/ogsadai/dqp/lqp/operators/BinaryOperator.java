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

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Base class for binary operators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class BinaryOperator extends AbstractOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Left child operator. */
    protected Operator mLeftChildOperator;

    /** Right child operator. */
    protected Operator mRightChildOperator;

    /** List of correlated attributes. */
    protected Set<Attribute> mCorrelatedAttributes =
        new HashSet<Attribute>();

    /**
     * {@inheritDoc}
     */
    public int getChildCount()
    {
        return 2;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator getChild(int position)
    {
        if (position == 0)
        {
            return mLeftChildOperator;
        }
        else if (position == 1)
        {
            return mRightChildOperator;
        }
        else
        {
            throw new IllegalStateException(
                "You can only request 0=left or 1=right child of a binary operator.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void replaceChild(Operator oldChild, Operator newChild)
    {
        if (mLeftChildOperator == oldChild)
        {
            setChild(0, newChild);
        }
        else if (mRightChildOperator == oldChild)
        {
            setChild(1, newChild);
        }
        else
        {
            throw new IllegalArgumentException(
                "Old child operator was not found");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBinary()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void setChild(int position, Operator child)
    {
        if (position == 0)
        {
            mLeftChildOperator = child;
        }
        else if (position == 1)
        {
            mRightChildOperator = child;
        }
        else
        {
            throw new IllegalStateException(
                "You can only request 0=left or 1=right child of a binary operator.");
        }

        if (child != null)
            child.setParent(this);
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        if (mLeftChildOperator == null || mRightChildOperator == null)
            throw new LQPException("Disconnected operator.");

        try
        {
            mLeftChildOperator.validate();
        }
        catch (AttributeNotFoundException e)
        {
            Attribute missingAttr = e.getAttribute();
            if(missingAttr.isCorrelated())
            {
                // Swallow exception
            }
            else
            {
                // This can be due to correlation
                Attribute matchingAttr = mRightChildOperator.getHeading()
                    .getMatchingAttribute(missingAttr);
    
                // Set to correlated and validate
                Annotation.addCorrAttrAnnotation(missingAttr, true);
                missingAttr.setType(matchingAttr.getType());
                mCorrelatedAttributes.add(missingAttr);

                validate();
            }
        }

        try
        {
            mRightChildOperator.validate();
        }
        catch (AttributeNotFoundException e)
        {
            Attribute missingAttr = e.getAttribute();
            if(missingAttr.isCorrelated())
            {
                // Swallow exception
            }
            else
            {
                // This can be due to correlation
                Attribute matchingAttr = mLeftChildOperator.getHeading()
                    .getMatchingAttribute(missingAttr);
    
                // Set to correlated and validate
                Annotation.addCorrAttrAnnotation(missingAttr, true);
                missingAttr.setType(matchingAttr.getType());
                mCorrelatedAttributes.add(missingAttr);
    
                validate();
            }            
        }

        if (mCorrelatedAttributes.size() > 0)
        {
            Annotation.addCorrelatedSetAnnotation(this,
                mCorrelatedAttributes);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        if (mLeftChildOperator == null || mRightChildOperator == null)
            throw new LQPException("Unconnected operator.");
        
        mRightChildOperator.update();
        mLeftChildOperator.update();
    }
    
    /**
     * {@inheritDoc}
     */
    public void disconnect() 
    {
        mLeftChildOperator = null;
        mRightChildOperator = null;
        mParentOperator = null;
    }

    /**
     * Returns operator index.
     * 
     * @param operator
     *            child operator
     * @return operator index or -1 if operator is not a child
     */
    public int getChildIndex(Operator operator)
    {
        if (mLeftChildOperator == operator)
        {
            return 0;
        }
        else if(mRightChildOperator == operator)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "(" + mID + " " + mLeftChildOperator + " "
            + mRightChildOperator + ")";
    }
}
