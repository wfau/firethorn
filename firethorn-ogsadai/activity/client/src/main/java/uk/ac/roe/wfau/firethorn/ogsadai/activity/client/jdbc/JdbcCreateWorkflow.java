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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowException;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateParam;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.resource.ResourceID;

/**
 * OGSA-DAI client workflow to create a new JdbcResource.
 *
 */
@Slf4j
public class JdbcCreateWorkflow
    {

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
     * 
     */
    public ResourceID execute(final JdbcCreateParam param)
    throws WorkflowException
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
            client.drer().execute(
                workflow,
                RequestExecutionType.SYNCHRONOUS
                );
            if (create.result().hasData())
                {
                return new ResourceID(
                    create.result().getDataValueIterator().nextAsString()
                    ); 
                }
            log.debug("Null result");
            return null ;
            }
        catch (Exception ouch) {
            log.debug("Exception while creating data source [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new WorkflowException(
                "Exception creating data source",
                ouch
                );
            }
        }
    }
