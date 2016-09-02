/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        final SimpleAttributeServiceImpl service = new SimpleAttributeServiceImpl(
            null
            );
        final Attribute attrib = service.getAttribute(
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
        final SimpleAttributeServiceImpl service = new SimpleAttributeServiceImpl(
            null
            );
        final Attribute attrib = service.getAttribute(
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
            8,
            attrib.getType()
            );
        assertFalse(
            attrib.isKey()
            );
        }

    //@Test
    public void test002()
        {
        final SimpleAttributeServiceImpl service = new SimpleAttributeServiceImpl(
            null
            );
        final Iterable<Attribute> iter = service.getAttributes(
            "JDBC_5"
            );
        for (final Attribute attrib : iter)
            {
            log.debug("Attribute ----");
            log.debug("  Name   [" + attrib.getName()   + "]");
            log.debug("  Source [" + attrib.getSource() + "]");
            log.debug("  Type   [" + attrib.getType()   + "]");
            log.debug("----");
            }
        }
    }
