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

package uk.org.ogsadai.expression.visitors;

import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.arithmetic.visitors.AttrRenameToPhysicalAttrArithmeticExprVisitor;

/**
 * Attribute renaming expression visitor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class AttrRenameToPhysicalAttrExpressionVisitor 
    extends AttrExpressionVisitorBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";


    private DataDictionary mDataDictionary;
    private DataNode mDataNode;

    /**
     * Constructor.
     */
    public AttrRenameToPhysicalAttrExpressionVisitor(
            DataDictionary dataDictionary, 
            DataNode dataNode)
    {
        mDataDictionary = dataDictionary;
        mDataNode = dataNode;
    }

    @Override
    protected void processLeftOperand(Operand operand)
    {
        AttrRenameToPhysicalAttrArithmeticExprVisitor v = 
            new AttrRenameToPhysicalAttrArithmeticExprVisitor(
                    mDataDictionary, mDataNode);
        ((ArithmeticExpressionOperand) operand).getExpression().accept(v);
    }

    @Override
    protected void processRightOperand(Operand operand)
    {
        processLeftOperand(operand);
    }

}
