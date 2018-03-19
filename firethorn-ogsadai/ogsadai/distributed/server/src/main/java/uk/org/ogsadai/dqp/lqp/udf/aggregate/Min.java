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
 * Function MIN.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Min extends SQLAggregateFunction
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    /** Output type. */
    private int mType;
    /** The current value. */
    private Number mMin;
    /** Numeric evaluator, depends on the column type. */
    private Evaluator mEval;

    /**
     * Constructs a new MIN function.
     */
    public Min()
    {
    }

    /**
     * Copy constructor.
     * 
     * @param min
     *            expression to copy
     */
    public Min(Min min)
    {
        mType = min.mType;
        mEval = min.mEval;
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
            if (mMin == null)
            {
                mMin = (Number)parameter;
            }
            else {
                mMin = mEval.min(mMin, (Number)parameter);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mMin != null)
        {
            return mMin;
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
        mMin = mEval.min(mMin, ((Min)function).mMin);
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) 
        throws IOException
    {
        Min min = new Min(this);
        if (!input.readBoolean())
        {
            switch (mType)
            {
                case TupleTypes._SHORT:
                    min.mMin = new Short(input.readShort());
                    break;
                case TupleTypes._INT:
                    min.mMin = new Integer(input.readInt());
                    break;
                case TupleTypes._LONG:
                    min.mMin = new Long(input.readLong());
                    break;
                case TupleTypes._FLOAT:
                    min.mMin = new Float(input.readFloat());
                    break;
                case TupleTypes._DOUBLE:
                    min.mMin = new Double(input.readDouble());
                    break;
                case TupleTypes._BIGDECIMAL:
                    min.mMin = new BigDecimal(input.readUTF());
                    break;
            }
        }
        return min;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeBoolean(mMin == null);
        if (mMin != null)
        {
            switch (mType)
            {
                case TupleTypes._SHORT:
                    output.writeShort(mMin.shortValue());
                    break;
                case TupleTypes._INT:
                    output.writeInt(mMin.intValue());
                    break;
                case TupleTypes._LONG:
                    output.writeLong(mMin.longValue());
                    break;
                case TupleTypes._FLOAT:
                    output.writeFloat(mMin.floatValue());
                    break;
                case TupleTypes._DOUBLE:
                    output.writeDouble(mMin.doubleValue());
                    break;
                case TupleTypes._BIGDECIMAL:
                    output.writeUTF(((BigDecimal)mMin).toString());
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
        return "MIN";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "MIN(value=" + mMin + ")";
    }

}
