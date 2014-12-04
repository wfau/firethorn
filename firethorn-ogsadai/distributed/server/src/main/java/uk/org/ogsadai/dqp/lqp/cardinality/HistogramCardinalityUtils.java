package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.LinkedList;
import java.util.List;

public class HistogramCardinalityUtils
{
    public static AttributeStatistics difference(
        HistogramBasedAttributeStatistics attrStats0,
        HistogramBasedAttributeStatistics attrStats1)
    {
        HistogramBasedAttributeStatistics result = 
            new HistogramBasedAttributeStatistics();

        HistogramBasedAttributeStatistics[] alignedStats = 
            HistogramBinAligner.alignBins(attrStats0, attrStats1);

        for (int i=0; i<alignedStats[0].getBins().size(); i++)
        {
            AttributeHistogramBin resultBin = difference(
                alignedStats[0].getBins().get(i), 
                alignedStats[1].getBins().get(i));
            if (resultBin.getNumRows() != 0)
            {
                result.addBin(resultBin);
            }
        }
        return result;
    }

    /**
     * Processes attribute statistics involved in a SELECT operator of the
     * type attr op attr. 
     * 
     * @param lhsStats  statistics for the left hand side attribute
     * @param operator  the arithmetic operator
     * @param rhsStats  statistics for the right hand side attribute
     * 
     * @return a list containing two attribute statistics.  The first is the
     *         new statistics for the left hand attribute, the second is the new
     *         statistics for the right hand side attribute.
     */
    public static List<AttributeStatistics> processSelectAttrOperatorAttr(
        HistogramBasedAttributeStatistics lhsStats,
        ArithmeticOperator operator,
        HistogramBasedAttributeStatistics rhsStats)
    {
        List<AttributeStatistics> result = 
            new LinkedList<AttributeStatistics>();
        
        HistogramBasedAttributeStatistics resultLhs =
            new HistogramBasedAttributeStatistics();
        HistogramBasedAttributeStatistics resultRhs =
            new HistogramBasedAttributeStatistics();
        
        for (AttributeHistogramBin bin : lhsStats.getBins())
        {
            double factor = rhsStats.fractionOfRowsSatifyingCondition(
                operator, bin.getRange());
            
            if (factor != 0)
            {
                resultLhs.addBin(bin.rescale(factor*bin.getNumRows()));
            }
        }
        
        ArithmeticOperator inverseOp = 
            ArithmeticOperator.swapOperands(operator);
        
        for (AttributeHistogramBin bin : rhsStats.getBins())
        {
            double factor = lhsStats.fractionOfRowsSatifyingCondition(
                    inverseOp, bin.getRange());

            if (factor != 0)
            {
                resultRhs.addBin(bin.rescale(factor*bin.getNumRows()));
            }
        }
        
        
        // The rescaling may not have done the same thing to both
        // sides. Normalise to the max.
        double newNumRows = 
            Math.max(resultLhs.getNumRows(), resultRhs.getNumRows());
        
        if (newNumRows == 0)
        {
            result.add(ScalarAttributeStatistics.ZERO);
            result.add(ScalarAttributeStatistics.ZERO);
        }
        else
        {
            result.add(resultLhs.rescale(newNumRows));
            result.add(resultRhs.rescale(newNumRows));
        }
        return result;
    }
    
    public static AttributeStatistics processSelectAttrEqualsBoundAttr(
        HistogramBasedAttributeStatistics lhsStats,
        HistogramBasedAttributeStatistics rhsStats)
    {
        return processEquiJoin(lhsStats, rhsStats);
    }

    /**
     * Processes the product of this histogram and another relation with the
     * given cardinality.
     * 
     * @param attrStats
     *            input histogram
     * @param cardinality
     *            cardinality of the other relation
     * @return resized attribute statistics
     */
    public static AttributeStatistics processProduct(
            HistogramBasedAttributeStatistics attrStats,
            double cardinality) 
    {
        HistogramBasedAttributeStatistics result = 
            new HistogramBasedAttributeStatistics();
        for (AttributeHistogramBin bin : attrStats.getBins())
        {
            result.addBin(new AttributeHistogramBin(
                    bin.getRange(), 
                    bin.getNumRows() * cardinality, 
                    bin.getNumValues()));
        }
        return result;
    }

