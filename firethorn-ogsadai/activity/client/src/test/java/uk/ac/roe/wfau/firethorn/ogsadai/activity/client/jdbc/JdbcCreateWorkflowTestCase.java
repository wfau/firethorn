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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;

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
public class JdbcCreateWorkflowTestCase
    {

    @Value("${firethorn.ogsadai.endpoint}")
    private String endpoint ;

    @Value("${firethorn.atlas.url}")
    private String jdbcurl ;

    @Value("${firethorn.atlas.user}")
    private String username ;

    @Value("${firethorn.atlas.pass}")
    private String password ;

    @Value("${firethorn.atlas.driver}")
    private String driver;

    @Test
    public void test000()
    throws Exception
        {
        final JdbcCreateWorkflow workflow = new JdbcCreateWorkflow(
            new URL(
                endpoint
                )
            );

        final JdbcCreateWorkflow.Result created = workflow.execute(
            new JdbcCreateWorkflow.Param()
                {
                @Override
                public String jdbcurl()
                    {
                    return jdbcurl;
                    }
                @Override
                public String username()
                    {
                    return username;
                    }
                @Override
                public String password()
                    {
                    return password;
                    }
                @Override
                public String driver()
                    {
                    return driver;
                    }
                }
            );

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
