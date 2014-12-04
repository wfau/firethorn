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

package uk.org.ogsadai.expression.arithmetic;

import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;

/**
 * Utility methods for dealing with arithmetic expressions.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ArithmeticExpressionUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Returns SQL compatible representation of an arithmetic expression.
     * 
     * @param expr
     *            arithmetic expression
     * @return SQL string
     */
    public static String getSQL(ArithmeticExpression expr)
    {
        SQLGenArithmeticExpressionVisitor v = 
            new SQLGenArithmeticExpressionVisitor();

        expr.accept(v);

        return v.getSQLString();
    }

    /**
     * Extracts attributes used in an expression.
     * 
     * @param expression
     *            arithmetic expression
     * @return a set of attributes
     */
    public static Set<Attribute> getAttributes(ArithmeticExpression expression)
    {
        AttrExtrArithmeticExprVisitor v = new AttrExtrArithmeticExprVisitor();
        expression.accept(v);

        return v.getAttributes();
    }
}
