// Copyright (c) The University of Edinburgh, 2011.
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

import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.EqualExpression;
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
import uk.org.ogsadai.expression.ValidatingExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionValidatingVisitor;

/**
 * Validates whether the given expression is an equals expression. If suitable
 * validating visitors are provided it also validates the operands of the
 * equals.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class EqualsValidatingExpressionVisitor 
    implements ValidatingExpressionVisitor
{
    /** Is the expression a valid equals? */
    private boolean mResult = false;

    /** Must we validate the operands? */
    private boolean mValidateOperands = false;

    /** Optional visitors used to validate the operands. */
    private ArithmeticExpressionValidatingVisitor[] mArithmeticExpressionVistors
        = new ArithmeticExpressionValidatingVisitor[2];
    
    /** The visitor that validated the LHS operand. */
    private ArithmeticExpressionValidatingVisitor mLHSVisitor;
    
    /** The visitor that validated the RHS operand. */
    private ArithmeticExpressionValidatingVisitor mRHSVisitor;
    

    /**
     * Constructor. The operands will not be validated.
     */
    public EqualsValidatingExpressionVisitor()
    {
    }
    
    /**
     * Constructor. The operands will be validated according to the given 
     * validation visitors. The expression will only validate one operand
     * validates according to one of these visitors and the order operand
     * validates according to the other.
     * 
     * @param aevv1 validating visitor to validate one operand
     * @param aevv2 validating visitor to validate one operand
     */
    public EqualsValidatingExpressionVisitor(
        ArithmeticExpressionValidatingVisitor aevv1,
        ArithmeticExpressionValidatingVisitor aevv2)
    {
        mValidateOperands = true;
        mArithmeticExpressionVistors[0] = aevv1;
        mArithmeticExpressionVistors[1] = aevv2;
    }
    
    public boolean isValid()
    {
        return mResult;       
    }
    
    /**
     * If <code>isValid</code> return <tt>true</tt> then this method will 
     * return the arithmetic visitor that validated for the left hand side of 
     * the equals expression.
     *  
     * @return the visitor that validated the left hand side of the equals 
     *         expression.
     */
    public ArithmeticExpressionValidatingVisitor getLHSVistor()
    {
        return mLHSVisitor;
    }
    
    /**
     * If <code>isValid</code> return <tt>true</tt> then this method will 
     * return the arithmetic visitor that validated for the right hand side of 
     * the equals expression.
     *  
     * @return the visitor that validated the right hand side of the equals 
     *         expression.
     */
    public ArithmeticExpressionValidatingVisitor getRHSVistor()
    {
        return mRHSVisitor;
    }

    @Override
    public void visitAndExpression(AndExpression expression)
    {
    }

    @Override
    public void visitOrExpression(OrExpression expression)
    {
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression)
    {
    }

    @Override
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
    }

    @Override
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        if (!mValidateOperands) 
        {
            mResult = true;
        }
        else
        {
            // Need to validate the operands
            Operand lhs = expression.getLeftOperand();
            Operand rhs = expression.getRightOperand();
        
            if (lhs instanceof ArithmeticExpressionOperand &&
                rhs instanceof ArithmeticExpressionOperand)
            {
                ArithmeticExpression[] exprs = new ArithmeticExpression[2];
            
                exprs[0] = ((ArithmeticExpressionOperand) lhs).getExpression();
                exprs[1] = ((ArithmeticExpressionOperand) rhs).getExpression();

                // One side must be true and the other side must be a sweep
                // expression
                for (int i=0; i<2; ++i)
                {
                    exprs[0].accept(mArithmeticExpressionVistors[i]);
                    exprs[1].accept(mArithmeticExpressionVistors[1-i]);
                    
                    if (mArithmeticExpressionVistors[0].isValid() &&
                        mArithmeticExpressionVistors[1].isValid())
                    {
                        mResult = true;
                        
                        mLHSVisitor = mArithmeticExpressionVistors[i];
                        mRHSVisitor = mArithmeticExpressionVistors[1-i];
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
    }

    @Override
    public void visitNotExpression(NotExpression expression)
    {
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression)
    {
    }

    @Override
    public void visitLikeExpression(LikeExpression expression)
    {
    }

    @Override
    public void visitInExpression(InExpression expression)
    {
    }
}
