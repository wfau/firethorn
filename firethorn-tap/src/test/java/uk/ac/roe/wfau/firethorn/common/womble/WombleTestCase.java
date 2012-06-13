/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.womble ;

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
public class WombleTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WombleTestCase.class
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
        assertNotNull(
            womble()
            );

        assertNotNull(
            womble().hibernate()
            );

        assertNotNull(
            womble().hibernate().factory()
            );

        assertNotNull(
            womble().hibernate().session()
            );

        assertNotNull(
            womble().widgeons()
            );

        assertNotNull(
            womble().mallards()
            );
        }
    }

