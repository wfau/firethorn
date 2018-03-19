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
package uk.org.ogsadai.dqp.presentation.common;

import java.util.LinkedList;
import java.util.List;

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
 * data node, by default no features are supported. 
 * 
 * @author The OGSA-DAI Project Team
 */
public class DenyByDefaultFeatureSupportHelper 
    implements ExpressionSupport, ArithmeticExpressionSupport, OperatorSupport,
               ArithmeticExpressionVisitor, ExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";
    
    /** String used to specific that + is supported. */
    public String PLUS = "+";
    /** String used to specific that - is supported. */
    public String MINUS = "-";
    /** String used to specific that / is supported. */
    public String DIVIDE = "/";
    /** String used to specific that * is supported. */
    public String MULTIPLY = "*";
    /** String used to specific that AND is supported. */
    public String AND = "AND";
    /** String used to specific that OR is supported. */
    public String OR = "OR";
    /** String used to specific that NOT is supported. */
    public String NOT = "NOT";
    /** String used to specific that LIKE is supported. */
    public String LIKE = "LIKE";
    /** String used to specific that IN is supported. */
    public String IN = "IN";
    /** String used to specific that = is supported. */
    public String EQ = "=";
    /** String used to specific that <> is supported. */
    public String NEQ = "<>";
    /** String used to specific that < is supported. */
    public String LT = "<";
    /** String used to specific that > is supported. */
    public String GT = ">";
    /** String used to specific that <= is supported. */
    public String LTE = "<=";
    /** String used to specific that >= is supported. */
    public String GTE= ">=";
    /** String used to specific that IS NULL is supported. */
    public String IS_NULL = "IS NULL";
    
    /** Supported function name, e.g. SQRT, AVG, CONTAINS. */
    private List<String> mSupportedFunctions;
    
    /** Supported operators, e.g. SELECT, PROJECT, RENAME */
    private List<OperatorID> mSupportedOperators;
    
    /** Supported math operators, e.g. PLUS, MINUS, DIVIDE, MULTIPLY. */
    private List<String> mSupportedMathOperators;
    
    /** 
     * Supported Boolean expression operators, e.g. AND, OR, NOT, LIKE, IN,
     * LESS_THAN, GREATER_THAN.  
     */
    private List<String> mSupportedExpressionOperators;

    /** Result that will be returned. */
    private boolean mResult;

    public DenyByDefaultFeatureSupportHelper() 
    {
        mSupportedFunctions = new LinkedList<String>();
        mSupportedOperators = new LinkedList<OperatorID>();
        mSupportedMathOperators = new LinkedList<String>();
        mSupportedExpressionOperators = new LinkedList<String>();
    }

    /**
     * Sets the supported functions.  Any previously stored functions
     * are overwritten.
     * 
     * @param functions names of functions that are supported.
     */
    public void setSupportedFunctions(List<String> functions)
    {
        mSupportedFunctions.clear();
        mSupportedFunctions.addAll(functions);
    }

    /**
     * Sets the supported operators.  Any previously stored operators
     * are overwritten.
     * 
     * @param operators names of those operators that are supported.
     */
    public void setSupportedOperators(List<String> operators)
    {
        mSupportedOperators.clear();
        for (String id : operators)
        {
            mSupportedOperators.add(OperatorID.getInstance(id));
        }
    }

    /**
     * Sets the supported math operators. Any previously stored operators
     * are overwritten.
     * 
     * @param mathOperators
     *            names of math operators that are supported. Options are:
     *            <tt>"+"</tt>, <tt>"-"</tt>, <tt>"/"</tt>, <tt>"*"</tt>.
     */
    public void setSupportedMathOperators(List<String> mathOperators)
    {
        mSupportedMathOperators.clear();
        mSupportedMathOperators.addAll(mathOperators);
    }

    /**
     * Sets the supported boolean expression operators. Any previously
     * stored operators are overwritten.
     * 
     * @param expressionOperators
     *            names of expression operators that are not supported. Options
     *            are: <tt>AND</tt>, <tt>OR</tt>, <tt>NOT</tt>, <tt>LIKE</tt>,
     *            <tt>IN</tt>, <tt>"="</tt>, <tt>"<>"</tt>, <tt>"<"</tt>,
     *            <tt>">"</tt>, <tt>"<="</tt>,<tt>">="</tt>.
     */
    public void setSupportedExpressionOperator(
        List<String> expressionOperators)
    {
        mSupportedExpressionOperators.clear();
        mSupportedExpressionOperators.addAll(expressionOperators);
    }


    @Override
    public boolean supportsOperator(OperatorID id)
    {
        return mSupportedOperators.contains(id.toString());
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
        return (mSupportedFunctions.contains(function.getName()));
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
        processBinaryExpression(expression, PLUS);
    }
    
    @Override
    public void visitMinus(Minus expression)
    {
        processBinaryExpression(expression, MINUS);
    }

    @Override
    public void visitDiv(Div expression)
    {
        processBinaryExpression(expression, DIVIDE);
    }

    @Override
    public void visitMult(Mult expression)
    {
        processBinaryExpression(expression, MULTIPLY);
    }

    private void processBinaryExpression(BinaryExpression expression, String op)
    {
        if (!mSupportedMathOperators.contains(op))
        {
            mResult = false;
        }
        else
        {
            expression.getLeftExpression().accept(this);
            if (mResult) expression.getRightExpression().accept(this);
        }
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

    @Override
    public void visitAndExpression(AndExpression expression)
    {
        processBooleanExpression(expression, AND);
    }

    @Override
    public void visitOrExpression(OrExpression expression)
    {
        processBooleanExpression(expression, OR);
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression)
    {
        processComparisonExpression(expression, LT);
    }

    @Override
    public void visitLessThanOrEqualExpression(
        LessThanOrEqualExpression expression)
    {
        processComparisonExpression(expression, LTE);
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression)
    {
        processComparisonExpression(expression, GT);
    }

    private void processBooleanExpression(BooleanExpression expr, String operator)
    {
        if (mSupportedExpressionOperators.contains(operator))
        {
            expr.getLeftExpression().accept(this);
            if (mResult) expr.getRightExpression().accept(this);
        }
        else
        {
            mResult = false;
        }
    }

    private void processComparisonExpression(ComparisonExpression expr, String operator)
    {
        if (mSupportedExpressionOperators.contains(operator))
        {
            ((ArithmeticExpressionOperand) expr.getLeftOperand())
                    .getExpression().accept(this);
            if (mResult)
            {
                ((ArithmeticExpressionOperand) expr.getRightOperand())
                    .getExpression().accept(this);
            }
        }
        else
        {
            mResult = false;
        }
    }
    
    
    @Override
    public void visitGreaterThanOrEqualExpression(
        GreaterThanOrEqualExpression expression)
    {
        processComparisonExpression(expression, GTE);
    }

    @Override
    public void visitEqualExpression(EqualExpression expression)
    {
        processComparisonExpression(expression, EQ);
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression)
    {
        processComparisonExpression(expression, NEQ);
    }

    @Override
    public void visitNotExpression(NotExpression expression)
    {
        if (mSupportedExpressionOperators.contains(NOT))
        {
            expression.getChildExpression().accept(this);
        }
        else
        {
            mResult = false;
        }
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression)
    {
        if (mSupportedExpressionOperators.contains(IS_NULL))
        {
            ((ArithmeticExpressionOperand) expression.getOperand())
                .getExpression().accept(this);
        }
        else
        {
            mResult = false;
        }
    }

    @Override
    public void visitLikeExpression(LikeExpression expression)
    {
        processComparisonExpression(expression, LIKE);
    }

    @Override
    public void visitInExpression(InExpression expression)
    {
        if (mSupportedExpressionOperators.contains(IN))
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
        else
        {
            mResult = false;
        }
    }
}
