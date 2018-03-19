package uk.org.ogsadai.dqp.lqp.optimiser.join;

public class JoinRank
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Selectivity. **/
    private double mSelectivity;
    /** Indicates if the two operands are on the same data node. */
    private boolean mSameDataNode;
    /** Indicates if the two operands are on the same evaluation node. */
    private boolean mSameEvaluationNode;
    /** Indicates it a join involves two table scans. */
    private boolean mTableScanJoin;

    /**
     * Constructor.
     * 
     * @param selectivity
     *      join selectivity factor
     * @param sameDataNode
     *      signifies if the two operands are on the same data node
     * @param sameEvaluationNode
     *      signifies if two two operands are on the same evaluation node
     * @param tableScanJoin
     *      signifies if both join operands are table scans
     */
    public JoinRank(
        double selectivity, 
        boolean sameDataNode,
        boolean sameEvaluationNode,
        boolean tableScanJoin)
    {
        mSelectivity = selectivity;
        mSameDataNode = sameDataNode;
        mSameEvaluationNode = sameEvaluationNode;
        mTableScanJoin = tableScanJoin;
    }

    /**
     * Compares two ranks.
     * 
     * @param rank
     *            rank to compare
     * @return <code>true</code> is rank is higher than rank passed as
     *         argument.
     */
    public boolean higherThan(JoinRank rank)
    {
        if ((mSameDataNode && !rank.mSameDataNode) || 
                (mSameEvaluationNode && !rank.mSameEvaluationNode) ||
                (mTableScanJoin && !rank.mTableScanJoin))
        {
            return true;
        }
        if ((!mSameDataNode && rank.mSameDataNode) ||
                (!mSameEvaluationNode && rank.mSameEvaluationNode) ||
                (!mTableScanJoin && rank.mTableScanJoin))
        {
            return false;
        }
        return (mSelectivity < rank.mSelectivity);
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "JoinRank[selectivity= " + mSelectivity + 
            ", SameDataNode: " + mSameDataNode +
            ", SameEvaluationNode: " + mSameEvaluationNode + 
            ", TableScanJoin: " + mTableScanJoin + "]";
    }
}
