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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
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
 * Workflow for the IvoaCreateResource Activity.
 *
 */
@Slf4j
public class IvoaCreateResourceWorkflow
    {
    /**
     * Public interface for the workflow params.
     *
     */
    public interface Param
    extends IvoaCreateResourceClient.Param
        {
        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public IvoaCreateResourceWorkflow(final String endpoint)
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
    public IvoaCreateResourceWorkflow(final URL endpoint)
        {
        this(
            new OgsaServiceClient(
                endpoint
                )
            );
        }

    /**
     * Public constructor.
     * @param service Our {@link OgsaServiceClient} client.
     *
     */
    public IvoaCreateResourceWorkflow(final OgsaServiceClient service)
        {
        this.servce = service ;
        }

    /**
     * Our {@link OgsaServiceClient} client.
     * 
     */
    private OgsaServiceClient servce ;  
    
    /**
     * Execute our workflow.
     * @param The workflow params.
     * @return A {@link CreateResourceResult} containing the results.
     *   
     */
    public CreateResourceResult execute(final Param param)
        {
        IvoaCreateResourceClient create = new IvoaCreateResourceClient(
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
            return new CreateResourceResult(
                servce.drer().execute(
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
            return new CreateResourceResult(
                "ServerCommsException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ServerException ouch)
            {
            log.debug("ServerException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "ServerException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientToolkitException ouch)
            {
            log.debug("ClientToolkitException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "ClientToolkitException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ResourceUnknownException ouch)
            {
            log.debug("ResourceUnknownException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "ResourceUnknownException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientException ouch)
            {
            log.debug("ClientException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "ClientException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (RequestException ouch)
            {
            log.debug("RequestException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "RequestException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (DataStreamErrorException ouch)
            {
            log.debug("DataStreamErrorException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "DataStreamErrorException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (UnexpectedDataValueException ouch)
            {
            log.debug("UnexpectedDataValueException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "UnexpectedDataValueException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (DataSourceUsageException ouch)
            {
            log.debug("DataSourceUsageException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "DataSourceUsageException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ActivityOutputUnreadableException ouch)
            {
            log.debug("ActivityOutputUnreadableException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new CreateResourceResult(
                "ActivityOutputUnreadableException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        }
    }
