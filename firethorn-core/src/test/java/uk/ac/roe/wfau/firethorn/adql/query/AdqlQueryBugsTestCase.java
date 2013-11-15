/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query;

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.QueryParam;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.QuerySelectFieldTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
@Slf4j
public class AdqlQueryBugsTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Distance is a reserved word in ADQL.
     * "SELECT neighbours.distanceMins AS distance"
     *
     */
    @Test
    public void test002S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" +
            "    distanceMins AS distance\n" +
            "FROM\n" +
            "    atlasSourceXtwomass_psc\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * Distance is a reserved word in ADQL.
     * "SELECT neighbours.distanceMins AS distance"
     *
     */
    @Test
    public void test002L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    distanceMins AS distance\n" +
            "FROM\n" +
            "    atlasSourceXtwomass_psc\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertEquals(
            "DISTANCE is an ADQL reserved word",
            query.syntax().warnings().iterator().next()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("dist", AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select top 10 atlasv20131029.dbo.atlassourcextwomass_psc.distancemins as dist from atlasv20131029.dbo.atlassourcextwomass_psc"
            );
        }

    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     *
     */
    @Test
    public void test003S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" +
            "    ra,\n" +
            "    dec\n" +
            "FROM\n" +
            "    TWOMASS..twomass_psc\n" +
            ""
            );

        assertEquals(
            AdqlQuery.Syntax.Level.STRICT,
            query.syntax().level()
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );

        // TODO Explicitly state LEGACY not available in warning.
        // TODO Change from a string match to a token in the parser grammar.

        }

    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     *
     */
    @Test
    public void test003L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    ra,\n" +
            "    dec\n" +
            "FROM\n" +
            "    TWOMASS..twomass_psc\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        assertEquals(
            "SQLServer '..' syntax is not required",
            query.syntax().warnings().iterator().next()
            );
        validate(
            query,
            new ExpectedField[] {
            new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
            new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        compare(
            query,
            "select top 10 twomass.dbo.twomass_psc.ra as ra, twomass.dbo.twomass_psc.dec as dec from twomass.dbo.twomass_psc"
            );
        }

    /**
     * Duplicate column names.
     * "SELECT atlas.ra, rosat.ra"
     *
     */
    @Test
    public void test004S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec,\n" +
            "    rosat.ra,\n" +
            "    rosat.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas,\n" +
            "    ROSAT.rosat_fsc AS rosat,\n" +
            "    atlasSourceXrosat_fsc AS neighbours\n" +
            "WHERE\n" +
            "    neighbours.masterObjID=atlas.sourceID\n" +
            "AND\n" +
            "    neighbours.slaveObjID=rosat.seqNo\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * Duplicate column names - do we rename if possible ?
     * "SELECT atlas.ra, rosat.ra"
     *
     */
    @Test
    public void test004L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec,\n" +
            "    rosat.ra,\n" +
            "    rosat.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas,\n" +
            "    ROSAT.rosat_fsc AS rosat,\n" +
            "    atlasSourceXrosat_fsc AS neighbours\n" +
            "WHERE\n" +
            "    neighbours.masterObjID=atlas.sourceID\n" +
            "AND\n" +
            "    neighbours.slaveObjID=rosat.seqNo\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     *
     */
    @Test
    public void test005S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            "WHERE\n" +
            "    atlas.sourceID % 100 = 0\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        // TODO Check the error message.
        }

    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     *
     */
    @Test
    public void test005L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            "WHERE\n" +
            "    atlas.sourceID % 100 = 0\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        compare(
            query,
            "select top 10 atlas.ra as ra, atlas.dec as dec from atlasv20131029.dbo.atlassource as atlas where atlas.sourceid%100 = 0"
            );
        }


    /**
     * Inner WHERE clause refers to table defined in outer FROM clause.
     * "WHERE masterObjID = neighbours.masterObjID"
     *
     */
    @Test
    public void test006L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas,\n" +
            "    TWOMASS.twomass_psc AS twomass,\n" +
            "    atlasSourceXtwomass_psc AS neighbours\n" +
            "WHERE\n" +
            "    masterObjID=atlas.sourceID\n" +
            "AND\n" +
            "    slaveObjID=twomass.pts_key\n" +
            "AND\n" +
            "    distanceMins IN (\n" +
            "        SELECT\n" +
            "            MIN(distanceMins)\n" +
            "        FROM\n" +
            "            atlasSourceXtwomass_psc\n" +
            "        WHERE\n" +
            "            masterObjID = neighbours.masterObjID\n" +
            "        )\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                //new ExpectedField("MIN", AdqlColumn.Type.FLOAT, 0),
                }
            );
        // TODO
        compare(
            query,
            "select top 10 atlas.ra as ra, atlas.dec as dec from atlasv20131029.dbo.atlassource as atlas, twomass.dbo.twomass_psc as twomass, atlasv20131029.dbo.atlassourcextwomass_psc as neighbours where neighbours.masterobjid = atlas.sourceid and neighbours.slaveobjid = twomass.pts_key and neighbours.distancemins in (select min(atlasv20131029.dbo.atlassourcextwomass_psc.distancemins) as min from atlasv20131029.dbo.atlassourcextwomass_psc where atlasv20131029.dbo.atlassourcextwomass_psc.masterobjid = neighbours.masterobjid)"
            );
        }

    /**
     * Simple nested query.
     *
     */
    @Test
    public void test006a()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    atlas.ra,\n" +
            "    atlas.dec\n" +
            "FROM\n" +
            "    atlasSource AS atlas,\n" +
            "    TWOMASS.twomass_psc AS twomass,\n" +
            "    atlasSourceXtwomass_psc AS neighbours\n" +
            "WHERE\n" +
            "    masterObjID=atlas.sourceID\n" +
            "AND\n" +
            "    slaveObjID=twomass.pts_key\n" +
            "AND\n" +
            "    distanceMins IN (\n" +
            "        SELECT\n" +
            "            distanceMins\n" +
            "        FROM\n" +
            "            atlasSourceXtwomass_psc\n" +
            "        WHERE\n" +
            "            distanceMins < 0.01\n" +
            "        )\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0),
                //new ExpectedField("MIN", AdqlColumn.Type.FLOAT, 0),
                }
            );
        // TODO
        compare(
            query,
            "select top 10 atlas.ra as ra, atlas.dec as dec from atlasv20131029.dbo.atlassource as atlas, twomass.dbo.twomass_psc as twomass, atlasv20131029.dbo.atlassourcextwomass_psc as neighbours where neighbours.masterobjid = atlas.sourceid and neighbours.slaveobjid = twomass.pts_key and neighbours.distancemins in (select min(atlasv20131029.dbo.atlassourcextwomass_psc.distancemins) as min from atlasv20131029.dbo.atlassourcextwomass_psc where atlasv20131029.dbo.atlassourcextwomass_psc.masterobjid = neighbours.masterobjid)"
            );
        }
    
    /**
     * DATETIME column.
     *
     */
    @Test
    public void test007L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    utDate,\n" +
            "    dateObs\n" +
            "FROM\n" +
            "    multiframe\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("utDate",  AdqlColumn.Type.DATETIME, 23),
                new ExpectedField("dateObs", AdqlColumn.Type.DATETIME, 23)
                }
            );

        // TODO - verify the JDBC column is DATE TIME, not TIMESTAMP.
        // Firethorn create table fails if we have two DATETIME columns.
        // OGSA-DAI insert into TIMESTAMP column fails.

        compare(
            query,
            "select top 10 atlasv20131029.dbo.multiframe.utdate as utdate, atlasv20131029.dbo.multiframe.dateobs as dateobs from atlasv20131029.dbo.multiframe"
            );
        }

    /**
     * Alias for nested query in SELECT.
     * SELECT ... FROM (SELECT .... AS ident ...) AS inner WHERE inner.ident = outer.field
     *
     */
    @Test
    public void test008L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 100\n" +
            "    neighbours.distanceMins\n" +
            "FROM\n" +
            "    atlassourcexDR8photoobj AS neighbours,\n" +
            "    (\n" +
            "    SELECT TOP 10\n" +
            "        sourceId AS ident\n" +
            "    FROM\n" +
            "        atlasSource\n" +
            "    ) AS sources\n" +
            "WHERE\n" +
            "    neighbours.masterObjID - sources.ident < 1000000\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("distanceMins", AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select top 100 neighbours.distancemins as distancemins from atlasv20131029.dbo.atlassourcexdr8photoobj as neighbours, (select top 10 atlasv20131029.dbo.atlassource.sourceid as ident from atlasv20131029.dbo.atlassource) as sources where neighbours.masterobjid-sources.ident < 1000000"
            );
        }

    /**
     * Single quoted string in WHERE clause.
     *
     */
    @Test
    public void test009a()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" +
            "    COUNT(*)\n" +
            "FROM\n" +
            "    Multiframe\n" +
            "WHERE\n" +
            "    project LIKE 'ATLAS%'" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        compare(
            query,
            "select count(*) as count_all from atlasv20131029.dbo.multiframe where atlasv20131029.dbo.multiframe.project like 'ATLAS%'"
            );
        }

    /**
     * Double quoted string in WHERE clause.
     *
     */
    @Test
    public void test009b()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" +
            "    COUNT(*)\n" +
            "FROM\n" +
            "    Multiframe\n" +
            "WHERE\n" +
            "    project LIKE \"ATLAS%\"" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }
    
    
    /**
     * Negative value for select expression.
     *
     */
    @Test
    public void test010()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
        	"SELECT -decBase FROM Multiframe WHERE MultiframeID > 0"
            );
    	assertEquals(
    	    AdqlQuery.Syntax.State.VALID,
	        query.syntax().state()
            );
       
        compare(
            query,
            "select -atlasv20131029.dbo.multiframe.decbase as decbase from atlasv20131029.dbo.multiframe where atlasv20131029.dbo.multiframe.multiframeid > 0"
            );
            
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("decbase", AdqlColumn.Type.FLOAT, 0),
                }
            );
        }
    }
