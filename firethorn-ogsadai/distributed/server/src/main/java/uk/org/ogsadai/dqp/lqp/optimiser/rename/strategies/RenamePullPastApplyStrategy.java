// Copyright (c) The University of Edinburgh, 2010.
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

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategy;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategyFactoryImpl;

/**
 * Strategy for pulling RENAME past APPLY.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullPastApplyStrategy implements RenamePullUpStrategy 
{
    
    @Override
    public RenameOperator pullUp(RenameOperator renameOperator) 
    {
        ApplyOperator apply = (ApplyOperator) renameOperator.getParent();

        // If rename heading includes correlated attributes then we can't pull
        boolean contains = false;
        for (Attribute attr : apply.getAttributesToBind())
            contains |= renameOperator.getHeading().contains(attr);
        if (contains)
            return null;

        // If rename does not affect correlated attributes then we can try

        // Reconnect wrapped attribute
        Operator wrapped = apply.getOperator();
        swap(apply, wrapped);

        // Try to pull past wrapped operator
        RenamePullUpStrategy strategy = (new RenamePullUpStrategyFactoryImpl())
                .getStrategy(wrapped);

        RenameOperator pulled = strategy.pullUp(renameOperator);
        swap(wrapped, apply);

        return pulled;
    }

    /**
     * Swaps operators keeping parent and children.
     * 
     * @param outer
     * @param inner
     */
    private void swap(Operator outer, Operator inner) 
    {
        outer.getParent().replaceChild(outer, inner);
        inner.setChild(0, outer.getChild(0));
        inner.setChild(1, outer.getChild(1));
        outer.disconnect();
    }

}
