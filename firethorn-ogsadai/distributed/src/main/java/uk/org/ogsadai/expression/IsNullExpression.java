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

import java.util.Collections;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Expression that implements IS NULL operation.
 *
 * @author The OGSA-DAI Project Team.
 */
public class IsNullExpression implements Expression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012";

    /** Child expression */
    private final Operand mChild;
    
    /**
     * Constructor.
     * @param child child expression
     */
    public IsNullExpression(Operand child)
    {
        mChild = child;
    }

    @Override
    public void configure(final TupleMetadata metadata) 
        throws ColumnNotFoundException, TypeMismatchException
    {
        configure(metadata, Collections.<Attribute>emptySet());
    }

    @Override
    public void configure(
        final TupleMetadata metadata, Set<Attribute> correlatedAttributes) 
        throws ColumnNotFoundException, TypeMismatchException
    {
        mChild.configure(metadata, correlatedAttributes);
    }

    @Override
    public Boolean evaluate(final Tuple tuple)
        throws ExpressionEvaluationException
    {
        return (mChild.getValue(tuple) == Null.getValue());
    }
    
    @Override
    public void accept(final ExpressionVisitor visitor)
    {
        visitor.visitIsNullExpression(this);
    }

    /**
     * Returns the operand of the expression.
     * 
     * @return operand
     */
    public Operand getOperand()
    {
        return mChild;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(mChild);
        sb.append(" IS NULL");
        return sb.toString();
    }
}
