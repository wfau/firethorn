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
    public void aaaa()
        {
        Mallard object = womble().mallards().create(
            "albert"
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        assertNotNull(
            object
            );
        assertNotNull(
            object.ident()
            );
        assertEquals(
            "albert",
            object.name()
            );

        one = object.ident();

        }

    @Test
    public void bbbb()
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
    public void cccc()
        {
        Mallard object = womble().mallards().select(
            one
            );
        assertEquals(
            "Albert",
            object.name()
            );
        }

    @Test
    public void dddd()
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
    public void eeee()
        {
        Mallard object = womble().mallards().select(
            one
            );

        int count = 0 ;
        for (Widgeon widgeon : object.widgeons().select())
            {
            count++ ;
            }
        assertEquals(
            count,
            1
            );

        }

    @Test
    public void ffff()
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
    public void gggg()
        {
        Mallard object = womble().mallards().select(
            one
            );

        int count = 0 ;
        for (Widgeon widgeon : object.widgeons().select())
            {
            count++ ;
            }
        assertEquals(
            count,
            2
            );

        }

    @Test
    public void hhhh()
        {
        Mallard object = womble().mallards().create(
            "albert"
            );
        two = object.ident();
        }

    @Test
    public void iiii()
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
    public void jjjj()
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
    public void kkkk()
        {
        Mallard object = womble().mallards().select(
            one
            );

        int count = 0 ;
        for (Widgeon widgeon : object.widgeons().select())
            {
            count++ ;
            }
        assertEquals(
            count,
            3
            );

        }

    @Test
    public void llll()
        {
        Mallard object = womble().mallards().select(
            two
            );

        int count = 0 ;
        for (Widgeon widgeon : object.widgeons().select())
            {
            count++ ;
            }
        assertEquals(
            count,
            2
            );

        }

/*
    //@Test
    @CreateAtomicMethod
    public void bbbb()
        {
logger.debug("CREATE -------------");
        Mallard created = womble().mallards().create(
            "albert"
            );
logger.debug("Created [{}][{}][{}]", new Object[]{ created.getClass().getName(), created.ident(), created.name()});
logger.debug("------------- DONE");

logger.debug("FLUSH -------------");
womble().hibernate().flush();
logger.debug("------------- DONE");

logger.debug("SELECT -------------");
        Mallard selected = womble().mallards().select(
            created.ident()
            );
logger.debug("Selected [{}][{}][{}]", new Object[]{ selected.getClass().getName(), selected.ident(), selected.name()});
logger.debug("------------- DONE");

        selected.name("Victoria");
        selected.update();

logger.debug("FLUSH -------------");
womble().hibernate().flush();
logger.debug("------------- DONE");

        selected.name("Alexandrina Victoria");
        selected.update();

logger.debug("FLUSH -------------");
womble().hibernate().flush();
logger.debug("------------- DONE");

        }


    //Test
    @CreateAtomicMethod
    public void eeee()
        {
logger.debug("CREATE -------------");
        Mallard created = womble().mallards().create(
            "albert"
            );
logger.debug("Created [{}][{}][{}]", new Object[]{ created.getClass().getName(), created.ident(), created.name()});
logger.debug("------------- DONE");


logger.debug("CHANGE -------------");
        created.name("Victoria");
logger.debug("------------- DONE");

logger.debug("FLUSH -------------");
womble().hibernate().flush();
logger.debug("------------- DONE");

        }


//    @Test
    public void cccc()
        {
        Mallard found = womble().mallards().select(
            new LongIdent(1)
            );

        if (null != found)
            {
            logger.debug("Found [{}][{}][{}]", new Object[]{ found.getClass().getName(), found.ident(), found.name()});
            }
        else {
            logger.debug("Not found");
            }
        }

    //@Test
    public void simple()
        {
        Mallard one = womble().mallards().create(
            "albert"
            );
        one.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        one.widgeons().insert(
            womble().widgeons().create(
                "0002",
                URI.create("ivo://org.astrogrid.test/0002")
                )
            );

        for (Widgeon widgeon : one.widgeons().select())
            {
            logger.debug("Widgeon [{}][{}]", widgeon.ident(), widgeon.name());
            }

        womble().hibernate().flush();

        }
 */

    }

