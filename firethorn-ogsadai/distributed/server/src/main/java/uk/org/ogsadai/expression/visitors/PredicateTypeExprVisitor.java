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

package uk.org.ogsadai.expression.visitors;

import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.GreaterThanExpression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.InExpression;
import uk.org.ogsadai.expression.IsNullExpression;
import uk.org.ogsadai.expression.LessThanExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.LikeExpression;
import uk.org.ogsadai.expression.NotEqualExpression;
import uk.org.ogsadai.expression.NotExpression;
import uk.org.ogsadai.expression.OrExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor.OperandType;

/**
 * An expression visitor that analyses the expression and decides on its
 * predicate type.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PredicateTypeExprVisitor implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Predicate type enumeration.
     * 
     * @author The OGSA-DAI Project Team.
     */
    public enum PredicateType
    {
        /** For attribute = constant. */
        EQ_ATTR_CONST,
        /** For f(attr*) = constant. Note that attr1+attr2 is f(attr*). */
        EQ_FUNC_CONST,
        /** For f(attr*) = attr. */
        EQ_FUNC_ATTR,
        /** For attr = attr. */
        EQ_ATTR_ATTR,
        /** For X [LT, GT, NEQ, ...] Y. */
        NON_EQ,
        /** For not atomic. */
        COMPOSITE
    };

    /** Predicate type. */
    private PredicateType mPredicateType;

    /**
     * Gets predicate type.
     * 
     * @return predicate type
     */
    public PredicateType getPredicateType()
    {
        return mPredicateType;
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        mPredicateType = PredicateType.COMPOSITE;
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        OperandTypeArithmeticExprVisitor leftVisitor = new OperandTypeArithmeticExprVisitor();
        OperandTypeArithmeticExprVisitor rightVisitor = new OperandTypeArithmeticExprVisitor();
        
        ((ArithmeticExpressionOperand) expression.getLeftOperand())
            .getExpression().accept(leftVisitor);
        ((ArithmeticExpressionOperand) expression.getRightOperand())
            .getExpression().accept(rightVisitor);
        
        OperandType leftType = leftVisitor.getOperandType();
        OperandType rightType = rightVisitor.getOperandType();
        
        if ((leftType == OperandType.ATTR && rightType == OperandType.CONST)
            || (rightType == OperandType.ATTR && leftType == OperandType.CONST))
        {
            mPredicateType = PredicateType.EQ_ATTR_CONST;
        }
        else if ((leftType == OperandType.FUNC && rightType == OperandType.CONST)
            || (rightType == OperandType.FUNC && leftType == OperandType.CONST))
        {
            mPredicateType = PredicateType.EQ_FUNC_CONST;
        }
        else if ((leftType == OperandType.ATTR && rightType == OperandType.FUNC)
            || (rightType == OperandType.ATTR && leftType == OperandType.FUNC))
        {
            mPredicateType = PredicateType.EQ_FUNC_ATTR;
        }
        else if ((leftType == OperandType.ATTR && rightType == OperandType.ATTR)
            || (rightType == OperandType.ATTR && leftType == OperandType.ATTR))
        {
            mPredicateType = PredicateType.EQ_ATTR_ATTR;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression)
    {
        mPredicateType = PredicateType.NON_EQ;
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        mPredicateType = PredicateType.COMPOSITE;
    }
}
