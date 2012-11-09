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
package uk.ac.roe.wfau.firethorn.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
public class RemoteJdbcTestCase
extends TestBase
    {
    @Autowired
    ApplicationContext spring ;

    //
    // Local properties file.
    Properties config = new Properties();

    public static final String CONFIG_PATH = "user.home" ;
    public static final String CONFIG_FILE = "firethorn.properties" ;

    @Before
    public void before()
    throws Exception
        {
        config.load(
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

    @Test
    public void testPull()
    throws Exception
        {

        //
        // Connect to the test database.
        final DataSource source = (DataSource) spring.getBean("RoeTwoMass");

        //
        // Create an empty resource tree.
        final JdbcResource jdbcResource = womble().jdbc().resources().create(
            this.unique(
                "base"
                ),
            source
            );

        //
        // Connect to the database.
        jdbcResource.connect(
            config.getProperty("uk.ac.roe.wfau.firethorn.jdbc.test.user"),
            config.getProperty("uk.ac.roe.wfau.firethorn.jdbc.test.pass")
            );

        //
        // Pull the metadata from the database
        jdbcResource.diff(
            false,
            true
            );
        //
        // Verify we got what we expected.
        display(
            jdbcResource
            );
        }
    
    public void display(final JdbcResource jdbcResource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", jdbcResource.name());

        for (final JdbcCatalog catalog : jdbcResource.catalogs().select())
            {
            log.debug("-- Catalog [{}]", new Object[] {catalog.name()});
            for (final JdbcSchema schema : catalog.schemas().select())
                {
                log.debug("--- Schema [{}.{}]", new Object[] {catalog.name(), schema.name()});
                for (final JdbcTable table : schema.tables().select())
                    {
                    log.debug("---- Table [{}.{}.{}]", new Object[] {catalog.name(), schema.name(), table.name()});
                    for (final JdbcColumn column : table.columns().select())
                        {
                        log.debug("----- Column [{}.{}.{}.{}]", new Object[] {catalog.name(), schema.name(), table.name(), column.name()});
                        }
                    }
                }
            }
        }
    }
