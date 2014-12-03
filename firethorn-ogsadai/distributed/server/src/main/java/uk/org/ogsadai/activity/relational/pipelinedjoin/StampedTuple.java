package uk.org.ogsadai.activity.relational.pipelinedjoin;

import java.util.LinkedList;

import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

public class StampedTuple extends SimpleTuple
{
    int mSize;

    public StampedTuple(Tuple t, TupleMetadata tmd, long arrivingStamp)
    {
        mElements = new LinkedList<Object>();
        mSize = tmd.getColumnCount();

        for (int i = 0; i < mSize; i++)
            mElements.add(t.getObject(i));

        mElements.add(Long.valueOf(arrivingStamp));
        mElements.add(Long.valueOf(-1));

        mSize += 2;
    }

    public TupleMetadata getStampedMetadata()
    {
        return null;
    }

    public void setDepartureStamp(long stamp)
    {
        mElements.remove(mSize - 1);
        mElements.add(Long.valueOf(stamp));
    }

    public long getArrivalStamp()
    {
        return (Long) mElements.get(mSize - 2);
    }

    public long getDepartureStamp()
    {
        return (Long) mElements.get(mSize - 1);
    }
    
    public Tuple getOriginalTuple()
    {
        return new SimpleTuple(mElements.subList(0, mSize-2));
    }
}
