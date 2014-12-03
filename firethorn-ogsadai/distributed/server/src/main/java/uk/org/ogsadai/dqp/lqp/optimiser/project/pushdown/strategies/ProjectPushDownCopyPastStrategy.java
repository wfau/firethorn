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

package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownStrategy;
import uk.org.ogsadai.resource.dataresource.dqp.DQPInternalException;

/**
 * Strategy copying PROJECT past an operator.  The head of the new PROJECT is
 * the set: (usedAttributes(PROJECT) UNION usedAttributes(PROJECT.child))
 * INTERSECTION head(PROJECT.child.child).
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownCopyPastStrategy 
    implements ProjectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2012.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ProjectPushDownCopyPastStrategy.class);

    /** Number of children to process. Defaults at all of them. */
    private int mNumChildren = 2;

    /** 
     * Constructor.
     */
    public ProjectPushDownCopyPastStrategy()
    {
        // Nothing to do
    }
    
    /** 
     * Constructor.
     * 
     * @param numChilden maximum number of children to process.
     */
    public ProjectPushDownCopyPastStrategy(int numChilden)
    {
        this();
        mNumChildren = numChilden;
    }

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator)
    {
        LOG.debug("Copying a project past a : " + projectOperator.getChild(0).getID());
        
        List<ProjectOperator> result = new LinkedList<ProjectOperator>();
        
        List<Attribute> usedAttributes = new ArrayList<Attribute>();
        
        // Get used attributes of project
        usedAttributes.addAll(projectOperator.getUsedAttributes());
        // Get used attributes of child
        Operator child = projectOperator.getChild(0);
        usedAttributes.addAll(child.getUsedAttributes());
        
        LOG.debug("Used attributes of project: " + projectOperator.getUsedAttributes());
        LOG.debug("Used attributes of child: " + child.getUsedAttributes());
        LOG.debug("Combined used attributes: " + usedAttributes);
        
        for (int i=0; i<child.getChildCount() && i<mNumChildren; ++i)
        {
            LOG.debug("Looking at grandchild " + i);
            // Get attributes of the grandchild's head
            Operator grandchild = child.getChild(i);
            List<Attribute> producedAttributes = new ArrayList<Attribute>(
                grandchild.getHeading().getAttributes());

            LOG.debug("Produced attributes: " + producedAttributes);
        
            AttributeUtils.removeAllMatching(
                usedAttributes, 
                producedAttributes,
                AttributeMatchMode.NAME_AND_NULL_SOURCE);

	        LOG.debug("Unnecessary attributes: " + producedAttributes);
	        
            if (! producedAttributes.isEmpty())
            {
                LOG.debug("Producing more than we need");
                
                // We are producing more attributes than we need.  We can insert
                // a new PROJECT here
                
                List<Attribute> requiredAttributes = new ArrayList<Attribute>();
                requiredAttributes.addAll(
                    grandchild.getHeading().getAttributes());
                
                AttributeUtils.retainAllMatching(usedAttributes,
                    requiredAttributes,
                    AttributeMatchMode.NAME_AND_NULL_SOURCE);
                
                LOG.debug("Required attributes: " + requiredAttributes);
                
                // If we have no attributes then just choose one, probably
                // doing a project and then throwing away all the data from
                // one side
                if (requiredAttributes.size() == 0)
                {
                    requiredAttributes.add(
                        grandchild.getHeading().getAttributes().get(0));
                }
                
                try
                {
                    ProjectOperator newProjectOperator = new ProjectOperator(
                        grandchild, requiredAttributes);
                    child.setChild(i, newProjectOperator);
                    newProjectOperator.setParent(child);
                    grandchild.setParent(newProjectOperator);
                    
                    projectOperator.update();

                    result.add(newProjectOperator);
                    
                    LOG.debug("adding new project operator");
                }
                catch (LQPException e)
                {
                    // This should not happen, if it does it is an internal 
                    // error
                    throw new DQPInternalException(e);
                }
            }
            else
            {
                LOG.debug("Producing exactly what we need");
            }
        }
        return result;
    }
}
