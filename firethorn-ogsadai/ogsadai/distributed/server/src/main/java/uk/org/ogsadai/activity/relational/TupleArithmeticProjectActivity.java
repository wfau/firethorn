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

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * This activity projects columns according to a given set of arithmetic
 * expressions.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data to which the projections are applied.</li>
 * <li> <code>expressions</code> - Type: OGSA-DAI list of {@link java.lang.String}
 * . This input specifies the projections as arithmetic expressions.</li>
 * <li> <code>resultColumnNames</code> - Type: OGSA-DAI list of {@link java.lang.String}
 * . This input specifies the names of the output columns.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
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
 * <li>The expressions and output columns names are read before reading a list
 * of tuples.</li>
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
 * <li>The server-side activity projects input columns to output columns 
 * according to a list of arithmetic expressions.</li>
 * <li>The number of input expressions must match the number of output column
 * names. Output column <code>n</code> is assigned the name
 * <code>resultColumnNames(n)</code> and its values are the results of applying
 * <code>expressions(n)</code>.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleArithmeticProjectActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";

    /** Activity input name - project expressions. */
    public static final String INPUT_EXPRESSION = "expressions";
    
    /** Activity input name - result column names. */
    public static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";

    /** Activity input name - data. */
    public static final String INPUT_DATA = "data";
    
    /** Activity output name - result . */
    public static final String OUTPUT_DATA = "result";

    /** Data output. */
    private BlockWriter mOutput;
    
    /** Function repository, or null if functions are not supported. */
    private FunctionRepository mFunctionRepository;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new TypedListActivityInput(INPUT_EXPRESSION, String.class),
            new TypedListActivityInput(INPUT_RESULT_COLUMN_NAMES, String.class),
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
        mOutput = getOutput();
        
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
        final ListIterator expressions = (ListIterator)iterationData[0];
        final ListIterator columnNames = (ListIterator)iterationData[1];
        
        // produce metadata for the output tuple list
        final TupleListIterator tuples = (TupleListIterator) iterationData[2];
        final TupleMetadata metadata = 
            (TupleMetadata) tuples.getMetadataWrapper().getMetadata();
        List<ArithmeticExpression> arithExpr = 
            new ArrayList<ArithmeticExpression>();
        TupleMetadata outputMetadata = 
            buildExpressions(expressions, columnNames, metadata, arithExpr);
        MetadataWrapper outputWrapper = new MetadataWrapper(outputMetadata);
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(outputWrapper);
        project(tuples, arithExpr);
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
     * Builds the output metadata.
     * 
     * @param expressions
     *            arithmetic expressions
     * @param columnNames
     *            output column names
     * @param metadata
     *            input metadata
     * @param arithmeticExpressions
     *            parsed expressions
     * @return metadata tuple metadata
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private TupleMetadata buildExpressions(
            final ListIterator expressions, 
            final ListIterator columnNames, 
            final TupleMetadata metadata,
            List<ArithmeticExpression> arithmeticExpressions)
        throws ActivityUserException, 
               ActivityProcessingException,
               ActivityTerminatedException
    {
        List<String> outputNames = getList(columnNames);
        List<String> projectExp = getList(expressions);
        validateInputLists(outputNames, projectExp);
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (int i=0; i<outputNames.size(); i++)
        {
            ArithmeticExpression expression;
            try
            {
                CommonTree tree = SQLQueryParser.getInstance()
                    .parseSQLForDerivedColumn(projectExp.get(i));
                expression = 
                    ArithmeticExpressionFactory.buildArithmeticExpression(
                        tree, mFunctionRepository);
                expression.configure(metadata);
                ColumnMetadata columnMetadata = 
                    getColumnMetadata(expression, outputNames.get(i));
                columns.add(columnMetadata);
                arithmeticExpressions.add(expression);
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
        TupleMetadata outputMetadata = new SimpleTupleMetadata(columns);
        return outputMetadata;
    }
    
    /**
     * Iterates over an activity input stream of string values and returns the
     * values in a list.
     * 
     * @param iterator
     *            input value iterator
     * @return a list of input values
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private List<String> getList(ListIterator iterator)
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<String> result = new ArrayList<String>();
        String columnName;
        while ((columnName = (String) iterator.nextValue()) != null)
        {
            result.add(columnName);
        }
        return result;
    }

    /**
     * Compares the lengths of the two inputs lists and raises an exception if
     * they are not the same.
     * 
     * @param outputNames
     *            output names
     * @param projectExp
     *            projection expressions
     * @throws UnmatchedActivityInputsException
     *             if the list lengths do not match
     */
    private void validateInputLists(
            List<String> outputNames,
            List<String> projectExp)
        throws UnmatchedActivityInputsException
    {
        int outputColumns = outputNames.size();
        if (outputColumns > projectExp.size())
        {
            throw new UnmatchedActivityInputsException(
                new String[] {INPUT_RESULT_COLUMN_NAMES, INPUT_EXPRESSION},
                new Object[] { 
                    outputNames.get(outputColumns-1), 
                    ControlBlock.NO_MORE_DATA } );
        }
        else if (outputColumns < projectExp.size())
        {
            throw new UnmatchedActivityInputsException(
                new String[] {INPUT_RESULT_COLUMN_NAMES, INPUT_EXPRESSION},
                new Object[] {
                    ControlBlock.NO_MORE_DATA, 
                    projectExp.get(projectExp.size()-1) } );
        }

    }
    
    /**
     * Constructs column metadata for the values of an arithmetic expression.
     * 
     * @param expression
     *            the expression which has been configured with input metadata
     * @param columnName
     *            the name of the output column
     * @return column metadata
     */
    private ColumnMetadata getColumnMetadata(
            ArithmeticExpression expression, String columnName)
    {
        ColumnMetadata expressionMetadata = expression.getMetadata();
        // the column metadata of the arithmetic expression doesn't contain
        // a column name so we have to add it
        int dot = columnName.indexOf(".");
        ColumnMetadata columnMetadata;
        if (dot > -1)
        {
            columnMetadata = new SimpleColumnMetadata(
                // column name is the part after the dot
                columnName.substring(dot+1), 
                // table name is the part up to the dot
                columnName.substring(0, dot),
                null, null, 
                expressionMetadata.getType(),
                expressionMetadata.getPrecision(),
                expressionMetadata.isNullable(),
                expressionMetadata.getColumnDisplaySize());
        }
        else
        {
            columnMetadata = new SimpleColumnMetadata(
                columnName, 
                expressionMetadata.getType(),
                expressionMetadata.getPrecision(),
                expressionMetadata.isNullable(),
                expressionMetadata.getColumnDisplaySize());
        }
        return columnMetadata;
    }
    
    /**
     * Project tuples in a simple arithmetic projection without aggregation -
     * one tuple produced for each tuple consumed.
     * 
     * @param tuples
     *            input tuple list iterator
     * @param arithExpr
     *            an arithmetic expression for each output column
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    private void project(
            final TupleListIterator tuples, 
            final List<ArithmeticExpression> arithExpr) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        Tuple tuple;
        int columnCount = arithExpr.size();
        try
        {
            while ((tuple=(Tuple)tuples.nextValue()) != null)
            {
                List<Object> elements = new ArrayList<Object>(columnCount);
                for (ArithmeticExpression expression : arithExpr)
                {
                    expression.evaluate(tuple);
                    elements.add(expression.getResult());
                }
                Tuple outTuple = new SimpleTuple(elements);
                writeBlock(outTuple);
            }
        }
        catch(ExpressionEvaluationException e)
        {
            throw new ActivityProcessingException(e);
        }
    }
    
    /**
     * Writes a block of data to the output.
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
            mOutput.write(block);
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
