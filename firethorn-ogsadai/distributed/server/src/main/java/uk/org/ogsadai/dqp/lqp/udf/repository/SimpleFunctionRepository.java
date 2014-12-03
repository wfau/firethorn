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

package uk.org.ogsadai.dqp.lqp.udf.repository;

import java.util.HashMap;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;

/**
 * Simple implementation of a function repository which stores all registered
 * functions in memory.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleFunctionRepository implements FunctionRepository
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Functions map. */
    private final Map<String, Class<? extends Function>> mFunctions =
        new HashMap<String, Class<? extends Function>>();

    /**
     * {@inheritDoc}
     */
    public Function getFunctionInstanceByName(String name) 
        throws NoSuchFunctionException
    {
        Class<? extends Function> cl = mFunctions.get(name.toLowerCase());
        if (cl == null)
        {
            throw new NoSuchFunctionException(name);
        }
        try
        {
            return cl.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new FunctionInstantiationException(cl);
        }
        catch (IllegalAccessException e)
        {
            throw new FunctionInstantiationException(cl);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void register(Class<? extends Function> cl)
    {
        try
        {
            // we need to instantiate the function to get the name
            Function function = cl.newInstance();
            mFunctions.put(function.getName().toLowerCase(), cl);
        }
        catch (InstantiationException e)
        {
            throw new FunctionInstantiationException(cl);
        }
        catch (IllegalAccessException e)
        {
            throw new FunctionInstantiationException(cl);
        }
    }
    
    /**
     * Returns the map of registered functions.
     * 
     * @return map of functions
     */
    public Map<String, Class<? extends Function>> getFunctions()
    {
        return mFunctions;
    }
    
}
