/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;

/**
 * Test for creating Ivoa resources.
 *
 */
@Slf4j
public class IvoaCreateResourceTestCase
extends IvoaResourceTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        final IvoaCreateResourceWorkflow workflow = new IvoaCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final SimpleResourceWorkflowResult created = workflow.execute(
            new IvoaCreateResourceWorkflow.SimpleParam(
                config().ivoa().services().get("WFAU").endpoint()
                )
            );

        log.debug("Status  [{}]", created.status());
        log.debug("Request [{}]", created.request());
        log.debug("Created [{}]", created.result());

        assertNotNull(
            created
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            created.status()
            );
        assertNotNull(
            created.request()
            );
        assertNotNull(
            created.result()
            );
        }
    }
