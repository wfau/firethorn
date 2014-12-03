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


package uk.org.ogsadai.dqp.lqp.udf.repository;

/**
 * Raised if instantiation of a function failed.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FunctionInstantiationException extends RuntimeException
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009";
    
    /**
     * Constructor.
     * 
     * @param cl
     *            class that could not be instantiated.
     */
    public FunctionInstantiationException(Class<?> cl)
    {
        super("A problem occurred instantiating class " + cl.getName());
    }

}
