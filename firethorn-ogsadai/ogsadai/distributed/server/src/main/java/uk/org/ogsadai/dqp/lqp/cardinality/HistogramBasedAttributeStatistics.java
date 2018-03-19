package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.LinkedList;
import java.util.List;

public class HistogramBasedAttributeStatistics implements AttributeStatistics
{
    /** Histogram bins. */
    private List<AttributeHistogramBin> mBins;
    private double mNumNulls;
    
    /**
     * Constructor.  Use addBin or setNumRows methods to add histogram
     * bins or set the cardinality count.
     */
    public HistogramBasedAttributeStatistics()
    {
        mBins = new LinkedList<AttributeHistogramBin>();
        mNumNulls = 0;
    }
    
    /**
     * Sets the number of rows with NULL values in this histogram.
     * 
     * @param numNulls
     *            number of NULL values
     */
    public void setNumNulls(double numNulls)
    {
        mNumNulls = numNulls;
    }

    /**
     * Adds a histogram bin.
     * 
     * @param histogramBin histogram bin.
     */
    public void addBin(AttributeHistogramBin histogramBin)
    {
        mBins.add(histogramBin);
    }
    
    public List<AttributeHistogramBin> getBins()
    {
        return mBins;
    }
    
    public boolean isEmpty()
    {
        return mBins.isEmpty();
    }
    
    @Override
    public double getNumRows()
    {
        double result = mNumNulls;
        for (AttributeHistogramBin bin : mBins)
        {
            result += bin.getNumRows();
        }
        return result;
    }
    
    @Override
    public double getNumValues() 
    {
        double result = 0;
        for (AttributeHistogramBin bin : mBins)
        {
            result += bin.getNumValues();
        }
        return result;
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
        AttributeHistogramBin bin = findBin((Number)constant);
        if (bin != null && !bin.isEmpty())
        {
            HistogramBasedAttributeStatistics result =
                new HistogramBasedAttributeStatistics();
            result.addBin(bin.processEqualsConstant((Number)constant));
            return result;
        }
        else
        {
            return ScalarAttributeStatistics.ZERO;
        }
        
    }
    

    @Override
    public AttributeStatistics processInequalityConstant(
        ArithmeticOperator op,
        Object constant)
    {
        HistogramBasedAttributeStatistics result = 
            new HistogramBasedAttributeStatistics();
        
        for(AttributeHistogramBin bin : mBins)
        {
            int comparison = bin.containsValue((Number) constant);
            
            if (op == ArithmeticOperator.GREATER_THAN || 
                op == ArithmeticOperator.GREATER_THAN_OR_EQUAL )
            {
                comparison = comparison * -1;
            }

            switch(comparison)
            {
            case 1 :
                result.addBin(bin);
                break;
            case 0 :
                AttributeHistogramBin newBin = 
                    bin.processInequalityConstant(op, (Number) constant);
                if (newBin != null && !newBin.isEmpty())
                {
                    result.addBin(newBin);
                }
                break;
            }
        }
        if (result.isEmpty())
        {
            return ScalarAttributeStatistics.ZERO;
        }
        return result;
    }

    @Override
    public AttributeStatistics processNotEqualConstant(
        Object constant)
    {
        HistogramBasedAttributeStatistics result =
            new HistogramBasedAttributeStatistics();

        AttributeHistogramBin containingBin = findBin((Number)constant);
        
        for( AttributeHistogramBin bin : mBins)
        {
            if (bin == containingBin)
            {
                AttributeHistogramBin newBin = 
                    bin.processNotEqualsConstant((Number)constant);
                if (newBin != null && !newBin.isEmpty())
                {
                    result.addBin(newBin);
                }
            }
            else
            {
                result.addBin(bin);
            }
        }
        if (result.isEmpty())
        {
            return ScalarAttributeStatistics.ZERO;
        }
        return result;
    }
    
    
    private AttributeHistogramBin findBin(Number value)
    {
        for(AttributeHistogramBin bin : mBins)
        {
            if (bin.containsValue(value) == 0)
            {
                return bin;
            }
        }
        return null;
    }

