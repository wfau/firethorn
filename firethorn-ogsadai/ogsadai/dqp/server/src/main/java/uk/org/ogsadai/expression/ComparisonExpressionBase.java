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
import java.util.Comparator;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.relational.RelationalUtils;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Abstract base class for comparison expressions.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class ComparisonExpressionBase implements ComparisonExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Left hand operand. */
    protected Operand mLHS = null;
    
    /** Right hand operand. */
    protected Operand mRHS = null;

    /** Compares operand values. */
    protected Comparator<Object> mComparator;
    
    /** Logger. */
    private static final DAILogger LOG = DAILogger
        .getLogger(ComparisonExpressionBase.class);

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
    public final Boolean evaluate(Tuple tuple)
        throws ExpressionEvaluationException
    {
        Comparable<Object> left;
        try
        {
            left = mLHS.getValue(tuple);
        }
        catch (ExpressionEvaluationException e)
        {
            if(e.canTreatAsNULL())
            {
                left = Null.getValue();
            }
            else
            {
                throw e;
            }
        }
        
        Comparable<Object> right;
        try
        {
            right = mRHS.getValue(tuple);
        }
        catch (ExpressionEvaluationException e)
        {
            if(e.canTreatAsNULL())
            {
                right = Null.getValue();
            }
            else
            {
                throw e;
            }
        }
        
        // If null/unknown values are compared the result is null/unknown
        if (left == Null.getValue() || right == Null.getValue())
        {
            return null;
        }

        // Things may go wrong inside comparator, some of the exceptions are not
        // fatal and can be safely ignored
        Boolean result = false;
        result = localEvaluate(left, right);
        return result;
    }

    /**
     * Part of evaluation implemented by each extending operator.
     * 
     * @param left
     *            left hand side operand
     * @param right
     *            right hand side operand
     * @return evaluation result
     */
    public abstract Boolean localEvaluate(Comparable<Object> left,
        Comparable<Object> right);
    
    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata)
        throws ColumnNotFoundException, TypeMismatchException
    {
        Set<Attribute> emptySet = Collections.emptySet();
        configure(metadata, emptySet);
    }

    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata, Set<Attribute> attributes)
        throws ColumnNotFoundException, TypeMismatchException
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Tuple metadata is: " + metadata);
        }
        mLHS.configure(metadata, attributes);
        mRHS.configure(metadata, attributes);

        // Skip type casting and comparator selection for ODNULL type operands
        if (mRHS.getType() != TupleTypes._ODNULL
            && mLHS.getType() != TupleTypes._ODNULL)
        {
            /* Type casting code */
            ArithmeticExpression rhs = ((ArithmeticExpressionOperand) mRHS)
                .getExpression();
            ArithmeticExpression lhs = ((ArithmeticExpressionOperand) mLHS)
                .getExpression();

            int leftType = mLHS.getType();
            int rightType = mRHS.getType();
            int compareAsType =
                TypeConverter.getComparisonType(leftType, rightType);

            if (LOG.isDebugEnabled())
            {
                LOG.debug(TupleTypes.getTypeName(mLHS.getType()) + " op "
                    + TupleTypes.getTypeName(mRHS.getType()) + " -> "
                    + TupleTypes.getTypeName(compareAsType));
            }

            if (leftType != compareAsType)
            {
                lhs.resetType();
                lhs.setContextType(compareAsType);
            }
            if (rightType != compareAsType)
            {
                rhs.resetType();
                rhs.setContextType(compareAsType);
            }
            /* End type casting code */

            mComparator = RelationalUtils.getComparator(mLHS.getType(), mRHS
                .getType());
        }
    }
}
