// Copyright (c) The University of Edinburgh, 2010-2012.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END


package uk.org.ogsadai.tuple.join;

import java.util.Iterator;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Base class for joins.
 * 
 * @author The OGSA-DAI Project Team.
 */
public abstract class Join
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010-2012";
    
    /** The join condition. */
    protected Expression mCondition;
    
    /** Indicates that the left side of the join will be stored. */
    protected boolean mStoreLeft = true;
    
    /** Tuple metadata of the input and output data sets. */
    protected TupleMetadata mLeft, mRight;
    protected TupleMetadata mJoined;
    
    /**
     * Sets the join condition.
     * 
     * @param condition
     *            condition expression
     */
    public void setCondition(Expression condition)
    {
        mCondition = condition;
    }
    
    /**
     * Indicates that the tuple list that is stored is on the right.
     * 
     * @param storeRight
     *            <code>true</code> if the second (right hand side) tuple list
     *            is stored
     */
    public void storeRightTuples(boolean storeRight)
    {
        mStoreLeft = !storeRight;
    }

    /**
     * Configures the join condition expression with the given metadata.
     * 
     * @param left
     *            metadata for the left input stream
     * @param right
     *            metadata for the right input stream
     * @throws ColumnNotFoundException
     *             if a column referenced by the condition does not exist in the
     *             metadata
     * @throws TypeMismatchException
     *             if a column type is not supported when evaluating expressions
     * @throws IncomparableTypesException
     *             if the join condition contains a comparison expression which
     *             compares incompatible types
     * @throws ConfigurationException 
     */
    public void configure(TupleMetadata left, TupleMetadata right)
        throws ColumnNotFoundException, 
               TypeMismatchException,
               IncomparableTypesException,
               ConfigurationException
    {
        mLeft = left;
        mRight = right;
        mJoined = JoinUtilities.merge(left, right);
        mCondition.configure(mJoined);
    }

    /**
     * Stores tuples from one side of the join.
     * 
     * @param iterator
     *            tuple list iterator
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    public void storeTuples(TupleListIterator iterator)
        throws ActivityUserException, 
               ActivityProcessingException,
               ActivityTerminatedException
    {
        Tuple tuple;
        while ((tuple = (Tuple) iterator.nextValue()) != null)
        {
            storeTuple(tuple);
        }
    }

    /**
     * Stores tuples from one side of the join.
     * 
     * @param iterable
     *            tuple list iterator
     */
    public void storeTuples(Iterable<Tuple> iterable)
        throws ActivityUserException, 
               ActivityProcessingException,
               ActivityTerminatedException
    {
        for (Tuple tuple : iterable)
        {
            storeTuple(tuple);
        }
    }

    /**
     * Store a tuple. Must be overriden by subclasses.
     * 
     * @param tuple
     *            input
     */
    protected abstract void storeTuple(Tuple tuple)         
        throws ActivityUserException, 
               ActivityProcessingException,
               ActivityTerminatedException;


    /**
     * Returns all candidate matches for the given tuple.
     * 
     * @param tuple
     *            input tuple
     * @return list of candidates
     */
    protected abstract Iterator<Tuple> getCandidateMatches(Tuple tuple);
    
    public TupleMetadata getJoinMetadata()
    {
        return mJoined;
    }

    /**
     * Produces a join between the given tuple and the stored tuples if they
     * match the condition.
     * 
     * @param tuple
     *            input tuple
     * @return iterates over the resulting joined tuples
     */
    public Iterable<Tuple> join(Tuple tuple)
    {
        if (mStoreLeft)
        {
            return new JoinedTupleIterator(tuple);
        }
        else
        {
            return new RightJoinedTupleIterator(tuple);
        }
    }
    
    /**
     * Indicates to the join implementation that there are no more streamed
     * tuples and the join is completed. The implementation may override this
     * method to take care of any cleaning up it needs to do.
     */
    public void close() 
    {
    }
    
    /**
     * Joins a tuple with the candidate matches and iterates over the joined
     * tuples for which the condition holds true. By default, the tuple will be
     * joined on the right.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class JoinedTupleIterator implements Iterable<Tuple>, Iterator<Tuple>
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        /** List of candidate matches. */
        private Iterator<Tuple> mCandidateMatches;
        /** Tuple to join. */
        private Tuple mTuple;
        /** Next output tuple or <code>null</code> if none is available. */ 
        private Tuple mNext;
        
        /**
         * Constructor.
         * @param tuple the tuple to join
         */
        public JoinedTupleIterator(Tuple tuple)
        {
            mTuple = tuple;
            mCandidateMatches = getCandidateMatches(tuple);
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            mNext = getNext();
            return (mNext != null);
        }

        /**
         * {@inheritDoc}
         */
        public Tuple next()
        {
            return mNext;
        }

        /**
         * Returns the next matching tuple.
         * 
         * @return next tuple or <code>null</code> if there are no more matching
         *         tuples
         */
        private Tuple getNext()
        {
            try
            {
                while (mCandidateMatches.hasNext())
                {
                    Tuple candidate = mCandidateMatches.next();
                    Tuple product = createProductTuple(candidate, mTuple);
                    Boolean evaluation = mCondition.evaluate(product);
                    if (evaluation != null && evaluation)
                    {
                        return product;
                    }
                }
            }
            catch(ExpressionEvaluationException e)
            {
                // TODO: Can we do something sensible here?
                throw new RuntimeException(e);
            }
            return null;
        }
        
        protected Tuple createProductTuple(Tuple candidate, Tuple tuple)
        {
            return new SimpleTuple(candidate, mTuple);
        }

        /**
         * {@inheritDoc}
         * @throws UnsupportedOperationException
         *             always
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        public Iterator<Tuple> iterator()
        {
            return this;
        }
        
    }

    /**
     * Creates a product tuple (tuple, candidate) so that the tuple is on the
     * right side.
     *
     * @author The OGSA-DAI Project Team.
     */
    private class RightJoinedTupleIterator extends JoinedTupleIterator
    {
        
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2009";

        public RightJoinedTupleIterator(Tuple tuple) 
        {
            super(tuple);
        }
        
        protected Tuple createProductTuple(Tuple candidate, Tuple tuple)
        {
            return new SimpleTuple(tuple, candidate);
        }
        
    }

}
