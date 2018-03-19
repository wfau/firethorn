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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownStrategy;
import uk.org.ogsadai.resource.dataresource.dqp.DQPInternalException;

/**
 * Strategy for pushing PROJECT operator down past a RENAME operator.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownPastRenameStrategy 
    implements ProjectPushDownStrategy
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ProjectPushDownPastRenameStrategy.class);

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator)
    {
        List<ProjectOperator> result = new LinkedList<ProjectOperator>();
        
        // Get used attributes of child
        Operator child = projectOperator.getChild(0);
        if (!(child instanceof RenameOperator))
        {
            return result;
        }
        RenameOperator renameOp = (RenameOperator) child;
        
        // Get used attributes of project
        List<Attribute> usedAttributes = new LinkedList<Attribute>();
        usedAttributes.addAll(projectOperator.getUsedAttributes());
        
        LOG.debug("PushDownProject: usedAttributes: " + usedAttributes);

        // Need to find the redundant attributes of the rename
        List<Attribute> redundantAttributes = new LinkedList<Attribute>();
        
        redundantAttributes.addAll(renameOp.getHeading().getAttributes());
        
        AttributeUtils.removeAllMatching(usedAttributes, redundantAttributes,
        AttributeMatchMode.NAME_AND_NULL_SOURCE);
        
        LOG.debug("PushDownProject: redundantAttributes: " + redundantAttributes);
        
        for( Attribute  attr: redundantAttributes)
        {
            renameOp.getRenameMap().removeRenamedAttribute(attr);
        }
        
        // Now we have to map each of the attributes through the RENAME op
        Set<Attribute> finalAttributes = new HashSet<Attribute>();
        for( Attribute attr: usedAttributes)
        {
            try
            {
                finalAttributes.add(
                    renameOp.getRenameMap().getOriginalAttribute(attr));
            }
            catch (AmbiguousMappingException e)
            {
                LOG.debug("ACH: Ally said this could not happen. It has! AmbiguousMappingException");
            }
            catch (AmbiguousAttributeException e)
            {
                LOG.debug("ACH: Ally said this could not happen. It has! AmbiguousAttributeException");
            }
        }

        LOG.debug("PushDownProject: final attributes: " + finalAttributes);

        try
        {
            Operator grandchild = child.getChild(0);
            ProjectOperator newProjectOperator = new ProjectOperator(
                grandchild, finalAttributes);
            child.setChild(0, newProjectOperator);
            newProjectOperator.setParent(child);
            grandchild.setParent(newProjectOperator);
            
            projectOperator.update();
            result.add(newProjectOperator);
        }
        catch (LQPException e)
        {
            // This should not happen, if it does it is an internal error
            throw new DQPInternalException(e);
        }
        
        return result;
    }
}
