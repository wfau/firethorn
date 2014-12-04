//Copyright (c) The University of Edinburgh 2007.
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

package uk.org.ogsadai.expression.arithmetic;

import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;

/**
 * Binary expression: div. Division.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class Div extends BinaryExpression 
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";
    
    /**
     * Constructor.
     * 
     * @param child1
     *            left hand side operand
     * @param child2
     *            right hand side operand
     */
    public Div(ArithmeticExpression child1, ArithmeticExpression child2) 
    {
        super(child1, child2);
    }

    /**
     * Copy constructor.
     * 
     * @param child1
     *            left hand side operand
     * @param child2
     *            right hand side operand
     * @param div
     *            copied div
     */
    public Div(
            ArithmeticExpression child1, 
            ArithmeticExpression child2, 
            Div div)
    {
        super(child1, child2);
        mType = div.mType;
        mEval = div.mEval;
    }

    /**
     * {@inheritDoc}
     */
    public Number localEvaluate(Number value1, Number value2)
    {
        return mEval.div(value1, value2);
    }

    /**
     * {@inheritDoc}
     */
    protected void printOperator(StringBuffer buf)
    {
        buf.append("/");
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor)
    {
        visitor.visitDiv(this);
    }

    
}
