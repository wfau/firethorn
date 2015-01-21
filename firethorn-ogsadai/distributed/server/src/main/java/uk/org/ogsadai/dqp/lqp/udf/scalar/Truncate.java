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

import java.math.BigDecimal;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar Truncate function. 
 * The parameter value can be any Number type. The output type is always a
 * Double.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Truncate extends LogicalExecutableFunctionBase 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2010.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(Truncate.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "Truncate";
    
    /** The result. */
    private Object mResult;
    
    /** The data type of X in the expression Truncate(X, Y). */
    private int mTypeX = -1;

    /** The data type of Y in the expression Truncate(X, Y). */
    private int mTypeY = -1;
    
    /**
     * Constructor.
     */
    public Truncate() 
    {
        super(2);  // Two parameter
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param sin
     */
    public Truncate(Truncate Truncate) 
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
     * Returns the type of X for the expression TRUNCATE(X,Y).
     * 
     * @return type.
     */
    protected int getTypeX() 
    {
        return mTypeX;
    }
    
    /**
     * Returns the type of Y for the expression TRUNCATE(X,Y).
     * 
     * @return type.
     */
    protected int getTypeY() 
    {
        return mTypeY;
    }
    /**
     * Configures the function with the given data types. 
     */
    public void configure(int... types) throws TypeMismatchException 
    {
        for (int i=0; i<2; i++) 
        {
            switch(types[i]) {
                case TupleTypes._SHORT:
                case TupleTypes._LONG:
                case TupleTypes._INT:
                case TupleTypes._DOUBLE:
                case TupleTypes._FLOAT:
                case TupleTypes._BIGDECIMAL:
                    break;
                default:  
                    throw new TypeMismatchException(types[0]);
            }
        }
        mTypeX = types[0];
        mTypeY = types[1];
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
            mResult = this.truncateDecimal((Double)parameters[0],(Integer)parameters[1]).doubleValue();
        }
    } 
    
    /**
     * {@inheritDoc}
     */
    public Object getResult() 
    {
        return mResult;
    }
    
    private  BigDecimal truncateDecimal(double x,int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}
