/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;


import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
@Slf4j
public class JdbcColumnSizeTestCase
    extends AtlasQueryTestBase
    {


    public void checksize(final JdbcTable table, final String colname, final int colsize)
    throws SQLException
        {
        final JdbcResource resource = table.resource();
        try {
            final Connection conn = resource.connection().open();
            final Statement  stmt = conn.createStatement();

            final ResultSet  rset = stmt.executeQuery("SELECT TOP 1 " + colname + " FROM " + table.name());
            final ResultSetMetaData rsmd = rset.getMetaData();

            assertEquals(
                colsize,
                rsmd.getPrecision(1)
                );
            }
        finally {
            resource.connection().close();
            }
        }

    /**
     * VARCHAR column size.
     *
     */
    @Test
    public void test001()
    throws SQLException
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" +
            "    project\n" +
            "FROM\n" +
            "    Multiframe\n" +
            "WHERE\n" +
            "    project LIKE 'ATLAS%'" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        compare(
            query,
            "select {ATLAS_VERSION}.dbo.multiframe.project as project from {ATLAS_VERSION}.dbo.multiframe where {ATLAS_VERSION}.dbo.multiframe.project like 'ATLAS%'"
            );
        //
        // Check the actual JDBC column size.
        checksize(
            query.results().jdbc(),
            "project",
            64
            );
        }
    }
