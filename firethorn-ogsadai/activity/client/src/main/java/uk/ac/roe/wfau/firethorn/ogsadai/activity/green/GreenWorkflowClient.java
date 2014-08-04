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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.green;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.management.ExtendedCreateRelationalResource;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Pipeline client that connects the components we need.
 *
 */
@Slf4j
public class GreenWorkflowClient
    {
    public static final String DRER_IDENT = "DataRequestExecutionResource" ;

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public GreenWorkflowClient(final String endpoint)
    throws MalformedURLException
        {
        this(
            new URL(
                endpoint
                )
            );
        }

    /**
     * Public constructor.
     *
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public GreenWorkflowClient(final URL endpoint)
        {
        this.endpoint = endpoint ;
        }

    /**
     * Our OGSA-DAI server endpoint URL.
     * 
     */
    private final URL endpoint ;

    /**
     * Our OGSA-DAI server client.
     * 
     */
    private Server server ; 

    /**
     * Our OGSA-DAI server client.
     * 
     */
    protected synchronized Server server()
        {
        if (this.server == null)
            {
            this.server = new JerseyServer();
            }
        return this.server;
        }

    /**
     * Our OGSA-DAI DRER client.
     * 
     */
    private DataRequestExecutionResource drer ; 

    /**
     * Our OGSA-DAI DRER client.
     * 
     */
    protected synchronized DataRequestExecutionResource drer()
        {
        if (this.drer == null)
            {
            this.drer = server().getDataRequestExecutionResource(
                new ResourceID(
                    DRER_IDENT
                    )
                );
            }
        return this.drer;
        }
    
    /**
     * Create our RelationalResource.
     * 
     */
    public ResourceID create()
        {
        // Setup
        ExtendedCreateRelationalResource create = new ExtendedCreateRelationalResource();
        create.addTemplateId(
            new ResourceID(
                "uk.org.ogsadai.JDBC_RESOURCE_TEMPLATE"
                )
            );
        create.addDatabaseURL(
            "jdbc:jtds:sqlserver://localhost:1432/ATLASDR1"
            );
        create.addDriverClass(
    		"net.sourceforge.jtds.jdbc.Driver"
            );
        create.addDatabaseUser(
    		"atlasro"
            );
        create.addDatabasePassword(
            "atlasropw"
            );
        
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(
            create.getResultOutput()
            );

        // Workflow.
        PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(create);
        workflow.add(deliver);

        // Execute.
        try {
            drer().execute(
                workflow,
                RequestExecutionType.SYNCHRONOUS
                );
            ResourceID resource = create.nextResult();
            log.debug("Got data source ID [{}]", resource);
            return resource ;
            }
        catch (Exception ouch) {
            log.debug("Exception while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return null ;
            }
        }
    
    /**
     * Query a RelationalResource.
     * 
     */
    public void query(final ResourceID target)
        {
        final Server server = new JerseyServer();
        server.setDefaultBaseServicesURL(
            this.endpoint
            );
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                DRER_IDENT
                )
            );
        // Workflow.
        PipelineWorkflow workflow = new PipelineWorkflow();
        //
        // Add our SQLQuery Activity.
        final SQLQuery select = new SQLQuery();
        workflow.add(
            select
            );
        select.setResourceID(
            target
            );
        select.addExpression(
            "SELECT TOP 10 ra, dec FROM atlassource"
            );
        //
        // Create our delivery handler.
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        workflow.add(
            delivery
            );
        delivery.connectInput(
            select.getDataOutput()
            );
        //
        // Execute our pipeline.
        try {
            final RequestResource request = drer.execute(
                workflow,
                RequestExecutionType.SYNCHRONOUS
                );
            }
        catch (final Exception ouch)
            {
            log.debug("Exception during request processing [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            }
        }
    }

