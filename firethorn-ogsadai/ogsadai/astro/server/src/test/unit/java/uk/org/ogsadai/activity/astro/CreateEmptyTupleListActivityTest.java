package uk.org.ogsadai.activity.astro;

import java.util.List;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

public class CreateEmptyTupleListActivityTest extends TestCase 
{
    
    public void testSimple() throws Exception 
    {
        
        MockInputPipe inputNames = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN,
                "a", "t.b", "c",
                ControlBlock.LIST_END,
        });
        MockInputPipe inputTypes = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN,
                TupleTypes._DOUBLE, TupleTypes._LONG, TupleTypes._STRING,
                ControlBlock.LIST_END,
        });
        
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        CreateEmptyTupleListActivity activity = new CreateEmptyTupleListActivity();
        
        activity.addInput(CreateEmptyTupleListActivity.INPUT_RESULT_COLUMN_NAMES, inputNames);
        activity.addInput(CreateEmptyTupleListActivity.INPUT_RESULT_COLUMN_TYPES, inputTypes);
        activity.addOutput(CreateEmptyTupleListActivity.OUTPUT_RESULT, output);
        
        activity.process();
        
        List<?> actual = output.getActualBlocks();
        assertEquals(3, actual.size());
        assertEquals(ControlBlock.LIST_BEGIN, actual.get(0));
        assertTrue(actual.get(1) instanceof MetadataWrapper);
        Object object = ((MetadataWrapper)actual.get(1)).getMetadata();
        assertTrue(object instanceof TupleMetadata);
        TupleMetadata metadata = (TupleMetadata)object;
        assertEquals(3, metadata.getColumnCount());
        ColumnMetadata col = metadata.getColumnMetadata(0);
        assertEquals("a", col.getName());
        assertNull(col.getTableName());
        assertEquals(TupleTypes._DOUBLE, col.getType());
        col = metadata.getColumnMetadata(1);
        assertEquals("b", col.getName());
        assertEquals("t", col.getTableName());
        assertEquals(TupleTypes._LONG, col.getType());
        col = metadata.getColumnMetadata(2);
        assertEquals("c", col.getName());
        assertNull(col.getTableName());
        assertEquals(TupleTypes._STRING, col.getType());
        assertEquals(ControlBlock.LIST_END, actual.get(2));
        
    }

    public void testUnmatchedInputs() throws Exception 
    {
        
        MockInputPipe inputNames = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN,
                "a", "b", "c",
                ControlBlock.LIST_END,
        });
        MockInputPipe inputTypes = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN,
                TupleTypes._DOUBLE, TupleTypes._LONG, 
                ControlBlock.LIST_END,
        });
        
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        
        CreateEmptyTupleListActivity activity = new CreateEmptyTupleListActivity();
        
        activity.addInput(CreateEmptyTupleListActivity.INPUT_RESULT_COLUMN_NAMES, inputNames);
        activity.addInput(CreateEmptyTupleListActivity.INPUT_RESULT_COLUMN_TYPES, inputTypes);
        activity.addOutput(CreateEmptyTupleListActivity.OUTPUT_RESULT, output);
        
        try
        {
            activity.process();
            fail("UnmatchedActivityInputsException expected.");
        }
        catch (UnmatchedActivityInputsException e)
        {
            // expected
        }
        
    }

}
