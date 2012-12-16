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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayBaseTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaResource;
import adql.query.from.ADQLTable;

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
            "adql_twomass"
            ); 
        //
        // Create our query.
        TuesdayAdqlQuery query = factories().adql().queries().create(
            workspace,
            IMPORTED_000
            );        
        //
        // Parse the query ...
        query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (TuesdayAdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (TuesdayAdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (TuesdayOgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.ogsa());
        }

    private static final String IMPORTED_001 =

          "SELECT"
        + "    twomass_scn.date,"
        + "    twomass_scn.scan,"
        + "    twomass_scn.tile,"
        + "    twomass_psc.ra || ' - ' || twomass_psc.dec as \"Position\""
        + " FROM"
        + "    adql_twomass.twomass_psc,"
        + "    adql_twomass.twomass_scn"
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
            "adql_twomass"
            ); 
        //
        // Create our query.
        TuesdayAdqlQuery query = factories().adql().queries().create(
            workspace,
            IMPORTED_001
            );        
        //
        // Parse the query ...
        query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (TuesdayAdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (TuesdayAdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (TuesdayOgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.ogsa());
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
        // Import the JdbcSchema into our AdqlWorkspace.
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
        // Create our query.
        TuesdayAdqlQuery query = factories().adql().queries().create(
            workspace,
            IMPORTED_002
            );        
        //
        // Parse the query ...
        query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (TuesdayAdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (TuesdayAdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (TuesdayOgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.ogsa());
        }


    /**
     * Process the tree of query objects.
     *
    public void process(final ADQLObject source, final Set<TuesdayOgsaResource<?>> resources, final Set<TuesdayAdqlTable> tables , final Set<TuesdayAdqlColumn> columns)
        {
        Iterable<ADQLObject> iter = new Iterable<ADQLObject>()
            {
            @Override
            public Iterator<ADQLObject> iterator()
                {
                return source.adqlIterator();
                }
            }; 
        
        for (ADQLObject object: iter)
            {
            log.debug("----");
            log.debug("ADQLObject [{}]", object.getClass().getName());

            if (object instanceof ADQLColumn)
                {
                log.debug("  ----");
                log.debug("  ADQLColumn [{}]", ((ADQLColumn) object).getName());
                if (((ADQLColumn) object).getDBLink() instanceof AdqlDBColumn)
                    {
                    TuesdayAdqlColumn adql = ((AdqlDBColumn) ((ADQLColumn) object).getDBLink()).column();
                    log.debug("  ----");
                    log.debug("  TuesdayAdqlColumn [{}]", adql.fullname());
                    log.debug("  TuesdayBaseColumn [{}]", adql.base().fullname());
                    columns.add(
                        adql
                        );                    
                    tables.add(
                        adql.table()
                        );
                    resources.add(
                        adql.ogsa().resource()
                        );
                    }
                continue;
                }

            if (object instanceof ADQLTable)
                {
                log.debug("  ----");
                log.debug("  ADQLTable [{}]", ((ADQLTable) object).getName());
                if (((ADQLTable) object).getDBLink() instanceof AdqlDBTable)
                    {
                    TuesdayAdqlTable adql = ((AdqlDBTable) ((ADQLTable) object).getDBLink()).table();
                    log.debug("  ----");
                    log.debug("  TuesdayAdqlTable [{}]", adql.fullname());
                    log.debug("  TuesdayBaseTable [{}]", adql.base().fullname());
                    tables.add(
                        adql
                        );
                    resources.add(
                        adql.ogsa().resource()
                        );
                    }
                continue;
                }

            process(
                object,
                resources,
                tables,
                columns
                );
            }
        }
     */
    }

