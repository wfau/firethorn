// Copyright (c) The University of Edinburgh, 2009.
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.udf.ExecutableFunction;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Wraps a function as an arithmetic expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ExecutableFunctionExpression implements ArithmeticExpression
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /** The wrapped function. */
    private ExecutableFunction mFunction;
    /** Type of this table column. */
    private ColumnMetadata mType;
    /** Type of this table column. */
    private ColumnMetadata mOriginalType;    
    /** Current column value. */
    private Object mValue;
    /** Context dependent type. */
    private int mContextType = -1;
    /** Context dependent value. */
    private Object mContextValue;

    /**
     * Constructs a new function expression.
     * 
     * @param function
     *            the function to wrap
     */
    public ExecutableFunctionExpression(ExecutableFunction function)
    {
        mFunction = function;
    }

    /**
     * Copy constructor. This method fails if the wrapped executable function
     * does not have a copy constructor.
     * 
     * @param copy
     *            the function to copy
     * @throws CloneNotSupportedException
     *             when wrapped executable function does not have a copy
     *             constructor
     */
    public ExecutableFunctionExpression(
            ExecutableFunctionExpression copy)
        throws CloneNotSupportedException
    {
        try
        {
            // try finding a copy constructor
            Constructor<? extends ExecutableFunction> constructor = 
                copy.mFunction.getClass().getConstructor(copy.mFunction.getClass());
            mFunction = constructor.newInstance(copy.mFunction);
            
            // clone parameter expressions
            List<ArithmeticExpression> exprList = 
                new ArrayList<ArithmeticExpression>();
            for(ArithmeticExpression e : copy.mFunction.getParameters())
            {
                exprList.add(CloneArithmeticExprVisitor.cloneExpression(e));
            }
            mFunction.initialise(exprList);
        }
        catch (Exception e)
        {
            // something went wrong, we can't clone the function
            // TODO we could build and configure the function from scratch
            throw new CloneNotSupportedException();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor)
    {
        visitor.visitFunction(this);
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
        TupleMetadata metadata, Set<Attribute> correlatedAttributes)
        throws TypeMismatchException
    {
        int[] parameterTypes = new int[mFunction.getParameters().size()];
        for (int i = 0; i < mFunction.getParameters().size(); i++)
        {
            mFunction.getParameters().get(i).configure(
                metadata, correlatedAttributes);
            parameterTypes[i] = mFunction.getParameters().get(i)
                .getMetadata().getType();
        }
        mFunction.configure(parameterTypes);
        
        mType =
            new SimpleColumnMetadata("", mFunction.getOutputType(), 0,
                ColumnMetadata.COLUMN_NULLABLE_UNKNOWN, 0);
        mOriginalType = mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public void evaluate(Tuple tuple) throws ExpressionEvaluationException
    {
        Object[] parameters = new Object[mFunction.getParameters().size()];
        for (int i=0; i<mFunction.getParameters().size(); i++)
        {
            mFunction.getParameters().get(i).evaluate(tuple);
            parameters[i] = mFunction.getParameters().get(i).getResult();
        }
        mFunction.put(parameters);
        
        mValue = mFunction.getResult();
        
        
        // Try to cast to a context specific object type
        if (mValue != Null.getValue() && mContextType != -1)
        {
            try
            {
                mContextValue = TypeConverter.convertObject(
                    mOriginalType.getType(), mContextType, mValue);
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
    public void setContextType(int type)
    {
        if (type != mType.getType())
        {    
            mContextType = type;
    
            mType = new SimpleColumnMetadata("", mContextType, 0,
                ColumnMetadata.COLUMN_NULLABLE_UNKNOWN, 0);
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
    public Object getResult()
    {
        if (mValue == Null.getValue())
        {
            return Null.getValue();
        }
        else
        {
            if (mContextType != -1)
            {
                return mContextValue;
            }
            else
            {
                if (mValue == null)
                {
                    mValue = mFunction.getResult();
                }
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
    public ArithmeticExpression[] getChildren()
    {
        return (ArithmeticExpression[]) mFunction.getParameters().toArray(
            new ArithmeticExpression[mFunction.getParameters().size()]);
    }
    
    /**
     * Returns the wrapped executable function.
     * 
     * @return wrapped function
     */
    public ExecutableFunction getExecutable()
    {
        return mFunction;
    }
    
    /**
     * Get expression in SQL format.
     * 
     * @return expression in SQL format.
     */
    public String toString()
    {
        return ExpressionUtils.generateSQL(this);
    }
}
