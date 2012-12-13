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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable.Mode;
import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable.ModeContainer;
import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlSchema;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaResource;
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
public class AdqlQueryTestCase
extends TestBase
    {

    /**
     * Our ADQL parser mode.
     * 
     */
    private Mode mode = Mode.ALIASED; 
    public ModeContainer mode(Mode mode)
        {
        this.mode = mode;
        return this.mode();
        }
    public ModeContainer mode()
        {
        return new ModeContainer()
            {
            @Override
            public Mode mode()
                {
                return AdqlQueryTestCase.this.mode ;
                }
            };
        }

    /**
     * Our Autowired AdqlDBTable factory.
     *
     */
    @Autowired
    private AdqlDBTable.Factory tables;
    public AdqlDBTable.Factory tables()
    	{
    	return this.tables ;
    	}

   /**
     * Resolve a table alias into a BaseTable. 
     *
     */
    public TuesdayBaseTable<?,?> resolve(ADQLTable querytable)
    throws Exception
        {
        return factories().base().tables().resolver().select(
            querytable.getDBLink().getDBName()
            );
        }

    /**
     * Simple ADQL query for the imported table.
     *
     */
    private static final String IMPORTED_000 = 

          "SELECT"
        + "    ra,"
        + "    dec,"
        + "    pts_key"
        + " FROM"
        + "    test_schema.twomass_psc"
        + " WHERE"
        + "    ra  Between '56.0' AND '57.9'"
        + " AND"
        + "    dec Between '24.0' AND '24.2'"
        + ""
        ;

	@Test
    public void testImported000()
    throws Exception
        {
        //
        // Create our JDBC resource.
        TuesdayJdbcResource twomass = factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeTWOMASS"
            );
        //
        // Create our ADQL workspace.
        TuesdayAdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import from our JDBC resource into our ADQL workspace.
        twomass.inport();
        workspace.schemas().create(
            twomass.schemas().select("TWOMASS.dbo"),
    		"test_schema"
            ); 
        //
        // Wrap the workspace tables as parser DBTables.
        List<DBTable> tableset = new ArrayList<DBTable>();
        for (TuesdayAdqlSchema schema : workspace.schemas().select())
            {
            for (TuesdayAdqlTable table : schema.tables().select())
                {
                tableset.add(
                    this.tables.create(
                        this.mode(
                            AdqlDBTable.Mode.ALIASED
                            ),
                        table
                        )
                    );
                }
            }
        //
        // Create an ADQL parser:
        final ADQLParser parser = new ADQLParser();
        //
        // Add a DBChecker using the DBTables.
        parser.setQueryChecker(
            new DBChecker(
                tableset
                )
            );
        //
        // Parse our query.
        ADQLQuery query = parser.parseQuery(
            IMPORTED_000
            );
        //
        // Iterate the list of tables used by the query.
    	for (ADQLTable querytable : query.getFrom().getTables())
    		{
    		log.debug("ADQLTable  [{}]", querytable.getName());
    		log.debug("  Class    [{}]", querytable.getClass().getName());
            log.debug("  Class    [{}]", querytable.getDBLink().getClass().getName());
    		log.debug(" --------- ");
    		log.debug("  Table    [{}]", querytable.getTableName());
    		log.debug("  Schema   [{}]", querytable.getSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getCatalogName());
    		log.debug("  FullName [{}]", querytable.getFullTableName());
    		log.debug(" ADQL --------- ");
    		log.debug("  Table    [{}]", querytable.getDBLink().getADQLName());
    		log.debug("  Schema   [{}]", querytable.getDBLink().getADQLSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getDBLink().getADQLCatalogName());
    		log.debug(" JDBC --------- ");
    		log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
    		log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
    		log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());

    		for (DBColumn column : query.getFrom().getDBColumns())
    			{
        		log.debug("   ------- ");
    			log.debug("  Column ADQL [{}][{}][{}][{}]", column.getTable().getADQLCatalogName(), column.getTable().getADQLSchemaName(), column.getTable().getADQLName(),     column.getADQLName());
    			log.debug("         OGSA [{}][{}][{}][{}]", column.getTable().getDBCatalogName(),  column.getTable().getDBSchemaName(), column.getTable().getDBName(), column.getDBName());
    			}

            TuesdayBaseTable<?,?> mapped = this.resolve(
                querytable
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

    private static final String IMPORTED_001 =

          "SELECT"
        + "    twomass_scn.date,"
        + "    twomass_scn.scan,"
        + "    twomass_scn.tile,"
        + "    twomass_psc.ra || ' - ' || twomass_psc.dec as \"Position\""
        + " FROM"
        + "    test_schema.twomass_psc,"
        + "    test_schema.twomass_scn"
        + " WHERE"
        + "    (Contains(Point('ICRS', twomass_psc.ra, twomass_psc.dec), Circle('ICRS', 10, 5, 1)) = 1)"
        + " AND"
        + "    (twomass_psc.scan_key = twomass_scn.scan_key)"
        + " ORDER BY pts_key"
        + ""
        ;

    @Test
    public void testImported001()
    throws Exception
        {
        //
        // Create our JDBC resource.
        TuesdayJdbcResource twomass = factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeTWOMASS"
            );
        //
        // Create our ADQL workspace.
        TuesdayAdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import a JdbcSchema into our AdqlWorkspace.
        twomass.inport();
        workspace.schemas().create(
            twomass.schemas().select("TWOMASS.dbo"),
            "test_schema"
            ); 
        //
        // Wrap the workspace tables as parser DBTables.

        List<DBTable> tableset = new ArrayList<DBTable>();
        for (TuesdayAdqlSchema schema : workspace.schemas().select())
            {
            for (TuesdayAdqlTable table : schema.tables().select())
                {
                tableset.add(
                    this.tables.create(
                        this.mode(),
                        table
                        )
                    );
                }
            }
        //
        // Create an ADQL parser:
        final ADQLParser parser = new ADQLParser();
        //
        // Add a DBChecker using the DBTables.
        parser.setQueryChecker(
            new DBChecker(
                tableset
                )
            );
        //
        // Parse our query.
        ADQLQuery query = parser.parseQuery(
            IMPORTED_001
            );
        //
        // Keep a list of the resources used.
        Set<TuesdayOgsaResource<?>> resources = new HashSet<TuesdayOgsaResource<?>>();
        //
        // Iterate the list of tables used by the query.
        for (ADQLTable querytable : query.getFrom().getTables())
            {
            log.debug("ADQLTable  [{}]", querytable.getName());
            log.debug("  Class    [{}]", querytable.getClass().getName());
            log.debug("  Class    [{}]", querytable.getDBLink().getClass().getName());
            log.debug("  Table    [{}]", querytable.getTableName());
            log.debug("  Schema   [{}]", querytable.getSchemaName());
            log.debug("  Catalog  [{}]", querytable.getCatalogName());
            log.debug("  FullName [{}]", querytable.getFullTableName());
            log.debug(" ADQL --------- ");
            log.debug("  Table    [{}]", querytable.getDBLink().getADQLName());
            log.debug("  Schema   [{}]", querytable.getDBLink().getADQLSchemaName());
            log.debug("  Catalog  [{}]", querytable.getDBLink().getADQLCatalogName());
            log.debug(" JDBC --------- ");
            log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
            log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
            log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());

            if (querytable.getDBLink() instanceof AdqlDBTable)
                {
                AdqlDBTable table = (AdqlDBTable) querytable.getDBLink(); 
                //
                // Add the table resource to our set.
                resources.add(
                    table.table().ogsa().resource()
                    );

                if (table.mode() == Mode.ALIASED)
                    {
                    TuesdayBaseTable<?,?> mapped = this.resolve(
                        querytable
                        );
                    log.debug(" Mode.ALIASED --------- ");
                    log.debug("  Class    [{}]", mapped.getClass().getName());
                    log.debug("  Alias    [{}]", mapped.alias());
                    log.debug("  Link     [{}]", mapped.link());
                    log.debug("  FullName [{}]", mapped.fullname());
                    log.debug("  Resource [{}]", mapped.resource().name());
                    }
                else {
                    log.debug(" Mode.DIRECT --------- ");
                    log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
                    log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
                    log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());
                    }
                }
            else {
                log.warn("DBLink is not instanceof AdqlDBTable");
                log.debug("  DBLink [{}]", querytable.getDBLink().getClass().getName());
                }
            }


        log.debug("Resources ----");
        for (TuesdayOgsaResource<?> resource : resources)
            {
            log.debug("Resource [{}][{}][{}]", resource.ident(), resource.ogsaid(), resource.name());
            }
        if (resources.size() == 1)
            {
            log.debug("Single resource, use direct");
            }
        else {
            log.debug("Multiple resource, use DQP");
            }

        ADQLTranslator translator = new PostgreSQLTranslator(false);
        log.debug("ADQL [{}]", query.toADQL());
        this.mode(Mode.DIRECT);
        log.debug("DIRECT SQL : {}", translator.translate(query));
        this.mode(Mode.ALIASED);
        log.debug("ALIASED SQL : {}", translator.translate(query));
        
        }

    private static final String IMPORTED_002 =

          "SELECT"
        + "    scn.date,"
        + "    scn.scan,"
        + "    scn.tile,"
        + "    psc.ra || ' - ' || psc.dec as \"Position\""
        + " FROM"
        + "    adql_twomass.twomass_psc AS psc,"
        + "    adql_twomass.twomass_scn AS scn,"
        + "    adql_twomass.twomass_pscXBestDR7PhotoObjAll AS match,"
        + "    adql_bestdr7.PhotoObjAll AS photo"
        + " WHERE"
        + "    (psc.scan_key = scn.scan_key)"
        + " AND"
        + "    (match.masterObjID = psc.pts_key)"
        + " AND"
        + "    (match.slaveObjID  = photo.objID)"
        + " AND"
        + "    (match.distanceMins < 0.01)"
        + " ORDER BY"
        + "    psc.pts_key"
        + ""
        ;


    @Test
    public void testImported002()
    throws Exception
        {
        //
        // Create our JDBC resources.
        TuesdayJdbcResource twomass = factories().jdbc().resources().create(
            "twomass",
            "spring:RoeTWOMASS"
            );
        TuesdayJdbcResource twoxmm = factories().jdbc().resources().create(
            "twoxmm",
            "spring:RoeTWOXMM"
            );
        TuesdayJdbcResource bestdr7  = factories().jdbc().resources().create(
            "bestdr7",
            "spring:RoeBestDR7"
            );
        twomass.inport();
        twoxmm.inport();
        bestdr7.inport();
        //
        // Create our ADQL workspace.
        TuesdayAdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import a JdbcSchema into our AdqlWorkspace.
        workspace.schemas().create(
            twomass.schemas().select(
                "TWOMASS.dbo"
                ),
            "adql_twomass"
            ); 
        workspace.schemas().create(
            twoxmm.schemas().select(
                "TWOXMM.dbo"
                ),
            "adql_twoxmm"
            ); 
        workspace.schemas().create(
            bestdr7.schemas().select(
                "BestDR7.dbo"
                ),
            "adql_bestdr7"
            ); 
        //
        // Wrap the workspace tables as parser DBTables.
        List<DBTable> tableset = new ArrayList<DBTable>();
        for (TuesdayAdqlSchema schema : workspace.schemas().select())
            {
            for (TuesdayAdqlTable table : schema.tables().select())
                {
                tableset.add(
                    this.tables.create(
                        this.mode(),
                        table
                        )
                    );
                }
            }
        //
        // Create an ADQL parser:
        final ADQLParser parser = new ADQLParser();
        //
        // Add a DBChecker using the DBTables.
        parser.setQueryChecker(
            new DBChecker(
                tableset
                )
            );
        //
        // Parse our query.
        ADQLQuery query = parser.parseQuery(
            IMPORTED_002
            );
        //
        // Keep a list of the resources used.
        Set<TuesdayOgsaResource<?>> resources = new HashSet<TuesdayOgsaResource<?>>();
        //
        // Iterate the list of tables used by the query.
        for (ADQLTable querytable : query.getFrom().getTables())
            {
            log.debug("ADQLTable  [{}]", querytable.getName());
            log.debug("  Class    [{}]", querytable.getClass().getName());
            log.debug("  Class    [{}]", querytable.getDBLink().getClass().getName());
            log.debug("  Table    [{}]", querytable.getTableName());
            log.debug("  Schema   [{}]", querytable.getSchemaName());
            log.debug("  Catalog  [{}]", querytable.getCatalogName());
            log.debug("  FullName [{}]", querytable.getFullTableName());
            log.debug(" ADQL --------- ");
            log.debug("  Table    [{}]", querytable.getDBLink().getADQLName());
            log.debug("  Schema   [{}]", querytable.getDBLink().getADQLSchemaName());
            log.debug("  Catalog  [{}]", querytable.getDBLink().getADQLCatalogName());
            log.debug(" JDBC --------- ");
            log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
            log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
            log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());

            if (querytable.getDBLink() instanceof AdqlDBTable)
                {
                AdqlDBTable table = (AdqlDBTable) querytable.getDBLink(); 
                //
                // Add the table resource to our set.
                resources.add(
                    table.table().ogsa().resource()
                    );

                if (table.mode() == Mode.ALIASED)
                    {
                    TuesdayBaseTable<?,?> mapped = this.resolve(
                        querytable
                        );
                    log.debug(" Mode.ALIASED --------- ");
                    log.debug("  Class    [{}]", mapped.getClass().getName());
                    log.debug("  Alias    [{}]", mapped.alias());
                    log.debug("  Link     [{}]", mapped.link());
                    log.debug("  FullName [{}]", mapped.fullname());
                    log.debug("  Resource [{}]", mapped.resource().name());
                    }
                else {
                    log.debug(" Mode.DIRECT --------- ");
                    log.debug("  Table    [{}]", querytable.getDBLink().getDBName());
                    log.debug("  Schema   [{}]", querytable.getDBLink().getDBSchemaName());
                    log.debug("  Catalog  [{}]", querytable.getDBLink().getDBCatalogName());
                    }
                }
            else {
                log.warn("DBLink is not instanceof AdqlDBTable");
                log.debug("  DBLink [{}]", querytable.getDBLink().getClass().getName());
                }
            }
    
        log.debug("Resources ----");
        for (TuesdayOgsaResource<?> resource : resources)
            {
            log.debug("Resource [{}][{}][{}]", resource.ident(), resource.ogsaid(), resource.name());
            }
        if (resources.size() == 1)
            {
            log.debug("Single resource, use direct");
            }
        else {
            log.debug("Multiple resource, use DQP");
            }
        
        ADQLTranslator translator = new PostgreSQLTranslator(false);
        log.debug("ADQL [{}]", query.toADQL());
        this.mode(Mode.DIRECT);
        log.debug("DIRECT SQL : {}", translator.translate(query));
        this.mode(Mode.ALIASED);
        log.debug("ALIASED SQL : {}", translator.translate(query));
          
        }
    }

