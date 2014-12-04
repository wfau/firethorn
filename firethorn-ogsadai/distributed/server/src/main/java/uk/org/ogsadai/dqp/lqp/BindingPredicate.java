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

package uk.org.ogsadai.dqp.lqp;

import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;

/**
 * Predicate expressing a binding pattern.
 * 
 * @author The OGSA-DAI Project Team
 */
public class BindingPredicate extends Predicate
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** A list of attributes to be bound. */
    private Attribute mAttributeToBeBound;

    /**
     * Constructs new bound predicate.
     * 
     * @param pred
     *            wrapped predicate
     * @param attributeToBeBound
     *            predicate attributes that need to be bound
     */
    public BindingPredicate(Predicate pred, Attribute attributeToBeBound)
    {
        mPredicateExpression = pred.getExpression();
        mAttributeToBeBound = attributeToBeBound;
    }

    /**
     * Constructor.
     * 
     * @param expression
     *            predicate expression
     * @param attributeToBeBound
     *            predicate attributes that need to be bound
     */
    public BindingPredicate(Expression expression, Attribute attributeToBeBound)
    {
        mPredicateExpression = expression;
        mAttributeToBeBound = attributeToBeBound;
    }

    /**
     * Get attribute to be bound.
     * 
     * @return attribute
     */
    public Attribute getBoundAttribute()
    {
        return mAttributeToBeBound;
    }

    /**
     * Get the expression providing value for the bound attribute.
     * 
     * @return value providing expression
     */
    public ArithmeticExpression getValueExpression()
    {
        return ((ArithmeticExpressionOperand) ((ComparisonExpression) mPredicateExpression).getRightOperand()).getExpression();
    }

    /**
     * {@inheritDoc}
     */
    public BindingPredicate getClone()
    {
        return new BindingPredicate(
            ExpressionUtils.getClone(mPredicateExpression), mAttributeToBeBound);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof BindingPredicate)
        {
            BindingPredicate p = (BindingPredicate) obj;

            return mPredicateExpression.toString().equals(
                p.mPredicateExpression.toString());
        }
        else
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return ExpressionUtils.generateSQL(mPredicateExpression);
    }

}
