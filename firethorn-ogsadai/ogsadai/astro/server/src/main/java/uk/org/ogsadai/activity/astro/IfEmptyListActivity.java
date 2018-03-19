package uk.org.ogsadai.activity.astro;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.metadata.MetadataWrapper;

public class IfEmptyListActivity extends IterativeActivity
{
    
    public static final String INPUT_DATA = "data";
    public static final String INPUT_CONTENT = "content";
    public static final String OUTPUT_EMPTY = "outputEmpty";
    public static final String OUTPUT_NON_EMPTY = "outputNonEmpty";
    private BlockWriter mOutputEmpty;
    private BlockWriter mOutputNonEmpty;
    private TypedListActivityInput mContent;
    private TypedListActivityInput mData;

    @Override
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        validateInput(INPUT_DATA);
        validateInput(INPUT_CONTENT);
        mContent = new TypedListActivityInput(INPUT_CONTENT, Object.class);
        mContent.setBlockReader(getInput(INPUT_CONTENT));
        mData = new TypedListActivityInput(INPUT_DATA, Object.class);
        mData.setBlockReader(getInput(INPUT_DATA));
        
        validateOutput(OUTPUT_EMPTY);
        validateOutput(OUTPUT_NON_EMPTY);
        mOutputEmpty = getOutput(OUTPUT_EMPTY);
        mOutputNonEmpty = getOutput(OUTPUT_NON_EMPTY);
    }

    @Override
    protected void processIteration()
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException 
    {
        try 
        {
            Object obj = mContent.read();
            if (obj == ControlBlock.NO_MORE_DATA)
            {
                return;
            }
            ListIterator contentList = (ListIterator) obj;
            List<Object> contents = getContents(contentList);
            
            Object block = mData.read();
            while (block != ControlBlock.NO_MORE_DATA)
            {
                ListIterator iterator = (ListIterator) block;
                boolean isEmpty = true;
                Object data;
                MetadataWrapper metadata = null;
                
                while ((data = iterator.nextValue()) != null)
                {
                    if (data instanceof MetadataWrapper)
                    {
                        metadata = (MetadataWrapper) data;
                    }
                    else
                    {
                        if (isEmpty)
                        {
                            mOutputNonEmpty.write(ControlBlock.LIST_BEGIN);
                            if (metadata != null)
                            {
                                mOutputNonEmpty.write(metadata);
                            }
                            isEmpty = false;
                        }
                        mOutputNonEmpty.write(data);
                    }
                }
                
                if (isEmpty)
                {
                    mOutputEmpty.write(ControlBlock.LIST_BEGIN);
                    for (Object object : contents)
                    {
                        mOutputEmpty.write(object);
                    }
                    mOutputEmpty.write(ControlBlock.LIST_END);
                }
                else
                {
                    mOutputNonEmpty.write(ControlBlock.LIST_END);
                }
                
                block = mData.read();
            }
            
        }
        catch (PipeIOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e) 
        {
            throw new ActivityTerminatedException();
        }
        catch (PipeClosedException e) 
        {
            // finished processing
            iterativeStageComplete();
        }
        iterativeStageComplete();
    }

    private List<Object> getContents(ListIterator iterator)
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException 
    {
        List<Object> result = new ArrayList<Object>();
        Object block;
        while ((block = iterator.nextValue()) != null)
        {
            result.add(block);
        }
        return result;
    }

    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post processing
    }

}
