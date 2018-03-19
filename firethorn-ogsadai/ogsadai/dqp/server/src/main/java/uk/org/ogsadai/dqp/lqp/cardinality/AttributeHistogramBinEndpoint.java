package uk.org.ogsadai.dqp.lqp.cardinality;

public class AttributeHistogramBinEndpoint
{
    // wish these to be private and this class to be immutable ideally

    private double mPoint;
    private boolean misInclusive;
    
    public AttributeHistogramBinEndpoint(double point, boolean isInclusive)
    {
        mPoint = point;
        misInclusive = isInclusive;
    }
    
    public double getPoint()
    {
        return mPoint;
    }
    
    public boolean getIsInclusive()
    {
        return misInclusive;
    }
    
    public void setPoint(double point)
    {
        mPoint = point;
    }
    
    public void setIsInclusive(boolean isInclusive)
    {
        misInclusive = isInclusive;
    }
}
