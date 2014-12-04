//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.expression.arithmetic;

/**
 * Evaluates arithmetic expressions with Short operands.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ShortEvaluator implements Evaluator
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
        int result = value1.shortValue() + value2.shortValue();
        return new Integer(result);
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
        int result = value1.shortValue() - value2.shortValue();
        return new Integer(result);
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
        int result = value1.shortValue() * value2.shortValue();
        return new Integer(result);
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
        int result = value1.shortValue() / value2.shortValue();
        return new Integer(result);
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
        if (value1.shortValue() < value2.shortValue())
        {
            return value1;
        }
        else
        {
            return value2;
        }
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
        if (value1.shortValue() > value2.shortValue())
        {
            return value1;
        }
        else
        {
            return value2;
        }
    }
}
