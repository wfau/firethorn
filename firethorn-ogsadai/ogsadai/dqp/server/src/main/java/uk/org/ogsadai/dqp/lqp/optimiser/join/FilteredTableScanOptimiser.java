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

package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
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
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Filtered scan optimiser tries to enforce additional filtering on table scans
 * that query single tables (usually without any filters). Results of such table
 * scans must participate in a join. It is a special kind of a join
 * optimisation.
 * <p>
 * Two configuration properties decide when this optimisation will be applied.
 * <ul>
 * <li>
 * Table to be filtered needs to have cardinality greater than defined by the
 * <code>bigtable.min.size</code> property (default: 1000).</li>
 * <li>
 * The ratio calculated by dividing the cardinality of the big table by the
 * cardinality of the other join operand must be greater than the value defined
 * by the <code>table.size.ratio</code> property (default: 10).</li>
 * </ul>
 * </p>
 * <p>
 * Users may explicitly define which tables should be considered for filtering
 * by providing the <code>table.to.filter</code> configuration property.
 * </p>
 * <p>
 * This optimisation must be applied after partitioning.
 * </p>
 * <p>
 * For best results ensure that the table scan implosion optimiser is applied
 * prior to this optimisation.
 * </p>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FilteredTableScanOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Property map. */
    private Map<String, List<String>> mProperties = 
        new HashMap<String, List<String>>();

    /** Property name. Explicit name of the table to filter. */
    public static final String TABLE_TO_FILTER = "table.to.filter";
    /** Property name. Minimum size of the table to filter. */
    public static final String BIGTABLE_MIN_SIZE = "bigtable.min.size";
    /** Property name. Minimum big/small table size ratio. */
    public static final String TABLE_SIZE_RATIO = "table.size.ratio";
    /** Default minimum size of the table to filter. */
    public static final long DEFAULT_MIN_SIZE = 1000;
    /** Default minimum big/small table size ratio. */
    public static final long DEFAULT_RATIO = 10;

    /** Minimum size of table to filter. */
    private long mMinSize = DEFAULT_MIN_SIZE;
    /** Minimum big/small table size ratio. */
    private long mRatio = DEFAULT_RATIO;

    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        List<InnerThetaJoinOperator> joinOperators =
            new ArrayList<InnerThetaJoinOperator>();
        
        // We deal only with equi-joins
        for (Operator op : OptimiserUtils.findOccurrences(lqpRoot,
            OperatorID.INNER_THETA_JOIN))
        {
            InnerThetaJoinOperator joinOperator = (InnerThetaJoinOperator) op;
            if (joinOperator.getPredicate().isAttrEqAttr())
            {
                joinOperators.add(joinOperator);
            }
        }

        // For each join - check if filtered scan optimisation can be applied
        for (InnerThetaJoinOperator joinOperator : joinOperators)
        {
            List<BranchTableScan> suitableTableScans =
                findSuitableTableScans(joinOperator);

            // If there are no suitable table scans skip to the next join
            if (suitableTableScans.size() == 0)
                continue;

            // Check if any of the tables are explicitly tagged for filtering
            BranchTableScan scanToFilter = null;
            if (checkTableToFilter(suitableTableScans.get(0).mOp.getTableName()))
            {
                scanToFilter = suitableTableScans.get(0);
            }

            if (suitableTableScans.size() > 1
                && checkTableToFilter(suitableTableScans.get(1).mOp.getTableName()))
            {
                // user tagged both inputs to the same join as candidates for
                // filtering - this is probably wrong - we roll back to the
                // default algorithm
                if (scanToFilter != null)
                {
                    scanToFilter = null;
                }
                else
                {
                    scanToFilter = suitableTableScans.get(1);
                }
            }

            // apply optimisation or roll back to the default algorithm
            if(scanToFilter != null)
            {
                applyOpt(joinOperator, scanToFilter);
            }
            else
            {
                
                if(suitableTableScans.size() > 1)
                // both join input branches have suitable table scans
                {
                    double cardinalities[] =
                    {
                        Annotation.getCardinalityAnnotation(suitableTableScans.get(0).mOp),
                        Annotation.getCardinalityAnnotation(suitableTableScans.get(1).mOp)
                    };
                    
                    int larger = (cardinalities[0] > cardinalities[1]) ? 0 : 1;
                    int smaller = (larger == 1 ? 0 : 1);
                    
                    // If larger table size does not exceed the minimum table to
                    // filter size or the ratio is too small - skip to next join
                    if (cardinalities[larger] < mMinSize)
                    {
                        continue;
                    }
                    else if (cardinalities[smaller] != 0 && cardinalities[larger] / cardinalities[smaller] < mRatio)
                    {
                        continue;
                    }
                    else
                    {
                        scanToFilter = suitableTableScans.get(larger);
                    }
                }
                else
                // only one of the join input branches has suitable table scan
                {
                    double scanOpCardinality =
                        Annotation.getCardinalityAnnotation(suitableTableScans.get(0).mOp);
                    int nonScanOpIdx =
                        (suitableTableScans.get(0).mBranch == 1 ? 0 : 1);

                    double nonScanCardinality =
                        Annotation.getCardinalityAnnotation(joinOperator.getChild(nonScanOpIdx));

                    // If table scan size does not exceed the minimum table to
                    // filter size or the ratio is too small - skip to next join
                    if (scanOpCardinality < mMinSize)
                    {
                        continue;
                    }
                    else if (scanOpCardinality / nonScanCardinality < mRatio)
                    {
                        continue;
                    }
                    else
                    {
                        scanToFilter = suitableTableScans.get(0);
                    }
                }
                
                applyOpt(joinOperator, scanToFilter);
            }
        }
        
        return lqpRoot;
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

        if (attrToBind.size() > 0)
        {
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
        }
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
     * Returns <code>true</code> when table name is in a list of tables to be
     * filtered.
     * 
     * @param tableName
     *            name of a table to be filtered
     * @return <code>true</code> if table name is in a list of tables to be
     *         filtered
     */
    private boolean checkTableToFilter(String tableName)
    {
        List<String> tablesToFilter = mProperties.get(TABLE_TO_FILTER);
        
        return (tablesToFilter != null) && tablesToFilter.contains(tableName);
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

    public void addProperty(String key, String value)
    {
        List<String> valueList = mProperties.get(key);
        if(valueList == null)
        {
            valueList = new ArrayList<String>();
            valueList.add(value);
            mProperties.put(key, valueList);
        }
        else
        {
            valueList.add(value);
        }
        
        if(key.equals(TABLE_SIZE_RATIO))
        {
            mRatio = Long.parseLong(value);
            if (mRatio < 0)
                throw new IllegalArgumentException("The " + TABLE_SIZE_RATIO
                    + " should be a non-negative integer.");
        }
        
        if(key.equals(BIGTABLE_MIN_SIZE))
        {
            mMinSize = Long.parseLong(value);
            if (mMinSize < 0)
                throw new IllegalArgumentException("The " + BIGTABLE_MIN_SIZE
                    + " should be a non-negative integer.");
        }
    }
    
    public void setProperties(Properties properties)
    {
        for (String key : properties.stringPropertyNames())
        {
            addProperty(key, properties.getProperty(key));
        }
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
