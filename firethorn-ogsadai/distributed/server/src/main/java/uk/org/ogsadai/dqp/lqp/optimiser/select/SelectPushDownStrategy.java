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

import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;

/**
 * Interface for select push down strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface SelectPushDownStrategy
{
    /**
     * Tries to push down select. If possible - reference to pushed down select
     * is returned - if push down in not allowed <code>null</code> is returned.
     * 
     * @param selectOperator
     *            operator to be pushed down
     * @return pushed down select or <code>null</code>
     */
    public SelectOperator pushDown(SelectOperator selectOperator);
}
