// Copyright (c) The University of Edinburgh, 2009.
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

package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.sql.Date;
import java.sql.Timestamp;

import org.joda.time.DateMidnight;

import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to get the date from a timestamp.  Usage example: 
 * GetDate(timestampColumn).
 *
 * @author The OGSA-DAI Project Team
 */
public class GetDate extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "" +
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** 
     * The result of applying the function, or null if Null is to be returned. 
     */
    private Date mResult;

    /**
     * Constructor.
     */
    public GetDate()
    {
        super(1);  // 1 parameter
    }

    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param getDate instance from which to copy state.
     */
    public GetDate(GetDate getDate)
    {
        this();
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "GetDate";
    }

    /**
     * {@inheritDoc}
     */
    public FunctionType getType()
    {
        return FunctionType.UDF_SCALAR;
    }

    /**
     * {@inheritDoc}
     */
    public void configure(int... types) throws TypeMismatchException
    {
        if (types[0] != TupleTypes._TIMESTAMP)
        {
            throw new TypeMismatchException(types[0]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._DATE;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mResult == null)
        {
            return Null.VALUE;
        }
        else
        {
            return mResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        if (parameters[0] == Null.VALUE)
        {
            mResult = null;
        }
        else
        {
            Timestamp timestamp = (Timestamp) parameters[0];
            DateMidnight dateMidnight = new DateMidnight(timestamp.getTime());
            mResult = new Date(dateMidnight.getMillis());
        }
    }
}
