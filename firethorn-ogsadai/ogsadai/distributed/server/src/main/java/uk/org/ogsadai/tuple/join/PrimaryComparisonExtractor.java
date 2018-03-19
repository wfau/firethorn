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


package uk.org.ogsadai.tuple.join;

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
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Class to extract the primary expression from an expression.  The
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
public class PrimaryComparisonExtractor implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** The result to return to the caller. */
    private ComparisonExpression mResult;
    /** Metadata of the left input. */
    private TupleMetadata mMetadataLeft;
    /** Metadata of the right input. */
    private TupleMetadata mMetadataRight;
    /** Stored operand expression of the primary expression - may be null. */
    private ArithmeticExpression mLeftOperand;
    /** Streamed operand expression of the primary expression - may be null. */
    private ArithmeticExpression mRightOperand;
    
    /**
     * Constructor.
     */
    public PrimaryComparisonExtractor()
    {
        mResult = null;
    }

    /**
     * Returns the arithmetic expression that corresponds to the left input
     * metadata.
     * 
     * @return expression
     */
    public ArithmeticExpression getLeftExpression()
    {
        return mLeftOperand;
    }
    
    /**
     * Returns the arithmetic expression that corresponds to the right input
     * metadata.
     * 
     * @return expression
     */
    public ArithmeticExpression getRightExpression()
    {
        return mRightOperand;
    }

    /**
     * Extracts the primary expression from the given expression.
     * 
     * @param expression
     *            expression from which to extract the primary expression.
     * @param metadataLeft
     *            tuple metadata
     * @param metadataRight
     *            tuple metadata
     * @return the primary expression or <code>null</code> if there is no
     *         primary expression.
     */
    public ComparisonExpression extract(
            Expression expression, 
            TupleMetadata metadataLeft,
            TupleMetadata metadataRight)
    {
        mMetadataLeft = metadataLeft;
        mMetadataRight = metadataRight;
        expression.accept(this);
        return mResult;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        expression.getLeftExpression().accept(this);
        if (mResult == null)
        {
            expression.getRightExpression().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        mResult = null;
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
        mResult = null;
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
        mResult = null;
    }

    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        mResult = null;
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
    private ComparisonExpression visitComparisonExpression(ComparisonExpression expression)
    {
        if (validateAttributes(expression.getLeftOperand(), mMetadataLeft)
             && validateAttributes(expression.getRightOperand(), mMetadataRight))
        {
            ArithmeticExpression e = 
                ((ArithmeticExpressionOperand)expression.getLeftOperand()).getExpression();
            mLeftOperand = CloneArithmeticExprVisitor.cloneExpression(e);
            e = ((ArithmeticExpressionOperand)expression.getRightOperand()).getExpression();
            mRightOperand = CloneArithmeticExprVisitor.cloneExpression(e);
            return expression;
        }
        if (validateAttributes(expression.getRightOperand(), mMetadataLeft)
                && validateAttributes(expression.getLeftOperand(), mMetadataRight))
        {
            // swap operands
            ArithmeticExpression e = 
                ((ArithmeticExpressionOperand)expression.getRightOperand()).getExpression();
            mLeftOperand = CloneArithmeticExprVisitor.cloneExpression(e);
            e = ((ArithmeticExpressionOperand)expression.getLeftOperand()).getExpression();
            mRightOperand = CloneArithmeticExprVisitor.cloneExpression(e);
            return expression;
        }
        return null;
    }
    
    /**
     * Checks that the attributes used in the arithmetic expression are present
     * in the metadata.
     * 
     * @param operand
     *            arithmetic expression
     * @param metadata
     *            metadata
     * @return <code>true</code> if the attributes used in the operand match
     *         those in the metadata
     */
    private boolean validateAttributes(Operand operand, TupleMetadata metadata)
    {
        AttributeExtractor validator = new AttributeExtractor();
        return validator.containsAttributes(
                ((ArithmeticExpressionOperand)operand).getExpression(), 
                metadata);
    }

}
