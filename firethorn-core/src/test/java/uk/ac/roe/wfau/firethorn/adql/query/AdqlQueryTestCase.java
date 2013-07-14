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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 * @todo - NEED TO SET UP CURRENT IDENTITY WITH A JdbcResource for storage.
 *
 */
@Slf4j
public class AdqlQueryTestCase
extends TestBase
    {

    private JdbcResource twomass ;
    private JdbcResource wfau    ;

    private JdbcResource resource ;
    private JdbcSchema   schema   ;

    /**
     * Create our resources.
     *
     */
    @Before
    public void init()
        {
        //
        // Create our JDBC resources.
        this.twomass = factories().jdbc().resources().create(
            "twomass",
            "test:twomass",
            "spring:RoeTWOMASS"
            );
        this.wfau = factories().jdbc().resources().create(
            "wfau",
            JdbcResource.ALL_CATALOGS,
            "test:wfau",
            "spring:RoeWFAU"
            );

        this.resource = factories().jdbc().resources().create(
            "userdata",
            "userdata",
            "spring:FireThornUserData"
            );
// TODO do we need this ?
/*        
        this.schema = this.resource.schemas().create(
            null,
            "PUBLIC"
            );
*/            
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
            log.debug("Column [{}]", column.namebuilder());
            }
        log.debug("Tables -- ");
        for (final AdqlTable table : query.tables())
            {
            log.debug("Table [{}]", table.namebuilder());
            }
        log.debug("Resources -- ");
        for (final BaseResource<?> target : query.resources())
            {
            log.debug("Resource [{}]", target.namebuilder());
            }
        log.debug("Query -- ");
        log.debug("Mode   [{}]", query.mode());
        log.debug("Status [{}]", query.status());
        log.debug("ADQL   [{}]", query.adql());
        log.debug("OSQL   [{}]", query.osql());
        log.debug("Target [{}]", query.primary().ident());
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
        + "    ra  BETWEEN '56.0' AND '57.9'"
        + " AND"
        + "    dec BETWEEN '24.0' AND '24.2'"
        + ""
        ;

	@Test
    public void testImported000()
    throws Exception
        {
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        final AdqlSchema schema = workspace.schemas().create(
            "adql_twomass"
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        //
        // Create the query and check the results.
        final AdqlQuery query = schema.queries().create(
            IMPORTED_000
            );
        //query.prepare();
        debug(query);

        assertEquals(
            AdqlQuery.Status.READY,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertNull(
            query.syntax().message()
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
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        final AdqlSchema schema = workspace.schemas().create(
            "adql_twomass"
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_scn"
                    )
            );
        //
        // Create the query and check the results.
        final AdqlQuery query = schema.queries().create(
            IMPORTED_001
            );
        //query.prepare();
        debug(query);

        assertEquals(
            AdqlQuery.Status.READY,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertNull(
            query.syntax().message()
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
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        final AdqlSchema schema = workspace.schemas().create(
            "adql_twomass"
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_scn"
                    )
            );
        schema.tables().create(
            this.twomass.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_pscXBestDR7PhotoObjAll"
                    )
            );

        final AdqlSchema other = workspace.schemas().create(
            "adql_bestdr7"
            );
        other.tables().create(
            this.wfau.schemas().select(
                "BestDR7",
                "dbo"
                ).tables().select(
                    "PhotoObjAll"
                    )
            );
        //
        // Create the query and check the results.
        final AdqlQuery query = schema.queries().create(
            IMPORTED_002
            );
        //query.prepare();
        debug(query);

        assertEquals(
            AdqlQuery.Status.READY,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DISTRIBUTED,
            query.mode()
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertNull(
            query.syntax().message()
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertNull(
            query.syntax().message()
            );
        }

    @Test
    public void testImported003()
    throws Exception
        {
        //
        // Create our ADQL workspace.
        final AdqlResource workspace = factories().adql().resources().create(
            "adql-workspace"
            );
        //
        // Import the JDBC tables into our workspace.
        final AdqlSchema schema = workspace.schemas().create(
            "adql_twomass"
            );
        schema.tables().create(
            this.wfau.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_psc"
                    )
            );
        schema.tables().create(
            this.wfau.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_scn"
                    )
            );
        schema.tables().create(
            this.wfau.schemas().select(
                "TWOMASS",
                "dbo"
                ).tables().select(
                    "twomass_pscXBestDR7PhotoObjAll"
                    )
            );

        final AdqlSchema other = workspace.schemas().create(
            "adql_bestdr7"
            );
        other.tables().create(
            this.wfau.schemas().select(
                "BestDR7",
                "dbo"
                ).tables().select(
                    "PhotoObjAll"
                    )
            );
        //
        // Create the query and check the results.
        final AdqlQuery query = schema.queries().create(
            IMPORTED_002
            );
        //query.prepare();
        debug(query);

        assertEquals(
            AdqlQuery.Status.READY,
            query.status()
            );
        assertEquals(
            AdqlQuery.Mode.DIRECT,
            query.mode()
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertNull(
            query.syntax().message()
            );
        }
    }

