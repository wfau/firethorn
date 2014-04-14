/*
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
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.tuple.Tuple;

/**
 * Activity to add processing delays.
 *
 */
public class DelaysActivity
extends MatchedIterativeActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        DelaysActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public DelaysActivity()
        {
        super();
        }

    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedOptionalActivityInput(
                DelaysParam.FIRST_DELAY,
                Integer.class,
                DelaysParam.DEFAULT_FIRST
                ),
            new TypedOptionalActivityInput(
                DelaysParam.LAST_DELAY,
                Integer.class,
                DelaysParam.DEFAULT_LAST
                ),
            new TypedOptionalActivityInput(
                DelaysParam.EVERY_DELAY,
                Integer.class,
                DelaysParam.DEFAULT_EVERY
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
            // Get our delay values.
            final Integer first = (Integer) inputs[0];
            final Integer last  = (Integer) inputs[1];
            final Integer every = (Integer) inputs[2];
            //
            // Get our tuple iterator.
            final TupleListIterator tuples = (TupleListIterator) inputs[3];
            //
            // Write the LIST_BEGIN marker and the tuple metadata.
            writer.write(
                ControlBlock.LIST_BEGIN
                );
            writer.write(
                tuples.getMetadataWrapper()
                );
            //
            // Wait for the initial delay.
            pause(
                first
                );
            //
            // Process our tuples.
            for (Tuple tuple ; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
                {
                pause(
                    every
                    );
                writer.write(
                    tuple
                    );
                }
            //
            // Wait for the final delay.
            pause(
                last
                );
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
    
    /**
     * Helper method to wrap up the parameter checking.
     * 
     */
    private void pause(Integer delay)
    throws InterruptedException
        {
        if ((delay != null) && (delay > 0))
            {
            Thread.sleep(
                delay
                );
            }
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

