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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.ContextParam;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.authorization.SecurityContext;

/**
 * Initial Activity to begin a pipeline, initializing the RequestContext callback information.
 *
 */
public class ContextActivity
extends MatchedIterativeActivity
implements SecureActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        ContextActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public ContextActivity()
        {
        super();
        }

    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedOptionalActivityInput(
                ContextParam.CALLBACK_PROTOCOL_INPUT,
                String.class
                ),
            new TypedOptionalActivityInput(
                ContextParam.CALLBACK_HOST_INPUT,
                String.class
                ),
            new TypedOptionalActivityInput(
                ContextParam.CALLBACK_PORT_INPUT,
                String.class
                ),
            new TypedOptionalActivityInput(
                ContextParam.CALLBACK_BASE_INPUT,
                String.class
                ),
            new TypedActivityInput(
                ContextParam.CONTEXT_IDENT_INPUT,
                String.class
                ),
            new TypedActivityInput(
                ContextParam.CONTEXT_PIPELINE_INPUT,
                String.class
                )
            };
        }

	private RequestContext context ;

	@Override
	public void setSecurityContext(SecurityContext context)
		{
        if ((context != null) && (context instanceof RequestContext))
            {
            this.context = (RequestContext) context; 
            }
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
        logger.debug("preprocess()");
        try {
            validateOutput(
        		ContextParam.CONTEXT_PIPELINE_OUTPUT
                );
            writer = getOutput(
        		ContextParam.CONTEXT_PIPELINE_OUTPUT
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
        try {
        	//
        	// Initialise our callback tool.
        	final CallbackHandler callback = new CallbackHandler(
				context
				); 	        
            //
            // Read our context inputs.
	        final String proto = (String) inputs[0];
	        final String host  = (String) inputs[1];
	        final String port  = (String) inputs[2];
	        final String base  = (String) inputs[3];
	        final String ident = (String) inputs[4];
	        //
	        // Update our context.
	        if (proto != null)
	        	{
	        	this.context.builder().protocol(
        			proto
        			);
	        	}
	        if (host != null)
	        	{
	        	this.context.builder().host(
        			host
        			);
	        	}
	        if (port != null)
	        	{
	        	this.context.builder().port(
        			port
        			);
	        	}
	        if (base != null)
	        	{
	        	this.context.builder().base(
        			base
        			);
	        	}
	        if (ident != null)
	        	{
	        	this.context.ident(
        			ident
        			);
	        	}
	        //
	        // Send a callback to indicate the pipeline is running.
	        // BUG The callback arrives before the Hibernate Session has completed. 
			//callback.running();	        

			//
            // Pass on our pipeline data to the next activity. 
	        final String value = (String) inputs[5];
	        logger.debug(" value [" + value + "]");
            writer.write(
        		value
                );
        	}
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosed during processing");
            iterativeStageComplete();
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
        logger.debug("postprocess()");
        }
    }

