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
 * Aggregation function to combine partial variance calculations computed 
 * independently into single variance result.  This approach allows just the
 * partial variance calculations to be transferred between OGSA-DAI servers
 * rather than all the data values over which the variance is being measured.
 * <p>
 * Three values are required by this function.  These are the values 
 * output by functions VarianceM2, Average and Count.
 * 
 * @author The OGSA-DAI Project Team
 */
public class VarianceCombine extends SQLAggregateFunction
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
     * Constructor.
     */
    public VarianceCombine()
    {
        // Empty constructor
    }
    
    /**
     * Copy constructor. 
     * 
     * @param varianceCombine
     *            function to copy
     */
    public VarianceCombine(VarianceCombine varianceCombine)
    {
        this();
        mIsBigDecimal = varianceCombine.mIsBigDecimal;
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
        if (types.length != 3)
        {
            // TODO: How should I report such an error.  Type mismatch does
            // not seem to handle it.
        }
        
        validateM2Type(types[0]);              // m2
        validateMeanType(types[1], types[0]);  // mean
        
        if (types[2] != TupleTypes._LONG)
        {
            throw new TypeMismatchException(types[2]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object m2 = parameters[0];
        Object mean = parameters[1];
        Object count = parameters[2];
        
        if (mean != Null.VALUE)
        {
            if (mIsBigDecimal)
            {
                putValues((BigDecimal) m2, (BigDecimal) mean, (Number) count);
            }
            else
            {
                putValues((Number) m2, (Number) mean, (Number) count);
            }
        }
    }
    
    /**
     * 
     * @param m2
     * @param mean
     * @param count
     */
    public void putValues(BigDecimal m2, BigDecimal mean, Number count)
    {
        // Merge equations are taken from the Parallel algorithm section of
        // http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
        
        BigDecimal countBD = BigDecimal.valueOf(count.longValue());
        
        BigDecimal delta = mean.subtract(mMeanBD);
        BigDecimal newN = mNBD.add(countBD);
        mMeanBD = mMeanBD.add(delta.multiply(countBD).divide(newN));
        mM2BD = mM2BD.add(m2).add(
            delta.multiply(delta).multiply(
                mNBD.multiply(countBD).divide(newN)));
        mNBD = newN;
    }
    
    /**
     * 
     * @param m2
     * @param mean
     * @param count
     */
    public void putValues(Number m2, Number mean, Number count)
    {
        double m2Double = m2.doubleValue();
        double meanDouble = mean.doubleValue();
        double countDouble = count.doubleValue();
        
        double delta = meanDouble - mMean;
        double newN = mN+countDouble;
        mMean = mMean + delta*countDouble/newN;
        mM2 = mM2 + m2Double + delta*delta*(mN*countDouble/newN);
        mN = newN;
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
        
        VarianceCombine variance = (VarianceCombine) function;
        
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
        VarianceCombine variance = new VarianceCombine(this);
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

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "VARIANCE_COMBINE";
    }

    
    /**
     * Validates that the parameter type for M2 is numeric.
     * 
     * @param type
     *            input type
     * @throws TypeMismatchException
     *             if the type is not numeric
     */
    private void validateM2Type(int type) throws TypeMismatchException
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
    
    /**
     * Validates that the parameter type for mean is numeric, and consistent
     * with the type for m2.
     * 
     * @param meanType
     *            mean type
     * @param m2Type
     *            m2type
     * @throws TypeMismatchException
     *             if the type is not numeric, or not consistent with the m2
     *             type
     */
    private void validateMeanType(int meanType, int m2Type) 
        throws TypeMismatchException
    {
        if (mIsBigDecimal)
        {
            if (meanType != TupleTypes._BIGDECIMAL)
            {
                throw new TypeMismatchException(m2Type, meanType);
            }
        }
        else
        {
            switch(meanType)
            {
                case TupleTypes._SHORT:
                case TupleTypes._INT:
                case TupleTypes._LONG:
                case TupleTypes._FLOAT:
                case TupleTypes._DOUBLE:
                    break;
                case TupleTypes._BIGDECIMAL:
                    throw new TypeMismatchException(m2Type, meanType);
                default: 
                    throw new TypeMismatchException(meanType);
            }
        }
    }    
}
