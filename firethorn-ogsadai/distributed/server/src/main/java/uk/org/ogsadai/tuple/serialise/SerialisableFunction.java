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


package uk.org.ogsadai.tuple.serialise;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import uk.org.ogsadai.dqp.lqp.udf.Function;

/**
 * Serialisable function interface. Functions that can write their current state
 * to an output stream and construct it from an input stream implement this 
 * interface.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface SerialisableFunction extends Function
{
    /**
     * Merges this function with another by merging their result values.
     * 
     * @param function
     *            function to merge with
     */
    public abstract void merge(SerialisableFunction function); 
    
    /**
     * Write the current status of the function to an output stream.
     * 
     * @param output
     *            output to write to
     * @throws IOException
     *             if a problem occurred writing to the stream
     */
    public abstract void serialise(DataOutputStream output) throws IOException;

    /**
     * Construct a new function from the input stream.
     * 
     * @param input
     *            input to read from
     * @return deserialised function
     * @throws IOException
     *             if a problem occurred writing to the stream
     */
    public abstract SerialisableFunction deserialise(DataInputStream input) 
        throws IOException;

}
