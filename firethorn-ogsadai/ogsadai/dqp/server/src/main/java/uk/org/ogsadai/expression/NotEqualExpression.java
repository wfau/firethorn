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

package uk.org.ogsadai.expression;

/**
 * Expression that implements a not equal to comparison.
 *
 * @author The OGSA-DAI Project Team.
 */
public class NotEqualExpression extends ComparisonExpressionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /**
     * Constructor.
     * 
     * @param lhs
     *            Left hand side operand.
     * @param rhs
     *            Right hand side operand.
     */
    public NotEqualExpression(final Operand lhs, final Operand rhs)
    {
        mLHS = lhs;
        mRHS = rhs;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean localEvaluate(Comparable<Object> left,
        Comparable<Object> right)
    {
        return mComparator.compare(left, right) != 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visitNotEqualExpression(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(mLHS);
        sb.append(" <> ");
        sb.append(mRHS);
        sb.append(")");
        return sb.toString();
    }
}
