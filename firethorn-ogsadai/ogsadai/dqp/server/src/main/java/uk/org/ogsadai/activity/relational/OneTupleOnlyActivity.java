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

import java.util.Collections;
import java.util.List;

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
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * An activity that completes successfully only if the input data list contains 
 * at most one tuple. 
 * If the input is an empty tuple list then a null tuple is returned. 
 * If the input list has more than one entry then an exception is raised.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This list
 * contains exactly one tuple which is either the input tuple or, if the input 
 * list was empty, a null tuple. 
 * </li>
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
 * <li>Depending on the input data, this activity either writes exactly one 
 * tuple to the output or raises an exception.</li>
 * <li>If the input tuple list is empty a NULL tuple is written to the output.</li>
 * <li>If the input tuple list has one element, this element is written to the
 * output.</li>
 * <li>If the input tuple list contains more than one element, a 
 * {@link TooManyTuplesException} is raised.
 * </li>
 * <li> For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 * data: { metadata  (a, b, c) }
 * result: { metadata  (a, b, c) }
 * </pre>
 * <pre>
 * data: { metadata }
 * result: { metadata  (null, null, null) }
 * </pre>
 * <pre>
 * data: { metadata (a, b, c) (d, e, f) }
 * result: -
 * exception: TooManyTuplesException
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OneTupleOnlyActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger for this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(OneTupleOnlyActivity.class);

    /** Data input name. */
    public static final String INPUT_DATA = "data";
    /** Data output name. */
    public static final String OUTPUT_RESULT = "result";

    private BlockWriter mOutput;

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA)
        };
    }

    @Override
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        TupleListIterator tuples = (TupleListIterator)iterationData[0];
        MetadataWrapper wrapper = tuples.getMetadataWrapper();
        Object block = tuples.nextValue();
        if (block == null)
        {
            block = getNullTuple((TupleMetadata)wrapper.getMetadata());
        }
        else if (tuples.nextValue() != null)
        {
            // check that the tuple list contains no more data
            // if it does then raise an exception
            LOG.debug("Too many tuples : only one row is expected.");
            throw new ActivityUserException(new TooManyTuplesException());
        }
        try
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            mOutput.write(wrapper);
            mOutput.write(block);
            mOutput.write(ControlBlock.LIST_END);
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

    private Object getNullTuple(TupleMetadata metadata)
    {
        int columnCount = metadata.getColumnCount();
        List<Null> columns = Collections.nCopies(columnCount, Null.getValue());
        return new SimpleTuple(columns);
    }

    @Override
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }


    
}
