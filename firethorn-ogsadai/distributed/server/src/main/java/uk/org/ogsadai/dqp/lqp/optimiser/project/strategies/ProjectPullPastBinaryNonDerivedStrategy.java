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
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.ProjectPullUpStrategy;

/**
 * Pulls PROJECT operator past binary, provided that parent is not using any
 * attributes that are derived in PROJECT.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ProjectPullPastBinaryNonDerivedStrategy implements
    ProjectPullUpStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public ProjectOperator pullUp(ProjectOperator operator)
    {
	int myIndex = ((BinaryOperator) operator.getParent())
		.getChildIndex(operator);

        // Check if pull introduces ambiguity. For example in
        // (JOIN{a.x = a.y} (PROJECT{a.x} a) (PROJECT{a.y} a))
        int otherIndex = (myIndex == 0) ? 1 : 0;
	List<Attribute> otherHeadAttr = operator.getParent().getChild(
		otherIndex).getHeading().getAttributes();
	Heading childHeading = operator.getChild(0).getHeading();
	for (Attribute a : otherHeadAttr)
	{
	    if (childHeading.contains(a))
		return null;
	}
	
        if (ProjectStrategyUtils.parentUsesDerivedAttributes(operator))
        {
            return null;
        }
        return ProjectStrategyUtils.pullPastBinary(operator);
    }
}
