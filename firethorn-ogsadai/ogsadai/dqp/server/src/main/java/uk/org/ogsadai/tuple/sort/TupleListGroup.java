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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.GroupOutputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Groups a tuple list.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TupleListGroup
{
    /** Lowest limit of free memory before storing to disk. */
    private static final long MEMORY_LIMIT = 100000;
    /** Minimum number of groups when storing to disk. */
    private static final long MIN_GROUPS = 1000;
    /** Maximum number of groups before storing to disk. */
    private static final long MAX_GROUPS = 100000;
    /** Tuple metadata. */
    private MetadataWrapper mMetadata;
    /** Output metadata. */
    private TupleMetadata mOutputMetadata;
    /** Current set of groups to which input tuples are written. */
    private SortedSet<GroupTuple> mCurrentGroups = 
        new TreeSet<GroupTuple>();
    /** Storage files. */
    private List<File> mFiles = new ArrayList<File>();
    /** Number of input tuples that have been read so far. */
    private int mCount;
    /** Aggregates. */
    private ExecutableFunctionExpression[] mAggregates;
    /** Grouping columns. */
    private int[] mColumns;

    /**
     * Constructs a new object.
     * 
     * @param columns
     *            indexes of the group by columns
     * @param aggregates
     *            aggregate functions for the non-group-by columns, indexed
     *            by the output column index
     */
    public TupleListGroup(
            int[] columns,
            ExecutableFunctionExpression[] aggregates)
    {
        mColumns = columns;
        mAggregates = aggregates;
    }
    
    /**
     * Write tuple metadata. This method must be called before any tuples are
     * written.
     * 
     * @param metadata
     *            tuple metadata
     * @throws TypeMismatchException
     */
    public void write(MetadataWrapper metadata) 
        throws TypeMismatchException
    {
        mMetadata = metadata;
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (ExecutableFunctionExpression function : mAggregates)
        {
            function.configure((TupleMetadata)metadata.getMetadata());
            
        }
        mOutputMetadata = new SimpleTupleMetadata(columns);
    }

    /**
     * Writes a new tuple to the groups.
     * 
     * @param tuple
     *            input tuple
     * @throws IOException
     * @throws UnsupportedTupleTypeException
     */
    public void write(Tuple tuple) 
        throws IOException,
               UnsupportedTupleTypeException
    {
        mCount++;
        storeGroups();
        GroupTuple comparable = new GroupTuple(tuple, mColumns);
        SortedSet<GroupTuple> tail = mCurrentGroups.tailSet(comparable);
        if (tail.isEmpty())
        {
            addNewGroup(comparable);
        }
        else
        {
            GroupTuple group = tail.first();
            if (group.equals(comparable))
            {
                group.evaluate(tuple);
            }
            else
            {
                addNewGroup(comparable);
            }
        }
        
    }
    
    /**
     * Adds a new group tuple to the list.
     * 
     * @param wrapped
     *            the tuple to add
     */
    private void addNewGroup(GroupTuple wrapped)
    {
        ExecutableFunctionExpression[] aggregates = 
            new ExecutableFunctionExpression[mAggregates.length];
        for (int i=0; i<aggregates.length; i++)
        {
            aggregates[i] = 
                (ExecutableFunctionExpression)CloneArithmeticExprVisitor
                    .cloneExpression(mAggregates[i]);
        }
        wrapped.setAggregates(aggregates);
        mCurrentGroups.add(wrapped);
    }
    
    /**
     * Indicates that the last tuple has been written.
     * 
     * @throws IOException
     * @throws UnsupportedTupleTypeException 
     */
    public void close() throws IOException, UnsupportedTupleTypeException
    {
        if (!mFiles.isEmpty())
        {
            writeGroupsToFile();
            System.gc();
        }
    }

    /**
     * Decides whether the current group set is written to file.
     * 
     * @throws IOException
     *             if a runtime IO error occurred
     * @throws UnsupportedTupleTypeException
     *             if serialisation of a tuple type is not supported
     */
    protected void storeGroups() throws UnsupportedTupleTypeException
    {
        // check if the memory is low and write to disk if there are many groups
        // write to disk if the dataset gets too big
        if ((mCurrentGroups.size() > MIN_GROUPS &&
                Runtime.getRuntime().freeMemory() < MEMORY_LIMIT) ||
                mCurrentGroups.size() > MAX_GROUPS)
        {
            writeGroupsToFile();
            mCurrentGroups = new TreeSet<GroupTuple>();
            System.gc(); System.gc(); System.gc();
        }
    }

    /**
     * Dumps the current group list to a temporary file.
     * 
     * @throws IOException
     *             if an IO error occurred
     * @throws UnsupportedTupleTypeException
     *             if a column type is not supported by the serialiser
     */
    protected void writeGroupsToFile() 
        throws IOException, 
               UnsupportedTupleTypeException
    {
        GroupOutputStream output = null;
        try
        {
            File file = File.createTempFile("group", null);
            mFiles.add(file);
            output = new GroupOutputStream(new FileOutputStream(file));
            Iterator<GroupTuple> iterator = mCurrentGroups.iterator();
            output.write(
                    (TupleMetadata)mMetadata.getMetadata(),
                    mColumns, 
                    mAggregates);
            while (iterator.hasNext())
            {
                output.writeTuple(iterator.next());
            }
        }
        catch (java.io.IOException e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (output != null)
            {
                output.close();
            }
            mCurrentGroups = null;
        }
    }
    
    /**
     * Returns a tuple list iterator which iterates through all tuple groups.
     * 
     * @return iterator
     * @throws UnsupportedTupleTypeException 
     */
    public TupleListIterator getTupleListIterator()
        throws UnsupportedTupleTypeException
    {
        if (mFiles.isEmpty())
        {
            return new SortedSetTupleListIterator(mCurrentGroups);
        }
        else
        {
            return new MergedGroupsIterator(
                    mFiles, 
                    mMetadata, 
                    mColumns, 
                    mAggregates);
        }
    }
    
    /**
     * A tuple list iterator that returns the groups that are stored in memory.
     * 
     * @author The OGSA-DAI Project Team.
     */
    private class SortedSetTupleListIterator implements TupleListIterator
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** List iterator. */
        private Iterator<GroupTuple> mIterator;

        /**
         * Constructor.
         * 
         * @param currentGroups
         *            the group list
         */
        public SortedSetTupleListIterator(SortedSet<GroupTuple> currentGroups)
        {
            mIterator = currentGroups.iterator();
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
            if (mIterator.hasNext())
            {
                return mIterator.next();
            }
            else 
            {
                return null;
            }
        }
        
    }

    // for testing
    List<File> getFiles()
    {
        return mFiles;
    }
    
}
