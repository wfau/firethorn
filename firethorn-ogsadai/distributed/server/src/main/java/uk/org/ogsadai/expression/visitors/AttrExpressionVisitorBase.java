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
import uk.org.ogsadai.expression.BooleanExpression;
import uk.org.ogsadai.expression.ComparisonExpression;
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
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.OrExpression;

/**
 * A base class for expression visitors dealing with attributes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class AttrExpressionVisitorBase implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    protected abstract void processLeftOperand(Operand operand);

    protected abstract void processRightOperand(Operand operand);
    
    /**
     * Visit comparison expression.
     * 
     * @param expression
     *            comparison expression
     */
    private void visitComparison(ComparisonExpression expression)
    {
        processLeftOperand(expression.getLeftOperand());
        processRightOperand(expression.getRightOperand());
    }

    /**
     * Visit boolean expression.
     * 
     * @param expression
     *            boolean expression
     */
    private void visitBoolean(BooleanExpression expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        visitBoolean(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        visitComparison(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression)
    {
        expression.getChildExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        visitBoolean(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        processLeftOperand(expression.getOperand());
    }

    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        processLeftOperand(expression.getLeftOperand());
        for (Operand operand : expression.getRightOperands())
        {
            processRightOperand(operand);
        }
    }
}
