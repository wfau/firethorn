//(c) University of Edinburgh, 2002 - 2007.
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


package uk.org.ogsadai.tuple.join;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Iterator for <code>MultiValueTreeMap</code> class.
 * 
 * @author The OGSA-DAI Project Team.
 * @param <T>
 *            map key
 * @param <U>
 *            map values
 */
class MultiValueTreeMapIterator<T,U> implements Iterator<U>
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE =
        "(c) University of Edinburgh 2002 - 2007.";

    /** Map of keys to list of objects, may be null. */
    private final Map<T,List<U>> mMap;
    
    /** List of objects, may be null. */
    private final List<U> mList;
    
    /** The list that should be excluded, may be null. */
    private final List<U> mExcludedList;
    
    /** Iterator to the map. */
    private final Iterator<List<U>> mMapIterator;
    
    /** Iterator to the list. */
    private final Iterator<U> mListIterator;
    
    /** Iterator to the values in a list within the map. */
    private Iterator<U> mMapValueIterator;
    
    /** The next object to return, null if we don't know it yet. */
    private U mNext;

    /**
     * Package protected constructor. Should only be called from
     * <code>MultiValueTreeMap</code> class.
     * <p>
     * The iterator constructed should:
     * <ul>
     * <li>iterate through the values of the map to obtain the lists stored in
     * the map and then give access to the values in these lists.</li>
     * <li>give access to the values in the list</li>
     * <li>not give access to the values in the list held within the map that
     * corresponds to the excluded key.</li>
     * </ul>
     * 
     * @param map
     *            a mapping of keys to list of objects, may be <code>null</code>
     * 
     * @param list
     *            a list of objects, may be <code>null</code>
     * 
     * @param excludedKey
     *            key to map that specifies a list whose values must not be
     *            accessible via the iterator, may be <code>null</code>
     */ 
    MultiValueTreeMapIterator( 
        final Map<T,List<U>> map, 
        final List<U> list, 
        final T excludedKey)
    {
        mMap = map;
        mList = list;
        mNext = null;
        mMapValueIterator = null;
        
        if (mMap != null) 
        {
            mMapIterator = mMap.values().iterator();
        }
        else
        {
            mMapIterator = null;
        }
        if (mList != null)
        {
            mListIterator = mList.iterator();
        }
        else
        {
            mListIterator = null;
        }
        
        if (mMap != null && excludedKey != null)
        {
            mExcludedList = mMap.get(excludedKey);
        }
        else
        {
            mExcludedList = null;
        }
    }
    

    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        // If we already have the next object looked up then we have a next
        if (mNext != null)
        {
            return true;
        }
        
        // Try to get the next object
        mNext = getNext();
        return (mNext != null);
    }

    /**
     * {@inheritDoc}
     */
    public U next()
    {
        U result = null;
        
        if (mNext != null)
        {
            result = mNext;
            mNext = null;
        }
        else
        {
            result = getNext();
        }
        
        if (result != null)
        {
            return result;
        }
        throw new NoSuchElementException();
    }

    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Gets the next value.
     * 
     * @return The next value to return, or <code>null</code> if there are
     *         no more values.
     */
    private U getNext()
    {
        // If we have a map value iterator then try that first
        if (mMapValueIterator != null)
        {
            if (mMapValueIterator.hasNext())
            {
                return mMapValueIterator.next();
            }
            else
            {
                mMapValueIterator = null;
            }
        }
        
        // If we get no joy there then try the map iterator to get more
        // values
        
        if (mMapIterator != null)
        {
            if (mMapIterator.hasNext())
            {
                List<U> values = mMapIterator.next();
                if (values != mExcludedList)
                {
                    mMapValueIterator = values.iterator();
                }
                return getNext();
            }
        }
        
        // Finally we iterate through the list
        if (mListIterator != null)
        {
            if (mListIterator.hasNext())
            {
                return mListIterator.next();
            }
        }
        
        return null;
    }
}
