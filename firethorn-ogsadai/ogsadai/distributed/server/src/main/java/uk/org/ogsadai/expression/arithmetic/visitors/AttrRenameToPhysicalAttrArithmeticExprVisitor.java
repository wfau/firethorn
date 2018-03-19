// Copyright (c) The University of Edinburgh, 2012.
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

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Renames the attributes in the expression with the local table name.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttrRenameToPhysicalAttrArithmeticExprVisitor extends
    AttrArithmeticExprVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private DataDictionary mDataDictionary;

    private DataNode mDataNode;

    /**
     * Constructor.
     * 
     * @param renameMap
     *            a from->to rename map
     */
    public AttrRenameToPhysicalAttrArithmeticExprVisitor(
            DataDictionary dictionary, DataNode dataNode)
    {
         mDataDictionary = dictionary;
         mDataNode = dataNode;
    }

    @Override
    public void visitFunction(ExecutableFunctionExpression function)
    {
        for (ArithmeticExpression expr : function.getExecutable().getParameters())
        {
            expr.accept(this);
        }
        
        for (ArithmeticExpression expr : function.getChildren())
        {
            expr.accept(this);
        }        
        
    }

    @Override
    public void visitTableColumn(TableColumn tableColumn)
    {
        try
        {
            if (tableColumn.getSource() != null)
            {
                String originalTableName = 
                        mDataDictionary.getOriginalTableName(
                                tableColumn.getSource(), mDataNode);
                tableColumn.rename(tableColumn.getName(), originalTableName);
            }
        }
        catch (TableNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
