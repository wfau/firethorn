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
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.QueryParam;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.QuerySelectFieldTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class AdqlQueryBugsTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Distance is a reserved word in ADQL.
     * "SELECT neighbours.distanceMins AS distance"
     * 
     */
    @Test
    public void test000()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    neighbours.distanceMins AS distance\n" + 
            "FROM\n" + 
            "    ATLASv20130426.atlasSourceXtwomass_psc as neighbours\n" + 
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
    public void test001()
        {
        
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    neighbours.distanceMins AS distance\n" + 
            "FROM\n" + 
            "    ATLASv20130426.atlasSourceXtwomass_psc as neighbours\n" + 
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
        }

    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     * 
     */
    @Test
    public void test002()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 10\n" + 
            "    twomass_psc.ra,\n" + 
            "    twomass_psc.dec\n" + 
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
        }
    
    /**
     * SQLServer '..' syntax to access default schema.
     * "FROM TWOMASS..twomass_psc"
     * 
     */
    @Test
    public void test003()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    twomass_psc.ra,\n" + 
            "    twomass_psc.dec\n" + 
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
        }

    /**
     * Duplicate column names throw Hibernate ConstraintViolationException.
     * "SELECT atlas.ra, rosat.ra"
     * 
     */
    @Test
    public void test004()
        {
        AdqlQuery query = this.queryspace.queries().create(
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
            "    ROSAT..rosat_fsc AS rosat,\n" + 
            "    atlasSourceXrosat_fsc AS neighbours\n" + 
            "WHERE\n" + 
            "    neighbours.masterObjID=atlas.sourceID\n" + 
            "AND\n" + 
            "    neighbours.slaveObjID=rosat.seqNo\n" + 
            "AND\n" + 
            "    neighbours.distanceMins < 0.1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        }

    /**
     * WHERE clause contains '%' modulus operator.
     * "atlas.sourceID % 100 = 0"
     * 
     */
    @Test
    public void test005()
        {
        AdqlQuery query = this.queryspace.queries().create(
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
        }

    /**
     * Inner WHERE clause refers to table defined in outer FROM clause.
     * "WHERE masterObjID=neighbours.masterObjID"
     * 
     */
    @Test
    public void test006()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    atlas.ra,\n" + 
            "    atlas.dec\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    BestDR8.PhotoObj AS bestdr8,\n" + 
            "    atlasSourceXDR8PhotoObj AS neighbours\n" + 
            "WHERE\n" + 
            "    masterObjID=atlas.sourceID\n" + 
            "AND\n" + 
            "    slaveObjID=bestdr8.ObjID\n" + 
            "AND\n" + 
            "    distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN(distanceMins)\n" + 
            "        FROM\n" + 
            "            atlasSourceXDR8PhotoObj\n" + 
            "        WHERE\n" + 
            "            masterObjID=neighbours.masterObjID\n" + 
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
                }
            );
        }

    /**
     * DATETIME column.
     * 
     */
    @Test
    public void test007()
        {
        AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" + 
            "    creationDate\n" + 
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
                new ExpectedField("creationDate",  AdqlColumn.Type.DATETIME, 0),
                }
            );
        }
    }
