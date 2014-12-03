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

package uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies;

import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategy;

/**
 * Strategy for pulling up though join like operators.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullPastJoinStrategy implements RenamePullUpStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger
        .getLogger(RenamePullPastJoinStrategy.class);

    /**
     * {@inheritDoc}
     */
    public RenameOperator pullUp(RenameOperator operator)
    {
        RenameOperator passRename;
        
        // check if ambiguities may be introduced and stop if yes
        int renameIndex = ((BinaryOperator) operator.getParent()).getChildIndex(operator);
        if (renameIndex==0) renameIndex = 1;
        else if (renameIndex==1) renameIndex = 0;
        Operator otherChild = ((BinaryOperator) operator.getParent()).getChild(renameIndex);
        
        int myIdx = 
            ((BinaryOperator) operator.getParent()).getChildIndex(operator);
        List<Attribute> oppositeAttrList = 
            operator.getParent().getChild(myIdx == 1 ? 0 : 1).getHeading().getAttributes();
        
        if (OptimiserUtils.attributeClash(oppositeAttrList, 
                          operator.getRenameMap().getOriginalAttributeList()))
        {
            return null;
        }
        
        try 
        {
            RenameStrategyUtils.renameParentAttributes(operator);
            passRename = RenameStrategyUtils.pullPastBinary(operator);
        } 
        catch (LQPException e) 
        {
            LOG.debug(e.getMessage());
            return null;
        }
        return passRename;
    }
}
