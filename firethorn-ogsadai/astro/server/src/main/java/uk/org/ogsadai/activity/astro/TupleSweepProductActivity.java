package uk.org.ogsadai.activity.astro;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
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
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * This activity executes a join of two ordered datasets where the join
 * condition is a range:
 * 
 * <pre>
 * a - windowSize &lt; x &lt; a + windowSize
 * </pre>
 * 
 * where a is a column of the first dataset and x is a column of the second
 * dataset.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li><code>data1</code>. Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * <li><code>data2</code>. Type: Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * <li><code>column1</code>. Type: {@link java.lang.String} or
 * {@link java.lang.Number}. Index or name of column in data1 to match against
 * the range. This is a mandatory input.</li>
 * <li><code>column2</code>. Type: {@link java.lang.String} or
 * {@link java.lang.Number}. Index or name of column in data2 to match against
 * the range. This is a mandatory input.</li>
 * <li><code>windowSize</code>. {@link java.lang.Number}. Specifies the range of
 * the sweep window. This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>. Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. The tuples produced by the
 * query.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>First, a value is read from each of the column1, column2, windowSize
 * inputs. Values from the second data input that match the sweep window are
 * stored in memory. The first data input is streamed through.</li>
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
 * This activity accepts two ordered tuple lists and performs an (inner) join
 * with condition <code>column1 - windowSize < column2 < column1 + windowSize.</code></li>
 * <li>Both input data sets must be ordered.</li>
 * <li>The columns that are being compared must be numeric.</li>
 * <li>Partial data may be produced if an error occurs at any stage of
 * processing.</li>
 * <li>The activity stores the matching window of the data2 input for each value
 * of data1. All data is stored in memory. Users should be aware that if the
 * window size provided is large there is a risk of the activity running out of
 * memory.</li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote the list
 * begin marker and '<code>}</code>' to denote the list end marker and
 * parentheses to denote OGSA-DAI tuples):
 * 
 * <pre>
 * column1: a
 * column2: x
 * windowSize: 0.2
 * data1: { metadata(a, b) (1.0, 2) (2.0, 1) (NULL, 1) (3, 5) }
 * data2: { metadata(x, y) (1.1, 56) (1.9, -23) (2.1, 2) (NULL, 0) }
 * result: { metadata(a, b, x, y) (1.0, 2, 1.1, 56) (2.0, 1, 1.9, -23) (2.0, 1, 2.1, 2) }
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */

public class TupleSweepProductActivity extends MatchedIterativeActivity
{
    
    private static final Logger LOG = Logger.getLogger(TupleSweepProductActivity.class);
    
    public static final String INPUT_COLUMN_1 = "column1";
    public static final String INPUT_COLUMN_2 = "column2";
    public static final String INPUT_DATA_1 = "data1";
    public static final String INPUT_DATA_2 = "data2";
    public static final String INPUT_RADIUS = "radius";
    public static final String INPUT_OFFSET = "offset";
    public static final String INPUT_SIZE = "size";
    public static final String OUTPUT_RESULT = "result";
    
