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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.exception.ActivityOutputUnreadableException;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;

/**
 * Workflow to test the {@link SQLQuery} Activity.
 *
 */
@Slf4j
public class SQLQueryWorkflow
    {
    /**
     * Public interface for the workflow params.
     *
     */
    public interface Param
        {
        public String source();
        public String query();
        }
    
    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public SQLQueryWorkflow(final String endpoint)
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
    public SQLQueryWorkflow(final URL endpoint)
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
    public SQLQueryWorkflow(final OgsaServiceClient service)
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
     * @return A {@link SimpleResourceWorkflowResult} containing the results.
     *   
     */
    public SimpleWorkflowResult execute(final Param param)
        {
        final SQLQuery sqlquery = new SQLQuery();
        sqlquery.setResourceID(
            param.source()
            );
        sqlquery.addExpression(
            param.query()
            );
        
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(
            sqlquery.getDataOutput()
            );

        PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(sqlquery);
        workflow.add(deliver);

        try {
            return new SimpleWorkflowResult(
                servce.drer().execute(
                    workflow,
                    RequestExecutionType.SYNCHRONOUS
                    )
                );
            }
        catch (ServerCommsException ouch)
            {
            log.debug("ServerCommsException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ServerCommsException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ServerException ouch)
            {
            log.debug("ServerException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ServerException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientToolkitException ouch)
            {
            log.debug("ClientToolkitException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ClientToolkitException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ResourceUnknownException ouch)
            {
            log.debug("ResourceUnknownException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ResourceUnknownException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ClientException ouch)
            {
            log.debug("ClientException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ClientException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (RequestException ouch)
            {
            log.debug("RequestException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "RequestException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        catch (ActivityOutputUnreadableException ouch)
            {
            log.debug("ActivityOutputUnreadableException while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                "ActivityOutputUnreadableException while creating data source [" + ouch.getMessage() + "]",
                ouch
                );
            }
        }
    }
