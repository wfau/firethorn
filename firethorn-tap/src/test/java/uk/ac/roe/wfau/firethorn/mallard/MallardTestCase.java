/*
 *
 */
package uk.ac.roe.wfau.firethorn.mallard ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class MallardTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        MallardTestCase.class
        );


    private static Identifier one ;
    private static Identifier two ;

    @Test
    public void test000()
        {
        Mallard object = womble().mallards().create(
            "albert"
            );
        one = object.ident();

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            object.name()
            );
        }

    @Test
    public void test001()
        {
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().select(
            one
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
        Mallard object = womble().mallards().create(
            "albert"
            );
        two = object.ident();
        }

    @Test
    public void test008()
        {
        Mallard frog = womble().mallards().select(
            one
            );
        Mallard toad = womble().mallards().select(
            two
            );

        for (Widgeon widgeon : frog.widgeons().select())
            {
            toad.widgeons().insert(
                widgeon
                );
            }
        }

    @Test
    public void test009()
        {
        Mallard object = womble().mallards().select(
            one
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
    public void test010()
        {
        Mallard object = womble().mallards().select(
            one
            );
        assertEquals(
            3,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test011()
        {
        Mallard object = womble().mallards().select(
            two
            );

        assertEquals(
            2,
            count(
                object.widgeons().select()
                )
            );
        }
    }

