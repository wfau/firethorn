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

package uk.org.ogsadai.dqp.lqp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.join.JoinGroup;

/**
 * Extracts join groups from a logical query plan.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class JoinGroupExtractor 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /**
     * Returns a list of join groups for a query plan. Joins within a join group
     * can be reordered.
     * 
     * @param lqpRoot
     *            query plan root operator
     *            
     * @return a list of join groups.  Groups neaer the leaves of the query
     *  plan tree will appear first in the list.
     */
    public static List<JoinGroup> getJoinGroups(Operator lqpRoot)
    {
        List<JoinGroup> joinGroupList = new ArrayList<JoinGroup>();

        buildJoinGroups(lqpRoot, joinGroupList);
        Collections.reverse(joinGroupList);
        return joinGroupList;
    }

    /**
     * Builds actual join groups.
     * 
     * @param lqpRoot
     * @param joinGroupList
     */
    private static void buildJoinGroups(Operator lqpRoot,
            List<JoinGroup> joinGroupList)    
    {
        if (lqpRoot.getID() == OperatorID.SELECT
            || lqpRoot.getID() == OperatorID.PRODUCT
            || lqpRoot.getID() == OperatorID.SEMI_JOIN
            || lqpRoot.getID() == OperatorID.INNER_THETA_JOIN
            || (lqpRoot.getID() == OperatorID.APPLY &&
                ((ApplyOperator) lqpRoot).getOperator().getID() 
                    == OperatorID.SEMI_JOIN))
        {
            // Create and build a new join group
            JoinGroup joinGroup = new JoinGroup(lqpRoot);
            buildGroup(lqpRoot, joinGroup);

            // If there is at least one join like operator in the group - add it
            // to the list
            if (joinGroup.hasJoin())
            {
                joinGroupList.add(joinGroup);
            }
            
            // Continue extracting join groups
            for (Operator op : joinGroup.getRelations())
            {
                continueWalking(op, joinGroupList);
            }
        }
        else
        {
            continueWalking(lqpRoot, joinGroupList);
        }
    }

    /**
     * Continues building a join group.
     * 
     * @param operator
     * @param joinGroupList
     */
    private static void continueWalking(Operator operator,
        List<JoinGroup> joinGroupList)
    {
        // if not a leaf
        if (operator.getChild(0) != null)
        {
            buildJoinGroups(operator.getChild(0), joinGroupList);
            if (operator.isBinary())
            {
                buildJoinGroups(operator.getChild(1), joinGroupList);
            }
        }
    }

    /**
     * Build a join group.
     * 
     * @param node
     *            join group root node
     * @param joinGroup
     */
    private static void buildGroup(Operator node, JoinGroup joinGroup)
    {
        if (node.getID() == OperatorID.SELECT)
        {
            // Add select predicates only when SELECT operator is an ancestor of
            // the first join operator in a join group or is between Product or
            // Join

            boolean parentOK = (node.getParent() instanceof ProductOperator)
                || (node.getParent() instanceof InnerThetaJoinOperator) ? true
                : false;

            boolean childOK = (node.getChild(0) instanceof ProductOperator)
                || (node.getChild(0) instanceof InnerThetaJoinOperator) ? true
                : false;

            if (!joinGroup.hasJoin() || (parentOK && childOK))
            {
                joinGroup.addPredicate(((SelectOperator) node).getPredicate());
                buildGroup(node.getChild(0), joinGroup);
            }
            else
            {
                joinGroup.addRelation(node);
            }
        }
        else if (node.getID() == OperatorID.INNER_THETA_JOIN)
        {
            joinGroup.addPredicate(((InnerThetaJoinOperator) node)
                .getPredicate());
            joinGroup.setHasJoin();
            buildGroup(node.getChild(0), joinGroup);
            buildGroup(node.getChild(1), joinGroup);
        }
        else if (node.getID() == OperatorID.PRODUCT)
        {
            joinGroup.setHasJoin();
            buildGroup(node.getChild(0), joinGroup);
            buildGroup(node.getChild(1), joinGroup);
        }
        else if (node.getID() == OperatorID.SEMI_JOIN)
        {
            SemiJoinOperator semiJoinOp = (SemiJoinOperator) node;

            List<Attribute> lhsAttributesUsedInPredicate = 
                new LinkedList<Attribute>(
                    semiJoinOp.getChild(0).getHeading().getAttributes());
            List<Attribute> rhsAttributesUsedInPredicate = 
                new LinkedList<Attribute>(
                    semiJoinOp.getChild(0).getHeading().getAttributes());
            
            AttributeUtils.retainAllMatching(
                semiJoinOp.getPredicate().getAttributes(),
                lhsAttributesUsedInPredicate,
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
            
            AttributeUtils.retainAllMatching(
                semiJoinOp.getPredicate().getAttributes(),
                rhsAttributesUsedInPredicate,
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
            
            joinGroup.setHasJoin();
            joinGroup.addSemiJoinPredicate(
                semiJoinOp.getPredicate(),   
                lhsAttributesUsedInPredicate,
                semiJoinOp);
            
            // Build only from the LHS.
            buildGroup(node.getChild(0), joinGroup);
        }
        else if (node.getID() == OperatorID.APPLY)
        {
            ApplyOperator applyOp = (ApplyOperator) node;
            
            if (applyOp.getOperator().getID() == OperatorID.SEMI_JOIN)
            {
                SemiJoinOperator semiJoinOp = 
                    (SemiJoinOperator) applyOp.getOperator();
                
                List<Attribute> predicateAndBoundAttributes = 
                    new LinkedList<Attribute>(
                        semiJoinOp.getPredicate().getAttributes());
                predicateAndBoundAttributes.addAll(
                    applyOp.getAttributesToBind());
                        
                List<Attribute> lhsAttributesUsedInPredicate = 
                    new LinkedList<Attribute>(
                        applyOp.getChild(0).getHeading().getAttributes());
                AttributeUtils.retainAllMatching(
                    predicateAndBoundAttributes,
                    lhsAttributesUsedInPredicate,
                    AttributeMatchMode.NAME_AND_NULL_SOURCE);

                joinGroup.setHasJoin();
                joinGroup.addAppliedSemiJoinPredicate(
                    semiJoinOp.getPredicate(),
                    lhsAttributesUsedInPredicate,
                    applyOp);
                
                // Build only from the LHS.
                buildGroup(node.getChild(0), joinGroup);
            }
        }
        else
        {
            joinGroup.addRelation(node);
        }
    }

}
