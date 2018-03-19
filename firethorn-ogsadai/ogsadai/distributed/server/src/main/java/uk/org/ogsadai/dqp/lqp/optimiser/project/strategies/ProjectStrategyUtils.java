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

package uk.org.ogsadai.dqp.lqp.optimiser.project.strategies;

import java.util.List;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;

/**
 * Utility code shared between project strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ProjectStrategyUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Pull past an operator without changes - just reconnect.
     * 
     * @param projectOperator
     *            operator to pull
     * @return reconnected project operator
     * @throws LQPException
     *             if update after pull fails
     */
    public static ProjectOperator pullPastNoChange(
        ProjectOperator projectOperator) throws LQPException
    {
        Operator child = projectOperator.getChild(0);
        Operator parent = projectOperator.getParent();
        Operator parentParent = parent.getParent();

        parentParent.replaceChild(parent, projectOperator);
        projectOperator.replaceChild(child, parent);
        parent.replaceChild(projectOperator, child);

        parentParent.update();
        return projectOperator;
    }

    /**
     * Pull past a binary operator appending heading from the opposite branch
     * (opposite operand).
     * 
     * @param projectOperator
     *            operator to pull
     * @return project operator pulled past a binary operator
     * @throws LQPException
     *             if update after pull fails
     */
    public static ProjectOperator pullPastBinary(ProjectOperator projectOperator)
    {
        int myIndex = ((BinaryOperator) projectOperator.getParent())
            .getChildIndex(projectOperator);
        
        ProjectOperator updatedProject;
        if (myIndex == 0)
        {
            updatedProject = projectOperator
                .getDisconnectedCopyAppendRight(projectOperator.getParent()
                    .getChild(1).getHeading().getAttributes());
        }
        else if (myIndex == 1)
        {
            updatedProject = projectOperator
                .getDisconnectedCopyAppendLeft(projectOperator.getParent()
                    .getChild(0).getHeading().getAttributes());
        }
        else
        {
            throw new IllegalStateException("Not a child");
        }

        Operator child = projectOperator.getChild(0);
        Operator parent = projectOperator.getParent();
        Operator parentParent = parent.getParent();

        parentParent.replaceChild(parent, updatedProject);
        updatedProject.setChild(0, parent);
        parent.replaceChild(projectOperator, child);

        projectOperator.disconnect();

        try
        {
            parentParent.update();            
            return updatedProject;
        }
        catch (LQPException e)
        {
            e.printStackTrace();
            throw new IllegalStateException("Illegal LQP state.");
        }
    }

    /**
     * Checks is a parent operator uses derived attributes.
     * 
     * @param projectOperator
     *            operator to test
     * @return <code>true</code> if parent uses derived attributes,
     *         <code>false</code> otherwise
     */
    public static boolean parentUsesDerivedAttributes(
	    ProjectOperator projectOperator)
    {
	for (Attribute attr : projectOperator.getParent().getUsedAttributes())
	{
	    if (AttributeUtils.containsMatching(attr, projectOperator
		    .getDerivedAttributes(),
		    AttributeMatchMode.NAME_AND_NULL_SOURCE))
	    {
		return true;
	    }
	}
	return false;
    }
}
