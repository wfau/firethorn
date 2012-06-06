/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
public class WidgeonTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WidgeonTestCase.class
        );

    @Before
    public void before()
        {
        logger.debug("before()");
        super.before();
        }

    @After
    public void after()
        {
        logger.debug("after()");
        super.after();
        }

    @Test
    public void simple()
        {
        Widgeon one = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );
        logger.debug("One [{}][{}]", one.ident(), one.name());

        Widgeon two = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );
        logger.debug("Two [{}][{}]", two.ident(), two.name());

        //womble().hibernate().statefull().session().flush();

        for (Widgeon widgeon : womble().widgeons().select())
            {
            logger.debug("Widgeon [{}]", widgeon);
            }

        }

    @Test
    public void nested()
        {
        Widgeon one = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        one.schemas().create("one.left");
        one.schemas().create("one.right");

        Widgeon two = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        two.schemas().create("two.left");
        two.schemas().create("two.right");

        //womble().hibernate().statefull().session().flush();

        for (Widgeon widgeon : womble().widgeons().select())
            {
            logger.debug("-------");
            logger.debug("Widgeon [{}]", widgeon);
            for (Widgeon.Schema schema : widgeon.schemas().select())
                {
                logger.debug("  Schema [{}]", schema);
                }
            }
        }
    }

