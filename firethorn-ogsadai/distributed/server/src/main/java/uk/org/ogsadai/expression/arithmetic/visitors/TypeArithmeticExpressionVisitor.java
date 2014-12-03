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

package uk.org.ogsadai.expression.arithmetic.visitors;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.BinaryExpression;
import uk.org.ogsadai.expression.arithmetic.Constant;
import uk.org.ogsadai.expression.arithmetic.Div;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.Minus;
import uk.org.ogsadai.expression.arithmetic.Mult;
import uk.org.ogsadai.expression.arithmetic.Plus;
import uk.org.ogsadai.expression.arithmetic.Star;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Visitor which traverses an expression tree and types it.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TypeArithmeticExpressionVisitor implements
        ArithmeticExpressionVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Attribute type. */
    private int mType = -1;
    /** Attribute source table. */
    private String mSource;
    /** List of available input attributes. */
    private List<Attribute> mAttributes;

    /**
     * Constructs a new visitor.
     * 
     * @param attributes
     *            input attributes
     */
    public TypeArithmeticExpressionVisitor(List<Attribute> attributes)
    {
        mAttributes = attributes;
    }
    
    /**
     * {@inheritDoc}
     */
    public void visitConstant(Constant expression)
    {
        mType = expression.getType();
        mSource = null;
    }

    /**
     * {@inheritDoc}
     */
    public void visitDiv(Div expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        mSource = null;
        List<ArithmeticExpression> paramList = function.getExecutable()
            .getParameters();

        int[] types = new int[paramList.size()];
        for (int i=0; i<paramList.size(); i++)
        {
            paramList.get(i).accept(this);
            types[i] = mType;
        }
        try
        {
            function.getExecutable().configure(types);
        }
        catch (TypeMismatchException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mType = function.getExecutable().getOutputType();
    }

    /**
     * {@inheritDoc}
     */
    public void visitMinus(Minus expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitMult(Mult expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitPlus(Plus expression)
    {
        visitBinary(expression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        Attribute matches = null;
        for (Attribute attribute : mAttributes)
        {
            if(attribute.getName().equals( tableColumn.getName() ))
            {
                if(attribute.getSource() != null && tableColumn.getSource() != null)
                {
                    if(attribute.getSource().equals( tableColumn.getSource() ))
                    {
                        matches = attribute;
                    }
                }
                else
                {
                    matches = attribute;
                }
            }
            
        }
        if (matches != null)
        {
            mType = matches.getType();
            mSource = matches.getSource();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitStar(Star expression)
    {
        mType = expression.getMetadata().getType();
        mSource = null;
    }

    /**
     * Visits the given binary expression and determines the type from the
     * children's types.
     * 
     * @param expression
     *            binary expression
     */
    private void visitBinary(BinaryExpression expression)
    {
        mSource = null;
        expression.getLeftExpression().accept(this);
        int type1 = mType;
        expression.getRightExpression().accept(this);
        int type2 = mType;
        try
        {
            mType = TypeConverter.getArithmeticType(type1, type2);
        }
        catch (TypeMismatchException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Returns the type of the expression that was visited.
     * 
     * @return expression type
     */
    public int getType()
    {
        return mType;
    }

    /**
     * Returns the source table of the expression that was visited.
     * 
     * @return source table of <code>null</code> if the expression is derived
     */
    public String getSource()
    {
        return mSource;
    }


}
