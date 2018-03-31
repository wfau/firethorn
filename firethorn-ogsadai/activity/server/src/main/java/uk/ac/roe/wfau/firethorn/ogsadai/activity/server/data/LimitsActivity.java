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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.LimitsParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue.CallbackHandler;
import uk.ac.roe.wfau.firethorn.ogsadai.context.RequestContext;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
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
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Activity to enforce limits.
 *
 */
public class LimitsActivity
extends MatchedIterativeActivity
implements SecureActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        LimitsActivity.class
        );

    /**
     * Our {@link Thread} {@link Executor} service.
     *
     */
    //private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Our request context.
     * 
     */
    private RequestContext context ;

    @Override
    public void setSecurityContext(SecurityContext context)
        {
        if ((context != null) && (context instanceof RequestContext))
            {
            this.context  = (RequestContext) context;
            }
        }

    /**
     * Our callback handler.
     * 
     */
    private CallbackHandler callback;
    
    /**
     * Public constructor.
     *
     */
    public LimitsActivity()
        {
        super();
        }

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
                LimitsParam.TUPLE_INPUT
                )
            };
        }

    /**
     * Our output {@link BlockWriter}.
     *
     */
    private BlockWriter writer;

    @Override
    protected void preprocess()
    throws ActivitySQLException, ActivityProcessingException
        {
        logger.debug("preprocess()");

		this.callback = new CallbackHandler(
			this.context
			); 	        

    	try {
            validateOutput(
                LimitsParam.TUPLE_OUTPUT
                );
            writer = getOutput(
                LimitsParam.TUPLE_OUTPUT
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
    protected void processIteration(final Object[] inputs)
    throws ActivityProcessingException,
           ActivityTerminatedException,
           ActivityUserException
        {
        logger.debug("processIteration(Object[])");

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
        // Send the LIST_BEGIN marker and the tuple metadata.
        start(tuples);
        //
        // Process our tuples.
        if (maxtime != LimitsParam.NaN)
            {
            outer(tuples, maxrows, maxcells, maxtime);
            }
        else {
            inner(tuples, maxrows, maxcells);
            }
        //
        // Send the LIST_END marker.
        done();
        }
    
    @Override
    protected void postprocess()
    throws ActivityProcessingException
        {
        logger.debug("postprocess()");
        }

    /**
     * Send the {@link ControlBlock.LIST_BEGIN} signal and tuple metadata.
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException 
     * @throws ActivityUserException 
     * 
     */
    private void start(final TupleListIterator tuples )
    throws ActivityProcessingException, ActivityUserException, ActivityTerminatedException
        {
        logger.debug("Start");
        try {
            writer.write(
                ControlBlock.LIST_BEGIN
                );
            writer.write(
                tuples.getMetadataWrapper()
                );
            }
        catch (PipeClosedException ouch)
            {
            logger.debug("PipeClosedException in start() [{}]", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeIOException ouch)
            {
            logger.debug("PipeIOException in start() [{}]", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeTerminatedException ouch)
            {
            logger.debug("PipeTerminatedException in start() [{}]", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        }
    
    /**
     * Send the {@link ControlBlock.LIST_END} signal and close our pipes.
     * 
     */
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
            logger.warn("PipeClosedException in done");
            }
        catch (PipeIOException ouch)
            {
            logger.debug("PipeIOException in done() [{}]", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (PipeTerminatedException ouch)
            {
            logger.debug("PipeTerminatedException in done() [{}]", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        finally
            {
            iterativeStageComplete();
            }
        }

    public static class WrapperException
    extends RuntimeException
        {
        private static final long serialVersionUID = 1L;

        public WrapperException(final Throwable cause)
            {
            super(cause);
            }
        }
    
    private void outer(final TupleListIterator tuples, final long maxrows, final long maxcells, final long maxtime)
        throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
        {
        logger.debug("Starting outer");
        final Future<?> future = executor.submit(
            new Runnable ()
                {
                public void run()
                    {
                    logger.debug("Running runnable");
                    try {
                        inner(
                            tuples,
                            maxrows,
                            maxcells
                            );
                        }
                    catch (Throwable ouch)
                        {
                        logger.debug("Wrapping exception [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                        throw new WrapperException(
                            ouch
                            ); 
                        }
                    finally {
                        logger.debug("Finished runnable");
                        }
                    }
                }
            );
        try {
            logger.debug("Trying future");
            future.get(
                maxtime,
                TimeUnit.MILLISECONDS
                );
            }
        catch (InterruptedException ouch)
            {
            logger.debug("Future interrupted [{}]", ouch.getClass().getName());
            logger.debug("Cancelling Future");
            future.cancel(true);
            logger.debug("Future cancelled");
            callback.truncated();
            throw new ActivityTerminatedException();
            }
        catch (TimeoutException ouch)
            {
            logger.debug("STOP -- Time limit reached [{}]", maxtime);
            logger.debug("Future timeout [{}]", ouch.getClass().getName());
            logger.debug("Cancelling Future");
            future.cancel(true);
            logger.debug("Future cancelled");
            callback.truncated();
            throw new ActivityTerminatedException();
            }
        catch (ExecutionException ouch)
            {
            logger.debug("Future ExecutionException [{}]", ouch.getClass().getName());
            logger.debug("Cancelling Future");
            logger.debug("Future cancelled");
            unwrap(ouch);
            }
        finally {
            logger.debug("Finished Future");
            }
        }

    /**
     * Unwrap an Exception from a {@link Future} {@link ExecutionException}.
     * 
     */
    private void unwrap(final Throwable exception)
    throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException    
        {
        logger.debug("Unwrapping exception [{}][{}]", exception.getClass().getName(), exception.getMessage());
        if (exception instanceof ActivityUserException)
            {
            throw (ActivityUserException) exception ; 
            }
        else if (exception instanceof ActivityProcessingException)
            {
            throw (ActivityProcessingException) exception ; 
            }
        else if (exception instanceof ActivityTerminatedException)
            {
            throw (ActivityTerminatedException) exception ; 
            }
        else if (exception.getCause() != null)
            {
            unwrap(
                exception.getCause()
                );
            }
        else {
            throw new ActivityProcessingException(
                exception
                );
            }
        }
    
    /**
     * Process our tuples.
     * @throws ActivityProcessingException 
     *  
     */
    private void inner(final TupleListIterator tuples, final long maxrows, final long maxcells)
        throws ActivityProcessingException, ActivityTerminatedException
        {
        logger.debug("Starting inner");
        //
        // Process our tuples.
        try {
            long rowcount = 0 ; 

            
            final MetadataWrapper wrapper  = tuples.getMetadataWrapper();
            final TupleMetadata metadata = (TupleMetadata) wrapper.getMetadata();
            final long colcount = metadata.getColumnCount();
            
            for (Tuple tuple ; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
                {
                if (maxrows != LimitsParam.NaN)
                    {
                    if (rowcount >= maxrows)
                        {
                        logger.debug("STOP -- Row limit reached [{}]", rowcount);
                        callback.truncated(
                            rowcount
                            );
                        break ;
                        }
                    }
                if (maxcells != LimitsParam.NaN)
                    {
                    long cellcount = rowcount * colcount;
                    if (cellcount >= maxcells)
                        {
                        logger.debug("STOP -- Cell limit reached [{}]", cellcount);
                        callback.truncated(
                            rowcount
                            );
                        break ;
                        }
                    }
                
                writer.write(
                    tuple
                    );
                rowcount++;
                }
            }
        
        catch (final ActivityTerminatedException ouch)
            {
            logger.warn("ActivityTerminatedException during processing");
            throw ouch;
            }
        catch (final ActivityProcessingException ouch)
            {
            logger.warn("ActivityProcessingException during processing");
            throw ouch;
            }
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosedException during processing");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (final PipeTerminatedException ouch)
            {
            logger.warn("PipeTerminatedException during processing");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (final PipeIOException ouch)
            {
            logger.warn("PipeIOException during processing");
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (final Throwable ouch)
            {
            logger.warn("Unknown Throwable during processing [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        finally {
            logger.debug("Finished inner");
            }
        }
    }

