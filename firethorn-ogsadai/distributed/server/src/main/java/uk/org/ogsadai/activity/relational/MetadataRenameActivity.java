// Copyright (c) The University of Edinburgh 2009.
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

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
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
 * <li> <code>data</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a
 * mandatory input. 
 * </li>
 * <li> <code>resultColumnNames</code>. Type: A list of {@link
 * java.lang.String}. This input specifies the new column names. This
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
 * object. This is a mandatory input. 
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
public class MetadataRenameActivity extends IterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Activity input name - data. */
    public static final String INPUT_DATA = "data";
    
    /** Activity input name - new column names. */
    public static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";
    
    /** Activity output name - result. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Data input. */
    public TupleListActivityInput mDataInput;
    
    /** Result column names input. */
    public TypedListActivityInput mResultColumnNamesInput;
    
    /** Activity output. */
    public BlockWriter mOutput;
    
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateInput(INPUT_RESULT_COLUMN_NAMES);
        validateInput(INPUT_DATA);
        validateOutput(OUTPUT_RESULT);
        mDataInput = new TupleListActivityInput(INPUT_DATA);
        mDataInput.setBlockReader(getInput(INPUT_DATA));
        mResultColumnNamesInput = 
            new TypedListActivityInput(INPUT_RESULT_COLUMN_NAMES, String.class);
        mResultColumnNamesInput.setBlockReader(getInput(INPUT_RESULT_COLUMN_NAMES));
        
        mOutput = getOutput();
    }

    protected void processIteration()
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        // Get the list of result column names
        Object outputColumnNamesListIterator = mResultColumnNamesInput.read();
        if (outputColumnNamesListIterator == ControlBlock.NO_MORE_DATA)
        {
            validateInputsAreMatched(
                ControlBlock.NO_MORE_DATA, mDataInput.read());
            iterativeStageComplete();
            return;
        }
        List<String> outputNames = getOutputColumnNames(
            (ListIterator) outputColumnNamesListIterator);

        // Get the tuple list iterator
        Object tuplesListIterator = mDataInput.read();
        if (tuplesListIterator == ControlBlock.NO_MORE_DATA)
        {
            validateInputsAreMatched(
                outputColumnNamesListIterator, ControlBlock.NO_MORE_DATA);
            iterativeStageComplete();
            return;
        }
        
        TupleListIterator tuples = (TupleListIterator)tuplesListIterator;
        TupleMetadata metadata = 
            (TupleMetadata)tuples.getMetadataWrapper().getMetadata();

        // Throw error if the wrong side, code is below
        int columnCount = metadata.getColumnCount(); 
        if (columnCount != outputNames.size())
        {
            throw new ActivityUserException(
                new Exception("Input column count does not match the output column count."));
        }

        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (int i=0; i<columnCount; i++)
        {
            String[] names = outputNames.get(i).split("\\.");
            ColumnMetadata old = metadata.getColumnMetadata(i);
            String source = null;
            String name;
            if (names.length == 1)
            {
                name = names[0];
            }
            else
            {
                source = names[0];
                name = names[1];
            }
            ColumnMetadata column = new SimpleColumnMetadata(
                    name,
                    source,
                    old.getResourceID(),
                    old.getDRES(),
                    old.getType(),
                    old.getPrecision(),
                    old.isNullable(),
                    old.getColumnDisplaySize()
            );
            columns.add(column);
        }
        TupleMetadata outMetadata = new SimpleTupleMetadata(columns);
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(new MetadataWrapper(outMetadata));
        Object block;
        while ((block = tuples.nextValue()) != null)
        {
            writeBlock(block);
        }
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

    /**
     * Validates that the inputs are matched, i.e. both inputs have the same
     * number of logical blocks.  This method is called when one of the inputs
     * the no more data control block. An exception is throw in the other
     * input block is not also this control block.
     * 
     * @param resultColumnNamesBlock 
     *    block read from the resultColumnNames input 
     * @param dataBlock
     *    block read from the data input
     *    
     * @throws UnmatchedActivityInputsException
     *    if the two inputs are not both the no more data control block.
     */
    private void validateInputsAreMatched(
        Object resultColumnNamesBlock,
        Object dataBlock) 
        throws UnmatchedActivityInputsException
    {
        if (resultColumnNamesBlock != ControlBlock.NO_MORE_DATA ||
            dataBlock != ControlBlock.NO_MORE_DATA)
        {
            throw new UnmatchedActivityInputsException(
                new String[]{"resultColumnNames", "data"},
                new Object[]{resultColumnNamesBlock, dataBlock});
        }
    }
}
