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
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Activity generating random tuples with a given schema.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>metadata</code>. Type: {@link java.lang.String}. String encoded tuple
 * metadata. Expected format:
 * <code>column_name:table_name:type:is_nullable,id:clients:4:true,...</code>.
 * Columns are comma separated. Metadata elements for a column are colon
 * separated. The <code>type</code> is an integer matching definitions in
 * <code>TupleTypes</code> and <code>is_nullable</code> is a boolean (expected
 * to be <code>true</code> or <code>false</code>).</li>
 * <li>
 * <code>count</code>. Type: {@link java.lang.Integer}. The number of random
 * tuples to be generated.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link uk.org.ogsadai.tuple.Tuple}
 * with the first item in the list an instance of
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * The inputs are read in the following order: <code>metadata</code>,
 * <code>count</code>.</li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * Generates <code>count</code> number of tuples with a given schema. See
 * {@link uk.org.ogsadai.activity.relational.RandomTableScanTupleGenerator} for
 * value ranges for different column types.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RandomTableScanActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(RandomTableScanActivity.class);
    
    /** Metadata input name. */
    public static final String INPUT_METADATA = "metadata";
    /** Count input name. */
    public static final String INPUT_COUNT = "count";
    /** Data output name. */
    public static final String OUTPUT_TUPLES = "data";
    /** Output block writer. */
    private BlockWriter mTupleOutput;
    
    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedActivityInput(INPUT_METADATA, String.class),
            new TypedActivityInput(INPUT_COUNT, Integer.class)
        };
    }

    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // NOOP
    }

    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        mTupleOutput = getOutput(OUTPUT_TUPLES);
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        String metadata = (String) iterationData[0];
        Integer count = (Integer) iterationData[1];
        
        RandomTableScanTupleGenerator generator = new RandomTableScanTupleGenerator(
            metadata);
        
        try
        {
            generator.generateTuples(mTupleOutput, count);
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
        catch (UnsupportedTupleTypeException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ColumnNotFoundException e)
        {
            throw new ActivityUserException(e);
        }
    }
}
