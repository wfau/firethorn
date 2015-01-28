/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp.CreateFireThornDQPClient.Param;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 * Test for JdbcCreateResource activity.
 *
 *
 */
@Slf4j
public class QueryFireThornDQPTestCase
extends OgsaResourceTestBase
    {
    
    @Test
    public void test001()
    throws Exception
        {
        final CreateFireThornDQPWorkflow workflow = new CreateFireThornDQPWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult result = workflow.execute(
            "albert"
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Request [{}]", result.request());
        log.debug("Created [{}]", result.result());

        assertNotNull(
            result
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        assertNotNull(
            result.request()
            );
        assertNotNull(
            result.result()
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        //final String query = "SELECT COUNT(*) FROM AtlasSource" ;
        final String query = "SELECT COUNT(ra) FROM JDBC_TABLE_8323073" ;
        
        final QueryFireThornDQPWorkflow workflow = new QueryFireThornDQPWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final WorkflowResult result = workflow.execute(
            new QueryFireThornDQPWorkflow.Param()
                {
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
                public JdbcInsertDataClient.Param insert()
                    {
                    return new JdbcInsertDataClient.Param()
                        {
                        @Override
                        public String ogsaid()
                            {
                            return "userdata";
                            }

                        @Override
                        public String table()
                            {
                            return "tableone";
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
                
                @Override
                public CreateFireThornDQPClient.Param create()
                    {
                    return new Param()
                        {
                        @Override
                        public String ident()
                            {
                            return "no-ident";
                            }
                        };
                    }
                }
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Request [{}]", result.request());

        assertNotNull(
            result
            );
        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        }
    }
