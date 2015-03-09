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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
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
    public static final String RESOURCE_ATLAS_ID = "ogsadai-e42abaff-24f9-4848-88db-6e00c12714e4";

    @Test
    public void test001()
    throws Exception
        {
        final CreateFireThornDQPWorkflow workflow = new CreateFireThornDQPWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult result = workflow.execute(
            RESOURCE_ATLAS_ID
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Request [{}]", result.request());
        log.debug("Created [{}]", result.result());

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
            result.result()
            );
        }
    }
