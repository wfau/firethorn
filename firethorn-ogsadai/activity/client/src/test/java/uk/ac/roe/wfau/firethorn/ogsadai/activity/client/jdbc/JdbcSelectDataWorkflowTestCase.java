/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * Simple test for OGSA-DAI queries.
 *
 *
 */
@Slf4j
public class JdbcSelectDataWorkflowTestCase
extends JdbcResourceTestBase
    {
    private Random random = new Random(); 
    
    @Test
    public void test000()
    throws Exception
        {

        final String query = "SELECT TOP 100 ra, dec FROM ATLASDR1.dbo.atlasSource"; 
        
        final JdbcCreateResourceWorkflow creator = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final CreateResourceResult atlasdata = creator.execute(
            config().jdbc().databases().get("atlas")
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
        
        final CreateResourceResult userdata = creator.execute(
            config().jdbc().databases().get("user")
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
        int  rand = random.nextInt();  

        /*
        final Formatter formatter = new Formatter(
            new StringBuilder()
            );
        formatter.format("UD_%016X_%016X", time, rand);
         */
        
        
        final StringBuffer buffer = new StringBuffer(); 
        final BaseEncoding encoding = BaseEncoding.base32().omitPadding().upperCase();

        buffer.append("UD_");
        buffer.append(
            encoding.encode(
                Longs.toByteArray(
                    time
                    )
                )
            );
        buffer.append(
            encoding.encode(
                Ints.toByteArray(
                    rand
                    )
                )
            );
        
        //final String name = formatter.out().toString();
        final String name = buffer.toString();

        
        final JdbcSelectDataWorkflow selector = new JdbcSelectDataWorkflow(
            new URL(
                config().endpoint()
                )
            ); 
        final WorkflowResult selected = selector.execute(
            atlasdata.resource(),
            new JdbcSelectDataWorkflow.Param()
                {
                @Override
                public LimitsClient.Param limits()
                    {
                    return new LimitsClient.Param()
                        {
                        @Override
                        public Long rows()
                            {
                            return new Long(1000);
                            }

                        @Override
                        public Long cells()
                            {
                            return null;
                            }

                        @Override
                        public Long time()
                            {
                            return null;
                            }
                        };
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
                        public String store()
                            {
                            return userdata.resource().toString();
                            }
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
                        public String store()
                            {
                            return userdata.resource().toString();
                            }
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