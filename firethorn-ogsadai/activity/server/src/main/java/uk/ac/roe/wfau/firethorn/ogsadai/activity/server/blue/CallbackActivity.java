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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
//import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.CallbackParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.data.DelaysParam;
import uk.ac.roe.wfau.firethorn.ogsadai.security.FirethornSecurityContext;
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
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.tuple.Tuple;

/**
 * Activity that sends status messages back to the BlueQuery callback endpoint. 
 * @todo Move a lot of this to a generic TupleProcessingActivity base class.
 *
 */
public class CallbackActivity
extends MatchedIterativeActivity
implements SecureActivity
    {

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        CallbackActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public CallbackActivity()
        {
        super();
        }

    @Override
    public ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[] {
            new TypedActivityInput(
                CallbackParam.QUERY_IDENT,
                String.class
                ),
            new TupleListActivityInput(
                CallbackParam.TUPLE_INPUT
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
    	try {
            validateOutput(
        		CallbackParam.TUPLE_OUTPUT
                );
            writer = getOutput(
        		CallbackParam.TUPLE_OUTPUT
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
        // Get our query ident. 
        this.ident = (String) inputs[0];        
        logger.debug("  Ident [{}]", ident);
        //
        // Get our tuple iterator.
        final TupleListIterator tuples = (TupleListIterator) inputs[1];

        //
        //
        try {
	        start(tuples);
	        running();
	        process(tuples);
	        done();
	        completed();
        	}
        catch (ActivityProcessingException ouch)
        	{
        	failed();
        	throw ouch ;
        	}
        catch (ActivityTerminatedException ouch)
        	{
        	failed();
        	throw ouch ;
        	}
        catch (ActivityUserException ouch)
        	{
        	failed();
        	throw ouch ;
        	}
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
        logger.debug("start()");
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
        logger.debug("done()");
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
    
    /**
     * Process our tuples.
     * @throws ActivityProcessingException 
     *  
     */
    private void process(final TupleListIterator tuples)
        throws ActivityProcessingException, ActivityTerminatedException
        {
        logger.debug("Starting process(TupleListIterator)");
        //
        // Process our tuples.
        try {
            for (Tuple tuple ; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
                {
                writer.write(
                    tuple
                    );
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
            logger.debug("Finished process(TupleListIterator)");
            }
        }

	@Override
	public void setSecurityContext(SecurityContext context)
		{
        if ((context != null) && (context instanceof FirethornSecurityContext))
            {
            setSecurityContext((FirethornSecurityContext) context); 
            }
		}

	private FirethornSecurityContext context ;
	protected void setSecurityContext(FirethornSecurityContext context)
		{
        this.context = context;
        logger.debug("setSecurityContext(FirethornSecurityContext)");
        logger.debug("  Base  [" + this.context.endpoint() + "]");
		}

	protected void running()
		{
		callback(
			"RUNNING",
			null
			);
		}

	protected void completed()
		{
		callback(
			"COMPLETED",
			null
			);
		}

	protected void failed()
		{
		callback(
			"FAILED",
			null
			);
		}

	protected void failed(final String message)
		{
		callback(
			"FAILED",
			message
			);
		}

	protected static final String ENDPOINT_BASE  = "endpoint"  ; 
	protected static final String ENDPOINT_IDENT = "ident" ; 
	protected static final String ENDPOINT_TEMPLATE = "{" + ENDPOINT_BASE + "}/blue/query/callback/{" + ENDPOINT_IDENT + "}" ; 

	protected static final String BLUE_QUERY_NEXT_KEY = "blue.query.next.status" ; 
	protected static final String BLUE_QUERY_TEXT_KEY = "blue.query.text" ; 

	protected String ident;
	protected String ident()
		{
		return this.ident ;
		}
	protected void ident(String ident)
		{
		this.ident = ident ;
		}
	
	protected void callback(final String status, final String text)
		{
        logger.debug("callback(String)");
        logger.debug("  Status  [" + status + "]");
        
		if (context != null)
			{
			//
			// The HTTP POST request params
			final Map<String, String> params = new HashMap<String, String>(); 
			if (status != null)
				{
				params.put(
					BLUE_QUERY_NEXT_KEY,
					status
					);
				}
			if (text != null)
				{
				params.put(
					BLUE_QUERY_TEXT_KEY,
					text
					);
				}
			//
			// The URL template values
			final Map<String, String> fields = new HashMap<String, String>(); 
			fields.put(
				ENDPOINT_BASE,
				context.endpoint()
				);
			fields.put(
				ENDPOINT_IDENT,
				this.ident()
				);

			/*
			 * This fails because spring-web-3.0.1 does not contain the right classes.
			 * https://stackoverflow.com/questions/15704342/how-to-properly-return-an-image-in-the-resonse-in-spring-app
			 * 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
				MediaType.MULTIPART_FORM_DATA
				);

			HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(params, headers);
			 */

	        logger.debug("Before callback");
	        logger.debug("  Ident  [{}]", ident);
	        logger.debug("  Base   [{}]", context.endpoint());
	        logger.debug("  Params [{}]", params);
	        logger.debug("  Fields [{}]", fields);
	        try {
				final ResponseBean bean = rest().postForObject(
					ENDPOINT_TEMPLATE,
					params,
					ResponseBean.class,
					fields
					);
	        	logger.debug("Response bean");
	        	logger.debug("  Ident [{}]", bean.getIdent());
	        	logger.debug("  Name  [{}]", bean.getName());
	        	logger.debug("  Self  [{}]", bean.getSelf());
        		}
	        catch (Exception ouch)
	        	{
		        logger.debug("Exception during callback", ouch);
	        	}
	        finally {
	        	logger.debug("After callback");
	        	}
			}
		}

    /**
     * The shared REST service client.
     * ** copied from MetadataServiceBase **
     *
     */
    private static final RestTemplate rest = new RestTemplate() ;
    static {
    	rest.setMessageConverters(
            Arrays.asList(
                new HttpMessageConverter<?>[]
                    {
                    new MappingJacksonHttpMessageConverter()
                    }
                )
            );
        }

    /**
     * The shared REST service client.
     * ** copied from MetadataServiceBase **
     *
     */
    public  RestTemplate rest()
        {
        return rest;
        }

    /**
     * JavaBean to handle the REST/JSON response.
     *  
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ResponseBean
    	{
        protected ResponseBean()
        	{
        	}

    	private String ident;
        public void setIdent(final String value)
            {
            this.ident = value;
            }
        public String getIdent()
            {
            return this.ident;
            }

    	private String name;
        public void setname(final String value)
            {
            this.name = value;
            }
        public String getName()
            {
            return this.name;
            }
        
    	private String self;
        public void setSelf(final String value)
            {
            this.self = value;
            }
        public String getSelf()
            {
            return this.self;
            }

    	private String status;
        public void setStatus(final String value)
            {
            this.status = value;
            }
        public String getStatus()
            {
            return this.status;
            }
    	}
   
    }

