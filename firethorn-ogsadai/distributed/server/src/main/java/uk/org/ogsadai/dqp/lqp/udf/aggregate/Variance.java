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
 * Variance aggregation function.
 * <p>
 * Algorithm is the on-line algorithm described in Wikipedia page:
 * http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance.
 * 
 * @author The OGSA-DAI Project Team
 */
public class Variance extends SQLAggregateFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** n from the algorithm. */
    private double mN;
    /** mean from the algorithm. */
    private double mMean;
    /** m2 from the algorithm. */
    private double mM2;
    /** n from the algorithm when using big decimal. */
    private BigDecimal mNBD;
    /** mean from the algorithm when using big decimal. */
    private BigDecimal mMeanBD;
    /** m2 from the algorithm when using big decimal. */
    private BigDecimal mM2BD;
    
    /** Are we using big decimals? */
    private boolean mIsBigDecimal;

    /** 
     * Default constructor.
     */
    public Variance()
    {
        // Empty constructor
    }
    
    /**
     * Copy constructor. 
     * 
     * @param variance
     *            function to copy
     */
    public Variance(Variance variance)
    {
        this();
        mIsBigDecimal = variance.mIsBigDecimal;
        if (mIsBigDecimal)
        {
            mMeanBD = BigDecimal.ZERO;
            mM2BD = BigDecimal.ZERO;
            mNBD = BigDecimal.ZERO;
        }
    }
    
    // --------------------------
    // ExecutableFunction methods 
    // --------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(int... types)
        throws TypeMismatchException
    {
        validateType(types[0]);
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        if (parameter != Null.VALUE)
        {
            if (mIsBigDecimal)
            {
                put((BigDecimal) parameter);
            }
            else
            {
                put((Number) parameter);
            }
        }
    }
    
    /**
     * Adds a new BigDecimal value to the computation.
     * 
     * @param x value to add to the computation.
     */
    private void put(BigDecimal x)
    {
        // n = n + 1
        mNBD = mNBD.add(BigDecimal.ONE);
        // delta = x - mean
        BigDecimal delta = x.subtract(mMeanBD);
        // mean = mean + delta/n
        int scale = delta.scale();
        if (scale < 5)
        {
            scale = 5;
        }
        mMeanBD = mMeanBD.add(
            delta.divide(mNBD, scale, BigDecimal.ROUND_HALF_UP));
        // M2 = M2 + delta*(x - mean)
        mM2BD = mM2BD.add(delta.multiply(x.subtract(mMeanBD)));
    }
    
    /**
     * Adds a new Number value to the computation.
     * 
     * @param x value to add to the computation.
     */
    private void put(Number x)
    {
        double xDouble = x.doubleValue();

        // n = n + 1
        mN  = mN + 1;
        // delta = x - mean
        double delta = xDouble - mMean;
        // mean = mean + delta/n
        mMean  = mMean + (delta/mN);
        // M2 = M2 + delta*(x - mean)
        mM2 = mM2 + delta * (xDouble - mMean);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mIsBigDecimal)
        {
            
            if (mNBD.compareTo(new BigDecimal(0.1)) < 0) // mN == 0
            {
                return Null.getValue();
            }
            int scale = mM2BD.scale();
            if (scale<5)
            {
                scale = 5;
            }
            BigDecimal variance = 
                mM2BD.divide(mNBD, scale, BigDecimal.ROUND_HALF_UP);
            return variance;
        }
        else
        {
            if (mN < 0.1) // mN == 0
            {
                return Null.getValue();
            }
            double variance = mM2/mN;
            return new Double(variance);
        }
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
    
    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        // Merge equations are taken from the Parallel algorithm section of
        // http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
        
        Variance variance = (Variance) function;
        
        if (variance.mIsBigDecimal)
        {
            BigDecimal delta = variance.mMeanBD.subtract(mMeanBD);
            BigDecimal newN = mNBD.add(variance.mNBD);
            mMeanBD = mMeanBD.add(delta.multiply(variance.mNBD).divide(newN));
            mM2BD = mM2BD.add(variance.mM2BD).add(
                delta.multiply(delta).multiply(
                    mNBD.multiply(variance.mNBD).divide(newN)));
            mNBD = newN;
        }
        else
        {
            double delta = variance.mMean - mMean;
            double newN = mN+variance.mN;
            mMean = mMean + delta*variance.mN/newN;
            mM2 = mM2 + variance.mM2 + delta*delta*(mN*variance.mN/newN);
            mN = newN;
        }
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) throws IOException
    {
        Variance variance = new Variance(this);
        variance.mIsBigDecimal = input.readBoolean();
        if (variance.mIsBigDecimal)
        {
            variance.mNBD    = new BigDecimal(input.readUTF());
            variance.mMeanBD = new BigDecimal(input.readUTF());
            variance.mM2BD   = new BigDecimal(input.readUTF());
        }
        else
        {
            variance.mN    = input.readDouble();
            variance.mMean = input.readDouble();
            variance.mM2   = input.readDouble();
        }
        return variance;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeBoolean(mIsBigDecimal);
        if (mIsBigDecimal)
        {
            output.writeUTF(mNBD.toString());
            output.writeUTF(mMeanBD.toString());
            output.writeUTF(mM2BD.toString());
        }
        else
        {
            output.writeDouble(mN);
            output.writeDouble(mMean);
            output.writeDouble(mM2);
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
        return "VARIANCE";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "VARIANCE";
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
                mMeanBD = BigDecimal.ZERO;
                mM2BD = BigDecimal.ZERO;
                mNBD = BigDecimal.ZERO;
                break;
            default: 
                throw new TypeMismatchException(type);
        }
    }
}
