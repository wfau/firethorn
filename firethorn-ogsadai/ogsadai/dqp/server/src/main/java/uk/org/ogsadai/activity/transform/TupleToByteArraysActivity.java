// Copyright (c) The University of Edinburgh,  2009.
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

package uk.org.ogsadai.activity.transform;

import java.io.IOException;

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
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.transform.BytesOutputStream.IOWrapperException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.serialise.TupleOutputStream;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * An activity that converts lists of tuples to an OGSA-DAI binary tuple stream.
 * The result is output as an OGSA-DAI list of <code>byte[]</code> of fixed
 * size.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code> Type: OGSA-DAI list of {@link uk.org.ogsadai.tuple.Tuple}
 * with the first item in the list an instance of
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. Tuples to be rendered in
 * binary format.</li>
 * <li>
 * <code>size</code> Type: {@link Integer}. Size of output byte arrays.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>. Type: OGSA-DAI list of <code>byte[]</code>. The tuples
 * rendered in an OGSA-DAI internal binary format.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
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
 * The activity receives as input a list of tuples and produces a binary format
 * output in the form of an OGSA-DAI list of <code>byte[]</code>. The tuple
 * metadata is written first followed by the binary rendering of the tuples.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleToByteArraysActivity extends MatchedIterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /** Activity input name of data input. */
    private static final String INPUT_DATA = "data";
    /** Activity input name of size input. */
    private static final String INPUT_SIZE = "size";
    /** Activity output name of result output. */
    private static final String OUTPUT = "result";
    /** The output block writer. */
    private BlockWriter mOutput;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA),
            new TypedActivityInput(INPUT_SIZE, Number.class)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
    {
        validateOutput(OUTPUT);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException,
               ActivityTerminatedException,
               ActivityUserException
    {
        int arraySize = ((Number)iterationData[1]).intValue();
        BytesOutputStream bytes = 
            new BytesOutputStream(mOutput, arraySize);
        TupleOutputStream output = new TupleOutputStream(bytes);
        TupleListIterator tuples = (TupleListIterator)iterationData[0];
        try
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            output.writeMetadata((TupleMetadata)tuples.getMetadataWrapper().getMetadata());
            Tuple tuple;
            while ((tuple = (Tuple)tuples.nextValue()) != null)
            {
                output.writeTuple(tuple);
            }
            output.close();
            mOutput.write(ControlBlock.LIST_END);
        }
        catch (IOWrapperException e)
        {
            if (e.getCause() instanceof PipeClosedException)
            {
                iterativeStageComplete();
                return;
            }
            if (e.getCause() instanceof PipeIOException)
            {
                throw new ActivityProcessingException(e.getCause());
            }
            if (e.getCause() instanceof PipeTerminatedException)
            {
                throw new ActivityTerminatedException();
            }
        }
        catch (IOException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (UnsupportedTupleTypeException e)
        {
            throw new ActivityUserException(e);
        }
        catch (PipeClosedException e)
        {
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
     * {@inheritDoc}
     */
    @Override
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }

}
