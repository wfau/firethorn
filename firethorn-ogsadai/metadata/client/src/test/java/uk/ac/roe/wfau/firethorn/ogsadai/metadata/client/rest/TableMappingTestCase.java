/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.rest;

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
        SimpleTableMappingServiceImpl service = new SimpleTableMappingServiceImpl(
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
        SimpleTableMappingServiceImpl service = new SimpleTableMappingServiceImpl(
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
        assertEquals(
            "ukidss",
            bean.resourceIdent()
            );
        }

    //@Test
    public void test002()
        {
        SimpleTableMappingServiceImpl service = new SimpleTableMappingServiceImpl(
            "http://localhost:8080/firethorn",
            null
            ); 
        TableMapping bean = service.getTableMapping(
            "JDBC_5"
            );
        assertNotNull(
            bean
            );
        assertEquals(
            "TWOMASS.dbo.twomass_psc",
            bean.tableName()
            );
        assertEquals(
            "JDBC_5",
            bean.tableAlias()
            );
        assertEquals(
            "twomass",
            bean.resourceIdent()
            );
        }
    }
