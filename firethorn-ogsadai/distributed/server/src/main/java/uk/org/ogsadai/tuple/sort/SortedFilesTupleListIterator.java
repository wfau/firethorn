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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.TupleInputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * A tuple list iterator that creates one sorted tuple list from a set of sorted
 * tuple lists stored in files.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SortedFilesTupleListIterator implements TupleListIterator
{
    /** Sorting columns. */
    private int[] mColumns;
    /** Tuple metadata. */
    private MetadataWrapper mMetadata;
    /** Input files. */
    private List<File> mFiles;
    /** Sorted list of the tuples at the top of the list, one from each file. */
    private SortedList<ComparableTupleAndInput> mTop = 
        new SortedTreeList<ComparableTupleAndInput>();
    /** Indicates whether the first tuples have been read. */
    private boolean mFirst = true;

    /**
     * {@inheritDoc}
     */
    public MetadataWrapper getMetadataWrapper() 
    {
        return mMetadata;
    }

    /**
     * {@inheritDoc}
     */
    public Object nextValue() 
        throws ActivityProcessingException
    {
        try
        {
            return nextTuple();
        } 
        catch (IOException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (ClassNotFoundException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (UnsupportedTupleTypeException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    /**
     * Sets the metadata for the tuple list.
     * 
     * @param metadata
     *            tuple metadata
     */
    public void store(MetadataWrapper metadata)
    {
        mMetadata = metadata;
    }

    /**
     * Sets names of the files that contain sorted tuple lists to merge.
     * 
     * @param files
     *            names of sorted files to merge
     */
    public void setFiles(List<File> files)
    {
        mFiles = files;
    }

    /**
     * Sets the columns by which the tuple list is ordered.
     * 
     * @param columns
     *            sort columns
     */
    public void setColumns(int[] columns)
    {
        mColumns = columns;
    }
    
    /**
     * Returns the next tuple in the sorted list.
     * 
     * @return a tuple
     * @throws IOException
     *             if there was a problem reading from the input files
     * @throws ClassNotFoundException
     *             if there was a problem deserialising tuples
     * @throws UnsupportedTupleTypeException 
     */
    public Tuple nextTuple() 
        throws IOException, 
               ClassNotFoundException, 
               UnsupportedTupleTypeException 
    {
        if (mFirst)
        {
            startReading();
            mFirst = false;
        }
        ComparableTupleAndInput key = mTop.poll();
        if (key == null)
        {
            return null;
        }
        
        TupleInputStream input = key.getInput();
        Tuple tuple;
        try
        {
            tuple = input.readTuple();
            insertSorted(new ComparableTuple(tuple, mColumns), input);
        } 
        catch (EOFException e)
        {
            input.close();
        } 
        catch (IOException e)
        {
            close();
            throw e;
        }
        return key.getTuple().getTuple();
    }

    /**
     * Open the input files and read a tuple from each.
     * 
     * @throws IOException
     *             if an IO error occurred
     * @throws ClassNotFoundException
     * @throws UnsupportedTupleTypeException
     */
    private void startReading()
        throws IOException, ClassNotFoundException, UnsupportedTupleTypeException
    {
        for (File file : mFiles)
        {
            TupleInputStream input = new TupleInputStream(
                    new FileInputStream(file), 
                    (TupleMetadata)mMetadata.getMetadata());
            Tuple tuple = input.readTuple();
            insertSorted(new ComparableTuple(tuple, mColumns), input);
        }
    }

    /**
     * Inserts a new tuple into the top list.
     * 
     * @param tuple
     *            tuple to insert
     * @param input
     *            the corresponding input stream
     */
    private void insertSorted(ComparableTuple tuple, TupleInputStream input)
    {
        ComparableTupleAndInput tupleinput = 
            new ComparableTupleAndInput(tuple, input);
        mTop.add(tupleinput);
    }
    
    /**
     * Closes the input streams.
     */
    public void close()
    {
        for (ComparableTupleAndInput tupleinput : mTop)
        {
            TupleInputStream stream = tupleinput.getInput();
            if (stream != null)
            {
                stream.close();
            }
        }
    }
    
    /**
     * Deletes all input files.
     */
    public void delete()
    {
        close();
        Iterator<File> iterator = mFiles.iterator();
        while (iterator.hasNext())
        {
            iterator.next().delete();
        }
    }

    /**
     * Contains a ComparableTuple and the TupleInputStream from which the tuple
     * has been read.
     * 
     * @author The OGSA-DAI Project Team.
     */
    private static class ComparableTupleAndInput 
        implements Comparable<ComparableTupleAndInput>
    {
        
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Tuple. */
        private ComparableTuple mTuple;
        /** Input stream. */
        private TupleInputStream mInput;

        /**
         * Constructor.
         * 
         * @param tuple
         *            the tuple
         * @param input
         *            the input stream
         */
        public ComparableTupleAndInput(
                ComparableTuple tuple, TupleInputStream input)
        {
            mTuple = tuple;
            mInput = input;
        }
        
        /**
         * {@inheritDoc}
         */
        public int compareTo(ComparableTupleAndInput compare)
        {
            return mTuple.compareTo(compare.mTuple);
        }
        
        /**
         * Returns the tuple.
         * @return tuple
         */
        public ComparableTuple getTuple()
        {
            return mTuple;
        }
        
        /**
         * Returns the input stream.
         * 
         * @return tuple input stream corresponding to the tuple
         */
        public TupleInputStream getInput()
        {
            return mInput;
        }
        
        /**
         * {@inheritDoc}
         */
        public String toString()
        {
            return "[" + mInput + "," + mTuple + "]";
        }
    }
}
