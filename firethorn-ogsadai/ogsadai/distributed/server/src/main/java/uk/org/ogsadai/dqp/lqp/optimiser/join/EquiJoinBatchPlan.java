package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

public class EquiJoinBatchPlan implements JoinPlan
{
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(EquiJoinBatchPlan.class);
    
    public static final String EQUI_JOIN_BATCH_BUILDER = "EquiJoinBatch";
    public static final String FILTERED_TABLE_SCAN_BUILDER = "filteredTableScan";
    
    private RequestDQPFederation mRequestFederation;
    private InnerThetaJoinOperator mJoin;
    private TableScanOperator mTableScan;
    private int mBranch;
    private double mCost;

    
    public EquiJoinBatchPlan(
            RequestDQPFederation requestFederation,
            double cost, 
            InnerThetaJoinOperator join, 
            TableScanOperator tableScan, 
            int branch) 
    {
        mRequestFederation = requestFederation;
        mJoin = join;
        mTableScan = tableScan;
        mBranch = branch;
        mCost = cost;
    }
    
    @Override
    public double getCost() 
    {
        return mCost;
    }

    @Override
    public void apply() throws LQPException
    {
        Annotation.addImplementationAnnotation(
                mJoin, EQUI_JOIN_BATCH_BUILDER);
        mJoin.addAnnotation("branch", mBranch);
        mJoin.addAnnotation("tableScan", mTableScan);

        // Find the attributes to bind for the join operator
        Set<Attribute> attrToBind =
            intersect(
                mJoin.getChild(1-mBranch).getHeading().getAttributes(),
                mJoin.getPredicate().getAttributes(),
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
        mJoin.addAnnotation("attributesToBind", attrToBind);
        
        Annotation.addImplementationAnnotation(
                mTableScan, FILTERED_TABLE_SCAN_BUILDER);
        mTableScan.addAnnotation("join", mJoin);

        // Find the attributes for the in-clause in the table scan
        Set<Attribute> attrForInClause =
            intersect(
                    mJoin.getChild(mBranch).getHeading().getAttributes(),
                    mJoin.getPredicate().getAttributes(),
                    AttributeMatchMode.NAME_AND_NULL_SOURCE);
        mTableScan.addAnnotation("attributesForInClause", attrForInClause);
        mTableScan.addAnnotation("requestFederation", mRequestFederation);
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
   
    
}
