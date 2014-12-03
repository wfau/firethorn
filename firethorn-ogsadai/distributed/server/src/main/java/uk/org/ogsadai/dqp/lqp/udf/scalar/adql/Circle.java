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
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Circle function for astronomy ADQL.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class Circle extends LogicalExecutableFunctionBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    private uk.org.ogsadai.dqp.spatial.Circle mCircle;
    
    /**
     * Constructor.
     */
    public Circle() 
    {
        super(4);
    }
    
    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public Circle(Circle circle) 
    {
        this();
    }

    
    @Override
    public String getName()
    {
        return "CIRCLE";
    }
    
    @Override
    public FunctionType getType()
    {
        return FunctionType.SQL_SCALAR;
    }
    
    @Override
    public void configure(int... types) throws TypeMismatchException
    {
        switch(types[0])
        {
            case TupleTypes._STRING:
                break;
            default:  
                throw new TypeMismatchException(types[0]);
        }
        switch(types[1])
        {
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
                break;
            default:  
                throw new TypeMismatchException(types[1]);
        }
        switch(types[2])
        {
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
                break;
            default:  
                throw new TypeMismatchException(types[1]);
        }
        switch(types[3])
        {
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
                break;
            default:  
                throw new TypeMismatchException(types[1]);
        }
    }
    
    @Override
    public void put(Object... parameters)
    {
        mCircle = new uk.org.ogsadai.dqp.spatial.Circle(
            ((Number) parameters[1]).doubleValue(),
            ((Number) parameters[2]).doubleValue(),
            ((Number) parameters[3]).doubleValue());
    }
    
    @Override
    public Object getResult()
    {
        return mCircle;
    }
    
    @Override
    public int getOutputType()
    {
        return TupleTypes._OBJECT;
    }
}
