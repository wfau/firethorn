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

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Attribute renaming arithmetic expression visitor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttrRenameArithmeticExprVisitor extends
    AttrArithmeticExprVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Rename map. */
    RenameMap mRenameMap;

    /**
     * Constructor.
     * 
     * @param renameMap
     *            a from->to rename map
     */
    public AttrRenameArithmeticExprVisitor(RenameMap renameMap)
    {
         mRenameMap = renameMap;
    }

    /**
     * {@inheritDoc}
     */
    public void visitFunction(ExecutableFunctionExpression function)
    {
        for (ArithmeticExpression expr : function.getExecutable()
            .getParameters())
        {
            expr.accept(this);
        }
        
        // TODO: Understand if we need to update both children and
        // parametersExpr
        for (ArithmeticExpression expr : function.getChildren())
        {
            expr.accept(this);
        }        
        
    }

    /**
     * {@inheritDoc}
     */
    public void visitTableColumn(TableColumn tableColumn)
    {
        Attribute attr;
        try
        {
            Attribute tcAttr = new AttributeImpl(
                    tableColumn.getName(), tableColumn.getSource());
            
            attr = mRenameMap.getRenamedAttribute(tcAttr);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if (attr != null)
        {
            tableColumn.rename(attr.getName(), attr.getSource());
        }
    }
}
