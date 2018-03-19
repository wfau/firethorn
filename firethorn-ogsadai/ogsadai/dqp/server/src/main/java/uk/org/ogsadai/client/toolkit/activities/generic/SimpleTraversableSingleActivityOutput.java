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


package uk.org.ogsadai.client.toolkit.activities.generic;

import java.util.ArrayList;

import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.activity.SimpleSingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.SingleActivityInput;
import uk.org.ogsadai.client.toolkit.exception.ActivityOutputUnconnectedException;

/**
 * Implementation of <code>TraversableSingleActivityOutput</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleTraversableSingleActivityOutput
    extends SimpleSingleActivityOutput
    implements TraversableSingleActivityOutput
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Activity that contains this output. */
    protected Activity mActivity;

    /** Connected inputs. */
    protected ArrayList<SingleActivityInput> mConnectedInputs;
    
    /** The pipe name. */
    protected String mPipeName;
    
    /** 
     * Should full validation be applied to this output.  If the activity is
     * connected to non-generic activities then full validation should not be
     * used.
     */
    protected boolean mUseFullValidation;

    /**
     * Constructor.  The pipe name will be automatically generated.
     * 
     * @param outputName  output name
     * @param activity    activity that contains this output
     */
    public SimpleTraversableSingleActivityOutput(
        String outputName,
        Activity activity)
    {
        this(outputName, activity, true);
    }
    
    /**
     * Constructor at supports an explicit pipe name.  Using an explicit pipe
     * name is not recommended but can be useful for writing unit tests that use
     * mock objects.
     * 
     * @param outputName  output name
     * @param pipeName    pipe name
     * @param activity    activity that contains this output
     */
    public SimpleTraversableSingleActivityOutput(
        String outputName,
        String pipeName,
        Activity activity)
    {
        this(outputName, pipeName, activity, true);
    }
    
    /**
     * Constructor.  The pipe name will be automatically generated.
     * 
     * @param outputName  output name
     * @param activity    activity that contains this output
     * @param fullValidation 
     *            <tt>true</tt> if full validation is to be performed to detect
     *            if the output is legally connected, <tt>false</tt> for more
     *            limited validation.  Limited validation is required if this
     *            output is connected to a non-generic activity.
     */
    public SimpleTraversableSingleActivityOutput(
        String outputName,
        Activity activity,
        boolean fullValidation)
    {
        super(outputName);
        mActivity = activity;
        mConnectedInputs = new ArrayList<SingleActivityInput>(1);
        mPipeName = super.getPipeName();
        mUseFullValidation = fullValidation;
    }
    
    /**
     * Constructor at supports an explicit pipe name.  Using an explicit pipe
     * name is not recommended but can be useful for writing unit tests that use
     * mock objects.
     * 
     * @param outputName  output name
     * @param pipeName    pipe name
     * @param activity    activity that contains this output
     * @param fullValidation 
     *            <tt>true</tt> if full validation is to be performed to detect
     *            if the output is legally connected, <tt>false</tt> for more
     *            limited validation.  Limited validation is required if this
     *            output is connected to a non-generic activity.
     */
    public SimpleTraversableSingleActivityOutput(
        String outputName,
        String pipeName,
        Activity activity,
        boolean fullValidation)
    {
        this(outputName, activity, fullValidation);
        mPipeName = pipeName;
    }

    /**
     * {@inheritDoc}
     */
    public String getPipeName()
    {
        return mPipeName;
    }
    
    /**
     * {@inheritDoc}
     */
    public SingleActivityInput[] getConnectedInputs()
    {
        return mConnectedInputs.toArray(new SingleActivityInput[]{});
    }

    /**
     * {@inheritDoc}
     */
    public void addConnection(SingleActivityInput input)
    {
        setIsConnected();
        mConnectedInputs.add(input);
    }

    /**
     * {@inheritDoc}
     */
    public void removeConnection(SingleActivityInput input)
    {
        mConnectedInputs.remove(input);
    }

    /**
     * {@inheritDoc}
     */
    public void disconnect()
    {
        for( SingleActivityInput input : mConnectedInputs)
        {
            if (input instanceof TraversableSingleActivityInput)
            {
                TraversableSingleActivityInput traversableInput = 
                    (TraversableSingleActivityInput) input;
                traversableInput.removeConnection(this);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Activity getActivity()
    {
        return mActivity;
    }
    
    /**
     * {@inheritDoc}
     */
    public void validateState() throws ActivityOutputUnconnectedException
    {
        super.validateState();
        
        // Super does not allow us to tell it that a connection has been 
        // broken so we need to check if the output is connected here
        if (mUseFullValidation && mConnectedInputs.size() == 0)
        {
            // Input is unconnected
            throw new ActivityOutputUnconnectedException(super.getOutputName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Output {\n");
        buffer.append("  name = ");
        buffer.append(getOutputName());
        buffer.append("\n  connected = ");
        buffer.append(mConnectedInputs.size() > 0);
        buffer.append("\n  pipe = Pipe[");
        buffer.append(mPipeName);
        buffer.append("]\n");
        buffer.append("}\n");
        
        return buffer.toString();

    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        if (obj instanceof SimpleTraversableSingleActivityOutput)
        {
            SimpleTraversableSingleActivityOutput rhs = 
                (SimpleTraversableSingleActivityOutput) obj;
            
            if (mActivity.equals(rhs.mActivity) &&
                mPipeName.equals(rhs.mPipeName))
            {
                return true;
            }
        }
        
        return false;
    }
}
