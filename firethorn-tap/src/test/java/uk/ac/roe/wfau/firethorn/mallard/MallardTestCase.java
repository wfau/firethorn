/*
 *
 */
package uk.ac.roe.wfau.firethorn.mallard ;

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

        womble().hibernate().statefull().session().flush();

        }

    @Test
    @Transactional(readOnly=false)
    public void shared()
        {
        Mallard albert = womble().mallards().create(
            "albert"
            );

logger.debug("FLUSH -------------");
womble().hibernate().statefull().session().flush();
logger.debug("------------- DONE");

        Widgeon one = womble().widgeons().create(
            "0001",
            URI.create("ivo://org.astrogrid.test/0001")
            );

//logger.debug("FLUSH -------------");
//womble().hibernate().statefull().session().flush();
//logger.debug("------------- DONE");

logger.debug("SELECT -------------");
        Mallard victoria = womble().mallards().select(
            albert.ident()
            );
logger.debug("------------- DONE");

logger.debug("SELECT -------------");
        Widgeon two = womble().widgeons().select(
            one.ident()
            );

logger.debug("------------- DONE");


logger.debug("ADD -------------");
        victoria.widgeons().insert(
            two
            );
logger.debug("------------- DONE");

//logger.debug("FLUSH -------------");
//        womble().hibernate().statefull().session().flush();
//logger.debug("------------- DONE");

/*
logger.debug("ADD -------------");
        albert.widgeons().insert(
            two
            );
logger.debug("------------- DONE");

logger.debug("FLUSH -------------");
        womble().hibernate().statefull().session().flush();
logger.debug("------------- DONE");

*/
/*

logger.debug("FLUSH -------------");
        womble().hibernate().statefull().session().flush();
logger.debug("------------- DONE");

logger.debug("COPY -------------");
        for (Widgeon widgeon : albert.widgeons().select())
            {
            victoria.widgeons().insert(
                widgeon
                );
            }
logger.debug("------------- DONE");

logger.debug("FLUSH -------------");
        womble().hibernate().statefull().session().flush();
logger.debug("------------- DONE");

logger.debug("ADD -------------");
        albert.widgeons().insert(
            womble().widgeons().create(
                "0003",
                URI.create("ivo://org.astrogrid.test/0003")
                )
            );
logger.debug("------------- DONE");

logger.debug("FLUSH -------------");
        womble().hibernate().statefull().session().flush();
logger.debug("------------- DONE");

logger.debug("ADD -------------");
        victoria.widgeons().insert(
            womble().widgeons().create(
                "0004",
                URI.create("ivo://org.astrogrid.test/0004")
                )
            );
logger.debug("------------- DONE");

logger.debug("LIST -------------");
        for (Mallard mallard : womble().mallards().select())
            {
            logger.debug("Mallard [{}][{}]", mallard.ident(), mallard.name());

            for (Widgeon widgeon : mallard.widgeons().select())
                {
                logger.debug("Widgeon [{}][{}]", widgeon.ident(), widgeon.name());
                }
            }
logger.debug("------------- DONE");
*/

        }
    }

