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


package uk.org.ogsadai.tuple.sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * A tree implementation of a sorted list.
 *
 * @param <T> element type
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SortedTreeList<T extends Comparable<? super T>> 
    implements SortedList<T>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    private TreeMap<T, List<T>> mMap = new TreeMap<T, List<T>>();
    
    /**
     * {@inheritDoc}
     */
    public void add(T data)
    {
        List<T> values = mMap.get(data);
        if (values != null)
        {
            values.add(data);
        }
        else
        {
            List<T> v = new ArrayList<T>();
            v.add(data);
            mMap.put(data, v);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean contains(T data)
    {
        return mMap.containsKey(data);
    }
    
    /**
     * {@inheritDoc}
     */
    public Iterator<T> iterator()
    {
        return new TreeIterator();
    }
    
    /**
     * {@inheritDoc}
     */
    public long size()
    {
        return mMap.size();
    }
    
    /**
     * Returns a list of all duplicates of the current smallest object and
     * removes them.
     * 
     * @return list of smallest objects
     */
    public List<T> pollAll()
    {
        T firstKey = mMap.firstKey();
        List<T> result = mMap.get(firstKey);
        mMap.remove(firstKey);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public T poll()
    {
        if (!mMap.isEmpty())
        {
            T key = mMap.firstKey();
            List<T> first = mMap.get(key);
            T result = first.remove(0);
            if (first.isEmpty())
            {
                mMap.remove(key);
            }
            return result;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return mMap.isEmpty();
    }

    /** 
     * An iterator implementation.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class TreeIterator implements Iterator<T>
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
       
        /** Iterates through the list of duplicates on the current node. */
        private Iterator<T> mCurrentDataIterator;
        private Iterator<T> mKeys;
        
        public TreeIterator()
        {
            mKeys = mMap.keySet().iterator();
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            if (mCurrentDataIterator == null || !mCurrentDataIterator.hasNext())
            {
                mCurrentDataIterator = null;
                return mKeys.hasNext();
            }
            else
            {
                return mCurrentDataIterator.hasNext();
            }
        }
        
        /**
         * {@inheritDoc}
         */
        public T next()
        {
            if (mCurrentDataIterator != null)
            {
                return mCurrentDataIterator.next();
            }
            else
            {
                if (mKeys.hasNext())
                {
                    List<T> list = mMap.get(mKeys.next());
                    mCurrentDataIterator = list.iterator();
                    return mCurrentDataIterator.next();
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        
    }
    
}
