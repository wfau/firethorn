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

package uk.org.ogsadai.dqp.lqp.optimiser.select.strategies;

import uk.org.ogsadai.dqp.lqp.operators.ScanBindApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownStrategy;

/**
 * Strategy to push a SELECT operator past a SCAN_BIND_APPLY operator.  This
 * strategy simply pushes the SELECT down the non-parameterised branch if 
 * possible.  No attempt is made to push the SELECT down the parameterised
 * branch.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SelectPushPastScanBindApplyStrategy 
    implements SelectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /**
     * {@inheritDoc}
     */
    public SelectOperator pushDown(SelectOperator selectOperator)
    {
        ScanBindApplyOperator scanBindApplyOp = 
            (ScanBindApplyOperator) selectOperator.getChild(0);
        
        SelectPushDownStrategy strategy;
        if (scanBindApplyOp.getParameterisedBranchIndex() == 0)
        {
            strategy = new SelectPushPastBinaryRightBranchStrategy();
        }
        else
        {
            strategy = new SelectPushPastBinaryLeftBranchStrategy();
        }
        return strategy.pushDown(selectOperator);
    }
}
