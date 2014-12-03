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

package uk.org.ogsadai.expression;

import java.util.Collections;
import java.util.Set;

import org.w3c.dom.Attr;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * An operand that is a field of a tuple.  When the operated is evaluated
 * the correspond value from the tuple will be returned.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ArithmeticExpressionOperand implements Operand
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Arithmetic expression in this operand. **/
    private ArithmeticExpression mExpression;

    /**
     * Constructor.
     * 
     * @param expression
     *            the arithmetic expression
     */
    public ArithmeticExpressionOperand(ArithmeticExpression expression)
    {
        mExpression = expression;
    }

    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata) 
        throws TypeMismatchException
    {
        Set<Attribute> emptySet = Collections.emptySet();
        configure(metadata, emptySet);
    }

    /**
     * {@inheritDoc}
     */
    public void configure(
        TupleMetadata metadata, Set<Attribute> correlatedAttributes) 
        throws TypeMismatchException
    {
        mExpression.configure(metadata, correlatedAttributes);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Comparable<Object> getValue(Tuple tuple)
        throws ExpressionEvaluationException
    {
        mExpression.evaluate(tuple);
        Object result = mExpression.getResult();
                
        if (result instanceof Comparable)
        {
            return (Comparable<Object>)result;
        }
        else if (result == Null.getValue())
        {
            return (Comparable<Object>) Null.getValue();
        }
        else
        {
            // TODO do something sensible here 
            throw new IllegalArgumentException(
                    "Not a comparable type: " + result.getClass().getName());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int getType()
    {
        return mExpression.getMetadata().getType();
    }
    
    /**
     * Returns the arithmetic expression.
     * 
     * @return arithmetic expression
     */
    public ArithmeticExpression getExpression()
    {
        return mExpression;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return mExpression.toString();
    }

}
