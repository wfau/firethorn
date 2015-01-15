// Copyright (c) The University of Edinburgh 2011.
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

package uk.org.ogsadai.activity.astro;

import java.util.ArrayList;
import java.util.List;

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
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Activity that modifies the tuple metadata and replaces the column
 * name in each column with the specified name.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>resultColumnNames</code>. Type: A list of {@link
 * java.lang.String}. This input specifies the new column names. This
 * is a mandatory input. 
 * </li>
 * <li> <code>resultColumnTypes</code>. Type: A list of {@link
 * java.lang.Number}. This input specifies the new column types as defined
 * by {@link uk.org.ogsadai.tuple.TupleTypes}. This
 * is a mandatory input. 
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list
 * an instance of {@link uk.org.ogsadai.metadata.MetadataWrapper}
 * containing a {@link uk.org.ogsadai.tuple.TupleMetadata}
 * object. This is an empty list with the specified metadata.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li> None. </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The <code>resultColumnNames</code> input is read first then the
 * <code>data</code> input.</li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li> None. </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * The activity modifies the tuple metadata of the input
 * tuple list. The column name in each column is replaced with
 * corresponding column name from the <code>resultColumnNames</code>
 * input. 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CreateEmptyTupleListActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";
    
    /** Activity input name - new column names. */
    public static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";
    
    /** Activity input name - new column names. */
    public static final String INPUT_RESULT_COLUMN_TYPES = "resultColumnTypes";
    
    /** Activity output name - result. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Data input. */
    public TupleListActivityInput mDataInput;
    
    /** Result column names input. */
    public TypedListActivityInput mResultColumnNamesInput;
    
    /** Activity output. */
    public BlockWriter mOutput;
    
    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TypedListActivityInput(INPUT_RESULT_COLUMN_NAMES, String.class),
                new TypedListActivityInput(INPUT_RESULT_COLUMN_TYPES, Number.class),
        };
    }

    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException,
               ActivityTerminatedException,
            ActivityUserException 
    {
        
        ListIterator names = (ListIterator)iterationData[0];
        ListIterator types = (ListIterator)iterationData[1];
        List<String> outputNames = getOutputColumnNames(names);
        List<Integer> outputTypes = getOutputColumnTypes(types);
        
        if (outputNames.size() != outputTypes.size())
        {
            throw new UnmatchedActivityInputsException(
                    new String[]{ INPUT_RESULT_COLUMN_NAMES, INPUT_RESULT_COLUMN_TYPES },
                    new Object[]{ outputNames, outputTypes });
        }
        
        int columnCount = outputNames.size();
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (int i=0; i<columnCount; i++)
        {
            String[] columnName = outputNames.get(i).split("\\.");
            String source = null;
            String name;
            if (columnName.length == 1)
            {
                name = columnName[0];
            }
            else
            {
                source = columnName[0];
                name = columnName[1];
            }
            ColumnMetadata column = new SimpleColumnMetadata(
                    name,
                    source,
                    null, 
                    null,
                    outputTypes.get(i),
                    0,
                    ColumnMetadata.COLUMN_NULLABLE_UNKNOWN,
                    0
            );
            columns.add(column);
        }
        TupleMetadata outMetadata = new SimpleTupleMetadata(columns);
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(new MetadataWrapper(outMetadata));
        writeBlock(ControlBlock.LIST_END);
    }
    
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }

    /**
     * Writes a block to the output.
     * 
     * @param block
     *            output block to write
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
    
    private List<String> getOutputColumnNames(ListIterator iterator) 
        throws ActivityUserException,
               ActivityProcessingException,
               ActivityTerminatedException
    {
        String column;
        List<String> outputNames = new ArrayList<String>();
        while ((column = (String)iterator.nextValue()) != null)
        {
            outputNames.add(column);
        }
        return outputNames;
    }

    private List<Integer> getOutputColumnTypes(ListIterator iterator) 
    throws ActivityUserException,
           ActivityProcessingException,
           ActivityTerminatedException
    {
        Number column;
        List<Integer> outputTypes = new ArrayList<Integer>();
        while ((column = (Number)iterator.nextValue()) != null)
        {
            outputTypes.add(column.intValue());
        }
        return outputTypes;
    }

}
