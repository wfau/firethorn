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
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.SimpleLiteralInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.SimplePipeInputDescriptor;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.ActivityInputUnspecifiedException;
import uk.org.ogsadai.data.DataValue;

/**
 * Implementation of <code>TraversableSingleActivityInput</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleTraversableSingleActivityInput 
    implements TraversableSingleActivityInput
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";

    /** The input name. */
    protected String mInputName;
    
    /** Activity that contains this input. */
    protected Activity mActivity;
    
    /** Data values associated with this input if it is a literal. */
    protected ArrayList<DataValue> mDataValues;
    
    /** Output to which this input is connected if it is a connection. */
    protected SingleActivityOutput mConnectedOutput;
    
    /** Is the input a literal? */
    protected boolean mIsLiteral;
    
    /**
     * Constructor.
     * 
     * @param inputName  input name
     * @param activity   activity that contains this input
     */
    public SimpleTraversableSingleActivityInput(
        String inputName,
        Activity activity)
    {
        mInputName = inputName;
        mActivity = activity;
        mDataValues = new ArrayList<DataValue>(10);
        mConnectedOutput = null;
        mIsLiteral = true;
    }
    
    /**
     * {@inheritDoc}
     */
    public void clearDataValues()
    {
        mDataValues.clear();
        switchToLiteral();
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
    public SingleActivityOutput getConnectedOutput()
    {
        return mConnectedOutput;
    }

    /**
     * {@inheritDoc}
     */
    public DataValue[] getDataValues()
    {
        return mDataValues.toArray(new DataValue[]{});
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLiteral()
    {
        return mIsLiteral;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataValues(DataValue[] values)
    {
        switchToLiteral();
        
        mDataValues.clear();
        
        for( DataValue value : values)
        {
            mDataValues.add(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void add(DataValue value)
    {
        switchToLiteral();
        mDataValues.add(value);
    }

    /**
     * {@inheritDoc}
     */
    public void connect(SingleActivityOutput output)
    {
        switchToConnection();
        disconnect();
        mConnectedOutput = output;

        // Tell the output about the connection
        if (mConnectedOutput instanceof TraversableSingleActivityOutput)
        {
            TraversableSingleActivityOutput traversableOutput = 
                (TraversableSingleActivityOutput) output;
            traversableOutput.addConnection(this);
        }
        else
        {
            output.setIsConnected();
        }
    }

    /**
     * {@inheritDoc}
     */
    public ActivityInputDescriptor getInputDescriptor()
    {
        if (mIsLiteral)
        {
            SimpleLiteralInputDescriptor literal = 
                new SimpleLiteralInputDescriptorWithEquals(mInputName);
            for( DataValue dataValue : mDataValues)
            {
                literal.add(dataValue);
            }
            return literal;
        }
        else
        {
            SimplePipeInputDescriptor pipeInput = 
                new SimplePipeInputDescriptorWithEquals(
                    mInputName,
                    mConnectedOutput.getPipeName());
            return pipeInput;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOperational()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ActivityIOIllegalStateException
    {
        if (mIsLiteral)
        {
            if (mDataValues.size() == 0)
            {
                throw new ActivityInputUnspecifiedException(mInputName);
            }
        }
        else
        {
            if (mConnectedOutput == null)
            {
                throw new ActivityInputUnspecifiedException(mInputName);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getInputName()
    {
        return mInputName;
    }
    
    /**
     * {@inheritDoc}
     */
    public void disconnect()
    {
        if (mConnectedOutput != null && 
            mConnectedOutput instanceof TraversableSingleActivityOutput)
        {
            TraversableSingleActivityOutput traversableOutput = 
                (TraversableSingleActivityOutput) mConnectedOutput;
            traversableOutput.removeConnection(this);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeConnection(SingleActivityOutput output)
    {
        if (mConnectedOutput == output)
        {
            mConnectedOutput = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Input {\n");
        buffer.append("  name = ");
        buffer.append(mInputName);
        
        if (!mIsLiteral)
        {
            String pipeName = "UNCONNECTED";
            if (mConnectedOutput != null)
            {
                pipeName = mConnectedOutput.getPipeName();
            }
            buffer.append("\n  pipe = ");
            buffer.append(pipeName);
            buffer.append("\n");
        }
        else 
        {
            buffer.append("\n  literals:\n");
            for( DataValue dataValue : mDataValues)
            {
                buffer.append("    ");
                buffer.append(dataValue);
                buffer.append("\n");
            }
        }
        buffer.append("}\n");
        
        return buffer.toString();
    }

    /**
     * Switches the input to a literal and disconnects from any connected
     * outputs.
     */
    protected void switchToLiteral()
    {
        mIsLiteral = true;
        if (mConnectedOutput != null)
        {
            disconnect();
        }
    }

    /**
     * Switches the input to a connection and clears any data values.
     */
    protected void switchToConnection()
    {
        mIsLiteral = false;
        mDataValues.clear();
    }
   
}
