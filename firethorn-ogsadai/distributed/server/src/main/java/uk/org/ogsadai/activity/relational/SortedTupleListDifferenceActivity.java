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


package uk.org.ogsadai.activity.relational;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.sort.ComparableTuple;

/** 
 * This activity performs a set difference on the input lists. It is assumed
 * that both input lists are sorted.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data from which the other list is removed.</li>
 * <li> <code>data2</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data which is removed from the other list.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A list begin marker and the metadata block is read from each input before
 * processing starts.</li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>The server-side activity removes occurrences of tuples of the
 * <code>data2</code> list from the <code>data1</code> input list.</li>
 * <li>The input lists are assumed to be sorted by all columns.</li>
 * <li>If the metadata of the input lists do not match a user exception
 * <code>MISMATCHED_METADATA_ERROR</code> is thrown.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SortedTupleListDifferenceActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    /** Activity input name - data. */
    public static final String INPUT_DATA_1 = "data1";

    /** Activity input name - data. */
    public static final String INPUT_DATA_2 = "data2";
    
    /** Activity output name - result. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Activity output. */
    public BlockWriter mOutput;
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA_1),
            new TupleListActivityInput(INPUT_DATA_2)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        TupleListIterator data1 = (TupleListIterator)iterationData[0];
        TupleListIterator data2 = (TupleListIterator)iterationData[1];
        TupleMetadata metadata = 
            (TupleMetadata)data1.getMetadataWrapper().getMetadata();
        TupleMetadata exceptMetadata = 
            (TupleMetadata)data2.getMetadataWrapper().getMetadata();
        if (!metadata.equals(exceptMetadata))
        {
            throw new ActivityUserException(new MismatchedMetadataException());
        }
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(data1.getMetadataWrapper());
        int[] columns = new int[metadata.getColumnCount()];
        for (int i=0; i<columns.length; i++)
        {
            columns[i] = i;
        }
        Tuple tupleData = (Tuple)data1.nextValue();
        Tuple tupleExcept = (Tuple)data2.nextValue();
        while (tupleData != null)
        {
            // if there are no more tuples from the difference set
            // write the rest of the blocks directly to output
            if (tupleExcept == null)
            {
                writeBlock(tupleData);
                tupleData = (Tuple)data1.nextValue();
            }
            else 
            {
                ComparableTuple comp1 = new ComparableTuple(tupleData, columns);
                ComparableTuple comp2 = new ComparableTuple(tupleExcept, columns);
                int result = comp1.compareTo(comp2);
                if (result < 0)
                {
                    writeBlock(tupleData);
                    tupleData = (Tuple)data1.nextValue();
                }
                else if (result == 0)
                {
                    // remove from set - no output
                    tupleData = (Tuple)data1.nextValue();
                }
                else // result > 0
                {
                    tupleExcept = (Tuple)data2.nextValue();
                }
            }
        }
        // iterate to the end of the tuple list
        while (tupleExcept != null)
        {
            tupleExcept = (Tuple)data2.nextValue();
        }
        writeBlock(ControlBlock.LIST_END);
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
    
}
