package uk.org.ogsadai.activity.astro;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

public class TupleSweepProductActivityTest extends TestCase
{

    public void testColumnIndex() throws Exception 
    {
        Object[] data1 = new Object[1003];
        data1[0] = ControlBlock.LIST_BEGIN;
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data1.length-1; i++)
        {
            data1[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        Object[] data2 = new Object[103];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data2.length-1; i++)
        {
            data2[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        boolean hasData = false;
        while (iterator.hasNext())
        {
            Object block = iterator.next();
            if (block == ControlBlock.LIST_END)
            {
                assertFalse(iterator.hasNext());
            }
            else
            {
                assertTrue(block instanceof Tuple);
                Tuple tuple = (Tuple)block;
                double d1 = tuple.getDouble(0);
                double d2 = tuple.getDouble(1);
                assertTrue(d2 < d1 + windowSize && d2 >= d1 - windowSize);
                hasData = true;
            }
        }
        assertTrue(hasData);
    }
    
    public void testOffsetSize() throws Exception 
    {
        Object[] data1 = new Object[1003];
        data1[0] = ControlBlock.LIST_BEGIN;
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data1.length-1; i++)
        {
            data1[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        Object[] data2 = new Object[103];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data2.length-1; i++)
        {
            data2[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        double offset = 5;
        double size = 20;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputOffset = new MockInputPipe(offset);
        MockInputPipe inputSize = new MockInputPipe(size);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_OFFSET, inputOffset);
        activity.addInput(TupleSweepProductActivity.INPUT_SIZE, inputSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        boolean hasData = false;
        while (iterator.hasNext())
        {
            Object block = iterator.next();
            if (block == ControlBlock.LIST_END)
            {
                assertFalse(iterator.hasNext());
            }
            else
            {
                assertTrue(block instanceof Tuple);
                Tuple tuple = (Tuple)block;
                double d1 = tuple.getDouble(0);
                double d2 = tuple.getDouble(1);
                assertTrue(d2 < d1 - offset + size && d2 >= d1 - offset);
                hasData = true;
            }
        }
        assertTrue(hasData);
    }
    
    public void testColumnName() throws Exception 
    {
        Object[] data1 = new Object[103];
        data1[0] = ControlBlock.LIST_BEGIN;
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data1.length-1; i++)
        {
            data1[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        Object[] data2 = new Object[103];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data2.length-1; i++)
        {
            data2[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe("A");
        MockInputPipe inputColumn2 = new MockInputPipe("A");
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        boolean hasData = false;
        while (iterator.hasNext())
        {
            Object block = iterator.next();
            if (block == ControlBlock.LIST_END)
            {
                assertFalse(iterator.hasNext());
            }
            else
            {
                assertTrue(block instanceof Tuple);
                Tuple tuple = (Tuple)block;
                double d1 = tuple.getDouble(0);
                double d2 = tuple.getDouble(1);
                assertTrue(d2 < d1 + windowSize && d2 >= d1 - windowSize);
                hasData = true;
            }
        }
        assertTrue(hasData);
    }

    public void testNulls() throws Exception 
    {
        Object[] data1 = new Object[5];
        data1[0] = ControlBlock.LIST_BEGIN;
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        data1[1] = new MetadataWrapper(metadata);
        data1[2] = new SimpleTuple(Arrays.asList(Null.VALUE));
        data1[3] = new SimpleTuple(Arrays.asList(1.0));
        data1[4] = ControlBlock.LIST_END;
        Object[] data2 = new Object[5];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[2] = new SimpleTuple(Arrays.asList(1.1));
        data2[3] = new SimpleTuple(Arrays.asList(Null.VALUE));
        data2[4] = ControlBlock.LIST_END;
        double windowSize = 1;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        assertTrue(iterator.hasNext());
        Object object = iterator.next();
        assertTrue(object instanceof Tuple);
        Tuple tuple = (Tuple)object;
        assertEquals(1.0, tuple.getDouble(0));
        assertEquals(1.1, tuple.getDouble(1));
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_END, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    public void testEmptyData1() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        Object[] data1 = new Object[3];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;
        Object[] data2 = new Object[103];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data2.length-1; i++)
        {
            data2[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_END, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    public void testEmptyData2() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        Object[] data1 = new Object[103];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        for (int i=2; i<data1.length-1; i++)
        {
            data1[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        data1[data1.length-1] = ControlBlock.LIST_END;
        Object[] data2 = new Object[3];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_END, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    public void testUnknownColumnName() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));

        Object[] data1 = new Object[3];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;

        Object[] data2 = new Object[3];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;

        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe("B");
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);

        try
        {
            activity.process();
            fail("Expected ColumnNotFoundException");
        }
        catch (ActivityUserException e)
        {
            // expected
            assertTrue(e.getCause() instanceof ColumnNotFoundException);
        }
        
    }

    public void testColumnIndexOutOfRange() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));

        Object[] data1 = new Object[3];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;

        Object[] data2 = new Object[3];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;

        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(3);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);

        try
        {
            activity.process();
            fail("Expected ColumnNotFoundException");
        }
        catch (ActivityUserException e)
        {
            // expected
            assertTrue(e.getCause() instanceof ColumnNotFoundException);
        }
        
    }

    public void testSizeNoOffset() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));

