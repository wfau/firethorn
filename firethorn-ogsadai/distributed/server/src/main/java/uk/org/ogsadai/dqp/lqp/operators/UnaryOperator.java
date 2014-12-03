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

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Abstract class representing unary operators in the logical query plan.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class UnaryOperator extends AbstractOperator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** The only child. */
    protected Operator mChildOperator;
    
    /**
     * {@inheritDoc}
     */
    public int getChildCount()
    {
        if (mChildOperator == null)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Operator getChild(final int position)
    {
        if (position != 0)
        {
            throw new IllegalStateException(mID + " is a unary operator.");
        }
        return mChildOperator;
    }

    /**
     * {@inheritDoc}
     */
    public void replaceChild(Operator oldChild, Operator newChild)
    {
        if (mChildOperator == oldChild)
        {
            setChild(0, newChild);
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
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException
     *             if position is not 0
     */
    public void setChild(final int position, final Operator child)
    {
        if (position != 0)
        {
            throw new IllegalStateException(mID + " is a unary operator.");
        }
        mChildOperator = child;

        if (child != null)
            child.setParent(this);
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        if (mChildOperator == null)
        {
            throw new IllegalStateException("Disconnected operator: " + this);
        }
        
        mChildOperator.validate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException
    {
        if (mChildOperator == null)
        {
            throw new IllegalStateException("Disconnected operator: " + this);
        }
        
        mChildOperator.update();
    }
    
    /**
     * {@inheritDoc}
     */
    public void disconnect()
    {
        mChildOperator = null;
        mParentOperator = null;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "(" + mID + " " + mChildOperator + ")";
    }
}
