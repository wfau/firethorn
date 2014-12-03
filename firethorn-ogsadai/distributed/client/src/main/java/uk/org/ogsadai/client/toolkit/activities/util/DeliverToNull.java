// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.client.toolkit.activities.util;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;

/**
 * This activity throws away it's inputs.
 *
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>input</code> - Type: {@link java.lang.Object}. This is a
 * mandatory input. </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.DeliverToNull</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>None.</li>
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
 * <li>Throws away all its inputs.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DeliverToNull extends BaseActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    public static final ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.DeliverToNull");
    
    /** Name of the activity input. */
    public static final String INPUT = "input";
    /** The activity data input. */
    private ActivityInput mInput;

    /**
     * Constructs a new activity with the given name.
     * 
     * @param activityName
     *            activity name
     */
    public DeliverToNull(ActivityName activityName)
    {
        super(activityName);
        mInput = new SimpleActivityInput(INPUT);
    }
    
    /**
     * Constructs a new activity with the default activity name.
     */
    public DeliverToNull()
    {
        this(DEFAULT_ACTIVITY_NAME);
    }
    
    /**
     * Connects the input to the given output.
     * 
     * @param output
     *            output to connect to the activity's input
     */
    public void connectInput(SingleActivityOutput output)
    {
        mInput.connect(output);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mInput };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[] {};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // no validation
    }

}
