/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;

/**
 * Test for JdbcCreateResource activity.
 *
 *
 */
@Slf4j
public class JdbcCreateResourceTestCase
extends JdbcResourceTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        final JdbcCreateResourceWorkflow workflow = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final CreateResourceResult created = workflow.execute(
            config().jdbc().databases().get("atlas")
            );

        log.debug("Status  [{}]", created.status());
        log.debug("Request [{}]", created.request());
        log.debug("Created [{}]", created.resource());

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
            created.resource()
            );
        }
    }
