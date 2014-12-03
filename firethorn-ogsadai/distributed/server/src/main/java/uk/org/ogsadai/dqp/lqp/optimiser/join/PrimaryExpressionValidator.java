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


package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
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
 * Class to validate that there is a primary expression in an expression.  The
 * primary expression is left most comparison expression that must evaluate to
 * true in order for the whole expression to evaluate to true.
 * <p>
 * Some examples:
 * <ul>
 *   <li> "(a &lt; b)", primary expression is "(a &lt; b)"</li>
 *   <li> "(a &lt; b) AND (c &gt; d)", primary expression is "(a &lt; b)"</li>
 *   <li> "((a &lt; b) OR (c &gt; d)) AND (e &lt; f)", primary expression is 
 *        "(e &lt; f)"</li>
 *   <li> "(a &lt; b) OR (c &gt; d)" has no primary expression</li>
 * </ul>
 * <p>
 * This class makes use of the Visitor pattern.  See the Design Patterns book
 * for more details of this pattern.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PrimaryExpressionValidator implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** The result to return to the caller. */
    private boolean mResult;
    /** Metadata of the left input. */
    private List<Attribute> mLeftAttributes;
    /** Metadata of the right input. */
    private List<Attribute> mRightAttributes;
    
    /**
     * Constructor.
     */
    public PrimaryExpressionValidator()
    {
        mResult = false;
    }

    /**
     * Extracts the primary expression from the given expression.
     * 
     * @param expression
     *            expression from which to extract the primary expression.
     * @param attributesLeft
     *            attributes from left child heading
     * @param attributesRight
     *            attributes from right child heading
     * @return the primary expression or <code>null</code> if there is no
     *         primary expression.
     */
    public boolean validate(
            Expression expression, 
            List<Attribute> attributesLeft,
            List<Attribute> attributesRight)
    {
        mLeftAttributes = attributesLeft;
        mRightAttributes = attributesRight;
        expression.accept(this);
        return mResult;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        expression.getLeftExpression().accept(this);
        if (!mResult)
        {
            expression.getRightExpression().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        mResult = false;
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        mResult = visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        mResult = false;
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression)
    {
        expression.accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression)
    {
        mResult = false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        mResult = false;
    }
    

    /**
     * Validates that all attributes used in the expression exist in the
     * metadata and assigns left and right operands.
     * 
     * @param expression
     *            expression to visit
     * @return the expression or <code>null</code> if the validation of
     *         attributes failed
     */
    private boolean visitComparisonExpression(ComparisonExpression expression)
    {
        return ((validateAttributes(expression.getLeftOperand(), mLeftAttributes)
             && validateAttributes(expression.getRightOperand(), mRightAttributes))
             || (validateAttributes(expression.getRightOperand(), mLeftAttributes)
             && validateAttributes(expression.getLeftOperand(), mRightAttributes)));
    }
    
    /**
     * Checks that all attributes used in the arithmetic expression are present
     * in the attribute list.
     * 
     * @param operand
     *            arithmetic expression
     * @param attributes
     *            attributes to validate
     * @return <code>true</code> if the attributes used in the operand match
     *         those in the metadata
     */
    private boolean validateAttributes(Operand operand, List<Attribute> attributes)
    {
        return AttributeExtractor.containsAttributes(
                ((ArithmeticExpressionOperand)operand).getExpression(),
                attributes);
    }

}
