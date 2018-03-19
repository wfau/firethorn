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

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SingleActivityInput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.DataValue;

/**
 * Class used to pass details of activity inputs from the 
 * <code>GenericActivity</code> class to the <code>BaseActivity</code> class.
 * Not all methods of the <code>ActivityInput</code> interface are implemented
 * here as only a few methods of this interface are called by the 
 * <code>BaseActivity</code> class.  The functionality provided by the other
 * methods in the interface is handled within the 
 * <code>SimpleTraversableSingleActivityInput</code> class that is used 
 * internally by <code>GenericActivity</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class GenericActivityActivityInput implements ActivityInput
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** All the inputs with a common input name. */
    protected ArrayList<TraversableSingleActivityInput> mInputs;
    
    /**
     * Constructor.
     * 
     * @param inputName  input name
     * @param inputs     all the single inputs corresponding to the input name
     */
    public GenericActivityActivityInput(
        String inputName,
        ArrayList<TraversableSingleActivityInput> inputs)
    {
        mInputs = inputs;
    }

    /**
     * {@inheritDoc}
     */
    public void add(DataValue value)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void add(int index, DataValue value)
        throws ArrayIndexOutOfBoundsException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void connect(SingleActivityOutput output)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void connect(int index, SingleActivityOutput output)
        throws ArrayIndexOutOfBoundsException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfInputs()
    {
        return mInputs.size();
    }

    /**
     * {@inheritDoc}
     */
    public SingleActivityInput[] getSingleActivityInputs()
    {
        return mInputs.toArray(new SingleActivityInput[]{});
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
    public void setNumberOfInputs(int count)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ActivityIOIllegalStateException
    {
        for( TraversableSingleActivityInput input : mInputs)
        {
            input.validateState();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        int i=0;
        for (TraversableSingleActivityInput input : mInputs)
        {
            buffer.append("Occurrence with index "+ (i++) + ":\n");
            buffer.append(input);
        }
        
        return buffer.toString();
    }
}
