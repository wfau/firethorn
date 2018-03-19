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

package uk.org.ogsadai.dqp.execute.workflow;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;

/**
 * Adds activities to serialise and deserialise tuple lists to a WebRowSet XML
 * representation.
 *
 * @author The OGSA-DAI Project Team.
 */
public class WebRowSetTupleSerialiser implements TupleSerialiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Size of character arrays */
    private int mBlockSize;

    /**
     * {@inheritDoc}
     */
    public void setBlockSize(int blockSize)
    {
        mBlockSize = blockSize;
    }

    /**
     * {@inheritDoc}
     */
    public TraversableSingleActivityOutput addSerialiser(
            SingleActivityOutput output,
            PipelineWorkflowBuilder builder)
    {
        GenericActivity tupleToWRS = new GenericActivity(
                "uk.org.ogsadai.TupleToWebRowSetCharArrays");
        tupleToWRS.createInput("data");
        tupleToWRS.createOutput("result");
        tupleToWRS.connectInput("data", output);
        builder.add(tupleToWRS);

        GenericActivity controlledRepeat = 
            new GenericActivity("uk.org.ogsadai.ControlledRepeat");
        controlledRepeat.createInput("repeatedInput");
        controlledRepeat.createInput("input");
        controlledRepeat.createOutput("repeatedOutput");
        controlledRepeat.createOutput("output");
        builder.add(controlledRepeat);
        controlledRepeat.connectInput("input", tupleToWRS.getOutput("result"));
        controlledRepeat.addInput("repeatedInput", mBlockSize);
        
        GenericActivity resize = new GenericActivity(
                "uk.org.ogsadai.CharArraysResize");
        resize.createInput("data");
        resize.createInput("sizeInChars");
        resize.createOutput("result");
        resize.connectInput(
            "data", controlledRepeat.getOutput("output"));
        resize.connectInput(
            "sizeInChars", controlledRepeat.getOutput("repeatedOutput"));
        builder.add(resize);

        return resize.getOutput("result");
    }

    /**
     * {@inheritDoc}
     */
    public TraversableSingleActivityOutput addDeserialiser(
            SingleActivityOutput output,
            PipelineWorkflowBuilder builder)
    {
        GenericActivity wrsToTuple = 
            new GenericActivity("uk.org.ogsadai.WebRowSetCharacterDataToTuple");
        wrsToTuple.createInput("data");
        wrsToTuple.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        wrsToTuple.connectInput("data", output);
        builder.add(wrsToTuple);
        return wrsToTuple.getOutput("result");
    }

}
