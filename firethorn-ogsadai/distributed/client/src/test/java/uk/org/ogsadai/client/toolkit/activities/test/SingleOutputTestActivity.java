// Copyright (c) The University of Edinburgh, 2002-2009.
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

package uk.org.ogsadai.client.toolkit.activities.test;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;

/**
 * Simple dummy client toolkit activity proxy with single output.
 * Useful for having an activity to connect to other activity proxy
 * inputs.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SingleOutputTestActivity extends BaseActivity implements Activity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2002-2009.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.SingleOutputTestActivity");
    
    /** Data input name. */
    public static final String DATA_INPUT = "data";
    
    /** Result output name. */
    public static final String RESULT_OUTPUT = "result";

    /** Data input. */
    private ActivityInput mDataInput;
    
    /** Result output. */
    private ActivityOutput mResultOutput;
    
    /**
     * Constructor.
     */
    public SingleOutputTestActivity()
    {
        super(DEFAULT_ACTIVITY_NAME);
        mDataInput                 = new SimpleActivityInput(DATA_INPUT);
        mResultOutput              = new SimpleActivityOutput(RESULT_OUTPUT);
    }

    /**
     * Connects the data input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectDataInput(final SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
        
    /**
     * Gets the result output.
     * @return data output.
     */
    public SingleActivityOutput getResultOutput()
    {
        return mResultOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Gets whether or not there is another result to be retrieved.
     * @return <code>true</code> if there is another result, <code>false</code>
     *         otherwise.
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
    public boolean hasNextResult() 
        throws DataStreamErrorException, 
               UnexpectedDataValueException, 
               DataSourceUsageException
    {
        return getResultOutputIterator().hasNext();
    }
     */
    
    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        mDataInput.validateState();
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[]{ mDataInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[]{ mResultOutput };
    }
}
