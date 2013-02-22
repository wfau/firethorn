/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMapping;

/**
 *
 *
 */
public class TableMappingTestCase
    {

    @Test
    public void test000()
        {
        TableMappingServiceImpl service = new TableMappingServiceImpl(
            "http://data.metagrid.co.uk/wfau/firethorn/test",
            null
            ); 

        TableMapping bean = service.getTableMapping(
            "TEST_000.json"
            );
        assertNotNull(
            bean
            );
        assertEquals(
            "catalog.schema.table",
            bean.tableName()
            );
        assertEquals(
            "TEST_000",
            bean.tableAlias()
            );
        assertEquals(
            "RESOURCE_000",
            bean.resourceIdent()
            );
        }

    @Test
    public void test001()
        {
        TableMappingServiceImpl service = new TableMappingServiceImpl(
            "http://data.metagrid.co.uk/wfau/firethorn/test",
            null
            ); 

        TableMapping bean = service.getTableMapping(
            "JDBC_120.json"
            );
        assertNotNull(
            bean
            );
        assertEquals(
            "UKIDSSDR5PLUS.dbo.gcsPointSource",
            bean.tableName()
            );
        assertEquals(
            "JDBC_120",
            bean.tableAlias()
            );
        assertNull(
            bean.resourceIdent()
            );
        }

    }
