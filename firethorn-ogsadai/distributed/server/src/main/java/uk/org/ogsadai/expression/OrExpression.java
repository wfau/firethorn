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

import java.util.Collections;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Expression that implements a boolean OR operation.
 *
 * @author The OGSA-DAI Project Team.
 */
public class OrExpression implements BooleanExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Left hand side expression. */
    private final Expression mLHS;
    
    /** Right hand side expression. */
    private final Expression mRHS;

    /**
     * Constructor.
     * 
     * @param lhs
     *            Left hand side expression.
     * @param rhs
     *            Right hand side expression.
     */
    public OrExpression(final Expression lhs, final Expression rhs)
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
        Boolean lhs = mLHS.evaluate(tuple);
        Boolean rhs = mRHS.evaluate(tuple);
        if (lhs!=null && rhs!=null)
        {
            return lhs || rhs;
        }else if (lhs != null)
        {
            return lhs;
        }else if (rhs != null)
        {
            return rhs;
        }else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Expression getLeftExpression()
    {
        return mLHS;
    }

    /**
     * {@inheritDoc}
     */
    public Expression getRightExpression()
    {
        return mRHS;
    }
    
    /**
     * {@inheritDoc}
     */
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visitOrExpression(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(mLHS);
        sb.append(" OR ");
        sb.append(mRHS);
        sb.append(")");
        return sb.toString();
    }
}
