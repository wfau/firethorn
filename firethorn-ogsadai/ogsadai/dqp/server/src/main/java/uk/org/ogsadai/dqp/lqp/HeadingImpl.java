// Copyright (c) The University of Edinburgh,  2008.
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

package uk.org.ogsadai.dqp.lqp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.PredicateSources.MatchType;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Default implementation of the OperatorHead interface.
 * 
 * @author The OGSA-DAI Project Team
 */
public class HeadingImpl implements Heading
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh,  2008";

    /** Ordered list of attributes. */
    private List<Attribute> mAttributes;
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(HeadingImpl.class);

    /**
     * Copy constructor. Creates a new heading object that is a deep copy of the
     * heading object passed as parameter. Heading attributes are copied with
     * annotations.
     * 
     * @param operatorHead
     *            head object.
     */
    public HeadingImpl(Heading operatorHead)
    {
        mAttributes = new ArrayList<Attribute>();
        for (Attribute attr : operatorHead.getAttributes())
        {
            mAttributes.add(attr.getClone(true));
        }
    }

    /**
     * Constructor.
     * 
     * @param attributes
     *            an ordered list of attributes.
     */
    public HeadingImpl(List<Attribute> attributes)
    {
        mAttributes = attributes;
    }

    /**
     * {@inheritDoc}
     */
    public void containsAll(Collection<Attribute> attributes)
        throws AmbiguousAttributeException, AttributeNotFoundException
    {
        List<Attribute> matchingAttrs = new ArrayList<Attribute>();
        for (Attribute cAttrib : attributes)
        {
            matchingAttrs.clear();
            for (Attribute mAttrib : mAttributes)
            {
                if (cAttrib.equals(mAttrib,
                        AttributeMatchMode.NAME_AND_NULL_SOURCE))
                {
                    matchingAttrs.add(mAttrib);
                }
            }

            if (matchingAttrs.size() == 0 && !cAttrib.isCorrelated())
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("ATTRIBUTE NOT FOUND " + cAttrib + ":"
                            + TupleTypes.getTypeName(cAttrib.getType())
                            + " NOT IN " + toString());
                }
                throw new AttributeNotFoundException(cAttrib);
            }
            else if (matchingAttrs.size() > 1
                    && !OptimiserUtils.checkIfAllSame(matchingAttrs))
            {
                throw new AmbiguousAttributeException(cAttrib);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAllUnambiguous(Collection<Attribute> attributes)
    {
        try
        {
            containsAll(attributes);            
        } 
        // AmbiguousAttributeException, AttributeNotFoundException
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean contains(Attribute attr)
    {
        return AttributeUtils.containsMatching(
            attr, mAttributes,AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }
    
    /**
     * {@inheritDoc}
     */
    public Attribute getMatchingAttribute(Attribute attribute)
        throws AmbiguousAttributeException, AttributeNotFoundException
    {
        List<Attribute> attrList = new ArrayList<Attribute>();
        for(Attribute attr : mAttributes)
        {
            if (attr.equals(attribute, AttributeMatchMode.NAME_AND_NULL_SOURCE))
            {
                attrList.add(attr);
            }
        }

        if(attrList.size() == 0)
        {
            throw new AttributeNotFoundException(attribute);
        }
        
        if(attrList.size() > 1 && !OptimiserUtils.checkIfAllSame(attrList))
        {
            throw new AmbiguousAttributeException(attribute);
        }
        
        return attrList.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public Heading createMerged(Heading head)
    {
        List<Attribute> newAttrList = new ArrayList<Attribute>();

        newAttrList.addAll(mAttributes);
        newAttrList.addAll(head.getAttributes());

        return new HeadingImpl(newAttrList);
    }

    /**
     * {@inheritDoc}
     */
    public List<Attribute> getAttributes()
    {
        return Collections.unmodifiableList(mAttributes);
    }

    /**
     * {@inheritDoc}
     */
    public void invalidateKeys()
    {
        List<Attribute> newAttrList = new ArrayList<Attribute>();
        for(Attribute attr : mAttributes)
        {
            newAttrList.add(attr.getCloneInvalidateKeys(true));
        }
        mAttributes = newAttrList;
    }
    
    /**
     * {@inheritDoc}
     */
    public Heading getClone()
    {
        return new HeadingImpl(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public TupleMetadata getTupleMetadata()
    {
        List<ColumnMetadata> colMDList = new ArrayList<ColumnMetadata>();
        for(Attribute a : mAttributes)
        {
            colMDList.add(new SimpleColumnMetadata(
                a.getName(), 
                a.getSource(),
                null, 
                null, 
                a.getType(), 
                0,
                ColumnMetadata.COLUMN_NULLABLE_UNKNOWN, 0));
        }
        return new SimpleTupleMetadata(colMDList);
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Attribute a : mAttributes)
        {
            sb.append(' ');
            sb.append(a);
            sb.append(':');
            sb.append(TupleTypes.getTypeName(a.getType()));
            sb.append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
}
