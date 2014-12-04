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


package uk.org.ogsadai.dqp.lqp.udf.aggregate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * Function AVERAGE.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Average extends SQLAggregateFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Current count. */
    private long mCount;
    /** Current sum of values. */
    private double mSum;
    /** Current sum of values in a big decimal or <code>null</code>. */
    private BigDecimal mSumBD;
    /** Are we using big decimals? */
    private boolean mIsBigDecimal;

    /**
     * Default constructor.
     */
    public Average()
    {
    }
    
    /**
     * Copy constructor. The current values are not copied.
     * 
     * @param avg
     *            function to copy
     */
    public Average(Average avg)
    {
        this();
        mIsBigDecimal = avg.mIsBigDecimal;
        if (mIsBigDecimal)
        {
            mSumBD = BigDecimal.ZERO;
        }
    }
    
    // -----------------------
    // LogicalFunction methods 
    // -----------------------

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "AVG";
    }

    // --------------------------
    // ExecutableFunction methods 
    // --------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(int... types) throws TypeMismatchException 
    {
        validateType(types[0]);
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        if (mIsBigDecimal)
        {
            return TupleTypes._BIGDECIMAL;
        }
        else
        {
            return TupleTypes._DOUBLE;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        if (parameter != Null.VALUE)
        {
            mCount++;
            add((Number)parameter);
        }
    }

    /**
     * Adds the given number to the current sum.
     * 
     * @param number
     *            number to add
     */
    private void add(Number number)
    {
        if (mIsBigDecimal)
        {
            mSumBD = mSumBD.add((BigDecimal)number);
        }
        else
        {
            mSum += number.doubleValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mCount != 0)
        {
            if (mIsBigDecimal)
            {
                // we want scale of at least 5 in case we're diving integers
                if (mSumBD.scale() < 5)
                {
                    return mSumBD.divide(new BigDecimal(mCount), 5, BigDecimal.ROUND_HALF_UP);
                }
                else
                {
                    return mSumBD.divide(new BigDecimal(mCount), BigDecimal.ROUND_HALF_UP);
                }
            }
            else
            {
                return new Double(mSum / mCount);
            }
        }
        else
        {
            return Null.VALUE;
        }
    }

    /**
     * Validates that the parameter type is numeric.
     * 
     * @param type
     *            input type
     * @throws TypeMismatchException
     *             if the type is not numeric
     */
    private void validateType(int type) throws TypeMismatchException
    {
        switch(type)
        {
            case TupleTypes._SHORT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE:
                mIsBigDecimal = false;
                break;
            case TupleTypes._BIGDECIMAL:
                mIsBigDecimal = true;
                mSumBD = BigDecimal.ZERO;
                break;
            default: 
                throw new TypeMismatchException(type);
        }
    }
    
    // ----------------------------
    // SerialisableFunction methods
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        Average avg = (Average)function;
        mCount += avg.mCount;
        mSum += avg.mSum;
        if (avg.mIsBigDecimal)
        {
            mSumBD.add(avg.mSumBD);
            mIsBigDecimal = true;
        }
        else
        {
            mIsBigDecimal = false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) throws IOException
    {
        Average avg = new Average(this);
        avg.mIsBigDecimal = input.readBoolean();
        if (avg.mIsBigDecimal)
        {
            avg.mSumBD = new BigDecimal(input.readUTF());
        }
        else
        {
            avg.mSum = input.readDouble();
        }
        avg.mCount = input.readLong();
        return avg;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeBoolean(mIsBigDecimal);
        if (mIsBigDecimal)
        {
            output.writeUTF(mSumBD.toString());
        }
        else
        {
            output.writeDouble(mSum);
        }
        output.writeLong(mCount);
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        if (mIsBigDecimal)
        {
            return "AVERAGE(sum=" + mSumBD + ", count=" + mCount + ")";
        }
        else
        {
            return "AVERAGE(sum=" + mSum + ", count=" + mCount + ")";
        }
    }



}
