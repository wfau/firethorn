/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
@Slf4j
public class AlbertTestCase
extends TestBase
    {

    public static Identifier ident ;

    @Test
    public void aaaa()
        {
        log.debug("aaaa ----");
        Widgeon object = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            object.name()
            );

        womble().hibernate().flush();
        ident = object.ident();

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            object.name()
            );

        log.debug("Created [{}][{}]", object.ident(), object.name());
        object.name("Albert");
        log.debug("Created [{}][{}]", object.ident(), object.name());

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );
        }

    @Test
    public void bbbb()
        {
        log.debug("bbbb ----");
        Widgeon object = womble().widgeons().select(
            ident
            );

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );

        log.debug("Selected [{}][{}]", object.ident(), object.name());
        object.name("Albert Augustus");
        log.debug("Selected [{}][{}]", object.ident(), object.name());

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus",
            object.name()
            );

       }

    @Test
    public void cccc()
        {
        log.debug("cccc ----");
        Widgeon object = womble().widgeons().select(
            ident
            );

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus",
            object.name()
            );

        log.debug("Selected [{}][{}]", object.ident(), object.name());
        object.name("Albert Augustus Charles Emmanuel");
        log.debug("Selected [{}][{}]", object.ident(), object.name());

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus Charles Emmanuel",
            object.name()
            );

       }
    }

