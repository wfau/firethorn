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

        final String query = "SELECT TOP 10000 ra, dec FROM ATLASDR1.dbo.atlasSource"; 
        
        final IvoaCreateResourceWorkflow creator = new IvoaCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final CreateResourceResult wfaudata = creator.execute(
            new IvoaCreateResourceWorkflow.Param()
                {
                @Override
                public String endpoint()
                    {
                    return config().services().get("WFAU").endpoint();
                    }
                }
            );
        assertNotNull(
            wfaudata
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            wfaudata.status()
            );
        assertNotNull(
            wfaudata.resource()
            );
        
        final CreateResourceResult userdata = creator.execute(
            new IvoaCreateResourceWorkflow.Param()
                {
                @Override
                public String endpoint()
                    {
                    return null;
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
        
        final String name = buffer.toString();

        
        final IvoaSelectDataWorkflow selector = new IvoaSelectDataWorkflow(
            new URL(
                config().endpoint()
                )
            ); 
        final WorkflowResult selected = selector.execute(
            wfaudata.resource(),
            userdata.resource(),
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
