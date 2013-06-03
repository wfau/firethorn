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
package uk.ac.roe.wfau.firethorn.tuesday.test.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 * TODO experiment with this
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
@Ignore
public class JdbcResourceTestThing
extends TestBase
    {

    /**
     * Our ComponentFactories instance.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Access to our ComponentFactories singleton instance.
     *
     */
    @Override
    public ComponentFactories factories()
        {
        return this.factories;
        }

    /**
     * Local properties file.
     *
     */
    private final Properties config = new Properties();

    /**
     * Local properties file.
     *
     */
    public Properties config()
        {
        return this.config;
        }

    public static final String CONFIG_PATH = "user.home" ;
    public static final String CONFIG_FILE = "firethorn.properties" ;

    @Before
    @Override
    public void before()
    throws Exception
        {
        log.debug("Before ----");
        this.config.load(
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
    @Override
    public void after()
        {
        log.debug("After ----");
        }

    public void display(final JdbcResource resource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", resource.name());

        for (final JdbcSchema schema : resource.schemas().select())
            {
            log.debug("--- Schema [{}][{}]", new Object[] {resource.name(), schema.name()});
            for (final JdbcTable table : schema.tables().select())
                {
                log.debug("---- Table [{}][{}.{}]", new Object[] {resource.name(), schema.name(), table.name()});
                for (final JdbcColumn column : table.columns().select())
                    {
                    log.debug("----- Column [{}][{}.{}.{}]", new Object[] {resource.name(), schema.name(), table.name(), column.name()});
                    }
                }
            }
        }
    }
