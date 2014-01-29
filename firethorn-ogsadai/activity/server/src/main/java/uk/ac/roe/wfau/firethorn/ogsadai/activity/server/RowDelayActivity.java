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

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.tuple.Tuple;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.RowDelayParam ;

/**
 *
 *
 */
public class RowDelayActivity
extends MatchedIterativeActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        RowDelayActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public RowDelayActivity()
        {
        super();
        }

    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedActivityInput(
                RowDelayParam.START_DELAY_INPUT,
                Integer.class
                ),
            new TypedActivityInput(
                RowDelayParam.END_DELAY_INPUT,
                Integer.class
                ),
            new TypedActivityInput(
                RowDelayParam.PAGE_SIZE_INPUT,
                Integer.class
                ),
            new TypedActivityInput(
                RowDelayParam.PAGE_DELAY_INPUT,
                Integer.class
                ),
            new TypedActivityInput(
                RowDelayParam.ROW_DELAY_INPUT,
                Integer.class
                ),
            new TupleListActivityInput(
                RowDelayParam.TUPLE_ITER_INPUT
                )
            };
        }

    /**
     * Block writer for our output.
     *
     */
    private BlockWriter writer;

    @Override
    protected void preprocess()
    throws ActivitySQLException, ActivityProcessingException
        {
    	try {
            validateOutput(
                RowDelayParam.TUPLE_ITER_OUTPUT
                );
            writer = getOutput(
                RowDelayParam.TUPLE_ITER_OUTPUT
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

    @Override
    protected void processIteration(final Object[] iteration)
    throws ActivityProcessingException,
           ActivityTerminatedException,
           ActivityUserException
        {
        try {
            //
            // Get our delay values.
            final Integer startDelay = (Integer) iteration[0];
            final Integer endDelay   = (Integer) iteration[1];
            final Integer pageSize   = (Integer) iteration[2];
            final Integer pageDelay  = (Integer) iteration[3];
            final Integer rowDelay   = (Integer) iteration[4];
            //
            // Get our tuple iterator.
            final TupleListIterator tuples = (TupleListIterator) iteration[5];
            //
            // Write the LIST_BEGIN marker and the tuple metadata.
            writer.write(
                ControlBlock.LIST_BEGIN
                );
            writer.write(
                tuples.getMetadataWrapper().getMetadata()
                );
            //
            // Wait for the initial delay.
            logger.debug("Start [{}]", startDelay);
            pause(
                startDelay
                );
            //
            // Process our tuples.
            Tuple tuple ;
            int   count = 0 ;
            while ((tuple = (Tuple) tuples.nextValue()) != null)
                {
                //
                // Wait for the page delay.
                logger.debug("Page [{}][{}]", pageSize, pageDelay);
                if ((pageSize != null) && (pageSize > 0))
                    {
                    if (count >= pageSize)
                        {
                        count = 0 ;
                        pause(
                            pageDelay
                            );
                        }
                    else {
                        count++ ;
                        }
                    }
                //
                // Wait for the row delay
                logger.debug("Row [{}]", rowDelay);
                pause(
                    rowDelay
                    );
                //
                // Write the tuple to our output.
                writer.write(
                    tuple
                    );
                }
            //
            // Wait for the final delay.
            logger.debug("End [{}]", endDelay);
            pause(
                endDelay
                );
            //
            // Write the list end marker
            writer.write(
                ControlBlock.LIST_END
                );
            }
        catch (final Throwable ouch)
            {
            logger.warn("Exception during processing", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    @Override
    protected void postprocess()
    throws ActivityProcessingException
        {
        }
    
    /**
     * Helper method to wrap up the parameter checking and exception handling.
     * 
     */
    private void pause(Integer delay)
        {
        if ((delay != null) && (delay > 0))
            {
            try {
                Thread.sleep(
                    delay
                    );
                }
            catch (InterruptedException ouch)
                {
                logger.warn("Exception during pause", ouch);
                }
            }
        }
    }