    /**
     * Processes a equi join between two attributes.
     * 
     * @param lhsStats  statistics for LHS of equi-join
     * @param rhsStats  statistics for LHS of equi-join
     * 
     * @return the statistics for the attribute after the join is applied. Note
     *         that the statistics for both the LHS attribute and RHS attribute
     *         will be identical after the equi-join so only one set of
     *         statistics is returned.
     */
    public static AttributeStatistics processEquiJoin(
        HistogramBasedAttributeStatistics lhsStats,
        HistogramBasedAttributeStatistics rhsStats)
    {
        // Align bins
        HistogramBasedAttributeStatistics[] alignedHistograms = 
            HistogramBinAligner.alignBins(lhsStats, rhsStats);
        
        // Process each bin
        HistogramBasedAttributeStatistics resultHistogram = 
            new HistogramBasedAttributeStatistics();
        
        for (int i=0; i<alignedHistograms[0].getBins().size(); ++i)
        {
            AttributeHistogramBin bin0 = alignedHistograms[0].getBins().get(i);
            AttributeHistogramBin bin1 = alignedHistograms[1].getBins().get(i);
            AttributeHistogramBin resultBin;
            
            if (bin0.isEmpty() || bin1.isEmpty())
            {
                resultBin = null;
            }
            else if (bin0.isKey() && bin1.isKey())
            {
                resultBin = new AttributeHistogramBin(
                    bin0.getRange(),
                    Math.min(bin0.getNumRows(), bin1.getNumRows()),
                    Math.min(bin0.getNumRows(), bin1.getNumRows()));
            }
            else if (bin0.isKey())
            {
                double numRows = 
                    Math.min(bin0.getNumValues(), bin1.getNumValues()) *
                    bin1.getNumRows()/bin1.getNumValues();

                resultBin = new AttributeHistogramBin(
                    bin0.getRange(),
                    numRows,
                    Math.min(bin0.getNumValues(), bin1.getNumValues()));
            }
            else if (bin1.isKey())
            {
                double numRows = 
                    Math.min(bin0.getNumValues(), bin1.getNumValues()) *
                    bin0.getNumRows()/bin0.getNumValues();
                    
                resultBin = new AttributeHistogramBin(
                    bin0.getRange(),
                    numRows,
                    Math.min(bin0.getNumValues(), bin1.getNumValues()));
            }
            else
            {
                double numRows = bin0.getNumRows() * bin1.getNumRows() /
                    Math.max(bin0.getNumValues(), bin1.getNumValues());
                
                double numValues = 
                    Math.min(Math.min(numRows, bin0.getNumValues()), 
                             bin1.getNumValues());
   
                resultBin = new AttributeHistogramBin(
                    bin0.getRange(), numRows, numValues);
           }
            
            if (resultBin != null)
            {
                resultHistogram.addBin(resultBin);
            }
        }
        if (resultHistogram.isEmpty())
        {
            return ScalarAttributeStatistics.ZERO;
        }
        return resultHistogram;
    }

    public static AttributeStatistics processEquiSemiJoin(
            HistogramBasedAttributeStatistics lhsStats,
            HistogramBasedAttributeStatistics rhsStats) 
    {
        // Align bins
        HistogramBasedAttributeStatistics[] alignedHistograms = 
            HistogramBinAligner.alignBins(lhsStats, rhsStats);

        // Process each bin
        HistogramBasedAttributeStatistics resultHistogram = 
            new HistogramBasedAttributeStatistics();
        for (int i=0; i<alignedHistograms[0].getBins().size(); ++i)
        {
            AttributeHistogramBin bin0 = alignedHistograms[0].getBins().get(i);
            AttributeHistogramBin bin1 = alignedHistograms[1].getBins().get(i);
            AttributeHistogramBin resultBin;
            
            if (bin0.isEmpty() || bin1.isEmpty())
            {
                resultBin = null;
            }
            else
            {
                double numValues = 
                    Math.min(bin0.getNumValues(), bin1.getNumValues());
                double numRows = 
                    bin0.getNumRows() * (numValues / bin0.getNumValues());
                    
                resultBin = new AttributeHistogramBin(
                    bin0.getRange(),
                    numRows,
                    numValues);
            }
            
            if (resultBin != null)
            {
                resultHistogram.addBin(resultBin);
            }
        }
        
        return resultHistogram;
    }
    
    public static HistogramBasedAttributeStatistics processDuplicateElimination(
            HistogramBasedAttributeStatistics attrStats) 
    {
        HistogramBasedAttributeStatistics result =
            new HistogramBasedAttributeStatistics();
        for (AttributeHistogramBin bin : attrStats.getBins())
        {
            if (bin.isKey())
            {
                result.addBin(bin);
            }
            else
            {
                result.addBin(new AttributeHistogramBin(
                    bin.getRange(), bin.getNumValues(), bin.getNumValues()));
            }
        }
        return result;
    }

    /**
     * Generates a new bin that contains the statistics for values in bin0 AND
     * NOT in bin1. This method assumes that the bins are aligned.
     * 
     * @param bin0
     *            bin statistics for the range
     * @param bin1
     *            bin containing the statistics for excluded values
     * @return statistics defined by bin0 excluding bin1
     */
    private static AttributeHistogramBin difference(
            AttributeHistogramBin bin0, AttributeHistogramBin bin1)
    {
        AttributeHistogramRange range = bin0.getRange();
        double numRows = Math.max(bin0.getNumRows() - bin1.getNumRows(), 0);
        double numValues = Math.min(bin0.getNumValues(), numRows);
        return new AttributeHistogramBin(range, numRows, numValues);
    }

    public static AttributeStatistics sum(
            HistogramBasedAttributeStatistics attrStats0,
            HistogramBasedAttributeStatistics attrStats1)
    {
        HistogramBasedAttributeStatistics result = 
            new HistogramBasedAttributeStatistics();

        HistogramBasedAttributeStatistics[] alignedStats = 
            HistogramBinAligner.alignBins(attrStats0, attrStats1);

        for (int i=0; i<alignedStats[0].getBins().size(); i++)
        {
            AttributeHistogramBin resultBin = sum(
                alignedStats[0].getBins().get(i), 
                alignedStats[1].getBins().get(i));
            if (resultBin.getNumRows() != 0)
            {
                result.addBin(resultBin);
            }
        }
        return result;
    }

    private static AttributeHistogramBin sum(
            AttributeHistogramBin bin0,
            AttributeHistogramBin bin1) 
    {
        AttributeHistogramRange range = bin0.getRange();
        double numRows = bin0.getNumRows() + bin1.getNumRows();
        double numValues = bin0.getNumValues() + bin1.getNumValues();
        return new AttributeHistogramBin(range, numRows, numValues);
    }

}
