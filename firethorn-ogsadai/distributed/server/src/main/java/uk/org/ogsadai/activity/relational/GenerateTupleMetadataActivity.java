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

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class GenerateTupleMetadataActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** Name of names input. */
    public static final String INPUT_NAMES = "names";
    /** Name of types . */
    public static final String INPUT_TYPES = "types";
    /** Name of table names input. */
    public static final String INPUT_TABLE_NAMES = "tableNames";
    /** Name of result output. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Result output block writer. */
    private BlockWriter mResultOutput;
    
    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedListActivityInput(INPUT_NAMES, String.class),
            new TypedListActivityInput(INPUT_TYPES, Number.class),
            new TypedListActivityInput(INPUT_TABLE_NAMES, String.class)};
    }

    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }

    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mResultOutput = getOutput(OUTPUT_RESULT);
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        ListIterator namesInput = (ListIterator) iterationData[0];
        ListIterator typesInput = (ListIterator) iterationData[1];
        ListIterator tableNamesInput = (ListIterator) iterationData[2];
        
        // Assume all the arrays are the same size
        // TODO: need error handling if this assumption is not met
        
        List<ColumnMetadata> columns = new LinkedList<ColumnMetadata>();
        
        String name;
        int type;
        String tableName;
        
        while( (name = (String) namesInput.nextValue()) != null)
        {
            type = ((Number) typesInput.nextValue()).intValue();
            tableName = (String) tableNamesInput.nextValue();
            
            ColumnMetadata column = new SimpleColumnMetadata(
                name,
                tableName,
                null,
                null,
                type,
                1,  // precision
                ColumnMetadata.COLUMN_NULLABLE_UNKNOWN,
                10);
            
            columns.add(column);
        }
        // Need to read the final blocks from the lists
        typesInput.nextValue();
        tableNamesInput.nextValue();
        
        TupleMetadata tupleMetadata = new SimpleTupleMetadata(columns);
        
        try
        {
            mResultOutput.write(new MetadataWrapper(tupleMetadata));
        }
        catch (PipeClosedException e)
        {
           // Do nothing
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
