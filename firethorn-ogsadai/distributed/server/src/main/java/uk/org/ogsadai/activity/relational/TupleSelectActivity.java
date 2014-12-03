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


package uk.org.ogsadai.activity.relational;

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
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * An activity selects tuples from the input list according to a condition 
 * (similar to a WHERE clause in SQL).
 * <p> 
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li><code>condition</code>. Type: {@link java.lang.String}. The condition to evaluate
 * for each input tuple.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The tuples from the input data that meet the condition.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A value is read from the condition input before the input data is
 * processed.</li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Evaluates the condition on a tuple and writes the tuple to the output if
 * the condition evaluates to <code>TRUE</code>.
 * </li>
 * <li>
 * The format of the condition is like an SQL WHERE or HAVING clause.
 * It can contain column names, arithmetic and boolean operators and constants. 
 * </li>
 * <li>
 * Columns can be identified by providing the full name (using the table name
 * prefix and column name TABLE.COLUMN) or by the simple name (using COLUMN name 
 * only without the table name prefix).
 * If a simple column name is ambiguous the full name must be used, otherwise
 * an exception is raised.
 * </li>
 * <li>
 * Like in SQL, comparison to a NULL value always evaluates to FALSE.
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   condition: a < b
 *   data: { metadata(a, b) (1, 2) (2, 1) (NULL, 1) }
 *   result: { metadata(a, b) (1, 2) }
 * </pre>
 * <pre>
 *   condition: (a + b)/3 = c
 *   data: { metadata(a, b, c) (1, 2, 3) (0, 3, 1) (0, 0, 0) }
 *   result: { metadata(a, b, c) (0, 3, 1) (0, 0, 0) }
 * </pre>
 * <pre>
 *   condition: A.id = B.id
 *   data: { metadata(A.id, B.id) (5, 5) (1, 2) (2, 3) (4, 4) }
 *   result: { metadata(A.id, B.id) (5, 5) (4, 4)  }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleSelectActivity extends MatchedIterativeActivity
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";
    
    private static final DAILogger LOG = 
        DAILogger.getLogger(TupleSelectActivity.class);
    
    /** Activity input name - condition. */
    public static final String INPUT_CONDITION = "condition";

    /** Activity input name - data. */
    public static final String INPUT_DATA = "data";
    
    /** Activity output name - result. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Data output */
    private BlockWriter mOutput;
    
    /** Function repository, or null if functions are not supported. */
    private FunctionRepository mFunctionRepository;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA),
            new TypedActivityInput(INPUT_CONDITION, String.class)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
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

    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        String condition = (String)iterationData[1];
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Condition: " + condition);
        }
        final TupleListIterator tuples = (TupleListIterator) iterationData[0];
        try
        {
            final CommonTree ast = 
                SQLQueryParser.getInstance().parseSQLForCondition(condition);
            Expression expression = ExpressionFactory.buildExpression(
                ast, mFunctionRepository);
            mOutput.write(ControlBlock.LIST_BEGIN);
            MetadataWrapper wrapper = tuples.getMetadataWrapper();
            expression.configure((TupleMetadata)wrapper.getMetadata());
            mOutput.write(wrapper);
            Tuple tuple;
            while ((tuple = (Tuple)tuples.nextValue()) != null)
            {
                Boolean evaluation = expression.evaluate(tuple);
                if (evaluation!= null && evaluation)
                {
                    mOutput.write(tuple);
                }
            }
            mOutput.write(ControlBlock.LIST_END);
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
        catch (ColumnNotFoundException e)
        {
            throw new ActivityUserException(new ExpressionException(e));
        } 
        catch (TypeMismatchException e)
        {
            throw new ActivityUserException(new ExpressionException(e));
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
        catch (SQLParserException e)
        {
            throw new ActivityUserException(e);
        }
        catch(ExpressionEvaluationException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post processing
    }
    
    

}
