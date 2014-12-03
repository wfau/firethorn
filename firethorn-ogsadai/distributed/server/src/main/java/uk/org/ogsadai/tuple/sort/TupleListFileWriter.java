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

import java.util.concurrent.Callable;

/**
 * A callable that serialises a tuple list and writes it to file.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TupleListFileWriter implements Callable
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Tuple list sorter. */
    private final TupleListSort mSort;
    /** Sorted tuple list. */
    private SortedList<ComparableTuple> mList;
    
    /**
     * Constructs a new file writer.
     * 
     * @param sort
     *            the object that actually writes the file
     * @param list
     *            a sorted list of tuples
     */
    public TupleListFileWriter(
            TupleListSort sort, 
            SortedList<ComparableTuple> list)
    {
        mSort = sort;
        mList = list;
    }

    /**
     * {@inheritDoc}
     */
    public Object call() throws Exception
    {
        mSort.writeList(mList);
        mList = null;
        System.gc();
        return null;
    }

    
    
    
}
