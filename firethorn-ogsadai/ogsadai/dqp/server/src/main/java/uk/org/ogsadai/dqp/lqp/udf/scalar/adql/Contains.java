// Copyright (c) The University of Edinburgh, 2011.
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

package uk.org.ogsadai.dqp.lqp.udf.scalar.adql;

import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.dqp.spatial.Point;
import uk.org.ogsadai.dqp.spatial.Shape;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Contains function for astronomy ADQL.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class Contains extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    private int mResult;
    
    /**
     * Constructor.
     */
    public Contains() 
    {
        super(2);
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public Contains(Contains contains) 
    {
        this();
    }

    
    @Override
    public String getName()
    {
        return "CONTAINS";
    }
    
    @Override
    public FunctionType getType()
    {
        return FunctionType.SQL_SCALAR;
    }
    
    @Override
    public void configure(int... types) throws TypeMismatchException
    {
        for( int type: types)
        {
            if (type != TupleTypes._OBJECT)
            {
                throw new TypeMismatchException(type);
            }
        }
    }
    
    @Override
    public void put(Object... parameters)
    {
        Point p = (Point) parameters[0];
        Shape region = (Shape) parameters[1];
        
        mResult = region.contains(p) ?  1 :  0;
    }
    
    @Override
    public Object getResult()
    {
        return mResult;
    }
    
    @Override
    public int getOutputType()
    {
        return TupleTypes._INT;
    }
}
