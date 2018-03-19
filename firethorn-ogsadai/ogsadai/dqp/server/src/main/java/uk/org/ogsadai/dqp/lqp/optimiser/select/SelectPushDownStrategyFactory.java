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


package uk.org.ogsadai.dqp.lqp.optimiser.select;

import uk.org.ogsadai.dqp.lqp.Operator;

/**
 * Factory interface for SELECT push down strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface SelectPushDownStrategyFactory
{
    /**
     * Get select push down strategy for a given operator to pass.
     * 
     * @param operator
     *            operator to pass
     * @return select push down strategy
     */
    public SelectPushDownStrategy getStrategy(Operator operator);
}