    public AttributeStatistics rescale(double newNumRows)
    {
        double numRows = getNumRows();
        
        if (numRows == newNumRows) return this;
        
        HistogramBasedAttributeStatistics result = 
            new HistogramBasedAttributeStatistics();
        double factor = newNumRows / numRows;
      
        for(AttributeHistogramBin bin : mBins)
        {
            double newBinRows = bin.getNumRows() * factor;
            result.addBin(bin.rescale(newBinRows));
        }    
        result.setNumNulls(mNumNulls * factor);
        return result;
    }
    
    /**
     * Gets the fraction of the number of rows that satisfy the condition
     * with respect to the given range.
     * <p>
     * For example, if the range is 0 to 10 and the operator is less than
     * then this method will estimate the fraction on the rows in the
     * histogram for which, given a value v in the range 0 to 10, v < x is
     * true where x is the value of the histogramed attribute for that row. 
     * 
     * @param op     arithmetic operator
     * @param range  range
     * 
     * @return fraction of the total number of rows in this histogram that 
     *         fall into the range.  It is a value between 0 and 1 inclusive.
     */
    public double fractionOfRowsSatifyingCondition(
        ArithmeticOperator op, AttributeHistogramRange range)
    {
        switch(op)
        {
        case EQUAL:
            return fractionOfRowsSatisfyingEqualCondition(range);
            
        case LESS_THAN:
        case LESS_THAN_OR_EQUAL:
        case GREATER_THAN:
        case GREATER_THAN_OR_EQUAL:
            return fractionOfRowsSatisfyingInequalityCondition(op, range);

        case NOT_EQUAL:
            return 1.0-fractionOfRowsSatisfyingEqualCondition(range);
            
        default:
            throw new RuntimeException(
                "Arithmetic Operator " + op + " not handled");
        }
    }
    
    /**
     * Calculates the fraction of the rows in the histogram that are likely
     * to satisfy the equality condition for a value in the given range.
     * <p>
     * For example, if the range is 0 to 10 then this method will estimate the 
     * fraction on the rows in the histogram for which, given a value v in the 
     * range 0 to 10, v = x is true where x is the value of the histogramed 
     * attribute for that row.  
     * 
     * @param range  range
     * 
     * @return fraction of the rows in the histogram that are likely to satisfy
     *         the equality condition.  This will be a value between 0 and 1 
     *         inclusive. If there are no rows in the histogram then 0 will be 
     *         returned.
     */
    private double fractionOfRowsSatisfyingEqualCondition(
        AttributeHistogramRange range)
    {
        double numRows = getNumRows();
        if (numRows == 0) return 0;
        
        double total = 0;
        for (AttributeHistogramBin bin : mBins)
        {
            AttributeHistogramBin subBin = 
                HistogramBinAligner.generateSubBin(bin, range);
            
            if (subBin.getNumValues() != 0)
            {
                total += subBin.getNumRows()/subBin.getNumValues();
            }
        }
        return total/numRows;
    }
    
    /**
     * Calculates the fraction of the rows in the histogram that are likely
     * to satisfy the given condition for a value in the given range.
     * <p>
     * For example, if the range is 0 to 10 and the operator is less than
     * then this method will estimate the fraction on the rows in the
     * histogram for which, given a value v in the range 0 to 10, v < x is
     * true where x is the value of the histogramed attribute for that row.  
     * 
     * @param op     arithmetic operator
     * @param range  range
     * 
     * @return fraction of the rows in the histogram that are likely to satisfy
     *         the given condition.  This will be a value between 0 and 1 
     *         inclusive. If there are no rows in the histogram then 0 will be 
     *         returned.
     */
    private double fractionOfRowsSatisfyingInequalityCondition(
        ArithmeticOperator op,
        AttributeHistogramRange range)
    {
        double numRows = getNumRows();
        if (numRows == 0) return 0;

        double midpoint = 
            range.getMin().getPoint() + 
            (range.getMax().getPoint()-range.getMin().getPoint())/2.0;
        
        double total = 0;
        for(AttributeHistogramBin bin : mBins)
        {
            int comparison = bin.containsValue(midpoint);
            
            if (op == ArithmeticOperator.GREATER_THAN || 
                op == ArithmeticOperator.GREATER_THAN_OR_EQUAL )
            {
                comparison = comparison * -1;
            }

            switch(comparison)
            {
            case -1 :
                total += bin.getNumRows();
                break;
            case 0 :
                total += bin.countRowsSatisfyingOperatorConstant(op, midpoint);
                break;
            }
        }

        return total/numRows;
    }

}
