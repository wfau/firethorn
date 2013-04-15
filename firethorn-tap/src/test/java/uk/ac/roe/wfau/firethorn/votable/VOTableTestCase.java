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
package uk.ac.roe.wfau.firethorn.votable;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceTestBase;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.JDBCFormatter;
import uk.ac.starlink.table.jdbc.JDBCFormatter.SqlColumn;
import uk.ac.starlink.table.jdbc.WriteMode;

/**
 *
 *
 */
@Slf4j
public class VOTableTestCase
extends JdbcResourceTestBase
    {

    @Autowired
    ApplicationContext spring ;

    @Test
    public void testVotableParser()
    throws Exception
        {
        final Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/random/random-10.xml"
                )
            );

        for (final StarTable table : iter)
            {
            log.debug("Table [{}][{}]",
                table.getRowCount(),
                table.getColumnCount()
                );

            for (int index = 0 ; index < table.getColumnCount() ; index++)
                {
                log.debug(
                    "Column [{}][{}]",
                    table.getColumnInfo(index).getName(),
                    table.getColumnInfo(index).getContentClass().getName()
                    );
                }
            }
        }

    /**
     * https://numberformat.wordpress.com/2010/06/03/creating-and-using-hsqldb-database-using-maven-in-10-minutes/
     * http://stackoverflow.com/questions/9403561/reload-database-connection-in-springhibernate-test-case
     *
     *
     */
    @Test
    public void testJdbcWriter()
    throws Exception
        {

        final Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/random/random-10.xml"
                )
            );

        for (final StarTable table : iter)
            {
            log.debug("Table [{}][{}]",
                table.getRowCount(),
                table.getColumnCount()
                );

            //
            // Change the column names (ADQL => JDBC)
            for (int index = 0 ; index < table.getColumnCount() ; index++)
                {
                final ColumnInfo column = table.getColumnInfo(index);
                log.debug(
                    "Column [{}]",
                    column.getName()
                    );
                column.setName(
                    "prefix-".concat(
                        column.getName()
                        )
                    );
                }

            final DataSource source = (DataSource) spring.getBean("HsqldbTempData");
            final Connection connection = source.getConnection();

            //connection.createStatement().execute("CREATE SCHEMA albert AUTHORIZATION DBA ;");
            //connection.createStatement().execute("SET SCHEMA albert ;");

            final JDBCFormatter formatter = new JDBCFormatter(
                connection,
                table
                );

            //log.debug("Create statement [{}]", formatter.getCreateStatement("myschema.mytable"));
            //log.debug("Insert statement [{}]", formatter.getInsertStatement("myschema.mytable"));

            for (int index = 0 ; index < table.getColumnCount() ; index++)
                {
                final SqlColumn column = formatter.getColumn(index);
                log.debug(
                    "Column [{}][{}]",
                    column.getColumnName(),
                    column.getTypeSpec()
                    );
                }

            formatter.createJDBCTable(
                "mytable",
                WriteMode.DROP_CREATE
                );

            // HSQLDB specific 'save to disc' command.
            //connection.createStatement().execute("SHUTDOWN;");
            //connection.close();

            }
        }
    }
