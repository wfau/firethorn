// Copyright (c) The University of Edinburgh, 2011.
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

package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.util.Random;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar Counter function. 
 * <p>
 * The Counter function returns the next counter value whenever it is
 * called. It start with 0 then 1 etc. 
 * <p>
 * Note the counter starts at 0 again for each instance of the function.
 * It is a useful function when combined with the TupleArithmeticSample
 * activity.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Counter extends LogicalExecutableFunctionBase 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2011.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(Counter.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "Counter";
    
    private long mCount = 0;
    
    /**
     * Constructor.
     */
    public Counter() 
    {
        super(0);  // Zero parameters
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param rand
     */
    public Counter(Counter counter) 
    {
        this();
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName() 
    {
        return FUNCTION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public FunctionType getType() 
    {
        return FunctionType.UDF_SCALAR;
    }
    
    /**
     * Configures the function with the given data types. 
     */
    public void configure(int... types) throws TypeMismatchException 
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public int getOutputType() 
    {
        return TupleTypes._LONG;
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
    } 
    
    /**
     * {@inheritDoc}
     */
    public Object getResult() 
    {
        return mCount++;
    }
}
