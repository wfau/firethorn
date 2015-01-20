/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 * Test for JdbcCreateResource activity.
 *
 *
 */
@Slf4j
public class CreateFireThornDQPTestCase
extends OgsaResourceTestBase
    {
    public static final String RESOURCE_DRER_ID = "";
    public static final String RESOURCE_ATLAS_ID = "atlas";

    @Test
    public void test000()
    throws Exception
        {
        CreateOriginalDQPClient create = new CreateOriginalDQPClient();
        create.addConfiguration(
            "<DQPResourceConfig>" +
            "    <dataResources>" +
            "        <resource " +
            "            url=\"" + config().endpoint() + "\"" +
            "            dsis=\"dataSinks\"" +
            "            dsos=\"dataSources\"" +
            "            drerID=\"" + RESOURCE_DRER_ID + "\"" + 
            "            resourceID=\"" + RESOURCE_ATLAS_ID + "\"" +
            "            isLocal=\"true\"" +
            "            />" +
            "    </dataResources>" +
            "</DQPResourceConfig>"
            );

        final ResourceID created = create(create);
        log.debug("DQP ID [{}]", created.toString());

        }
    
    private ResourceID create(final CreateOriginalDQPClient create)
    throws Exception
        {
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        deliver.connectInput(create.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(create);
        pipeline.add(deliver);

        OgsaServiceClient service = new OgsaServiceClient(
            new URL(
                config().endpoint()
                )
            );

        RequestResource request = service.drer().execute(
            pipeline,
            RequestExecutionType.SYNCHRONOUS
            );

        assertEquals(
            "CreateDQPResource workflow - request status",
            RequestExecutionStatus.COMPLETED, 
            request.getRequestExecutionStatus()
            );
        assertTrue(
            "New resource ID found",
            create.hasNextResult()
            );

        return create.nextResult();
        }
    
    @Test
    public void test001()
    throws Exception
        {
        final CreateFireThornDQPWorkflow workflow = new CreateFireThornDQPWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final CreateResourceResult result = workflow.execute(
            "albert"
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Request [{}]", result.request());
        log.debug("Created [{}]", result.resource());

        assertNotNull(
            result
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        assertNotNull(
            result.request()
            );
        assertNotNull(
            result.resource()
            );
        }
    }
