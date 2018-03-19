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

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategy;

/**
 * Tries to pull RENAME past another RENAME (merge).
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullPastRenameStrategy implements RenamePullUpStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public RenameOperator pullUp(RenameOperator operator)
    {
        RenameOperator renameParent = (RenameOperator) operator.getParent();
        Operator reconnectTargetParent = renameParent.getParent();

        RenameMap renameMap = new SimpleRenameMap(
                operator.getRenameMap().getOriginalAttributeList(), 
                renameParent.getRenameMap().getRenamedAttributeList());

        RenameOperator mergedRenameOperator = new RenameOperator(renameMap);

        reconnectTargetParent.replaceChild(renameParent, mergedRenameOperator);
        mergedRenameOperator.setChild(0, operator.getChild(0));

        try
        {
            reconnectTargetParent.update();
        }
        catch (LQPException e)
        {
            // if transformation introduces exceptions it is illegal
            return null;
        }

        operator.disconnect();
        renameParent.disconnect();

        return mergedRenameOperator;
    }
}
