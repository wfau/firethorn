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

package uk.org.ogsadai.dqp.lqp.udf;

import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Represents a function which can be executed by an activity.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface ExecutableFunction extends Function
{

    /**
     * Initialises the tuple function with the specified parameter types. This
     * method also performs type validation.
     * 
     * @param types
     *            metadata of the input parameters
     * @throws TypeMismatchException
     *             when parameters have incompatible types
     */
    public void configure(int... types) throws TypeMismatchException;
    
    /**
     * Puts the input parameters for evaluation by the function.
     * 
     * @param parameters
     *            input tuple
     */
    public void put(Object... parameters);

    /**
     * Returns the function result, for all tuples which have been put up to
     * this point. Behaviour might be different for aggregation functions.
     * Calling this function resets the function.
     * 
     * @return the function result
     */
    public Object getResult();

    /**
     * Returns the type (as a TupleType) of the result produced by this function.
     * 
     * @return output type
     */
    public int getOutputType();
}
