/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateParam;
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
        
        JdbcCreateWorkflow client = new JdbcCreateWorkflow(
            new URL(
                endpoint
                )
            );

        ResourceID created = client.execute(
            new JdbcCreateParam()
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
        }
    }
