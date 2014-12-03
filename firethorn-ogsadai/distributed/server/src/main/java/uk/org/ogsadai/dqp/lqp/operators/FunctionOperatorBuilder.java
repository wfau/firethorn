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

package uk.org.ogsadai.dqp.lqp.operators;

import java.lang.reflect.Constructor;
import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.udf.Function;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalFunction;
import uk.org.ogsadai.dqp.lqp.udf.repository.NoSuchFunctionException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;

/**
 * Builds operators for relation valued functions.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FunctionOperatorBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** The compiler configuration. */
    private CompilerConfiguration mCompilerConfiguration;

    /** The function repository. */
    private FunctionRepository mFunctionRepository;

    /**
     * Creates new operator builder.
     * 
     * @param compilerConfiguration
     *            compiler configuration
     * @param functionRepository
     *            function repository
     */
    public FunctionOperatorBuilder(CompilerConfiguration compilerConfiguration,
        FunctionRepository functionRepository)
    {
        mCompilerConfiguration = compilerConfiguration;
        mFunctionRepository = functionRepository;
    }

    /**
     * Builds an operator for a relation valued function. Tries to map a
     * function to a specific operator or if not defined created a generic
     * operator.
     * 
     * @param functionName
     *            function name
     * @param parameterList
     *            list of parameters
     * @param childCount
     *            number of relation valued inputs
     * @return a specific operator or a generic operator encapsulating relation
     *         valued function
     * @throws LQPException
     *          if there is a problem building the query plan 
     */
    public Operator buildRelFunctionOperator(String functionName,
        List<ArithmeticExpression> parameterList, int childCount) throws LQPException
    {
        Class<? extends Operator> funcOpClass = mCompilerConfiguration
            .getFunctionOperatorClass(functionName);

        if (funcOpClass == null)
        {
            Function relFunction = null;
            try
            {
                relFunction = mFunctionRepository
                    .getFunctionInstanceByName(functionName);
                relFunction.initialise(parameterList);

                if(childCount == 2)
                {
                    if (relFunction.getType() != FunctionType.UDF_REL_BINARY)
                    {
                        throw new LQPException(
                            "Given the input attributes - expected "
                                + functionName + " to be UDF_REL_BINARY");
                    }
                    return new BinaryRelFunctionOperator(
                        (LogicalFunction) relFunction);
                }
                else if(childCount == 1)
                {
                    if (relFunction.getType() != FunctionType.UDF_REL_UNARY)
                    {
                        throw new LQPException(
                            "Given the input attributes - expected "
                                + functionName + " to be UDF_REL_UNARY");
                    }
                    return new UnaryRelFunctionOperator(
                        (LogicalFunction) relFunction);
                }
                else if(childCount == 0)
                {
                    if (relFunction.getType() != FunctionType.UDF_REL_SCAN)
                    {
                        throw new LQPException(
                            "Given the input attributes - expected "
                                + functionName + " to be UDF_REL_SCAN");
                    }
                    return new ScanRelFunctionOperator(
                        (LogicalFunction) relFunction);
                }
            }
            catch (NoSuchFunctionException e)
            {
                e.printStackTrace();
                throw new LQPException(e);
            }
        }

        Constructor<? extends Operator> c = null;
        Operator operator = null;
        try
        {
            c = funcOpClass.getConstructor(new Class[] { List.class });
            operator = c.newInstance(parameterList);
        }
        // SecurityException, NoSuchMethodException, IllegalArgumentException,
        // InstantiationException, IllegalAccessException,
        // InvocationTargetException
        catch (Exception e)
        {
            e.printStackTrace();
            throw new LQPException(e);
        }

        return operator;
    }

}
