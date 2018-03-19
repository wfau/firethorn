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

package uk.org.ogsadai.tuple.join;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A tree map that can store multiple values for each key. The tree is sorted
 * according to the key and allows efficient selection of ranges.
 * 
 * @author The OGSA-DAI Project Team.
 * @param <T>
 * @param <U>
 */
public class MultiValueTreeMap<T, U>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /**
     * The TreeMap used as the basis of the implementation. To handle multiple
     * values this map points to a list of values.
     */
    private SortedMap<T, List<U>> mTreeMap;

    /**
     * Constructs an empty tree map.
     */
    public MultiValueTreeMap()
    {
        mTreeMap = new TreeMap<T, List<U>>();
    }

    /**
     * Constructs an empty tree map with Comparator comparator for ordering.
     * 
     * @param comparator
     *            a comparator for ordering the TreeMap.
     */
    public MultiValueTreeMap(Comparator<Object> comparator)
    {
        mTreeMap = new TreeMap<T, List<U>>(comparator);
    }

    /**
     * Adds the given object value to the map using the given key.
     * 
     * @param key
     *            key used to access the value
     * @param value
     *            value to store
     */
    public void add(T key, U value)
    {
        List<U> list = mTreeMap.get(key);
        if (list == null)
        {
            list = new LinkedList<U>();
            mTreeMap.put(key, list);
        }
        list.add(value);
    }

    /**
     * Gets the value objects whose key is less than the specified key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects.
     */
    public Iterator<U> getLessThan(T key)
    {
        // Get the head map, this will not contain any equal values
        Map<T, List<U>> headMap = mTreeMap.headMap(key);

        // Build the iterator
        return buildIterator(headMap, null);
    }

    /**
     * Gets the value objects whose key is greater than the specified key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getGreaterThan(T key)
    {
        // Get the head map, this will contain any equal values
        Map<T, List<U>> tailMap = mTreeMap.tailMap(key);

        // Build the iterator
        return buildIterator(tailMap, null, key);
    }

    /**
     * Gets the value objects whose key is less than or equal to the specified
     * key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getLessThanOrEqual(T key)
    {
        // Get the head map, this will not contain any equal values
        Map<T, List<U>> headMap = mTreeMap.headMap(key);

        // Get the equal values
        List<U> equalList = mTreeMap.get(key);

        // Build the iterator
        return buildIterator(headMap, equalList);
    }

    /**
     * Gets the value objects whose key is greater than or equal to the
     * specified key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getGreaterThanOrEqual(T key)
    {
        // Get the head map, this will contain any equal values
        Map<T, List<U>> tailMap = mTreeMap.tailMap(key);

        // Build the iterator
        return buildIterator(tailMap, null);
    }

    /**
     * Gets the value objects whose key is equal to the specified key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getEqual(T key)
    {
        // Get the equal values
        List<U> equalList = mTreeMap.get(key);

        // Build the iterator
        return buildIterator(null, equalList);
    }

    /**
     * Gets the value objects whose key is not equal to the specified key.
     * 
     * @param key
     *            key
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getNotEqual(T key)
    {
        // Build the iterator
        return buildIterator(mTreeMap, null, key);
    }

    /**
     * Gets the value objects whose key is greater than <code>fromKey</code> (or
     * equal to if <code>includeFrom</code> is <code>true</code>) and less than
     * <code>toKey</code> (or equal to if <code>includeTo</code> is
     * <code>true</code>).
     * 
     * @param fromKey
     *            key to range from
     * @param toKey
     *            key to range to
     * @param includeFrom
     *            include the values corresponding to the fromKey if
     *            <code>true</code>
     * @param includeTo
     *            include the values corresponding the toKey if
     *            <code>true</code>
     * @return An iterator to the value objects.
     */
    public Iterator<U> getRange(T fromKey, T toKey, 
            boolean includeFrom,
            boolean includeTo)
    {
        // Get the sub map, this will contain the fromKey and exclude the toKey
        Map<T, List<U>> subMap = mTreeMap.subMap(fromKey, toKey);

        T excludedKey = null;
        List<U> equalList = null;
        if (!includeFrom)
        {
            excludedKey = fromKey;
        }
        if (includeTo)
        {
            // Get the equal values
            equalList = mTreeMap.get(toKey);
        }

        // Build the iterator
        return buildIterator(subMap, equalList, excludedKey);

    }

    /**
     * Gets all value objects.
     * 
     * @return An iterator to the value objects
     */
    public Iterator<U> getAll()
    {
        return buildIterator(mTreeMap, null);
    }

    /**
     * Builds an iterator that will access the values in the given map and list
     * starting with those in the map and the those in the list.
     * 
     * @param map
     *            The map. The interator returned will give access to the values
     *            of the map sorted by their keys. Can be <code>null</code>.
     * 
     * @param list
     *            The list. The interator returned will give access to the
     *            values in the list in their current order. Can be
     *            <code>null</code>.
     * 
     * @return iterator given access to the data.
     */
    private Iterator<U> buildIterator(Map<T, List<U>> map, List<U> list)
    {
        return buildIterator(map, list, null);
    }

    /**
     * Builds an iterator that will access the values in the given map and list
     * starting with those in the map and the those in the list but excludes
     * values associated with the given key.
     * 
     * @param map
     *            the map. The iterator returned will give access to the values
     *            of the map sorted by their keys. Can be <code>null</code>.
     * 
     * @param list
     *            the list. The iterator returned will give access to the values
     *            in the list in their current order. Can be <code>null</code>.
     * 
     * @param excludedKey
     *            all values in the map associated with this key will be
     *            excluded from the result.
     * 
     * @return iterator giving access to the data.
     */
    private Iterator<U> buildIterator(Map<T, List<U>> map, List<U> list,
            T excludedKey)
    {
        return new MultiValueTreeMapIterator<T, U>(map, list, excludedKey);
    }
}
