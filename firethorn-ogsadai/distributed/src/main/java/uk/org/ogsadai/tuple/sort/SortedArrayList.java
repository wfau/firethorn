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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A sorted list implementation that stores the list in an array.
 *
 * @param <T> objects in the list
 * @author The OGSA-DAI Project Team.
 */
public class SortedArrayList<T extends Comparable<? super T>> 
    implements SortedList<T>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** The underlying array list */
    private List<T> mList;
    
    /**
     * Construct a new list.
     */
    public SortedArrayList()
    {
        mList = new ArrayList<T>();
    }
    
    /**
     * {@inheritDoc}
     */
    public void add(T object)
    {
        int index = Collections.binarySearch(mList, object);
        if (index >= 0)
        {
            mList.add(index, object);
        }
        else
        {
            mList.add(-index-1, object);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<T> iterator()
    {
        return mList.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public long size()
    {
        return mList.size();
    }

    /**
     * {@inheritDoc}
     */
    public T poll()
    {
        if (size() > 0)
        {
            return mList.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(T object)
    {
        return mList.contains(object);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return mList.isEmpty();
    }

}
