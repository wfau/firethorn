package uk.org.ogsadai.dqp.firethorn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramBin;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramBinEndpoint;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramRange;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.HistogramBasedAttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.ScalarAttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.SimpleCardinalityStatistics;

/**
 * Collects DQP statistics from a JDBC connection to a database.
 */
public class MetadataCollector
{
    /**
     * Creates attribute statistics for all columns in the database table.
     * 
     * @param connection
     *            database connection
     * @param table
     *            table name
     * @param numBins
     *            number of bins to create for histogram statistics
     * @return cardinality statistics for the table
     * @throws SQLException
     */
    public CardinalityStatistics createStatistics(
            Connection connection, String table, int numBins)
    throws SQLException
    {
        SimpleCardinalityStatistics result = new SimpleCardinalityStatistics();
        ResultSet resultSet = 
                executeQuery(connection, "SELECT count(*) FROM " + table);
        resultSet.next();
        double numRows = resultSet.getDouble(1);
        resultSet.close();
        System.out.println("Total number of rows in table '" + table + "' : " + numRows);
        resultSet = connection.getMetaData().getColumns(null, null, table, null);
        while (resultSet.next())
        {
            String column = resultSet.getString(4);
            System.out.println("Creating histograms for " + column);
            int type = resultSet.getInt(5);
            AttributeStatistics attrStats = null;
            switch (type)
            { 
            // only numeric types
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.REAL:
            case Types.SMALLINT:
            case Types.TINYINT:
                attrStats = createHistograms(connection, table, column, numBins);
                break;
            default:
                attrStats = createScalarAttributeStatistics(connection, table, column, numRows);
                break;
            }
            if (attrStats != null)
            {
                System.out.println("rows=" + attrStats.getNumRows() +
                        ", values=" + attrStats.getNumValues() +
                        ", nulls=" + attrStats.getNumNulls());
                result.addAttributeStatistics(
                        new AttributeImpl(column, table), attrStats);
            }
        }
        return result;
    }
    
    /**
     * Returns the number of values of the attribute.
     * 
     * @param connection
     *            database connection
     * @param table
     *            table name
     * @param column
     *            column name
     * @return number of values in the column
     * @throws SQLException
     */
    public double getNumValues(Connection connection, String table, String column) 
            throws SQLException
    {
        ResultSet resultSet = 
                executeQuery(connection, 
                        "SELECT count(*) FROM (SELECT DISTINCT " + column + 
                        " FROM " + table + ") t");
        resultSet.next();
        double result = resultSet.getDouble(1);
        resultSet.close();
        return result;
    }
    
    /**
     * Returns the number of NULL entries in the column.
     * 
     * @param connection
     *            database connection
     * @param table
     *            table name
     * @param column
     *            column name
     * @return number of NULL values in the column
     * @throws SQLException
     */
    public double getNumNulls(Connection connection, String table, String column) 
            throws SQLException
    {
        ResultSet resultSet = 
                executeQuery(connection, 
                        "SELECT count(*) FROM " + table + 
                        " WHERE " + column + " IS NULL");
        resultSet.next();
        double result = resultSet.getDouble(1);
        resultSet.close();
        return result;
    }
    
    /**
     * Creates histogram based statistics for a column.
     * 
     * @param connection
     *            database connection
     * @param table
     *            table name
     * @param column
     *            column name
     * @param numBins
     *            number of histogram bins
     * @return attribute statistics
     * @throws SQLException
     */
    public HistogramBasedAttributeStatistics createHistograms(
            Connection connection, String table, String column, int numBins) 
                    throws SQLException
    {
        HistogramBasedAttributeStatistics result = 
                new HistogramBasedAttributeStatistics();
        double numNulls = getNumNulls(connection, table, column);
        result.setNumNulls(numNulls);
        ResultSet resultSet = 
                executeQuery(connection, 
                        "SELECT min(" + column + "), max(" + column + ") " +
                        "FROM " + table);
        resultSet.next();
        double min = resultSet.getDouble(1);
        double max = resultSet.getDouble(2);
        resultSet.close();
        System.out.println("min=" + min + ", max=" + max);
        double binSize = (max - min)/numBins;
        for (int i=0; i<numBins; i++)
        {
            double lower = min + i*binSize;
            double upper = (i==numBins-1)? max : min + (i+1)*binSize;
            String whereClause;
            boolean isMinInclusive;
            if (i == 0)
            {
                // include min in the first bin
                whereClause = " WHERE " + column + " >= " + lower + 
                        " AND " + column + " <= " + upper;
                isMinInclusive = true;
            }
            else
            {
                whereClause = " WHERE " + column + " > " + lower + 
                        " AND " + column + " <= " + upper;
                isMinInclusive = false;
            }
            String query = "SELECT count(*) FROM " + table + whereClause;
            resultSet = executeQuery(connection, query);
            resultSet.next();
            double numRows = resultSet.getDouble(1);
            resultSet.close();
            resultSet = executeQuery(
                    connection, 
                    "SELECT count(*) FROM (SELECT DISTINCT " + 
                    column + " FROM " + table + whereClause +") t");
            resultSet.next();
            double numValues = resultSet.getDouble(1);
            resultSet.close();
            AttributeHistogramRange range =
                    new AttributeHistogramRange(
                        new AttributeHistogramBinEndpoint(lower, isMinInclusive), 
                        new AttributeHistogramBinEndpoint(upper, true));
            AttributeHistogramBin histogramBin = 
                    new AttributeHistogramBin(
                            range, numRows, numValues);
            System.out.println( i + ": bin=[" + lower + "," + upper + "], rows=" 
                            + numRows + ", values=" + numValues);
            result.addBin(histogramBin);
        }
        return result;
    }
    
    /**
     * Creates simple scalar statistics for a column.
     * 
     * @param connection
     *            database connection
     * @param table
     *            table name
     * @param column
     *            column name
     * @param numRows
     *            total number of rows in the table
     * @return attribute statistics
     * @throws SQLException
     */
    public AttributeStatistics createScalarAttributeStatistics(
            Connection connection, String table, String column, double numRows)
    throws SQLException
    {
        double numValues = getNumValues(connection, table, column);
        double numNulls = getNumNulls(connection, table, column);
        return new ScalarAttributeStatistics(numRows, numValues, numNulls);
    }
    
    private ResultSet executeQuery(Connection connection, String query) 
            throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}
