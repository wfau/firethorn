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

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaTable;

import adql.db.DBChecker;
import adql.db.DBColumn;
import adql.db.DBTable;
import adql.db.DefaultDBColumn;
import adql.db.DefaultDBTable;
import adql.parser.ADQLParser;
import adql.query.ADQLQuery;
import adql.query.from.ADQLTable;
import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;
import adql.translator.PostgreSQLTranslator;

//import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMapping;
//import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

/**
 *
 */
@Slf4j
public class AdqlParserTestCase
extends TestBase
    {

    /**
     * Our autowired AdqlDBTable factory.
     *
     */
    @Autowired
    private AdqlDBTable.Factory tables;
    public AdqlDBTable.Factory tables()
    	{
    	return this.tables ;
    	}

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
        null,//"adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        null,//"jdbc_catalog",

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
        null,//"adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        null,//"jdbc_catalog",

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
        null,//"adql_catalog",

        "jdbc_table",
        "jdbc_schema",
        null,//"jdbc_catalog",

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
        null,//"jdbc_catalog", // Aliased table still maps to the original JDBC table.

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
        null,//"jdbc_catalog", // Aliased table still maps to the original JDBC table.

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
            null,//"jdbc_catalog",
            null,//"adql_catalog",
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
    	TuesdayJdbcResource jdbcResource = factories().jdbc().resources().create(
			"jdbc_resource"
			);
    	//
        // Create base catalog, schema and table.
    	jdbcResource.schemas().create(
            "jdbc_schema"
            ).tables().create(
                "jdbc_table"
                );
        //
        // Create the columns.
    	jdbcResource.schemas().select(
            "jdbc_schema"
            ).tables().select(
                "jdbc_table"
                ).columns().create(
                    "jdbc_ra"
                    );

    	jdbcResource.schemas().select(
            "jdbc_schema"
            ).tables().select(
                "jdbc_table"
                ).columns().create(
                    "jdbc_dec"
                    );

    	jdbcResource.schemas().select(
            "jdbc_schema"
            ).tables().select(
                "jdbc_table"
                ).columns().create(
                    "jdbc_pts"
                    );

        //
        // Create our ADQL resource.
        TuesdayAdqlResource adqlResource = factories().adql().resources().create("test"); 
        adqlResource.schemas().create(
        		jdbcResource.schemas().select(
                    "jdbc_schema"
                    ),
        		"adql_schema"
        		);
        //
        // Change the table name.
		adqlResource.schemas().select(
				"adql_schema"
				).tables().select(
					"jdbc_table"
					).name(
						"adql_table"
						);
        //
        // Change the column names.
		adqlResource.schemas().select(
				"adql_schema"
				).tables().select(
					"adql_table"
					).columns().select(
						"jdbc_ra"
						).name(
							"adql_ra"
							);
		adqlResource.schemas().select(
				"adql_schema"
				).tables().select(
					"adql_table"
					).columns().select(
						"jdbc_dec"
						).name(
							"adql_dec"
							);
		adqlResource.schemas().select(
				"adql_schema"
				).tables().select(
					"adql_table"
					).columns().select(
						"jdbc_pts"
						).name(
							"adql_pts"
							);
        
        return tables().create(
        		adqlResource.schemas().select(
    				"adql_schema"
    				).tables().select(
						"adql_table"
    					)
    		) ;
        }

    public ADQLQuery query(final DBTable metadata, final String query)
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
        return parser.parseQuery(
    		query
            );
    	}
    /**
     * Parse a query and check the results.
     *
     */
    public void check(final DBTable metadata, final String[] data)
    throws Exception
        {
        //
        // Parse a query and check the results.
        check(
            query(
        		metadata,
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
/*
 *
        assertEquals(
            data[8],
            query.getFrom().getTables().get(0).getDBLink().getDBName()
            );
 * 
 */
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
/*
 * 
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
 * 
 */
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

    /**
     * Simple ADQL query for the imported table.
     *
     */
    private static final String[] IMPORTED_000 = {

          "SELECT"
        + "    ra,"
        + "    dec,"
        + "    pts_key"
        + " FROM"
        + "    adql_schema.twomass_psc"
        + " WHERE"
        + "    ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    dec Between '24.0' AND '24.2'"
        + "",

        "twomass_psc",
        null,
        null,         
        "twomass_psc",

        "twomass_psc",
        "adql_schema",
        null,

        "twomass_psc",
        "dbo",
        null,

        "ra",
        "dec",
        "pts",

        "ra",
        "dec",
        "pts"

        };

    private static final String[] IMPORTED_001 = {

        "SELECT"
      + "    pts_key,"
      + "    ra || ' - ' || dec as \"Position\""
      + " FROM"
      + "    twomass_psc"
      + " WHERE"
      + "    Contains(Point('ICRS', ra, dec), Circle('ICRS', 10, 5, 1)) = 1"
      + " ORDER BY pts_key"
      + "",

      "twomass_psc",
      null,
      null,         
      "twomass_psc",

      "twomass_psc",
      "adql_schema",
      null,

      "twomass_psc",
      "dbo",
      null,

      "ra",
      "dec",
      "pts",

      "ra",
      "dec",
      "pts"

      };

    
    public TuesdayJdbcResource resource()
		{
	    return factories().jdbc().resources().create(
	        "test-resource",
	        "spring:RoeLiveData"
	        );
		}

	public TuesdayAdqlResource workspace()
		{
	    return factories().adql().resources().create(
	        "test-workspace"
	        );
		}

	@Test
    public void testImportedTable()
    throws Exception
        {
        TuesdayJdbcResource adqlresource  = resource();
        TuesdayAdqlResource adqlworkspace = workspace();
        //
        // Import a JdbcSchema into our AdqlWorkspace.
        adqlresource.inport();
        adqlworkspace.schemas().create(
    		adqlresource.schemas().select("TWOMASS.dbo"),
    		"adql_schema"
            ); 
        //
        // Create an ADQL DBTable.
        AdqlDBTable aqdbtable = this.tables.create(
    		adqlworkspace.schemas().select(
    		    "adql_schema"
    		    ).tables().select(
    		        "twomass_psc"
    		        )
    		);

        ADQLQuery query = query(
    		aqdbtable,
    		IMPORTED_000[0]
            );
        
    	for (ADQLTable querytable : query.getFrom().getTables())
    		{
    		log.debug("ADQLTable  [{}]", querytable.getName());
    		log.debug("  Class    [{}]", querytable.getClass().getName());
    		log.debug(" --------- ");
    		log.debug("  Table    [{}]", querytable.getTableName());
    		log.debug("  Schema   [{}]", querytable.getSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getCatalogName());
    		log.debug("  FullName [{}]", querytable.getFullTableName());
    		log.debug(" ADQL --------- ");
    		log.debug("  Table    [{}]", querytable.getDBLink().getADQLName());
    		log.debug("  Schema   [{}]", querytable.getDBLink().getADQLSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getDBLink().getADQLCatalogName());
    		log.debug(" OGSA --------- ");
    		log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
    		log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());

    		for (DBColumn column : query.getFrom().getDBColumns())
    			{
        		log.debug("   ------- ");
    			log.debug("  Column ADQL [{}][{}][{}][{}]", column.getTable().getADQLCatalogName(), column.getTable().getADQLSchemaName(), column.getTable().getADQLName(),     column.getADQLName());
    			log.debug("         OGSA [{}][{}][{}][{}]",   column.getTable().getDBCatalogName(),  column.getTable().getDBSchemaName(), column.getTable().getDBName(), column.getDBName());
    			}

            TuesdayBaseTable<?,?> mapped = resolve(
                querytable.getDBLink().getDBName()
                );

            log.debug(" MAPPED --------- ");
            log.debug("  Class    [{}]", mapped.getClass().getName());
            log.debug("  Alias    [{}]", mapped.alias());
            log.debug("  Link     [{}]", mapped.link());
            log.debug("  FullName [{}]", mapped.fullname());
            log.debug("  Resource [{}]", mapped.resource().name());

    		}

    	ADQLTranslator translator = new PostgreSQLTranslator(false);
    	log.debug("ADQL [{}]", query.toADQL());
    	log.debug("SQL  [{}]",  translator.translate(query));

        }

    public TuesdayBaseTable<?,?> resolve(String source)
    throws Exception
        {
        return factories().base().tables().resolver().select(
            source
            );
        }
    }