        Object[] data1 = new Object[3];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;

        Object[] data2 = new Object[3];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;

        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(3);
        MockInputPipe inputSize = new MockInputPipe(5);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_SIZE, inputSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);

        try
        {
            activity.process();
            fail("Expected InvalidActivityInputsException");
        }
        catch (InvalidActivityInputsException e)
        {
            // expected
        }
    }
    
    public void testOffsetNoSize() throws Exception 
    {
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));

        Object[] data1 = new Object[3];
        data1[0] = ControlBlock.LIST_BEGIN;
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;

        Object[] data2 = new Object[3];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;

        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(3);
        MockInputPipe inputOffset = new MockInputPipe(5);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_OFFSET, inputOffset);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);

        try
        {
            activity.process();
            fail("Expected InvalidActivityInputsException");
        }
        catch (InvalidActivityInputsException e)
        {
            // expected
        }
        
    }

    public void testEmptyProduct() throws Exception 
    {
        Object[] data1 = new Object[103];
        data1[0] = ControlBlock.LIST_BEGIN;
        ColumnMetadata column = new SimpleColumnMetadata("A", TupleTypes._DOUBLE, 0, 0, 0);
        TupleMetadata metadata = new SimpleTupleMetadata(Arrays.asList(column));
        data1[1] = new MetadataWrapper(metadata);
        data1[data1.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data1.length-1; i++)
        {
            data1[i] = new SimpleTuple(Arrays.asList(i+1000-2));
        }
        Object[] data2 = new Object[103];
        data2[0] = ControlBlock.LIST_BEGIN;
        data2[1] = new MetadataWrapper(metadata);
        data2[data2.length-1] = ControlBlock.LIST_END;
        for (int i=2; i<data2.length-1; i++)
        {
            data2[i] = new SimpleTuple(Arrays.asList(i-2));
        }
        double windowSize = 5;
        MockInputPipe inputData1 = new MockInputPipe(data1);
        MockInputPipe inputData2 = new MockInputPipe(data2);
        MockInputPipe inputColumn1 = new MockInputPipe(0);
        MockInputPipe inputColumn2 = new MockInputPipe(0);
        MockInputPipe inputWindowSize = new MockInputPipe(windowSize);
        
        MockOutputPipe outputResult = new MockOutputPipe(new Object[]{});
        TupleSweepProductActivity activity = new TupleSweepProductActivity();
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_1, inputData1);
        activity.addInput(TupleSweepProductActivity.INPUT_DATA_2, inputData2);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_1, inputColumn1);
        activity.addInput(TupleSweepProductActivity.INPUT_COLUMN_2, inputColumn2);
        activity.addInput(TupleSweepProductActivity.INPUT_RADIUS, inputWindowSize);
        activity.addOutput(TupleSweepProductActivity.OUTPUT_RESULT, outputResult);
        
        activity.process();
        
        List<?> actualBlocks = outputResult.getActualBlocks();
        Iterator<?> iterator = actualBlocks.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof MetadataWrapper);
        assertTrue(iterator.hasNext());
        assertEquals(ControlBlock.LIST_END, iterator.next());
        assertFalse(iterator.hasNext());
    }

}
