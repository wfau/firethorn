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

package uk.org.ogsadai.dqp.lqp.operators;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;

/**
 * Represents a project expression that is an arithmetic expression. 
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ArithmeticExpressionProjectExpression implements ProjectExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2012";

    /** Arithmetic expression. */
    private ArithmeticExpression mExpression;
    /** Alias for the expression. */
    private String mAlias;
    /** The heading attribute for this expression. */
    private Attribute mHeading;
    
    /**
     * Constructor.
     * 
     * @param expr     arithmetic expression
     * @param alias    alias 
     * @param heading  heading attribute for this expression
     */
    public ArithmeticExpressionProjectExpression(
        ArithmeticExpression expr, String alias, Attribute heading)
    {
        mExpression =  expr;
        mAlias = alias;
        mHeading = heading;
    }
    
    /**
     * Gets the arithmetic expression.
     * 
     * @return arithmetic expression.
     */
    public ArithmeticExpression getExpression()
    {
        return mExpression;
    }
    
    /**
     * Gets the alias.
     * 
     * @return alias
     */
    public String getAlias()
    {
        return mAlias;
    }
    
    /**
     * Gets the heading.
     * 
     * @return heading attribute.
     */
    public Attribute getHeading()
    {
        return mHeading;
    }
    
    public String toString()
    {
        return "[ " + mExpression + ", " + mAlias + ", " + mHeading + "]";
    }
}
