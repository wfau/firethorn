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
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;

/**
 * Implementations of this interface add activities to a builder which serialise
 * and deserialise tuple lists into a certain format.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface TupleSerialiser
{
    /**
     * Sets the size of a block that is produced by the serialiser. A block
     * could be a byte array or a character array, depending on the
     * implementation.
     * 
     * @param blockSize
     *            size of blocks
     */
    public void setBlockSize(int blockSize);

    /**
     * Adds serialiser activities to the given builder. The data is streamed
     * from the output.
     * 
     * @param output
     *            output providing the serialised tuple list
     * @param builder
     *            builds a pipeline workflow
     * @return the output producing the serialised tuple list
     */
    public TraversableSingleActivityOutput addSerialiser(
            SingleActivityOutput output,
            PipelineWorkflowBuilder builder);
    
    /**
     * Adds deserialiser activities to the given builder. The input data is
     * streamed from the output.
     * 
     * @param output
     *            output providing the deserialised tuple list
     * @param builder
     *            builds a pipeline workflow
     * @return the output producing the deserialised tuple list
     */
    public TraversableSingleActivityOutput addDeserialiser(
            SingleActivityOutput output,
            PipelineWorkflowBuilder builder);

}
