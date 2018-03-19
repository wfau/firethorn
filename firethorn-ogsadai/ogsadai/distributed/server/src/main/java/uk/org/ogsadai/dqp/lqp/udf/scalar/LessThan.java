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

import java.math.BigDecimal;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to calculate less than.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class LessThan extends LogicalExecutableFunctionBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2011.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(LessThan.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "LessThan";
    
    /** The data type of X in the expression LessThan(X, Y). */
    private int mTypeX = -1;

    /** The data type of Y in the expression LessThan(X, Y). */
    private int mTypeY = -1;
    
    /** The type use for the comparison. */
    private int mComparisonType = -1;

    /** The evaluation result. */
    private Object mResult;
    
    /**
     * Constructor.
     */
    public LessThan() 
    {
        super(2);
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public LessThan(LessThan lessThan) 
    {
        this();
        mTypeX = lessThan.getTypeX();
        mTypeY = lessThan.getTypeY();
        mComparisonType = lessThan.mComparisonType;
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
     * Configures the function with the given data types. The parameter 
     * must be passed in the order [type of X, type of Y] where the aim is to 
     * do X<Y.
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
        mComparisonType = 
            TypeConverter.getArithmeticType(mTypeX, mTypeY);
    }
    
    /**
     * Returns the type of X for the expression X<Y.
     * 
     * @return type.
     */
    protected int getTypeX() 
    {
        return mTypeX;
    }
    
    /**
     * Returns the type of Y for the expression X<Y.
     * 
     * @return type.
     */
    protected int getTypeY() 
    {
        return mTypeY;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getOutputType() 
    {
        return TupleTypes._BOOLEAN;
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
        if (parameters[0] == Null.VALUE || parameters[1] == Null.VALUE)
        {
            mResult = Null.VALUE;
        }
        else
        {
            Object x = null;
            Object y = null;
            try 
            {
                x = TypeConverter.convertObject(
                    mTypeX, mComparisonType, parameters[0]);
                y = TypeConverter.convertObject(
                    mTypeY, mComparisonType, parameters[1]);
            } 
            catch (TypeConversionException e) 
            {
                LOG.error(e, true);
                throw new RuntimeException(e);
            }
            switch(mComparisonType) 
            {
                case TupleTypes._SHORT:
                    short xs = (Short)x;
                    short ys = (Short)y;
                    mResult = (xs<ys);
                    break;
                case TupleTypes._LONG:
                    long xl = (Long)x;
                    long yl = (Long)y;
                    mResult = (xl<yl);
                    break;
                case TupleTypes._INT:
                    int xi = (Integer)x;
                    int yi = (Integer)y;
                    mResult = (xi<yi);
                    break;
                case TupleTypes._DOUBLE:
                    double xd = (Double)x;
                    double yd = (Double)y;
                    mResult = (xd<yd);
                    break;
                case TupleTypes._FLOAT:
                    float xf = (Float)x;
                    float yf = (Float)y;
                    mResult = (xf<yf);
                    break;
                case TupleTypes._BIGDECIMAL:
                    BigDecimal xb = (BigDecimal)x;
                    BigDecimal yb = (BigDecimal)y;
                    mResult = (xb.compareTo(yb) < 0);
                    break;
            }
        }
    }
}
