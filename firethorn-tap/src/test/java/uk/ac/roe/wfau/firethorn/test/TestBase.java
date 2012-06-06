/*
 *
 */
package uk.ac.roe.wfau.firethorn.test ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import java.util.Iterator;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;  

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import org.springframework.transaction.annotation.Transactional;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

/**
 * Base class for tests.
 * The test is run using SpringJUnit4ClassRunner in order to support the @Autowired annotation.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(
    locations = {
        "classpath:database-config.xml",
        "classpath:hibernate-config.xml",
        "classpath:scheduler-config.xml",
        "classpath:component-config.xml"
        }
    )  
@Transactional
@TransactionConfiguration(
    transactionManager="FireThornTransactionManager",
    defaultRollback = false
    )
public abstract class TestBase
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        TestBase.class
        );

    /**
     * Our test Womble.
     *
     */
    @Autowired
    private Womble womble ;
    public Womble womble()
        {
        return this.womble;
        }

    @Before
    public void before()
        {
        logger.debug("before()");
        }

    @After
    public void after()
        {
        logger.debug("after()");
        this.flush();
        }

    public void flush()
        {
        logger.debug("flush()");
//        womble().hibernate().flush();
        }

    /**
     * The test class load time.
     *
     */
    protected static long start = System.currentTimeMillis() ;

    /**
     * A counter for unique names.
     *
     */
    protected static long uniques = 0 ;

    /**
     * Generate a unique string.
     *
     */
    public String unique()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(
            String.valueOf(
                start
                )
            );
        builder.append("-");
        builder.append(
            String.valueOf(
                uniques++
                )
            );
        return builder.toString();
        }

    /**
     * Generate a unique string.
     *
     */
    public String unique(String prefix)
        {
        StringBuilder builder = new StringBuilder();
        builder.append(
            prefix
            );
        builder.append("-");
        builder.append(
            unique()
            );
        return builder.toString();
        }

    /**
     * Generate a unique URI.
     *
     */
    public URI unique(URI base)
        {
        return base.resolve(
            URI.create(
                unique()
                )
            );
        }

    /**
     * Count the members in a Iterable set.
     *
     */
    public long count(Iterable iterable)
        {
        long count = 0 ;
        for (Object object : iterable)
            {
            count++ ;
            }
        return count ;
        }

    }

