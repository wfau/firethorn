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

package uk.org.ogsadai.dqp.lqp.optimiser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.Branch;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousMappingException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQuery;

/**
 * Utility functions used by optimisers.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OptimiserUtils
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /**
     * Checks consistency of double linked query plan tree.
     * 
     * @param root
     */
    public static void checkTreeLinks(Operator root)
    {
        for(int i=0; i<root.getChildCount(); i++)
        {
            Operator child = root.getChild(i);
            
            if(child != null)
            {
                if (root != child.getParent())
                    throw new IllegalStateException("We have a bastard: "
                        + child);
                checkTreeLinks(child);
            }
        }
    }
    
    /**
     * Finds all the occurrences of a given operator type.
     * 
     * @param root  
     *            LQP root
     * 
     * @param operatorID
     *            type of operators to be returned
     *    
     * @return a list of operators of the specified type.
     */
    public static List<Operator> findOccurrences(
        Operator root, OperatorID operatorID)
    {
        List<Operator> result = new LinkedList<Operator>();
        findOccurrences(root, operatorID, result);
        return result;
    }

    /**
     * Finds all the occurrences of a given operator type.
     * 
     * @param root  
     *            LQP root
     * 
     * @param operatorID
     *            type of operators to be returned
     *            
     * @param resultList
     *            list to add any operators of the specified type.
     */
    private static void findOccurrences(
        Operator root, OperatorID operatorID, List<Operator> resultList)
    {
        if (root.getID() == operatorID)
        {
            resultList.add(root);
        }
        
        for (int i=0; i<root.getChildCount(); ++i)
        {
            findOccurrences(root.getChild(i), operatorID, resultList);
        }
    }

    /**
     * Walks query plan and extracts leaf operators. Branch setting switches
     * between left deep and right deep search (when right deep search is
     * performed - leafs from right branches will have lower indexes in the list
     * from leafs from the left branches).
     * 
     * @param branchOrder
     *            LEFT or RIGHT branch order
     * @param lqpRoot
     *            query plan root operator
     * @return a list of leaf operators
     */
    public static List<Operator> getLeafs(Branch branchOrder, Operator lqpRoot)
    {
        List<Operator> leafList = new ArrayList<Operator>();
        findLeafs(leafList, lqpRoot, branchOrder);

        return leafList;
    }

    /**
     * Performs the actual leaf search.
     * 
     * @param leafList
     * @param currentOperator
     * @param branchOrder
     */
    private static void findLeafs(List<Operator> leafList,
            Operator currentOperator, Branch branchOrder)    
    {
        if (currentOperator.getChild(0) == null)
        {
            leafList.add(currentOperator);
        }
        else
        {
            if (branchOrder == Branch.RIGHT)
            {
                if (currentOperator.isBinary())
                {
                    findLeafs(leafList, currentOperator.getChild(1),
                        branchOrder);
                }
                findLeafs(leafList, currentOperator.getChild(0), branchOrder);
            }
            else
            {
                findLeafs(leafList, currentOperator.getChild(0), branchOrder);
                if (currentOperator.isBinary())
                {
                    findLeafs(leafList, currentOperator.getChild(1),
                        branchOrder);
                }
            }
        }
    }

    /**
     * Find candidates to be pulled up. Candidate operators are operators of a
     * given type that have no descendants of the same type. Operators annotated
     * with the <code>skipAnnotationKey</code> are ignored.
     * 
     * @param root
     *            LQP root
     * @param operatorID
     *            type of candidate
     * @param skipAnnotationKey
     *            skip annotation key
     * @return a list of operator candidates
     */
    public static List<Operator> findPullUpCandidates(Operator root,
        OperatorID operatorID, String skipAnnotationKey)
    {
        List<Operator> candidateOpList = new LinkedList<Operator>();

        findCandidates(root, operatorID, candidateOpList, skipAnnotationKey);

        return candidateOpList;
    }

    /**
     * Finds candidates to be pulled up. Operators annotated with the
     * <code>skipAnnotationKey</code> are ignored.
     * 
     * @param root
     *            LQP root
     * @param operatorID
     *            type of candidate
     * @param skipAnnotationKey
     *            skip annotation key
     * @param candidateOperators
     */
    private static void findCandidates(
        Operator root, 
        OperatorID operatorID,
        List<Operator> candidateOperators,
        String skipAnnotationKey)
    {
        if (root.getID() == operatorID)
        {
            // if we have already tried to push up this rename - skip it
            if (root.getAnnotation(skipAnnotationKey) != null)
            {
                return;
            }

            // Remove first ancestor already in the list
            removeFirstAncestor(root, operatorID, candidateOperators);

            candidateOperators.add(root);
            findCandidates(
                root.getChild(0), 
                operatorID, 
                candidateOperators, 
                skipAnnotationKey);
        }
        else if (root.isBinary())
        {
            findCandidates(root.getChild(0), operatorID,
                candidateOperators, skipAnnotationKey);
            findCandidates(root.getChild(1), operatorID,
                candidateOperators, skipAnnotationKey);
        }
        else
        {
            Operator child = root.getChild(0);
            if (child != null)
            {
                findCandidates(child, operatorID,
                    candidateOperators, skipAnnotationKey);
            }
        }
    }
    
    /**
     * Removes the first ancestor of the specified type from the of the given
     * list of candidate operators.
     * 
     * @param operator          operator whose ancestors have to be removed
     * @param operatorID        type of operator we are looking for
     * @param candidateOperators list of candidate operators
     */
    private static void removeFirstAncestor(
        Operator operator, 
        OperatorID operatorID, 
        List<Operator> candidateOperators)
    {
        Operator op = operator;
        
        while( op.getParent() != null)
        {
            op = op.getParent();
            if (op.getID() == operatorID)
            {
                if (candidateOperators.contains(op))
                {
                    candidateOperators.remove(op);
                    break;
                }
            }
        }
    }

    /**
     * Inserts new operator above the operator currently in a tree. Update on
     * the parent of the insertOp is performed.
     * 
     * @param treeOp
     *            operator currently in a tree
     * @param insertOp
     *            operator to insert
     * @throws LQPException
     *             when update after insertion fails
     */
    public static void insertAbove(Operator treeOp, Operator insertOp)
	    throws LQPException
    {
	Operator parent = treeOp.getParent();
	parent.replaceChild(treeOp, insertOp);
	insertOp.setChild(0, treeOp);
	parent.update();
    }
    
    /**
     * Pulls <code>operator</code> past its parent.
     * 
     * @param operator
     *            operator to be pulled up
     * @return pulled up operator
     * @throws LQPException
     *             when update fails
     */
    public static Operator pullPastNoChange(Operator operator)
	    throws LQPException
    {
	Operator child = operator.getChild(0);
	Operator parent = operator.getParent();
	Operator parentParent = parent.getParent();

	parentParent.replaceChild(parent, operator);
	operator.replaceChild(child, parent);
	parent.replaceChild(operator, child);

	parentParent.update();
	return operator;
    }

    /**
     * Replaces an operator in a tree with a new operator. The child 
     * relationship,
     * parent->oldOp->children
     * is changed to, 
     * parent->newOp->children
     * The parent relationship is also changed from, 
     * parent<-oldOp<-children
     * to,
     * parent<-newOp<-children
     * 
     * Finally, the children and parent of oldOp are set to null. 
     * 
     * It is up to the caller to make sure that newOp can take as many children 
     * as oldOp. For example, the method will fail if oldOp is a binary 
     * operator and newOp is a unary operator.
     * 
     * @param oldOp
     * @param newOp
     */
    public static void replaceOperator(Operator oldOp, Operator newOp)
    {
        for (int i=0; i<oldOp.getChildCount(); i++) {
            Operator child = oldOp.getChild(i);
            newOp.setChild(i, child);
            child.setParent(newOp);
        }
        oldOp.getParent().replaceChild(oldOp, newOp);
        oldOp.disconnect();
    }

    /**
     * Replaces a branch of a tree with a new branch.
     * 
     * @param oldOp
     *            root of the branch to be removed
     * @param newOp
     *            root of the branch to be inserted
     */
    public static void replaceBranch(Operator oldOp, Operator newOp)
    {
        oldOp.getParent().replaceChild(oldOp, newOp);
    }
    
    /**
     * Removes an operator with one child.
     * 
     * parent -> operator -> child
     * 
     * will be replaced with
     * 
     * parent -> child
     * 
     * @param op
     * @throws LQPException
     */
    public static void removeOneChildOperator(Operator op) throws LQPException {
        if (op.getChildCount() > 1) {
            throw new LQPException("This method cannot remove an operator with more than one child.");
        }
        Operator parent = op.getParent();
        Operator child = op.getChild(0);
        parent.replaceChild(op, child);
        child.setParent(parent);
        op.disconnect();
    }
    
    /**
     * Check if all attributes in a list are in fact the same attribute.
     * 
     * @param attributes
     *            list of attributes
     * @return <code>true</code> if all attributes are equal to each other
     */
    public static boolean checkIfAllSame(List<Attribute> attributes)
    {
        Attribute previous = attributes.get(0);
        for (int i = 0; i < attributes.size(); i++)
        {
            Attribute current = attributes.get(i);
            if (!previous.equals(current))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check if there are any attributes with the same name and source in the 
     * aggregate of the two lists. Note that besides checking if any attribute 
     * from listA clashes with any attribute with listB and vise versa, this 
     * method also checks if there is a clash within the individual lists 
     * themselves.
     * 
     * @param listA
     *      first list
     * @param listB
     *      second list
     * @return
     *      <code>true</code> if at least one attribute exists in both lists
     */
    public static boolean attributeClash(List<Attribute> listA, List<Attribute> listB)
    {
        List<Attribute> allAttrs = new ArrayList<Attribute>(listA);
        allAttrs.addAll(listB);
        
        for (Attribute cAttrib : allAttrs)
        {
            for (Attribute mAttrib : allAttrs)
            {
                if (cAttrib != mAttrib)  // Allow matches with self
                {
                    if (cAttrib.equals(
                            mAttrib,
                            AttributeMatchMode.NAME_AND_NULL_SOURCE))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Tests to see if the given portion of query plan can be ordered 
     * according to the given attribute.
     * 
     * @param root  root of the portion of query plan to be checked
     * @param attr  attribute to order by
     * 
     * @return <tt>true</tt> if the query plan could be ordered with respect to
     *         the given attribute, <tt>false</tt> if it cannot.
     */
    public static boolean canQueryPlanBeOrdered(Operator root, Attribute attr)
    {
        // If it is already annotated as ordered then the result depends on
        // whether it is the same attribute
        Object orderByAttribute = 
            root.getAnnotation(Annotation.ORDERED_BY_ATTR);
        if (orderByAttribute != null)
        {
            
            return  attr.equals(((Attribute)orderByAttribute));
        }
        
        // It is not already ordered, the query plan could be ordered if
        // it is conceptually a table scan.
        return isConceptuallyTableScan(root);
    }
    
    
    /**
     * Checks if an operator is "conceptually a scan". A "conceptually scan" 
     * operator in the context of this optimiser class is an operator that is a 
     * a table scan or that conforms to the pattern <code>(E|R)*TS</code> 
     * which means exchange or rename operators occurring any number of times 
     * followed by a table scan.
     * 
     * @param op   operator
     * @return    <code>true</code> if operator is conceptually a table scan, 
     *            <code>false</code> otherwise.
     */
    public static boolean isConceptuallyTableScan(Operator op) 
    {
        if (op.getID() == OperatorID.TABLE_SCAN) 
        {
            return true;
        }
        else if ((op.getID() == OperatorID.RENAME || 
                  op.getID() == OperatorID.EXCHANGE) &&
                  op.getChildCount() == 1) 
        {
            return isConceptuallyTableScan(op.getChild(0));
        }
        else 
        {
            return false;
        }
    }
    
    /**
     * Alters the given portion of query plan such that the output tuple
     * stream is ordered with respect to the given attribute.
     * <p>
     * Before calling this method <tt>canQueryPlanBeOrdered</tt> must be called
     * to check if the portion of query plan can be altered.
     * 
     * @param root  root of the portion of query plan to be checked
     * @param attr  attribute to order by
     * 
     * @throws AmbiguousMappingException
     * @throws AmbiguousAttributeException
     */
    public static void orderQueryPlan(Operator root, Attribute attr)
        throws AmbiguousMappingException, AmbiguousAttributeException
    {
        Operator tmpOp = root;
        Attribute sortAttribute = attr;
        while(tmpOp.getID() != OperatorID.TABLE_SCAN) 
        {
            tmpOp.addAnnotation(Annotation.ORDERED_BY_ATTR, attr);
            if (tmpOp.getID() == OperatorID.RENAME) 
            {
                RenameOperator rename = (RenameOperator)tmpOp;
                sortAttribute = 
                    rename.getRenameMap().getOriginalAttribute(sortAttribute);
            }
            tmpOp = tmpOp.getChild(0);
        }
        
        tmpOp.addAnnotation(Annotation.ORDERED_BY_ATTR, attr);
        TableScanQuery tsQuery = ((TableScanOperator)tmpOp).getQuery();
        tsQuery.setSort(sortAttribute);
        
    }
    
    public static void copyPlacementAnnotations(Operator src, Operator dest)
    {
        copyAnnotation(Annotation.DATA_NODE, src, dest);
        copyAnnotation(Annotation.EVALUATION_NODE, src, dest);
        copyAnnotation(Annotation.PARTITION, src, dest);
    }
    
    public static void copyAnnotation(String key, Operator src, Operator dest)
    {
        Object value = src.getAnnotation(key);
        if (value != null) dest.addAnnotation(key, value);
    }
    
}
