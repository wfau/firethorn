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

import java.util.Iterator;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * Iterates over a sorted tuple list.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SortedTupleListIterator implements TupleListIterator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Underlying iterator. */
    private Iterator<ComparableTuple> mTuples;
    /** Tuple Metadata. */
    private MetadataWrapper mMetadata;

    /**
     * Constructs a new iterator.
     * 
     * @param metadata
     *            tuple metadata
     * @param currentList
     *            the tuple list
     */
    public SortedTupleListIterator(
            MetadataWrapper metadata, 
            SortedList<ComparableTuple> currentList)
    {
        mTuples = currentList.iterator();
        mMetadata = metadata;
    }

    /**
     * {@inheritDoc}
     */
    public MetadataWrapper getMetadataWrapper() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        return mMetadata;
    }

    /**
     * {@inheritDoc}
     */
    public Object nextValue() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        if (mTuples.hasNext())
        {
            return mTuples.next().getTuple();
        }
        else
        {
            return null;
        }
    }

}
