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

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
@Slf4j
public class JdbcRemoteTestCase
extends TestBase
    {
    @Autowired
    ApplicationContext spring ;

    //
    // Local properties file.
    private final Properties config = new Properties();

    public static final String CONFIG_PATH = "user.home" ;
    public static final String CONFIG_FILE = "firethorn.properties" ;

    @Override
    @Before
    public void before()
    throws Exception
        {
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

    @Test
    public void testPull()
    throws Exception
        {

        //
        // Connect to the test database.
        // final DataSource source = (DataSource) spring.getBean("RoeTunnel");

        //
        // Create an empty resource tree.
        final JdbcResource jdbcResource = factories().jdbc().resources().create(
            "twomass",
            this.unique(
                "base"
                ),
            "spring:RoeTWOMASS"
            );

        /*
        // Set the database connection properties.
        jdbcResource.connection().url(
            "spring:RoeTWOMASS"
            );
         */
        //
        // Verify we got what we expected.
        display(
            jdbcResource
            );
        //
        // Close our JDBC connection.
        jdbcResource.connection().close();

        }

    public void display(final JdbcResource resource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", resource.name());

        for (final JdbcSchema schema : resource.schemas().select())
            {
            log.debug("--- Schema [{}]", schema.fullname());
            for (final JdbcTable table : schema.tables().select())
                {
                log.debug("---- Table [{}]", table.fullname());
                for (final JdbcColumn column : table.columns().select())
                    {
                    log.debug("----- Column [{}]", column.fullname());
                    }
                }
            }
        }
    }
