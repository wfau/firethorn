// Copyright (c) The University of Edinburgh, 2009.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.join.Join;
import uk.org.ogsadai.tuple.join.JoinUtilities;

/**
 * Performs a join of two tuple lists. The join implementation is loaded at
 * runtime and is configured in the activity configuration. The join can be
 * an inner join or a left/right outer join.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>data2</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>condition</code>. Type: {@link java.lang.String} The join 
 * condition. This a mandatory input. </li>
 * <li> <code>data1Metadata</code>. Type: {@link java.lang.String} Metadata for
 * the data1 input data list. This an optional input. </li>
 * <li> <code>data2Metadata</code>. Type: {@link java.lang.String} Metadata for
 * the data2 input data list. This an optional input. </li>
 * <li> <code>readFirst</code>. Type: {@link java.lang.String} The name of the
 * data input that is read first (and stored). 
 * This is an optional input with the default value <code>data1</code>.</li>
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
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * <code>join.implementation</code> - a mandatory configuration
 * setting providing the name of a join implementation. This is the name of 
 * a Java class extending <code>uk.org.ogsadai.tuple.join.Join</code>.
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>First, a value is read from each of the condition, readFirst, 
 * data1Metadata and data2Metadata input. Then a complete tuple list is
 * read from one data input (as specified by the readFirst value). Finally
 * the other data input is streamed through, joining tuples by 
 * combining each streamed tuple with each of the stored tuples and evaluating
 * the condition on the joined tuple.</li>
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
 * The input stream of the readFirst input is stored. 
 * If the length of the
 * input lists is known in advance, or can be estimated, the user should ensure
 * that the longer list is connected to the other input for minimum
 * memory/storage use.
 * </li>
 * <li>
 * By default, the input from data1 is read first and stored.
 * </li>
 * <li>
 * If the metadata is provided on the both optional metadata inputs the activity
 * can start storing the readFirst side of the data input before the other side
 * becomes available. This can be useful to avoid deadlock problems.
 * </li>
 * <li>
 * Depending on the configured join implementation, the activity can produce an 
 * inner join or an outer join. Available join implementations are
 *  <ul>
 *   <li><code>uk.org.ogsadai.tuple.join.ProductJoin</code> - a simple join 
 *   implementation which takes the product of the two data inputs and evaluates 
 *   the join condition on each tuple. Applicable to any join condition.</li>
 *   <li><code>uk.org.ogsadai.tuple.join.ThetaJoin</code> - a hash join 
 *   implementation for join conditions with a primary expression. Raises
 *   an exception if no primary expression is found.</li>
 *   <li><code>uk.org.ogsadai.tuple.join.OuterJoin</code> - simple product
 *   join implementation which produces an outer join.</li>
 *  </ul>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleJoinActivity extends IterativeActivity implements ConfigurableActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Logger for this class. */
    private static final DAILogger LOG = DAILogger.getLogger(TupleJoinActivity.class);

    /** data1 input name. */
    private static final String INPUT_DATA1 = "data1";
    /** data2 input name. */
    private static final String INPUT_DATA2 = "data2";
    /** data1Metadata input name. */
    private static final String INPUT_DATA1_METADATA = "data1Metadata";
    /** data2Metadata input name. */
    private static final String INPUT_DATA2_METADATA = "data2Metadata";
    /** condition input name. */
    private static final String INPUT_CONDITION = "condition";
    /** readFirst input name. */
    private static final String INPUT_READ_FIRST = "readFirst";
    /** result output name. */
    private static final String OUTPUT_RESULT = "result";

    /** Output/ */
    protected BlockWriter mOutput;
    
    /** Function repository, or null if functions are not supported. */
    protected FunctionRepository mFunctionRepository;

    /** Join implementation */
    private static final Key JOIN_IMPLEMENTATION = 
        new Key("join.implementation");
    /** Join implementation class. */
    protected Join mJoin;
    /** Join name. */
    protected String mJoinName;
    
    /** Condition input. */
    protected TypedActivityInput mInputCondition;
    /** Left data input (data1). */
    protected TupleListActivityInput mInputDataLeft;
    /** Right data input (data2). */
    protected TupleListActivityInput mInputDataRight;
    /** Left data metadata input (data1Metadata). */
    protected TypedActivityInput mInputMetadataLeft;
    /** Right data metadata input (data2Metadata). */
    protected TypedActivityInput mInputMetadataRight;
    /** Read first input ("data1" or "data2"). */
    protected TypedActivityInput mInputReadFirst;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preprocess()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        mJoin = loadJoin(mJoinName);
        LOG.debug("Created new join instance.");
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
        validateInput(INPUT_CONDITION);
        BlockReader input = getInput(INPUT_CONDITION);
        mInputCondition = new TypedActivityInput(INPUT_CONDITION, String.class);
        mInputCondition.setBlockReader(input);
        validateInput(INPUT_DATA1);
        input = getInput(INPUT_DATA1);
        mInputDataLeft = new TupleListActivityInput(INPUT_DATA1);
        mInputDataLeft.setBlockReader(input);
        validateInput(INPUT_DATA2);
        input = getInput(INPUT_DATA2);
        mInputDataRight = new TupleListActivityInput(INPUT_DATA2);
        mInputDataRight.setBlockReader(input);
        input = getInput(INPUT_DATA1_METADATA);
        if (input != null)
        {
            mInputMetadataLeft = new TypedActivityInput(INPUT_DATA1_METADATA, String.class);
            mInputMetadataLeft.setBlockReader(input);
        }
        input = getInput(INPUT_DATA2_METADATA);
        if (input != null)
        {
            mInputMetadataRight = new TypedActivityInput(INPUT_DATA2_METADATA, String.class);
            mInputMetadataRight.setBlockReader(input);
        }
        validateInput(INPUT_READ_FIRST);
        input = getInput(INPUT_READ_FIRST);
        mInputReadFirst = new TypedActivityInput(INPUT_READ_FIRST, String.class);
        mInputReadFirst.setBlockReader(input);
        
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processIteration()
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        Object block = mInputCondition.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            iterativeStageComplete();
            return;
        }
        Expression condition = getCondition((String)block);
        if (mInputMetadataLeft == null || mInputMetadataRight == null)
        {
            processWithoutMetadata(condition);
        }
        else
        {
            processWithMetadata(condition);
        }
    }
    
    private void processWithMetadata(Expression condition)
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        TupleMetadata metadataLeft = getMetadata((String)mInputMetadataLeft.read());
        TupleMetadata metadataRight = getMetadata((String)mInputMetadataRight.read());
        String readFirst = (String)mInputReadFirst.read();
        boolean readRightInputFirst = INPUT_DATA2.equals(readFirst);
        mJoin.setCondition(condition);
        mJoin.storeRightTuples(readRightInputFirst);
        try
        {
            mJoin.configure(metadataLeft, metadataRight);
        }
        catch (Exception e)
        {
            // ColumnNotFoundException
            // UnsupportedOperandTypeException
            // IncomparableTypesException
            // ConfigurationException
            throw new ActivityUserException(e);
        }
        TupleListActivityInput streamedTuples;
        if (readRightInputFirst)
        {
            LOG.debug("Reading and storing RIGHT input data stream.");
            TupleListIterator tuples = (TupleListIterator)mInputDataRight.read();
            mJoin.storeTuples(tuples);
            streamedTuples = mInputDataLeft;
        }
        else
        {
            LOG.debug("Reading and storing LEFT input data stream.");
            TupleListIterator tuples = (TupleListIterator)mInputDataLeft.read();
            mJoin.storeTuples(tuples);
            streamedTuples = mInputDataRight;
        }
        TupleListIterator tuples = (TupleListIterator)streamedTuples.read();
        join(tuples, metadataLeft, metadataRight);
    }

    /**
     * Waits for the metadata from both streams to arrive before it starts
     * processing the join. This is the default behaviour if the metadata is not
     * specified seperately on another input. Used for backwards compatibility
     * although there's a danger that this may cause deadlocks in the workflow.
     * 
     * @param condition
     *            join condition
     * @throws ActivityUserException
     *             if a user exception occurred
     * @throws ActivityProcessingException
     *             if a server exception occurred
     * @throws ActivityTerminatedException
     *             if the activity was terminated
     */
    private void processWithoutMetadata(Expression condition)
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        String readFirst = (String)mInputReadFirst.read();
        boolean readRightInputFirst = INPUT_DATA2.equals(readFirst);
        TupleListIterator streamedTuples;
        TupleListIterator storedTuples;
        TupleMetadata metadataLeft;
        TupleMetadata metadataRight;
        if (readRightInputFirst)
        {
            LOG.debug("Reading RIGHT input data stream first.");
            storedTuples = (TupleListIterator)mInputDataRight.read();
            streamedTuples = (TupleListIterator)mInputDataLeft.read();
            metadataLeft = (TupleMetadata) streamedTuples.getMetadataWrapper().getMetadata();
            metadataRight = (TupleMetadata) storedTuples.getMetadataWrapper().getMetadata();
        }
        else
        {
            LOG.debug("Reading LEFT input data stream first...");
            storedTuples = (TupleListIterator)mInputDataLeft.read();
            LOG.debug("Have tuple list iterator for left");
            streamedTuples = (TupleListIterator)mInputDataRight.read();
            LOG.debug("Have tuple list iterator for right");
            metadataLeft = (TupleMetadata) storedTuples.getMetadataWrapper().getMetadata();
            LOG.debug("Have the left metadata " + metadataLeft);
            metadataRight = (TupleMetadata) streamedTuples.getMetadataWrapper().getMetadata();
            LOG.debug("Have the right metadata " + metadataRight);
        }
        mJoin.setCondition(condition);
        mJoin.storeRightTuples(readRightInputFirst);
        try
        {
            mJoin.configure(metadataLeft, metadataRight);
        }
        catch (Exception e)
        {
            // ColumnNotFoundException
            // UnsupportedOperandTypeException
            // IncomparableTypesException
            // ConfigurationException
            throw new ActivityUserException(e);
        }
        LOG.debug("Storing input tuples.");
        mJoin.storeTuples(storedTuples);
        join(streamedTuples, metadataLeft, metadataRight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }
    
    /**
     * Parses the metadata input and constructs a metadata object from it.
     * 
     * @param metadata
     *            metadata string
     * @return tuple metadata
     * @throws ActivityUserException
     *             if there was a problem parsing the string
     */
    private TupleMetadata getMetadata(String metadata) 
        throws ActivityUserException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new StringReader(metadata));
            String line;
            List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
            while ((line = reader.readLine()) != null)
            {
                String name = line;
                String tableName = reader.readLine();
                ResourceID resourceID = new ResourceID(reader.readLine());
                URI dresURI = new URI(reader.readLine());
                int type = Integer.parseInt(reader.readLine());
                int isNullable = Integer.parseInt(reader.readLine());
                int precision = Integer.parseInt(reader.readLine());
                int columnDisplaySize = Integer.parseInt(reader.readLine());
                ColumnMetadata column = 
                    new SimpleColumnMetadata(
                            name,
                            tableName, 
                            resourceID, 
                            dresURI, 
                            type, 
                            precision, 
                            isNullable, 
                            columnDisplaySize);
                columns.add(column);
            }
            TupleMetadata result = new SimpleTupleMetadata(columns);
            return result;
        }
        catch (IOException e)
        {
            throw new ActivityUserException(e);
        }
        catch (URISyntaxException e)
        {
            throw new ActivityUserException(e);
        }
    }

    /**
     * Returns the join condition as an expression tree.
     * 
     * @param condition
     *            join condition
     * @return root of the expression tree
     * @throws ActivityProcessingException
     * @throws ActivityUserException
     */
    private Expression getCondition(String condition) 
        throws ActivityProcessingException, ActivityUserException
    {
        try
        {
            CommonTree ast = 
                SQLQueryParser.getInstance().parseSQLForCondition(condition);
            return ExpressionFactory.buildExpression(ast, mFunctionRepository);
        }
        catch (SQLParserException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (ExpressionException e)
        {
            throw new ActivityUserException(e);
        }
    }
    
    private void join(
            TupleListIterator streamedTuples, 
            TupleMetadata metadataLeft, 
            TupleMetadata metadataRight) 
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        
        LOG.debug("Starting join.");
        writeBlock(ControlBlock.LIST_BEGIN);
        writeBlock(new MetadataWrapper(mJoin.getJoinMetadata()));
        Tuple tuple;
        LOG.debug("Streaming input tuples.");
        while ((tuple = (Tuple)streamedTuples.nextValue()) != null)
        {
            for (Tuple joined : mJoin.join(tuple))
            {
                writeBlock(joined);
            }
        }
        mJoin.close();
        writeBlock(ControlBlock.LIST_END);
        LOG.debug("Completed join.");
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

    private Join loadJoin(String impl) throws ActivityProcessingException
    {
        try
        {
            Class<? extends Join> joinClass = Class.forName(impl).asSubclass(Join.class);
            return (Join)joinClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (InstantiationException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ActivityProcessingException(e);
        }

    }
    
    public void configureActivity(KeyValueProperties properties)
    {
        mJoinName = (String)properties.get(JOIN_IMPLEMENTATION);
        LOG.debug("Using join implementation: " + mJoinName);
    }


}
