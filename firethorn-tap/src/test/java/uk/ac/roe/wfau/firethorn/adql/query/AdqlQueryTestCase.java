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
package uk.ac.roe.wfau.firethorn.adql.query ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;
import adql.query.from.ADQLTable;


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
    public BaseTable<?,?> resolve(final ADQLTable querytable)
    throws Exception
        {
        return factories().base().tables().resolve(
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
        final JdbcResource twomass = factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeTWOMASS"
            );
        //
        // Assign the OGSA resource ID.
        twomass.ogsaid(
            "twomass"
            );
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import from our JDBC resource into our ADQL workspace.
        twomass.inport();
        workspace.schemas().inport(
            twomass.schemas().select("TWOMASS.dbo"),
            "test_schema"
            );
        //
        // Create our query.
        final AdqlQuery query = workspace.queries().create(
            IMPORTED_000
            );
        //
        // Parse the query ...
        //query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (final OgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Connects -- ");
        for (final String connect : query.connects())
            {
            log.debug("Connect [{}]", connect);
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.osql());
        }

    private static final String IMPORTED_001 =

          "SELECT"
        + "    twomass_scn.date,"
        + "    twomass_scn.scan,"
        + "    twomass_scn.tile,"
        + "    twomass_psc.ra || ' - ' || twomass_psc.dec as \"Position\""
        + " FROM"
        + "    twomass.twomass_psc,"
        + "    twomass.twomass_scn"
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
        final JdbcResource twomass = factories().jdbc().resources().create(
            "test-resource",
            "spring:RoeTWOMASS"
            );
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Assign the OGSA resource ID.
        twomass.ogsaid(
            "twomass"
            );
        //
        // Import a JdbcSchema into our AdqlWorkspace.
        twomass.inport();
        workspace.schemas().inport(
            twomass.schemas().select("TWOMASS.dbo"),
            "twomass"
            );
        //
        // Create our query.
        final AdqlQuery query = workspace.queries().create(
            IMPORTED_001
            );
        //
        // Parse the query ...
        //query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (final OgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Connects -- ");
        for (final String connect : query.connects())
            {
            log.debug("Connect [{}]", connect);
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.osql());
        }

    private static final String IMPORTED_002 =

          "SELECT"
        + "    scn.date,"
        + "    scn.scan,"
        + "    scn.tile,"
        + "    psc.ra || ' - ' || psc.dec as \"Position\""
        + " FROM"
        + "    twomass.twomass_psc AS psc,"
        + "    twomass.twomass_scn AS scn,"
        + "    twomass.twomass_pscXBestDR7PhotoObjAll AS match,"
        + "    bestdr7.PhotoObjAll AS photo"
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
        final JdbcResource twomass = factories().jdbc().resources().create(
            "twomass",
            "spring:RoeTWOMASS"
            );
        final JdbcResource twoxmm = factories().jdbc().resources().create(
            "twoxmm",
            "spring:RoeTWOXMM"
            );
        final JdbcResource bestdr7  = factories().jdbc().resources().create(
            "bestdr7",
            "spring:RoeBestDR7"
            );
        //
        // Assign the OGSA resource IDs.
        twomass.ogsaid(
            "twomass"
            );
        twoxmm.ogsaid(
            "twoxmm"
            );
        bestdr7.ogsaid(
            "bestdr7"
            );
        //
        // Import the metadata.
        twomass.inport();
        twoxmm.inport();
        bestdr7.inport();
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import the JdbcSchema into our AdqlWorkspace.
        workspace.schemas().inport(
            twomass.schemas().select(
                "TWOMASS.dbo"
                ),
            "twomass"
            );
        workspace.schemas().inport(
            twoxmm.schemas().select(
                "TWOXMM.dbo"
                ),
            "twoxmm"
            );
        workspace.schemas().inport(
            bestdr7.schemas().select(
                "BestDR7.dbo"
                ),
            "bestdr7"
            );
        //n
        // Create our query.
        final AdqlQuery query = workspace.queries().create(
            IMPORTED_002
            );
        //
        // Parse the query ...
        //query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (final OgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Connects -- ");
        for (final String connect : query.connects())
            {
            log.debug("Connect [{}]", connect);
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.osql());
        }

    @Test
    public void testImported003()
    throws Exception
        {
        //
        // Create our JDBC resources.
        final JdbcResource twomass = factories().jdbc().resources().create(
            "twomass",
            "spring:RoeTWOMASS"
            );
        final JdbcResource twoxmm = factories().jdbc().resources().create(
            "twoxmm",
            "spring:RoeTWOXMM"
            );
        final JdbcResource bestdr7  = factories().jdbc().resources().create(
            "bestdr7",
            "spring:RoeBestDR7"
            );
        //
        // Assign the OGSA resource IDs.
        twomass.ogsaid(
            "shared"
            );
        twoxmm.ogsaid(
            "shared"
            );
        bestdr7.ogsaid(
            "shared"
            );
        //
        // Import the metadata.
        twomass.inport();
        twoxmm.inport();
        bestdr7.inport();
        //
        // Re-assign the JDBC table resources.
/*        
        for (final JdbcSchema schema : twoxmm.schemas().select())
            {
            log.debug("Relocating schema [{}]", schema.name());
            ((JdbcSchemaEntity)schema).resource(
                twomass
                );
            }
        for (final JdbcSchema schema : bestdr7.schemas().select())
            {
            log.debug("Relocating schema [{}]", schema.name());
            ((JdbcSchemaEntity)schema).resource(
                twomass
                );
            }
        factories().hibernate().flush();
 */            
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "test-workspace"
            );
        //
        // Import the JdbcSchema into our AdqlWorkspace.
        workspace.schemas().inport(
            twomass.schemas().select(
                "TWOMASS.dbo"
                ),
            "twomass"
            );
        workspace.schemas().inport(
            twoxmm.schemas().select(
                "TWOXMM.dbo"
                ),
            "twoxmm"
            );
        workspace.schemas().inport(
            bestdr7.schemas().select(
                "BestDR7.dbo"
                ),
            "bestdr7"
            );
        //
        // Create our query.
        final AdqlQuery query = workspace.queries().create(
            IMPORTED_002
            );
        //
        // Parse the query ...
        //query.parse();
        //
        // Check the results ...
        log.debug("Columns -- ");
        for (final AdqlColumn column : query.columns())
            {
            log.debug("Column [{}]", column.fullname());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.fullname());
            }
        log.debug("Resources -- ");
        for (final OgsaResource<?> resource : query.resources())
            {
            log.debug("Resource [{}]", resource.fullname());
            }
        log.debug("Connects -- ");
        for (final String connect : query.connects())
            {
            log.debug("Connect [{}]", connect);
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OGSA   [{}]", query.osql());
        }
    }

