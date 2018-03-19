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

package uk.org.ogsadai.dqp.lqp.udf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.SQLGenArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.TypeArithmeticExpressionVisitor;

/**
 * Base class providing convenience implementations for classes that implement
 * logical function and executable function, for example SQL aggregate functions
 * such as COUNT.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class LogicalExecutableFunctionBase 
    implements LogicalFunction, ExecutableFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Annotations. */
    protected Map<String, Object> mAnnotations = 
        new ConcurrentHashMap<String, Object>();
    
    /** Headings of children. */
    protected Heading[] mInputHeadings;
    
    /** Input parameter expressions. */
    private List<ArithmeticExpression> mParemeterExprList = 
        new ArrayList<ArithmeticExpression>();

    /**
     * Constructs a new logical executable function with the given number of
     * parameters.
     * 
     * @param parameterCount
     *            number of parameters
     */
    public LogicalExecutableFunctionBase(int parameterCount)
    {
        mInputHeadings = new Heading[parameterCount];
    }

    /**
     * {@inheritDoc}
     */
    public Heading getHeading()
    {
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (int i = 0; i < mInputHeadings.length; i++)
        {
            ArithmeticExpression expression = mParemeterExprList.get(i);
            TypeArithmeticExpressionVisitor visitor = new TypeArithmeticExpressionVisitor(
                mInputHeadings[i].getAttributes());
            expression.accept(visitor);
            Attribute attribute = new AttributeImpl("", visitor.getType());
            attributes.add(attribute);
        }
        return new HeadingImpl(attributes);
    }

    /**
     * {@inheritDoc}
     */
    public void setInputHeading(int index, Heading heading)
    {
        mInputHeadings[index] = heading;
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        
        for (int i=0; i<mInputHeadings.length; i++)
        {
            ArithmeticExpression e = mParemeterExprList.get(i);
            e.accept(visitor);
            mInputHeadings[i].containsAll(visitor.getAttributes());
            visitor.reset();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ArithmeticExpression> getParameters()
    {
        return mParemeterExprList;
    }
    
    /**
     * {@inheritDoc}
     */
    public void initialise(List<ArithmeticExpression> parameters)
    {
        mParemeterExprList = parameters;
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotation(String key, Object value)
    {
        mAnnotations.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object getAnnotation(String key)
    {
        return mAnnotations.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public void removeAnnotation(String key)
    {
        mAnnotations.remove(key);
    }
    
    @Override
    public Map<String, Object> getAnnotations()
    {
        return mAnnotations;
    }

    /**
     * {@inheritDoc}
     */
    public String toSQL()
    {
        StringBuilder name = new StringBuilder();
        name.append(getName());
        name.append("(");
        
        ArithmeticExpression e;
        SQLGenArithmeticExpressionVisitor visitor = 
            new SQLGenArithmeticExpressionVisitor();
        
        for (int j=0; j<mParemeterExprList.size()-1; j++)
        {
            e = mParemeterExprList.get(j);
            e.accept(visitor);
            name.append(visitor.getSQLString());
            visitor.reset();
            name.append(",");
        }
        e = mParemeterExprList.get(mParemeterExprList.size()-1);
        e.accept(visitor);
        name.append(visitor.getSQLString());
        name.append(")");

        return name.toString();
    }

}
