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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import uk.ac.roe.wfau.firethorn.ogsadai.context.RequestContext;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.CallbackParam;

/**
 * Toolkit for sending a callback message to FireThorn. 
 *
 */
public class CallbackHandler
    {
    /**
     * The callback URL path, {@value}.
     * 
     */
    protected static final String CALLBACK_PATH = "/blue/query/callback/" ;

    /**
     * Our debug logger.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(
        CallbackHandler.class
        );

    /**
     * Protected constructor.
     *
     */
    public CallbackHandler(final RequestContext context)
        {
        super();
        this.context = context;
        }

	private RequestContext context ;

	public void running()
		{
		callback(
			"RUNNING",
			"PARTIAL",
			null
			);
		}

	public void running(final Long count)
		{
		callback(
            "RUNNING",
            "PARTIAL",
			count
			);
		}

	public void completed()
		{
		callback(
            "COMPLETED",
            "COMPLETED",
            null
			);
		}

	public void completed(final Long count)
		{
		callback(
            "COMPLETED",
			"COMPLETED",
			count
			);
		}

   public void truncated()
        {
        callback(
            null,
            "TRUNCATED",
            null
            );
        }

   public void truncated(final Long count)
       {
       callback(
           null,
           "TRUNCATED",
           count
           );
       }
   
	public void failed()
		{
		callback(
            "FAILED",
            "PARTIAL",
            null
			);
		}

	public void failed(final Long count)
		{
		callback(
            "FAILED",
            "PARTIAL",
			count
			);
		}

	protected void callback(final String taskState)
		{
		callback(
			taskState,
			null,
			null
			);
		}

	protected void callback(final String taskState, final Long count)
        {
        callback(
            taskState,
            null,
            count
            );
        }
	
	protected void callback(final String taskState, final String resultState, final Long count)
		{
        logger.debug("callback(String, Long)");
        logger.debug("  task    [" + taskState + "]");
        logger.debug("  results [" + resultState + "]");
        logger.debug("  count   [" + count + "]");
        
		if (context == null)
			{
	        logger.debug("null context - skipping callback");
			}
		else {
			if (context.ident() == null)
				{
		        logger.debug("null ident - skipping callback");
				}
			else {
				//
				// Build our callback endpoint.
				final StringBuilder endpoint = context.endpoint();
                endpoint.append(CALLBACK_PATH);
                endpoint.append(context.ident());

                logger.debug("Before callback");
		        logger.debug("  Ident    [{}]", context.ident());
		        logger.debug("  Endpoint [{}]", endpoint.toString());
		        try {
					final ResponseBean bean = rest().postForObject(
						endpoint.toString(),
						new RequestBean()
							{
							@Override
							public String getIdent()
								{
								return context.ident();
								}
							@Override
							public String getState()
								{
								return taskState;
								}
							@Override
							public Long getResultCount()
								{
								return count;
								}
                            @Override
                            public String getResultState()
                                {
                                return resultState;
                                }
                            @Override
                            public String getMessage()
                                {
                                // TODO Auto-generated method stub
                                return null;
                                }
							},
						ResponseBean.class
						);
		        	logger.debug("Response bean");
		        	logger.debug("  Ident [{}]", bean.getIdent());
                    logger.debug("  Name  [{}]", bean.getName());
                    logger.debug("  State [{}]", bean.getState());
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
		}

    /**
     * The shared REST service client.
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
     *
     */
    public  RestTemplate rest()
        {
        return rest;
        }

    /**
     * JavaBean to handle the REST/JSON request.
     *  
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    public abstract static class RequestBean
    implements CallbackParam.RequestBean
    	{
    	}

    /**
     * JavaBean to handle the REST/JSON response.
     *  
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ResponseBean
    implements CallbackParam.ResponseBean
    	{
        protected ResponseBean()
        	{
        	}

    	private String ident;
		@Override
        public String getIdent()
            {
            return this.ident;
            }
        public void setIdent(final String value)
            {
            this.ident = value;
            }

    	private String name;
		@Override
        public String getName()
            {
            return this.name;
            }
        public void setName(final String value)
            {
            this.name = value;
            }
        
    	private String self;
		@Override
        public String getSelf()
            {
            return this.self;
            }
        public void setSelf(final String value)
            {
            this.self = value;
            }

    	private String status;
		@Override
        public String getState()
            {
            return this.status;
            }
        public void setStatus(final String value)
            {
            this.status = value;
            }
    	}
    }

