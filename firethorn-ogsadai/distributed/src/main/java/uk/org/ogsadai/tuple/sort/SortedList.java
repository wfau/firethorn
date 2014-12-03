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

/**
 * A sorted list of objects.
 *
 * @param <T> object type
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface SortedList<T extends Comparable<? super T>> extends Iterable<T>
{
    /**
     * Adds a new object.
     * 
     * @param object
     *            the object to add to the list
     */
    public void add(T object);

    /**
     * Returns <code>true</code> if the list contains the specified element.
     * 
     * @param object
     *            element whose presence in the list is to be tested
     * @return <code>true</code> if the list contains the specified element,
     *         <code>false</code> otherwise
     */
    public boolean contains(T object);
    
    /**
     * Returns the size of the list.
     * 
     * @return list length
     */
    public long size();

    /**
     * Returns and removes the smallest element in the list.
     * 
     * @return smallest element
     */
    public T poll();
    
    /**
     * Returns true if the list contains no elements.
     * 
     * @return <code>true</code> if the list is empty
     */
    public boolean isEmpty();

}
