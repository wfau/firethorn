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

import java.util.Collections;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * A binary numeric operator in an arithmetic expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class BinaryExpression implements ArithmeticExpression
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";

    /** Evaluator for this operator. */
    protected Evaluator mEval;
    /** left child expression. */
    protected ArithmeticExpression mLeftChild;
    /** right child expression. */
    protected ArithmeticExpression mRightChild;
    /** Return type. */
    protected ColumnMetadata mType;
    /** Type of this table column. */
    protected ColumnMetadata mOriginalType;
    /** Context dependent type. */
    protected int mContextType = -1;
    /** Expression value. */
    private Object mValue;
    /** Expression value cast to a context type. */
    private Object mContextValue;

    /**
     * Constructs a new binary numeric operator with the given children.
     * 
     * @param child1
     *            left hand child expression
     * @param child2
     *            right hand child expression
     */
    public BinaryExpression(ArithmeticExpression child1,
        ArithmeticExpression child2)
    {
        mLeftChild = child1;
        mRightChild = child2;
    }

    /**
     * {@inheritDoc}
     */
    public void setContextType(int type)
    {
        if (type != mType.getType())
        {    
            mContextType = type;
    
            mType = new SimpleColumnMetadata(null, mContextType, 0,
                ColumnMetadata.COLUMN_NO_NULLS, 0);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void resetType()
    {
        mContextType = -1;
        mType = mOriginalType;
    }
    
    /**
     * Performs operation specific evaluation.
     * 
     * @param lhs
     *            left hand side value
     * @param rhs
     *            right hand side value
     * @return result of the evaluation
     */
    public abstract Number localEvaluate(Number lhs, Number rhs);
    
    /**
     * {@inheritDoc}
     */
    public final void evaluate(Tuple tuple) throws ExpressionEvaluationException
    {
        mLeftChild.evaluate(tuple);
        mRightChild.evaluate(tuple);
        
        Object value1 = mLeftChild.getResult();
        Object value2 = mRightChild.getResult();
        
        if (value1 == Null.getValue() || value2 == Null.getValue())
        {
            mValue = Null.getValue();
        }
        else
        {
            mValue = localEvaluate((Number)value1, (Number)value2);
        }
        
        if (mContextType != -1)
        {
            try
            {
                mContextValue =
                    TypeConverter.convertObject(mOriginalType.getType(),
                        mContextType, mValue);
            }
            catch (TypeConversionException e)
            {
                throw new ExpressionEvaluationException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final Object getResult()
    {
        if(mContextType != -1)
        {
            return mContextValue;
        }
        else
        {
            return mValue;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ArithmeticExpression[] getChildren()
    {
        return new ArithmeticExpression[] { mLeftChild, mRightChild };
    }

    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata)
        throws TypeMismatchException
    {
        Set<Attribute> emptySet = Collections.emptySet();
        configure(metadata, emptySet);
    }
    
    /**
     * {@inheritDoc}
     */
    public void configure(
        TupleMetadata metadata, 
        Set<Attribute> correlatedAttributes)
        throws TypeMismatchException
    {
        mLeftChild.configure(metadata, correlatedAttributes);
        mRightChild.configure(metadata, correlatedAttributes);
        
        ColumnMetadata colLeft = mLeftChild.getMetadata();
        ColumnMetadata colRight = mRightChild.getMetadata();

        int leftType = colLeft.getType();
        int rightType = colRight.getType();
        
        int resultType = TypeConverter.getArithmeticType(leftType, rightType);
        
        /* Type casting code */
        if (leftType != resultType)
        {
            mLeftChild.resetType();
            mLeftChild.setContextType(resultType);
        }
        if (rightType != resultType)
        {
            mRightChild.resetType();
            mRightChild.setContextType(resultType);
        }
        /* End type casting code */
        
        mType = new SimpleColumnMetadata(null, resultType, getPrecision(colLeft
            .getPrecision(), colRight.getPrecision()), isNullable(
                colLeft.isNullable(), colRight.isNullable()), 0);
        mOriginalType = mType;
        
        mEval = NumericEvaluators.getEvaluator(resultType);
    }
    

    /**
     * Returns the nullable value derived from the children.
     * 
     * @param value1
     *            left hand child
     * @param value2
     *            right hand child
     * @return nullable value
     */
    private int isNullable(int value1, int value2)
    {
        switch (value1)
        {
            case ColumnMetadata.COLUMN_NO_NULLS:
                switch (value2)
                {
                    case ColumnMetadata.COLUMN_NO_NULLS:
                        return ColumnMetadata.COLUMN_NO_NULLS;
                    case ColumnMetadata.COLUMN_NULLABLE:
                        return ColumnMetadata.COLUMN_NULLABLE;
                    case ColumnMetadata.COLUMN_NULLABLE_UNKNOWN:
                        return ColumnMetadata.COLUMN_NULLABLE_UNKNOWN;
                }
                break;
            case ColumnMetadata.COLUMN_NULLABLE:
                switch (value2)
                {
                    case ColumnMetadata.COLUMN_NO_NULLS:
                        return ColumnMetadata.COLUMN_NULLABLE;
                    case ColumnMetadata.COLUMN_NULLABLE:
                        return ColumnMetadata.COLUMN_NULLABLE;
                    case ColumnMetadata.COLUMN_NULLABLE_UNKNOWN:
                        return ColumnMetadata.COLUMN_NULLABLE_UNKNOWN;
                }
                break;
            case ColumnMetadata.COLUMN_NULLABLE_UNKNOWN:
                return ColumnMetadata.COLUMN_NULLABLE_UNKNOWN;
        }
        throw new IllegalArgumentException("Illegal nullable value");
    }

    /**
     * Derives the precision from the child precisions.
     * 
     * @param precision
     * @param precision2
     * @return precision
     */
    private int getPrecision(int precision, int precision2)
    {
        // TODO do something sensible here
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public ColumnMetadata getMetadata()
    {
        return mType;
    }

    /**
     * Append the operator name (e.g. "+") to the given string buffer.
     * 
     * @param buf
     *            buffer
     */
    protected abstract void printOperator(StringBuffer buf);

    /**
     * Gets left operand expression.
     * 
     * @return left operand expression
     */
    public ArithmeticExpression getLeftExpression()
    {
        return mLeftChild;
    }

    /**
     * Gets right operand expression.
     * 
     * @return right operand expression.
     */
    public ArithmeticExpression getRightExpression()
    {
        return mRightChild;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        buf.append(mLeftChild);
        printOperator(buf);
        buf.append(mRightChild);
        buf.append(")");
        return buf.toString();
    }

}
