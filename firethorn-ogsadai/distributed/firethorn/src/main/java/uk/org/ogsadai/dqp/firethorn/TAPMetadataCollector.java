package uk.org.ogsadai.dqp.firethorn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.ClientProtocolException;

import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.org.ogsadai.activity.astro.TAPService;
import uk.org.ogsadai.activity.astro.QueryFailedException;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramBin;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramBinEndpoint;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeHistogramRange;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.HistogramBasedAttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.ScalarAttributeStatistics;

public class TAPMetadataCollector 
{
    /**
     * Returns the number of rows in a table.
     * 
     * @param tapService
     *            URL of the TAP service
     * @param table
     *            table name
     * @return number of values in the column
     * @throws TimeoutException 
     * @throws QueryFailedException 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public double getNumRows(TAPService service, String table) 
    throws ClientProtocolException, IOException, QueryFailedException, TimeoutException 
    {
        String query = "SELECT count(*) FROM " + table;
        StarTable starTable = service.runQuery(query);
        return extractDouble(starTable);        
    }

    /**
     * Returns the number of values of the attribute.
     * 
     * @param service
     *            TAP service
     * @param table
     *            table name
     * @param column
     *            column name
     * @return number of values in the column
     * @throws TimeoutException
     * @throws QueryFailedException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public double getNumValues(TAPService service, String table, String column) 
    throws ClientProtocolException, IOException, QueryFailedException, TimeoutException 
    {
        String query = "SELECT count(DISTINCT " + column + ") FROM " + table;
        StarTable starTable = service.runQuery(query);
        return extractDouble(starTable);
    }
    
    /**
     * Returns the number of NULL entries in the column.
     * 
     * @param service
     *            TAP service
     * @param table
     *            table name
     * @param column
     *            column name
     * @return number of NULL values in the column
     * @throws TimeoutException 
     * @throws QueryFailedException 
     * @throws IOException 
     * @throws ClientProtocolException 
     * @throws SQLException
     */
    public double getNumNulls(TAPService service, String table, String column) 
            throws ClientProtocolException, IOException, QueryFailedException, TimeoutException 
    {
        String query = "SELECT count(*) FROM " + table + 
                        " WHERE " + column + " IS NULL";
        StarTable starTable = service.runQuery(query);
        return extractDouble(starTable);
    }
    
    /**
     * Creates histogram based statistics for a column.
     * 
     * @param tapService
     *            URL of the TAP service
     * @param table
     *            table name
     * @param column
     *            column name
     * @param numBins
     *            number of histogram bins
     * @param isKey
     *            indicates whether the column is a key and the number of values
     *            is the same as the total number of rows (meaning that distinct
     *            values are not queried for keys)
     * @param valueEstimateFactor
     *            estimate values as a factor of the total number of rows if the
     *            distinct values query fails, for example if the factor is .1
     *            then the number of values is assigned as 1/10 of the number of
     *            rows. This factor is ignored if the column is a key.
     * @return attribute statistics
     * @throws IOException
     * @throws TimeoutException
     * @throws QueryFailedException
     */
    public HistogramBasedAttributeStatistics createHistograms(
            TAPService service, 
            String table, 
            String column, 
            int numBins, 
            boolean isKey,
            double valueEstimateFactor) 
    throws IOException, QueryFailedException, TimeoutException 
    {
        HistogramBasedAttributeStatistics result = 
                new HistogramBasedAttributeStatistics();
        double numNulls = getNumNulls(service, table, column);
        result.setNumNulls(numNulls);
        String query = "SELECT min(" + column + "), max(" + column + ") " +
                       "FROM " + table;
        StarTable starTable = service.runQuery(query);
        RowSequence sequence = starTable.getRowSequence();
        sequence.next();
        Object[] row = sequence.getRow();
        double min = ((Number)row[0]).doubleValue();
        double max = ((Number)row[1]).doubleValue();
        sequence.close();
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
                whereClause = " WHERE " + column + " <= " + upper;
                isMinInclusive = true;
            }
            else
            {
                whereClause = " WHERE " + column + " > " + lower + 
                        " AND " + column + " <= " + upper;
                isMinInclusive = false;
            }
            query = "SELECT count(*) FROM " + table + whereClause;
            starTable = service.runQuery(query);
            double numRows = extractDouble(starTable);
            double numValues;
            
            if (isKey)
            {
                numValues = numRows;
            }
            else
            {
                query = "SELECT count(DISTINCT " + column + ") FROM " + 
                        table + whereClause;
                try
                {
                    starTable = service.runQuery(query);
                    numValues = extractDouble(starTable);
                }
                catch (Throwable e)
                {
                    System.out.println(
                            "Warning: Estimating number of values for range "
                                    + whereClause);
                    numValues = numRows * valueEstimateFactor;
                }
            }
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
     * @param tapService
     *            URL of the TAP service
     * @param table
     *            table name
     * @param column
     *            column name
     * @param numRows
     *            total number of rows in the table
     * @param valueEstimateFactor
     *            estimate for the number of values if the query for distinct
     *            values is not supported
     * @return attribute statistics
     * @throws TimeoutException
     * @throws QueryFailedException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public AttributeStatistics createScalarAttributeStatistics(
            TAPService service,
            String table,
            String column, 
            double numRows,
            double valueEstimateFactor) 
    throws ClientProtocolException, IOException, QueryFailedException, TimeoutException
    {
        double numValues;
        try
        {
            numValues = getNumValues(service, table, column);
        }
        catch (Throwable e)
        {
            System.out.println("Warning: Estimating number of rows");
            numValues = numRows * valueEstimateFactor;
        }
        double numNulls = getNumNulls(service, table, column);
        // if there are NULLs then we add 1 to the values as NULL is a value
        if (numNulls > 0) numValues = numValues + 1;
        return new ScalarAttributeStatistics(numRows, numValues, numNulls);
    }
    
    private double extractDouble(StarTable starTable)
    throws IOException, QueryFailedException
    {
        RowSequence sequence = starTable.getRowSequence();
        try
        {
            sequence.next(); 
            Object[] row = sequence.getRow();
            if (row[0] instanceof Number)
            {
                return ((Number)row[0]).doubleValue();
            }
            else
            {
                throw new QueryFailedException("Unexpected result: " + row[0]);
            }
        }
        finally
        {
            sequence.close();
        }
    }
    
}
