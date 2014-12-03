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

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function convert a boolean to an integer. True maps to 1 and false
 * maps to 0.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BooleanToInteger extends LogicalExecutableFunctionBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2011.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(BooleanToInteger.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "BooleanToInteger";
    
    /** The evaluation result. */
    private Object mResult;
    
    /**
     * Constructor.
     */
    public BooleanToInteger() 
    {
        super(1);
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public BooleanToInteger(BooleanToInteger booleanToInteger) 
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
        if (types[0] != TupleTypes._BOOLEAN)
        {
            throw new TypeMismatchException(types[0]);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int getOutputType() 
    {
        return TupleTypes._INT;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult() 
    {
        return mResult;
    }
    
    /**
     * Puts the input parameters X and Y where the aim is to do X<Y. The 
     * parameters must be passed in the order [X, Y].
     * 
     */
    public void put(Object... parameters) 
    {
        if (parameters[0] == Null.VALUE)
        {
            mResult = Null.VALUE;
        }
        else
        {
            if (((Boolean)parameters[0]).booleanValue())
            {
                mResult = new Integer(1);
            }
            else
            {
                mResult = new Integer(0);
            }
        }
    }
}
