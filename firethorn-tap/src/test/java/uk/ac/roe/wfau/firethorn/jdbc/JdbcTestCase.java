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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.hsqldb.lib.Collection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.votable.VOTableStarTableParser;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource.AdqlColumn;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource.AdqlTable;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcColumn;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcSchema;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcTable;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.JDBCFormatter;
import uk.ac.starlink.table.jdbc.WriteMode;

/**
 *
 *
 */
@Slf4j
public class JdbcTestCase
extends TestBase
    {
    @Autowired
    ApplicationContext spring ;

    public String clean(String string)
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
        String catname = clean(unique("catalog"));
        String schname = clean(unique("schema"));
        String tabname = clean(unique("table"));

        //
        // Connect to the test database.
        DataSource source = (DataSource) spring.getBean("MemData");
        Connection connection = source.getConnection();

        //
        // Create a new schema (HSQLDB specific).
        connection.createStatement().executeUpdate(
            "CREATE SCHEMA " + schname + " AUTHORIZATION DBA ;"
            );
        connection.createStatement().executeUpdate(
            "SET SCHEMA " + schname + " ;"
            );

        //
        // Load our test data from file.
        Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/testdata-001.xml"
                )
            ); 
        //
        // Extract each of the StarTables.
        for (StarTable stars : iter)
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
            JDBCFormatter formatter = new JDBCFormatter(
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
            JdbcResource resource = womble().resources().jdbc().create(
                this.unique(
                    "base"
                    ),
                source
                );
            //
            // Pull the metadata from the database
            resource.diff(
                false,
                true
                );
            //
            // Verify we got what we expected.
            display(resource);
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
        DataSource source = (DataSource) spring.getBean("MemData");
        Connection connection = source.getConnection();
        //
        // Create our JdbcResource.
        JdbcResource jdbcResource = womble().resources().jdbc().create(
            unique(
                "jdbc-resource"
                ),
            source
            );
        //
        // Pull the current metadata.
        jdbcResource.diff(
            false,
            true
            );
        //
        // Select our catalog and schema.
        JdbcCatalog jdbcCatalog = jdbcResource.catalogs().select(
            "PUBLIC"
            );
        JdbcSchema jdbcSchema = jdbcCatalog.schemas().select(
            "PUBLIC"
            ); 
        //
        // Create our AdqlResource.
        AdqlResource adqlResource = jdbcResource.views().create(
            unique(
                "adql-resource"
                )
            ); 

        //
        // Load our test data from file.
        Iterable<StarTable> iter = VOTableStarTableParser.iterable(
            new FileInputStream(
                "src/test/data/votable/testdata-002.xml"
                )
            ); 
        //
        // Iterate the tables.
        for (StarTable stars : iter)
            {
            //
            // Expected errors.
            List<JdbcResource.Diference> expected = new ArrayList<JdbcResource.Diference>();
            //
            // Create JdbcTable.
            JdbcTable jdbcTable = jdbcSchema.tables().create(
                clean(
                    unique(
                        "jdbc-table"
                        )
                    ).toUpperCase()
                );
            //
            // Add the table to our expected errors.
            expected.add(
                new JdbcResource.Diference(
                    JdbcResource.Diference.Type.TABLE,
                    jdbcTable.name(),
                    null
                    )
                );
            //
            // Find our AdqlTable.
            AdqlTable adqlTable = jdbcTable.views().search(
                adqlResource
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
                ColumnInfo columnInfo = stars.getColumnInfo(index);
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

                
                JdbcColumn jdbcColumn = jdbcTable.columns().create(
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
                    new JdbcResource.Diference(
                        JdbcResource.Diference.Type.COLUMN,
                        jdbcColumn.name(),
                        null
                        )
                    );
*/
                //
                // Find our AdqlColumn.
                AdqlColumn adqlColumn = jdbcColumn.views().search(
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
            List<JdbcResource.Diference> before = jdbcResource.diff(
                false,
                false
                );
            assertEquals(
                expected.size(),
                before.size()
                );
            for (JdbcResource.Diference diff : expected)
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
            List<JdbcResource.Diference> after = jdbcResource.diff(
                false,
                false
                );
for (JdbcResource.Diference diff : after)
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
        }

    public void display(JdbcResource resource)
        {
        log.debug("---");
        log.debug("- Resource [{}]", resource.name());

        for (JdbcCatalog catalog : resource.catalogs().select())
            {
            log.debug("-- Catalog [{}]", catalog.name());
            for (JdbcSchema schema : catalog.schemas().select())
                {
                log.debug("--- Schema [{}]", schema.name());
                for (JdbcTable table : schema.tables().select())
                    {
                    log.debug("---- Table [{}]", table.name());
                    for (JdbcColumn column : table.columns().select())
                        {
                        log.debug("----- Column [{}]", column.name());
                        }
                    }
                }
            }

        log.debug("---");
        for (AdqlResource view : resource.views().select())
            {
            log.debug("- View [{}]", resource.name());
            for (AdqlCatalog catalog : view.catalogs().select())
                {
                log.debug("-- Catalog [{}]", catalog.name());
                for (AdqlSchema schema : catalog.schemas().select())
                    {
                    log.debug("--- Schema [{}]", schema.name());
                    for (AdqlTable table : schema.tables().select())
                        {
                        log.debug("---- Table [{}]", table.name());
                        for (AdqlColumn column : table.columns().select())
                            {
                            log.debug("----- Column [{}]", column.name());
                            }
                        }
                    }
                }
            }
        }

    public void assertContains(List<?> collection, Object object)
        {
        assertTrue(
            collection.contains(
                object
                )
            );
        }
    }
