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

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownStrategy;

/**
 * Pushes SELECT down past binary to the right branch.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushPastBinaryRightBranchStrategy implements
    SelectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger
        .getLogger(SelectPushPastBinaryRightBranchStrategy.class);
    
    /**
     * {@inheritDoc}
     */
    public SelectOperator pushDown(SelectOperator selectOperator)
    {
        SelectOperator passSelect = null;
        BinaryOperator binaryOperator = (BinaryOperator) selectOperator
            .getChild(0);

        try
        {
            binaryOperator.getChild(1).getHeading().containsAll(
                selectOperator.getUsedAttributes());
            passSelect = SelectStrategyUtils
                .pushDownToBranch(selectOperator, 1);
            return passSelect;
        }
        // AmbiguousAttributeException, AttributeNotFoundException
        catch (Exception e)
        {
            // if transformation causes exceptions it is illegal
            LOG.debug(e.getMessage());
            return null;
        }
    }
}
