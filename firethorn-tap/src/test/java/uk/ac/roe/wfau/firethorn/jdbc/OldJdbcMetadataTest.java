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

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.votable.VOTableStarTableParser;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcDiference;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcTable;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.JDBCFormatter;
import uk.ac.starlink.table.jdbc.WriteMode;

/**
 *
 *
 */
@Slf4j
public class OldJdbcMetadataTest
extends TestBase
    {
    @Autowired
    ApplicationContext spring ;

    public String clean(final String string)
        {
        String temp = string ;
        temp = temp.replace(
            '.',
            '_'
            );
        temp = temp.replace(
            '-',
            '_'
            );
        return temp ;
        }

    @Test
    public void testPull()
    throws Exception
        {
        final String catname = clean(unique("catalog"));
        final String schname = clean(unique("schema"));
        final String tabname = clean(unique("table"));

        //
        // Load our test data from file.
        final Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/testdata-001.xml"
                )
            );

        //
        // Connect to the test database.
        final DataSource source = (DataSource) spring.getBean("MemData");
        final Connection connection = source.getConnection();

        //
        // Create a new schema (HSQLDB specific).
        connection.createStatement().executeUpdate(
            "CREATE SCHEMA " + schname + " AUTHORIZATION DBA ;"
            );
        connection.createStatement().executeUpdate(
            "SET SCHEMA " + schname + " ;"
            );

        //
        // Extract each of the StarTables.
        for (final StarTable stars : iter)
            {
            //
            // Alter the table name (if required).
            log.debug("VOTable [{}]", stars.getName());
            stars.setName(
                tabname
                );
            //
            // Alter the column names (if required).
            stars.getColumnInfo(0).setName("one");
            stars.getColumnInfo(1).setName("two");
            //
            // Wrap the table in a JDBCFormatter.
            final JDBCFormatter formatter = new JDBCFormatter(
                connection,
                stars
                );
            //
            // Write the data to the database.
            formatter.createJDBCTable(
                stars.getName(),
                WriteMode.DROP_CREATE
                );
            //
            // Create an empty resource tree.
            final JdbcResource jdbcResource = womble().jdbc().resources().create(
                this.unique(
                    "base"
                    )
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
        }

    @Test
    public void testPush()
    throws Exception
        {
        //String catname = clean(unique("catalog"));
        //String schname = clean(unique("schema"));
        //String tabname = clean(unique("table"));

        //
        // Connect to the test database.
        final DataSource source = (DataSource) spring.getBean("MemData");
        final Connection connection = source.getConnection();
        //
        // Create our JdbcResource.
        final JdbcResource jdbcResource = womble().jdbc().resources().create(
            unique(
                "jdbc-resource"
                )
            );
        //
        // Pull the current metadata.
        jdbcResource.diff(
            false,
            true
            );
        //
        // Select our catalog and schema.
        final JdbcCatalog jdbcCatalog = jdbcResource.catalogs().select(
            "PUBLIC"
            );
        final JdbcSchema jdbcSchema = jdbcCatalog.schemas().select(
            "PUBLIC"
            );
        //
        // Create our AdqlResource.
        final AdqlResource adqlResource = womble().adql().resources().create(
            unique(
                "adql-resource"
                )
            );
        //
        // Create our AdqlCatalog.
        final AdqlCatalog adqlCatalog = adqlResource.catalogs().create(
            unique(
                "adql-catalog"
                )
            );
        //
        // Create our AdqlSchema.
        final AdqlSchema adqlSchema = adqlCatalog.schemas().create(
            unique(
                "adql-schema"
                )
            );
        
        //
        // Load our test data from file.
        final Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/testdata-002.xml"
                )
            );
        //
        // Iterate the tables.
        for (final StarTable stars : iter)
            {
            //
            // Expected errors.
            final List<JdbcDiference> expected = new ArrayList<JdbcDiference>();
            //
            // Create our JdbcTable.
            final JdbcTable jdbcTable = jdbcSchema.tables().create(
                clean(
                    unique(
                        "jdbc-table"
                        )
                    ).toUpperCase()
                );
            //
            // Add the table to our expected errors.
            expected.add(
                new JdbcDiference(
                    JdbcDiference.Type.TABLE,
                    jdbcTable.name(),
                    null
                    )
                );
            //
            // Create our AdqlTable.
            final AdqlTable adqlTable = adqlSchema.tables().create(
                jdbcTable
                );

            //
            // Rename our AdqlTable to use the StarTable name.
            adqlTable.name(
                "adql-".concat(
                    stars.getName()
                    )
                );
            //
            // Rename the StarTable to use the JdbcTable name.
            stars.setName(
                "adql-".concat(
                    jdbcTable.name()
                    )
                );
            //
            // Iterate the StarTable columns.
            for (int index = 0 ; index < stars.getColumnCount() ; index++)
                {
                final ColumnInfo columnInfo = stars.getColumnInfo(index);
                log.debug("Column [{}]", columnInfo.getName());
                //
                // Create our JdbcColumn.
/*
 *
 * Code is available in private methods of uk.ac.starlink.table.jdbc.JDBCFormatter.
 * Copy into our own class and modify so that we can be consistent on both sides of our service.
 * http://starjava.jach.hawaii.edu/viewvc/trunk/table/src/main/uk/ac/starlink/table/jdbc/JDBCFormatter.java?view=annotate
 *
 *
 * http://www.postgresql.org/docs/9.2/static/datatype-character.html
 * http://hsqldb.org/doc/guide/ch09.html#alter_table-section
 * http://www.star.bristol.ac.uk/~mbt/stil/
 * https://docs.jboss.org/hibernate/orm/4.1/javadocs/org/hibernate/dialect/Dialect.html
 *
 */


                final JdbcColumn jdbcColumn = jdbcTable.columns().create(
                    clean(
                        unique(
                            "jdbc-column"
                            )
                        ).toUpperCase()
                    );
                //
                // Add the column to our expected errors.
/*
 * If the table hasn't been created yet,
 * then a call to diff() won't pick up the columns.
 *
                expected.add(
                    new JdbcResource.JdbcDiference(
                        JdbcResource.JdbcDiference.Type.COLUMN,
                        jdbcColumn.name(),
                        null
                        )
                    );
*/
                //
                // Find our AdqlColumn.
                final AdqlColumn adqlColumn = jdbcColumn.views().search(
                    adqlResource
                    );
                //
                // Rename our AdqlColumn to use the ColumnInfo name
                adqlColumn.name(
                    "adql-".concat(
                        columnInfo.getName()
                        )
                    );
                //
                // Rename ColumnInfo to use the JdbcColumn name.
                columnInfo.setName(
                    jdbcColumn.name()
                    );
                }
            //
            // Check the differences.
            final List<JdbcDiference> before = jdbcResource.diff(
                false,
                false
                );
            assertEquals(
                expected.size(),
                before.size()
                );
            for (final JdbcDiference diff : expected)
                {
                assertContains(
                    before,
                    diff
                    );
                }
            //
            // Push the JdbcTable updates to the database.
            jdbcResource.diff(
                true,
                false
                );
            //
            // Check the differences.
            final List<JdbcDiference> after = jdbcResource.diff(
                false,
                false
                );
for (final JdbcDiference diff : after)
    {
    log.debug("----- Diff [{}][{}]", diff.type(), diff.meta());
    }
            assertEquals(
                0,
                after.size()
                );
            //
            // Insert the test data into the database.

            }
        //
        // Display what we got.
        display(
            jdbcResource
            );
        display(
            adqlResource
            );
        }

    public void display(final JdbcResource jdbcResource)
        {
        log.debug("---");
        log.debug("- JDBC resource [{}]", jdbcResource.name());

        for (final JdbcCatalog catalog : jdbcResource.catalogs().select())
            {
            log.debug("-- Catalog [{}]", catalog.name());
            for (final JdbcSchema schema : catalog.schemas().select())
                {
                log.debug("--- Schema [{}]", schema.name());
                for (final JdbcTable table : schema.tables().select())
                    {
                    log.debug("---- Table [{}]", table.name());
                    for (final JdbcColumn column : table.columns().select())
                        {
                        log.debug("----- Column [{}]", column.name());
                        }
                    }
                }
            }
        }

    public void display(final AdqlResource adqlResource)
        {
        log.debug("---");
        log.debug("- ADQL resource [{}]", adqlResource.name());

        for (final AdqlCatalog catalog : adqlResource.catalogs().select())
            {
            log.debug("-- Catalog [{}]", catalog.name());
            for (final AdqlSchema schema : catalog.schemas().select())
                {
                log.debug("--- Schema [{}]", schema.name());
                for (final AdqlTable table : schema.tables().select())
                    {
                    log.debug("---- Table [{}]", table.name());
                    for (final AdqlColumn column : table.columns().select())
                        {
                        log.debug("----- Column [{}]", column.name());
                        }
                    }
                }
            }
        }

    public void assertContains(final List<?> collection, final Object object)
        {
        assertTrue(
            collection.contains(
                object
                )
            );
        }
    }
