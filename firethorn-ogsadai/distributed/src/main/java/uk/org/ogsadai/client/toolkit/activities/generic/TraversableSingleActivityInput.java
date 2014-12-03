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
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.SingleActivityInput;
import uk.org.ogsadai.data.DataValue;

/**
 * Interface used for traversable single activity inputs.  A traversable input
 * allows discovery of the connected output and also supports editing of the
 * input.
 *
 * @author The OGSA-DAI Project Team
 */
public interface TraversableSingleActivityInput extends SingleActivityInput
{
    /**
     * Gets the name of the input.
     * 
     * @return input name
     */
    String getInputName();
    
    /**
     * Gets the activity to which this input belongs.
     * 
     * @return the activity to which this input belongs.
     */
    Activity getActivity();
    
    /**
     * Returns if the input is a literal input.  A literal input is in inputs
     * whose values are specified by the client as oppose to being obtained
     * from the output of another activity.
     * 
     * @return <tt>true</tt> if the input is a literal input, <tt>false</tt>
     *         otherwise.
     */
    boolean isLiteral();
    
    /**
     * Clears the data values of the literal input.  This will switch the
     * input to be a literal input if it is previously a connected input.
     * A literal input with no values is invalid so after calling this method
     * values must be added using either the <tt>add</tt> or 
     * <tt>setDataValues</tt> methods.
     */
    void clearDataValues();

    /**
     * Gets the data values corresponding to the literal input.
     * 
     * @return the data values.
     */
    DataValue[] getDataValues();
    
    /**
     * Sets the data values corresponding to the literal input.  This will 
     * switch the input to be a literal input if it was previously a connected
     * input.  Any previously stored data values will be overwritten.
     * 
     * @param values data values
     */
    void setDataValues(DataValue[] values);
    
    
    /**
     * Gets any output to which this input is connected.  
     * 
     * @return the output that this input is connected to, or <tt>null</tt> if 
     *         the input is unconnected.
     */
    SingleActivityOutput getConnectedOutput();
   
    
    /***
     *  Disconnects the input.  If the input was connected to an output that
     *  belongs to a <tt>GenericActivity</tt> then that output will also be 
     *  informed of the disconnection.
     */
    void disconnect();
    
    /**
     * Removes the specified connection from the input.  This will be called
     * if the <tt>disconnect</tt> is called on an output to which this activity
     * is connected.
     * 
     * @param output output that is now disconnected.
     */
    void removeConnection(SingleActivityOutput output);
}
