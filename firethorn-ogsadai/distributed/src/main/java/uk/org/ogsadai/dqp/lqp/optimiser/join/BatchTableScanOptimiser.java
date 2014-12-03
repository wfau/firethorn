// Copyright (c) The University of Edinburgh, 2011.
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

package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.FilteredTableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.QueryApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.udf.repository.SimpleFunctionRepository;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser that chooses when to execute a batch table scan.
 * <p>
 * THIS IMPLEMENTATION SIMPLY DOES IT WHERE EVERY POSSIBLE.  THIS IS NOT
 * CORRECT. WE NEED SOMETHING MORE CLEVER HERE.
 */
public class BatchTableScanOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2011";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(BatchTableScanOptimiser.class);

    @Override
    public Operator optimise(Operator lqpRoot,
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration,
        RequestDetails requestDetails) throws LQPException
    {
        // Decide when to do it and which side it do it for - initially
        // lets do it always where possible
        List<InnerThetaJoinOperator> equiJoins = findEquiJoins(lqpRoot);
        
        for (InnerThetaJoinOperator join : equiJoins)
        {
            List<BranchTableScan> suitableTableScans =
                findSuitableTableScans(join);
            
            if (suitableTableScans.size() > 0)
            {
                // We have at least one option - if there is more than one
                // we should decide which on to use. Currently we always
                // use the first one.  Ought to choose the one with the
                // smallest cardinality I guess
                BranchTableScan batchTableScan = suitableTableScans.get(0);
                
                // Create the new query plan
                applyOpt(join, batchTableScan);
            }
        }
        
        return lqpRoot;
    }

    /**
     * Finds all the equi-joins in the given query plan
     * 
     * @param root root of the query plan
     * 
     * @return all the join operators that are equi-joins
     */
    private static List<InnerThetaJoinOperator> findEquiJoins(Operator root)
    {
        List<InnerThetaJoinOperator> joinOperators =
            new ArrayList<InnerThetaJoinOperator>();
        
        // We deal only with equi-joins
        for (Operator op : OptimiserUtils.findOccurrences(
                root, OperatorID.INNER_THETA_JOIN))
        {
            InnerThetaJoinOperator joinOperator = (InnerThetaJoinOperator) op;
            if (joinOperator.getPredicate().isAttrEqAttr())
            {
                joinOperators.add(joinOperator);
            }
        }
        
        return joinOperators;
    }

    /**
     * Finds a list of table scan operators suitable for filtering for a given
     * join operator.
     * 
     * @param joinOperator
     *            join operator
     * @return a list of table scan operators suitable for filtering
     */
    private List<BranchTableScan> findSuitableTableScans(
        InnerThetaJoinOperator joinOperator)
    {
        List<BranchTableScan> suitableTSList = new ArrayList<BranchTableScan>();

        TableScanOperator tableScanOperator;
        if ((tableScanOperator =
                findSuitableTableScan(joinOperator.getChild(0))) != null)
        {
            suitableTSList.add(new BranchTableScan(tableScanOperator, 0));
        }
        if ((tableScanOperator =
                findSuitableTableScan(joinOperator.getChild(1))) != null)
        {
            suitableTSList.add(new BranchTableScan(tableScanOperator, 1));
        }

        return suitableTSList;
    }

    /**
     * Finds table scan in a query plan branch. The search is terminated when
     * binary operator is encountered.
     * 
     * @param operator
     *            root of the the query plan branch
     * @return table scan operator or <code>null</code> if no suitable scan
     *         found
     */
    private TableScanOperator findSuitableTableScan(Operator operator)
    {
        Operator current = operator;
        while(current != null && !current.isBinary())
        {
            if(current.getID() == OperatorID.TABLE_SCAN)
            {
                TableScanOperator tso = (TableScanOperator) current;
                if (tso.getQuery().supportsFilteredTableScan())
                {
                    return (TableScanOperator) current;
                }
            }
            else
            {
                current = current.getChild(0);
            }
        }
        
        return null;
    }
    
    
    /**
     * Applies filtered table scan optimisation using a given join and table
     * scan. Join operator is converted to a QUERY_APPLY operator and table scan
     * is converted to a FILTERED_TABLE_SCAN operator.
     * 
     * @param joinOperator
     *            join operator to be converted to query apply
     * @param scanOperator
     *            scan operator to be converted to filtered table scan
     * @throws LQPException
     *             if update after conversion fails
     */
    private void applyOpt(InnerThetaJoinOperator joinOperator,
        BranchTableScan scanOperator) throws LQPException
    {
        Predicate joinPredicate = joinOperator.getPredicate();
        int branchToFilter = scanOperator.mBranch;

        Set<Attribute> attrToBind =
            intersect(
                joinOperator.getChild(branchToFilter == 1 ? 0 : 1).getHeading().getAttributes(),
                joinPredicate.getAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
        
        Set<Attribute> attrForInClause =
            intersect(
                joinOperator.getChild(branchToFilter).getHeading().getAttributes(),
                joinPredicate.getAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE);

        if (attrToBind.size() > 0)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("attribute to bind: " + attrToBind.iterator().next());
                LOG.debug("attribute for in clause: " + 
                    attrForInClause.iterator().next());
            }
            
            QueryApplyOperator queryApplyOp =
                new QueryApplyOperator(joinOperator, attrToBind);
            
            queryApplyOp.setChild(0, joinOperator.getChild(0));
            queryApplyOp.setChild(1, joinOperator.getChild(1));

            joinOperator.getParent().replaceChild(joinOperator, queryApplyOp);
            joinOperator.disconnect();

            FilteredTableScanOperator ftso =
                new FilteredTableScanOperator(scanOperator.mOp, joinPredicate);
            scanOperator.mOp.getParent().replaceChild(scanOperator.mOp, ftso);

            scanOperator.mOp.disconnect();
            queryApplyOp.getParent().update();

            // Add the aggregate expression to the query apply op
            queryApplyOp.setAggregateExpressions(new String[]{
                "STRING_AGGREGATE(" + attrToBind.iterator().next() + ")"
            });
            
            // Give this aggregate expression a unique name
            String uniqueName = "dqpBatchData_267276";
            queryApplyOp.setAggregateNames(new String[]{uniqueName});
            
            // Choose a batch size
            // TODO: Need to do better here choosing the size. Keeping very
            // low for testing
            queryApplyOp.setBatchSize(20);
            
            // Put an IN expression into the filtered table scan
            StringBuilder sb = new StringBuilder();
            sb.append(attrForInClause.iterator().next());
            sb.append(" IN ( DQP_REPLACE(").append(uniqueName).append(") )");
            
            SimpleFunctionRepository funcRepo = new SimpleFunctionRepository();
            funcRepo.register(DQPReplace.class);
            
            Predicate pred = new CommonPredicate(sb.toString(), funcRepo);
            ftso.getQuery().addPredicate(pred);
        }
    }
    
    /**
     * Intersects a list and a collection. Returns a set of those entries in the
     * list that are contained on the collection.
     * 
     * @param list
     *            list
     * @param collection
     *            collection
     * @param attributeMatchMode
     *            attribute match mode
     * 
     * @return intersection of the list and the collection
     */
    private Set<Attribute> intersect(
        List<Attribute> list,
        Collection<Attribute> collection,
        AttributeMatchMode attributeMatchMode)
    {
        Set<Attribute> intersection = new HashSet<Attribute>();
        for (Attribute a1 : list)
        {
            boolean found = false;
            for (Attribute a2 : collection)
            {
                if (a1.equals(a2, attributeMatchMode))
                {
                    found = true;
                    break;
                }
            }
            if (found)
            {
                intersection.add(a1);
            }
        }
        return intersection;
    }
    
    /**
     * Wrapper class for table scans. Includes information about the input
     * branch index of the direct ancestor join.
     * 
     * @author The OGSA-DAI Project Team.
     */
    private class BranchTableScan
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE =
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** Wrapped table scan operator. */
        TableScanOperator mOp;
        /** Direct ancestor join input branch index. */
        int mBranch;
     
        /**
         * Constructor.
         * 
         * @param operator
         *      table scan operator to wrap
         * @param branch
         *      direct ancestor join input branch index
         */
        public BranchTableScan(TableScanOperator operator, int branch)
        {
            mOp = operator;
            mBranch = branch;
        }
    }
}
