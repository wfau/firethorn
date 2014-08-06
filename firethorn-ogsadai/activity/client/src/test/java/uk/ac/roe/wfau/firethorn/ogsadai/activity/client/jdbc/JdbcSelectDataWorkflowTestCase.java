/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Formatter;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedLongs;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient.Param;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateResourceWorkflow;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcSelectDataWorkflow;
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
public class JdbcSelectDataWorkflowTestCase
    {
    private Random random = new Random(); 
    
    
    @Value("${firethorn.ogsadai.endpoint}")
    private String endpoint ;

    @Value("${firethorn.atlas.url}")
    private String atlasurl ;

    @Value("${firethorn.atlas.user}")
    private String atlasuser ;

    @Value("${firethorn.atlas.pass}")
    private String atlaspass ;

    @Value("${firethorn.atlas.driver}")
    private String atlasdriver;

    
    
    @Value("${firethorn.user.url}")
    private String userurl ;

    @Value("${firethorn.user.user}")
    private String useruser ;

    @Value("${firethorn.user.pass}")
    private String userpass ;

    @Value("${firethorn.user.driver}")
    private String userdriver;

    
    
    @Test
    public void test000()
    throws Exception
        {
        final String query = "SELECT TOP 5000 ra, dec FROM ATLASDR1.dbo.atlasSource"; 
        
        final JdbcCreateResourceWorkflow creator = new JdbcCreateResourceWorkflow(
            new URL(
                endpoint
                )
            );

        final JdbcCreateResourceWorkflow.Result atlasdata = creator.execute(
            new JdbcCreateResourceWorkflow.Param()
                {
                @Override
                public String jdbcurl()
                    {
                    return atlasurl;
                    }
                @Override
                public String username()
                    {
                    return atlasuser;
                    }
                @Override
                public String password()
                    {
                    return atlaspass;
                    }
                @Override
                public String driver()
                    {
                    return atlasdriver;
                    }
                @Override
                public boolean writable()
                    {
                    return false;
                    }
                }
            );
        assertNotNull(
            atlasdata
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            atlasdata.status()
            );
        assertNotNull(
            atlasdata.resource()
            );
        
        final JdbcCreateResourceWorkflow.Result userdata = creator.execute(
            new JdbcCreateResourceWorkflow.Param()
                {
                @Override
                public String jdbcurl()
                    {
                    return userurl;
                    }
                @Override
                public String username()
                    {
                    return useruser;
                    }
                @Override
                public String password()
                    {
                    return userpass;
                    }
                @Override
                public String driver()
                    {
                    return userdriver;
                    }
                @Override
                public boolean writable()
                    {
                    return false;
                    }
                }
            );
        assertNotNull(
            userdata
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            userdata.status()
            );
        assertNotNull(
            userdata.resource()
            );

        long time = System.currentTimeMillis();         
        long rand = random.nextLong();  

        final Formatter formatter = new Formatter(
            new StringBuilder()
            );
        formatter.format("UD_%016X_%016X", time, rand);
        

        final StringBuffer buffer = new StringBuffer(); 
        final BaseEncoding encoding = BaseEncoding.base32().omitPadding();

        buffer.append("UD_");
        buffer.append(
            encoding.encode(
                Longs.toByteArray(
                    time
                    )
                )
            );
        buffer.append("_");
        buffer.append(
            encoding.encode(
                Longs.toByteArray(
                    rand
                    )
                )
            );
        
        //final String name = formatter.out().toString();
        final String name = buffer.toString();

        
        final JdbcSelectDataWorkflow selector = new JdbcSelectDataWorkflow(
            new URL(
                endpoint
                )
            ); 
        final WorkflowResult selected = selector.execute(
            atlasdata.resource(),
            userdata.resource(),
            new JdbcSelectDataWorkflow.Param()
                {
                
                @Override
                public LimitsClient.Param limits()
                    {
                    return null;
                    }

                @Override
                public JdbcSelectDataClient.Param select()
                    {
                    return new JdbcSelectDataClient.Param()
                        {
                        @Override
                        public String query()
                            {
                            return query;
                            }
                        };
                    }

                @Override
                public JdbcCreateTableClient.Param create()
                    {
                    return new JdbcCreateTableClient.Param()
                        {
                        @Override
                        public String table()
                            {
                            return name;
                            }
                        };
                    }
                
                @Override
                public JdbcInsertDataClient.Param insert()
                    {
                    return new JdbcInsertDataClient.Param()
                        {
                        @Override
                        public String table()
                            {
                            return name ;
                            }
                        @Override
                        public Integer first()
                            {
                            return new Integer(1000);
                            }
                        @Override
                        public Integer block()
                            {
                            return new Integer(1000);
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
