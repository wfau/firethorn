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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcProductType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * TODO experiment with this
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseBuilder.html
 *
 */
@Slf4j
public class JdbcConnectionTestCase
    extends JdbcResourceTestThing
    {

    /**
     * Check the local PostgreSQL database.
     *
     */
    //@Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        final JdbcResource resource = factories().jdbc().resources().create(
            "ogsa-resource",
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:PgsqlLocalTest"
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
            JdbcProductType.PGSQL.alias(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            JdbcProductType.PGSQL,
            JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }

    /**
     * Check the local MySQL database.
     *
     */
    //@Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            factories()
            );
        final JdbcResource resource = factories().jdbc().resources().create(
            "ogsa-resource",
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:MysqlLocalTest"
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
            JdbcProductType.MYSQL.alias(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            JdbcProductType.MYSQL,
            JdbcProductType.match(
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
        final JdbcResource resource = factories().jdbc().resources().create(
            "ogsa-resource",
            unique("resource")
            );
        assertNotNull(
            resource
            );
        resource.connection().url(
            "spring:RoeTWOMASS"
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
            JdbcProductType.MSSQL.alias(),
            resource.connection().metadata().getDatabaseProductName()
            );
        assertEquals(
            JdbcProductType.MSSQL,
            JdbcProductType.match(
                resource.connection().metadata().getDatabaseProductName()
                )
            );
        }
    }
