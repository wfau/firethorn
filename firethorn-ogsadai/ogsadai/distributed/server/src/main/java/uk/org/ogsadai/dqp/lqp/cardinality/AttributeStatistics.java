package uk.org.ogsadai.dqp.lqp.cardinality;

/**
 * Statistics for an attribute of a relational table.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface AttributeStatistics
{
    /**
     * Returns the total number of rows including NULL values.
     * 
     * @return number of rows
     */
    double getNumRows();

    /**
     * Returns the number of values.
     * 
     * @return number of values
     */
    double getNumValues();

    /**
     * Returns the number of rows that are NULL for this attribute.
     * 
     * @return number of NULLs
     */
    double getNumNulls();

    /**
     * Returns the estimated statistics for the attribute after applying the
     * SELECT expression <code>attribute = constant</code>.
     * 
     * @param constant
     *            constant
     * @return attribute statistics resulting from the expression
     */
    AttributeStatistics processEqualConstant(Object constant);

    /**
     * Returns the estimated statistics for the attribute after applying the
     * SELECT expression <code>attribute op constant</code> where
     * <code>op</code> is one of <, <=, >, >= .
     * 
     * @param constant
     *            constant
     * @param op
     *            arithmetic inequality operator
     * @return attribute statistics resulting from the expression
     */
    AttributeStatistics processInequalityConstant(
            ArithmeticOperator op, Object constant);

    /**
     * Returns the estimated statistics for the attribute after applying the
     * SELECT expression <code>attribute != constant</code>.
     * 
     * @param constant
     *            constant
     * @return attribute statistics resulting from the expression
     */
    AttributeStatistics processNotEqualConstant(Object constant);

    /**
     * Rescales the attribute statistics with the new number of rows.
     * 
     * @param newNumRows
     *            new total number of rows
     * @return rescaled attribute statistics
     */
    AttributeStatistics rescale(double newNumRows);
    
}
