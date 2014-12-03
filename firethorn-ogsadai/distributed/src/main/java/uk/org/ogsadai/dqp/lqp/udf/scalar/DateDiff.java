// Copyright (c) The University of Edinburgh, 2009-2010.
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

import org.joda.time.DateMidnight;
import org.joda.time.Days;

import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;
/**
 * Difference between Dates function.
 * <p>
 * Function will return a number of days between two Date/Timestamps using
 * the date part only, not any additional time information.
 * <p>
 * The return type will be an int representing the number of days.
 * <p>
 * Return value will be negative if the first date chronologically precedes 
 * the second date, positive if first date is chronologically after the 
 * second date.
 * <p>
 * Operates whole number of days only.
 * <p>
 * 
 * @author The OGSA-DAI Project Team
 */
public class DateDiff extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2010.";

    /** Date value one. */
    private DateMidnight mValueOne = null;
    
    /** Date value two. */
    private DateMidnight mValueTwo = null;
        
    /**
     * Constructor for DateDiff.
     */
    public DateDiff()
    {
        super(2);
    }

    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param dateDiff instance from which to copy state.
     */
    public DateDiff(DateDiff dateDiff)
    {
        this();
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "DateDiff";
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "DATEDIFF(" 
            + (mValueOne) 
            + ", " + (mValueTwo) + ")";
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
        switch(types[0])
        {
            case TupleTypes._DATE:
            case TupleTypes._TIMESTAMP:
                break;
            default:  
                throw new TypeMismatchException(types[0]);
        }
        switch(types[1])
        {
            case TupleTypes._DATE:
            case TupleTypes._TIMESTAMP: 
                break;
            default:  
                throw new TypeMismatchException(types[1]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        // TODO Auto-generated method stub
        return TupleTypes._INT;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult()
    {
        if(mValueOne==null || mValueTwo==null)
        {
            // if either value is null, result is null
            return Null.VALUE;
        }
        else
        {   
            // Turns result negative to match how DB datediff function works
            // for example mysql datediff
            // whole days only
            return -Days.daysBetween(mValueOne, mValueTwo).getDays();
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
            mValueOne = new DateMidnight(currentParameter);
        }
        else
        {
            mValueOne=null;
        }
        
        currentParameter = parameters[1];
        if((currentParameter != null) && (currentParameter != Null.VALUE))
        {
            mValueTwo = new DateMidnight(currentParameter);
        }
        else
        {
            mValueTwo=null;
        }
    }
}
