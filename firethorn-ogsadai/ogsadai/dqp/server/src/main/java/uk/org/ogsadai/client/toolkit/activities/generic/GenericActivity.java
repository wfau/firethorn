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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.activity.ActivityStatus;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.DataValueIterator;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SimpleDataValueIterator;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.StreamErrorDataValueIterator;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.RequestAndStatusHandler;
import uk.org.ogsadai.client.toolkit.activity.RequestBuilder;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.ActivityIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.RequestStatusUnknownException;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * Client toolkit activity than can be used to to represent any activity.  This
 * is useful if there is not a client toolkit class for any activity.  
 * Additionally this class provides better support for editing pipelines of
 * activities.  All all the activities in the pipeline are instances of this
 * class a range of workflow editing functionality is available.
 *
 * @author The OGSA-DAI Project Team
 */
public class GenericActivity extends BaseActivity 
    implements ResourceActivity, RequestAndStatusHandler
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Resource ID, or null if there is no resource. */
    protected ResourceID mResourceID;

    /** Inputs. */
    protected Map<String, ArrayList<TraversableSingleActivityInput>> mInputs;
    
    /** Outputs. */
    protected Map<String, ArrayList<TraversableSingleActivityOutput>> mOutputs;
    
    /** Request status as obtained from the server. */
    private RequestStatus mRequestStatus;
    
    /** Specifies full validation of an activity output. */
    public static final boolean FULL_VALIDATION = true;

    /** Specifies limited validation of an activity output. */
    public static final boolean LIMITED_VALIDATION = false;
    

    /**
     * Constructor.
     * 
     * @param activityName activity name.
     */
    public GenericActivity(String activityName)
    {
        this(new ActivityName(activityName));
    }

    /**
     * Constructor.
     * 
     * @param activityName activity name.
     */
    public GenericActivity(ActivityName activityName)
    {
        super(activityName);
        
        mInputs = 
            new HashMap<String, ArrayList<TraversableSingleActivityInput>>();
        mOutputs = 
            new HashMap<String, ArrayList<TraversableSingleActivityOutput>>();
    }
    
    /**
     * Sets the activity name.  This is the name of the activity as exposed by
     * the OGSA-DAI server.
     * 
     * @param activityName activity name.
     */
    public void setActivityName(String activityName)
    {
        setActivityName(new ActivityName(activityName));
    }


    /**
     * Sets the ID of the resource the activity is targeted at.  By default 
     * the activity will not be targeted at a resource.
     * 
     * @param resourceID resource ID, or <tt>null</tt> to target the activity
     *                   at no resource.
     */
    public void setResourceID(final String resourceID)
    {
        mResourceID = new ResourceID(resourceID);
    }
    
    /**
     * Sets the ID of the resource the activity is targeted at.  By default 
     * the activity will not be targeted at a resource.
     * 
     * @param resourceID resource ID, or <tt>null</tt> to target the activity
     *                   at no resource.
     */
    public void setResourceID(final ResourceID resourceID)
    {
        mResourceID = resourceID;
    }

    /**
     * Gets the ID of the resource the activity is targeted at.
     * 
     * @return resource ID, or <tt>null</tt> if the activity is not targeted
     *         at a resource.
     */
    public ResourceID getResourceID()
    {
        return mResourceID;
    }

    /**
     * Creates an input with the given input name.  The activity will have a
     * single occurrence of this input.
     * 
     * @param inputName name of the input.
     */
    public void createInput(String inputName)
    {
        createInput(inputName, 1);
    }
    
    /**
     * Creates an input with the given input name.  The activity will have the
     * specified number of occurrences of this input.
     * 
     * @param inputName name of the input
     * @param numInputs number of occurrences of the input
     */
    public void createInput(String inputName, int numInputs)
    {
        if (numInputs < 1 )
        {
            throw new InvalidParameterException(
                "numInputs must be greater than 0");
        }
        
        ArrayList<TraversableSingleActivityInput> inputs = 
            new ArrayList<TraversableSingleActivityInput>(numInputs);
        for (int i=0; i<numInputs; ++i)
        {
            inputs.add(
                new SimpleTraversableSingleActivityInput(
                    inputName, this));
        }
        
        mInputs.put(inputName, inputs);
    }

    /**
     * Inserts a new input at the specified position.
     * 
     * @param inputName input name.
     * @param index     index (zero based).  The new input will be placed at
     *                  this position in the list of inputs with this name.
     *                  
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void insertInput(String inputName, int index)
        throws IndexOutOfBoundsException, IllegalArgumentException
    {
        if (index < 0 )
        {
            throw new IndexOutOfBoundsException(
                "index must be 0 or greater");
        }
        
        if (mInputs.containsKey(inputName))
        {
            ArrayList<TraversableSingleActivityInput> inputs =
                mInputs.get(inputName);
            
            inputs.add(
                index, 
                new SimpleTraversableSingleActivityInput(inputName, this));
        }
        else
        {
            throwUnknownInputException(inputName);
        }
    }

    /**
     * Removes all occurrences of the specified input.
     * 
     * @param inputName input name.
     * 
     * @return <tt>true</tt> if the activity contained the specified input,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeInput(String inputName)
    {
        boolean result = false;
        
        if (mInputs.containsKey(inputName))
        {
            // Disconnect any connections
            ArrayList<TraversableSingleActivityInput> inputs = 
                mInputs.get(inputName);
            
            for( TraversableSingleActivityInput input : inputs)
            {
                input.disconnect();
            }
 
            // Remove the input altogether
            mInputs.remove(inputName);
            result = true;
        }
        
        return result;
    }
    
    /**
     * Removes a specified occurrence of the specified input.
     * 
     * @param inputName input name.
     * @param index     index (zero based) specifying the occurrence of the 
     *                  input to remove. 
     * 
     * @return <tt>true</tt> if the activity contained the specified input,
     *         <tt>false</tt> otherwise.
     *         
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public boolean removeInput(String inputName, int index) 
        throws IndexOutOfBoundsException
    {
        if (index < 0 )
        {
            throw new IndexOutOfBoundsException(
                "index must be 0 or greater");
        }

        boolean result = false;
        
        if (mInputs.containsKey(inputName))
        {
            ArrayList<TraversableSingleActivityInput> inputs = 
                mInputs.get(inputName);
            
            if (index < inputs.size())
            {
                TraversableSingleActivityInput input = inputs.get(index);
                input.disconnect();
                inputs.remove(index);
                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * Creates an output with the given output name.  The activity will have a
     * single occurrence of this output.
     * 
     * @param outputName name of the output.
     */
    public void createOutput(String outputName)
    {
        createOutput(outputName, 1, true);
    }

    /**
     * Creates an output with the given output name.  The activity will have a
     * single occurrence of this output.
     * 
     * @param outputName 
     *            name of the output.
     * @param fullValidation 
     *            <tt>true</tt> if full validation is to be performed to detect
     *            if the output is legally connected, <tt>false</tt> for more
     *            limited validation.  Limited validation is required if this
     *            output is connected to a non-generic activity.
     */
    public void createOutput(String outputName, boolean fullValidation)
    {
        createOutput(outputName, 1, fullValidation);
    }

    /**
     * Creates an output with the given output name.  The activity will have the
     * specified number of occurrences of this output.
     * 
     * @param outputName name of the output
     * @param numOutputs  number of occurrences of the output
     */
    public void createOutput(String outputName, int numOutputs)
    {
        createOutput(outputName, numOutputs, true);
    }
    
    /**
     * Creates an output with the given output name.  The activity will have the
     * specified number of occurrences of this output.
     * 
     * @param outputName name of the output
     * @param numOutputs  number of occurrences of the output
     * @param fullValidation 
     *            <tt>true</tt> if full validation is to be performed to detect
     *            if the output is legally connected, <tt>false</tt> for more
     *            limited validation.  Limited validation is required if this
     *            output is connected to a non-generic activity.
     */
    public void createOutput(
        String outputName, int numOutputs, boolean fullValidation)
    {
        if (numOutputs < 1 )
        {
            throw new InvalidParameterException(
                "numOutputs must be greater than 0");
        }
        
        ArrayList<TraversableSingleActivityOutput> outputs = 
            new ArrayList<TraversableSingleActivityOutput>(numOutputs);
        for (int i=0; i<numOutputs; ++i)
        {
            outputs.add(
                new SimpleTraversableSingleActivityOutput(
                    outputName, this, fullValidation));
        }
        
        mOutputs.put(outputName, outputs);
    }
    
    /**
     * Inserts a new output occurrence at the specified position.
     * 
     * @param outputName output name.
     * @param index      index (zero based).  The new output will be placed at
     *                   this position in the list of outputs with this name.
     *                  
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalArgumentException if the output is unknown.
     */
    public void insertOutput(String outputName, int index)
        throws IndexOutOfBoundsException, IllegalArgumentException
    {
        insertOutput(outputName, index, true);
    }

    /**
     * Inserts a new output occurrence at the specified position.
     * 
     * @param outputName output name.
     * @param index      index (zero based).  The new output will be placed at
     *                   this position in the list of outputs with this name.
     * @param fullValidation 
     *            <tt>true</tt> if full validation is to be performed to detect
     *            if the output is legally connected, <tt>false</tt> for more
     *            limited validation.  Limited validation is required if this
     *            output is connected to a non-generic activity.
     *                  
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalArgumentException if the output is unknown.
     */
    public void insertOutput(
        String outputName, int index, boolean fullValidation)
        throws IndexOutOfBoundsException, IllegalArgumentException
    {
        if (index < 0 )
        {
            throw new IndexOutOfBoundsException(
                "index must be 0 or greater");
        }
        
        if (mOutputs.containsKey(outputName))
        {
            ArrayList<TraversableSingleActivityOutput> outputs =
                mOutputs.get(outputName);
            
            outputs.add(
                index, 
                new SimpleTraversableSingleActivityOutput(
                    outputName, this, fullValidation));
        }
        else
        {
            throwUnknownOutputException(outputName);
        }
    }
    
    /**
     * Removes all occurrences of the specified output.
     * 
     * @param outputName output name.
     * 
     * @return <tt>true</tt> if the activity contained the specified output,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeOutput(String outputName)
    {
        boolean result = false;
        
        if (mOutputs.containsKey(outputName))
        {
            // Disconnect any connections
            ArrayList<TraversableSingleActivityOutput> outputs = 
                mOutputs.get(outputName);
            
            for( TraversableSingleActivityOutput output : outputs)
            {
                output.disconnect();
            }

            mOutputs.remove(outputName);
            result = true;
        }
        
        return result;
    }
    
    /**
     * Removes a specified occurrence of the specified output.
     * 
     * @param outputName output name.
     * @param index      index (zero based) specifying the occurrence of the 
     *                   output to remove. 
     * 
     * @return <tt>true</tt> if the activity contained the specified output,
     *         <tt>false</tt> otherwise.
     *         
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public boolean removeOutput(String outputName, int index)
        throws IndexOutOfBoundsException
    {
        if (index < 0 )
        {
            throw new IndexOutOfBoundsException(
                "index must be 0 or greater");
        }

        boolean result = false;
        
        if (mOutputs.containsKey(outputName))
        {
            ArrayList<TraversableSingleActivityOutput> outputs = 
                mOutputs.get(outputName);
            
            if (index < outputs.size())
            {
                TraversableSingleActivityOutput output = outputs.get(index);
                output.disconnect();

                outputs.remove(index);
                result = true;
            }
        }
        
        return result;
    }
    

    /**
     * Adds a literal data value to the end of the list of data values for
     * the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param dataValue data value
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInput(String inputName, DataValue dataValue)
    {
        addInput(inputName, 0, dataValue);
    }
    
    /**
     * Adds a literal data value to the end of the list of data values for
     * the specified input occurrence.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param index     index (zero based) specifying which input occurrence
     *                  to add the data value to
     * @param dataValue data value
     * 
     * @throws IllegalArgumentException if the input is unknown.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */    
    public void addInput(String inputName, int index, DataValue dataValue)
    {
        if (!mInputs.containsKey(inputName))
        {
            throwUnknownInputException(inputName);
        }
        
        ArrayList<TraversableSingleActivityInput> inputs = 
            mInputs.get(inputName);
        
        inputs.get(index).add(dataValue);
    }

    /**
     * Adds multiple literal data values to the end of the list of data values 
     * for the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName  input name
     * @param dataValues data values
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInput(String inputName, DataValue[] dataValues)
    {
        addInput(inputName, 0, dataValues);
    }

    /**
     * Adds multiple literal data values to the end of the list of data values 
     * for the specified input occurrence.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName  input name
     * @param index      index (zero based) specifying which input occurrence
     *                   to add the data values to
     * @param dataValues data values
     * 
     * @throws IllegalArgumentException if the input is unknown.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */    
    public void addInput(String inputName, int index, DataValue[] dataValues)
    {
        if (!mInputs.containsKey(inputName))
        {
            throwUnknownInputException(inputName);
        }
        
        ArrayList<TraversableSingleActivityInput> inputs = 
            mInputs.get(inputName);
        
        for (int i=0; i<dataValues.length; ++i)
        {
            inputs.get(index).add(dataValues[i]);
        }
    }

    /**
     * Adds a literal data value to the end of the list of data values for
     * the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param stringDataValue string data value
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInput(String inputName, String stringDataValue)
    {
        addInput(inputName, new StringData(stringDataValue));
    }
    
    /**
     * Adds a literal data value to the end of the list of data values for
     * the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param integerDataValue integer data value
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInput(String inputName, int integerDataValue)
    {
        addInput(inputName, new IntegerData(integerDataValue));
    }
    
    /**
     * Adds an OGSA-DAI list of String data values to the end of the list of 
     * data values for the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param stringDataValues string data values to add
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInputList(String inputName, String[] stringDataValues)
    {
        addInput(inputName, ListBegin.VALUE);
        for( String value : stringDataValues)
        {
            addInput(inputName, new StringData(value));
        }
        addInput(inputName, ListEnd.VALUE);
    }
    
    /**
     * Adds an OGSA-DAI list of int data values to the end of the list of 
     * data values for the specified input.
     * <p>
     * Switches this input to be a literal input if it was previously a 
     * connected input.
     * 
     * @param inputName input name
     * @param intDataValues int data values to add
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void addInputList(String inputName, int[] intDataValues)
    {
        addInput(inputName, ListBegin.VALUE);
        for( int value : intDataValues)
        {
            addInput(inputName, new IntegerData(value));
        }
        addInput(inputName, ListEnd.VALUE);
    }

    /**
     * Connects the specified input to the specified output.
     * <p>
     * Switches the input to be a connected input if it was previously a
     * literal input.
     * 
     * @param inputName input name
     * @param output    output to connect to
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public void connectInput(String inputName, SingleActivityOutput output)
    {
        connectInput(inputName, 0, output);
    }

    
    /**
     * Connects the specified input occurrence to the specified output.
     * <p>
     * Switches the input to be a connected input if it was previously a
     * literal input.
     * 
     * @param inputName input name
     * @param index     index (zero based) specifying which input occurrence
     *                  to add the data values to
     * @param output    output to connect to
     * 
     * @throws IllegalArgumentException if the input is unknown.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */    
    public void connectInput(
        String inputName, int index, SingleActivityOutput output)
    {
        if (!mInputs.containsKey(inputName))
        {
            throwUnknownInputException(inputName);
        }
        
        ArrayList<TraversableSingleActivityInput> inputs = 
            mInputs.get(inputName);
        
        inputs.get(index).connect(output);
    }

    /**
     * Connects the specified input to the specified output.
     * <p>
     * Switches the input to be a connected input if it was previously a
     * literal input.
     * 
     * @param input  input 
     * @param output output to connect to
     */  
    public void connectInput(
        TraversableSingleActivityInput input, SingleActivityOutput output)
    {
        input.connect(output);
    }
    
    /**
     * Gets the specified output.
     * 
     * @param outputName output name
     * 
     * @return the traversable single activity output object.
     * 
     * @throws IllegalArgumentException if the output is unknown.
     */
    public TraversableSingleActivityOutput getOutput(String outputName)
    {
        return getOutput(outputName,0);
    }
    
    /**
     * Gets the specified output occurrence.
     * 
     * @param outputName output name
     * @param index      index (zero based) specifying the occurrence of the 
     *                   output to return. 
     * 
     * @return the traversable single activity output object.
     * 
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalArgumentException if the output is unknown.
     */
    public TraversableSingleActivityOutput getOutput(
        String outputName, int index)
    {
        if (!mOutputs.containsKey(outputName))
        {
            throwUnknownOutputException(outputName);
        }
        
        ArrayList<TraversableSingleActivityOutput> outputs = 
            mOutputs.get(outputName);
        
        return outputs.get(index);
    }

    
    /**
     * Gets all the output occurrences for the named output.
     * 
     * @param outputName output name
     * 
     * @return all the traversable single activity output objects for this
     *         output.
     * 
     * @throws IllegalArgumentException if the output is unknown.
     */
    public ArrayList<TraversableSingleActivityOutput> getOutputs(
        String outputName)
    {
        if (!mOutputs.containsKey(outputName))
        {
            throwUnknownOutputException(outputName);
        }
        
        return new ArrayList<TraversableSingleActivityOutput>( 
            mOutputs.get(outputName));
    }

    /**
     * Gets all the outputs for the activity.
     * 
     * @return a mapping from output name to lists of traversable single
     *         activity outputs.
     */
    public Map<String, ArrayList<TraversableSingleActivityOutput>> getAllOutputs()
    {
        return new HashMap<String, ArrayList<TraversableSingleActivityOutput>>( 
            mOutputs);
    }


    /**
     * Gets whether or not the specified input exists.
     * 
     * @param inputName input name
     * 
     * @return <tt>true</tt> if the input exists, <tt>false</tt> otherwise.
     */
    public boolean hasInput(String inputName)
    {
        return mInputs.containsKey(inputName);
    }
    
    /**
     * Gets whether or not the specified output exists.
     * 
     * @param outputName output name
     * 
     * @return <tt>true</tt> if the output exists, <tt>false</tt> otherwise.
     */
    public boolean hasOutput(String outputName)
    {
        return mOutputs.containsKey(outputName);
    }
    
    /**
     * Gets the specified input.
     * 
     * @param inputName input name
     * 
     * @return the traversable single activity input object.
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public TraversableSingleActivityInput getInput(String inputName)
    {
        return getInput(inputName, 0);
    }
    
    /**
     * Gets the specified input occurrence.
     * 
     * @param inputName  input name
     * @param index      index (zero based) specifying the occurrence of the 
     *                   input to return. 
     * 
     * @return the traversable single activity input object.
     * 
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalArgumentException if the input is unknown.
     */
    public TraversableSingleActivityInput getInput(String inputName, int index)
    {
        if (!mInputs.containsKey(inputName))
        {
            throwUnknownInputException(inputName);
        }
        
        ArrayList<TraversableSingleActivityInput> inputs = 
            mInputs.get(inputName);
        
        return inputs.get(index);
    }

    /**
     * Gets all the input occurrences for the named input.
     * 
     * @param inputName input name
     * 
     * @return all the traversable single activity input objects for this
     *         input.
     * 
     * @throws IllegalArgumentException if the input is unknown.
     */
    public ArrayList<TraversableSingleActivityInput> getInputs(
        String inputName)
    {
        if (!mInputs.containsKey(inputName))
        {
            throwUnknownInputException(inputName);
        }
        
        return new ArrayList<TraversableSingleActivityInput>( 
            mInputs.get(inputName));
    }
    
    /**
     * Gets all the inputs for the activity.
     * 
     * @return a mapping from input name to lists of traversable single
     *         activity inputs.
     */
    public Map<String, ArrayList<TraversableSingleActivityInput>> getAllInputs()
    {
        return new HashMap<String, ArrayList<TraversableSingleActivityInput>>( 
            mInputs);
    }
    

    /**
     * Removes the specified input occurrence.
     * 
     * @param input  the input to be removed.
     * 
     * @return <tt>true</tt> if the activity contained the specified output,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeInput(TraversableSingleActivityInput input)
    {
        boolean result = false;
        
        String inputName = input.getInputName();
        input.disconnect();
        
        if (mInputs.containsKey(inputName))
        {
            ArrayList<TraversableSingleActivityInput> inputs = 
                mInputs.get(inputName);
            inputs.remove(input);
            result = true;
            
            if (inputs.size() == 0)
            {
                mInputs.remove(inputName);
            }
        }
        
        return result;
    }

    /**
     * Removes the specified output occurrence.
     * 
     * @param output  the output to be removed.
     * 
     * @return <tt>true</tt> if the activity contained the specified output,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeOutput(TraversableSingleActivityOutput output)
    {
        boolean result = false;
        
        String outputName = output.getOutputName();
        output.disconnect();
        
        if (mOutputs.containsKey(outputName))
        {
            ArrayList<TraversableSingleActivityOutput> outputs = 
                mOutputs.get(outputName);
            outputs.remove(output);
            result = true;
            
            if (outputs.size() == 0)
            {
                mOutputs.remove(outputName);
            }
        }
        
        return result;
    }
    
    /**
     * Gets an iterator to the data values that are the output of the activity.
     * 
     * @param resultName name of the result to get.
     * 
     * @return iterator that gives access to the data. The iterator will output
     *         objects of type <code>DataValue</code>.
     *         
     * @throws RequestStatusUnknownException
     *           if the final request status has not yet been received from the 
     *           server.
     */
    public DataValueIterator getDataValueIterator(String resultName)
    {
        // If we don't have the final request status then throw an error
        if (mRequestStatus == null || 
            (!mRequestStatus.getExecutionStatus().hasFinished()))
        {
            throw new RequestStatusUnknownException();
        }
        
        DataValueIterator dataValueIterator = new SimpleDataValueIterator( 
            mRequestStatus.getDataValueIterator(
                getInstanceName(), resultName));
            
        // If the activity is in error then put this data value iterator
        // in a wrapper than ends with an error
        ActivityStatus activityStatus = 
            mRequestStatus.getActivityProcessingStatus(
                getInstanceName()).getStatus();
        if (activityStatus == ActivityStatus.ERROR || 
            activityStatus == ActivityStatus.TERMINATED )
        {
            dataValueIterator = 
                new StreamErrorDataValueIterator(dataValueIterator);
        }
        return dataValueIterator;
    }
    
    /**
     * {@inheritDoc}
     */
    public void buildRequest(RequestBuilder requestBuilder)
        throws ActivityIllegalStateException
    {
        if (requestBuilder.mustValidateState())
        {
            validateState();
        }
        
        ActivityInputDescriptor[] inputs = getInputsForRequestBuilder();
        SingleActivityOutput[] outputs = getOutputsForRequestBuilder();

        if (mResourceID == null)
        {
            requestBuilder.addActivity(
                getActivityName(),
                getInstanceName(),
                inputs,
                outputs);
        }
        else
        {
            requestBuilder.addActivity(
                getActivityName(),
                getInstanceName(),
                mResourceID,
                inputs,
                outputs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        boolean includeResourceID = (mResourceID != null);
        return writeToString(includeResourceID,mResourceID);
    }
    
    /**
     * Gets the array of activity input descriptors required by the request
     * builder.
     *  
     * @return array of input descriptor, one of each input.
     */
    protected ActivityInputDescriptor[] getInputsForRequestBuilder()
    {
        ArrayList<ActivityInputDescriptor> result = 
            new ArrayList<ActivityInputDescriptor>();
        
        // Get the keys and sort them.  Sorting is done so that testing is
        // easy because output order is predictable and not based on hash map
        // implementation.
        ArrayList<String> sortedKeys = new ArrayList<String>(mInputs.keySet());
        Collections.sort(sortedKeys);
        
        for( String key : sortedKeys )
        {
            ArrayList<TraversableSingleActivityInput> inputs =
                mInputs.get(key);
            
            for( TraversableSingleActivityInput input : inputs)
            {
                result.add(input.getInputDescriptor());
            }
        }

        return result.toArray(new ActivityInputDescriptor[]{});
    }

    /**
     * Gets the array of single activity outputs required by the request
     * builder.
     *  
     * @return array of single activity outputs, one of each output.
     */
    protected SingleActivityOutput[] getOutputsForRequestBuilder()
    {
        ArrayList<SingleActivityOutput> result = 
            new ArrayList<SingleActivityOutput>();
        
        // Get the keys and sort them.  Sorting is done so that testing is
        // easy because output order is predictable and not based on hash map
        // implementation.
        ArrayList<String> sortedKeys = new ArrayList<String>(mOutputs.keySet());
        Collections.sort(sortedKeys);

        for( String key : sortedKeys )
        {
            ArrayList<TraversableSingleActivityOutput> outputs = 
                mOutputs.get(key);
            
            for( TraversableSingleActivityOutput output : outputs)
            {
                result.add(output);
            }
        }
        return result.toArray(new SingleActivityOutput[]{});
    }

    /**
     * Throws an exception reporting that an input is unknown.
     * 
     * @param inputName  input name
     * 
     * @throws IllegalArgumentException always.
     */
    protected void throwUnknownInputException(String inputName)
        throws IllegalArgumentException
    {
        throw new IllegalArgumentException(
            "Unknown input name: " + inputName);
    }

    /**
     * Throws an exception reporting that an output is unknown.
     * 
     * @param outputName  output name
     * 
     * @throws IllegalArgumentException always.
     */
    protected void throwUnknownOutputException(String outputName)
        throws IllegalArgumentException
    {
        throw new IllegalArgumentException(
            "Unknown output name: " + outputName);
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        ArrayList<ActivityInput> result = new ArrayList<ActivityInput>();
        
        for( Entry<String, ArrayList<TraversableSingleActivityInput>> entry : 
             mInputs.entrySet())
        {
            // This maps to an activity input
            ActivityInput input = 
                new GenericActivityActivityInput(
                    entry.getKey(),
                    entry.getValue());
                
            result.add(input);
        }

        return result.toArray(new ActivityInput[]{});
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        ArrayList<ActivityOutput> result = new ArrayList<ActivityOutput>();
        
        for( Entry<String, ArrayList<TraversableSingleActivityOutput>> entry : 
             mOutputs.entrySet())
        {
            // This maps to an activity output
            ActivityOutput output = 
                new GenericActivityActivityOutput(
                    entry.getKey(),
                    entry.getValue());
                
            result.add(output);
        }

        return result.toArray(new ActivityOutput[]{});
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // Nothing to do here
    }
    
    /**
     * {@inheritDoc}
     */
    public void processRequestStatus(final RequestStatus requestStatus)
    {
        super.processRequestStatus(requestStatus);

        //  Just store the request status
        mRequestStatus = requestStatus;
    }
}
