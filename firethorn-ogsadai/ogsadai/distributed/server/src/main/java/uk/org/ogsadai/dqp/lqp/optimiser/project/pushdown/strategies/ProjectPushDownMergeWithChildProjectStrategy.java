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
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownStrategy;

/**
 * Strategy merging two PROJECT operators.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownMergeWithChildProjectStrategy
    implements ProjectPushDownStrategy
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
        try
        {
            Operator child = projectOperator.getChild(0);
            Operator grandchild = child.getChild(0);
            Operator parent = projectOperator.getParent();
            
            ProjectOperator mergedOp = projectOperator.createMegredWithChild();
            result.add(mergedOp);
            
            mergedOp.setChild(0, grandchild);
            parent.replaceChild(projectOperator, mergedOp);
                        
            projectOperator.disconnect();
            
            // child will be in the list of candidates to be pushed down - it
            // needs to be ignored after being merged
            child.disconnect();
            child.addAnnotation(ProjectPushDownOptimiser.IGNORE, true);
            
            parent.update();
            parent.validate();
        }
        catch (LQPException e)
        {
            throw new RuntimeException(e);
        }
        
        return result;
    }
}
