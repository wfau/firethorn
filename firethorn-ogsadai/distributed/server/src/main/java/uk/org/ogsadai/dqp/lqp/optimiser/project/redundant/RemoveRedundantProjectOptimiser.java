// Copyright (c) The University of Edinburgh, 2009-2012.
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

package uk.org.ogsadai.dqp.lqp.optimiser.project.redundant;

import java.util.HashSet;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser to remove redundant PROJECT operators from the logical query
 * plan.  A PROJECT is redundant if it contains no arithmetic expressions nor
 * functions (thus it contains only column names) and the PROJECT operator's
 * heading has the same attribute list as its child operator's heading.
 * 
 * @author The OGSA-DAI Project Team
 */
public class RemoveRedundantProjectOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2012.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(RemoveRedundantProjectOptimiser.class);

    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        List<Operator> projectOperators = 
            OptimiserUtils.findOccurrences(lqpRoot, OperatorID.PROJECT);
        
        for( Operator op : projectOperators)
        {
            ProjectOperator projectOp = (ProjectOperator) op;
            Operator childOp = projectOp.getChild(0);
            
            // Remove any project operator whose heading is exactly the
            // same as the heading of its child 
            if (!projectOp.hasDerivedAttributes() &&
                    projectOp.getHeading().getAttributes().equals(
                        childOp.getHeading().getAttributes()))
            {
                removeProject(projectOp, childOp);
            }
        }
        
        projectOperators = 
            OptimiserUtils.findOccurrences(lqpRoot, OperatorID.PROJECT);
        for( Operator op : projectOperators)
        {
            ProjectOperator projectOp = (ProjectOperator) op;
            Operator childOp = projectOp.getChild(0);

            // Remove any project that has an ancestor PROJECT and whose
            // heading is only a reordering of the child's heading
            if (!projectOp.hasDerivedAttributes() &&
                    hasProjectAncestors(projectOp))
            {
                HashSet<Attribute> projectHeading = 
                    new HashSet<Attribute>(projectOp.getHeading().getAttributes());
                HashSet<Attribute> childHeading = 
                    new HashSet<Attribute>(childOp.getHeading().getAttributes());
                if (projectHeading.equals(childHeading))
                {
                    removeProject(projectOp, childOp);
                }
            }
        }
        
        projectOperators = 
            OptimiserUtils.findOccurrences(lqpRoot, OperatorID.PROJECT);
        for( Operator op : projectOperators)
        {
            ProjectOperator projectOp = (ProjectOperator) op;

            if (!projectOp.hasDerivedAttributes() &&
                    hasAncestorProjectSeparatedBySelects(projectOp))
            {
                Operator childOp = projectOp.getChild(0);
                // This is a redundant project
                removeProject(projectOp, childOp);
            }
        }
                
        return lqpRoot;
    }

    /**
     * Removes the given project operator from the query plan.
     * 
     * @param projectOp
     *            project operator to remove
     * @param childOp
     *            child operator of the project operator
     */
    private void removeProject(ProjectOperator projectOp, Operator childOp) 
    {
        LOG.debug("Found a redundant project, removing it.");
        Operator parentOp = projectOp.getParent();
        
        parentOp.replaceChild(projectOp, childOp);
        childOp.setParent(parentOp);
        projectOp.disconnect();
    }

    /**
     * Tests whether the given operator has a PROJECT ancestor.
     * 
     * @param operator
     *            PROJECT operator
     * @return <code>true</code> if the given operator has a PROJECT ancestor
     *         and <code>false</code> otherwise
     */
    private boolean hasProjectAncestors(ProjectOperator operator) 
    {
        Operator op = operator.getParent();
        while(op.getID() != OperatorID.NIL)
        {
            if (op.getID() == OperatorID.PROJECT) return true;
            op = op.getParent();
        }
        return false;
    }

    /**
     * Tests whether the given operator has a PROJECT ancestor that is separated
     * from this operator only by SELECT operators.
     * 
     * @param operator operator
     * 
     * @return <code>true</code> if given operator has a PROJECT ancestor that 
     * is separated from this operator only by SELECT operators, 
     * <code>false</code> otherwise.
     */
    private boolean hasAncestorProjectSeparatedBySelects(Operator operator)
    {
        Operator op = operator.getParent();

        while(op.getID() != OperatorID.NIL)
        {
            if (op.getID() == OperatorID.PROJECT) return true;
            if (op.getID() != OperatorID.SELECT) return false;
            
            op = op.getParent();
        }
        return false;
    }
}
