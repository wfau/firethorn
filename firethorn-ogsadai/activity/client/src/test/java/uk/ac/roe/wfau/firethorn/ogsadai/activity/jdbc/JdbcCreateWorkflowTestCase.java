/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.jdbc;

import static org.junit.Assert.assertNotNull;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class JdbcCreateWorkflowTestCase
    {

    @Value("${firethorn.ogsadai.endpoint}")
    protected String endpoint ;

    @Value("${firethorn.atlas.url}")
    protected String url ;

    @Value("${firethorn.atlas.user}")
    protected String username ;

    @Value("${firethorn.atlas.pass}")
    protected String password ;

    @Value("${firethorn.atlas.driver}")
    protected String driver ;
    
    @Test
    public void test000()
    throws Exception
        {
        JdbcCreateWorkflow workflow = new JdbcCreateWorkflow(
            endpoint
            );
        ResourceID resource = workflow.execute(
            new JdbcCreateParam()
                {
                @Override
                public String url()
                    {
                    return url;
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
            resource
            );
        }
    }
