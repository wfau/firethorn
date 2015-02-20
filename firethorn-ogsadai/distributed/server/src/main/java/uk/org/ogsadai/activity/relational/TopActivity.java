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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
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
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.ExpressionFactory;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * An activity selects tuples from the input list according to a condition 
 * (similar to a WHERE clause in SQL).
 * <p> 
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li><code>condition</code>. Type: {@link java.lang.String}. The condition to evaluate
 * for each input tuple.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The tuples from the input data that meet the condition.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A value is read from the condition input before the input data is
 * processed.</li>
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
 * Evaluates the condition on a tuple and writes the tuple to the output if
 * the condition evaluates to <code>TRUE</code>.
 * </li>
 * <li>
 * The format of the condition is like an SQL WHERE or HAVING clause.
 * It can contain column names, arithmetic and boolean operators and constants. 
 * </li>
 * <li>
 * Columns can be identified by providing the full name (using the table name
 * prefix and column name TABLE.COLUMN) or by the simple name (using COLUMN name 
 * only without the table name prefix).
 * If a simple column name is ambiguous the full name must be used, otherwise
 * an exception is raised.
 * </li>
 * <li>
 * Like in SQL, comparison to a NULL value always evaluates to FALSE.
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   condition: a < b
 *   data: { metadata(a, b) (1, 2) (2, 1) (NULL, 1) }
 *   result: { metadata(a, b) (1, 2) }
 * </pre>
 * <pre>
 *   condition: (a + b)/3 = c
 *   data: { metadata(a, b, c) (1, 2, 3) (0, 3, 1) (0, 0, 0) }
 *   result: { metadata(a, b, c) (0, 3, 1) (0, 0, 0) }
 * </pre>
 * <pre>
 *   condition: A.id = B.id
 *   data: { metadata(A.id, B.id) (5, 5) (1, 2) (2, 3) (4, 4) }
 *   result: { metadata(A.id, B.id) (5, 5) (4, 4)  }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TopActivity extends MatchedIterativeActivity
{

    /**
     * Parameter name for the row limit.
     * 
     */
    public static final String ROW_LIMIT  = "limit.rows"  ;

    /**
     * Default row limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_ROWS = new Long(0);
    
    /**
     * Parameter name for the cell limit.
     * 
     */
    public static final String CELL_LIMIT = "limit.cells" ;

    /**
     * Default cell limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_CELLS = new Long(0);
    
    /**
     * Parameter name for the time limit.
     * 
     */
    public static final String TIME_LIMIT = "limit.time"  ;

    /**
     * Default time limit (0 = no limit).
     * 
     */
    public static final Long DEFAULT_TIME = new Long(0);
    
    /**
     * Parameter name for the input tuples.
     * 
     */
    public static final String TUPLE_INPUT  = "tuples" ;
    
    /**
     * Parameter name for the output tuples.
     * 
     */
    public static final String TUPLE_OUTPUT = "tuples" ;

    /**
     * Default Activity name.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.org.ogsadai.Top" ;
    

    /**
     * Parameter name for the row number column name.
     * 
     */
    public static final String COLUMN_NAME = "colname" ;

    /**
     * Parameter name for the delay before the first row.
     * 
     */
    public static final String FIRST_DELAY = "delay.first" ;

    /**
     * Default delay before the first row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_FIRST = new Integer(0);

    /**
     * Parameter name for the delay after the last row.
     * 
     */
    public static final String LAST_DELAY  = "delay.last"  ;

    /**
     * Default delay after the last row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_LAST = new Integer(0);
    
    /**
     * Parameter name for the delay between every row.
     * 
     */
    public static final String EVERY_DELAY = "delay.every" ;

    /**
     * Default delay for every row (0 = no delay).
     * 
     */
    public static final Integer DEFAULT_EVERY = new Integer(0);


/**
 * Our debug logger.
 *
 */
private static Logger logger = LoggerFactory.getLogger(
    TopActivity.class
    );

/**
 * Public constructor.
 *
 */
public TopActivity()
    {
    super();
    }

/**
 * {@inheritDoc}
 */
@Override
public ActivityInput[] getIterationInputs()
    {
    return new ActivityInput[] {

        };
    }

/**
 * Block writer for our output.
 *
 */
private BlockWriter writer;

/**
 * {@inheritDoc}
 */
@Override
protected void preprocess()
throws ActivitySQLException, ActivityProcessingException
    {
    logger.debug("preprocess()");
	try {
        validateOutput(
        		this.TUPLE_OUTPUT
            );
        writer = getOutput(
        		this.TUPLE_OUTPUT
            );
        }
    catch (final Exception ouch)
        {
        logger.warn("Exception validating outputs", ouch);
        throw new ActivityProcessingException(
            ouch
            );
        }
    }

/**
 * {@inheritDoc}
 */
@Override
protected void processIteration(final Object[] inputs)
throws ActivityProcessingException,
       ActivityTerminatedException,
       ActivityUserException
    {
    logger.debug("processIteration(Object[])");
    try {
        //
        // Get our limits.
        final long maxrows  = (Long) inputs[0];
        final long maxcells = (Long) inputs[1];
        final long maxtime  = (Long) inputs[2];

        logger.debug("Max rows  [{}]", maxrows);
        logger.debug("Max cells [{}]", maxcells);
        logger.debug("Max time  [{}]", maxtime);
        
        //
        // Get our tuple iterator.
        final TupleListIterator tuples = (TupleListIterator) inputs[3];

        //
        // Write the LIST_BEGIN marker and the tuple metadata.
        logger.debug("Starting");
        writer.write(
            ControlBlock.LIST_BEGIN
            );
        writer.write(
            tuples.getMetadataWrapper()
            );

        //
        // Process our tuples.
        long rowcount = 0 ; 
        for (Tuple tuple ; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
            {
            writer.write(
                tuple
                );
            rowcount++;
            if (maxrows != 0)
                {
                if (rowcount >= maxrows)
                    {
                    logger.debug("STOP -- Row limit reached [{}]", maxrows);
                    iterativeStageComplete();
                    break ;
                    }
                }
            }
        //
        // Write the list end marker
        done();
        }
    catch (final ActivityProcessingException ouch)
        {
        throw ouch ;
        }
    catch (final PipeClosedException ouch)
        {
        logger.warn("PipeClosed during processing");
        iterativeStageComplete();
        done();
        }
    catch (final Throwable ouch)
        {
        logger.warn("Exception during processing", ouch);
        throw new ActivityProcessingException(
            ouch
            );
        }
    }

/**
 * {@inheritDoc}
 */
@Override
protected void postprocess()
throws ActivityProcessingException
    {
    logger.debug("postprocess()");
    }

// Common base class ?
private void done()
throws ActivityProcessingException
    {
    try {
        writer.write(
            ControlBlock.LIST_END
            );
        }
    catch (final PipeClosedException ouch)
        {
        logger.warn("PipeClosed during done");
        }
    catch (final Throwable ouch)
        {
        logger.warn("Exception during closing", ouch);
        throw new ActivityProcessingException(
            ouch
            );
        }
    }
}

