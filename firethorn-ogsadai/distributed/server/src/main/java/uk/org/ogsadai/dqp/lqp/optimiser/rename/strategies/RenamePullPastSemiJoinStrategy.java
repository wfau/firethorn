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

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategy;

/**
 * Tries to pull RENAME past [ANTI] SEMI JOIN.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullPastSemiJoinStrategy implements RenamePullUpStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public RenameOperator pullUp(RenameOperator operator)
    {
        try
        {     
            int myIdx = 
            ((BinaryOperator) operator.getParent()).getChildIndex(operator);
            List<Attribute> oppositeAttrList = 
                operator.getParent().getChild(myIdx == 1 ? 0 : 1).getHeading().getAttributes();
        
            if (OptimiserUtils.attributeClash(oppositeAttrList, 
                    operator.getRenameMap().getOriginalAttributeList()))
            {
                return null;
            }

            RenameStrategyUtils.renameParentAttributes(operator);

            int childIndex = ((BinaryOperator) operator.getParent())
                    .getChildIndex(operator);

            if (childIndex == 0) 
            {
                return RenameStrategyUtils.pullPastNoChange(operator);
            } 
            else if (childIndex == 1) 
            {
                // swallow rename
                operator.getParent().replaceChild(operator,
                        operator.getChild(0));
                operator.disconnect();

                return null;
            }
            else
            {
                return null;
            }
        }
        catch (LQPException e)
        {
            // if transformation causes exceptions it is illegal
            return null;
        }
    }
    
 }
