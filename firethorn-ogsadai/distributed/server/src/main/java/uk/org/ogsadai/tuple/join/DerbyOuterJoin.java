// Copyright (c) The University of Edinburgh, 2012.
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

import java.util.Arrays;
import java.util.Iterator;

import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

public class DerbyOuterJoin extends DerbyJoin
{
    
    private Tuple mNullTuple;
    
    @Override
    public void configure(TupleMetadata left, TupleMetadata right)
        throws ColumnNotFoundException, 
               TypeMismatchException,
               IncomparableTypesException,
               ConfigurationException
    {
        super.configure(left, right);
        if (mStoreLeft)
        {
            mNullTuple = createNullTuple(left.getColumnCount());
        }
        else
        {
            mNullTuple = createNullTuple(right.getColumnCount());
        }
    }

    
    @Override
    public Iterable<Tuple> join(final Tuple tuple)
    {
        Iterable<Tuple> matches = super.join(tuple);
        final Iterator<Tuple> iterator = matches.iterator();
        return new Iterable<Tuple>() 
        {

            public Iterator<Tuple> iterator()
            {
                return new Iterator<Tuple>()
                {
                    private boolean mIsFirst = true;
                    private Tuple mNext;

                    public boolean hasNext()
                    { 
                        if (mIsFirst)
                        {
                            mIsFirst = false;
                            if (iterator.hasNext())
                            {
                                return true;
                            }
                            else
                            {
                                mNext = getNullJoinedTuple(tuple);
                                return true;
                            }
                        }
                        else
                        {
                            return iterator.hasNext();
                        }
                    }

                    public Tuple next()
                    {
                        if (mNext != null)
                        {
                            Tuple result = mNext;
                            mNext = null;
                            return result;
                        }
                        else
                        {
                            return iterator.next();
                        }
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                    
                };
            }
            
        };
    }
    
    private Tuple getNullJoinedTuple(Tuple tuple)
    {
        if (mStoreLeft)
        {
            return new SimpleTuple(mNullTuple, tuple); 
        }
        else
        {
            return new SimpleTuple(tuple, mNullTuple); 
        }

    }
    
    /**
     * Creates a null tuple with the given number of columns.
     * 
     * @param columnCount
     *            number of columns
     * @return a null tuple
     */
    private Tuple createNullTuple(int columnCount)
    {
        Object[] elements = new Object[columnCount];
        Arrays.fill(elements, Null.VALUE);
        return new SimpleTuple(elements);
    }
}
