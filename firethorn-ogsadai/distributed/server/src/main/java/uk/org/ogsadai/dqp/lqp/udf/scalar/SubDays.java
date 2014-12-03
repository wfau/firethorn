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
import org.joda.time.DateTime;

import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Subtraction of Days function.
 * <p>
 * Function will subtract a number of days from a given timestamp 
 * or date object.
 * <p>
 * The return type will match the input type of the timestamp
 *  or date parameter. If the input is of type Date, 
 *  a date will be returned.
 * <p>
 * Operates subtraction of whole number of days only.
 * <p>
 * 
 * @author The OGSA-DAI Project Team
 */
public class SubDays extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** For determining if function is operating on a timestamp. */
    private boolean mIsTimeStamp = false;
    
    /** Timestamp for value to operate and return. */
    private Timestamp mResultTimestamp = null;
    
    /** Date for value to operate and return. */
    private Date mResultDate = null;
    
    /** number of days to subtract. */
    private int mSubValue;
    
    /**
     * Constructor for SubDays.
     */
    public SubDays()
    {
        super(2);
    }

    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param subDays instance from which to copy state.
     */
    public SubDays(SubDays subDays)
    {
        this();
        mIsTimeStamp = subDays.mIsTimeStamp;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "SubDays";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "SUBDAYS(" + (mIsTimeStamp?mResultTimestamp:mResultDate) + ", " + mSubValue + ")";
    }
    
    /**
     * {@inheritDoc}
     */
    public FunctionType getType()
    {
        // TODO Auto-generated method stub
        return FunctionType.UDF_SCALAR;
    }

    /**
     * {@inheritDoc}
     */
    public void configure(int... types) throws TypeMismatchException
    {
        // TODO Auto-generated method stub
        switch(types[0])
        {
            case TupleTypes._DATE:
                mIsTimeStamp=false;
                break;
            case TupleTypes._TIMESTAMP:
                mIsTimeStamp=true;
                break;
            default:  
                throw new TypeMismatchException(types[0]);
        }
        if (!TupleTypes.isNumeric(types[1]))
        {
            throw new TypeMismatchException(types[1]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        // TODO Auto-generated method stub
        return mIsTimeStamp?TupleTypes._TIMESTAMP:TupleTypes._DATE;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if (mIsTimeStamp)
        {
            if (mResultTimestamp == null)
            {
                return Null.VALUE;
            } else
            {
                return new Timestamp(((new DateTime(mResultTimestamp))
                        .minusDays(mSubValue)).getMillis());
            }
        } else
        {
            if (mResultDate == null)
            {
                return Null.VALUE;
            } else
            {
                return new Date(((new DateMidnight(mResultDate))
                        .minusDays(mSubValue)).getMillis());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters)
    {
        Object currentParameter = parameters[0];
        if((currentParameter != null) && (currentParameter != Null.VALUE))
        {
            if(mIsTimeStamp)
            {
                mResultTimestamp = (Timestamp) currentParameter;   
            }
            else
            {
                mResultDate = (Date) currentParameter;
            }
        }
        else
        {
            mResultTimestamp = null;
            mResultDate = null;
        }
        
        currentParameter = parameters[1];
        if((currentParameter != null) && (currentParameter != Null.VALUE))
        {
            mSubValue = ((Number)currentParameter).intValue();
        }
        else
        {
            mSubValue = 0;
        }
    }
}
