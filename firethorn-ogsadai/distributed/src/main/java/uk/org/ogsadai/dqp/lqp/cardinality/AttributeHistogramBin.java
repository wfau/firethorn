package uk.org.ogsadai.dqp.lqp.cardinality;


/**
 *
 * 
 * @author ahume
 */
public class AttributeHistogramBin
{
    private final AttributeHistogramRange mRange;
    private final double mNumRows;
    private final double mNumValues;
    
    public AttributeHistogramBin(
        AttributeHistogramRange range,
        double numRows, 
        double numValues)
    {
        mRange = range;
        mNumRows = numRows;
        mNumValues = numValues;
    }

    public AttributeHistogramBin(
            double min, 
            boolean isMinInclusive, 
            double max, 
            boolean isMaxInclusive, 
            double numRows, 
            double numValues)
    {
        mRange = new AttributeHistogramRange(
            new AttributeHistogramBinEndpoint(min, isMinInclusive),
            new AttributeHistogramBinEndpoint(max, isMaxInclusive));
        mNumRows = numRows;
        mNumValues = numValues;
    }

    public boolean isKey()
    {
        return mNumRows == mNumValues;
    }
    
    public boolean isEmpty()
    {
        return mNumRows == 0;
    }
    
    public double getNumRows() 
    {
        return mNumRows;
    }

    public double getNumValues() 
    {
        return mNumValues;
    }
    
    public AttributeHistogramRange getRange()
    {
        return mRange;
    }

    public AttributeHistogramBin rescale(double newNumRows)
    {
        double newNumValues = mNumValues;
        if (newNumValues > newNumRows) newNumValues = newNumRows;
        
        return new AttributeHistogramBin(mRange, newNumRows, newNumValues);
    }
    
//
//    public void processGroupBy() 
//    {
//        mNumRows = mNumValues;
//    }
    
    /**
     * Tests if the bin contains the given value.
     * 
     * @param value value
     * 
     * @return 0 if the bin contains the value, -1 if the value is less than
     *         the bin range, 1 if the value is greater than the bin range.
     */
    public int containsValue(Number obj)
    {
        double value = ((Number)obj).doubleValue();
        
        if(value < mRange.getMin().getPoint())
        {
            return -1;
        }
        else if (value == mRange.getMin().getPoint())
        {
            if (mRange.getMin().getIsInclusive())
            {
                return 0;
            }
            else 
            {
                return -1;
            }
        }
        else if (value < mRange.getMax().getPoint())
        {
            return 0;
        }
        else if (value == mRange.getMax().getPoint())
        {
            if (mRange.getMax().getIsInclusive())
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        else 
        {
            return 1;
        }
    }
    
    public double countRowsSatisfyingOperatorConstant(ArithmeticOperator op, Number constant)
    {
        switch(op)
        {
        case EQUAL:
            return countRowsSatisfyingEqualsConstant(constant);
        case LESS_THAN:
        case LESS_THAN_OR_EQUAL:
        case GREATER_THAN:
        case GREATER_THAN_OR_EQUAL:
            return countRowsSatisfyingInequalityConstant(op, constant);
        case NOT_EQUAL:
            return countRowsSatisfyingNotEqualsConstant(constant);
        default:
            throw new RuntimeException("Unsupported operator: " + op);
        }
    }
        
    public AttributeHistogramBin processEqualsConstant(Number obj)
    {
        double constant = obj.doubleValue();
        return new AttributeHistogramBin(
            constant,
            true,
            constant,
            true,
            mNumRows/mNumValues,
            1);
    }

    private double countRowsSatisfyingEqualsConstant(Number obj)
    {
        if (mNumRows == 0) return 0;
        return mNumRows/mNumValues;
    }
    
    public AttributeHistogramBin processNotEqualsConstant(Number obj)
    {
        double constant = obj.doubleValue();
        double factor = (mNumValues-1)/mNumValues;
        
        boolean isMinInclusive = mRange.getMin().getIsInclusive();
        boolean isMaxInclusive = mRange.getMax().getIsInclusive();
        if (constant == mRange.getMin().getPoint()) isMinInclusive = false;
        if (constant == mRange.getMax().getPoint()) isMaxInclusive = false;
        
        if (mNumValues > 1)
        {
            return new AttributeHistogramBin(
                mRange.getMin().getPoint(),
                isMinInclusive,
                mRange.getMax().getPoint(),
                isMaxInclusive,
                factor * mNumRows,
                mNumValues -1);
        }
        else
        {
            return null;
        }
    }
    
    private double countRowsSatisfyingNotEqualsConstant(Number obj)
    {
        if (mNumRows == 0) return 0;

        double factor = (mNumValues-1)/mNumValues;
        return factor * mNumRows;
    }

    public AttributeHistogramBin processInequalityConstant(
        ArithmeticOperator op, Number obj)
    {
        if (mRange.getMin().getPoint() == mRange.getMax().getPoint() )
        {
            if (op == ArithmeticOperator.GREATER_THAN || 
                op == ArithmeticOperator.LESS_THAN)
            {
                // Can only have one value in this bin and we want those less
                // than it so that must give zero rows and zero values
                return null;
            }
            else
            {
                // else just keep the bin as it is
                return this;
            }
        }
        else
        {
            double constant = obj.doubleValue();
            double factor = 1;

            double min = mRange.getMin().getPoint();
            double max = mRange.getMax().getPoint();
            boolean isMinInclusive = mRange.getMin().getIsInclusive();
            boolean isMaxInclusive = mRange.getMax().getIsInclusive();
            
            switch(op)
            { 
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
                factor = 
                    (constant-mRange.getMin().getPoint())/
                    (mRange.getMax().getPoint()-mRange.getMin().getPoint());
                max = constant;
                isMaxInclusive = (op == ArithmeticOperator.LESS_THAN_OR_EQUAL);
                break;
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                factor = 
                    (mRange.getMax().getPoint()-constant)/
                    (mRange.getMax().getPoint()-mRange.getMin().getPoint());
                min = constant;
                isMinInclusive = 
                    (op == ArithmeticOperator.GREATER_THAN_OR_EQUAL);
                break;
            }
            
            return new AttributeHistogramBin(
                min, isMinInclusive, 
                max, isMaxInclusive, 
                factor * mNumRows, factor * mNumValues);
        }
    }
       
    private double countRowsSatisfyingInequalityConstant(
        ArithmeticOperator op, Number obj)
    {
        if (mRange.getMin().getPoint() == mRange.getMax().getPoint() )
        {
            if (op == ArithmeticOperator.GREATER_THAN || 
                op == ArithmeticOperator.LESS_THAN)
            {
                return 0;
            }
            else
            {
                return mNumRows;
            }
        }
        else
        {
            double constant = obj.doubleValue();
            double factor = 1;

            switch(op)
            { 
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
                factor = 
                    (constant-mRange.getMin().getPoint())/
                    (mRange.getMax().getPoint()-mRange.getMin().getPoint());
                break;
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                factor = 
                    (mRange.getMax().getPoint()-constant)/
                    (mRange.getMax().getPoint()-mRange.getMin().getPoint());
                break;
            }

            return factor * mNumRows;
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
       
        sb.append("HistogramBin( ").append(mRange);
        sb.append(" numRows: ").append(mNumRows).append(", numValues: ");
        sb.append(mNumValues).append(" )");
        return sb.toString();
    }
}
