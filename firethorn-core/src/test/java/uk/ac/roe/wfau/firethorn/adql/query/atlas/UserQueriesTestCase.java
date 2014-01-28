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
            "        )",

            new ExpectedField[] {
                new ExpectedField("upetromag",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gpetromag",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("rpetromag",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("ipetromag",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("zpetromag",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_u", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_g", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_r", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_i", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_z", AdqlColumn.Type.FLOAT, 0)
                }
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

            "SELECT\n" + 
            "    TOP 10 \n" + 
            "    atlas.upetromag AS g, \n" + 
            "    photoobj.petromag_u\n" + 
            "FROM\n" + 
            "    atlassource AS atlas,\n" + 
            "    bestdr9.photoobj AS photoobj,\n" + 
            "    atlassourcexdr9photoobj AS x \n" + 
            "WHERE\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    x.slaveobjID = photoobj.objID",
            
            "select\n" + 
            "    top 10 atlas.upetromag as g,\n" + 
            "    photoobj.petromag_u as petromag_u\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlassource as atlas,\n" + 
            "    bestdr9.dbo.photoobj as photoobj,\n" + 
            "    atlasdr1.dbo.atlassourcexdr9photoobj as x\n" + 
            "where\n" + 
            "    x.masterobjid = atlas.sourceid\n" + 
            "and\n" + 
            "    x.slaveobjid = photoobj.objid",

            new ExpectedField[] {
                new ExpectedField("g",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("petromag_u", AdqlColumn.Type.FLOAT, 0)
                }
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
            "SELECT\n" + 
            "    TOP 100000\n" + 
            "    photoobj.dered_u,\n" + 
            "    photoobj.dered_g, \n" + 
            "    photoobj.dered_r,\n" + 
            "    photoobj.dered_i,\n" + 
            "    photoobj.dered_z, \n" + 
            "    photoobj.z\n" + 
            "FROM\n" + 
            "    bestdr9.photozrf AS photozrf\n" + 
            "LEFT JOIN\n" + 
            "    bestdr9.photoobj AS photoobj\n" + 
            "ON\n" + 
            "    photozrf.objID = photoobj.objID",

            "select\n" + 
            "    top 100000\n" + 
            "    photoobj.dered_u as dered_u,\n" + 
            "    photoobj.dered_g as dered_g,\n" + 
            "    photoobj.dered_r as dered_r,\n" + 
            "    photoobj.dered_i as dered_i,\n" + 
            "    photoobj.dered_z as dered_z,\n" + 
            "    photoobj.z as z\n" + 
            "from\n" + 
            "    bestdr9.dbo.photozrf as photozrf\n" + 
            "left outer join\n" + 
            "    bestdr9.dbo.photoobj as photoobj\n" + 
            "on\n" + 
            "    photozrf.objid = photoobj.objid",

            new ExpectedField[] {
                new ExpectedField("dered_u", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dered_g", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dered_r", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dered_i", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dered_z", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("z",       AdqlColumn.Type.FLOAT, 0)
                }
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
            "SELECT\n" + 
            "    TOP 5\n" + 
            "    atlas.upetromag AS upetromag,\n" + 
            "    atlas.gpetromag AS gpetromag,    \n" + 
            "    atlas.rpetromag AS rpetromag,\n" + 
            "    atlas.ipetromag AS ipetromag,\n" + 
            "    atlas.zpetromag AS zpetromag,\n" + 
            "    atlas.au AS uatlasextinction ,\n" + 
            "    atlas.ag AS gatlasextinction ,\n" + 
            "    atlas.ar AS ratlasextinction,\n" + 
            "    atlas.ai AS iatlasextinction,\n" + 
            "    atlas.az AS zatlasextinction,\n" + 
            "    photoobj.petromag_u ,\n" + 
            "    photoobj.petromag_g ,\n" + 
            "    photoobj.petromag_r ,\n" + 
            "    photoobj.petromag_i ,\n" + 
            "    photoobj.petromag_z ,\n" + 
            "    photoobj.petromagerr_u,\n" + 
            "    photoobj.petromagerr_g,\n" + 
            "    photoobj.petromagerr_r,\n" + 
            "    photoobj.petromagerr_i,\n" + 
            "    photoobj.petromagerr_z,\n" + 
            "    photoobj.extinction_u, \n" + 
            "    photoobj.extinction_g,\n" + 
            "    photoobj.extinction_r,\n" + 
            "    photoobj.extinction_i,\n" + 
            "    photoobj.extinction_z,\n" + 
            "    photozrf.z, \n" + 
            "    atlas.ra       AS ATLAS_RA ,\n" + 
            "    atlas.dec      AS ATLAS_DEC ,\n" + 
            "    photoobj.ra    AS PHOTOOBJ_RA ,\n" + 
            "    photoobj.dec   AS PHOTOOBJ_DEC , \n" + 
            "    atlas.sourceId AS ATLAS_ID ,\n" + 
            "    photoobj.objID AS SDSS_ID \n" + 
            "FROM\n" + 
            "    atlassource AS atlas,\n" + 
            "    bestdr9.photoobj AS photoobj,\n" + 
            "    bestdr9.photozrf AS photozrf,\n" + 
            "    atlassourcexdr9photoobj AS x\n" + 
            "WHERE\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    photozrf.objid = photoobj.objid\n" + 
            "AND\n" + 
            "    photozrf.objid = x.slaveobjid\n" + 
            "AND\n" + 
            "    atlas.gpetromag > 0 AND atlas.rpetromag > 0 AND atlas.ipetromag > 0 AND atlas.zpetromag > 0\n" + 
            "AND\n" + 
            "    gpperrbits = 0 AND rpperrbits = 0 AND ipperrbits = 0 AND zpperrbits = 0\n" + 
            "AND\n" + 
            "    photoobj.clean = 1\n" + 
            "AND\n" + 
            "    atlas.mergedclass = 1\n" + 
            "AND\n" + 
            "    x.distanceMins < 1/60.0 \n" + 
            "AND\n" + 
            "    x.sdssprimary = 1 \n" + 
            "AND\n" + 
            "    x.sdsstype = 3 \n" + 
            "AND\n" + 
            "    x.distanceMins IN ( \n" + 
            "        SELECT MIN (distanceMins) \n" + 
            "            FROM\n" + 
            "                atlassourcexdr9photoobj\n" + 
            "            WHERE\n" + 
            "                masterobjid = x.masterobjid \n" + 
            "        )",

            "select\n" + 
            "    top 5\n" + 
            "    atlas.upetromag as upetromag,\n" + 
            "    atlas.gpetromag as gpetromag,\n" + 
            "    atlas.rpetromag as rpetromag,\n" + 
            "    atlas.ipetromag as ipetromag,\n" + 
            "    atlas.zpetromag as zpetromag,\n" + 
            "    atlas.au as uatlasextinction,\n" + 
            "    atlas.ag as gatlasextinction,\n" + 
            "    atlas.ar as ratlasextinction,\n" + 
            "    atlas.ai as iatlasextinction,\n" + 
            "    atlas.az as zatlasextinction,\n" + 
            "    photoobj.petromag_u as petromag_u,\n" + 
            "    photoobj.petromag_g as petromag_g,\n" + 
            "    photoobj.petromag_r as petromag_r,\n" + 
            "    photoobj.petromag_i as petromag_i,\n" + 
            "    photoobj.petromag_z as petromag_z,\n" + 
            "    photoobj.petromagerr_u as petromagerr_u,\n" + 
            "    photoobj.petromagerr_g as petromagerr_g,\n" + 
            "    photoobj.petromagerr_r as petromagerr_r,\n" + 
            "    photoobj.petromagerr_i as petromagerr_i,\n" + 
            "    photoobj.petromagerr_z as petromagerr_z,\n" + 
            "    photoobj.extinction_u as extinction_u,\n" + 
            "    photoobj.extinction_g as extinction_g,\n" + 
            "    photoobj.extinction_r as extinction_r,\n" + 
            "    photoobj.extinction_i as extinction_i,\n" + 
            "    photoobj.extinction_z as extinction_z,\n" + 
            "    photozrf.z as z,\n" + 
            "    atlas.ra as atlas_ra,\n" + 
            "    atlas.dec as atlas_dec,\n" + 
            "    photoobj.ra as photoobj_ra,\n" + 
            "    photoobj.dec as photoobj_dec,\n" + 
            "    atlas.sourceid as atlas_id,\n" + 
            "    photoobj.objid as sdss_id\n" + 
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
            "    atlas.gpetromag > 0 and atlas.rpetromag > 0 and atlas.ipetromag > 0 and atlas.zpetromag > 0\n" + 
            "and\n" + 
            "    atlas.gpperrbits = 0 and atlas.rpperrbits = 0 and atlas.ipperrbits = 0 and atlas.zpperrbits = 0\n" + 
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
            "            min(atlasdr1.dbo.atlassourcexdr9photoobj.distancemins) as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj.masterobjid = x.masterobjid\n" + 
            "            )",

            new ExpectedField[] {
                new ExpectedField("upetromag",          AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("gpetromag",          AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("rpetromag",          AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("ipetromag",          AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("zpetromag",          AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("uatlasextinction",   AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("gatlasextinction",   AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("ratlasextinction",   AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("iatlasextinction",   AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("zatlasextinction",   AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromag_u",         AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromag_g",         AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromag_r",         AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromag_i",         AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromag_z",         AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromagerr_u",      AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromagerr_g",      AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromagerr_r",      AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromagerr_i",      AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("petromagerr_z",      AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("extinction_u",       AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("extinction_g",       AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("extinction_r",       AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("extinction_i",       AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("extinction_z",       AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("z",                  AdqlColumn.Type.FLOAT,  0),
                new ExpectedField("ATLAS_RA",           AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("ATLAS_DEC",          AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("PHOTOOBJ_RA",        AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("PHOTOOBJ_DEC",       AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("ATLAS_ID",           AdqlColumn.Type.LONG,   0),
                new ExpectedField("SDSS_ID",            AdqlColumn.Type.LONG,   0)
                }
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
            "SELECT\n" + 
            "    atlas.framesetID,\n" + 
            "    atlas.sourceid,\n" + 
            "    atlas.gpetromag,\n" + 
            "    atlas.ag,\n" + 
            "    photoobj.objID,\n" + 
            "    photoobj.petromag_g ,\n" + 
            "    photoobj.extinction_g\n" + 
            "FROM\n" + 
            "    atlasSourceXDr9photoobj as x\n" + 
            "LEFT JOIN\n" + 
            "    atlassource AS atlas\n" + 
            "ON\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "LEFT JOIN\n" + 
            "    bestdr9.photoobj AS photoobj\n" + 
            "ON\n" + 
            "    x.slaveobjID = photoobj.objID\n" + 
            "WHERE\n" + 
            "    atlas.gpperrbits = 0 AND atlas.gerrbits = 0\n" + 
            "AND\n" + 
            "    photoobj.clean = 1\n" + 
            "AND\n" + 
            "    x.distanceMins < 1/60.0\n" + 
            "AND\n" + 
            "    atlas.mergedclass = -1\n" + 
            "AND\n" + 
            "    x.sdsstype = 6\n" + 
            "AND\n" + 
            "    x.distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN (distanceMins)\n" + 
            "        FROM\n" + 
            "            atlassourcexdr9photoobj\n" + 
            "        WHERE\n" + 
            "            masterobjid = x.masterobjid\n" + 
            "      )",
            
            "select\n" + 
            "    atlas.framesetid as framesetid,\n" + 
            "    atlas.sourceid as sourceid,\n" + 
            "    atlas.gpetromag as gpetromag,\n" + 
            "    atlas.ag as ag,\n" + 
            "    photoobj.objid as objid,\n" + 
            "    photoobj.petromag_g as petromag_g,\n" + 
            "    photoobj.extinction_g as extinction_g\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlassourcexdr9photoobj as x\n" + 
            "left outer join\n" + 
            "    atlasdr1.dbo.atlassource as atlas\n" + 
            "on\n" + 
            "    x.masterobjid = atlas.sourceid\n" + 
            "left outer join\n" + 
            "    bestdr9.dbo.photoobj as photoobj\n" + 
            "on\n" + 
            "    x.slaveobjid = photoobj.objid\n" + 
            "where\n" + 
            "    atlas.gpperrbits = 0 and atlas.gerrbits = 0\n" + 
            "and\n" + 
            "    photoobj.clean = 1\n" + 
            "and\n" + 
            "    x.distancemins < 1 / 60.0\n" + 
            "and\n" + 
            "    atlas.mergedclass = -1\n" + 
            "and\n" + 
            "    x.sdsstype = 6\n" + 
            "and\n" + 
            "    x.distancemins in (\n" + 
            "        select\n" + 
            "            min(atlasdr1.dbo.atlassourcexdr9photoobj.distancemins) as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlassourcexdr9photoobj.masterobjid = x.masterobjid\n" + 
            "            )",

            new ExpectedField[] {
                new ExpectedField("framesetID",     AdqlColumn.Type.LONG,  0),
                new ExpectedField("sourceid",       AdqlColumn.Type.LONG,  0),
                new ExpectedField("gpetromag",      AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("ag",             AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("objID",          AdqlColumn.Type.LONG,  0),
                new ExpectedField("petromag_g",     AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("extinction_g",   AdqlColumn.Type.FLOAT, 0)
                }
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
            State.PARSE_ERROR,
            "SELECT\n" + 
            "    TOP 100000\n" + 
            "    photozrf.dered_u,\n" + 
            "    photozrf.dered_g, \n" + 
            "    photozrf.dered_r,\n" + 
            "    photozrf.dered_i,\n" + 
            "    photozrf.dered_z, \n" + 
            "    photozrf.z\n" + 
            "FROM\n" + 
            "    bestdr9.photozrf AS photozrf"
            );
        }
    }
