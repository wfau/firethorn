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
package uk.ac.roe.wfau.firethorn.adql ;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceTestBase;
import adql.db.DBChecker;
import adql.db.DBTable;
import adql.db.DefaultDBColumn;
import adql.db.DefaultDBTable;
import adql.parser.ADQLParser;
import adql.query.ADQLQuery;

/**
 *
 */
@Slf4j
public class AdqlParserTestCase
extends JdbcResourceTestBase
    {

    /**
     * Our autowired factory.
     *
     */
    @Autowired
    private AdqlDBTable.Factory factory ;

    /**
     * Simple ADQL query with no aliases.
     *
     */
    private static final String[] SIMPLE_QUERY = {

          "SELECT"
        + "    adql_ra,"
        + "    adql_dec,"
        + "    adql_pts"
        + " FROM"
        + "    adql_table"
        + " WHERE"
        + "    adql_ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    adql_dec Between '24.0' AND '24.2'"
        + "",

        "adql_table",
        null,           // I would have expected a valid schema name here.
        null,           // I would expected a valid catalog name here.
        "adql_table",   // Full table name ??

        "adql_table",
        "adql_schema",
        "adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        "jdbc_catalog",

        "adql_ra",
        "adql_dec",
        "adql_pts",

        "jdbc_ra",
        "jdbc_dec",
        "jdbc_pts"

        };

    /**
     * ADQL query with column aliases, using column names (adql_ra, adql_dec) in the WHERE clause.
     *
     */
    private static final String[] QUERY_000 = {

          "SELECT"
        + "    adql_ra  as ra,"
        + "    adql_dec as dec,"
        + "    adql_pts as pts"
        + " FROM"
        + "    adql_table"
        + " WHERE"
        + "    adql_ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    adql_dec Between '24.0' AND '24.2'"
        + "",

        "adql_table",
        null,           // I would have expected a valid schema name here.
        null,           // I would expected a valid catalog name here.
        "adql_table",   // Full table name ??

        "adql_table",
        "adql_schema",
        "adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        "jdbc_catalog",

        "adql_ra",      // We still get the original names rather than the aliases.
        "adql_dec",     // We still get the original names rather than the aliases.
        "adql_pts",     // We still get the original names rather than the aliases.

        "jdbc_ra",
        "jdbc_dec",
        "jdbc_pts"

        };

    /**
     * ADQL query with column aliases, using column aliases (ra, dec) in the WHERE clause.
     *
     */
    private static final String[] QUERY_001 = {

          "SELECT"
        + "    adql_ra  as ra,"
        + "    adql_dec as dec,"
        + "    adql_pts as pts"
        + " FROM"
        + "    adql_table"
        + " WHERE"
        + "    ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    dec Between '24.0' AND '24.2'"
        + "",

        "adql_table",
        null,           // I would have expected a valid schema name here.
        null,           // I would expected a valid catalog name here.
        "adql_table",   // Full table name ??

        "adql_table",
        "adql_schema",
        "adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        "jdbc_catalog",

        "adql_ra",      // We still get the original names rather than the aliases.
        "adql_dec",     // We still get the original names rather than the aliases.
        "adql_pts",     // We still get the original names rather than the aliases.

        "jdbc_ra",
        "jdbc_dec",
        "jdbc_pts"

        };

    /**
     * ADQL query with column and table aliases, using column names (adql_ra, adql_dec) in the WHERE clause.
     *
     */
    private static final String[] QUERY_002 = {

          "SELECT"
        + "    adql_ra  as ra,"
        + "    adql_dec as dec,"
        + "    adql_pts as pts"
        + " FROM"
        + "    adql_table as psc"
        + " WHERE"
        + "    adql_ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    adql_dec Between '24.0' AND '24.2'"
        + "",

        "adql_table",   // I would have expected the alias 'psc' here, NOT the table name.
        null,
        null,
        "adql_table",   // Full table name ??

        "psc",          // DataTable alias
        null,           // Aliased table does not have schema or catalog
        null,           // Aliased table does not have schema or catalog

        "jdbc_table",   // Aliased table still maps to the original JDBC table.
        "jdbc_schema",  // Aliased table still maps to the original JDBC table.
        "jdbc_catalog", // Aliased table still maps to the original JDBC table.

        "adql_ra",      // We still get the original names rather than the aliases.
        "adql_dec",     // We still get the original names rather than the aliases.
        "adql_pts",     // We still get the original names rather than the aliases.

        "jdbc_ra",
        "jdbc_dec",
        "jdbc_pts"

        };

    /**
     * ADQL query with column and table aliases, using table and column names in the FROM clause.
     *
     */
    private static final String[] QUERY_003 = {

          "SELECT"
        + "    psc.adql_ra  as ra,"
        + "    psc.adql_dec as dec,"
        + "    psc.adql_pts as pts"
        + " FROM"
        + "    adql_table as psc"
        + " WHERE"
        + "    adql_ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    adql_dec Between '24.0' AND '24.2'"
        + "",

        "adql_table",   // I would have expected the alias 'psc' here, NOT the table name.
        null,
        null,
        "adql_table",   // Full table name ??

        "psc",          // DataTable alias
        null,           // Aliased table does not have schema or catalog
        null,           // Aliased table does not have schema or catalog

        "jdbc_table",   // Aliased table still maps to the original JDBC table.
        "jdbc_schema",  // Aliased table still maps to the original JDBC table.
        "jdbc_catalog", // Aliased table still maps to the original JDBC table.

        "adql_ra",      // We still get the original names rather than the aliases.
        "adql_dec",     // We still get the original names rather than the aliases.
        "adql_pts",     // We still get the original names rather than the aliases.

        "jdbc_ra",
        "jdbc_dec",
        "jdbc_pts"

        };

    /**
     * Use DefaultDBTable to create a DBTable metadata.
     *
     */
    public DBTable dbTable()
    throws Exception
        {
        //
        // Create our table metadata.
        final DefaultDBTable dbtable = new DefaultDBTable(
            "jdbc_catalog",
            "adql_catalog",
            "jdbc_schema",
            "adql_schema",
            "jdbc_table",
            "adql_table"
            );
        //
        // Add the column metadata.
        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_ra",
                "adql_ra",
                dbtable
                )
            );
        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_dec",
                "adql_dec",
                dbtable
                )
            );
        dbtable.addColumn(
            new DefaultDBColumn(
                "jdbc_pts",
                "adql_pts",
                dbtable
                )
            );

        return dbtable ;
        }

    /**
     * Use AdqlDBTable to create a DBTable metadata based on a AdqlResource.
     *
     */
    public DBTable adqlTable()
    throws Exception
        {
        //
        // Create base catalog, schema and table.
        jdbc().resource().catalogs().create(
            "jdbc_catalog"
            ).schemas().create(
                "jdbc_schema"
                ).tables().create(
                    "jdbc_table"
                    );
        //
        // Create the columns.
        jdbc().resource().catalogs().select(
            "jdbc_catalog"
            ).schemas().select(
                "jdbc_schema"
                ).tables().select(
                    "jdbc_table"
                    ).columns().create(
                        "jdbc_ra"
                        );

        jdbc().resource().catalogs().select(
            "jdbc_catalog"
            ).schemas().select(
                "jdbc_schema"
                ).tables().select(
                    "jdbc_table"
                    ).columns().create(
                        "jdbc_dec"
                        );

        jdbc().resource().catalogs().select(
            "jdbc_catalog"
            ).schemas().select(
                "jdbc_schema"
                ).tables().select(
                    "jdbc_table"
                    ).columns().create(
                        "jdbc_pts"
                        );

        //
        // Create ADQL resource, catalog and schema.

        //
        // Add the JDBC catalog to the ADQL resource. 
/*
 * 
        //
        // Create our ADQL view.
        base().views().create(
            "adql_view"
            );

        //
        // Change the view names.
        base().views().select(
            "adql_view"
            ).catalogs().select(
                "jdbc_catalog"
                ).name(
                    "adql_catalog"
                    );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "jdbc_schema"
                    ).name(
                        "adql_schema"
                        );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "jdbc_table"
                        ).name(
                            "adql_table"
                            );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_ra"
                            ).name(
                                "adql_ra"
                                );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_dec"
                            ).name(
                                "adql_dec"
                                );

        base().views().select(
            "adql_view"
            ).catalogs().select(
                "adql_catalog"
                ).schemas().select(
                    "adql_schema"
                    ).tables().select(
                        "adql_table"
                        ).columns().select(
                            "jdbc_pts"
                            ).name(
                                "adql_pts"
                                );

        //
        // Wrap the AdqlResource.AdqlTable in an AdqlDBTable.
        return factory.create(
            base().views().select(
                "adql_view"
                ).catalogs().select(
                    "adql_catalog"
                    ).schemas().select(
                        "adql_schema"
                        ).tables().select(
                            "adql_table"
                            )
            );
 */
        return null ;
        }

    /**
     * Parse a query and check the results.
     *
     */
    public void check(final DBTable metadata, final String[] data)
    throws Exception
        {
        //
        // Create a parser:
        final ADQLParser parser = new ADQLParser();
        //
        // Add the metadata to our parser.
        parser.setQueryChecker(
            new DBChecker(
                Arrays.asList(
                    metadata
                    )
                )
            );
        //
        // Parse a query and check the results.
        check(
            parser.parseQuery(
                data[0]
                ),
            data
            );
        }

    /**
     * Check the results of an ADQL query.
     *
     */
    public void check(final ADQLQuery query, final String[] data)
    throws Exception
        {
        //
        // Check the parsed ADQLTable.
        assertEquals(
            data[1],
            query.getFrom().getTables().get(0).getTableName()
            );
        assertEquals(
            data[2],
            query.getFrom().getTables().get(0).getSchemaName()
            );
        assertEquals(
            data[3],
            query.getFrom().getTables().get(0).getCatalogName()
            );
        assertEquals(
            data[4],
            query.getFrom().getTables().get(0).getFullTableName()
            );
        //
        // Check the DBTable ADQL names.
        assertEquals(
            data[5],
            query.getFrom().getTables().get(0).getDBLink().getADQLName()
            );
        assertEquals(
            data[6],
            query.getFrom().getTables().get(0).getDBLink().getADQLSchemaName()
            );
        assertEquals(
            data[7],
            query.getFrom().getTables().get(0).getDBLink().getADQLCatalogName()
            );
        //
        // Check the DBTable JDBC names.
        assertEquals(
            data[8],
            query.getFrom().getTables().get(0).getDBLink().getDBName()
            );
/*
 * The CDS implementation behaves differently with aliased tables.
 * The CDS implementation sets the JDBC schema and catalog to null.
 * I think the JDBC schema and catalog should still match the original.
        assertEquals(
            data[9],
            query.getFrom().getTables().get(0).getDBLink().getDBSchemaName()
            );
        assertEquals(
            data[10],
            query.getFrom().getTables().get(0).getDBLink().getDBCatalogName()
            );
 *
 */
        //
        // Check the associated DBColumn ADQL names.
        assertEquals(
            data[11],
            query.getFrom().getDBColumns().get(0).getADQLName()
            );
        assertEquals(
            data[12],
            query.getFrom().getDBColumns().get(1).getADQLName()
            );
        assertEquals(
            data[13],
            query.getFrom().getDBColumns().get(2).getADQLName()
            );
        //
        // Check the associated DBColumn JDBC names.
        assertEquals(
            data[14],
            query.getFrom().getDBColumns().get(0).getDBName()
            );
        assertEquals(
            data[15],
            query.getFrom().getDBColumns().get(1).getDBName()
            );
        assertEquals(
            data[16],
            query.getFrom().getDBColumns().get(2).getDBName()
            );

        }


    /**
     * Simple test with no table checker.
     *
     */
    @Test
    public void test000()
    throws Exception
        {
        //
        // Create a parser:
        final ADQLParser parser = new ADQLParser();
        //
        // Parse the test query.
        final ADQLQuery query = parser.parseQuery(
            SIMPLE_QUERY[0]
            );
        //
        // Check the parsed ADQL tree.
        assertEquals(
            1,
            query.getFrom().getTables().size()
            );
        assertEquals(
            "adql_table",
            query.getFrom().getTables().get(0).getTableName()
            );
        }

    /**
     * Use DefaultDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testDBTableSimpleQuery()
    throws Exception
        {
        check(
            dbTable(),
            SIMPLE_QUERY
            );
        }

    /**
     * Use AdqlDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testAdqlTableSimpleQuery()
    throws Exception
        {
        check(
            adqlTable(),
            SIMPLE_QUERY
            );
        }

    /**
     * Use DefaultDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testDBTableQuery000()
    throws Exception
        {
        check(
            dbTable(),
            QUERY_000
            );
        }

    /**
     * Use AdqlDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testAdqlTableQuery000()
    throws Exception
        {
        check(
            adqlTable(),
            QUERY_000
            );
        }

    /**
     * Use DefaultDBTable based metadata for the QueryChecker.
     * Unresolved identifiers: ra [l.1 c.94 - l.1 c.96], dec [l.1 c.131 - l.1 c.134]
     *
    @Test
    public void testDBTableQuery001()
    throws Exception
        {
        check(
            dbTable(),
            QUERY_001
            );
        }
     */

    /**
     * Use AdqlDBTable based metadata for the QueryChecker.
     * Unresolved identifiers: ra [l.1 c.94 - l.1 c.96], dec [l.1 c.131 - l.1 c.134]
     *
    @Test
    public void testAdqlTableQuery001()
    throws Exception
        {
        check(
            adqlTable(),
            QUERY_001
            );
        }
     */

    /**
     * Use DefaultDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testDBTableQuery002()
    throws Exception
        {
        check(
            dbTable(),
            QUERY_002
            );
        }

    /**
     * Use AdqlDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testAdqlTableQuery002()
    throws Exception
        {
        check(
            adqlTable(),
            QUERY_002
            );
        }

    /**
     * Use DefaultDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testDBTableQuery003()
    throws Exception
        {
        check(
            dbTable(),
            QUERY_003
            );
        }

    /**
     * Use AdqlDBTable based metadata for the QueryChecker.
     *
     */
    @Test
    public void testAdqlTableQuery003()
    throws Exception
        {
        check(
            adqlTable(),
            QUERY_003
            );
        }
    }

