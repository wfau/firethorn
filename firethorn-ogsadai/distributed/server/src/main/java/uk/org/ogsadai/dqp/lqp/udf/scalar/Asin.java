// Copyright (c) The University of Edinburgh, 2010.
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
 * Scalar Asin function. 
 * The parameter value can be any Number type. The output type is always a
 * Double.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Asin extends LogicalExecutableFunctionBase 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2010.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(Asin.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "Asin";
    
    /** The result. */
    private Object mResult;

    /**
     * Constructor.
     */
    public Asin() 
    {
        super(1);  // One parameter
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param sin
     */
    public Asin(Asin asin) 
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
        switch (types[0])
        {
            case TupleTypes._SHORT:
            case TupleTypes._LONG:
            case TupleTypes._INT:
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._BIGDECIMAL:
            case TupleTypes._ODNULL:
                break;
            default:  
                throw new TypeMismatchException(types[0]);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int getOutputType() 
    {
        return TupleTypes._DOUBLE;
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        if (parameters[0] == Null.VALUE)
        {
            mResult = Null.VALUE;
        }
        else
        {
            mResult = Math.asin(((Number)parameters[0]).doubleValue());
        }
    } 
    
    /**
     * {@inheritDoc}
     */
    public Object getResult() 
    {
        return mResult;
    }
}
