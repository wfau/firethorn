package uk.org.ogsadai.dqp.lqp.cardinality;

public class AttributeHistogramRange
{
    private AttributeHistogramBinEndpoint mMin;
    private AttributeHistogramBinEndpoint mMax;
    
    public AttributeHistogramRange(
        AttributeHistogramBinEndpoint min, AttributeHistogramBinEndpoint max)
    {
        mMin = min;
        mMax = max;
    }
    
    public AttributeHistogramBinEndpoint getMin()
    {
        return mMin;
    }
    
    public AttributeHistogramBinEndpoint getMax()
    {
        return mMax;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(mMin.getIsInclusive() ? "[" : "(");
        sb.append(mMin.getPoint());
        sb.append(",");
        sb.append(mMax.getPoint());
        sb.append(mMax.getIsInclusive() ? "]" : ")");
        return sb.toString();
    }
}
