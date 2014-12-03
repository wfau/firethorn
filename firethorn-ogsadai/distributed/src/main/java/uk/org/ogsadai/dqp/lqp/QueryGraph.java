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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.lqp.PredicateSources.MatchType;
import uk.org.ogsadai.dqp.lqp.exceptions.AmbiguousAttributeException;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.exceptions.UnsatisfiableBindingPatternException;
import uk.org.ogsadai.dqp.lqp.exceptions.UnsupportedBindingPatternsException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.NilOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProductOperator;
import uk.org.ogsadai.dqp.lqp.operators.ScanBindApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownOptimiser;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Query graph.
 * 
 * TODO: add more comments
 * 
 * @author The OGSA-DAI Project Team
 */
public class QueryGraph
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Internal graph representation. */
    private Graph<Operator, Predicate> mGraph = 
        new SparseMultigraph<Operator, Predicate>();

    /**
     * Add nodes to the query graph.
     * 
     * @param operatorList
     *            a list of operators
     */
    public void addOperators(List<Operator> operatorList)
    {
        for (Operator o : operatorList)
        {
            mGraph.addVertex(o);
        }
    }

    /**
     * Add predicates (edges) to a query graph. Some predicates (for example
     * conjunctions) will be of no use in the query graph and will be returned.
     * 
     * @param predicateList
     *            a list of predicates to be added to the query plan
     * @return a list of rejected predicates
     * @throws UnsatisfiableBindingPatternException
     *             if a binding predicate can not be satisfied
     * @throws UnsupportedBindingPatternsException
     *             when binding value is derived from more than one source
     * @throws AmbiguousAttributeException
     * @throws AttributeNotFoundException
     */
    public List<Predicate> addPredicates(List<Predicate> predicateList)
        throws UnsatisfiableBindingPatternException,
        UnsupportedBindingPatternsException, AmbiguousAttributeException,
        AttributeNotFoundException
    {
        List<Predicate> rejectedPredicates = new LinkedList<Predicate>();
    
        for (Predicate p : predicateList)
        {
            // if binding predicate
            if (p instanceof BindingPredicate)
            {
                BindingPredicate bp = (BindingPredicate) p;
                PredicateSources ps = 
                    PredicateSources.getMatch(p, mGraph.getVertices());
    
                if (ps.getMatchType() == MatchType.AMBIGUOUS)
                {
                    throw new AmbiguousAttributeException(ps
                        .getAmbiguousAttribute());
                }
                else if (ps.getMatchType() == MatchType.NOT_FOUND)
                {
                    throw new AttributeNotFoundException(ps
                        .getMissingAttribute());
                }
                else if (ps.getMatchType() == MatchType.BOTH_SIDES_N_SOURCES)
                {
                    throw new UnsupportedBindingPatternsException(
                        "Value for a bingding pattern needs to be a "
                            + "function of attributes from a single source.");
                }
                else if (ps.getMatchType() == MatchType.BOTH_SIDES_SAME_SOURCE)
                {
                    throw new UnsatisfiableBindingPatternException();
                }
                else if (ps.getMatchType() == MatchType.CONST_1_SOURCE)
                {
                    // we have a constant expression
                    Operator op = ps.getSource();
                    mGraph.addEdge(p, op, op, EdgeType.DIRECTED);
    
                    continue;
                }
                else
                {
                    // check if satisfiable and add
                    Operator lhsSource = ps.getLHSSource();
                    Operator rhsSource = ps.getRHSSource();
    
                    if (lhsSource.getHeading().contains(bp.getBoundAttribute()))
                    {
                        if (lhsSource != rhsSource)
                            for (Predicate pred : mGraph.getOutEdges(lhsSource))
                                if (mGraph.getEdgeType(pred) == EdgeType.DIRECTED
                                    && mGraph.getOpposite(lhsSource, pred) == rhsSource)
                                    throw new UnsatisfiableBindingPatternException();
    
                        mGraph.addEdge(p, rhsSource, lhsSource,
                            EdgeType.DIRECTED);
                    }
                    else
                    {
                        if (lhsSource != rhsSource)
                            for (Predicate pred : mGraph.getOutEdges(rhsSource))
                                if (mGraph.getEdgeType(pred) == EdgeType.DIRECTED
                                    && mGraph.getOpposite(rhsSource, pred) == lhsSource)
                                    throw new UnsatisfiableBindingPatternException();
    
                        mGraph.addEdge(p, lhsSource, rhsSource,
                            EdgeType.DIRECTED);
                    }
    
                    continue;
                }
            }
            else // Not a binding predicate
            {
                // if all on one relation
                Operator operator;
                if ((operator = findOperatorWillAllSelectAttributes(p)) != null)
                {
                    mGraph.addEdge(p, operator, operator, EdgeType.UNDIRECTED);
                    continue;
                }
                // if equijoin
                else if (p.isAttrEqAttr())
                {
                    PredicateSources ps = PredicateSources.getMatch(p, mGraph
                        .getVertices());
    
                    if (ps.getMatchType() == MatchType.BOTH_SIDES_1_SOURCE)
                    {
                        mGraph.addEdge(p, ps.getLHSSource(), ps.getRHSSource(),
                            EdgeType.UNDIRECTED);
    
                        continue;
                    }
                }
            }
            rejectedPredicates.add(p);
        }
        return rejectedPredicates;
    }

    /**
     * Finds an operator which provides all attributes used by a predicate.
     * 
     * @param predicate
     * @return an operator providing all attributes used by a predicate or
     *         <code>null</code>
     */
    private Operator findOperatorWillAllSelectAttributes(Predicate predicate)
    {
        Operator lastMatch = null;
        int matches = 0;
        for (Operator o : mGraph.getVertices())
        {
            if (o.getHeading()
                .containsAllUnambiguous(predicate.getAttributes()))
            {
            lastMatch = o;
            matches++;
            }
        }

        if (matches == 1)
            return lastMatch;
        else
            return null;
    }

    /**
     * Returns a query plan matching the query graph. This operation resets the
     * state of the query graph to an empty graph.
     * 
     * @return root operator of a logical query plan
     * @throws LQPException
     */
    public Operator getQueryPlan() throws LQPException
    {
        Operator currentRoot = null;
        // process self loops
        while (hasSelfLoops())
        {
            Predicate selfLoop = getNextSelfLoop();
            Operator newOp = null;
            Operator firstOp = mGraph.getEndpoints(selfLoop).getFirst();
            if (mGraph.getEdgeType(selfLoop) == EdgeType.DIRECTED)
            {
                // try to push it down - deals with renames
                NilOperator tempNil = new NilOperator();
                SelectOperator tempSelect = new SelectOperator(firstOp,
                    selfLoop);
                tempNil.setChild(0, tempSelect);

                SelectPushDownOptimiser opt = new SelectPushDownOptimiser();
                try
                {
                    opt.optimise(tempNil, null, null, null);
                    TableScanOperator ts = (TableScanOperator) OptimiserUtils
                        .findOccurrences(tempNil, OperatorID.TABLE_SCAN)
                        .get(0);

                    SelectOperator pushedDownSelect = tempSelect;

                    Predicate pred = pushedDownSelect.getPredicate();
                    pushedDownSelect.getParent().replaceChild(pushedDownSelect,
                        pushedDownSelect.getChild(0));

                    ts.getQuery().addPredicate(pred);

                    pushedDownSelect.disconnect();

                    tempNil.getChild(0).update();
                    newOp = tempNil.getChild(0);

                    tempNil.disconnect();
                }
                catch (LQPException e)
                {
                    throw new IllegalStateException(e);
                }
                // rebuildGraph(null, Arrays.asList(new Predicate[] { selfLoop
                // }));
                rebuildGraph(newOp, Arrays.asList(new Predicate[] { selfLoop }));
            }
            else
            {
                SelectOperator selectOperator = new SelectOperator(firstOp,
                    selfLoop);

                rebuildGraph(selectOperator, Arrays
                    .asList(new Predicate[] { selfLoop }));
            }
        }

        // Prioritise binding patterns that have undirected edges as well
        // while has directed edges between the same two operators. In such 
        // cases the directed edge must be processed first.
        Predicate pred;
        while ((pred = getCandidateProviderWithUndirectedEdge() ) != null )
        {
            currentRoot = processDirectedEdge(
                mGraph.getSource(pred), 
                mGraph.getDest(pred));
        }
        
        // while has undirected edges
        while (hasUndirectedEdges())
        {
            Predicate nextPredicate = getNextPredicate();
            Operator sourceOp = mGraph.getEndpoints(nextPredicate).getFirst();
            Operator destOp = mGraph.getEndpoints(nextPredicate).getSecond();

            List<Predicate> predicateEdgesToFold = new ArrayList<Predicate>();
            for (Predicate p : getOutgoingEdges(sourceOp))
            {
                if (destOp == getOpposite(sourceOp, p))
                {
                    predicateEdgesToFold.add(p);
                }
            }
    
            // we may need to fold undirected edges too
            InnerThetaJoinOperator joinOperator = new InnerThetaJoinOperator(
                new CommonPredicate(predicateEdgesToFold));
            // turn to apply and add bound predicates explicitly - watch the
            // ordering
            joinOperator.setChild(0, sourceOp);
            joinOperator.setChild(1, destOp);
            joinOperator.update();
    
            try
            {
                rebuildGraph(joinOperator, predicateEdgesToFold);
            }
            catch (UnsatisfiableBindingPatternException e)
            {
                throw new LQPException(e);
            }
            currentRoot = joinOperator;
        }
        
        // while has directed edges
        while (hasDirectedEdges())
        {
            currentRoot = processDirectedEdge(getCandidateProvider(), null);
        }
    
        // while has nodes
        while (hasNodesToJoin())
        {
            Operator candidate;
            if (currentRoot == null)
                candidate = getNextNode(null);
            else
                candidate = currentRoot;
    
            Operator destination = null;
    
            if (candidate == null)
                candidate = getNextNode(null);
    
            destination = getNextNode(candidate);
    
            ProductOperator productOperator = new ProductOperator(candidate,
                destination);
            productOperator.update();
    
            currentRoot = productOperator;
            try
            {
                rebuildGraph(productOperator, new LinkedList<Predicate>());
            }
            catch (UnsatisfiableBindingPatternException e)
            {
                throw new LQPException(e);
            }
        }
    
        Operator returnOperator = (currentRoot == null) ? getNextNode(null)
            : currentRoot;
        // Clear query graph
        mGraph = new SparseMultigraph<Operator, Predicate>();
    
        return returnOperator;
    }

    /**
     * Processed a directed edge that exits the given candidate operator.
     * 
     * @param candidate   candidate operator
     * @param destination destination operator, can be <tt>null</null>
     * 
     * @return            the new graph root
     * 
     * @throws LQPException if an error occurs.
     */
    private Operator processDirectedEdge(
        Operator candidate,
        Operator destination) throws LQPException
    {
        List<Predicate> predicateEdgesToFold = new ArrayList<Predicate>();

        for (Predicate p : getOutgoingEdges(candidate))
        {
            if (destination == null)
            {
                destination = getDestination(p);
                predicateEdgesToFold.add(p);
            }
            else
            {
                if (getOpposite(candidate, p) == destination)
                {
                    predicateEdgesToFold.add(p);
                }
            }
        }
        List<BindingPredicate> bindingPredicates = 
            new ArrayList<BindingPredicate>();
        List<Predicate> joinPredicates = new ArrayList<Predicate>();

        for (Predicate p : predicateEdgesToFold)
            if (p instanceof BindingPredicate)
                bindingPredicates.add((BindingPredicate) p);
            else
                joinPredicates.add(p);

        Operator joinOperator;
        if (joinPredicates.size() > 0)
        {
            joinOperator = new InnerThetaJoinOperator(new CommonPredicate(
                joinPredicates));
        }
        else
        {
            joinOperator = new ProductOperator();
        }

        ScanBindApplyOperator applyOperator = new ScanBindApplyOperator(
            joinOperator, bindingPredicates);

        applyOperator.setChild(0, candidate);
        applyOperator.setChild(1, destination);
        applyOperator.update();

        try
        {
            rebuildGraph(applyOperator, predicateEdgesToFold);
        }
        catch (UnsatisfiableBindingPatternException e)
        {
            throw new LQPException(e);
        }
        return applyOperator;
    }
    
    
    /**
     * Rebuilds graph.
     * 
     * @param foldedOperator
     *            operator created by folding all edges between two nodes
     * @param edgesToFold
     *            a list of folded edges
     * @throws UnsatisfiableBindingPatternException
     *             when binding pattern can not be satisfied
     * @throws AttributeNotFoundException
     * @throws AmbiguousAttributeException
     * @throws UnsupportedBindingPatternsException
     */
    private void rebuildGraph(Operator foldedOperator,
        List<Predicate> edgesToFold)
        throws UnsatisfiableBindingPatternException,
        UnsupportedBindingPatternsException, AmbiguousAttributeException,
        AttributeNotFoundException
    {
        List<Predicate> predicateList = new LinkedList<Predicate>(mGraph
            .getEdges());
        List<Operator> operatorList = new LinkedList<Operator>(mGraph
            .getVertices());
    
        if (foldedOperator != null)
        {
            for (int i = 0; i < foldedOperator.getChildCount(); i++)
                operatorList.remove(foldedOperator.getChild(i));
            operatorList.add(foldedOperator);
        }
        predicateList.removeAll(edgesToFold);
    
        mGraph = new SparseMultigraph<Operator, Predicate>();
        for (Operator o : operatorList)
            mGraph.addVertex(o);
    
        addPredicates(predicateList);
    }

    /**
     * Returns internal representation of a graph. Use for testing only.
     * 
     * @return internal graph representation
     */
    Graph<Operator, Predicate> getGraph()
    {
        return mGraph;
    }

    /**
     * Gets random candidate node-operator that provides values for binding.
     * 
     * @return provider node-operator
     */
    private Operator getCandidateProvider()
    {
        for (Operator o : mGraph.getVertices())
        {
            int outEdges = 0;
            int inEdges = 0;
    
            for (Predicate p : mGraph.getInEdges(o))
                if (mGraph.getEdgeType(p) == EdgeType.DIRECTED)
                    inEdges++;
            for (Predicate p : mGraph.getOutEdges(o))
                if (mGraph.getEdgeType(p) == EdgeType.DIRECTED)
                    outEdges++;
    
            if ((inEdges == 0) && (outEdges > 0))
            return o;
        }
    
        return null;
    }

    
    private Predicate getCandidateProviderWithUndirectedEdge()
    {
        for (Operator o : mGraph.getVertices())
        {
            int outEdges = 0;
            int inEdges = 0;
            boolean hasUndirectedEdge = false;
            Predicate predicate = null;
            
            for (Predicate p : mGraph.getInEdges(o))
            {
                if (mGraph.getEdgeType(p) == EdgeType.DIRECTED) 
                    inEdges++;
            }
            for (Predicate p : mGraph.getOutEdges(o))
            {
                if (mGraph.getEdgeType(p) == EdgeType.DIRECTED)
                {
                    outEdges++;

                    // Is there an undirected edge between the same two 
                    // operators?
                    Operator dest = mGraph.getDest(p);
                    for (Predicate pp : mGraph.getOutEdges(o))
                    {
                        if (mGraph.getEdgeType(pp) == EdgeType.UNDIRECTED &&
                            mGraph.getEndpoints(pp).contains(dest))
                        {
                            hasUndirectedEdge = true;
                            predicate = p;
                        }
                    }
                }
            }

            if ((inEdges == 0) && (outEdges > 0))
            {
                
                return predicate;
            }
        }
    
        return null;
    }
    
    
    /**
     * Gets outgoing edges for an operator.
     * 
     * @param operator
     *            node-operator
     * @return a collection of outgoing edges
     */
    private Collection<Predicate> getOutgoingEdges(Operator operator)
    {
        return new LinkedList<Predicate>(mGraph.getOutEdges(operator));
    }

    /**
     * Gets destination of the directed edge-predicate.
     * 
     * @param predicate
     *            directed edge-predicate
     * @return destination node-operator
     */
    private Operator getDestination(Predicate predicate)
    {
        return mGraph.getDest(predicate);
    }

    /**
     * Gets node-operator on the other side of the edge-predicate.
     * 
     * @param operator
     *            node-operator
     * @param predicate
     *            edge-predicate
     * @return opposite node-operator
     */
    private Operator getOpposite(Operator operator, Predicate predicate)
    {
        return mGraph.getOpposite(operator, predicate);
    }

    /**
     * Checks if query graph has directed edges.
     * 
     * @return <code>true</code> if query graph has directed edges.
     */
    private boolean hasDirectedEdges()
    {
        return mGraph.getEdgeCount(EdgeType.DIRECTED) > 0;
    }

    private boolean hasSelfLoops()
    {
        for (Predicate p : mGraph.getEdges())
        {
            Operator firstOp = mGraph.getEndpoints(p).getFirst();
            Operator secondOp = mGraph.getEndpoints(p).getSecond();
    
            if (firstOp == secondOp)
                return true;
        }
        return false;
    }

    private boolean hasUndirectedEdges()
    {
        return mGraph.getEdgeCount(EdgeType.UNDIRECTED) > 0;
    }
    
    
    /**
     * Checks if query graph has edges.
     * 
     * @return <code>true</code> if query graph has edges.
     */
    private boolean hasEdges()
    {
        return mGraph.getEdgeCount() > 0;
    }

    /**
     * Checks if query graph has nodes to join.
     * 
     * @return <code>true</code> there are nodes to join
     */
    private boolean hasNodesToJoin()
    {
        return mGraph.getVertexCount() > 1;
    }

    /**
     * Gets random node-operator from the query graph. The node-operator will be
     * different from the node-operator passed as an argument.
     * 
     * @param operator
     *            node-operator
     * @return node-operator or <code>null</code> if next node-operator could
     *         not be determined
     */
    private Operator getNextNode(Operator operator)
    {
        int count = mGraph.getVertexCount();
        if (count > 0)
        {
            Iterator<Operator> iter = mGraph.getVertices().iterator();
            Operator next = iter.next();
    
            if (operator == null)
                return next;
            else if (next == operator && count == 1)
                return null;
            else if (next == operator)
                return iter.next();
            else
                return next;
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets next self loop.
     * 
     * @return next self loop or <code>null</code> if not found
     */
    private Predicate getNextSelfLoop()
    {
        for (Predicate p : mGraph.getEdges())
            if (mGraph.getEndpoints(p).getFirst() == mGraph.getEndpoints(p)
                .getSecond())
                return p;
        return null;
    }

    /**
     * Gets the predicate to use for the next join operator.  This method
     * will return a predicate that connects two operators with the same
     * data node if possible, otherwise it will prefer a predicate that
     * connects two operators on the same evaluation node, otherwise it
     * will just return any predicate.
     * <p>
     * This choice of predicate based on data and evaluation node has been
     * added to ensure maximum opportunities for SQL implosion later on.
     * This relates to the solution to bug (ticket#327).
     * 
     * @return the next predicate to use.
     */
    private Predicate getNextPredicate()
    {
        // Prefer a predicate connecting operators with same data node,
        // if this is not possible then prefer same evaluation node,
        // if this is not possible then return any predicate
        
        Predicate bestSoFar = null;

        for( Predicate p : mGraph.getEdges(EdgeType.UNDIRECTED))
        {
            Operator sourceOp = mGraph.getEndpoints(p).getFirst();
            Operator destOp = mGraph.getEndpoints(p).getSecond();
            
            DataNode dn1 = getDataNode(sourceOp); 
            DataNode dn2 = getDataNode(destOp); 
            
            if (dn1 != null && dn2 != null && 
                getDataNode(sourceOp).equals(getDataNode(destOp)))
            {
                return p;
            }
            
            EvaluationNode en1 = getEvaluationNode(sourceOp);
            EvaluationNode en2 = getEvaluationNode(destOp);
            
            if (en1 != null && en2 != null && 
                getEvaluationNode(sourceOp).equals(getEvaluationNode(destOp)))
            {
                bestSoFar = p;
            }
            else if (bestSoFar == null)
            {
                bestSoFar = p;
            }
        }
        return bestSoFar;
    }
    
    
    /**
     * Gets the data node associated with an operator.
     * 
     * @param op operator
     * 
     * @return the data node associated with the operator, or 
     *         <code>null</code> if unknown.
     */
    private DataNode getDataNode(Operator op)
    {
        // If we have a data node then return it
        DataNode dataNode = null;
        if (op instanceof ScanOperator)
        {
            dataNode = ((ScanOperator) op).getDataNode();
        }
        if (dataNode != null) return dataNode;
        
        // If one child then get its data node
        int childCount = op.getChildCount(); 
        if (childCount == 0)
        {
            return null;
        }
        else if (childCount == 1)
        {
            return getDataNode(op.getChild(0));
        }
        else
        {
            DataNode dn0 = getDataNode(op.getChild(0));
            DataNode dn1 = getDataNode(op.getChild(1));
            
            if (dn0 == null || dn1 == null)
            {
                return null;
            }
            else if (dn0.equals(dn1))
            {
                return dn0;
            }
        }
        return null;
    }
    
    /**
     * Gets the evaluation node associated with an operator.
     * 
     * @param op operator
     * 
     * @return the evaluation node associated with the operator, or 
     *         <code>null</code> if unknown.
     */
    private EvaluationNode getEvaluationNode(Operator op)
    {
        // If we have a evaluation node then return it
        EvaluationNode evalNode = null;
        if (op instanceof ScanOperator)
        {
            evalNode = ((ScanOperator) op).getDataNode().getEvaluationNode();
        }
        if (evalNode != null) return evalNode;
        
        // If one child then get its data node
        int childCount = op.getChildCount(); 
        if (childCount == 0)
        {
            return null;
        }
        else if (childCount == 1)
        {
            return getEvaluationNode(op.getChild(0));
        }
        else
        {
            EvaluationNode dn0 = getEvaluationNode(op.getChild(0));
            EvaluationNode dn1 = getEvaluationNode(op.getChild(1));
            
            if (dn0 == null || dn1 == null)
            {
                return null;
            }
            else if (dn0.equals(dn1))
            {
                return dn0;
            }
        }
        return null;
    }
}
