package uk.org.ogsadai.activity.relational.pipelinedjoin;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.relational.PipelinedTupleJoinActivity;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.join.JoinRelation;

/**
 * Gets woken up to perform reactive and cleanup stage.
 * 
 * @author bdobrzel
 */
public class Controller
{
    Lock mLock;

    BlockWriter mOutput;
    
    boolean mIsStreaming;
    
    Exception mException;

    public enum Relation
    {
        LEFT, RIGHT
    };

    JoinRelation mLeft;

    JoinRelation mRight;

    boolean mFirstInsert;
    
    long mSequenceStamp;
    
    /** Logger for this class. */
    private static final DAILogger LOG =
        DAILogger.getLogger(Controller.class);

    public Controller(JoinRelation left, JoinRelation right, BlockWriter output)
    {
        mOutput = output;
        mLeft = left;
        mRight = right;

        mLock = new ReentrantLock();
        
        mIsStreaming = true;
        mFirstInsert = true;
        
        LOG.debug("LEFT: " + left.getStoredRelationMetadata());
        LOG.debug("RIGHT: " + right.getStoredRelationMetadata());
        
        PipelinedTupleJoinActivity.t1 = System.currentTimeMillis();
    }

    int count = 0, last;
    
    public void insert(Tuple tuple, Relation relation)
        throws PipeClosedException, PipeIOException, PipeTerminatedException
    {
        Iterator<Tuple> matches;
        
        // We need to lock when accessing in memory relations
        mLock.lock();
        mSequenceStamp++;
        if(mFirstInsert)
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            TupleMetadata tmd =
                new SimpleTupleMetadata(mLeft.getStoredRelationMetadata(),
                    mLeft.getProbingRelationMetadata());
            mOutput.write(new MetadataWrapper(tmd));
            mFirstInsert = false;
        }
        matches = insertAndProbe(tuple, relation);
        
        // Pipe has its own synchronization
        while(matches.hasNext())
        {
            Tuple joined =
                (relation == Relation.LEFT) ? joinTuples(tuple, matches.next())
                    : joinTuples(matches.next(), tuple);
            mOutput.write(joined);
            count++;
            if(count % 100 == 0)
            {
                System.out.println("BENCH\t" + count + "\t"
                    + (System.currentTimeMillis() - PipelinedTupleJoinActivity.t1)
                    / 1000.0);
            }
        }
        mLock.unlock();
    }

    public void exceptionEvent(Exception e)
    {
        e.printStackTrace();
        mIsStreaming = false;
        mException = e;
    }
    
    public void finalize() throws PipeClosedException, PipeIOException,
        PipeTerminatedException
    {
        mOutput.write(ControlBlock.LIST_END);
    }
    
    public boolean isStreaming()
    {
        return mIsStreaming;
    }
    
    public Exception getException()
    {
        return mException;
    }
    
    private Iterator<Tuple> insertAndProbe(Tuple tuple, Relation relation)
    {
        JoinRelation storeRelation;
        JoinRelation probeRelation;
        
        if(relation == Relation.LEFT)
        {
            storeRelation = mLeft;
            probeRelation = mRight;
        }
        else
        {
            storeRelation = mRight;
            probeRelation = mLeft;
        }
        storeRelation.store(tuple);
        
        return probeRelation.probe(tuple);
    }
    
    private Tuple joinTuples(Tuple t, Tuple u)
    {
        // This may have alternative logic depending on the join type
        
        Tuple joined = new SimpleTuple(t, u);
        return joined;
    }
    
}
