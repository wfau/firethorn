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
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;
import uk.org.ogsadai.util.BigDecimalUtil;

/**
 * Standard deviation aggregation function.
 * <p>
 * Algorithm is the on-line algorithm described in Wikipedia page:
 * http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance.
 * 
 * @author The OGSA-DAI Project Team
 */
public class StandardDeviation extends SQLAggregateFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Variance function to do most of the work. */
    private Variance mVariance;
    
    /** 
     * Default constructor.
     */
    public StandardDeviation()
    {
        mVariance = new Variance();
    }
    
    /**
     * Copy constructor. 
     * 
     * @param stddev
     *            function to copy
     */
    public StandardDeviation(StandardDeviation stddev)
    {
        this();
        mVariance = new Variance(stddev.mVariance);
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
        mVariance.configure(types);
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        mVariance.put(parameters);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        Object varianceObj = mVariance.getResult();
        if (varianceObj == Null.VALUE)
        {
            return Null.VALUE;
        }
        else if (varianceObj instanceof Double)
        {
            double variance = ((Double) varianceObj).doubleValue();
            return new Double(Math.sqrt(variance));
        }
        else
        {
            BigDecimal variance = (BigDecimal) varianceObj;
            return BigDecimalUtil.root(variance, 2, variance.scale());
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return mVariance.getOutputType();
    }
    
    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        StandardDeviation stddev = (StandardDeviation) function;
        mVariance.merge(stddev.mVariance);
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) 
        throws IOException
    {
        StandardDeviation result = new StandardDeviation();
        result.mVariance = (Variance) mVariance.deserialise(input);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        mVariance.serialise(output);
    }

    // -----------------------
    // LogicalFunction methods 
    // -----------------------
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "STDDEV";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "STDDEV";
    }
}
