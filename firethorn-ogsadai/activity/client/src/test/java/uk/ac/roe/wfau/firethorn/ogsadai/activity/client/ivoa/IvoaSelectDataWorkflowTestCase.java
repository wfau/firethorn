/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Random;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.CreateResourceResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateResourceWorkflow;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateTableClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * Test for querying Ivoa resources.
 *
 */
public class IvoaSelectDataWorkflowTestCase
extends IvoaResourceTestBase
    {
    private Random random = new Random(); 
    
    @Test
    public void test000()
    throws Exception
        {
        final TapService source = config().ivoa().services().get("VIZIER");
        final IvoaCreateResourceWorkflow ivoacreator = new IvoaCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );
        final CreateResourceResult ivoadata = ivoacreator.execute(
            new IvoaCreateResourceWorkflow.SimpleParam(
                source.endpoint()
                )
            );
        assertNotNull(
            ivoadata
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            ivoadata.status()
            );
        assertNotNull(
            ivoadata.resource()
            );

        final JdbcCreateResourceWorkflow jdbccreator = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );
        final CreateResourceResult userdata = jdbccreator.execute(
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
        
        final String tablename = buffer.toString();
        
        final IvoaSelectDataWorkflow selector = new IvoaSelectDataWorkflow(
            new URL(
                config().endpoint()
                )
            ); 
        final WorkflowResult selected = selector.execute(
            ivoadata.resource(),
            new IvoaSelectDataWorkflow.Param()
                {
                @Override
                public LimitsClient.Param limits()
                    {
                    return new LimitsClient.Param()
                        {
                        @Override
                        public Long rows()
                            {
                            return new Long(10);
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
                public IvoaSelectDataClient.Param select()
                    {
                    return new IvoaSelectDataClient.Param()
                        {
                        @Override
                        public String query()
                            {
                            return source.example();
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
                            return tablename;
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
                            return tablename ;
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
