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
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Workflow for the CreateFTDQPResource Activity.
 *
 */
@Slf4j
public class CreateFireThornDQPWorkflow
extends BaseWorkflow
    {
    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public CreateFireThornDQPWorkflow(final String endpoint)
    throws MalformedURLException
        {
        super(
            endpoint
            );
        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public CreateFireThornDQPWorkflow(final URL endpoint)
        {
        super(
            endpoint
            );
        }

    /**
     * Public constructor.
     * @param service Our {@link OgsaServiceClient} client.
     *
     */
    public CreateFireThornDQPWorkflow(final OgsaServiceClient service)
        {
        super(
            service
            );
        }

    /**
     * Execute our workflow.
     *   
     */
    public ResourceWorkflowResult execute(final String ident)
        {
        return execute(
            new CreateFireThornDQPClient.Param()
                {
                @Override
                public String target()
                    {
                    return ident;
                    }
                }
            );
        }

    /**
     * Execute our workflow.
     *   
     */
    public ResourceWorkflowResult execute(final CreateFireThornDQPClient.Param param)
        {
        CreateFireThornDQPClient create = new CreateFireThornDQPClient(
            param
            );
        
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(
            create.output()
            );

        PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(create);
        workflow.add(deliver);

        try {
            return new SimpleResourceWorkflowResult(
                service().drer().execute(
                    workflow,
                    RequestExecutionType.SYNCHRONOUS
                    ),
                new ResourceID(
                    create.output().getDataValueIterator().nextAsString()
                    )
                );
            }
        catch (Exception ouch)
            {
            log.debug("Exception while creating DQP resource [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            return new SimpleResourceWorkflowResult(
                ouch
                );
            }
        }
    }
