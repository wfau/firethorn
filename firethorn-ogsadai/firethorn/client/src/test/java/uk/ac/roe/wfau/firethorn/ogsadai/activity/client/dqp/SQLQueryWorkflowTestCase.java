package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;
/**
 *
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp.CreateFireThornDQPWorkflow;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateResourceWorkflow;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcResourceTestBase;

/**
 * Test for JdbcCreateResource activity.
 *
 *
 */
@Slf4j
public class SQLQueryWorkflowTestCase
extends JdbcResourceTestBase
    {
    public static class SQLQueryParam
    implements SQLQueryWorkflow.Param
        {
        public SQLQueryParam(final String source, final String query)
            {
            this.query  = query ;
            this.source = source ;
            }
        
        private String query;
        private String source;

        @Override
        public String source()
            {
            return source ;
            }
        @Override
        public String query()
            {
            return query;
            }
        }

    //
    // Create new JDBC resource for Atlas ..
    // Actually, we should call FireThorn to get this.
    //
    
    @Test
    public void test000()
    throws Exception
        {
        final JdbcCreateResourceWorkflow workflow = new JdbcCreateResourceWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult result = workflow.execute(
            config().jdbc().databases().get("atlas")
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Result  [{}]", result.result());

        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        assertNotNull(
            result.result()
            );
        }
/*
 * http://localhost:8080/firethorn/jdbc/table/10616833
 * ATLASDR1.dbo.atlasSource
 * JDBC_TABLE_11534337
 * ogsadai-a33b80bd-3a40-447b-80bd-31bb5d16d066
 * 
 */
    
    protected final SQLQueryParam DIRECT = new SQLQueryParam(
        "ogsadai-a33b80bd-3a40-447b-80bd-31bb5d16d066",
        "SELECT COUNT(ra) FROM atlassource WHERE ra BETWEEN 80.0 AND 80.1"
        );
    
    @Test
    public void test001()
    throws Exception
        {
        final SQLQueryWorkflow workflow = new SQLQueryWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final WorkflowResult result = workflow.execute(
            DIRECT
            );
        
        log.debug("Status  [{}]", result.status());

        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        }
    
    
    @Test
    public void test002()
    throws Exception
        {
        final CreateFireThornDQPWorkflow workflow = new CreateFireThornDQPWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final ResourceWorkflowResult result = workflow.execute(
            DIRECT.source()
            );

        log.debug("Status  [{}]", result.status());
        log.debug("Result  [{}]", result.result());

        assertEquals(
            WorkflowResult.Status.COMPLETED,            
            result.status()
            );
        assertNotNull(
            result.result()
            );
        }
    
    protected static final String TABLE_ALIAS = "JDBC_TABLE_11534337" ;
    protected static final String DQP_RESOURCEID = "ogsadai-8e545c64-f56b-4376-8bed-956c6412c3b3";

    protected final SQLQueryParam INDIRECT = new SQLQueryParam(
        DQP_RESOURCEID,
        "SELECT COUNT(ra) FROM " + TABLE_ALIAS + " WHERE ra BETWEEN 80.0 AND 80.1"
        );
    
    @Test
    public void test003()
    throws Exception
        {
        final SQLQueryWorkflow workflow = new SQLQueryWorkflow(
            new URL(
                config().endpoint()
                )
            );

        final WorkflowResult result = workflow.execute(
            INDIRECT
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
        assertNotNull(
            result.request()
            );
        }

    }
