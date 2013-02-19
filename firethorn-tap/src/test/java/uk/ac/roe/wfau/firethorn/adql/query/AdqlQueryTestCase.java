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

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 *
 */
@Slf4j
public class AdqlQueryTestCase
extends TestBase
    {

    private JdbcResource twomass ;
    private JdbcResource twoxmm  ;
    private JdbcResource bestdr7 ;
    private AdqlResource workspace; 

    /**
     * Create our resources.
     *  
     */
    @Before
    public void init()
        {
        log.debug(" twomass [{}]", twomass);
        log.debug(" twoxmm  [{}]", twoxmm);
        log.debug(" bestdr7 [{}]", bestdr7);
        //
        // Create our JDBC resources and import the metadata.
        // TODO -should the import be automatic?
        if (twomass == null)
            {
            twomass = factories().jdbc().resources().create(
                "twomass-resource",
                "spring:RoeTWOMASS"
                );
            twomass.inport();
            }
        if (twoxmm == null)
            {
            twoxmm = factories().jdbc().resources().create(
                "twoxmm-resource",
                "spring:RoeTWOXMM"
                );
            twoxmm.inport();
            }
        if (bestdr7 == null)
            {
            bestdr7 = factories().jdbc().resources().create(
                "bestdr7-resource",
                "spring:RoeBestDR7"
                );
            bestdr7.inport();
            }
        //
        // Create our ADQL workspace.
        this.workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the some of the JDBC tables into our ADQL workspace.
        this.workspace.schemas().inport(
            twomass.schemas().select(
                "TWOMASS.dbo"
                ),
            "adql_twomass"
            );
        this.workspace.schemas().inport(
            twoxmm.schemas().select(
                "TWOXMM.dbo"
                ),
            "adql_twoxmm"
            );
        this.workspace.schemas().inport(
            bestdr7.schemas().select(
                "BestDR7.dbo"
                ),
            "adql_bestdr7"
            );
        }
    
    /**
     * Create an AdqlQuery and display the results.
     * 
     */
    public AdqlQuery query(final String input)
        {
        return this.workspace.queries().create(
            input
            );
        }

    /**
     * Debug display of a query.
     *
     */
    public void debug(final AdqlQuery query)
        {
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
        for (final BaseResource<?> resource : query.resources())
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
        log.debug("OSQL   [{}]", query.osql());
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
        + "    adql_twomass.twomass_psc"
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
        // Assign *different* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-twomass"
            );
        twoxmm.ogsaid(
            "ogsa-twoxmm"
            );
        bestdr7.ogsaid(
            "ogsa-bestdr7"
            );
        //
        // Parse the query and check the results.
        final AdqlQuery query = this.query(
            IMPORTED_000
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
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
        // Assign *different* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-twomass"
            );
        twoxmm.ogsaid(
            "ogsa-twoxmm"
            );
        bestdr7.ogsaid(
            "ogsa-bestdr7"
            );
        //
        // Parse the query and check the results.
        final AdqlQuery query = this.query(
            IMPORTED_001
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
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
        // Assign *different* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-twomass"
            );
        twoxmm.ogsaid(
            "ogsa-twoxmm"
            );
        bestdr7.ogsaid(
            "ogsa-bestdr7"
            );
        //
        // Parse the query and check the results.
        final AdqlQuery query = this.query(
            IMPORTED_002
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DISTRIBUTED,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        }

    @Test
    public void testImported003()
    throws Exception
        {
        //
        // Assign the *same* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-shared"
            );
        twoxmm.ogsaid(
            "ogsa-shared"
            );
        bestdr7.ogsaid(
            "ogsa-shared"
            );
        //
        // Parse the query and check the results.
        final AdqlQuery query = this.query(
            IMPORTED_002
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        }

    @Test
    public void testImported004()
    throws Exception
        {
        //
        // Assign *different* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-twomass"
            );
        twoxmm.ogsaid(
            "ogsa-twoxmm"
            );
        bestdr7.ogsaid(
            "ogsa-bestdr7"
            );
        //
        // Parse the query and display the results.
        final AdqlQuery query = this.query(
            IMPORTED_002
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DISTRIBUTED,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        //
        // Assign the *same* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-shared"
            );
        twoxmm.ogsaid(
            "ogsa-shared"
            );
        bestdr7.ogsaid(
            "ogsa-shared"
            );
        //
        // Re-process the same query and check the results.
        query.input(
            null
            );
        query.input(
            IMPORTED_002
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        //
        // Assign *different* OGSA resource IDs.
        twomass.ogsaid(
            "ogsa-twomass"
            );
        twoxmm.ogsaid(
            "ogsa-twoxmm"
            );
        bestdr7.ogsaid(
            "ogsa-bestdr7"
            );
        //
        // Re-process the same query and check the results.
        query.input(
            null
            );
        query.input(
            IMPORTED_002
            );
        assertEquals(
            AdqlQuery.Status.EDITING,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DISTRIBUTED,
            query.mode()
            );
        assertEquals(
            AdqlQuerySyntax.Status.VALID,
            query.syntax().status()
            );
        assertIsNull(
            query.syntax().error()
            );
        }
    }

