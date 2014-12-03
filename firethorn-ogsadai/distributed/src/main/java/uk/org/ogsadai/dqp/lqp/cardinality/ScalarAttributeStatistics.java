package uk.org.ogsadai.dqp.lqp.cardinality;


public class ScalarAttributeStatistics implements AttributeStatistics
{
    public static final ScalarAttributeStatistics ZERO = 
        new ScalarAttributeStatistics(0,0,0);
    
    /** Total number of rows, this includes NULL values. */
    private final double mNumRows;
    /** Number of values. */
    private final double mNumValues;
    /** Number of rows with NULL value. */
    private final double mNumNulls;

    /**
     * Creates a scalar statistics object with the given number of rows and
     * values and NULL rows.
     * 
     * @param numRows
     *            number of rows
     * @param numValues
     *            number of values
     * @param numNulls
     *            number of NULL values
     */
    public ScalarAttributeStatistics(
            double numRows, double numValues, double numNulls)
    {
        mNumRows = numRows;
        mNumValues = numValues;
        mNumNulls = numNulls;
    }

    /**
     * Creates a scalar statistics object with the given number of rows and
     * values and zero NULL rows.
     * 
     * @param numRows
     *            number of rows
     * @param numValues
     *            number of values
     */
    public ScalarAttributeStatistics(double numRows, double numValues)
    {
        this(numRows, numValues, 0);
    }
    
    /**
     * Creates a scalar statistics object and assigns a default fraction of the
     * number of rows as the values if the number of values is unknown.
     * 
     * @param numRows total number of rows
     */
    public ScalarAttributeStatistics(double numRows)
    {
        this(numRows, numRows/10);
    }

    @Override
    public double getNumRows()
    {
        return mNumRows;
    }

    @Override
    public double getNumValues()
    {
        return mNumValues;
    }

    @Override
    public double getNumNulls()
    {
        return mNumNulls;
    }

    @Override
    public AttributeStatistics processEqualConstant(
        Object constant)
    {
        if (mNumRows == 0) return this;
        return new ScalarAttributeStatistics((mNumRows-mNumNulls)/mNumValues, 1);
    }

    @Override
    public AttributeStatistics processInequalityConstant(
        ArithmeticOperator op,
        Object constant)
    {
        if (mNumValues == 0) return this;
        double numNotNull = mNumRows-mNumNulls;
        return new ScalarAttributeStatistics(.5 * numNotNull, .5 * mNumValues);
    }

    @Override
    public AttributeStatistics processNotEqualConstant(Object constant)
    {
        if (mNumRows == 0) return this;
        double numNotNull = mNumRows-mNumNulls;
        return new ScalarAttributeStatistics(
                numNotNull - numNotNull/mNumValues, 
                mNumValues - 1);
    }

    @Override
    public AttributeStatistics rescale(double newNumRows)
    {
        if (mNumRows == 0) return this;
        double newNumValues = 
            (newNumRows > mNumValues) ? mNumValues : newNumRows;
        double newNumNulls = mNumNulls * newNumRows / mNumRows;
        return new ScalarAttributeStatistics(newNumRows, newNumValues, newNumNulls);
    }
    
    @Override
    public String toString() 
    {
        return "ScalarAttributeStatistics(numRows=" + mNumRows +
               ", numValues=" + mNumValues + 
               ", numNulls=" + mNumNulls + ")";
    }

}
