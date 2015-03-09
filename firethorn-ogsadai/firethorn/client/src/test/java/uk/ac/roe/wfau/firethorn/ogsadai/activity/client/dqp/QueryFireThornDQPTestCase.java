/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp.CreateFireThornDQPClient.Param;

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
/*
        final JdbcCreateResourceWorkflow workflow = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult created = workflow.execute(
            config().jdbc().databases().get("atlas")
            );

        log.debug("Status  [{}]", created.status());
        log.debug("Request [{}]", created.request());
        log.debug("Created [{}]", created.result());

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
            created.result()
            );
 */
        
        }
    
    
    @Test
    public void test003()
    throws Exception
        {
        //final String query = "SELECT COUNT(*) FROM AtlasSource" ;
        //final String query = "SELECT COUNT(ra) FROM JDBC_TABLE_8323073" ;
        //final String query = "SELECT TOP 5 ra FROM JDBC_TABLE_8323073 " ;
        //final String query = "SELECT ra FROM JDBC_TABLE_8323073 LIMIT 5" ;
        final String query = "SELECT COUNT(ra) FROM JDBC_TABLE_8323073 WHERE ra BETWEEN 1.0 AND 2.0" ;
        
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
                public CreateFireThornDQPClient.Param create()
                    {
                    return new Param()
                        {
                        @Override
                        public String target()
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
