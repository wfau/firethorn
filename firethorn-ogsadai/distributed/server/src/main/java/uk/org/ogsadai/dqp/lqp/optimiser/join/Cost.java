package uk.org.ogsadai.dqp.lqp.optimiser.join;

public class Cost
{
    private double mReadCount;
    private double mLookupCount;
    private double mQueryCount;
    private double mMaterialiseCount;

    public Cost() 
    {
    }
    
    public void setReads(double count)
    {
        mReadCount = count;
    }
    
    public void setLookups(double count)
    {
        mLookupCount = count;
    }
    
    public void setQueries(double count)
    {
        mQueryCount = count;
    }
    
    public void setMaterialise(double count)
    {
        mMaterialiseCount = count;
    }
    
    public double getCost()
    {
        return mReadCount + mLookupCount + mQueryCount + mMaterialiseCount;
    }
}
