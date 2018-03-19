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

import uk.org.ogsadai.expression.arithmetic.Evaluator;
import uk.org.ogsadai.expression.arithmetic.NumericEvaluators;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * Aggregation function MAX.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Max extends SQLAggregateFunction
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    private Number mMax;
    /** Numerical evaluator to calculate max - depends on the parameter type. */
    private Evaluator mEval;
    /** Type of the output parameter. */
    private int mType;
    
    /**
     * Default constructor.
     */
    public Max()
    {
    }

    /**
     * Copy constructor which initialises MAX with the same type but does not 
     * copy the value.
     * 
     * @param max
     *            function to copy
     */
    public Max(Max max)
    {
        mType = max.mType;
        mEval = max.mEval;
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
        mType = parameterTypes[0];
        mEval = NumericEvaluators.getEvaluator(mType);
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        if (parameter != Null.VALUE)
        {
            if (mMax == null)
            {
                mMax = (Number)parameter;
            }
            else
            {
                mMax = mEval.max(mMax, (Number)parameter);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mMax != null)
        {
            return mMax;
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
        return mType;
    }

    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        Number max = ((Max)function).mMax;
        if (max != null)
        {
            if (mMax != null)
            {
                mMax = mEval.max(mMax, max);
            }
            else
            {
                mMax = max;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) throws IOException
    {
        Max max = new Max(this);
        // boolean indicating whether the value is null
        if (!input.readBoolean())
        {
            switch (mType)
            {
                case TupleTypes._SHORT:
                    max.mMax = new Short(input.readShort());
                    break;
                case TupleTypes._INT:
                    max.mMax = new Integer(input.readInt());
                    break;
                case TupleTypes._LONG:
                    max.mMax = new Long(input.readLong());
                    break;
                case TupleTypes._FLOAT:
                    max.mMax = new Float(input.readFloat());
                    break;
                case TupleTypes._DOUBLE:
                    max.mMax = new Double(input.readDouble());
                    break;
                case TupleTypes._BIGDECIMAL:
                    max.mMax = new BigDecimal(input.readUTF());
                    break;
            }
        }
        return max;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeBoolean(mMax == null);
        if (mMax != null)
        {
            switch (mType)
            {
                case TupleTypes._SHORT:
                    output.writeShort(mMax.shortValue());
                    break;
                case TupleTypes._INT:
                    output.writeInt(mMax.intValue());
                    break;
                case TupleTypes._LONG:
                    output.writeLong(mMax.longValue());
                    break;
                case TupleTypes._FLOAT:
                    output.writeFloat(mMax.floatValue());
                    break;
                case TupleTypes._DOUBLE:
                    output.writeDouble(mMax.doubleValue());
                    break;
                case TupleTypes._BIGDECIMAL:
                    output.writeUTF(((BigDecimal)mMax).toString());
                    break;
            }
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
        return "MAX";
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "MAX(value=" + mMax + ")";
    }

}
