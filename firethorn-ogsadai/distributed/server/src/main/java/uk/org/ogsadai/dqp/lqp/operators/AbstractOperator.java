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

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;

/**
 * Abstract base class for all operators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class AbstractOperator implements Operator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Operator heading. */
    protected Heading mOperatorHeading;

    /** Parent operator. */
    protected Operator mParentOperator;

    /** Operator ID. */
    protected OperatorID mID;

    /** Annotation map. */
    protected Map<String, Object> mAnnotations =
        new ConcurrentHashMap<String, Object>();

    /**
     * {@inheritDoc}
     */
    public Heading getHeading()
    {
        return mOperatorHeading;
    }

    /**
     * {@inheritDoc}
     */
    public Operator getParent()
    {
        return mParentOperator;
    }

    /**
     * {@inheritDoc}
     */
    public OperatorID getID()
    {
        return mID;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPartitionLeaf()
    {
        // Default value is false.  Those operators that are partition leafs
        // will override
        return false;
    }

    /**
     * Sets the heading of the operator.
     * 
     * @param head
     *            heading
     */
    public void setHeading(Heading head)
    {
        mOperatorHeading = head;
    }

    /**
     * {@inheritDoc}
     */
    public void setParent(Operator parent)
    {
        mParentOperator = parent;
    }

    /**
     * {@inheritDoc}
     */
    public Object getAnnotation(String key)
    {
        return mAnnotations.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotation(String key, Object value)
    {
        mAnnotations.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public void removeAnnotation(String key)
    {
        mAnnotations.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getAnnotations()
    {
        return mAnnotations;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Attribute> getUsedAttributes()
    {
        return Collections.emptySet();
    }
    
    /**
     * {@inheritDoc}
     */
    public void renameUsedAttributes(RenameMap renameMap) throws LQPException
    {
        // NOOP
    }
    
}
