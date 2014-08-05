/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.query;

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
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient.Param;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.InsertClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateWorkflow;
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
public class QueryWorkflowTestCase
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

    @Value("${firethorn.ogsadai.store}")
    private String store;
    
    
    @Test
    public void test000()
    throws Exception
        {
        final String query = "SELECT TOP 10 ra, dec FROM atlasSource"; 
        
        final JdbcCreateWorkflow creator = new JdbcCreateWorkflow(
            new URL(
                endpoint
                )
            );
        final JdbcCreateWorkflow.Result created = creator.execute(
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
            created.resource()
            );

        final QueryWorkflow selector = new QueryWorkflow(
            new URL(
                endpoint
                )
            ); 
        final WorkflowResult selected = selector.execute(
            new QueryWorkflow.Param()
                {
                @Override
                public ResourceID resource()
                    {
                    return created.resource();
                    }
                
                @Override
                public String query()
                    {
                    return query;
                    }
                
                @Override
                public LimitsClient.Param limits()
                    {
                    return null;
                    }
                
                @Override
                public InsertClient.Param insert()
                    {
                    return new InsertClient.Param()
                        {
                        @Override
                        public String store()
                            {
                            return store;
                            }

                        @Override
                        public String table()
                            {
                            return "fred";
                            }

                        @Override
                        public Integer first()
                            {
                            return null;
                            }

                        @Override
                        public Integer block()
                            {
                            return null;
                            }
                        };
                    }
                
                @Override
                public DelaysClient.Param delays()
                    {
                    return null;
                    }
                }
            );

        assertNotNull(
            selected
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            selected.status()
            );
        }
    }
