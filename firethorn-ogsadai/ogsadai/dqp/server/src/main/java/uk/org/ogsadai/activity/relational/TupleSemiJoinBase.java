// Copyright (c) The University of Edinburgh, 2010.
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


package uk.org.ogsadai.activity.relational;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.join.Join;

/**
 * This is a parent class for semi-join and anti-join activites. The 
 * implementation of the two is basically similar except that semi-join writes 
 * tuples if there is a match while anti-join writes tuples if there 
 * isn't a match. So it makes sense to factor out the common code to a parent 
 * class.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>data2</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>condition</code> Type: String. This is a mandatory
 * input. The join condition.</li>
 * </ul>
 * <p>
 * Activity output:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * <code>join.implementation</code> - a mandatory configuration
 * setting providing the name of a join implementation.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
abstract class TupleSemiJoinBase 
    extends MatchedIterativeActivity
    implements ConfigurableActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";

    protected static final String INPUT_DATA_1 = "data1";
    protected static final String INPUT_DATA_2 = "data2";
    protected static final String INPUT_CONDITION = "condition";
    protected static final String OUTPUT_RESULT = "result";
    protected BlockWriter mOutput;

    protected static final Key JOIN_IMPLEMENTATION = 
        new Key("join.implementation");
    protected String mJoinName;

    /** Function repository, or null if functions are not supported. */
    protected FunctionRepository mFunctionRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedActivityInput(INPUT_CONDITION, String.class),
            new TupleListActivityInput(INPUT_DATA_1),
            new TupleListActivityInput(INPUT_DATA_2)
        };
                                 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    
        // Get the function repository if there is one.
        mFunctionRepository = null;
        if (OGSADAIContext.getInstance().containsKey(
            FunctionRepository.FUNCTION_REPOSITORY_KEY))
        {
            mFunctionRepository = 
                (FunctionRepository) OGSADAIContext.getInstance().get(
                    FunctionRepository.FUNCTION_REPOSITORY_KEY);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post-processing
    }
    
    /**
     * Returns the join condition as an expression tree.
     * 
     * @param condition
     *            join condition
     * @return root of the expression tree
     * @throws ActivityProcessingException
     * @throws ActivityUserException
     */
    protected Expression getCondition(String condition) 
        throws ActivityProcessingException, ActivityUserException
    {
        try
        {
            CommonTree ast = 
                SQLQueryParser.getInstance().parseSQLForCondition(condition);
            return ExpressionFactory.buildExpression(ast, mFunctionRepository);
        }
        catch (SQLParserException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }

    }
    
    /**
     * Writes a block to the output.
     * 
     * @param block
     *            output block to write
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    protected void writeBlock(Object block) 
        throws ActivityProcessingException, ActivityTerminatedException
    {
        try
        {
            mOutput.write(block);
        } 
        catch (PipeClosedException e)
        {
            // complete
            iterativeStageComplete();
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }

    }

    protected Join loadJoin(String impl) throws ActivityProcessingException
    {
        try
        {
            Class<? extends Join> joinClass = Class.forName(impl).asSubclass(Join.class);
            return (Join)joinClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (InstantiationException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ActivityProcessingException(e);
        }

    }
    
    public void configureActivity(KeyValueProperties properties)
    {
        mJoinName = (String)properties.get(JOIN_IMPLEMENTATION);
    }
}
