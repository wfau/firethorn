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

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;

/**
 * Strategy to push a PROJECT operator past a UNION operator.  We can only 
 * push past a UNION ALL operator otherwise the PROJECT will alter the duplicate
 * detection.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownPastUnionStrategy 
    extends ProjectPushDownCopyPastStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ProjectPushDownPastUnionStrategy.class);

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator)
    {
        Operator child = projectOperator.getChild(0);
        
        if (child instanceof UnionOperator)
        {
            UnionOperator union = (UnionOperator) child;
            
            if (LOG.isDebugEnabled())
            {
                LOG.debug(
                    "Trying to push PROJECT down past UNION.  Bag Op = " +
                    union.isBagOperator());
            }
            
            if (union.isBagOperator())
            {
                // Duplicates are allowed (UNION ALL) so we can push the
                // project further down
                return super.pushDown(projectOperator);
            }
            else
            {
                // Cannot push a PROJECT down past a set UNION with duplicate
                // removal
                return new LinkedList<ProjectOperator>();
            }
        }
        else
        {
            // Shouldn't really use the strategy for a non UNION operator
            return super.pushDown(projectOperator);
        }
    }
}
