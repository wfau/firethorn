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
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Expression visitor extracting an operand type.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OperandTypeArithmeticExprVisitor implements
    ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Operand type enumeration.
     * 
     * @author The OGSA-DAI Project Team.
     */
    public enum OperandType
    {
        /** Attribute. */
        ATTR,
        /** Function of an attribute. */
        FUNC,
        /** Constant. */
        CONST
    }
    
    /** Type of an operand. */
    private OperandType mOperandType;

    /**
     * Returns operand type. <code>ATTR</code> if arithmetic expression is an
     * attribute, <code>FUNC</code> is we have a function call or a derived
     * value, <code>CONST</code> when we have a constant.
     * 
     * @return operand type
     */
    public OperandType getOperandType()
    {
        return mOperandType;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitConstant(Constant expression)
    {
        mOperandType = OperandType.CONST;
    }

    /**
     * {@inheritDoc}
     */
    public void visitDiv(Div expression)
    {
        visitBinaryExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        mOperandType = OperandType.FUNC;
    }

    /**
     * {@inheritDoc}
     */
    public void visitMinus(Minus expression)
    {
        visitBinaryExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitMult(Mult expression)
    {
        visitBinaryExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitPlus(Plus expression)
    {
        visitBinaryExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitStar(Star expression)
    {
        // it should not appear in this context
        mOperandType = OperandType.FUNC;
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        mOperandType = OperandType.ATTR;
    }
    
    private void visitBinaryExpression(BinaryExpression expression)
    {
        OperandTypeArithmeticExprVisitor visitorLeft = new OperandTypeArithmeticExprVisitor();
        expression.getLeftExpression().accept(visitorLeft);
        OperandTypeArithmeticExprVisitor visitorRight = new OperandTypeArithmeticExprVisitor();
        expression.getRightExpression().accept(visitorRight);
        OperandType left = visitorLeft.getOperandType();
        OperandType right = visitorRight.getOperandType();
        if (left == OperandType.CONST && right == OperandType.CONST)
        {
            mOperandType = OperandType.CONST;
        }
        else
        {
            mOperandType = OperandType.FUNC;
        }
    }

}
