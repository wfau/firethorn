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
 * Aggregation function SUM.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Sum extends SQLAggregateFunction
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** The current result. */
    private BigDecimal mSum;
    /** Converts numbers into BigDecimal. */
    private Converter mConverter;

    /**
     * Constructs a new SUM expression.
     */
    public Sum()
    {
    }
    
    /**
     * Copy constructor.
     * 
     * @param sum
     *            expression to copy
     */
    public Sum(Sum sum)
    {
        mConverter = sum.mConverter;
    }
    
    // -----------------------
    // LogicalFunction methods 
    // -----------------------
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "SUM";
    }
    
    // --------------------------
    // ExecutableFunction methods 
    // --------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(int... parameterTypes)
        throws TypeMismatchException
    {
        int type = parameterTypes[0];
        switch(type)
        {
            case TupleTypes._SHORT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
                mConverter = new IntegerConverter();
                break;
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE: 
                mConverter = new DecimalConverter();
                break;
            case TupleTypes._BIGDECIMAL:
                mConverter = new BigDecimalConverter();
                break;
            default: 
                throw new TypeMismatchException(type);
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
            if (mSum == null)
            {
                mSum = mConverter.getValue((Number)parameter);
            }
            else
            {
                mSum = mSum.add(mConverter.getValue((Number)parameter));
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mSum != null)
        {
            return mSum;
        }
        else
        {
            return Null.VALUE;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._BIGDECIMAL;
    }

    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        BigDecimal sum = ((Sum)function).mSum; 
        if (mSum != null)
        {
            if (sum != null)
            {
                mSum = mSum.add(sum);
            }
        }
        else
        {
            if (sum != null)
            {
                mSum = sum;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) 
        throws IOException
    {
        Sum sum = new Sum(this);
        sum.mConverter = mConverter;
        if (!input.readBoolean())
        {
            sum.mSum = new BigDecimal(input.readUTF());
        }
        return sum;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeBoolean(mSum == null);
        if (mSum != null)
        {
            output.writeUTF(mSum.toString());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "SUM(value=" + mSum + ")";
    }

    /**
     * Converter interface.
     *
     * @author The OGSA-DAI Project Team.
     */
    private interface Converter
    {
        /**
         * Returns a number as a BigDecimal.
         * 
         * @param value
         *            number to convert
         * @return wrapped number value
         */
        public BigDecimal getValue(Number value);
    }
    
    /**
     * Converts integers to BigDecimals.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class IntegerConverter implements Converter
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        /**
         * {@inheritDoc}
         */
        public BigDecimal getValue(Number value)
        {
            return new BigDecimal(value.longValue());
        }
    }
    
    /**
     * Converts decimals to BigDecimals.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class DecimalConverter implements Converter
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";

        /**
         * {@inheritDoc}
         */
        public BigDecimal getValue(Number value)
        {
            return new BigDecimal(value.doubleValue());
        }
    }
    /** 
     * Returns BigDecimals.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class BigDecimalConverter implements Converter
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";

        /**
         * {@inheritDoc}
         */
        public BigDecimal getValue(Number value)
        {
            return (BigDecimal)value;
        }
    }
}
