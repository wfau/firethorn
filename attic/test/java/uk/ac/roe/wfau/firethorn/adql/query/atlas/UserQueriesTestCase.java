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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
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
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    atlas.uPetroMag as uPetroMag,\n" + 
            "    atlas.gPetroMag as gPetroMag,\n" + 
            "    atlas.rPetroMag as rPetroMag,\n" + 
            "    atlas.iPetroMag as iPetroMag,\n" + 
            "    atlas.zPetroMag as zPetroMag,\n" + 
            "    PhotoObj.petromag_u ,\n" + 
            "    PhotoObj.petromag_g ,\n" + 
            "    PhotoObj.petromag_r ,\n" + 
            "    PhotoObj.petromag_i ,\n" + 
            "    PhotoObj.petromag_z\n" + 
            "FROM\n" + 
            "    atlasSource as atlas,\n" + 
            "    bestdr9..PhotoObj as PhotoObj,\n" + 
            "    bestdr9..PhotozRF as PhotozRF,\n" + 
            "    atlasSourceXDR9PhotoObj as X\n" + 
            "WHERE\n" + 
            "    X.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    PhotozRF.objid = PhotoObj.objid\n" + 
            "AND\n" + 
            "    PhotozRF.objid = X.slaveobjid\n" + 
            "AND\n" + 
            "    atlas.gPetroMag > 0 and atlas.rPetroMag > 0 and atlas.iPetroMag > 0 and atlas.zPetroMag > 0\n" + 
            "AND\n" + 
            "    gppErrBits = 0 AND rppErrBits = 0 AND ippErrBits = 0 AND zppErrBits = 0\n" + 
            "AND\n" + 
            "    PhotoObj.clean = 1\n" + 
            "AND\n" + 
            "    atlas.mergedClass = 1\n" + 
            "AND\n" + 
            "    X.distanceMins < 1/60.0\n" + 
            "AND\n" + 
            "    X.sdssPrimary = 1\n" + 
            "AND\n" + 
            "    X.sdssType = 3\n" + 
            "AND\n" + 
            "    X.distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN (distanceMins)\n" + 
            "        FROM\n" + 
            "            atlasSourceXDR9PhotoObj\n" + 
            "        WHERE\n" + 
            "            masterObjID = X.masterObjID\n" + 
            "        )",

            "select\n" + 
            "    atlas.uPetroMag as uPetroMag,\n" + 
            "    atlas.gPetroMag as gPetroMag,\n" + 
            "    atlas.rPetroMag as rPetroMag,\n" + 
            "    atlas.iPetroMag as iPetroMag,\n" + 
            "    atlas.zPetroMag as zPetroMag,\n" + 
            "    PhotoObj.petromag_u as petromag_u,\n" + 
            "    PhotoObj.petromag_g as petromag_g,\n" + 
            "    PhotoObj.petromag_r as petromag_r,\n" + 
            "    PhotoObj.petromag_i as petromag_i,\n" + 
            "    PhotoObj.petromag_z as petromag_z\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlasSource as atlas,\n" + 
            "    bestdr9.dbo.PhotoObj as PhotoObj,\n" + 
            "    bestdr9.dbo.PhotozRF as PhotozRF,\n" + 
            "    atlasdr1.dbo.atlasSourceXDR9PhotoObj as x\n" + 
            "where\n" + 
            "    x.masterObjID = atlas.sourceID\n" + 
            "and\n" + 
            "    PhotozRF.objid = PhotoObj.objid\n" + 
            "and\n" + 
            "    PhotozRF.objid = x.slaveobjid\n" + 
            "and\n" + 
            "    atlas.gPetroMag > 0\n" + 
            "and\n" + 
            "    atlas.rPetroMag > 0\n" + 
            "and\n" + 
            "    atlas.iPetroMag > 0\n" + 
            "and\n" + 
            "    atlas.zPetroMag > 0\n" + 
            "and\n" + 
            "    atlas.gppErrBits = 0\n" + 
            "and\n" + 
            "    atlas.rppErrBits = 0\n" + 
            "and\n" + 
            "    atlas.ippErrBits = 0\n" + 
            "and\n" + 
            "    atlas.zppErrBits = 0\n" + 
            "and\n" + 
            "    PhotoObj.clean = 1\n" + 
            "and\n" + 
            "    atlas.mergedClass = 1\n" + 
            "and\n" + 
            "    x.distanceMins < 1 / 60.0\n" + 
            "and\n" + 
            "    x.sdssPrimary = 1\n" + 
            "and\n" + 
            "    x.sdssType = 3\n" + 
            "and\n" + 
            "    x.distanceMins in (\n" + 
            "        select\n" + 
            "            min(atlasdr1.dbo.atlasSourceXDR9PhotoObj.distanceMins)as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj.masterObjID = x.masterObjID\n" + 
            "        )",

            new ExpectedField[] {
                new ExpectedField("uPetroMag",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gPetroMag",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("rPetroMag",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("iPetroMag",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("zPetroMag",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_u", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_g", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_r", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_i", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_z", AdqlColumn.AdqlType.FLOAT, 0)
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
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    TOP 10 \n" + 
            "    atlas.uPetroMag AS g, \n" + 
            "    PhotoObj.petromag_u\n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    bestdr9.PhotoObj AS PhotoObj,\n" + 
            "    atlasSourceXDR9PhotoObj AS x \n" + 
            "WHERE\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    x.slaveobjID = PhotoObj.objID",
            
            "select\n" + 
            "    top 10 atlas.uPetroMag as g,\n" + 
            "    PhotoObj.petromag_u as petromag_u\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlasSource as atlas,\n" + 
            "    bestdr9.dbo.PhotoObj as PhotoObj,\n" + 
            "    atlasdr1.dbo.atlasSourceXDR9PhotoObj as x\n" + 
            "where\n" + 
            "    x.masterObjID = atlas.sourceID\n" + 
            "and\n" + 
            "    x.slaveobjid = PhotoObj.objid",

            new ExpectedField[] {
                new ExpectedField("g",  AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("petromag_u", AdqlColumn.AdqlType.FLOAT, 0)
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
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "SELECT\n" + 
            "    TOP 100000\n" + 
            "    PhotoObj.dered_u,\n" + 
            "    PhotoObj.dered_g, \n" + 
            "    PhotoObj.dered_r,\n" + 
            "    PhotoObj.dered_i,\n" + 
            "    PhotoObj.dered_z, \n" + 
            "    PhotoObj.z\n" + 
            "FROM\n" + 
            "    bestdr9.PhotozRF AS PhotozRF\n" + 
            "LEFT JOIN\n" + 
            "    bestdr9.PhotoObj AS PhotoObj\n" + 
            "ON\n" + 
            "    PhotozRF.objID = PhotoObj.objID",

            "select\n" + 
            "    top 100000\n" + 
            "    PhotoObj.dered_u as dered_u,\n" + 
            "    PhotoObj.dered_g as dered_g,\n" + 
            "    PhotoObj.dered_r as dered_r,\n" + 
            "    PhotoObj.dered_i as dered_i,\n" + 
            "    PhotoObj.dered_z as dered_z,\n" + 
            "    PhotoObj.z as z\n" + 
            "from\n" + 
            "    bestdr9.dbo.PhotozRF as PhotozRF\n" + 
            "left outer join\n" + 
            "    bestdr9.dbo.PhotoObj as PhotoObj\n" + 
            "on\n" + 
            "    PhotozRF.objid = PhotoObj.objid",

            new ExpectedField[] {
                new ExpectedField("dered_u", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dered_g", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dered_r", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dered_i", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("dered_z", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("z",       AdqlColumn.AdqlType.FLOAT, 0)
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
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "SELECT\n" + 
            "    TOP 5\n" + 
            "    atlas.uPetroMag AS uPetroMag,\n" + 
            "    atlas.gPetroMag AS gPetroMag,    \n" + 
            "    atlas.rPetroMag AS rPetroMag,\n" + 
            "    atlas.iPetroMag AS iPetroMag,\n" + 
            "    atlas.zPetroMag AS zPetroMag,\n" + 
            "    atlas.au AS uAtlasExtinction ,\n" + 
            "    atlas.ag AS gAtlasExtinction ,\n" + 
            "    atlas.ar AS rAtlasExtinction,\n" + 
            "    atlas.ai AS iAtlasExtinction,\n" + 
            "    atlas.az AS zAtlasExtinction,\n" + 
            "    PhotoObj.petromag_u ,\n" + 
            "    PhotoObj.petromag_g ,\n" + 
            "    PhotoObj.petromag_r ,\n" + 
            "    PhotoObj.petromag_i ,\n" + 
            "    PhotoObj.petromag_z ,\n" + 
            "    PhotoObj.petromagerr_u,\n" + 
            "    PhotoObj.petromagerr_g,\n" + 
            "    PhotoObj.petromagerr_r,\n" + 
            "    PhotoObj.petromagerr_i,\n" + 
            "    PhotoObj.petromagerr_z,\n" + 
            "    PhotoObj.extinction_u, \n" + 
            "    PhotoObj.extinction_g,\n" + 
            "    PhotoObj.extinction_r,\n" + 
            "    PhotoObj.extinction_i,\n" + 
            "    PhotoObj.extinction_z,\n" + 
            "    PhotozRF.z, \n" + 
            "    atlas.ra       AS ATLAS_RA ,\n" + 
            "    atlas.dec      AS ATLAS_DEC ,\n" + 
            "    PhotoObj.ra    AS PHOTOOBJ_RA ,\n" + 
            "    PhotoObj.dec   AS PHOTOOBJ_DEC , \n" + 
            "    atlas.sourceId AS ATLAS_ID ,\n" + 
            "    PhotoObj.objID AS SDSS_ID \n" + 
            "FROM\n" + 
            "    atlasSource AS atlas,\n" + 
            "    bestdr9.PhotoObj AS PhotoObj,\n" + 
            "    bestdr9.PhotozRF AS PhotozRF,\n" + 
            "    atlasSourceXDR9PhotoObj AS x\n" + 
            "WHERE\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "AND\n" + 
            "    PhotozRF.objid = PhotoObj.objid\n" + 
            "AND\n" + 
            "    PhotozRF.objid = x.slaveobjid\n" + 
            "AND\n" + 
            "    atlas.gPetroMag > 0 AND atlas.rPetroMag > 0 AND atlas.iPetroMag > 0 AND atlas.zPetroMag > 0\n" + 
            "AND\n" + 
            "    gppErrBits = 0 AND rppErrBits = 0 AND ippErrBits = 0 AND zppErrBits = 0\n" + 
            "AND\n" + 
            "    PhotoObj.clean = 1\n" + 
            "AND\n" + 
            "    atlas.mergedClass = 1\n" + 
            "AND\n" + 
            "    x.distanceMins < 1/60.0 \n" + 
            "AND\n" + 
            "    x.sdssPrimary = 1 \n" + 
            "AND\n" + 
            "    x.sdssType = 3 \n" + 
            "AND\n" + 
            "    x.distanceMins IN ( \n" + 
            "        SELECT MIN (distanceMins) \n" + 
            "            FROM\n" + 
            "                atlasSourceXDR9PhotoObj\n" + 
            "            WHERE\n" + 
            "                masterObjID = x.masterObjID \n" + 
            "        )",

            "select\n" + 
            "    top 5\n" + 
            "    atlas.uPetroMag as uPetroMag,\n" + 
            "    atlas.gPetroMag as gPetroMag,\n" + 
            "    atlas.rPetroMag as rPetroMag,\n" + 
            "    atlas.iPetroMag as iPetroMag,\n" + 
            "    atlas.zPetroMag as zPetroMag,\n" + 
            "    atlas.au as uAtlasExtinction,\n" + 
            "    atlas.ag as gAtlasExtinction,\n" + 
            "    atlas.ar as rAtlasExtinction,\n" + 
            "    atlas.ai as iAtlasExtinction,\n" + 
            "    atlas.az as zAtlasExtinction,\n" + 
            "    PhotoObj.petromag_u as petromag_u,\n" + 
            "    PhotoObj.petromag_g as petromag_g,\n" + 
            "    PhotoObj.petromag_r as petromag_r,\n" + 
            "    PhotoObj.petromag_i as petromag_i,\n" + 
            "    PhotoObj.petromag_z as petromag_z,\n" + 
            "    PhotoObj.petromagerr_u as petromagerr_u,\n" + 
            "    PhotoObj.petromagerr_g as petromagerr_g,\n" + 
            "    PhotoObj.petromagerr_r as petromagerr_r,\n" + 
            "    PhotoObj.petromagerr_i as petromagerr_i,\n" + 
            "    PhotoObj.petromagerr_z as petromagerr_z,\n" + 
            "    PhotoObj.extinction_u as extinction_u,\n" + 
            "    PhotoObj.extinction_g as extinction_g,\n" + 
            "    PhotoObj.extinction_r as extinction_r,\n" + 
            "    PhotoObj.extinction_i as extinction_i,\n" + 
            "    PhotoObj.extinction_z as extinction_z,\n" + 
            "    PhotozRF.z as z,\n" + 
            "    atlas.ra as atlas_ra,\n" + 
            "    atlas.dec as atlas_dec,\n" + 
            "    PhotoObj.ra as PhotoObj_ra,\n" + 
            "    PhotoObj.dec as PhotoObj_dec,\n" + 
            "    atlas.sourceID as atlas_id,\n" + 
            "    PhotoObj.objid as sdss_id\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlasSource as atlas,\n" + 
            "    bestdr9.dbo.PhotoObj as PhotoObj,\n" + 
            "    bestdr9.dbo.PhotozRF as PhotozRF,\n" + 
            "    atlasdr1.dbo.atlasSourceXDR9PhotoObj as x\n" + 
            "where\n" + 
            "    x.masterObjID = atlas.sourceID\n" + 
            "and\n" + 
            "    PhotozRF.objid = PhotoObj.objid\n" + 
            "and\n" + 
            "    PhotozRF.objid = x.slaveobjid\n" + 
            "and\n" + 
            "    atlas.gPetroMag > 0 and atlas.rPetroMag > 0 and atlas.iPetroMag > 0 and atlas.zPetroMag > 0\n" + 
            "and\n" + 
            "    atlas.gppErrBits = 0 and atlas.rppErrBits = 0 and atlas.ippErrBits = 0 and atlas.zppErrBits = 0\n" + 
            "and\n" + 
            "    PhotoObj.clean = 1\n" + 
            "and\n" + 
            "    atlas.mergedClass = 1\n" + 
            "and\n" + 
            "    x.distanceMins < 1 / 60.0\n" + 
            "and\n" + 
            "    x.sdssPrimary = 1\n" + 
            "and\n" + 
            "    x.sdssType = 3\n" + 
            "and\n" + 
            "    x.distanceMins in (\n" + 
            "        select\n" + 
            "            min(atlasdr1.dbo.atlasSourceXDR9PhotoObj.distanceMins) as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj.masterObjID = x.masterObjID\n" + 
            "            )",

            new ExpectedField[] {
                new ExpectedField("uPetroMag",          AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("gPetroMag",          AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("rPetroMag",          AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("iPetroMag",          AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("zPetroMag",          AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("uAtlasExtinction",   AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("gAtlasExtinction",   AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("rAtlasExtinction",   AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("iAtlasExtinction",   AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("zAtlasExtinction",   AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromag_u",         AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromag_g",         AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromag_r",         AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromag_i",         AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromag_z",         AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromagerr_u",      AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromagerr_g",      AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromagerr_r",      AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromagerr_i",      AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("petromagerr_z",      AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("extinction_u",       AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("extinction_g",       AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("extinction_r",       AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("extinction_i",       AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("extinction_z",       AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("z",                  AdqlColumn.AdqlType.FLOAT,  0),
                new ExpectedField("ATLAS_RA",           AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("ATLAS_DEC",          AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("PHOTOOBJ_RA",        AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("PHOTOOBJ_DEC",       AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("ATLAS_ID",           AdqlColumn.AdqlType.LONG,   0),
                new ExpectedField("SDSS_ID",            AdqlColumn.AdqlType.LONG,   0)
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
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,
            "SELECT\n" + 
            "    atlas.frameSetID,\n" + 
            "    atlas.sourceID,\n" + 
            "    atlas.gPetroMag,\n" + 
            "    atlas.ag,\n" + 
            "    PhotoObj.objID,\n" + 
            "    PhotoObj.petromag_g ,\n" + 
            "    PhotoObj.extinction_g\n" + 
            "FROM\n" + 
            "    atlasSourceXDr9PhotoObj as x\n" + 
            "LEFT JOIN\n" + 
            "    atlasSource AS atlas\n" + 
            "ON\n" + 
            "    x.masterobjID = atlas.sourceID\n" + 
            "LEFT JOIN\n" + 
            "    bestdr9.PhotoObj AS PhotoObj\n" + 
            "ON\n" + 
            "    x.slaveobjID = PhotoObj.objID\n" + 
            "WHERE\n" + 
            "    atlas.gppErrBits = 0 AND atlas.gerrbits = 0\n" + 
            "AND\n" + 
            "    PhotoObj.clean = 1\n" + 
            "AND\n" + 
            "    x.distanceMins < 1/60.0\n" + 
            "AND\n" + 
            "    atlas.mergedClass = -1\n" + 
            "AND\n" + 
            "    x.sdssType = 6\n" + 
            "AND\n" + 
            "    x.distanceMins IN (\n" + 
            "        SELECT\n" + 
            "            MIN (distanceMins)\n" + 
            "        FROM\n" + 
            "            atlasSourceXDR9PhotoObj\n" + 
            "        WHERE\n" + 
            "            masterObjID = x.masterObjID\n" + 
            "      )",
            
            "select\n" + 
            "    atlas.frameSetID as frameSetID,\n" + 
            "    atlas.sourceID as sourceID,\n" + 
            "    atlas.gPetroMag as gPetroMag,\n" + 
            "    atlas.ag as ag,\n" + 
            "    PhotoObj.objid as objid,\n" + 
            "    PhotoObj.petromag_g as petromag_g,\n" + 
            "    PhotoObj.extinction_g as extinction_g\n" + 
            "from\n" + 
            "    atlasdr1.dbo.atlasSourceXDR9PhotoObj as x\n" + 
            "left outer join\n" + 
            "    atlasdr1.dbo.atlasSource as atlas\n" + 
            "on\n" + 
            "    x.masterObjID = atlas.sourceID\n" + 
            "left outer join\n" + 
            "    bestdr9.dbo.PhotoObj as PhotoObj\n" + 
            "on\n" + 
            "    x.slaveobjid = PhotoObj.objid\n" + 
            "where\n" + 
            "    atlas.gppErrBits = 0 and atlas.gerrbits = 0\n" + 
            "and\n" + 
            "    PhotoObj.clean = 1\n" + 
            "and\n" + 
            "    x.distanceMins < 1 / 60.0\n" + 
            "and\n" + 
            "    atlas.mergedClass = -1\n" + 
            "and\n" + 
            "    x.sdssType = 6\n" + 
            "and\n" + 
            "    x.distanceMins in (\n" + 
            "        select\n" + 
            "            min(atlasdr1.dbo.atlasSourceXDR9PhotoObj.distanceMins) as min\n" + 
            "        from\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj\n" + 
            "        where\n" + 
            "            atlasdr1.dbo.atlasSourceXDR9PhotoObj.masterObjID = x.masterObjID\n" + 
            "            )",

            new ExpectedField[] {
                new ExpectedField("frameSetID",     AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("sourceID",       AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("gPetroMag",      AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("ag",             AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("objID",          AdqlColumn.AdqlType.LONG,  0),
                new ExpectedField("petromag_g",     AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("extinction_g",   AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }

    /**
     * User reported query.
     * http://redmine.roe.ac.uk/issues/321
     * Why should this fail ?
     *
     */
    @Test
    public void test0601()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,
            "SELECT\n" + 
            "    TOP 100000\n" + 
            "    PhotozRF.dered_u,\n" + 
            "    PhotozRF.dered_g, \n" + 
            "    PhotozRF.dered_r,\n" + 
            "    PhotozRF.dered_i,\n" + 
            "    PhotozRF.dered_z, \n" + 
            "    PhotozRF.z\n" + 
            "FROM\n" + 
            "    bestdr9.PhotozRF AS PhotozRF"
            );
        }
    }
