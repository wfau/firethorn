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
 * Adds activities that serialise tuple lists to a binary representation.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ByteArrayTupleSerialiser implements TupleSerialiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Size of byte arrays to produce. */
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
    public TraversableSingleActivityOutput addDeserialiser(
            SingleActivityOutput output, 
            PipelineWorkflowBuilder builder)
    {
        GenericActivity byteArraysToTuple = 
            new GenericActivity("uk.org.ogsadai.ByteArraysToTuple");
        byteArraysToTuple.createInput("data");
        byteArraysToTuple.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        byteArraysToTuple.connectInput("data", output);
        builder.add(byteArraysToTuple);
        
        return byteArraysToTuple.getOutput("result");
    }

    /**
     * {@inheritDoc}
     */
    public TraversableSingleActivityOutput addSerialiser(
            SingleActivityOutput output,
            PipelineWorkflowBuilder builder)
    {
        GenericActivity controlledRepeat = 
            new GenericActivity("uk.org.ogsadai.ControlledRepeat");
        controlledRepeat.createInput("repeatedInput");
        controlledRepeat.createInput("input");
        controlledRepeat.createOutput("repeatedOutput");
        controlledRepeat.createOutput("output");
        builder.add(controlledRepeat);
        controlledRepeat.connectInput("input", output);
        controlledRepeat.addInput("repeatedInput", mBlockSize);

        GenericActivity tupleToByteArrays =
            new GenericActivity("uk.org.ogsadai.TupleToByteArrays");
        tupleToByteArrays.createInput("data");
        tupleToByteArrays.createInput("size");
        tupleToByteArrays.createOutput("result");
        tupleToByteArrays.connectInput(
            "data", controlledRepeat.getOutput("output"));
        tupleToByteArrays.connectInput(
            "size", controlledRepeat.getOutput("repeatedOutput"));
        builder.add(tupleToByteArrays);
        return tupleToByteArrays.getOutput("result");
    }

}
