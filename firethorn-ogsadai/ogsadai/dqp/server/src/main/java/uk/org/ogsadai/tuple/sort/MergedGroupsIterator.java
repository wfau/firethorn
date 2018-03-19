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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.GroupInputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Reads groups from a list of ordered files and merges them. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class MergedGroupsIterator implements TupleListIterator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Metadata of the group tuples. */
    private MetadataWrapper mMetadata;
    /** Tuple inputs. */
    private SortedTreeList<TupleAndInput> mTupleInputs = 
        new SortedTreeList<TupleAndInput>();
    /** Grouping columns. */
    private int[] mColumns;
    /** Aggregate functions. */
    private ExecutableFunctionExpression[] mAggregates;
    
    /**
     * Constructs a new iterator.
     * 
     * @param files
     *            sorted files from which to read groups
     * @param metadata
     *            tuple metadata for tuples
     * @param columns
     *            group-by columns
     * @param aggregates
     *            aggregate function columns
     * @throws UnsupportedTupleTypeException 
     */
    public MergedGroupsIterator(
            List<File> files, 
            MetadataWrapper metadata,
            int[] columns,
            ExecutableFunctionExpression[] aggregates) 
        throws UnsupportedTupleTypeException 
    {
        mColumns = columns;
        mMetadata = metadata;
        mAggregates = aggregates;
        for (File file : files)
        {
            try
            {
                TupleAndInput ti = new TupleAndInput();
                ti.mInput = new GroupInputStream(
                        new FileInputStream(file), 
                        (TupleMetadata)metadata.getMetadata(),
                        mColumns,
                        mAggregates);
                try
                {
                    ti.mTuple = ti.mInput.readTuple();
                    mTupleInputs.add(ti);
                }
                catch (EOFException e)
                {
                    ti.mInput.close();
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public MetadataWrapper getMetadataWrapper() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        return mMetadata;
    }

    /**
     * {@inheritDoc}
     */
    public Object nextValue()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        if (mTupleInputs.isEmpty())
        {
            return null;
        }
        List<TupleAndInput> list = mTupleInputs.pollAll();
        GroupTuple merged = null;
        for (TupleAndInput ti : list)
        {
            try
            {
                TupleAndInput newelem = new TupleAndInput();
                newelem.mInput = ti.mInput;
                if (merged != null)
                {
                    merged.mergeGroups(ti.mTuple);
                }
                else
                {
                    merged = ti.mTuple;
                }
                newelem.mTuple = ti.mInput.readTuple();
                mTupleInputs.add(newelem);
            }
            catch (EOFException e)
            {
                ti.mInput.close();
            }
            catch (IOException e)
            {
                throw new ActivityProcessingException(e);
            }
        }
        return merged;
    }
    
    /**
     * Contains an input stream and a group tuple that has been read from the
     * input stream.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class TupleAndInput implements Comparable<TupleAndInput>
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Tuple. */
        public GroupTuple mTuple;
        /** Input stream. */
        public GroupInputStream mInput;
        
        /**
         * {@inheritDoc}
         */
        public int compareTo(TupleAndInput other)
        {
            return mTuple.compareTo(other.mTuple);
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean equals(Object other)
        {
            if (other instanceof TupleAndInput)
            {
                return (mTuple.equals(((TupleAndInput)other).mTuple));
            }
            else
            {
                return false;
            }
        }
        
        /**
         * {@inheritDoc}
         */
        public String toString()
        {
            return "TupleAndInput: tuple = " + mTuple + ", input = " + mInput;
        }
    }

}
