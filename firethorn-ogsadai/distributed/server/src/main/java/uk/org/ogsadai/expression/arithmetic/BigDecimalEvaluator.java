//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.expression.arithmetic;

import java.math.BigDecimal;

/**
 * Evaluates arithmetic expressions with BigInteger operands.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class BigDecimalEvaluator implements Evaluator
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
 
    /**
     * {@inheritDoc}
     */
    public Number plus(Number value1, Number value2)
    {
        if (value1 == null || value2 == null)
        {
            return null;
        }
        return (((BigDecimal)value1).add((BigDecimal)value2));
    }

    /**
     * {@inheritDoc}
     */
    public Number minus(Number value1, Number value2)
    {
        if (value1 == null || value2 == null)
        {
            return null;
        }
        return (((BigDecimal)value1).subtract((BigDecimal)value2));
    }

    /**
     * {@inheritDoc}
     */
    public Number mult(Number value1, Number value2)
    {
        if (value1 == null || value2 == null)
        {
            return null;
        }
        return (((BigDecimal)value1).multiply((BigDecimal)value2));
    }

    /**
     * {@inheritDoc}
     */
    public Number div(Number value1, Number value2)
    {
        if (value1 == null || value2 == null)
        {
            return null;
        }
        return (((BigDecimal)value1).divide((BigDecimal)value2, BigDecimal.ROUND_HALF_UP));
    }
    
    /**
     * {@inheritDoc}
     */
    public Number min(Number value1, Number value2)
    {
        if (value1 == null)
        {
            return value2;
        }
        if (value2 == null)
        {
            return value1;
        }
        return (((BigDecimal)value1).min((BigDecimal)value2));
    }

    /**
     * {@inheritDoc}
     */
    public Number max(Number value1, Number value2)
    {
        if (value1 == null)
        {
            return value2;
        }
        if (value2 == null)
        {
            return value1;
        }
        return (((BigDecimal)value1).max((BigDecimal)value2));
    }
}
