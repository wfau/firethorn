// Copyright (c) The University of Edinburgh, 2008-2010.
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Utility methods for dealing with attribute collections.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttributeUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2010";

    /**
     * Check for equality of two attribute lists.
     * 
     * @param lOne
     *            list one
     * @param lTwo
     *            list two
     * @param matchMode
     *            which match mode to use
     * @return <code>true</code> when lists match
     */
    public static boolean listEqual(List<Attribute> lOne, List<Attribute> lTwo,
        AttributeMatchMode matchMode)
    {
        if (lOne.size() != lTwo.size())
        {
            return false;
        }
    
        for (int i = 0; i < lOne.size(); i++)
        {
            if (!lOne.get(i).equals(lTwo.get(i), matchMode))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes duplicate attributes from a set. Semantics of equality check is
     * controlled by the match mode.
     * 
     * @param attrSet
     *            set to filter
     * @param matchMode
     *            which match mode to use
     * @return set with removed duplicates (same object)
     */
    public static Set<Attribute> removeDuplicates(Set<Attribute> attrSet,
        AttributeMatchMode matchMode)
    {
        return new HashSet<Attribute>(removeDuplicates(
            new LinkedList<Attribute>(attrSet), matchMode));
    }

    /**
     * Removes duplicate attributes from a list. Semantics of equality check is
     * controlled by the match mode.
     * 
     * @param attrList
     *            list to filter
     * @param matchMode
     *            which match mode to use
     * @return list with removed duplicates (same object)
     */
    public static List<Attribute> removeDuplicates(List<Attribute> attrList,
        AttributeMatchMode matchMode)
    {
        for (int i = 0; i < attrList.size(); i++)
        {
            for (int j = i + 1; j < attrList.size(); j++)
            {
                Attribute a1 = attrList.get(j);
                Attribute a2 = attrList.get(i);
                if (a2.equals(a1, matchMode))
                {
                    attrList.remove(i);
                    i--;
                    break;
                }
            }
        }
        return attrList;
    }

    /**
     * Filters out all attributes from <code>attrList</code> which are not in
     * the <code>attrToRetain</code> list. Semantics of equality check is
     * controlled by the match mode.
     * 
     * @param attrToRetain
     *            a list of attributes to retain
     * @param attrList
     *            a list to be filtered
     * @param matcMode
     *            which match mode to use
     */
    public static void retainAllMatching(Iterable<Attribute> attrToRetain,
        List<Attribute> attrList, AttributeMatchMode matcMode)
    {
        for (int i = 0; i < attrList.size(); i++)
        {
            if (!containsMatching(attrList.get(i), attrToRetain,
                AttributeMatchMode.NAME_AND_NULL_SOURCE))
            {
                attrList.remove(i);
                i--;
            }
        }
    }

    /**
     * Removes from <code>attrList</code> all attributes which are also in
     * <code>attrToRemove</code>. Semantics of equality check is controlled by
     * the match mode.
     * 
     * @param attrToRemove
     *            a collection of attributes to remove
     * @param attrList
     *            a list to filter
     * @param matchMode
     *            which match mode to use
     */
    public static void removeAllMatching(Iterable<Attribute> attrToRemove,
        List<Attribute> attrList, AttributeMatchMode matchMode)
    {
        for (Attribute a : attrToRemove)
        {
            removeMatching(a, attrList, matchMode);
        }
    }

    /**
     * Removes attribute from a list. Semantics of equality check is controlled
     * by the match mode.
     * 
     * @param attr
     *            attribute to remove
     * @param attrList
     *            a list to filter
     * @param matchMode
     *            which match mode to use
     */
    public static void removeMatching(Attribute attr, List<Attribute> attrList,
        AttributeMatchMode matchMode)
    {
        for (int i = 0; i < attrList.size(); i++)
        {
            if (attr.equals(attrList.get(i), matchMode))
            {
                attrList.remove(i);
                i--;
            }
        }
    }

    /**
     * Check if a collection contains an attribute. Semantics of equality check
     * is controlled by the match mode.
     * 
     * @param attr
     *            attribute
     * @param coll
     *            a collection to test
     * @param matchMode
     *            which match mode to use
     * @return <code>true</code> if a matching attribute is in a list
     */
    public static boolean containsMatching(Attribute attr,
        Iterable<Attribute> coll, AttributeMatchMode matchMode)
    {
        for (Attribute a : coll)
        {
            if (a.equals(attr, matchMode))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of all matching attributes in a collection. Semantics of
     * equality check is controlled by the match mode.
     * 
     * @param attr
     *            attribute
     * @param coll
     *            a collection to test
     * @param matchMode
     *            which match mode to use
     * @return a list of matching attributes
     */
    public static List<Attribute> getMatching(Attribute attr,
        Collection<Attribute> coll, AttributeMatchMode matchMode)
    {
        List<Attribute> matchingAttrs = new ArrayList<Attribute>();
    
        for (Attribute a : coll)
        {
            if (a.equals(attr, matchMode))
            {
            matchingAttrs.add(a);
            }
        }
        return matchingAttrs;
    }

    /**
     * Returns an index of the first matching attribute in a list. Semantics of
     * equality check is controlled by the match mode.
     * 
     * @param attr
     *            attribute
     * @param attrList
     *            attribute list
     * @param matchMode
     *            which match mode to use
     * @return an index of a first matching attribute
     */
    public static int getMatchingIndex(Attribute attr,
        List<Attribute> attrList, AttributeMatchMode matchMode)
    {
        for (int i = 0; i < attrList.size(); i++)
        {
            if (attr.equals(attrList.get(i), matchMode))
            {
                return i;
            }
        }
        return -1;
    }
}
