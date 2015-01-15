package uk.org.ogsadai.activity.astro;

import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import junit.framework.TestCase;

public class IfEmptyListActivityTest extends TestCase 
{

    public void testSimple() throws Exception 
    {
        
        MockInputPipe inputContents = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END     
        });
        MockInputPipe inputLists = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 3, 4, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 3, 4, 5, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, ControlBlock.LIST_END,
        });
        
        MockOutputPipe outputEmpty = new MockOutputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END,     
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END,     
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END,     
        });
        
        MockOutputPipe outputNonEmpty = new MockOutputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 3, 4, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 3, 4, 5, ControlBlock.LIST_END,
        });

        IfEmptyListActivity activity = new IfEmptyListActivity();
        activity.addInput(IfEmptyListActivity.INPUT_DATA, inputLists);
        activity.addInput(IfEmptyListActivity.INPUT_CONTENT, inputContents);
        activity.addOutput(IfEmptyListActivity.OUTPUT_EMPTY, outputEmpty);
        activity.addOutput(IfEmptyListActivity.OUTPUT_NON_EMPTY, outputNonEmpty);
        
        activity.process();
        outputEmpty.verify();
        outputNonEmpty.verify();
    }
    
    public void testOneEmptyList() throws Exception 
    {
        MockInputPipe inputContents = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END     
        });
        MockInputPipe inputLists = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, ControlBlock.LIST_END,
        });
        
        MockOutputPipe output = new MockOutputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END,     
        });
        MockOutputPipe outputNonEmpty = new MockOutputPipe(new Object[] {
        });
        
        IfEmptyListActivity activity = new IfEmptyListActivity();
        activity.addInput(IfEmptyListActivity.INPUT_DATA, inputLists);
        activity.addInput(IfEmptyListActivity.INPUT_CONTENT, inputContents);
        activity.addOutput(IfEmptyListActivity.OUTPUT_EMPTY, output);
        activity.addOutput(IfEmptyListActivity.OUTPUT_NON_EMPTY, outputNonEmpty);
        
        activity.process();
        output.verify();
    }

    public void testNoEmptyList() throws Exception 
    {
        MockInputPipe inputContents = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END     
        });
        MockInputPipe inputLists = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
        });
        
        MockOutputPipe output = new MockOutputPipe(new Object[] {
        });
        
        MockOutputPipe outputNonEmpty = new MockOutputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
                ControlBlock.LIST_BEGIN, 6, 7, 8, ControlBlock.LIST_END,
        });
        
        IfEmptyListActivity activity = new IfEmptyListActivity();
        activity.addInput(IfEmptyListActivity.INPUT_DATA, inputLists);
        activity.addInput(IfEmptyListActivity.INPUT_CONTENT, inputContents);
        activity.addOutput(IfEmptyListActivity.OUTPUT_EMPTY, output);
        activity.addOutput(IfEmptyListActivity.OUTPUT_NON_EMPTY, outputNonEmpty);
        
        activity.process();
        output.verify();
        outputNonEmpty.verify();
    }

    public void testNoList() throws Exception 
    {
        MockInputPipe inputContents = new MockInputPipe(new Object[] {
                ControlBlock.LIST_BEGIN, 1, 2, 3, ControlBlock.LIST_END     
        });
        MockInputPipe inputLists = new MockInputPipe(new Object[] {
        });
        
        MockOutputPipe output = new MockOutputPipe(new Object[] {
        });
        MockOutputPipe outputNonEmpty = new MockOutputPipe(new Object[] {
        });
        
        IfEmptyListActivity activity = new IfEmptyListActivity();
        activity.addInput(IfEmptyListActivity.INPUT_DATA, inputLists);
        activity.addInput(IfEmptyListActivity.INPUT_CONTENT, inputContents);
        activity.addOutput(IfEmptyListActivity.OUTPUT_EMPTY, output);
        activity.addOutput(IfEmptyListActivity.OUTPUT_NON_EMPTY, outputNonEmpty);
        
        activity.process();
        output.verify();
    }

}
