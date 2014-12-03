// Copyright (c) The University of Edinburgh, 2008-2012.
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Expression that implements a LIKE operation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class LikeExpression implements ComparisonExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";

    /** Left hand side operand. */
    private final Operand mLHS;

    /** Right hand side operand. */
    private final Operand mRHS;

    /**
     * Constructor.
     * 
     * @param lhs
     *            Left hand side operand.
     * @param rhs
     *            Right hand side operand.
     */
    public LikeExpression(final Operand lhs, final Operand rhs)
    {
        mLHS = lhs;
        mRHS = rhs;
    }

    /**
     * {@inheritDoc}
     */
    public void configure(final TupleMetadata metadata) 
        throws ColumnNotFoundException, 
               TypeMismatchException
    {
        Set<Attribute> emptySet = Collections.emptySet();
        configure(metadata, emptySet);
    }
    
    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata, Set<Attribute> attributes)
        throws ColumnNotFoundException, 
               TypeMismatchException
    {
        mLHS.configure(metadata, attributes);
        mRHS.configure(metadata, attributes);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean evaluate(final Tuple tuple)
        throws ExpressionEvaluationException
    {
        Object left = mLHS.getValue(tuple);
        Object right = mRHS.getValue(tuple);
        if (left == Null.getValue() || right == Null.getValue())
        {
            return null;
        }
        return matches(left.toString(), right.toString());
    }

    /**
     * Returns <code>true</code> if the input string matches the pattern. SQL
     * pattern matching is supported: <code>_</code> matches a single character,
     * and <code>%</code> matches any number of characters. For example,
     * <code>A%</code> matches <code>ABCDE</code> or <code>A</code> whereas
     * <code>A_</code> matches <code>AB</code> but not <code>ABCDE</code> or
     * <code>A</code>.
     * 
     * @param input
     *            string to match against pattern
     * @param pattern
     *            the pattern to match
     * @return <code>true</code> if the input string matches the pattern
     */
    protected boolean matches(String input, String pattern)
    {
        String p = pattern;
        // escape special characters by adding a backslash in front of them
        List<String> special = Arrays.asList("\\^", "\\\\", "\\+", "\\^",
            "\\.", "\\$", "\\?", "\\(", "\\)", "\\[", "\\]", "\\{", "\\}",
            "\\|");
        for (String regex : special)
        {
            p = p.replaceAll(regex, "\\\\" + regex);
        }
        // treat * separately -- why ??
        p = p.replaceAll("\\\u002a", "\\\\*");
        p = p.replaceAll("%", ".*").replaceAll("_", ".");

        return input.matches(p);
    }

    /**
     * {@inheritDoc}
     */
    public Operand getLeftOperand()
    {
        return mLHS;
    }

    /**
     * {@inheritDoc}
     */
    public Operand getRightOperand()
    {
        return mRHS;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visitLikeExpression(this);
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(mLHS);
        sb.append(" < ");
        sb.append(mRHS);
        sb.append(")");
        return sb.toString();
    }
}
