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
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.converters.StringConversionException;
import uk.org.ogsadai.tuple.converters.StringConversionStrategy;
import uk.org.ogsadai.tuple.converters.StringConversionStrategyFactory;

/**
 * A literal constant.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Constant implements ArithmeticExpression
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    /** Column type of the constant. */
    private ColumnMetadata mType;
    /** Column type of the constant. */
    private ColumnMetadata mOriginalType;
    /** Constant value. */
    private final Object mValue;
    /** Type of the constant. */ 
    private final int mValueType;
    /** Context dependent type. */
    private int mContextType = -1;
    /** Context dependent value. */
    private Object mContextValue;

    /**
     * Creates a new constant expression for the given type and value.
     * 
     * @param type
     *            type of constant
     * @param object
     *            the string representation of the constant
     * @throws StringConversionException
     *             when string can not be converted to a specified type
     */
    public Constant(int type, String object) throws StringConversionException
    {
        StringConversionStrategy strategy = StringConversionStrategyFactory
            .getLiteralStrategy(type);
        mValue = strategy.getObject(object);
        mValueType = type;
    }
    
    /**
     * Constructs a copy of the given constant.
     * 
     * @param constant
     *            constant value
     */
    public Constant(Constant constant)
    {
        mValue = constant.mValue;
        mType = constant.mType;
        mOriginalType = mType;
        mValueType = constant.mValueType;
    }
    
    /**
     * {@inheritDoc}
     */
    public ArithmeticExpression[] getChildren()
    {
        return new ArithmeticExpression[0];
    }
    
    /**
     * {@inheritDoc}
     */
    public void configure(TupleMetadata metadata)
        throws TypeMismatchException
    {
        mType = new SimpleColumnMetadata(
                null, 
                mValueType, 
                0, 
                ColumnMetadata.COLUMN_NO_NULLS, 
                0);
        mOriginalType = mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public void configure(
        TupleMetadata metadata, Set<Attribute> correlatedAttributes)
        throws TypeMismatchException
    {
        configure(metadata);
    }
    

    /**
     * {@inheritDoc}
     */
    public void evaluate(Tuple tuple) throws ExpressionEvaluationException
    {
        // Try to cast to a context specific object type
        if (mContextType != -1)
        {
            StringConversionStrategy strategy = StringConversionStrategyFactory
                .getLiteralStrategy(mContextType);

            try
            {
                mContextValue = strategy.getObject(mValue.toString());
            }
            catch (StringConversionException e)
            {
                throw new ExpressionEvaluationException(e);
            }
            catch (NumberFormatException e)
            {
                throw new ExpressionEvaluationException(
                    new StringConversionException("Expected literal of type "
                        + mContextType + " found: " + mValue.toString()));
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mValue == Null.getValue())
        {
            return Null.getValue();
        }
        else
        {
            if (mContextType != -1 && mContextValue != null)
            {
                return mContextValue;
            }
            else
            {
                return mValue;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ColumnMetadata getMetadata()
    {
        return mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return mValue.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor)
    {
        visitor.visitConstant(this);
    }

    /**
     * Returns the tuple type of this constant.
     * 
     * @return constant type
     */
    public int getType()
    {
        if(mContextType != -1)
        {
            return mContextType;
        }
        else
        {
            return mValueType;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void setContextType(int type)
    {
        if (type != mValueType)
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
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (!(obj instanceof Constant))
        {
            return false;
        }
        
        Constant c = (Constant) obj;
        return this.mValue.equals(c.mValue);
    }
}
