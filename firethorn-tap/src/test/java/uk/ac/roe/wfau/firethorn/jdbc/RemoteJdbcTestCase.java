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
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable;

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
        final TuesdayJdbcResource jdbcResource = factories().jdbc().resources().create(
            this.unique(
                "base"
                )
            );
        /*
        final JdbcResource jdbcResource = womble().jdbc().resources().select(
            "base.1352861674755.0"
            ).iterator().next();
         */

        //
        // Set the database connection properties.
        jdbcResource.connection().url(
            "spring:RoeTWOMASS"
            );
        //
        // Scan the resource for catalogs.
        jdbcResource.inport();

        //
        // Close our JDBC connection.
        jdbcResource.connection().close();

        //
        // Verify we got what we expected.
        display(
            jdbcResource
            );
        }

    public void display(final TuesdayJdbcResource resource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", resource.name());

        for (final TuesdayJdbcSchema schema : resource.schemas().select())
            {
            log.debug("--- Schema [{}]", schema.fullname());
            for (final TuesdayJdbcTable table : schema.tables().select())
                {
                log.debug("---- Table [{}]", table.fullname());
                for (final TuesdayJdbcColumn column : table.columns().select())
                    {
                    log.debug("----- Column [{}]", column.fullname());
                    }
                }
            }
        }
    }
