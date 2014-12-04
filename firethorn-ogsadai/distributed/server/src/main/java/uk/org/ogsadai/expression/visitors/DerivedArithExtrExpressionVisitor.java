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
 * Visitor to discover if an arithmetic expression is derived or not. A 
 * derived arithmetic expression is one that contains functions or mathematical
 * operations.  An non-derived arithmetic expression is just a table column
 * name.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DerivedArithExtrExpressionVisitor 
    implements ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    private boolean mIsDerived = true;

    /**
     * Gets whether the expression is derived or not. This method should be
     * called after the arithmetic expression has accepted the visitor.
     * 
     * @return <tt>true</tt> if the expression is derived, <tt>false</tt>
     *         otherwise.
     */
    public boolean isDerived()
    {
        return mIsDerived;
    }

    @Override
    public void visitConstant(Constant expression)
    {
    }

    @Override
    public void visitPlus(Plus expression)
    {
    }

    @Override
    public void visitMinus(Minus expression)
    {
    }

    @Override
    public void visitDiv(Div expression)
    {
    }

    @Override
    public void visitMult(Mult expression)
    {
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        mIsDerived = false;
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
    }

    @Override
    public void visitStar(Star expression)
    {
    }
}
