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

import java.sql.Time;
import java.sql.Timestamp;

import org.joda.time.DateTime;

import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to extract the minute of an hour from a timestamp or time.
 * Example use, GetMinuteOfHourFromTime(timestampColumn), GetMinuteOfHourFromTime(timeColumn).
 * 
 * @author The OGSA-DAI Project Team
 */
public class GetMinuteOfHourFromTime extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";

    /** Date time to store the given value. */
    private DateTime mDateTime;
    /** Type of data we expect to receive. */
    private int mType;
    
    /**
     * Constructor.
     */
    public GetMinuteOfHourFromTime()
    {
        super(1); // 1 parameter
    }
    
    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param getMinuteOfHourFromTime instance from which to copy state.
     */
    public GetMinuteOfHourFromTime(
        GetMinuteOfHourFromTime getMinuteOfHourFromTime)
    {
        this();
        mType = getMinuteOfHourFromTime.mType;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "GetMinuteOfHourFromTime";
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
        mType = types[0];
        switch (mType)
        {
            case TupleTypes._TIME:
            case TupleTypes._TIMESTAMP:
                break;
            default:
                throw new TypeMismatchException(mType);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._LONG;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mDateTime == null)
        {
            return Null.VALUE;
        }
        else
        {
            return new Long(mDateTime.getMinuteOfHour());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object parameter = parameters[0];
        
        if (parameter == Null.VALUE)
        {
            mDateTime = null;
        }
        else
        {
            switch(mType)
            {
                case TupleTypes._TIME:
                    Time time = (Time) parameter;
                    mDateTime = new DateTime(time.getTime());
                    break;
                case TupleTypes._TIMESTAMP:
                    Timestamp ts = (Timestamp) parameter;
                    mDateTime = new DateTime(ts.getTime());
                    break;
            }
        }
    }
}
