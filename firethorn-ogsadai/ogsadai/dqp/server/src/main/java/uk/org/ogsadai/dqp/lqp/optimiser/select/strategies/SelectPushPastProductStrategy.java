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

package uk.org.ogsadai.dqp.lqp.optimiser.select.strategies;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownStrategy;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Pushes SELECT past PROCUCT. If SELECT predicate is not using any predicates
 * this strategy switched to <code>SelectPushPastSetOpStrategy</code>.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushPastProductStrategy implements SelectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public SelectOperator pushDown(SelectOperator selectOperator)
    {     
        // If no attributes are used in the predicate - push down a copy to each
        // branch
        if (ExpressionUtils.getUsedAttributes(
            selectOperator.getPredicate().getExpression()).size() == 0)
        {
            SelectPushDownStrategy spds = new SelectPushPastSetOpStrategy();
            return spds.pushDown(selectOperator);
        }

        Operator selectParent = selectOperator.getParent();
        Operator productOperator = selectOperator.getChild(0);

        SelectOperator passSelect = null;
        try
        {            
            productOperator.getChild(0).getHeading().containsAll(
                selectOperator.getUsedAttributes());

            passSelect = SelectStrategyUtils.pushDownToBranch(selectOperator,
                0);
            return passSelect;
        }
        // AmbiguousAttributeException, AttributeNotFoundException
        catch (Exception e)
        {
            // if transformation causes exceptions it is illegal
            // but passing to right branch may still work
        }

        try
        {
            productOperator.getChild(1).getHeading().containsAll(
                selectOperator.getUsedAttributes());
            passSelect = SelectStrategyUtils.pushDownToBranch(selectOperator,
                1);
            return passSelect;
        }
        // AmbiguousAttributeException, AttributeNotFoundException
        catch (Exception e)
        {
            // if transformation causes exceptions it is illegal
        }
        
        // if pass to branch successful 
        if(passSelect != null)
        {
            return passSelect;
        }
        else
        {    
            // Turn into JOIN
            Operator joinOperator = new InnerThetaJoinOperator(selectOperator
                .getPredicate());
    
            joinOperator.setChild(0, productOperator.getChild(0));
            joinOperator.setChild(1, productOperator.getChild(1));
            selectParent.replaceChild(selectOperator, joinOperator);
    
            selectOperator.disconnect();
            productOperator.disconnect();
    
            try
            {
                selectParent.update();
            }
            catch (LQPException e)
            {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
