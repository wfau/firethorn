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

        object.name("Albert");

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

        object.name("Albert Augustus");

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

        object.name("Albert Augustus Charles Emmanuel");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus Charles Emmanuel",
            object.name()
            );

       }
    }

