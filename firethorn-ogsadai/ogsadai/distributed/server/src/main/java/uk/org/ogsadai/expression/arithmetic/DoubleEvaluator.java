//Copyright (c) The University of Edinburgh 2007.
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


package uk.org.ogsadai.expression.arithmetic;

/**
 * Evaluates arithmetic expressions with Double operands.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DoubleEvaluator implements Evaluator
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";
    
    /**
     * {@inheritDoc}
     */
    public Number plus(Number value1, Number value2)
    {
        if (value1 == null || value2 == null)
        {
            return null;
        }
        double result = value1.doubleValue() + value2.doubleValue();
        return new Double(result);
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
        double result = value1.doubleValue() - value2.doubleValue();
        return new Double(result);
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
        double result = value1.doubleValue() * value2.doubleValue();
        return new Double(result);
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
        double result = value1.doubleValue() / value2.doubleValue();
        return new Double(result);
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
        if (Double.compare(value1.doubleValue(), value2.doubleValue()) < 0)
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
        if (Double.compare(value1.doubleValue(), value2.doubleValue()) > 0)
        {
            return value1;
        }
        else
        {
            return value2;
        }
    }
}
