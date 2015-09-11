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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class QueryBugsTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Distance is a reserved word in ADQL.
     * " SELECT neighbours.distanceMins AS distance"
     *
     */
    @Test
    public void test002S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    distanceMins AS distance" +
            " FROM" +
            "    atlasSourceXtwomass_psc"
            );
        }

    /**
     * Distance is a reserved word in ADQL.
     * " SELECT neighbours.distanceMins AS distance"
     *
     */
    @Test
    public void test002L()
    throws QueryProcessingException
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    distanceMins AS distance" +
            " FROM" +
            "    atlasSourceXtwomass_psc",

            " SELECT TOP 10" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.distancemins AS dist" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc",

            new ExpectedField[] {
                new ExpectedField("dist", AdqlColumn.AdqlType.FLOAT, 0),
                }
            );

        assertEquals(
            "DISTANCE is an ADQL reserved word",
            query.syntax().warnings().iterator().next()
            );
        }

    /**
     * SQLServer '..' syntax to access default schema.
     * " FROM TWOMASS..twomass_psc"
     *
     */
    @Test
    public void test003S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    TWOMASS..twomass_psc"
            );

        // TODO Explicitly state LEGACY not available in warning.
        // TODO Change from a string match to a token in the parser grammar.

        }

    /**
     * SQLServer '..' syntax to access default schema.
     * " FROM TWOMASS..twomass_psc"
     *
     */
    @Test
    public void test003L()
    throws QueryProcessingException
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    TWOMASS..twomass_psc",

            " SELECT TOP 10" +
            "    twomass.dbo.twomass_psc.ra  AS ra," +
            "    twomass.dbo.twomass_psc.dec AS dec" +
            " FROM" +
            "    twomass.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );

        assertEquals(
            "SQLServer '..' syntax is not required",
            query.syntax().warnings().iterator().next()
            );
        }

    /**
     * Duplicate column names.
     * " SELECT atlas.ra, rosat.ra"
     *
     */
    @Test
    public void test004S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec," +
            "    rosat.ra," +
            "    rosat.dec" +
            " FROM" +
            "    atlasSource AS atlas," +
            "    rosat_fsc AS rosat," +
            "    atlasSourceXrosat_fsc AS neighbours" +
            " WHERE" +
            "    neighbours.masterObjID=atlas.sourceID" +
            " AND" +
            "    neighbours.slaveObjID=rosat.seqNo"
            );
        }

    /**
     * Duplicate column names - do we rename if possible ?
     * " SELECT atlas.ra, rosat.ra"
     *
     */
    @Test
    public void test004L()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec," +
            "    rosat.ra," +
            "    rosat.dec" +
            " FROM" +
            "    atlasSource AS atlas," +
            "    rosat_fsc AS rosat," +
            "    atlasSourceXrosat_fsc AS neighbours" +
            " WHERE" +
            "    neighbours.masterObjID=atlas.sourceID" +
            " AND" +
            "    neighbours.slaveObjID=rosat.seqNo"
            );
        }

    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     *
     */
    @Test
    public void test005S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec" +
            " FROM" +
            "    atlasSource AS atlas" +
            " WHERE" +
            "    atlas.sourceID % 100 = 0"
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
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec" +
            " FROM" +
            "    atlasSource AS atlas" +
            " WHERE" +
            "    atlas.sourceID % 100 = 0",

            " SELECT TOP 10" +
            "    atlas.ra  AS ra," +
            "    atlas.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource AS atlas" +
            " WHERE" +
            "    atlas.sourceID % 100 = 0",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0),
                }
            );
        }


    /**
     * Inner WHERE clause refers to table defined in outer FROM clause.
     * " WHERE masterObjID = neighbours.masterObjID"
     *
     */
    @Test
    public void test006L()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec" +
            " FROM" +
            "    atlasSource AS atlas," +
            "    twomass_psc AS twomass," +
            "    atlasSourceXtwomass_psc AS neighbours" +
            " WHERE" +
            "    masterObjID=atlas.sourceID" +
            " AND" +
            "    slaveObjID=twomass.pts_key" +
            " AND" +
            "    distanceMins IN (" +
            "        SELECT" +
            "            MIN(distanceMins)" +
            "        FROM" +
            "            atlasSourceXtwomass_psc" +
            "        WHERE" +
            "            masterObjID = neighbours.masterObjID" +
            "        )",

            " SELECT TOP 10" +
            "    atlas.ra AS ra," +
            "    atlas.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource AS atlas," +
            "    twomass.dbo.twomass_psc AS twomass," +
            "    {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc AS neighbours" +
            " WHERE" +
            "    neighbours.masterObjID = atlas.sourceID" +
            " AND" +
            "    neighbours.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbours.distancemins IN" +
            "        (" +
            "        SELECT" +
            "            MIN({ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.distancemins) AS min" +
            "        FROM" +
            "            {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc" +
            "        WHERE" +
            "            {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.masterObjID = neighbours.masterObjID" +
            "        )",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * Simple nested query.
     *
     */
    @Test
    public void test006a()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    atlas.ra," +
            "    atlas.dec" +
            " FROM" +
            "    atlasSource AS atlas," +
            "    twomass_psc AS twomass," +
            "    atlasSourceXtwomass_psc AS neighbours" +
            " WHERE" +
            "    masterObjID=atlas.sourceID" +
            " AND" +
            "    slaveObjID=twomass.pts_key" +
            " AND" +
            "    distanceMins IN (" +
            "        SELECT" +
            "            distanceMins" +
            "        FROM" +
            "            atlasSourceXtwomass_psc" +
            "        WHERE" +
            "            distanceMins < 0.01" +
            "        )",

            " SELECT TOP 10" +
            "    atlas.ra  AS ra," +
            "    atlas.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource AS atlas," +
            "    twomass.dbo.twomass_psc AS twomass," +
            "    {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc AS neighbours" +
            " WHERE" +
            "    neighbours.masterObjID = atlas.sourceID" +
            " AND" +
            "    neighbours.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbours.distancemins IN" +
            "        (" +
            "        SELECT" +
            "            {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.distancemins AS distancemins" +
            "        FROM" +
            "            {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc" +
            "        WHERE" +
            "            {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.distancemins < 0.01" +
            "        )",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    /**
     * DATETIME column.
     *
     */
    @Test
    public void test007L()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    utDate," +
            "    dateObs" +
            " FROM" +
            "    Multiframe",

            " SELECT TOP 10" +
            "    {ATLAS_VERSION}.dbo.Multiframe.utDate  AS utDate," +
            "    {ATLAS_VERSION}.dbo.Multiframe.dateObs AS dateObs" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.Multiframe",

            new ExpectedField[] {
                new ExpectedField("utDate",  AdqlColumn.AdqlType.DATETIME, 0),
                new ExpectedField("dateObs", AdqlColumn.AdqlType.DATETIME, 0)
                }
            );

        // TODO - verify the JDBC column is DATE TIME, not TIMESTAMP.
        // Firethorn create table fails if we have two DATETIME columns.
        // OGSA-DAI insert into TIMESTAMP column fails.

        }

    /**
     * Alias for nested query in SELECT.
     * SELECT ... FROM (SELECT .... AS ident ...) AS inner WHERE inner.ident = outer.field
     *
     */
    @Test
    public void test008L()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 100" +
            "    neighbours.distanceMins" +
            " FROM" +
            "    atlasSourceXDR8PhotoObj AS neighbours," +
            "    (" +
            "    SELECT TOP 10" +
            "        sourceID AS ident" +
            "    FROM" +
            "        atlasSource" +
            "    ) AS sources" +
            " WHERE" +
            "    neighbours.masterObjID - sources.ident < 1000000",

            " SELECT TOP 100" +
            "    neighbours.distanceMins AS distanceMins" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR8PhotoObj AS neighbours," +
            "    (" +
            "    SELECT TOP 10" +
            "        {ATLAS_VERSION}.dbo.atlasSource.sourceID AS ident" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlasSource" +
            "    ) AS sources" +
            " WHERE" +
            "    neighbours.masterObjID - sources.ident < 1000000",

            new ExpectedField[] {
                new ExpectedField("distanceMins", AdqlColumn.AdqlType.FLOAT, 0),
                }
            );
        }

    /**
     * Single quoted string in WHERE clause.
     *
     */
    @Test
    public void test009a()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    COUNT(*)" +
            " FROM" +
            "    Multiframe" +
            " WHERE" +
            "    project LIKE 'ATLAS%'",

            " SELECT" +
            "    COUNT(*) AS COUNT_ALL" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.multiframe" +
            " WHERE" +
            "    {ATLAS_VERSION}.dbo.multiframe.project LIKE 'ATLAS%'",

            new ExpectedField[] {
                new ExpectedField("COUNT_ALL", AdqlColumn.AdqlType.LONG, 0),
                }
            );
        }

    /**
     * Double quoted string in WHERE clause.
     *
     */
    @Test
    public void test009b()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT" +
            "    COUNT(*)" +
            " FROM" +
            "    Multiframe" +
            " WHERE" +
            "    project LIKE \"ATLAS%\""
            );
        }

    /**
     * Negative value for select expression.
     *
     */
    @Test
    public void test010()
    throws QueryProcessingException
        {
        validate(
            Level.LEGACY,
            State.VALID,

        	" SELECT" +
        	"    -decBase" +
        	" FROM" +
        	"    Multiframe" +
        	" WHERE" +
        	"    MultiframeID > 0",

        	" SELECT" +
        	"    -{ATLAS_VERSION}.dbo.multiframe.decbase AS decbase" +
        	" FROM" +
        	"    {ATLAS_VERSION}.dbo.multiframe" +
        	" WHERE" +
        	"    {ATLAS_VERSION}.dbo.multiframe.multiframeid > 0",

            new ExpectedField[] {
                new ExpectedField("decBase", AdqlColumn.AdqlType.FLOAT, 0),
                }
            );
        }
    }
