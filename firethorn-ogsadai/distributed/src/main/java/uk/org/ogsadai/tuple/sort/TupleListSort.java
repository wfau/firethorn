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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.TupleOutputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Sorts a tuple list. Tuples are written to a sorted list until the list grows
 * too large, then the contents are dumped to disk.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleListSort
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Minimum free memory below which the current list is written to file.*/
    private static final long MEMORY_LIMIT = 100000;
    /** Maximum length of the tuple list before it is written to file. */
    private static final int MAX_LIST_LENGTH = 100000;
    
    /** Current sorted tuple list (in-memory). */
    private SortedList<ComparableTuple> mCurrentList = 
        new SortedTreeList<ComparableTuple>();

    /** Names of the output files. */
    private List<File> mFiles = new ArrayList<File>();
    
    /** Order by columns. */
    private int[] mColumns;
    
    private SortOrder[] mSortOrders = null;
    
    /** Number of tuples that have been written. */
    private long mCount;
    /** Tuple metadata. */
    private MetadataWrapper mMetadata;
    
    /** Concurrency utility - task executor. */
    private ExecutorService mExecute;
    /**
     * The current file writer that writes the previous tuple list to file, or
     * <code>null</code> if no file is currently being written.
     */
    private Future mCurrentFileWriter;
        
    /**
     * Constructs a new sort that orders by the specified columns.
     * 
     * @param columns
     *            sorting columns
     */
    public TupleListSort(int[] columns)
    {
        mColumns = columns;
        mExecute = Executors.newCachedThreadPool();
    }

    public TupleListSort(int[] columns, SortOrder[] sortOrders)
    {
    	this(columns);
    	
    	if(columns.length != sortOrders.length)
    	{
			throw new RuntimeException(
					"The size of columns array and sortOrders array must be the same.");
    	}
    	mSortOrders = sortOrders;
    }

    /**
     * Writes the tuple list to a file.
     * 
     * @param list
     *            list to write
     * @throws UnsupportedTupleTypeException
     *             if a tuple type is not supported
     * @throws SortException
     *             if an IO error or some other problem occurred
     */
    public void writeList(SortedList<ComparableTuple> list) 
        throws UnsupportedTupleTypeException
    {
        TupleOutputStream output = null; 
        try
        {
            File file = File.createTempFile("sort", null);
            mFiles.add(file);
            output = new TupleOutputStream(new FileOutputStream(file));
            output.writeMetadata((TupleMetadata)mMetadata.getMetadata());
            for (ComparableTuple tuple : list)
            {
                output.writeTuple(tuple.getTuple());
            }
        }
        catch (IOException e)
        {
            throw new SortException(e);
        }
        finally
        {
            if (output != null)
            {
                output.close();
                output = null;
            }
        }
    }

    /**
     * Writes the current list to file. Only one list is written at any one
     * time. If a writer is already executing this method waits until it has
     * finished.
     * 
     * @throws SortException if an IO error occurred or some other problem
     */
    private void writeToFile()
    {
        if (mCurrentFileWriter != null)
        {
            // wait until the previous file writer has finished
            try
            {
                mCurrentFileWriter.get();
            } 
            catch (Exception e)
            {
                throw new SortException(e);
            }
        }
        TupleListFileWriter writer = new TupleListFileWriter(this, mCurrentList);
        mCurrentFileWriter = mExecute.submit(writer);
        mCurrentList = new SortedTreeList<ComparableTuple>();
    }
    
    /**
     * Writes the metadata wrapper for this tuple list.
     * 
     * @param metadata
     *            tuple metadata
     */
    public void write(MetadataWrapper metadata)
    {
        mMetadata = metadata;
    }

    /**
     * Writes a tuple.
     * 
     * @param tuple
     *            tuple to add to the sorted lists
     * @throws SortException
     */
    public void write(Tuple tuple)
    {
        mCount++;
        if (Runtime.getRuntime().freeMemory() < MEMORY_LIMIT 
                || mCurrentList.size() >= MAX_LIST_LENGTH)
        {
            writeToFile();
        }
        mCurrentList.add(new ComparableTuple(tuple, mColumns, mSortOrders));
    }
    
    /**
     * Indicates that no more data will be written.
     * @throws SortException
     */
    public void close()
    {
        if (mFiles.size() > 0)
        {
            writeToFile();          
            try
            {
                mCurrentFileWriter.get();
            } 
            catch (Exception e)
            {
                throw new SortException(e);
            }
        }
    }

    /**
     * Returns the list of files containing the sorted tuple lists. If complete
     * tuple list was stored in memory it will be written to a file now.
     * 
     * @return list of tuple list files
     * @throws SortException
     *             if a list could not be written to a file
     */
    public List<File> getFiles() 
    {
        if (mFiles.size() == 0)
        {
            writeToFile();
            try
            {
                mCurrentFileWriter.get();
            } 
            catch (Exception e)
            {
                throw new SortException(e);
            }
        }
        return mFiles;
    }
    
    /**
     * Returns a tuple list iterator for the sorted tuple list. 
     * 
     * @throws IllegalStateException
     *             if <code>sort</code> has not been called first
     * @return sorted tuple list
     */
    public TupleListIterator getTupleListIterator()
    {
        if (mFiles.size() == 0)
        {
            return new SortedTupleListIterator(mMetadata, mCurrentList);
        }
        else
        {
            SortedFilesTupleListIterator iter = 
                new SortedFilesTupleListIterator();
            iter.setColumns(mColumns);
            iter.setFiles(mFiles);
            iter.store(mMetadata);
            return iter;
        }
    }
    
    
}
