/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class UserQueriesTestCase
    extends AtlasQueryTestBase
    {

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/315
     *
     */
    @Test
    public void test0101()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "SELECT\n" + 
            "    atlas.upetromag as upetromag,\n" + 
            "    atlas.gpetromag as gpetromag,\n" + 
            "    atlas.rpetromag as rpetromag,\n" + 
            "    atlas.ipetromag as ipetromag,\n" + 
            "    atlas.zpetromag as zpetromag,\n" + 
            "    photoobj.petromag_u ,\n" + 
            "    photoobj.petromag_g ,\n" + 
            "    photoobj.petromag_r ,\n" + 
            "    photoobj.petromag_i ,\n" + 
            "    photoobj.petromag_z\n" + 
            "FROM\n" + 
            "    atlassource as atlas,\n" + 
            "    bestdr9..photoobj as photoobj,\n" + 
            "    bestdr9..photozrf as photozrf,\n" + 
            "    atlassourcexdr9photoobj as X\n" + 
            "WHERE\n" + 
            "    X.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    photozrf.objid = photoobj.objid\n" + 
            "AND\n" + 
            "    photozrf.objid = X.slaveobjid\n" + 
            "AND\n" + 
            "    atlas.gpetromag > 0 and atlas.rpetromag > 0 and atlas.ipetromag > 0 and atlas.zpetromag > 0\n" + 
            "AND\n" + 
            "    gpperrbits = 0 AND rpperrbits = 0 AND ipperrbits = 0 AND zpperrbits = 0\n" + 
            "AND\n" + 
            "    photoobj.clean = 1\n" + 
            "AND\n" + 
            "    atlas.mergedclass = 1\n" + 
            "AND\n" + 
            "    X.distanceMins < 1/60.0\n" + 
            "AND\n" + 
            "    X.sdssprimary = 1\n" + 
            "AND\n" + 
            "    X.sdsstype = 3\n" + 
            "AND\n" + 
            "    X.distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN (distanceMins)\n" + 
            "        FROM\n" + 
            "            atlassourcexdr9photoobj\n" + 
            "        WHERE\n" + 
            "            masterobjid = X.masterobjid\n" + 
            "        )",

            "select\n" + 
            "    atlas.upetromag as upetromag,\n" + 
            "    atlas.gpetromag as gpetromag,\n" + 
            "    atlas.rpetromag as rpetromag,\n" + 
            "    atlas.ipetromag as ipetromag,\n" + 
            "    atlas.zpetromag as zpetromag,\n" + 
            "    photoobj.petromag_u as petromag_u,\n" + 
            "    photoobj.petromag_g as petromag_g,\n" + 
            "    photoobj.petromag_r as petromag_r,\n" + 
            "    photoobj.petromag_i as petromag_i,\n" + 
            "    photoobj.petromag_z as petromag_z\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlassource as atlas,\n" + 
            "    bestdr9.dbo.photoobj as photoobj,\n" + 
            "    bestdr9.dbo.photozrf as photozrf,\n" + 
            "    atlasdr1.dbo.atlassourcexdr9photoobj as x\n" + 
            "where\n" + 
            "    x.masterobjid = atlas.sourceid\n" + 
            "and\n" + 
            "    photozrf.objid = photoobj.objid\n" + 
            "and\n" + 
            "    photozrf.objid = x.slaveobjid\n" + 
            "and\n" + 
            "    atlas.gpetromag > 0\n" + 
            "and\n" + 
            "    atlas.rpetromag > 0\n" + 
            "and\n" + 
            "    atlas.ipetromag > 0\n" + 
            "and\n" + 
            "    atlas.zpetromag > 0\n" + 
            "and\n" + 
            "    atlas.gpperrbits = 0\n" + 
            "and\n" + 
            "    atlas.rpperrbits = 0\n" + 
            "and\n" + 
            "    atlas.ipperrbits = 0\n" + 
            "and\n" + 
            "    atlas.zpperrbits = 0\n" + 
            "and\n" + 
            "    photoobj.clean = 1\n" + 
            "and\n" + 
            "    atlas.mergedclass = 1\n" + 
            "and\n" + 
            "    x.distancemins < 1 / 60.0\n" + 
            "and\n" + 
            "    x.sdssprimary = 1\n" + 
            "and\n" + 
            "    x.sdsstype = 3\n" + 
            "and\n" + 
            "    x.distancemins in (\n" + 
            "        select\n" + 
            "            min(atlasdr1.dbo.atlassourcexdr9photoobj.distancemins)as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj.masterobjid = x.masterobjid\n" + 
            "        )"
/*
            new ExpectedField[] {
                new ExpectedField("", AdqlColumn.Type.FLOAT, 0),
                }
*/                
            );
        }
    
    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/316
     *
     */
    @Test
    public void test0201()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "",
            ""
            );
        }

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/317
     *
     */
    @Test
    public void test0301()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "",
            ""
            );
        }

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/319
     *
     */
    @Test
    public void test0401()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "",
            ""
            );
        }

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/320
     *
     */
    @Test
    public void test0501()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "",
            ""
            );
        }

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/321
     *
     */
    @Test
    public void test0601()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "",
            ""
            );
        }
    }
