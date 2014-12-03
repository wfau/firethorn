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
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Replaces attribute in an expression with arithmetic expression.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttrReplaceArithmeticExprVisitor extends
    CloneArithmeticExprVisitor
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Attribute to expression map. */
    private Map<Attribute, ArithmeticExpression> mAttrToExprMap;

    /** Used to signify if replacement have occurred. */
    private boolean mNewResult = false;
    
    /**
     * Constructor.
     * 
     * @param attrToExprMap
     *            maps attributes to arithmetic expressions
     */
    public AttrReplaceArithmeticExprVisitor(
        Map<Attribute, ArithmeticExpression> attrToExprMap)
    {
        mAttrToExprMap = attrToExprMap;
    }

    /**
     * Gets expression with replaced attributes.
     * 
     * @return arithmetic expression
     */
    public ArithmeticExpression getResult()
    {
        return mCurrentExpression;
    }

    /**
     * Check if any attributes were replaced.
     * 
     * @return <code>true</code> if replacement occurred
     */
    public boolean isResultNew()
    {
        return mNewResult;
    }

    /**
     * {@inheritDoc}
     */
    protected ArithmeticExpression cloneArithmeticExpression(
        ArithmeticExpression expression)
    {
        AttrReplaceArithmeticExprVisitor visitor = 
            new AttrReplaceArithmeticExprVisitor(mAttrToExprMap);
        expression.accept(visitor);
        
        ArithmeticExpression clone = visitor.getClone();
        
        if (visitor.isResultNew())
        {
            mNewResult = true;
        }

        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        ExecutableFunctionExpression efe;
        try
        {
            efe = new ExecutableFunctionExpression(function);
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
            // Can not do much about it
            throw new RuntimeException(e);
        }

        List<ArithmeticExpression> exprList = efe.getExecutable()
            .getParameters();
        for (int i = 0; i < exprList.size(); i++)
        {
            ArithmeticExpression expr = exprList.get(i);

            AttrReplaceArithmeticExprVisitor v = new AttrReplaceArithmeticExprVisitor(
                mAttrToExprMap);
            expr.accept(v);

            if (v.isResultNew())
            {
                exprList.set(i, v.getResult());
                mNewResult = true;
            }
        }

        mCurrentExpression = efe;
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        Attribute attr = 
            new AttributeImpl(tableColumn.getName(), tableColumn.getSource());

        ArithmeticExpression e = mAttrToExprMap.get(attr);
        if (e != null)
        {
            mCurrentExpression = e;
            mNewResult = true;
        }
        else
        {
            super.visitTableColumn(tableColumn);
        }
    }
}
