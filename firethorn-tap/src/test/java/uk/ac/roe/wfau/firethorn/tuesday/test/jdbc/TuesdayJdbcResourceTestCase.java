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

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayFactories;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcConnection;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcConnectionEntity;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcMetadata;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable;
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
public class TuesdayJdbcResourceTestCase
    extends TestBase
    {

    /**
     * Our TuesdayFactories instance.
     *
     */
    @Autowired
    private TuesdayFactories factories;

    /**
     * Access to our TuesdayFactories singleton instance.
     *
     */
    public TuesdayFactories factories()
        {
        return this.factories;
        }

    //
    // Local properties file.
    private Properties config = new Properties();

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

    @Test
    public void test000()
    throws Exception
        {
        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            unique("resource")
            );
        assertNotNull(
            resource
            );
        }
    /**
     * Check the local PostgreSQL database.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:PgSqlLocalTest"
            );
        assertNotNull(
            resource.connection().open()
            );
        assertNotNull(
            resource.connection().metadata()
            );
        assertEquals(
            "PostgreSQL",
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.PGSQL.name(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.PGSQL,
            TuesdayJdbcResource.JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }

    /**
     * Check the local MySQL database.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:MySqlLocalTest"
            );
        assertNotNull(
            resource.connection().open()
            );
        assertNotNull(
            resource.connection().metadata()
            );
        assertEquals(
            "MySQL",
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.MYSQL.name(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.MYSQL,
            TuesdayJdbcResource.JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }

    /**
     * Check the live ROE database.
     *
     */
    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:RoeLiveData"
            );
        assertNotNull(
            resource.connection().open()
            );
        assertNotNull(
            resource.connection().metadata()
            );
        assertEquals(
            "Microsoft SQL Server",
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.MSSQL.name(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            TuesdayJdbcResource.JdbcProductType.MSSQL,
            TuesdayJdbcResource.JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        TuesdayJdbcResource resource = factories().jdbc().resources().create(
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:RoeLiveData"
            //"spring:PgSqlLocalTest"
            //"spring:MySqlLocalTest"
            );
        resource.inport();

        display(resource);
        
        }

    public void display(final TuesdayJdbcResource resource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", resource.name());

        for (final TuesdayJdbcSchema schema : resource.schemas().select())
            {
            log.debug("--- Schema [{}][{}]", new Object[] {resource.name(), schema.name()});
            for (final TuesdayJdbcTable table : schema.tables().select())
                {
                log.debug("---- Table [{}][{}.{}]", new Object[] {resource.name(), schema.name(), table.name()});
                for (final TuesdayJdbcColumn column : table.columns().select())
                    {
                    log.debug("----- Column [{}][{}.{}.{}]", new Object[] {resource.name(), schema.name(), table.name(), column.name()});
                    }
                }
            }
        }

    @Test
    public void test005()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        TuesdayJdbcConnection connection = new TuesdayJdbcConnectionEntity(
            "spring:RoeLiveData"
            );
        List<TuesdayJdbcResource> resources = new ArrayList<TuesdayJdbcResource>();

        for (String catalog : connection.catalogs())
            {
            log.debug("Catalog [{}]",catalog);
            String url = "jdbc:jtds:sqlserver://localhost:1433/" + catalog ;             

            TuesdayJdbcResource resource = factories().jdbc().resources().create(
                catalog
                );
            resources.add(
                resource
                );
            resource.connection().url(
                url
                );
            resource.connection().user(
                this.config.getProperty(
                    "firethorn.wfau.user"
                    )
                );
            resource.connection().pass(
                this.config.getProperty(
                    "firethorn.wfau.pass"
                    )
                );
            try {
                resource.inport();
                }
            catch(Exception ouch)
                {
                log.debug("Failed to import metadata [{}]", ouch.getMessage());
                }
            finally {
                resource.connection().close();
                }
            }
        for (TuesdayJdbcResource resource : resources)
            {
            display(
                resource
                );            
            }
        }
    }
