// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;

/**
 * Interface for strategies to push PROJECT operators down the query plan.
 *
 * @author The OGSA-DAI Project Team
 */
public interface ProjectPushDownStrategy
{
    /**
     * Tries to push down a project operator past its child.
     * 
     * @param projectOperator
     *      project operator to be pushed down
     * @return
     *      list of newly pushed down PROJECT operators.
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator);
}
