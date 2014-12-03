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

package uk.org.ogsadai.expression.arithmetic.visitors;

import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.BinaryExpression;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;

/**
 * A base class for arithmetic expression visitors dealing with attributes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class AttrArithmeticExprVisitorBase implements
    ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Visit binary expression.
     * 
     * @param expression
     *            binary expression
     */
    protected void visitBinary(BinaryExpression expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitConstant(Constant expression)
    {
        // NOOP
    }

    /**
     * {@inheritDoc}
     */
    public void visitDiv(Div expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitMinus(Minus expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitMult(Mult expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitPlus(Plus expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitStar(Star expression)
    {
        // NOOP
    }

}
