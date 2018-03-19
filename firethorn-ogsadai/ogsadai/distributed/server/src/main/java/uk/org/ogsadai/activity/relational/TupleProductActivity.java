// Copyright (c) The University of Edinburgh, 2008 - 2009.
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


package uk.org.ogsadai.activity.relational;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.TupleInputStream;
import uk.org.ogsadai.tuple.serialise.TupleOutputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Constructs the product from two tuple inputs. This activity iterates and
 * stores one tuple list in full before iterating through the other tuple
 * list and producing the product. If the list is too long to be stored in 
 * memory it is written to a file.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>data2</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>readFirst</code>. Type: {@link java.lang.String} The name of the
 * data input that is read first (and stored). 
 * This is an optional input with the default value <code>data2</code>.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The product of the two input data streams.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A value is read from the readFirst input, then a complete tuple list is
 * read from one data input (as specified by the readFirst value), and finally
 * the other data input is streamed through, producing product tuples by 
 * combining each streamed tuple with each of the stored tuples.</li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Produces the product of the two input tuple lists, that is, each tuple on
 * the left is merged with each tuple on the right, producing a tuple list of
 * length n*m if n is the length of data list 1 and m is the length of data 
 * list 2.
 * </li>
 * <li>
 * The input stream of the readFirst input is stored. 
 * If the length of the
 * input lists is known in advance, or can be estimated, the user should ensure
 * that the longer list is connected to the other input for minimum
 * memory/storage use.
 * </li>
 * <li>
 * By default, the input from data2 is read first and stored.
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   data1: { metadataA (1, 2) (3, 4) }
 *   data2: { metadataB (a, b) (c, d) }
 *   result: { metadataAB (1, 2, a, b) (1, 2, c, d) (3, 4, a, b) (3, 4, c, d) }
 * </pre>
 * <pre>
 *   readFirst: data1
 *   data1: { metadataA (1, 2) (3, 4) }
 *   data2: { metadataB (a, b) (c, d) }
 *   result: { metadataAB (1, 2, a, b) (3, 4, a, b) (1, 2, c, d) (3, 4, c, d) }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleProductActivity extends IterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    /** Activity input name - data1. */
    public static final String INPUT_DATA_1 = "data1";
    
    /** Activity input name - data2. */
    public static final String INPUT_DATA_2 = "data2";

    /** Activity input name - read first. */
    public static final String INPUT_READ_FIRST = "readFirst";
    
    /** Activity output name - result. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Activity output. */
    public BlockWriter mOutput;

    private TupleListActivityInput mDataInputLeft;

    private TupleListActivityInput mDataInputRight;

    private TypedActivityInput mReadFirst;
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
        validateInput(INPUT_DATA_1);
        validateInput(INPUT_DATA_2);
        mDataInputLeft = new TupleListActivityInput(INPUT_DATA_1);
        mDataInputLeft.setBlockReader(getInput(INPUT_DATA_1));
        mDataInputRight = new TupleListActivityInput(INPUT_DATA_2);
        mDataInputRight.setBlockReader(getInput(INPUT_DATA_2));
        BlockReader input = getInput(INPUT_READ_FIRST);
        if (input != null)
        {
            mReadFirst = new TypedActivityInput(INPUT_READ_FIRST, String.class);
            mReadFirst.setBlockReader(input);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration()
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        // by default we stream the left side and store the right side of the product
        boolean readLeftFirst = false;
        TupleListActivityInput storedInput = mDataInputRight;
        TupleListActivityInput streamedInput = mDataInputLeft;
        if (mReadFirst != null)
        {
            Object block = mReadFirst.read();
            if (block == ControlBlock.NO_MORE_DATA)
            {
                iterativeStageComplete();
                return;
            }
            if (INPUT_DATA_1.equals((String)block))
            {
                readLeftFirst = true;
                storedInput = mDataInputLeft;
                streamedInput = mDataInputRight;
            }
        }
        Object block = storedInput.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            iterativeStageComplete();
            return;
        }
        TupleListIterator stored = (TupleListIterator)block;
        TupleMetadata storedMetadata = 
            (TupleMetadata) stored.getMetadataWrapper().getMetadata();
        TupleList storedTuples = readTuples(stored);
        TupleListIterator streamed = (TupleListIterator)streamedInput.read();
        TupleMetadata streamedMetadata = 
            (TupleMetadata) streamed.getMetadataWrapper().getMetadata();
        writeBlock(ControlBlock.LIST_BEGIN);
        if (readLeftFirst)
        {
            writeMetadata(storedMetadata, streamedMetadata);
        }
        else
        {
            writeMetadata(streamedMetadata, storedMetadata);
        }
        Tuple tuple;
        while ((tuple = (Tuple)streamed.nextValue()) != null)
        {
            Iterator<Tuple> iterator = storedTuples.getIterator();
            while (iterator.hasNext())
            {
                Tuple storedTuple = iterator.next();
                if (readLeftFirst)
                {
                    writeBlock(new SimpleTuple(storedTuple, tuple));
                }
                else
                {
                    writeBlock(new SimpleTuple(tuple, storedTuple));
                }
            }
        }
        writeBlock(ControlBlock.LIST_END);
        storedTuples.delete();
        storedTuples = null;
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess()
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
    {
        // no post-processing
    }

    /**
     * Constructs metadata for the product of the given tuple lists and writes
     * it to the output.
     * 
     * @param metadata1
     *            first tuple list metadata
     * @param metadata2
     *            second tuple list metadata
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private void writeMetadata(
            TupleMetadata metadata1, 
            TupleMetadata metadata2) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (int i=0; i<metadata1.getColumnCount(); i++)
        {
            columns.add(metadata1.getColumnMetadata(i));
        }
   
        for (int i=0; i<metadata2.getColumnCount(); i++)
        {
            columns.add(metadata2.getColumnMetadata(i));
        }
        
        TupleMetadata output = new SimpleTupleMetadata(columns);
        MetadataWrapper wrapper = new MetadataWrapper(output);
        writeBlock(wrapper);
    }

    /**
     * Writes a block to the output.
     * 
     * @param block
     *            output block to write
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private void writeBlock(Object block) 
        throws ActivityProcessingException, ActivityTerminatedException
    {
        try
        {
            mOutput.write(block);
        } 
        catch (PipeClosedException e)
        {
            // complete
            iterativeStageComplete();
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }

    }
    
    /**
     * Reads all tuples in the list. If the list grows to large to fit into
     * memory it is stored to a temporary file.
     * 
     * @param tuples
     *            input tuple iterator
     * @return a list of tuples
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private TupleList readTuples(TupleListIterator tuples)
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        TupleMetadata metadata = 
            (TupleMetadata)tuples.getMetadataWrapper().getMetadata();
        List<Tuple> list = new LinkedList<Tuple>();
        TupleOutputStream output = null;
        Tuple block;
        File file = null;
        int count = 0;
        while ((block=(Tuple)tuples.nextValue()) != null)
        {
            count++;
            if (output == null)
            {
                list.add(block);
                if (Runtime.getRuntime().freeMemory() < 1000000)
                {
                    try
                    {
                        file = File.createTempFile("OGSA-DAI-", null);
                        output = new TupleOutputStream(new FileOutputStream(file));
                        output.writeMetadata(metadata);
                        for (Tuple tuple : list)
                        {
                            output.writeTuple(tuple);
                        }
                        list = null;
                    }
                    catch (IOException e)
                    {
                        throw new ActivityProcessingException(e);
                    }
                    catch (UnsupportedTupleTypeException e)
                    {
                        throw new ActivityProcessingException(e);
                    }
                }
            }
            else 
            {
                try
                {
                    output.writeTuple(block);
                }
                catch (Throwable e)
                {
                    throw new ActivityProcessingException(e);
                }
            }
        }
        if (output != null)
        {
            output.close();
            try
            {
                return new TupleListFile(file, metadata);
            }
            catch (Exception e)
            {
                throw new ActivityProcessingException(e);
            }
        }
        else
        {   
            return new WrappedTupleList(list);
        }

    }       
    
    /**
     * Provides access to a tuple list. This is basically a resettable iterator.
     *
     * @author The OGSA-DAI Project Team.
     */
    private static interface TupleList
    {
        /**
         * Gets a new iterator.
         * 
         * @return iterator
         */
        public Iterator<Tuple> getIterator();

        /**
         * Indicates that the list is no longer used and allows the implementing
         * class to release resources.
         */
        public void delete();
    }
    
    /**
     * Iterates over tuples in a file.
     *
     * @author The OGSA-DAI Project Team.
     */
    private static class TupleListFile implements TupleList
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Tuple metadata. */
        private TupleMetadata mMetadata;
        /** File that stores the tuples. */
        private File mFile;

        /**
         * Constructor.
         * 
         * @param file
         *            file that stores tuples
         * @param metadata
         *            tuple metadata
         */
        public TupleListFile(File file, TupleMetadata metadata)
        {
            mFile = file;
            mMetadata = metadata;
        }
        
        /**
         * {@inheritDoc}
         */
        public Iterator<Tuple> getIterator()
        {
            return new TupleListFileIterator(mFile, mMetadata);
        }

        /**
         * {@inheritDoc}
         */
        public void delete()
        {
            mFile.delete();
        }
        
    }
    
    /**
     * Iterates over a tuple list stored in a file.
     * 
     * @author The OGSA-DAI Project Team.
     */
    private static class TupleListFileIterator implements Iterator<Tuple>
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Tuple input stream to read from. */
        private TupleInputStream mInput;
        /** Currently available tuple. */
        private Tuple mCurrent;

        /**
         * Constructor.
         * 
         * @param file
         *            file to read from
         * @param metadata
         *            tuple metadata
         */
        public TupleListFileIterator(File file, TupleMetadata metadata)
        {
            try
            {
                mInput = new TupleInputStream(new FileInputStream(file), metadata);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedTupleTypeException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            if (mInput == null)
            {
                return false;
            }
            try
            {
                if (mCurrent == null)
                {
                    mCurrent = mInput.readTuple();
                }
                return true;
            }
            catch (EOFException e)
            {
                mInput.close();
                return false;
            }
            catch (IOException e)
            {
                mInput.close();
                mInput = null;
                return false;
            }
        }

        /**
         * {@inheritDoc}
         */
        public Tuple next()
        {
            if (hasNext())
            {   
                Tuple result = mCurrent;
                mCurrent = null;
                return result;
            }
            else
            {
                throw new NoSuchElementException();
            }
            
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
    }
    
    /**
     * Wraps a tuple list stored in memory.
     *
     * @author The OGSA-DAI Project Team.
     */
    private static class WrappedTupleList implements TupleList
    {
        
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Wrapped tuple list. */
        private List<Tuple> mTuples;

        /**
         * Constructor.
         * 
         * @param list
         *            list to wrap
         */
        public WrappedTupleList(List<Tuple> list)
        {
            mTuples = list;
        }

        /**
         * {@inheritDoc}
         */
        public Iterator<Tuple> getIterator()
        {
            return mTuples.iterator();
        }

        /**
         * {@inheritDoc}
         */
        public void delete()
        {
            mTuples = null;
        }
        
    }
    
}