    private BlockWriter mOutput;

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
                new TypedActivityInput(INPUT_COLUMN_1, Object.class),
                new TypedActivityInput(INPUT_COLUMN_2, Object.class),
                new TupleListActivityInput(INPUT_DATA_1),
                new TupleListActivityInput(INPUT_DATA_2),
                new TypedOptionalActivityInput(INPUT_RADIUS, Number.class),
                new TypedOptionalActivityInput(INPUT_OFFSET, Number.class),
                new TypedOptionalActivityInput(INPUT_SIZE, Number.class),
        };
    }

    @Override
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
            throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException 
    {
        double offset, size;
        if (iterationData[4] != null)
        {
            offset = ((Number)iterationData[4]).doubleValue();
            size = 2*offset;
        }
        else 
        {
            if (iterationData[5] == null)
            {
                throw new InvalidActivityInputsException(1, INPUT_OFFSET);
            }
            if (iterationData[6] == null)
            {
                throw new InvalidActivityInputsException(1, INPUT_SIZE);
            }
            
            offset = ((Number)iterationData[5]).doubleValue();
            size = ((Number)iterationData[6]).doubleValue();
        }
        TupleListIterator data1 = (TupleListIterator)iterationData[2];
        TupleListIterator data2 = (TupleListIterator)iterationData[3];
        int column1 = getColumn(iterationData[0], data1.getMetadataWrapper());
        int column2 = getColumn(iterationData[1], data2.getMetadataWrapper());
        
        writeBlock(ControlBlock.LIST_BEGIN);
        TupleMetadata metadata = new SimpleTupleMetadata(
                (TupleMetadata)data1.getMetadataWrapper().getMetadata(),
                (TupleMetadata)data2.getMetadataWrapper().getMetadata());
        LOG.debug(metadata);
        writeBlock(new MetadataWrapper(metadata));
        TupleWindow window = new TupleWindow(data2, column2, mOutput);
        boolean data1isEmpty = true;
        Tuple tuple;
        while ((tuple = (Tuple)data1.nextValue()) != null)
        {
            data1isEmpty = false;
            try
            {
                Object object = tuple.getObject(column1);
                // ignore NULL
                if (Null.VALUE == object)
                {
                    continue;
                }
                double value = ((Number)object).doubleValue();
                double lower = value - offset;
                double upper = value - offset + size;
                if (!window.updateWindow(tuple, lower, upper))
                {
                    iterativeStageComplete();
                    return;
                }
            }
            catch (ColumnNotFoundException e)
            {
                throw new ActivityUserException(e);
            }
        }
        if (data1isEmpty)
        {
            // empty the other list and output nothing
            while ((tuple = (Tuple)data2.nextValue()) != null);
        }
        writeBlock(ControlBlock.LIST_END);
    }
    
    private int getColumn(Object column, MetadataWrapper metadataWrapper) 
        throws ActivityUserException 
    {
        TupleMetadata metadata = (TupleMetadata)metadataWrapper.getMetadata();
        int position = -1;
        if (column instanceof String)
        {
            TableColumn tc = new TableColumn((String)column);
            position = tc.getColumnIndex(metadata);
        }
        else if (column instanceof Number)
        {
            position = ((Number)column).intValue();
        }
        if (position < 0 || position > metadata.getColumnCount())
        {
            throw new ActivityUserException(
                    new ColumnNotFoundException(String.valueOf(column)));
        }
        return position;
    }

    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        // no postprocessing
    }
    
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

    private static class TupleWindow
    {

        private int mColumn;
        private TupleListIterator mData;
        private boolean mDataClosed = false;
        private List<Tuple> mMatching;
        private List<Tuple> mTuples;
        private BlockWriter mOut;

        public TupleWindow(TupleListIterator tuples, int column, BlockWriter output)
        {
            mColumn = column;
            mData = tuples;
            mMatching = new LinkedList<Tuple>();
            mTuples = new LinkedList<Tuple>();
            mOut = output;
        }
        
        public boolean updateWindow(
                Tuple tuple, double lower, double upper) 
            throws ActivityUserException, 
                   ActivityProcessingException,
                   ActivityTerminatedException 
        {
            try
            {
                // remove all tuples outside the matching window
                cleanTuplesWindow(lower);
                
                // now add all new tuples in the matching window
                addTuplesToWindow(lower, upper);

                // output products
                for (Tuple tuple2 : mMatching)
                {
                    Tuple product = new SimpleTuple(tuple, tuple2);
                    mOut.write(product);
                }
            }
            catch (ColumnNotFoundException e)
            {
                throw new ActivityUserException(e);
            } 
            catch (PipeClosedException e) 
            {
                return false;
            }
            catch (PipeIOException e) 
            {
                throw new ActivityProcessingException(e);
            }
            catch (PipeTerminatedException e) 
            {
                throw new ActivityTerminatedException();
            }
            return true;
        }
        
        private void cleanTuplesWindow(double lower)
        {
            while (mMatching.size() > 0)
            {
                Tuple tuple = mMatching.get(0);
                if (tuple.getDouble(mColumn) < lower)
                {
                    mMatching.remove(0);
                }
                else
                {
                    break;
                }
            }
        }
        
        private void addTuplesToWindow(double lower, double upper) 
            throws ColumnNotFoundException, 
                   ActivityUserException,
                   ActivityProcessingException, 
                   ActivityTerminatedException
        {
            while (mTuples.size() > 0)
            {
                Tuple tuple = mTuples.get(0);
                double value = tuple.getDouble(mColumn);
                if (value < upper) 
                {
                    if (value >= lower) 
                    {
                        mMatching.add(tuple);
                    }
                    mTuples.remove(0);
                }
                else
                {
                    break;
                }
            }
            Tuple tuple;
            while (!mDataClosed && (tuple = (Tuple)mData.nextValue()) != null)
            {
                Object object = tuple.getObject(mColumn);
                // ignore NULL
                if (object == Null.VALUE)
                {
                    continue;
                }
                double value = ((Number)object).doubleValue();
                if (value >= lower && value < upper) 
                {
                    mMatching.add(tuple);
                }
                else if (value >= upper)
                {
                    mTuples.add(tuple);
                }
            }
            mDataClosed = true;
        }
        
    }
}
