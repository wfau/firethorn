/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 *
 *
 */
public class AttributeServiceTestCase
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(AttributeServiceTestCase.class);

    @Test
    public void test000()
        {
        AttributeServiceImpl service = new AttributeServiceImpl(
            "http://data.metagrid.co.uk/wfau/firethorn/test/",
            null
            ); 
        Attribute attrib = service.getAttribute(
            "table-000",
            "column-000.json"
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

    @Test
    public void test002()
        {
        AttributeServiceImpl service = new AttributeServiceImpl(
            "http://localhost:8080/firethorn/",
            null
            ); 
        Iterable<Attribute> iter = service.getAttributes(
            "JDBC_5"
            );
        for (Attribute attrib : iter)
            {
            log.debug("Attribute ----");
            log.debug("  Name   [" + attrib.getName()   + "]");
            log.debug("  Source [" + attrib.getSource() + "]");
            log.debug("  Type   [" + attrib.getType()   + "]");
            log.debug("----");
            }
        }
    }
