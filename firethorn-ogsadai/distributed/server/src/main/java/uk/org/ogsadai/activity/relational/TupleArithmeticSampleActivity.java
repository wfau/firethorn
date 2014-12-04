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

import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.StringActivityInput;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * This activity divides tuples between multiple outputs according to a
 * given expression.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * 
 * <li> <code>expression</code> - Type: {@link java.lang.String}. 
 * This input specifies the expression used to determine which output a tuple
 * should be written to.</li>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.  This output can have
 * multiple occurrences.  Tuples for which the expression evaluates to 0 will be
 * written to the first occurrence, those that evaluate to 1 to the second
 * occurrence and so on. Tuple for which the expression evaluates to a value
 * outside the range 0..<em>n</em>-1 (where <em>n</em> is the number of 
 * <code>result</code> outputs) will not be written to any output.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The expression input is read before reading a list of tuples.</li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * The activity writes each input tuple to zero or one of the outputs depending
 * of the result of evaluating the expression on that tuple. If the result is
 * 0 the tuple is written to the first output, if the result is 1 the tuple
 * is written to the second output etc.  If the result is outside the range
 * 0..<em>n</em>-1 (where <em>n</em> is the number of outputs) the tuple is not
 * written to any output.
 * </li>
 * <li>
 * The list markers and tuple metadata block are written to each of the outputs.
 * </li>
 * <li>
 * Example expressions include:
 * <ul>
 *  <li><code>"Floor(Rand(NULL) * 2)"</code> - to split the data randomly over 2
 *      outputs.  Or if there is only one output to randomly select half of the
 *      tuples.</li>
 *  <li><code>"Mod(attributeName, 4)"</code> - to split the the data over 4 
 *      outputs by applying the <code>Mod</code> function to the  specified 
 *      attribute.</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleArithmeticSampleActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010.";

    /** Activity input name - expression */
    public static final String INPUT_EXPRESSION = "expression";
    
    /** Activity input name - data. */
    public static final String INPUT_DATA = "data";
    
    /** Activity output name - result . */
    public static final String OUTPUT_DATA = "result";

    /** Data output. */
    private List mOutputs;
    
    /** Function repository, or null if functions are not supported. */
    private FunctionRepository mFunctionRepository;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new StringActivityInput(INPUT_EXPRESSION),
            new TupleListActivityInput(INPUT_DATA)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        mOutputs = getOutputs(OUTPUT_DATA);
        
        // Get the function repository if there is one.  Without a function
        // repository we can handle all derived attributes except those that
        // use functions.
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
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        final String expression = (String)iterationData[0];
        final TupleListIterator tuples = (TupleListIterator) iterationData[1];

        // Get the metadata
        final TupleMetadata metadata = 
            (TupleMetadata) tuples.getMetadataWrapper().getMetadata();
        
        // Produce the expression
        ArithmeticExpression expr = buildExpression(expression, metadata);
        
        MetadataWrapper outputWrapper = new MetadataWrapper(metadata);
        
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(outputWrapper);
        
        try
        {
            Tuple tuple;
            while ((tuple=(Tuple)tuples.nextValue()) != null)
            {
                int index = -1;
                
                expr.evaluate(tuple);
                Object result = expr.getResult();
                if (result instanceof Number)
                {
                    index = ((Number)result).intValue();
                }
                writeBlock(tuple, index);
            }
        }
        catch(ExpressionEvaluationException e)
        {
            throw new ActivityProcessingException(e);
        }

        writeBlock(ControlBlock.LIST_END);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }

    /**
     * Builds expression.
     * 
     * @param expressions 
     *            arithmetic expression string
     * @param metadata
     *            input metadata
     * @return arithmetic expression object used to evaluate expressions on
     *         tuples.
     *         
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private ArithmeticExpression buildExpression(
        String expr, TupleMetadata metadata)
            throws ActivityUserException, 
                   ActivityProcessingException,
                   ActivityTerminatedException
    {
        try
        {
            CommonTree tree = 
                SQLQueryParser.getInstance().parseSQLForDerivedColumn(expr);
            ArithmeticExpression expression = 
                ArithmeticExpressionFactory.buildArithmeticExpression(
                    tree, mFunctionRepository);
            expression.configure(metadata);
            
            return expression;
        } 
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ColumnNotFoundException e)
        {
            throw new ActivityUserException(new ExpressionException(e));
        } 
        catch (TypeMismatchException e)
        {
            throw new ActivityUserException(new ExpressionException(e));
        }
        catch (SQLParserException e)
        {
            throw new ActivityUserException(e);
        }
    }
    

    /**
     * Writes a block of data to all the outputs.
     * 
     * @param block
     *            data block
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private void writeBlock(Object block) 
        throws ActivityProcessingException, ActivityTerminatedException
    {
        try
        {
            Iterator it=mOutputs.iterator();
            while(it.hasNext())
            {
                ((BlockWriter) it.next()).write(block);
            }
        }
        catch (PipeClosedException e)
        {
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
    
    /**
     * Writes a block of data to the indexed output.
     * 
     * @param block
     *            data block
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private void writeBlock(Object block, int outputIndex) 
        throws ActivityProcessingException, ActivityTerminatedException
    {
        try
        {
            if (outputIndex >= 0 && outputIndex < mOutputs.size())
            {
                ((BlockWriter) mOutputs.get(outputIndex)).write(block);
            }
        }
        catch (PipeClosedException e)
        {
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
}
