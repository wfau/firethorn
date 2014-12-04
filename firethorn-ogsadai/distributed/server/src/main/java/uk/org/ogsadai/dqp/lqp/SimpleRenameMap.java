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

package uk.org.ogsadai.dqp.lqp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;

/**
 * Simple implementation of the RenameMap interface.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleRenameMap implements RenameMap
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    // Mapping is encoded in the list index original(i) <-> renamed(i)

    /** List of original attributes. */
    private List<Attribute> mOriginalAttributes;
    /** List of renamed attributes. */
    private List<Attribute> mRenamedAttributes;
    /** Which matching mode to use. */
    AttributeMatchMode mMatchingMode = AttributeMatchMode.NAME_AND_NULL_SOURCE;

    /**
     * Constructor.
     */
    public SimpleRenameMap()
    {
        mOriginalAttributes = new ArrayList<Attribute>();
        mRenamedAttributes = new ArrayList<Attribute>();
    }

    /**
     * Constructor.
     * 
     * @param originalAttrList
     *            a list of original attributes
     * @param renamedAttrList
     *            a list of renamed attributes
     */
    public SimpleRenameMap(List<Attribute> originalAttrList,
            List<Attribute> renamedAttrList)
    {
        mOriginalAttributes = new ArrayList<Attribute>(originalAttrList);
        mRenamedAttributes = new ArrayList<Attribute>(renamedAttrList);
    }

    /**
     * {@inheritDoc}
     */
    public void add(Attribute originalAttribute, Attribute renamedAttribute)
    {
        mOriginalAttributes.add(originalAttribute.getClone(false));
        mRenamedAttributes.add(renamedAttribute.getClone(false));
    }
    
    /**
     * {@inheritDoc}
     */
    public Attribute getOriginalAttribute(Attribute renamedAttribute)
            throws AmbiguousAttributeException, AmbiguousMappingException
    {
        List<Attribute> matchingAttrs = AttributeUtils.getMatching(
                renamedAttribute, mRenamedAttributes, mMatchingMode);

        if (matchingAttrs.size() == 0)
        {
            return null;
        }

        if (matchingAttrs.size() > 1
                && !OptimiserUtils.checkIfAllSame(matchingAttrs))
        {
            throw new AmbiguousAttributeException(renamedAttribute);
        }

        // Now check to see how this is mapped in case it is ambiguous
        Attribute matched = matchingAttrs.get(0);
        
        List<Attribute> candidateResults = new LinkedList<Attribute>();
        int index = 0;
        for (Attribute renamed : mRenamedAttributes)
        {
            if (renamed.equals(matched, mMatchingMode))
            {
                candidateResults.add(mOriginalAttributes.get(index));
            }
            index++;
        }

        if (candidateResults.size() > 1
            && !OptimiserUtils.checkIfAllSame(candidateResults))
        {
            throw new AmbiguousMappingException(candidateResults.get(0));
        }

        return candidateResults.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public Attribute getRenamedAttribute(Attribute originalAttribute)
            throws AmbiguousAttributeException, AmbiguousMappingException
    {
        List<Attribute> matchingAttrs = AttributeUtils.getMatching(
                originalAttribute, mOriginalAttributes, mMatchingMode);

        if (matchingAttrs.size() == 0)
        {
            return null;
        }

        if (matchingAttrs.size() > 1
                && !OptimiserUtils.checkIfAllSame(matchingAttrs))
        {
            throw new AmbiguousAttributeException(originalAttribute);
        }

        // Now check to see how this is mapped in case it is ambiguous
        Attribute matched = matchingAttrs.get(0);
        
        List<Attribute> candidateResults = new LinkedList<Attribute>();
        int index = 0;
        for (Attribute orig : mOriginalAttributes)
        {
            if (orig.equals(matched, mMatchingMode))
            {
                candidateResults.add(mRenamedAttributes.get(index));
            }
            index++;
        }
        
        if (candidateResults.size() > 1
                && !OptimiserUtils.checkIfAllSame(candidateResults))
        {
    		throw new AmbiguousMappingException(candidateResults.get(0));
        }

        return candidateResults.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public List<Attribute> getOriginalAttributeList()
    {
        return mOriginalAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public List<Attribute> getRenamedAttributeList()
    {
        return mRenamedAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public void resetRenamedAttributeSources(String newSource)
    {
        for (int i = 0; i < mRenamedAttributes.size(); i++)
        {
            mRenamedAttributes.set(i, mRenamedAttributes.get(i)
                    .getCloneNewSource(newSource, false));
        }
    }

    /**
     * {@inheritDoc}
     */
    public RenameMap getRenamedToOriginalMap(Collection<Attribute> attributes)
            throws AmbiguousMappingException, AmbiguousAttributeException
    {
        RenameMap renamedToOrigialMap = new SimpleRenameMap();

        for (Attribute attr : attributes)
        {
            Attribute origAttr = getOriginalAttribute(attr);

            if (origAttr != null)
            {
                if (!attr.equals(origAttr, mMatchingMode)
                        && AttributeUtils.containsMatching(origAttr,
                                attributes, mMatchingMode))
                {
                    throw new AmbiguousMappingException(attr);
                }

                if (!origAttr.equals(attr, mMatchingMode))
                {
                    renamedToOrigialMap.add(attr, getOriginalAttribute(attr));
                }
            }
        }
        return renamedToOrigialMap;
    }

    /**
     * {@inheritDoc}
     */
    public RenameMap getOriginalToRenamedMap(List<Attribute> attributeList)
            throws AmbiguousMappingException, AmbiguousAttributeException
    {
        RenameMap origialToRenamedMap = new SimpleRenameMap();

        for (Attribute attr : attributeList)
        {
            Attribute renamedAttr = getRenamedAttribute(attr);

            if (renamedAttr != null)
            {
                if (!attr.equals(renamedAttr, mMatchingMode)
                        && AttributeUtils.containsMatching(renamedAttr,
                                attributeList, mMatchingMode))
                {
                    throw new AmbiguousMappingException(attr);
                }

                if (!renamedAttr.equals(attr, mMatchingMode))
                {
                    origialToRenamedMap.add(attr, getRenamedAttribute(attr));
                }
            }
        }

        return origialToRenamedMap;
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        return mOriginalAttributes.size();
    }

    /**
     * {@inheritDoc}
     */
    public void removeRenamedAttribute(Attribute attr)
    {
        int i = 0;
        for (Attribute a : mRenamedAttributes)
        {
            if (a.equals(attr, mMatchingMode))
            {
                mRenamedAttributes.remove(i);
                mOriginalAttributes.remove(i);
                break;
            }
            i++;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setAttributeMatchMode(AttributeMatchMode matchMode)
    {
        mMatchingMode = matchMode;
    }

    @Override
    public String toString()
    {
        return "ORIG: " + mOriginalAttributes + "\n" + "RENM: "
                + mRenamedAttributes + "\n" + mMatchingMode;
    }
}
