/*
 *
 */
package uk.ac.roe.wfau.firethorn.mallard ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;

/**
 *
 */
@Slf4j
public class MallardTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    @Test
    public void test000()
        {
        ident[0] = womble().mallards().create(
            "albert"
            ).ident();
        ident[1] = womble().mallards().create(
            "albert"
            ).ident();
        }

    @Test
    public void test001()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );

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
    public void test002()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );
        }

    @Test
    public void test003()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );
        }


    @Test
    public void test004()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            1,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test005()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );
        }

    @Test
    public void test006()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            2,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test007()
        {
        assertNotNull(
            ident[0]
            );
        assertNotNull(
            ident[1]
            );
        Mallard frog = womble().mallards().select(
            ident[0]
            );
        Mallard toad = womble().mallards().select(
            ident[1]
            );
        for (Widgeon widgeon : frog.widgeons().select())
            {
            toad.widgeons().insert(
                widgeon
                );
            }
        }

    @Test
    public void test008()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        }

    @Test
    public void test009()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            3,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test010()
        {
        assertNotNull(
            ident[1]
            );
        Mallard object = womble().mallards().select(
            ident[1]
            );
        assertEquals(
            2,
            count(
                object.widgeons().select()
                )
            );
        }
    }

