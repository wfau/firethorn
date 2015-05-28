/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.test ;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * Base class for tests.
 * Using SpringJUnit4ClassRunner to support @Autowired annotations.
 * This class does not handle transactions.
 * 
    loader = AnnotationConfigContextLoader.class,
 * 
 *
 */
@Slf4j
@Ignore
@RunWith(
    SpringJUnit4ClassRunner.class
    )
@ContextConfiguration(
    locations = {
        "classpath:database-config.xml",
        "classpath:hibernate-config.xml",
        "classpath:component-config.xml",
        "classpath:scheduler-config.xml"
        }
    )
public abstract class TestRoot
    {

    /**
     * Our factories instance.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Our factories instance.
     *
     */
    public ComponentFactories factories()
        {
        return this.factories;
        }

    public void flush()
        {
        log.debug("flush()");
        factories().hibernate().flush();
        }

    /**
     * The test class load time.
     *
     */
    protected static long start = System.currentTimeMillis() ;

    /**
     * A shared counter for unique names.
     *
     */
    protected static long uniques = 0 ;

    /**
     * Glue for generated names.
     *
     */
    public static final String UNIQUE_NAME_GLUE = "." ;

    /**
     * Generate a unique string.
     *
     */
    public String unique()
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            String.valueOf(
                start
                )
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
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
    public String unique(final String prefix)
        {
        final StringBuilder builder = new StringBuilder();
        builder.append(
            prefix
            );
        builder.append(
            UNIQUE_NAME_GLUE
            );
        builder.append(
            unique()
            );
        return builder.toString();
        }

    /**
     * Generate a unique URI.
     *
     */
    public URI unique(final URI base)
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
    public long count(final Iterable<?> iterable)
        {
        long count = 0 ;
        for (@SuppressWarnings("unused") final Object object : iterable)
            {
            count++ ;
            }
        return count ;
        }

    /**
     * Test configuration.
     *
    @Configuration
    @ComponentScan("uk.ac.roe.wfau.firethorn")
    @PropertySource("file:${user.home}/firethorn.properties")
    public static class TestConfig
        {
        @Autowired
        private Environment environment ;
        public String property(final String name)
            {
            return environment.getProperty(
                name
                );
            }
        }
     */

    /**
     * Test configuration.
     *
     */
    public static interface TestConfig
        {
        /**
         * Read a test configuration property.
         * @param key The property key.
         * @return The property value.
         *
         */
        public String property(final String key);

        /**
         * Read a test configuration property.
         * @param key The property key.
         * @param value The default value.
         * @return The property value.
         *
         */
        public String property(final String key, final String value);

        }
    
    /**
     * Local properties file.
     *
     */
    private final Properties props = new Properties();

    /**
     * Local properties file.
     *
     */
    public static final String CONFIG_PATH = "user.home" ;

    /**
     * Local properties file.
     *
     */
    public static final String CONFIG_FILE = "firethorn.properties" ;

    @Before
    public void before()
    throws Exception
        {
        log.debug("Before ----");
        this.props.load(
            new FileInputStream(
                new File(
                    System.getProperty(
                        CONFIG_PATH
                        ),
                    CONFIG_FILE
                    )
                )
            );
        }

    @After
    public void after()
    throws Exception
        {
        log.debug("After ----");
        }

    /**
     * Test configuration.
     *
     */
    public TestConfig config()
        {
        return new TestConfig()
            {
            @Override
            public String property(final String key)
                {
                return props.getProperty(
                    key
                    );
                }

            @Override
            public String property(final String key, final String value)
                {
                return props.getProperty(
                    key,
                    value
                    );
                }
            };
        }
    }

