package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;
/**
 *
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
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

    protected static final String ATLAS_RESOURCEID  = "ogsadai-0fa43253-938d-4072-9b28-06c6de74b990";
    protected static final String ATLAS_TABLE_ALIAS = "JDBC_TABLE_11534337" ;
    protected static final String DQP_RESOURCEID    = "ogsadai-8f1f002d-64e8-4e15-ba05-e79eb36dcee0";
    
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

    /**
     * Create new JDBC resource for Atlas.
     * Replace with a call to FireThorn.
     *
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
     */
    
    protected final SQLQueryParam DIRECT = new SQLQueryParam(
        ATLAS_RESOURCEID,
        "SELECT COUNT(ra) FROM atlassource WHERE ra BETWEEN 80.0 AND 80.1"
        );
    
    /**
     * DIRECT query to Atlas Source table.
     *
     */
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
    
    /**
     * Create a new empty DQP.
     * 
     */
    @Test
    public void test002()
    throws Exception
        {
        final CreateDQPWorkflow workflow = new CreateDQPWorkflow(
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

    protected final SQLQueryParam INDIRECT = new SQLQueryParam(
        DQP_RESOURCEID,
        "SELECT COUNT(ra) FROM " + ATLAS_TABLE_ALIAS + " WHERE ra BETWEEN 80.0 AND 80.1"
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
