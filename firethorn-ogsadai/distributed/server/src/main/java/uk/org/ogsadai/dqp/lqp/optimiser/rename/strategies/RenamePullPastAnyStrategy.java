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

import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpStrategy;

/**
 * Pull up RENAME strategy for passing operators which do not require any
 * changes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenamePullPastAnyStrategy implements RenamePullUpStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public RenameOperator pullUp(RenameOperator operator)
    {
        RenameOperator passOperator;
        try
        {
            passOperator = RenameStrategyUtils.pullPastNoChange(operator);
        }
        catch (LQPException e)
        {
            // if operator push causes exception - it is illegal
            return null;
        }
        return passOperator;
    }
}
