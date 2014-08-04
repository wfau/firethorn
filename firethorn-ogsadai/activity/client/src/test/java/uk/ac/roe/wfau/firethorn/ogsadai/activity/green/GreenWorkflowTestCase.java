/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.green;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleQueryTestBase;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.DelaysClient.Param;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Simple test for OGSA-DAI queries.
 *
 *
 */
@Slf4j
@RunWith(
    SpringJUnit4ClassRunner.class
    )
@ContextConfiguration(
    locations = {
        "classpath:component-config.xml",
        }
    )
public class GreenWorkflowTestCase
    {
    @Value("${firethorn.ogsadai.dqp}")
    protected String dqp ;

    @Value("${firethorn.ogsadai.store}")
    protected String store ;

    @Value("${firethorn.ogsadai.endpoint}")
    protected String endpoint ;

    @Test
    public void test000()
    throws Exception
        {
        GreenWorkflowClient client = new GreenWorkflowClient(
            new URL(
                endpoint
                )
            );

        ResourceID resource = client.create();
        client.query(
            resource
            );
        }
    
    }
