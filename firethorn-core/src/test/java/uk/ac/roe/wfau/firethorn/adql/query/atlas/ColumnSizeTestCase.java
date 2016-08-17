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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 *
 *
 */
public class ColumnSizeTestCase
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
    throws SQLException, QueryProcessingException
        {
        final GreenQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    project" +
            " FROM" +
            "    Multiframe" +
            " WHERE" +
            "    project LIKE 'ATLAS%'",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.multiframe.project AS project" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.multiframe" +
            " WHERE" +
            "    {ATLAS_VERSION}.dbo.multiframe.project LIKE 'ATLAS%'",

            new ExpectedField[] {
                new ExpectedField("project", AdqlColumn.AdqlType.CHAR, 64)
                }
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
