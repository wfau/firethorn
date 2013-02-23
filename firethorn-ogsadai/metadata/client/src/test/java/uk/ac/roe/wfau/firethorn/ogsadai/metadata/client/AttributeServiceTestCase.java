/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 *
 *
 */
public class AttributeServiceTestCase
    {

    @Test
    public void test000()
        {
        AttributeServiceImpl service = new AttributeServiceImpl(
            "http://data.metagrid.co.uk/wfau/firethorn/test",
            null
            ); 
        Attribute attrib = service.getAttribute(
            "table-000",
            "attrib-000.json"
            );
        assertNotNull(
            attrib
            );
        assertEquals(
            "catalog.schema.table",
            attrib.getSource()
            );
        assertEquals(
            "attrib-name",
            attrib.getName()
            );
        }

    @Test
    public void test001()
        {
        AttributeServiceImpl service = new AttributeServiceImpl(
            "http://localhost:8080/firethorn/",
            null
            ); 
        Attribute attrib = service.getAttribute(
            "JDBC_5",
            "ra"
            );
        assertNotNull(
            attrib
            );
        assertEquals(
            "JDBC_5",
            attrib.getSource()
            );
        assertEquals(
            "ra",
            attrib.getName()
            );
        assertEquals(
            0,
            attrib.getType()
            );
        assertFalse(
            attrib.isKey()
            );
        }

    }
