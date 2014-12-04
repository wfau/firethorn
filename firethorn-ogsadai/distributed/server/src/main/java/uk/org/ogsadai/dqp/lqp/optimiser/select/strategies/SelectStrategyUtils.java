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

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;

/**
 * Utility methods shared by SELECT push down strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectStrategyUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Pushes select down past an unary operator.
     * 
     * @param selectOperator
     *            operator to be pushed down
     * @return pushed down SELECT
     * @throws RuntimeException
     *             when operator update after push down fails
     */
    public static SelectOperator pushDownNoChange(SelectOperator selectOperator)
    {
        return pushDownToBranch(selectOperator, 0);
    }

    /**
     * Pushes SELECT to one of the branches.
     * 
     * @param selectOperator
     *            operator to be pushed down
     * @param branchIdx
     *            branch into which to push
     * @return pushed down SELECT operator
     * @throws RuntimeException
     *             when operator update after push down fails
     */
    public static SelectOperator pushDownToBranch(
        SelectOperator selectOperator, int branchIdx)
    {
        Operator operatorToPass = selectOperator.getChild(0);
        Operator operatorToPassChild = operatorToPass.getChild(branchIdx);
        Operator selectParent = selectOperator.getParent();

        selectParent.replaceChild(selectOperator, operatorToPass);
        operatorToPass.replaceChild(operatorToPassChild, selectOperator);
        selectOperator.replaceChild(operatorToPass, operatorToPassChild);

        try
        {
            selectParent.update();
        }
        catch (LQPException e)
        {
            // If it happens the caller failed to ensure that operation is
            // permitted.
            throw new RuntimeException(e);
        }

        return selectOperator;
    }

    /**
     * Checks if the SELECT operator uses attributes derived in the PROJECT
     * child operator.
     * 
     * @param selectOperator
     *            operator to test
     * @return <code>true</code> if SELECT uses derived attributes
     */
    public static boolean usesChildProjectDerivedAttributes(
        SelectOperator selectOperator)
    {
        ProjectOperator projectOperator = (ProjectOperator) selectOperator
            .getChild(0);
        for (Attribute attr : selectOperator.getUsedAttributes())
        {
            if (AttributeUtils.containsMatching(
                attr, projectOperator.getDerivedAttributes(), 
                AttributeMatchMode.NO_TYPE))
            {
                return true;
            }
        }
        return false;
    }

}
