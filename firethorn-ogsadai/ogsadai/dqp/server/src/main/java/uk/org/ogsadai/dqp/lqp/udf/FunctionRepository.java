// Copyright (c) The University of Edinburgh, 2008.
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

import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;

/**
 * Function repository interface.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface FunctionRepository
{
    /** Key for function repository in OGSA-DAI context. */
    public static final String FUNCTION_REPOSITORY_KEY = 
        "uk.org.ogsadai.dqp.FUNCTION_REPOSITORY";

    /**
     * Registers the specified class with the repository. An instance of this
     * function will be created to lookup the name of the function.
     * 
     * @param function
     *            function to register
     */
    public void register(Class<? extends Function> function);

    /**
     * Creates a new function instance for the function identified by the
     * specified name and returns it. Function names are case insensitive. 
     * 
     * @param name
     *            name of the function
     * @return instance of the function class identified by the input name
     * @throws NoSuchFunctionException
     *             if there was no function with the specified name in the
     *             repository
     */
    public Function getFunctionInstanceByName(String name) throws NoSuchFunctionException;
}
