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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.query;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.InsertClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.resource.ResourceID;

/**
 * OGSA-DAI client workflow to create a new JdbcResource.
 *
 */
@Slf4j
public class QueryWorkflow
    {
    /**
     * Public interface for the workflow parameters.
     *
     */
    public interface Param
        {
        /**
         * The target resource ID.
         *
         */
        public ResourceID resource();

        /**
         * The ADQL/SQL query.
         *
         */
        public String query();

        /**
         * The {@link InsertActivity} params.
         *
         */
        public InsertClient.Param insert();
        
        /**
         * The {@link DelaysActivity} params.
         *
         */
        public DelaysClient.Param  delays();

        /**
         * The {@link LimitsActivity} params.
         *
         */
        public LimitsClient.Param limits();

        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public QueryWorkflow(final String endpoint)
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
    public QueryWorkflow(final URL endpoint)
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
    public QueryWorkflow(final OgsaServiceClient client)
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
     * 
     */
    public WorkflowResult execute(final Param param)
        {
        final SQLQuery select = new SQLQuery();
        select.setResourceID(
            param.resource()
            );
        select.addExpression(
            param.query()
            );

        final DelaysClient delay = new DelaysClient(
            select.getDataOutput(),
            param.delays()
            );

        final LimitsClient limits = new LimitsClient(
            delay.output(),
            param.limits()
            );
        
        final InsertClient insert = new InsertClient(
            limits.output(),
            param.insert()
            );
        
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        delivery.connectInput(
            insert.output()
            );

        final PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(
            select
            );
        workflow.add(
            delay
            );
        workflow.add(
            limits
            );
        workflow.add(
            insert
            );
        workflow.add(
            delivery
            );
        
        try {
            return new SimpleWorkflowResult(
                client.drer().execute(
                    workflow,
                    RequestExecutionType.SYNCHRONOUS
                    )
                );
            }
        catch (ResourceUnknownException ouch)
            {
            log.debug("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleWorkflowResult(
                ouch
                );
            }
        catch (RequestException ouch)
            {
            log.debug("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleWorkflowResult(
                ouch
                );
            }
        catch (ClientException ouch)
            {
            log.debug("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleWorkflowResult(
                ouch
                );
            }
        }
    }
