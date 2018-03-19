// Copyright (c) The University of Edinburgh, 2010.
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

import uk.org.ogsadai.common.msgs.DAILogger;
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
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Class to extract the range expressions (from - to) from an expression.
 * <p>
 * Some examples:
 * <ul>
 *   <li> "(a &lt; b) AND (a &gt; b-5)", from expression is "b-5", to expression is "b"</li>
 *   <li> "((a+5) &lt; b) AND ((a+5) &gt; (b-5))", from expression is "b-5", to expression is "b"</li>
* </ul>
 * <p>
 * This class makes use of the Visitor pattern.  See the Design Patterns book
 * for more details of this pattern.
 *
 * @author The OGSA-DAI Project Team.
 */
public class RangeComparisonExtractor implements ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(RangeComparisonExtractor.class);

    /** First comparison expression that defines a range. */
    private ComparisonExpression mPrimary;
    /** Second comparison expression that defines a range. */
    private ComparisonExpression mSecondary;
    /** Metadata of the stored input. */
    private TupleMetadata mMetadataStored;
    /** Metadata of the streamed input. */
    private TupleMetadata mMetadataStreamed;
    /** Stored operand expression of the primary expression - may be null. */
    private ArithmeticExpression mStoredOperand;
    /** Streamed operand expression of the primary expression - may be null. */
    private ArithmeticExpression mFromOperand;
    /** Streamed operand expression of the secondary expression - may be null. */
    private ArithmeticExpression mToOperand;

    /**
     * Constructor.
     */
    public RangeComparisonExtractor()
    {
        mPrimary = null;
        mSecondary = null;
    }

    /**
     * Returns the arithmetic expression that corresponds to the left input
     * metadata.
     * 
     * @return expression
     */
    public ArithmeticExpression getStoredExpression()
    {
        return mStoredOperand;
    }
    
    /**
     * Returns the arithmetic expression that corresponds to the right input
     * metadata.
     * 
     * @return expression
     */
    public ArithmeticExpression getFromExpression()
    {
        return mFromOperand;
    }

    /**
     * Returns the arithmetic expression that corresponds to the right input
     * metadata.
     * 
     * @return expression
     */
    public ArithmeticExpression getToExpression()
    {
        return mToOperand;
    }
    
    public boolean includeTo()
    {
        return mSecondary instanceof LessThanOrEqualExpression;
    }

    public boolean includeFrom()
    {
        return mPrimary instanceof GreaterThanOrEqualExpression;
    }

    /**
     * Extracts the range expressions from the given expression.
     * 
     * @param expression
     *            expression from which to extract the primary expression.
     * @param metadataStored
     *            tuple metadata for the stored side of the join
     * @param metadataStreamed
     *            tuple metadata for the streamed side of the join
     * @return <code>true</code> if expressions that define a range have been
     *         found, <code>false</code> otherwise
     */
    public boolean extract(
            Expression expression, 
            TupleMetadata metadataStored,
            TupleMetadata metadataStreamed)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Extracting range from expression: " + expression);
        }
        mMetadataStored = metadataStored;
        mMetadataStreamed = metadataStreamed;
        expression.accept(this);
        orderExpressions();
        return (mPrimary != null && mSecondary != null);
    }
    
    private void orderExpressions()
    {
        if (mPrimary == null || mSecondary == null)
        {
            return;
        }
        if (((mSecondary instanceof GreaterThanExpression) || 
                (mSecondary instanceof GreaterThanOrEqualExpression))
                && ((mPrimary instanceof LessThanExpression) ||
                    (mPrimary instanceof LessThanOrEqualExpression)))
        {
            // swap from and to expressions
            ArithmeticExpression e = mToOperand;
            mToOperand = mFromOperand;
            mFromOperand = e;
            ComparisonExpression p = mPrimary;
            mPrimary = mSecondary;
            mSecondary = p;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression)
    {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression)
    {
        mPrimary = null;
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression)
    {
        visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression)
    {
         visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression)
    {
        visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression)
    {
        visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        visitComparisonExpression(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression)
    {
        mPrimary = null;
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
        mPrimary = null;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression)
    {
        mPrimary = null;
    }
    
    
    private void visitComparisonExpression(ComparisonExpression expression)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Visiting comparison expression: " + expression);
        }
        if (mPrimary == null)
        {
            mPrimary = findPrimary(expression);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Primary expression is: " + mPrimary);
            }
        }
        else if (mSecondary == null)
        {
            mSecondary = findSecondary(expression);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Secondary expression is: " + mSecondary);
            }
        }
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
    private ComparisonExpression findPrimary(ComparisonExpression expression)
    {
        ArithmeticExpression[] expressions = {
            ((ArithmeticExpressionOperand)
                expression.getLeftOperand()).getExpression(),
            ((ArithmeticExpressionOperand)
                expression.getRightOperand()).getExpression()
        };
        
        // If don't have any metadata then store the expression that is
        // a table column name
        if (mMetadataStored == null && mMetadataStreamed == null)
        {
            // If don't have any metadata then store the expression that is
            // a table column name
            for (int i=0; i<2; ++i)
            {
                if (expressions[i] instanceof TableColumn)
                {
                    mStoredOperand = CloneArithmeticExprVisitor.cloneExpression(
                        expressions[i]);
                    mFromOperand = CloneArithmeticExprVisitor.cloneExpression(
                        expressions[1-i]);
                }
            }
            return expression;
        }
        if (validateAttributes(expression.getLeftOperand(), mMetadataStored) &&
            validateAttributes(expression.getRightOperand(), mMetadataStreamed))
        {
            mStoredOperand = 
                CloneArithmeticExprVisitor.cloneExpression(expressions[0]);
            mFromOperand = 
                CloneArithmeticExprVisitor.cloneExpression(expressions[1]);
            return expression;
        }
        if (validateAttributes(expression.getRightOperand(), mMetadataStored) &&
            validateAttributes(expression.getLeftOperand(), mMetadataStreamed))
        {
            // swap operands
            mStoredOperand = 
                CloneArithmeticExprVisitor.cloneExpression(expressions[1]);
            mFromOperand = 
                CloneArithmeticExprVisitor.cloneExpression(expressions[0]);
            return expression;
        }
        return null;
    }
    
    private ComparisonExpression findSecondary(ComparisonExpression expression)
    {
        ArithmeticExpression left = 
            ((ArithmeticExpressionOperand)expression.getLeftOperand()).getExpression();
        ArithmeticExpression right = 
            ((ArithmeticExpressionOperand)expression.getRightOperand()).getExpression();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Looking for a secondary expression for stored operand : " + mStoredOperand);
        }

        if (equals(mStoredOperand, left))
        {
            LOG.debug("The left hand side of this expression matches the " +
            		  "stored operand");
            mToOperand = right;
            return expression;
        }
        else if (equals(mStoredOperand, right))
        {
            LOG.debug("The right hand side of this expression matches the " +
            		  "stored operand");
            mToOperand = left;
            return expression;
        }
        LOG.debug("None of this expression matches the stored operand");
        return null;
    }
    
    /**
     * Tests whether two arithmetic expressions are equal.
     * 
     * @param a
     *            arithmetic expression
     * @param b
     *            arithmetic expression
     * @return <code>true</code> if the input expressions are the same
     */
    private boolean equals(ArithmeticExpression a, ArithmeticExpression b)
    {
        if (!a.getClass().equals(b.getClass())) return false;
        if (a.getChildren().length != b.getChildren().length) return false;
        if (a.getChildren().length == 0)
        {
            return a.equals(b);
        }
        for (int i=0; i < a.getChildren().length; i++)
        {
            if (!equals(a.getChildren()[i], b.getChildren()[i])) return false;
        }
        return true;
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
