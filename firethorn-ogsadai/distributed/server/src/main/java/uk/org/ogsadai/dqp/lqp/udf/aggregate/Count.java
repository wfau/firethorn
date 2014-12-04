// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.dqp.lqp.udf.aggregate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * Aggregation function COUNT. Values are counted only if they are not null.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Count extends SQLAggregateFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Current count. */
    private long mCount;
    
    /** 
     * Default constructor.
     */
    public Count()
    {
    }
    
    /**
     * Copy constructor. The current count is not copied.
     * 
     * @param count
     *            function to copy
     */
    public Count(Count count)
    {
        this();
    }
    
    // --------------------------
    // ExecutableFunction methods 
    // --------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(int... parameterTypes)
        throws TypeMismatchException
    {
        // we're not interested in the input type
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        if (parameter != Null.VALUE)
        {
            mCount++;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        return mCount;
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._LONG;
    }
    
    // ----------------------------
    // SerialisableFunction methods 
    // ----------------------------
    
    /**
     * {@inheritDoc}
     */
    public void merge(SerialisableFunction function)
    {
        if (function instanceof Count)
        {
            mCount += ((Count)function).mCount;
        }
    }

    /**
     * {@inheritDoc}
     */
    public SerialisableFunction deserialise(DataInputStream input) throws IOException
    {
        Count count = new Count();
        count.mCount = input.readLong();
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public void serialise(DataOutputStream output) throws IOException
    {
        output.writeLong(mCount);
    }

    // -----------------------
    // LogicalFunction methods 
    // -----------------------
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "COUNT";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "COUNT(value=" + mCount + ")";
    }



}
