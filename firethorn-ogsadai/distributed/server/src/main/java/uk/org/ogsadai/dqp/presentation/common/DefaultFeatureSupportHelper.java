// Copyright (c) The University of Edinburgh, 2012.
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

package uk.org.ogsadai.dqp.presentation.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.common.ArithmeticExpressionSupport;
import uk.org.ogsadai.dqp.common.ExpressionSupport;
import uk.org.ogsadai.dqp.common.OperatorSupport;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.BooleanExpression;
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
 * Class used to define the features (typically of SQL) that are supported by a
 * data node. This class defaults to those features that most databases support.
 * All operators are supported as are all arithmetic expressions. The only
 * variable is which functions are supported.
 * <p>
 * By default the following functions are supported:
 * <ul>
 *   <li>COUNT</li>
 *   <li>MIN</li>
 *   <li>MAX</li>
 *   <li>AVG</li>
 *   <li>SUM</li>
 *   <li>STDDEV</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class DefaultFeatureSupportHelper
    implements ExpressionSupport, ArithmeticExpressionSupport, OperatorSupport,
    ArithmeticExpressionVisitor, ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
    "Copyright (c) The University of Edinburgh, 2011";

    /** Functions that are supported. */
    private Set<String> mSupportedFunctions;
    
    /** Used to store a result as various visitors are being applied. */
    private boolean mResult;
    
    /** The default functions that are supported. */
    private static final String[] DEFAULT_FUNCTIONS = 
        { "STDDEV", "COUNT", "AVG", "MIN", "MAX", "SUM", "DQP_REPLACE" };
    
    /**
     * Constructor.
     */
    public DefaultFeatureSupportHelper()
    {
        mSupportedFunctions = new HashSet<String>();
        mSupportedFunctions.addAll(Arrays.asList(DEFAULT_FUNCTIONS));
    }
 
    /**
     * Sets the whole list of functions that are supported. The default list
     * of functions will be replaced with this list.
     * 
     * @param functions the names of the supported functions.
     */
    public void setSupportedFunctions(List<String> functions)
    {
        mSupportedFunctions.clear();
        mSupportedFunctions.addAll(functions);
    }
    
    /**
     * Sets the functions that will be supported in addition to the default
     * functions.
     * 
     * @param functions the names of the supported functions.
     */
    public void setAdditionalSupportedFunctions(List<String> functions)
    {
        mSupportedFunctions.addAll(functions);
    }

    /**
     * Sets the functions from the default list that are not supported.
     * 
     * @param functions names of the unsupported functions.
     */
    public void setUnsupportedFunctions(List<String> functions)
    {
        mSupportedFunctions.removeAll(functions);
    }
    
    
    @Override
    public boolean supportsOperator(OperatorID id)
    {
        return true;
    }

    @Override
    public boolean supportsArithmeticExpression(ArithmeticExpression expr)
    {
        mResult = true;
        expr.accept(this);
        return mResult;
    }

    @Override
    public boolean supportsFunction(Function function)
    {
        return mSupportedFunctions.contains(function.getName());
    }

    @Override
    public boolean supportsExpression(Expression expr)
    {
        mResult = true;
        expr.accept(this);
        return mResult;
    }

    @Override
    public void visitConstant(Constant expression)
    {
    }

    @Override
    public void visitPlus(Plus expression)
    {
        processBinaryExpression(expression);
    }

    @Override
    public void visitMinus(Minus expression)
    {
        processBinaryExpression(expression);
    }

    @Override
    public void visitDiv(Div expression)
    {
        processBinaryExpression(expression);
    }

    @Override
    public void visitMult(Mult expression)
    {
        processBinaryExpression(expression);
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        if (!mSupportedFunctions.contains(function.getExecutable().getName()))
        {
            mResult = false;
        }
        else
        {
            for (ArithmeticExpression expr : function.getChildren())
            {
                if (mResult) expr.accept(this);
                else break;
            }
        }
    }

    @Override
    public void visitStar(Star expression)
    {
    }
    
    private void processBinaryExpression(BinaryExpression expression)
    {
        expression.getLeftExpression().accept(this);
        if (mResult) expression.getRightExpression().accept(this);
    }

    @Override
    public void visitAndExpression(AndExpression expression)
    {
        processBooleanExpression(expression);
    }

    @Override
    public void visitOrExpression(OrExpression expression)
    {
        processBooleanExpression(expression);
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitNotExpression(NotExpression expression)
    {
        expression.getChildExpression().accept(this);
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression)
    {
        ((ArithmeticExpressionOperand) expression.getOperand())
            .getExpression().accept(this);
    }

    @Override
    public void visitLikeExpression(LikeExpression expression)
    {
        processComparisonExpression(expression);
    }

    @Override
    public void visitInExpression(InExpression expression)
    {
        ((ArithmeticExpressionOperand) expression.getLeftOperand())
            .getExpression().accept(this);
    
        for (Operand operand : expression.getRightOperands())
        {
            if (mResult)
            {
                ((ArithmeticExpressionOperand) operand).getExpression()
                    .accept(this);
            }
            else break;
        }
    }

    private void processBooleanExpression(BooleanExpression expr)
    {
        expr.getLeftExpression().accept(this);
        if (mResult) expr.getRightExpression().accept(this);
    }

    private void processComparisonExpression(ComparisonExpression expr)
    {
        ((ArithmeticExpressionOperand) expr.getLeftOperand())
            .getExpression().accept(this);
        if (mResult)
        {
            ((ArithmeticExpressionOperand) expr.getRightOperand())
                .getExpression().accept(this);
        }
    }
}
