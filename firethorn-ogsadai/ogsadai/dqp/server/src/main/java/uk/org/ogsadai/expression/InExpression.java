// Copyright (c) The University of Edinburgh, 2011.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Expression that implements a IN operation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class InExpression implements Expression
{
    /** Left hand side operand. */
    private final Operand mLHS;

    /** Right hand side operands. */
    private final List<Operand> mRHS;
    
    private final List<EqualExpression> mEqualsExpressions; 

    /**
     * Constructor.
     * 
     * @param lhs
     *            Left hand side operand.
     * @param rhs
     *            Right hand side operands.
     */
    public InExpression(final Operand lhs, final List<Operand> rhs)
    {
        mLHS = lhs;
        mRHS = rhs;
        mEqualsExpressions = new ArrayList<EqualExpression>(rhs.size());
        for( Operand operand : mRHS)
        {
            mEqualsExpressions.add(new EqualExpression(lhs,operand));
        }
    }
    
    @Override
    public void configure(TupleMetadata metadata)
        throws ColumnNotFoundException, TypeMismatchException
    {
        configure(metadata, Collections.<Attribute>emptySet());
    }

    @Override
    public void configure(TupleMetadata metadata,
        Set<Attribute> correlatedAttributes) throws ColumnNotFoundException,
        TypeMismatchException
    {
        for (Expression expr : mEqualsExpressions)
        {
            expr.configure(metadata, correlatedAttributes);
        }
    }

    @Override
    public Boolean evaluate(Tuple tuple) throws ExpressionEvaluationException
    {
        for (Expression expr : mEqualsExpressions)
        {
            if (expr.evaluate(tuple)) return true;
        }
        return false;
    }

    /**
     * Gets left operand.
     * 
     * @return Left operand.
     */
    public Operand getLeftOperand()
    {
        return mLHS;
    }
    
    /**
     * Gets right operands.
     * 
     * @return Gets right operand.
     */
    public List<Operand> getRightOperands()
    {
        return mRHS;
    }
    
    @Override
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visitInExpression(this);
    }
    
    @Override
    public String toString() 
    {
        return mLHS + " IN " + mRHS;
    }
}
