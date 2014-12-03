package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.ArrayList;
import java.util.List;

import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class EquiJoinBatch implements JoinImplementation 
{
    private int mBatchSize = 1000;
    
    public void setBatchSize(int batchSize)
    {
        mBatchSize = batchSize;
    }

    @Override
    public JoinPlan process(
            RequestDQPFederation requestFederation,
            InnerThetaJoinOperator joinOperator)
    {
        if (!joinOperator.getPredicate().isAttrEqAttr()) return null;
        
        List<BranchTableScan> suitableTableScans =
            findSuitableTableScans(joinOperator);
        
        if (suitableTableScans.size() == 0) return null;
        
        Double[] card =  { 
                getChildCardinality(0, joinOperator), 
                getChildCardinality(1, joinOperator)
        };
        
        // To create as few batch queries as possible we wish to stream on
        // the side with the lowest cardinality and hence we repeatedly query 
        // the side with the largest cardinality.
        double maxCardinality = -1.0;
        BranchTableScan choice = null;
        for (BranchTableScan tableScan : suitableTableScans)
        {
            if (card[tableScan.mBranch] > maxCardinality)
            {
                maxCardinality = card[tableScan.mBranch];
                choice = tableScan;
            }
        }
        
        // The result cardinality is a good estimate of the total number of
        // reads from the repeated side. It is true if there are no duplicate 
        // join attribute values in each batch of values from the streamed side. 
        // If the streamed side has duplicates then using the result cardinality
        // is a overestimation of the number of reads, and hence lookups.
        Cost cost = new Cost();
        int indexStreamedSide = 1-choice.mBranch;
        Double resultCardinality = Annotation.getCardinalityAnnotation(joinOperator);
        cost.setReads(card[indexStreamedSide] + resultCardinality);
        cost.setLookups(resultCardinality);
        cost.setQueries(Math.ceil(card[indexStreamedSide]/(double)mBatchSize));
            
        // Create the new query plan
        EquiJoinBatchPlan plan = new EquiJoinBatchPlan(
                requestFederation,
                cost.getCost(),
                joinOperator, 
                choice.mOp, choice.mBranch);
        return plan;

    }
    
    private Double getChildCardinality(int index, Operator operator) 
    {
        Double childCard =
            Annotation.getCardinalityAnnotation(operator.getChild(index));
        if (childCard == null)
        {
            throw new RuntimeException(
                    "Operator " + operator.getChild(index) + 
                    " has no CardinalityStatistics annotation");
        }
        return childCard;
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
                current = null;
            }
            else
            {
                current = current.getChild(0);
            }
        }
        return null;
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
             "Copyright (c) The University of Edinburgh, 2012";
            
         /** Wrapped table scan operator. */
         TableScanOperator mOp;
         /** Direct ancestor join input branch index. */
         int mBranch;

        /**
         * Constructor.
         * 
         * @param operator
         *            table scan operator to wrap
         * @param branch
         *            direct ancestor join input branch index
         */
        public BranchTableScan(TableScanOperator operator, int branch) {
            mOp = operator;
            mBranch = branch;
        }
        
        @Override
        public String toString() 
        {
            return "BranchTableScan(branch=" + mBranch + ")";
        }
    }

}
