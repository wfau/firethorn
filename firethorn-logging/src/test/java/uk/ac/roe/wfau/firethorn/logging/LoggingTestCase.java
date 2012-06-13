/*
 *
 */
package uk.ac.roe.wfau.firethorn.logging ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

/**
 *
 */
//@Slf4j
@Logged
public class LoggingTestCase
    {

    @Test
    public void test()
        {
        log.debug("This should be logged");
        }

    }

