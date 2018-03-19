//Copyright (c) The University of Edinburgh 2009.
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

import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Clones an arithmetic expression, retaining the configuration of each
 * expression node but resetting the values.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CloneArithmeticExprVisitor implements
        ArithmeticExpressionVisitor
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Cloned expression. */
    protected ArithmeticExpression mCurrentExpression;
    
    /**
     * Clones the given expression.
     * 
     * @param expression
     *            arithmetic expression to clone
     * @return a copy of the input expression but values have been reset
     */
    public static ArithmeticExpression cloneExpression(
            ArithmeticExpression expression)
    {
        CloneArithmeticExprVisitor visitor = 
            new CloneArithmeticExprVisitor();
        return visitor.cloneArithmeticExpression(expression);
    }
    
    /**
     * Clones arithmetic expression.
     * 
     * @param expression
     *            expression to clone
     * @return cloned expression
     */
    protected ArithmeticExpression cloneArithmeticExpression(
            ArithmeticExpression expression)
    {
        CloneArithmeticExprVisitor visitor = 
            new CloneArithmeticExprVisitor();
        expression.accept(visitor);
        return visitor.getClone();
    }

    /**
     * Gets cloned expression.
     * 
     * @return cloned expression
     */
    protected ArithmeticExpression getClone()
    {
        return mCurrentExpression;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitConstant(Constant expression)
    {
        mCurrentExpression = 
            new Constant(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitDiv(Div expression)
    {
        ArithmeticExpression[] children = expression.getChildren();
        mCurrentExpression = 
            new Div(
                    cloneArithmeticExpression(children[0]), 
                    cloneArithmeticExpression(children[1]), 
                    expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        try
        {
            mCurrentExpression = 
                new ExecutableFunctionExpression(function);
        }
        catch (CloneNotSupportedException e)
        {
            // TODO construct the expression again from scratch?
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitMinus(Minus expression)
    {
        ArithmeticExpression[] children = expression.getChildren();
        mCurrentExpression = 
            new Minus(
                    cloneArithmeticExpression(children[0]), 
                    cloneArithmeticExpression(children[1]), 
                    expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitMult(Mult expression)
    {
        ArithmeticExpression[] children = expression.getChildren();
        mCurrentExpression = 
            new Mult(
                    cloneArithmeticExpression(children[0]), 
                    cloneArithmeticExpression(children[1]), 
                    expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitPlus(Plus expression)
    {
        ArithmeticExpression[] children = expression.getChildren();
        mCurrentExpression = 
            new Plus(
                    cloneArithmeticExpression(children[0]), 
                    cloneArithmeticExpression(children[1]), 
                    expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        mCurrentExpression = new TableColumn(tableColumn);
    }

    /**
     * {@inheritDoc}
     */
    public void visitStar(Star expression)
    {
        mCurrentExpression = new Star();
    }

}
