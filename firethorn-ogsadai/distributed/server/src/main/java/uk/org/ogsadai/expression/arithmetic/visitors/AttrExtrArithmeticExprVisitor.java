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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Attribute extracting arithmetic expression visitor. Attributes extracted by
 * this visitor will always have their types set to <code>-1</code> (unknown).
 * The rationale behind this is that arithmetic expressions in a logical query
 * plan have purely symbolic meaning. When you look at the expression like
 * <code>a.id = 100</code> you can not tell the exact type of the
 * <code>a.id</code> attribute. That's why attributes extracted from expressions
 * have unknown type.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttrExtrArithmeticExprVisitor extends
    AttrArithmeticExprVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Extracted attributes. */
    private Set<Attribute> mAttributes = new HashSet<Attribute>();

    /** Extracted ordered attributes. */
    private List<Attribute> mOrderedAttributes = new LinkedList<Attribute>();

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        for (ArithmeticExpression ae : function.getExecutable().getParameters())
        {
            ae.accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        Attribute newAttr = new AttributeImpl(
            tableColumn.getName(), 
            -1,
            tableColumn.getSource());

        if (!mAttributes.contains(newAttr))
        {
            mAttributes.add(newAttr);
            mOrderedAttributes.add(newAttr);
        }
    }

    /**
     * Returns a set of extracted attributes.
     * 
     * @return extracted attributes list
     */
    public Set<Attribute> getAttributes()
    {
        return mAttributes;
    }
    
    /**
     * Returns a list of extracted attributes in the order they are visited.
     * Duplicates will not be added twice.
     * 
     * @return extracted attributes list
     */
    public List<Attribute> getOrderedAttributes()
    {
        return mOrderedAttributes;
    }
    
    /**
     * Resets the state.
     */
    public void reset()
    {
        mAttributes = new HashSet<Attribute>();
    }
}
