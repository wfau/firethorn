package uk.org.ogsadai.activity.relational.pipelinedjoin;

import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.relational.pipelinedjoin.Controller.Relation;
import uk.org.ogsadai.tuple.Tuple;

public class Streamer extends Thread
{
    TupleListIterator mTupleStream;

    Controller mController;

    Relation mRelation;

    public Streamer(Controller controller, TupleListIterator tupleStream,
        Relation relation)
    {
        mController = controller;
        mTupleStream = tupleStream;
        mRelation = relation;
    }

    @Override
    public void run()
    {
        Tuple tuple;
        try
        {
            while ((tuple = (Tuple) mTupleStream.nextValue()) != null
                && mController.isStreaming())
            {
                mController.insert(tuple, mRelation);
            }
        }
        // ActivityUserException, ActivityProcessingException,
        // ActivityTerminatedException, PipeClosedException, PipeIOException,
        // PipeTerminatedException
        catch (Exception e)
        {
            mController.exceptionEvent(e);
        }
    }
}
