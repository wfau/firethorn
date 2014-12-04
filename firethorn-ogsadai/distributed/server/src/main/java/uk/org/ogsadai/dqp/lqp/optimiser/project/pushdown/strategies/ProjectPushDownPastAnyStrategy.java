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

package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies;

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownStrategy;
import uk.org.ogsadai.resource.dataresource.dqp.DQPInternalException;

/**
 * Strategy for PROJECT push down optimiser that pushes PROJECT down past any
 * child.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownPastAnyStrategy implements ProjectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator)
    {
        List<ProjectOperator> result = new LinkedList<ProjectOperator>();
        
        Operator parent = projectOperator.getParent();
        Operator child = projectOperator.getChild(0);
        Operator grandchild = child.getChild(0);
        
        parent.replaceChild(projectOperator, child);
        
        child.setParent(parent);
        child.setChild(0, projectOperator);
        
        projectOperator.setParent(child);
        projectOperator.setChild(0, grandchild);
        
        grandchild.setParent(projectOperator);
        
        try
        {
            parent.update();
        }
        catch (LQPException e)
        {
            // This should not happen, if it does it is an internal error
            throw new DQPInternalException(e);
        }

        return result;
    }
}
