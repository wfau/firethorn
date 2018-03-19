package uk.org.ogsadai.activity.transform;

import java.io.IOException;

import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.TupleOutputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

public class TupleToByteArraysStreamActivity extends ActivityBase 
{

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    /** Activity input name of data input. */
    public static final String INPUT_DATA = "data";
    /** Activity input name of size input. */
    public static final String INPUT_SIZE = "size";
    /** Activity output name of result output. */
    public static final String OUTPUT = "result";
    /** The output block writer. */
    private BlockWriter mOutput;
    private BlockReader mData;
    private BlockReader mSize;
    
    private void prepare() 
    throws InvalidActivityInputsException, InvalidActivityOutputsException
    {
        validateInput(INPUT_DATA);
        mData = getInput(INPUT_DATA);
        validateInput(INPUT_SIZE);
        mSize = getInput(INPUT_SIZE);
        validateOutput(OUTPUT);
        mOutput = getOutput(OUTPUT);
    }
    
    @Override
    public void process() 
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
    {
        prepare();
        while (processIteration());
    }
     
    private boolean processIteration()
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
    {
        // determine the maximum block size
        Object block = readBlock(mSize);
        int arraySize;
        if (block == ControlBlock.NO_MORE_DATA)
        {
            // we're finished
            return false;
        }
        
        if (block instanceof Number)
        {
            arraySize = ((Number)block).intValue();
        }
        else throw new InvalidInputValueException(INPUT_SIZE, block);
            
        BytesOutputStream bytes = 
            new BytesOutputStream(mOutput, arraySize);
        TupleOutputStream output = new TupleOutputStream(bytes);
        block = readBlock(mData);
        if (block != ControlBlock.LIST_BEGIN)
        {
            throw new ActivityUserException(
                    new MalformedListBeginException(INPUT_DATA));
        }
        block = readBlock(mData);
        if (!(block instanceof MetadataWrapper))
        {
            throw new InvalidInputValueException(
                    INPUT_DATA,
                    MetadataWrapper.class, block.getClass());
        }
        if (!writeBlock(ControlBlock.LIST_BEGIN))
        {
            // no more data required
            return false;
        }
        try 
        {
            output.writeMetadata(
                    (TupleMetadata)((MetadataWrapper)block).getMetadata());
        } 
        catch (UnsupportedTupleTypeException e) 
        {
            throw new ActivityProcessingException(e);
        }
        catch (IOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        
        while (writeReadableBlocks(output));
        return writeBlock(ControlBlock.LIST_END);
    }

    private boolean writeReadableBlocks(TupleOutputStream output) 
        throws ActivityProcessingException,
               ActivityUserException,
               ActivityTerminatedException 
    {
        try 
        {
            int numBlocksReadable = mData.getNumBlocksReadable();
            while (numBlocksReadable == 0)
            {
                // flush in case there is anything in the buffer
                output.flush();
                // block until the next tuple is available
                if (!writeTuple(output))
                {
                    return false;
                }
                // now check again how many tuples are readable
                numBlocksReadable = mData.getNumBlocksReadable();
            }
            // if a number of tuples are readable then write them all out
            for (int i=0; i<numBlocksReadable; i++)
            {
                if (!writeTuple(output))
                {
                    return false;
                }
            }
            // write it all to the stream
            output.flush();
            return true;
            
        } 
        catch (IOException e) 
        {
            throw new ActivityProcessingException(e);
        } 
    }
    
    private Object readBlock(BlockReader reader) 
        throws ActivityProcessingException, ActivityTerminatedException, DataError
    {
        try
        {
            return reader.read();
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
    
    private boolean writeTuple(TupleOutputStream output) 
        throws ColumnNotFoundException, IOException, 
        ActivityUserException, 
        ActivityProcessingException, 
        ActivityTerminatedException
    {
        Object block = readBlock(mData);
        if (block == ControlBlock.LIST_END)
        {
            output.flush();
            return false;
        }
        if (block == ControlBlock.NO_MORE_DATA)
        {
            output.flush();
            throw new ActivityUserException(
                    new MalformedListEndException(INPUT_DATA));
        }
        if (block instanceof Tuple)
        {
            output.writeTuple((Tuple)block);
        }
        else
        {
            throw new InvalidInputValueException(INPUT_DATA,
                    Tuple.class, block.getClass());
        }
        return true;
    }
    
    private boolean writeBlock(Object block) 
        throws ActivityProcessingException, ActivityTerminatedException
    {
        try 
        {
            mOutput.write(block);
            return true;
        } 
        catch (PipeClosedException e) 
        {
            return false;
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

}
