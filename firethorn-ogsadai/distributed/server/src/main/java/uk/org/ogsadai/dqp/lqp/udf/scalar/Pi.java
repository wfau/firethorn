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


import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to get the value of PI.  Usage example: 
 * PI().
 *
 * @author The OGSA-DAI Project Team
 */
public class Pi extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "" +
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** 
     * The result of applying the function, or null if Null is to be returned. 
     */
    private Double mResult;

    /**
     * Constructor.
     */
    public Pi()
    {
        super(0);  // 1 parameter
    }

    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param pi instance from which to copy state.
     */
    public Pi(Pi pi)
    {
        this();
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "Pi";
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
       
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType()
    {
        return TupleTypes._DOUBLE;
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
       mResult = Math.PI;
        
    }
}
