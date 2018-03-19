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

import java.util.List;

import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
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
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;

/**
 * A visitor that generates SQL string for a logical expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SQLGenExpressionVisitor implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** SQL string for currently processed branch. */
    private String mCurrentString;

    /**
     * Generate SQL for boolean expression.
     * 
     * @param expression
     * @param operatorString
     */
    private void visitBoolean(BooleanExpression expression,
        String operatorString)
    {
        String leftString;
        String rightString;
        
        expression.getLeftExpression().accept(this);
        leftString = mCurrentString;
        expression.getRightExpression().accept(this);
        rightString = mCurrentString;

        mCurrentString = "(" + leftString + ") " + operatorString + " (" + rightString + ")";
    }

    /**
     * Generate SQL for atomic boolean expression.
     * 
     * @param expression
     * @param operatorString
     */
    private void visitComparison(ComparisonExpression expression,
        String operatorString)
    {
        String leftString;
        String rightString;

        SQLGenArithmeticExpressionVisitor mLeftVisitor = 
            new SQLGenArithmeticExpressionVisitor();
        SQLGenArithmeticExpressionVisitor mRightVisitor = 
            new SQLGenArithmeticExpressionVisitor();
        
        ((ArithmeticExpressionOperand) expression.getLeftOperand())
            .getExpression().accept(mLeftVisitor);
        leftString = mLeftVisitor.toString();
        
        ((ArithmeticExpressionOperand) expression.getRightOperand())
            .getExpression().accept(mRightVisitor);
        rightString = mRightVisitor.toString();

        mCurrentString = leftString + ' ' + operatorString + ' ' + rightString;
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        visitBoolean(expression, "AND");
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        visitBoolean(expression, "OR");
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        visitComparison(expression, "=");
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        visitComparison(expression, ">");
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        visitComparison(expression, ">=");
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        visitComparison(expression, "<");
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        visitComparison(expression, "<=");
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression)
    {
        visitComparison(expression, "LIKE");
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        visitComparison(expression, "<>");
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression)
    {
        expression.getChildExpression().accept(this);
        mCurrentString = "NOT " + mCurrentString;
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        SQLGenArithmeticExpressionVisitor visitor = 
            new SQLGenArithmeticExpressionVisitor();
        
        ((ArithmeticExpressionOperand) expression.getOperand())
            .getExpression().accept(visitor);

        mCurrentString = visitor.toString() + " IS NULL";
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        StringBuilder result = new StringBuilder();
        
        // Left
        SQLGenArithmeticExpressionVisitor leftVisitor = 
            new SQLGenArithmeticExpressionVisitor();
        ((ArithmeticExpressionOperand) expression.getLeftOperand())
            .getExpression().accept(leftVisitor);
        result.append(leftVisitor.toString());
        
        // Operator
        result.append(" IN (");
        
        // Right
        List<Operand> rightOperands = expression.getRightOperands();
        
        boolean firstOperand = true;
        for( Operand operand : rightOperands )
        {
            SQLGenArithmeticExpressionVisitor rightVisitor = 
                new SQLGenArithmeticExpressionVisitor();
            ((ArithmeticExpressionOperand) operand).getExpression().accept(
                rightVisitor);
            if (!firstOperand)
            {
                result.append(", ");
            }
            result.append(rightVisitor.toString());
            firstOperand = false;
        }
        result.append(")");
        
        mCurrentString = result.toString();
    }

    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return mCurrentString;
    }
}
