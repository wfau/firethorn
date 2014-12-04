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

package uk.org.ogsadai.expression.arithmetic.visitors;

import java.util.List;

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
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * A visitor that generates SQL string for an arithmetic expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SQLGenArithmeticExpressionVisitor implements
    ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Current string for a branch */
    private String mCurrentString;

    /**
     * Generate SQL for a binary expression.
     * 
     * @param expression
     * @param operatorString
     */
    private void visitBinary(BinaryExpression expression, String operatorString)
    {
        String leftString;
        String rightString;

        expression.getLeftExpression().accept(this);
        leftString = mCurrentString;
        expression.getRightExpression().accept(this);
        rightString = mCurrentString;

        mCurrentString = "(" + leftString + " " + operatorString + " "
            + rightString + ")";
    }

    /**
     * {@inheritDoc}
     */
    public void visitConstant(Constant expression)
    {
        switch (expression.getType())
        {
            case TupleTypes._BIGDECIMAL:
            case TupleTypes._BOOLEAN:
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
            case TupleTypes._SHORT:
            case TupleTypes._ODNULL:
                mCurrentString = expression.toString();
                break;
            default:
                mCurrentString = "'" + expression.toString() + "'";
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitDiv(Div expression)
    {
        visitBinary(expression, "/");
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(function.getExecutable().getName());
        sb.append('(');

        List<ArithmeticExpression> parameterList = function.getExecutable()
            .getParameters();

        for (int i = 0; i < parameterList.size(); i++)
        {
            ArithmeticExpression expr = parameterList.get(i);
            SQLGenArithmeticExpressionVisitor v = new SQLGenArithmeticExpressionVisitor();
            expr.accept(v);

            sb.append(v.toString());
            if (i < parameterList.size() - 1)
            {
                sb.append(',');
            }
        }
        sb.append(')');

        mCurrentString = sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void visitMinus(Minus expression)
    {
        visitBinary(expression, "-");
    }

    /**
     * {@inheritDoc}
     */
    public void visitMult(Mult expression)
    {
        visitBinary(expression, "*");
    }

    /**
     * {@inheritDoc}
     */
    public void visitPlus(Plus expression)
    {
        visitBinary(expression, "+");
    }

    /**
     * {@inheritDoc}
     */
    public void visitStar(Star expression)
    {
        mCurrentString = "*";
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        if (tableColumn.getSource() != null)
        {
            mCurrentString = tableColumn.getSource() + "."
                + tableColumn.getName();
        }
        else
        {
            mCurrentString = tableColumn.getName();
        }
    }
    
    /**
     * Returns generated SQL string.
     * 
     * @return SQL string
     */
    public String getSQLString()
    {
        return mCurrentString;
    }
    
    /**
     * Resets visitor state.
     */
    public void reset()
    {
        mCurrentString = null;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return mCurrentString;
    }
    
}
