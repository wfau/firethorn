/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowException;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.exception.ActivityOutputUnreadableException;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.resource.ResourceID;

/**
 * OGSA-DAI client workflow to create a new JdbcResource.
 *
 */
@Slf4j
public class JdbcCreateWorkflow
    {
    /**
     * Public interface for the workflow params.
     *
     */
    public interface Param
    extends JdbcCreateActivity.Param
        {
        }

    /**
     * Workflow results.
     * 
     */
    public static class Result
    extends SimpleWorkflowResult
        {
        public Result(final RequestResource request, ResourceID resource)
            {
            super(
                request
                );
            this.resource = resource;
            }

        /**
         * Public constructor.
         * 
         */
        public Result(final Throwable cause)
            {
            super(
                cause
                );
            }

        /**
         * Public constructor.
         * 
         */
        public Result(final String message, final Throwable cause)
            {
            super(
                message,
                cause
                );
            }

        /**
         * The created resource ID.
         * 
         */
        private ResourceID resource ;

        /**
         * The created resource ID.
         * 
         */
        public ResourceID resource()
            {
            return this.resource;
            }
        }
    
    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public JdbcCreateWorkflow(final String endpoint)
    throws MalformedURLException
        {
        this(
            new OgsaServiceClient(
                new URL(
                    endpoint
                    )
                )
            );
        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public JdbcCreateWorkflow(final URL endpoint)
        {
        this(
            new OgsaServiceClient(
                endpoint
                )
            );
        }

    /**
     * Public constructor.
     * @param endpoint Our {@link OgsaServiceClient} client.
     *
     */
    public JdbcCreateWorkflow(final OgsaServiceClient client)
        {
        this.client = client ;
        }

    /**
     * Our {@link OgsaServiceClient} client.
     * 
     */
    private OgsaServiceClient client ;  
    
    /**
     * Execute our workflow.
     * @param The workflow params.
     * 
     */
    public Result execute(final Param param)
        {
        JdbcCreateActivity create = new JdbcCreateActivity(
            param
            ); 
        
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(
            create.result()
            );

        PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(create);
        workflow.add(deliver);

        try {
            return new Result(
                client.drer().execute(
                    workflow,
                    RequestExecutionType.SYNCHRONOUS
                    ),
                new ResourceID(
                    create.result().getDataValueIterator().nextAsString()
                    )
                );
            }
        catch (ServerCommsException ouch)
            {
            log.debug("ServerCommsException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ServerCommsException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ServerException ouch)
            {
            log.debug("ServerException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ServerException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientToolkitException ouch)
            {
            log.debug("ClientToolkitException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ClientToolkitException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ResourceUnknownException ouch)
            {
            log.debug("ResourceUnknownException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ResourceUnknownException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientException ouch)
            {
            log.debug("ClientException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ClientException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (RequestException ouch)
            {
            log.debug("RequestException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "RequestException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (DataStreamErrorException ouch)
            {
            log.debug("DataStreamErrorException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "DataStreamErrorException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (UnexpectedDataValueException ouch)
            {
            log.debug("UnexpectedDataValueException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "UnexpectedDataValueException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (DataSourceUsageException ouch)
            {
            log.debug("DataSourceUsageException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "DataSourceUsageException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ActivityOutputUnreadableException ouch)
            {
            log.debug("ActivityOutputUnreadableException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new Result(
                "ActivityOutputUnreadableException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        }
    }
