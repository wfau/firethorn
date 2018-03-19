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

import org.antlr.runtime.tree.CommonTree;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.relational.pipelinedjoin.Controller;
import uk.org.ogsadai.activity.relational.pipelinedjoin.Streamer;
import uk.org.ogsadai.activity.relational.pipelinedjoin.Controller.Relation;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.join.JoinRelation;

/**
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PipelinedTupleJoinActivity extends IterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Logger for this class. */
    private static final DAILogger LOG =
        DAILogger.getLogger(PipelinedTupleJoinActivity.class);
    /** data1 input name. */
    public static final String INPUT_DATA1 = "data1";
    /** data2 input name. */
    public static final String INPUT_DATA2 = "data2";
    /** condition input name. */
    public static final String INPUT_CONDITION = "condition";
    /** result output name. */
    public static final String OUTPUT_RESULT = "result";

    /** Output/ */
    protected BlockWriter mOutput;
    /** Function repository, or null if functions are not supported. */
    protected FunctionRepository mFunctionRepository;
    /** Condition input. */
    protected TypedActivityInput mInputCondition;
    /** Left data input (data1). */
    protected TupleListActivityInput mInputDataLeft;
    /** Right data input (data2). */
    protected TupleListActivityInput mInputDataRight;

    JoinRelation mLeftRelation;
    
    JoinRelation mRightRelation;
    
    public static long t1;
    
    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        t1 = System.currentTimeMillis();
        
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
    protected void processIteration() throws ActivityProcessingException,
        ActivityTerminatedException, ActivityUserException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Start processing");
        
        Object block = mInputCondition.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            iterativeStageComplete();
            return;
        }
        Expression condition = getCondition((String)block);
        
        TupleListIterator leftTuples =
            (TupleListIterator) mInputDataLeft.read();
        TupleListIterator rightTuples =
            (TupleListIterator) mInputDataRight.read();
        TupleMetadata leftMetadata =
            (TupleMetadata) leftTuples.getMetadataWrapper().getMetadata();
        TupleMetadata rightMetadata =
            (TupleMetadata) rightTuples.getMetadataWrapper().getMetadata();
        
        mLeftRelation = new JoinRelation();
        mRightRelation = new JoinRelation();
        try
        {
            mLeftRelation.configure(condition, leftMetadata, rightMetadata);
            mRightRelation.configure(condition, rightMetadata, leftMetadata);
        }
        // ColumnNotFoundException, TypeMismatchException,
        // IncomparableTypesException, ConfigurationException
        catch (Exception e)
        {
            throw new ActivityUserException(e);
        }
        
        // spawn two threads sharing a lock and a reactor
        Controller controller =
            new Controller(mLeftRelation, mRightRelation, mOutput);
        Streamer leftStreamer =
            new Streamer(controller, leftTuples, Relation.LEFT);
        Streamer rightStreamer =
            new Streamer(controller, rightTuples, Relation.RIGHT);
        
        leftStreamer.start();
        rightStreamer.start();
        
        try
        {
            leftStreamer.join();
            rightStreamer.join();
        }
        catch (InterruptedException e) {
            // TODO: handle exception
        }
        
        // cleanup stuff
        if(controller.getException() != null)
        {
            processExceptions(controller.getException());
        }
        
        try
        {
            controller.finalize();
        }
        catch (Exception e)
        {
            processExceptions(e);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("End processing");
    }
    
    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // TODO Auto-generated method stub
        
    }
    
    private void processExceptions(Exception e)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        if (e instanceof PipeIOException)
            throw new ActivityProcessingException(e);
        else if (e instanceof PipeClosedException)
            iterativeStageComplete();
        else if (e instanceof PipeTerminatedException)
            throw new ActivityTerminatedException();
        else if (e instanceof ActivityUserException)
            throw (ActivityUserException) e;
        else if (e instanceof ActivityTerminatedException)
            throw (ActivityTerminatedException) e;
        else if (e instanceof ActivityProcessingException)
            throw (ActivityProcessingException) e;
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


}
