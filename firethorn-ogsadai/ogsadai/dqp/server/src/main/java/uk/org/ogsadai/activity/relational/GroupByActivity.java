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
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ColumnNameAmbiguousException;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
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
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;
import uk.org.ogsadai.tuple.sort.GroupTuple;
import uk.org.ogsadai.tuple.sort.TupleListGroup;

/**
 * Groups a list of tuples according to some group by columns and aggregate
 * functions.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>columnIds</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * The IDs of the group-by columns but the list may be empty.</li>
 * <li> <code>aggregates</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * The aggregate functions to be applied to each group. This is a mandatory
 * input but the list may be empty.</li>
 * <li> <code>resultColumnNames</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * Column names of the result columns. This is a mandatory input.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The product of the two input data streams.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A list is read from each of the <code>columnIds</code>, 
 * <code>aggregates</code>, and the <code>resultColumnNames</code> inputs 
 * before the input data is processed.
 * </li>
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
 * Input tuples are grouped by providing column names. For each group, the 
 * provided aggregate functions are applied to all tuples in the group. The
 * output tuple contains the grouping columns and the values of the aggregate
 * functions.
 * </li>
 * <li>
 * This activity behaves like a GROUP BY clause in an SQL statement.
 * </li>
 * <li>
 * The supported functions are loaded from the function repository which must be 
 * present in the OGSA-DAI context. 
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   columnIds: { "city" }
 *   aggregates: { }
 *   resultColumnNames: { "c" }
 *   data: { metadata("name", "city") ("Amy", "Edinburgh") ("Bartek", "Edinburgh") ("Charles", "London") }
 *   result: { metadata("c") ("Edinburgh") ("London") }
 * </pre>
 * <pre>
 *   columnIds: { "category" }
 *   aggregates: { "AVG(age)" }
 *   resultColumnNames: { "category", "average age" }
 *   data: { metadata("age", "category") (49, "A") (35, "B") (23, "A") (42, "A") (24, "B") (53, "C") }
 *   result: { metadata("category", "average age") ("A", 38) ("B", 29.5) ("C", 53) }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class GroupByActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Data input name */
    public static final String INPUT_DATA = "data";
    /** Grouping column IDs input name */
    public static final String INPUT_GROUPBY_COLUMNS = "columnIds";
    /** Aggregate functions input name. */
    public static final String INPUT_AGGREGATES = "aggregates";
    /** Column names of the result input name. */
    public static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";
    /** Data output name */
    public static final String OUTPUT_RESULT = "result";

    /** Function repository, or null if functions are not supported. */
    private FunctionRepository mFunctionRepository;

    /** Output block writer */
    private BlockWriter mOutput;

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TupleListActivityInput(INPUT_DATA),
            new TypedListActivityInput(INPUT_GROUPBY_COLUMNS, String.class),
            new TypedListActivityInput(INPUT_AGGREGATES, String.class),
            new TypedListActivityInput(INPUT_RESULT_COLUMN_NAMES, String.class)
        };
    }

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

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        TupleListIterator tuples = (TupleListIterator)iterationData[0];
        int[] columns = getGroupByColumns(
                (ListIterator)iterationData[1],
                (TupleMetadata)tuples.getMetadataWrapper().getMetadata());
        List<String> columnNames = getColumnNames((ListIterator)iterationData[3]);
        ExecutableFunctionExpression[] aggregates = 
            getAggregateFunctions((ListIterator)iterationData[2]);

        if (columns.length + aggregates.length != columnNames.size())
        {
            throw new ActivityUserException(new UnmatchedColumnNamesException());
        }
        
        TupleListGroup group = new TupleListGroup(columns, aggregates);
        MetadataWrapper wrapper = tuples.getMetadataWrapper();
        try
        {
            group.write(wrapper);
        } 
        catch (TypeMismatchException e)
        {
            throw new ActivityUserException(e);
        }
        Tuple tuple;
        try
        {
            while ((tuple = (Tuple)tuples.nextValue()) != null)
            {
                group.write(tuple);
            }
            group.close();
        }
        catch (UnsupportedTupleTypeException e)
        {
            throw new ActivityUserException(e);
        }
        MetadataWrapper metadata = 
            constructOutputMetadata(wrapper, columnNames, columns, aggregates);
        writeGroups(metadata, group);
        
    }

    @Override
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // nothing
    }
    
    private int[] getGroupByColumns(ListIterator columnList, TupleMetadata metadata) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<Integer> groupBy = new ArrayList<Integer>();
        String value;
        while ((value = (String)columnList.nextValue()) != null)
        {
            TableColumn column = new TableColumn(value);
            try
            {
                groupBy.add(column.getColumnIndex(metadata));
            }
            catch (ColumnNotFoundException e)
            {
                throw new ActivityUserException(e);
            }
            catch (ColumnNameAmbiguousException e)
            {
                throw new ActivityUserException(e);
            }
        }
        
        int[] result = new int[groupBy.size()];
        int i=0;
        for (Integer col : groupBy)
        {
            result[i++] = col;
        }
        return result;
    }
    
    private List<String> getColumnNames(ListIterator input) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<String> columnNames = new ArrayList<String>();
        String name;
        while ((name = (String) input.nextValue()) != null)
        {
            columnNames.add(name);
        }
        return columnNames;
    }

    private ExecutableFunctionExpression[] getAggregateFunctions(ListIterator aggregates) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        String expression;
        List<ExecutableFunctionExpression> functions = 
            new ArrayList<ExecutableFunctionExpression>();
        try
        {
            while ((expression = (String)aggregates.nextValue()) != null)
            {
                CommonTree tree = SQLQueryParser.getInstance()
                    .parseSQLForDerivedColumn(expression);
                ArithmeticExpression arithExp = 
                    ArithmeticExpressionFactory.buildArithmeticExpression(
                        (CommonTree)tree.getChild(0),
                        mFunctionRepository);
                if (!(arithExp instanceof ExecutableFunctionExpression))
                {
                    throw new ActivityUserException(
                        new NoFunctionException(arithExp));
                }
                functions.add((ExecutableFunctionExpression)arithExp);
            }
        }
        catch (SQLParserException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
        
        return functions.toArray(new ExecutableFunctionExpression[functions.size()]);

    }
    
    private void writeGroups(MetadataWrapper metadata, TupleListGroup group) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // now get groups out
        try
        {
            TupleListIterator groups = group.getTupleListIterator();
            GroupTuple groupTuple;
            mOutput.write(ControlBlock.LIST_BEGIN);
            mOutput.write(metadata);
            while ((groupTuple = (GroupTuple)groups.nextValue()) != null)
            {
                Tuple tuple = mapGroupToTuple(groupTuple);
                mOutput.write(tuple);
            }
            mOutput.write(ControlBlock.LIST_END);
        } 
        catch (UnsupportedTupleTypeException e)
        {
            throw new ActivityUserException(e);
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
    
    private Tuple mapGroupToTuple(GroupTuple group)
    {
        Object[] groupValues = group.getGroupColumns();
        ExecutableFunctionExpression[] groupAggregates = group.getAggregates();
        
        List<Object> elements = new ArrayList<Object>();
        for (Object value : groupValues)
        {
            elements.add(value);
        }
        for (ExecutableFunctionExpression function : groupAggregates)
        {
            elements.add(function.getResult());
        }
        Tuple result = new SimpleTuple(elements);
        return result;
    }
    
    private MetadataWrapper constructOutputMetadata(
            MetadataWrapper input, 
            List<String> columnNames, 
            int[] columns,
            ExecutableFunctionExpression[] aggregates)
    {
        TupleMetadata metadata = (TupleMetadata)input.getMetadata();
        List<ColumnMetadata> columnMetadata = new ArrayList<ColumnMetadata>();
        int columnIndex = 0;
        for (int column : columns)
        {
            ColumnMetadata in = metadata.getColumnMetadata(column);
            String name = columnNames.get(columnIndex++);
            String table = null;
            int dotIndex = name.indexOf(".");
            if (dotIndex >= 0)
            {
                table = name.substring(0, dotIndex);
                name = name.substring(dotIndex + 1);
            }
            // nullify the resourceID and DRES
            ColumnMetadata out = 
                new SimpleColumnMetadata(
                        name,
                        table,
                        null,
                        null,
                        in.getType(), 
                        in.getPrecision(), 
                        in.isNullable(), 
                        in.getColumnDisplaySize()); 
            columnMetadata.add(out);
        }
        for (ExecutableFunctionExpression function : aggregates)
        {
            ColumnMetadata in = function.getMetadata();
            // nullify the table name, resourceID and DRES 
            ColumnMetadata out = 
                new SimpleColumnMetadata(
                        columnNames.get(columnIndex++), 
                        in.getType(), 
                        in.getPrecision(), 
                        in.isNullable(), 
                        in.getColumnDisplaySize()); 
            columnMetadata.add(out);
        }
        TupleMetadata out = new SimpleTupleMetadata(columnMetadata);
        MetadataWrapper result = new MetadataWrapper(out);
        return result;
    }
    
}
