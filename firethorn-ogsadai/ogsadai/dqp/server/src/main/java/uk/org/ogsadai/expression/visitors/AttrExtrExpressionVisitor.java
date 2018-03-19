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

package uk.org.ogsadai.expression.visitors;

import java.util.HashSet;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrExtrArithmeticExprVisitor;

/**
 * Attribute extracting expression visitor. Attribute extraction for operands is
 * delegated to {@link AttrExtrArithmeticExprVisitor}.
 * 
 * @see AttrExtrArithmeticExprVisitor
 * @author The OGSA-DAI Project Team.
 */
public class AttrExtrExpressionVisitor extends AttrExpressionVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Extracted attributes. */
    private Set<Attribute> mAttributes = new HashSet<Attribute>();
    /** LHS attributes. */
    private Set<Attribute> mLHSAttributes = new HashSet<Attribute>();
    /** RHS attributes. */
    private Set<Attribute> mRHSAttributes = new HashSet<Attribute>();

    /**
     * Gets extracted attributes.
     * 
     * @return extracted attributes
     */
    public Set<Attribute> getAttributes()
    {
        return mAttributes;
    }
    
    /**
     * Get left hand side extracted attributes.
     * 
     * @return lhs extracted attributes
     */
    public Set<Attribute> getLHSAttributes()
    {
        return mLHSAttributes;
    }

    /**
     * Get right hand side extracted attributes.
     * 
     * @return rhs extracted attributes
     */
    public Set<Attribute> getRHSAttributes()
    {
        return mRHSAttributes;
    }

    @Override
    protected void processLeftOperand(Operand operand)
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        ((ArithmeticExpressionOperand) operand).getExpression().accept(visitor);

        mLHSAttributes = visitor.getAttributes();
        mAttributes.addAll(visitor.getAttributes());
    }

    @Override
    protected void processRightOperand(Operand operand)
    {
        AttrExtrArithmeticExprVisitor visitor = 
            new AttrExtrArithmeticExprVisitor();
        ((ArithmeticExpressionOperand) operand).getExpression().accept(visitor);

        mRHSAttributes = visitor.getAttributes();
        mAttributes.addAll(visitor.getAttributes());
    }
}
