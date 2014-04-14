/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.DelaysParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.LimitsParam;
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
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.tuple.Tuple;

/**
 * Activity to enforce limits.
 *
 */
public class LimitsActivity
extends MatchedIterativeActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        LimitsActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public LimitsActivity()
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
            new TypedOptionalActivityInput(
                LimitsParam.ROW_LIMIT,
                Long.class,
                LimitsParam.DEFAULT_ROWS
                ),
            new TypedOptionalActivityInput(
                LimitsParam.CELL_LIMIT,
                Long.class,
                LimitsParam.DEFAULT_CELLS
                ),
            new TypedOptionalActivityInput(
                LimitsParam.TIME_LIMIT,
                Long.class,
                LimitsParam.DEFAULT_TIME
                ),
            new TupleListActivityInput(
                DelaysParam.TUPLE_INPUT
                )
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
                DelaysParam.TUPLE_OUTPUT
                );
            writer = getOutput(
                DelaysParam.TUPLE_OUTPUT
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

