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


package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.List;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.udf.ExecutableFunction;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.tuple.TypeMismatchException;


/**
 * Dummy DQP function used to identify where in an SQL query attribute values 
 * are to be inserted when executing a batch join.  This function is inserted 
 * by BatchTableScanOptimiser and detected by BatchTableScanBuilder.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DQPReplace implements ExecutableFunction
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    private List<ArithmeticExpression> mParameters;
    
    public DQPReplace()
    {
    }
    
    public DQPReplace(DQPReplace dqpReplace)
    {
        this();
    }

    @Override
    public Object getAnnotation(String key)
    {
        return null;
    }

    @Override
    public void addAnnotation(String key, Object value)
    {
    }

    @Override
    public void removeAnnotation(String key)
    {
    }

    @Override
    public Map<String, Object> getAnnotations()
    {
        return null;
    }
    
    @Override
    public void initialise(List<ArithmeticExpression> parameters)
    {
        mParameters = parameters;
    }

    @Override
    public String getName()
    {
        return "DQP_REPLACE";
    }

    @Override
    public FunctionType getType()
    {
        return FunctionType.SQL_SCALAR;
    }

    @Override
    public List<ArithmeticExpression> getParameters()
    {
        return mParameters;
    }

    @Override
    public String toSQL()
    {
        return "$REPLACE";
    }

    @Override
    public void configure(int... types) throws TypeMismatchException
    {
    }

    @Override
    public void put(Object... parameters)
    {
    }

    @Override
    public Object getResult()
    {
        return null;
    }

    @Override
    public int getOutputType()
    {
        return 0;
    }
}