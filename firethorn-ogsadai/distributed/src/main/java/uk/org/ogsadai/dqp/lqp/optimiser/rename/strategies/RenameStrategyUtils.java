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

package uk.org.ogsadai.dqp.lqp.optimiser.rename.strategies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;

/**
 * Methods shared by RENAME pull up strategies.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RenameStrategyUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Update RENAME operator's parent attributes based on rename map.
     * 
     * @param renameOperator
     *      operator defining the rename map
     * @throws LQPException
     */
    protected static void renameParentAttributes(
        RenameOperator renameOperator) throws LQPException
    {
        Operator renameParent = renameOperator.getParent();
        Set<Attribute> usedAttrList = renameParent.getUsedAttributes();

        RenameMap renToOrigMap = 
            renameOperator.getRenameMap().getRenamedToOriginalMap(usedAttrList);

        renameParent.renameUsedAttributes(renToOrigMap);
    }

    /**
     * Pulls RENAME operator past unary operator.
     * 
     * @param renameOperator
     *            operator to pull
     * @return pulled up operator
     * @throws LQPException
     *             if update on pulled up operator's parent fails
     */
    protected static RenameOperator pullPastUnary(RenameOperator renameOperator)
        throws LQPException
    {
        Operator renamePartent = renameOperator.getParent();

        Operator renameParentParent = renamePartent.getParent();
        Operator renameChild = renameOperator.getChild(0);

        renamePartent.replaceChild(renameOperator, renameChild);
        RenameMap map = renameOperator.getRenameMap();
        map.setAttributeMatchMode(AttributeMatchMode.NO_TYPE);
        renamePartent.renameUsedAttributes(
                map.getRenamedToOriginalMap(renamePartent.getUsedAttributes()));
        renamePartent.update();

        List<Attribute> updatedHeadingAttr =
            new LinkedList<Attribute>(
                renamePartent.getHeading().getAttributes());

        // now if rename parent is a project we need something special it may
        // be the case
        if(renamePartent.getID() == OperatorID.PROJECT)
        {
            ProjectOperator project = (ProjectOperator) renamePartent;
            
            AttributeUtils.removeAllMatching(
                project.getDerivedAttributes(),
                updatedHeadingAttr,
                AttributeMatchMode.NAME_AND_SOURCE);
        }
        
        RenameMap origToRenMap = 
            renameOperator.getRenameMap().getOriginalToRenamedMap(
                updatedHeadingAttr);

        RenameMap newRenameMap = new SimpleRenameMap();
        for (Attribute attr : renamePartent.getHeading().getAttributes())
        {
            Attribute renamedAttr;
            if ((renamedAttr = origToRenMap.getRenamedAttribute(attr)) != null)
            {
                newRenameMap.add(attr, renamedAttr);
            }
            else
            {
                newRenameMap.add(attr, attr);
            }
        }
        
        if (newRenameMap.getOriginalAttributeList().equals(
            newRenameMap.getRenamedAttributeList()))
        {
            // redundant, reconnect and return null
            renameOperator.disconnect();

            return null;
        }
        else
        {
            RenameOperator newRenameOperator = new RenameOperator(newRenameMap);
            
            newRenameOperator.setChild(0, renamePartent);
            renameParentParent.replaceChild(renamePartent, newRenameOperator);
            renameOperator.disconnect();            
            renameParentParent.update();
            
            return newRenameOperator;
        }
    }

    /**
     * Pulls RENAME operator past binary operator.
     * 
     * @param renameOperator
     *            operator to be pulled up
     * @return pulled up operator
     * @throws LQPException
     *             if update on pulled up operator's parent fails
     */
    protected static RenameOperator pullPastBinary(RenameOperator renameOperator)
        throws LQPException
    {
        Operator renamePartent = renameOperator.getParent();
        Operator renameParentParent = renamePartent.getParent();
        Operator renameChild = renameOperator.getChild(0);

        int renameIndex = ((BinaryOperator) renamePartent)
            .getChildIndex(renameOperator);

        // build new rename map
        List<Attribute> origAttrList;
        List<Attribute> renamedAttrList;
        if (renameIndex == 0)
        {
            origAttrList = new ArrayList<Attribute>(renameOperator
                .getRenameMap().getOriginalAttributeList());
            renamedAttrList = new ArrayList<Attribute>(renameOperator
                .getRenameMap().getRenamedAttributeList());

            List<Attribute> rightChildHeadAttr = renamePartent.getChild(1)
                .getHeading().getAttributes();
            origAttrList.addAll(rightChildHeadAttr);
            renamedAttrList.addAll(rightChildHeadAttr);
        }
        else if (renameIndex == 1)
        {
            // This is different from renameIndex == 0 to maintain ordering of
            // heading attributes.
            List<Attribute> leftChildHeadAttr = renamePartent.getChild(0)
                .getHeading().getAttributes();
            origAttrList = new ArrayList<Attribute>(leftChildHeadAttr);
            renamedAttrList = new ArrayList<Attribute>(leftChildHeadAttr);

            origAttrList.addAll(renameOperator.getRenameMap()
                .getOriginalAttributeList());
            renamedAttrList.addAll(renameOperator.getRenameMap()
                .getRenamedAttributeList());
        }
        else
        {
            throw new IllegalStateException(
                "Rename is not a child of its parent?");
        }

        RenameOperator newRenameOperator = new RenameOperator(
            new SimpleRenameMap(origAttrList, renamedAttrList));

        renamePartent.replaceChild(renameOperator, renameChild);
        renamePartent.update();

        newRenameOperator.setChild(0, renamePartent);
        renameParentParent.replaceChild(renamePartent, newRenameOperator);
        renameOperator.disconnect();
        renameParentParent.update();

        return newRenameOperator;
    }

    /**
     * Pull past any operator without change.
     * 
     * @param renameOperator
     *            operator to be pulled up
     * @return pulled up operator
     * @throws LQPException
     *             if update on pulled up operator's parent fails
     */
    protected static RenameOperator pullPastNoChange(
        RenameOperator renameOperator) throws LQPException
    {
        Operator child = renameOperator.getChild(0);
        Operator parent = renameOperator.getParent();
        Operator parentParent = parent.getParent();

        parentParent.replaceChild(parent, renameOperator);
        renameOperator.replaceChild(child, parent);
        parent.replaceChild(renameOperator, child);

        parentParent.update();
        return renameOperator;
    }
}
