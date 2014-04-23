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
package uk.ac.roe.wfau.firethorn.adql.query.papers;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/439
 *
 */
public class HamblyTestCase
    extends AtlasQueryTestBase
    {

	/**
     * test001
     */
    @Test
    public void test001()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT " + 
    		" COUNT(*) FROM Programme AS t1, RequiredFilters AS t2" + 
    		" WHERE t1.programmeID=t2.programmeID"
          
            );
        }
    
	/**
     * test002
     */
    @Test
    public void test002()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT " + 
    		" COUNT(*) FROM gpsSource" +
    		" WHERE l BETWEEN 0.0 AND 1.0"
          
            );
        }
    
	/**
     * test003
     */
    @Test
    public void test003()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT " + 
    		" CAST(ROUND(1,0) AS INT) AS longitude, " +
            " COUNT(*) " +
    		" FROM gpsSource " + 
            " GROUP BY CAST(ROUND(1,0) AS INT) " + 
    		" ORDER BY CAST(ROUND(1,0) AS INT)"
          
            );
        }
    
    
	/**
     * test004
     */
    @Test
    public void test004()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT " + 
            " TOP 10 s.sourceID,s.ra,s.dec,m.mjdObs AS jmjd, d.isoMag AS hIsoMag " +
            " FROM gpsSource AS s, gpsMer, geLog AS l, gpsDetection AS d, Multiframe AS m " +
            " WHERE s.frameSetID = l.frameSetID AND l.hmfID = d.multiframeID " +
            " AND l.heNum = d.extNum AND s.hseqNum = d.seqNum " +
            " AND l.jmfID = m.multiframeID AND l.jmfID > 0 AND l.hmfID > 0 "
            );
        }
    
    /**
     * test005
     */
    @Test
    public void test005()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT zAperMag3 − jAperMag3 AS zmj, zAperMag3 AS z FROM gcsPointSource " +
            " WHERE ra BETWEEN +84.00 AND +85.00 AND dec BETWEEN −2.85 AND −2.30 AND " +
            " zAperMag3 > 11.3 AND yAperMag3 > 11.5 AND jAperMag3 > 11.0 AND hAperMag3 > 11.3 AND k_1AperMag3 > 9.9 AND " +
            " zAperMag3 < 5.0 * (zAperMag3 − jAperMag3) + 10.0 AND jAperMag3 − hAperMag3 > 0.3 " 
            
            );
        }
    
    /**
     * test006
     */
    @Test
    public void test006()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT COUNT(*) FROM reliableLasPointSource " +
            " WHERE SQUARE(muRA) + SQUARE(muDec) > 5.0 * SQRT(SQUARE(muRA*sigMuRA)+SQUARE(muDec*sigMuDec)) " 		
            );
        }

    /**
     * test007
     */
    @Test
    public void test007()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT COUNT(*) FROM reliableLasPointSource " +
            " WHERE (j_1ObjID > 0 AND j_2ObjID < 0) OR (j_1ObjID < 0 AND j_2ObjID > 0)"
           );
            
        }

    
    /**
     * test008
     */
    @Test
    public void test008()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT ra, dec, jPetroMag − aj as j, (jAperMag3 − aj) − (kAperMag3 − ak) as jmk " +
            " FROM reliableDxsSource WHERE mergedClass NOT BETWEEN − 1 AND 0 AND + " +
            " jPetroMagErr BETWEEN 0 AND 0.2 AND kPetroMagErr BETWEEN 0 AND 0.2"
           );
            
        }
    
    /**
     * test009
     */
    @Test
    public void test009()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT CAST(ROUND(kab * 2.0,0) AS INT)/2.0 AS KAB, LOG10(COUNT( * )/0.89) AS logN " +
            " FROM ( SELECT (kPetroMag − ak)+1.900 AS kab FROM udsSource WHERE mergedClass NOT BETWEEN − 1 AND 0 " + 
            " AND jPetroMag > 0.0 AND kPetroMag > 0.0 ) AS T " +
            " GROUP BY CAST(ROUND(kab * 2.0,0) AS INT)/2.0 " +
            " ORDER BY CAST(ROUND(kab * 2.0,0) AS INT)/2.0 "
           );
            
        }
    
    /**
     * test010
     */
    @Test
    public void test010()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT MIN(ra),MAX(ra),MIN(dec),MAX(dec), ((MAX(ra) − MIN(ra)) * COS(RADIANS(AVG(dec)))) * (MAX(dec) − MIN(dec)) " + 
            " AS area) " +
            " FROM udsSource) " 
           );
            
        }
 
    /**
     * test011
     */
    @Test
    public void test011()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT CAST(ROUND(l * 6.0,0) AS INT)/6.0 AS lon, CAST(ROUND(b*6.0,0) AS INT)/6.0 AS lat, COUNT(*) AS num " +
            " FROM gpsSource WHERE k_1Class BETWEEN − 2 AND − 1 AND k_1ppErrBits < 256 AND  (priOrSec=0 OR priOrSec=frameSetID)  " +
            " GROUP BY CAST(ROUND(l*6.0,0) AS INT)/6.0, CAST(ROUND(b*6.0,0) AS INT)/6.0"
           );
            
        }    
    
    /**
     * test012
     */
    @Test
    public void test012()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT psfMag_i − psfMag_z AS imz, psfMag_z − j 1AperMag3 AS zmj, psfMag_i − yAperMag3 AS imy , ymj_1Pnt AS ymj " + 
            " FROM lasPointSource AS s, lasSourceXDR5PhotoObj AS x, BestDR5..PhotoObj AS p  " + 
            " WHERE s.sourceID = x.masterObjID AND x.slaveObjID = p.objID AND x.distanceMins < 1.0/60.0 AND  " + 
            " x.distanceMins IN  " + 
	            " ( SELECT MIN(distanceMins) FROM lasSourceXDR5PhotoObj  " + 
	            " WHERE masterObjID = x.masterObjID AND sdssPrimary = 1 AND sdssType = 6 )  " + 
	            " AND psfMag i > 0.0 AND psfMag i − yAperMag3 > 4.0 AND ymj_1Pnt < 0.8  " + 
	            " AND psfMagErr_u > 0.3 AND psfMagErr_g > 0.3 AND psfMagErr_r > 0.3 "  
            );
        }  
    

    /**
     * test013
     */
    @Test
    public void test013()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT s.ra as alpha, s.dec as delta, (petroMag_u − extinction_u) AS u, (petroMag_g − extinction_g) AS g, (petroMag_r − extinction_r) AS r , (petroMag_i − extinction_i) AS i, (petroMag_z − extinction_z) AS z, (yPetroMag-ay) AS y , (j_1PetroMag-aj) AS j, (hPetroMag-ah) AS h, (kPetroMag-ak) AS k, z.z AS redshift, (modelMag_g − extinction_g) − ( modelMag_r − extinction_r) AS gmr , (yAperMag3 − ay)-(kAperMag3 − ak) AS ymk, (modelMag_u − extinction_u) − (modelMag_g − extinction_g) AS umg, (kPetroMag − ak) − 25 − 5 * ( LOG10(2 * 2.998e5 * (1 + z.z − SQRT(1 + z.z))/75) ) AS M_K " +  
            " FROM lasExtendedSource AS s, lasSourceXDR5PhotoObj AS x, BestDR5..PhotoObj AS p, BestDR5..SpecObj AS z  " + 
            " WHERE z.specObjID=p.specObjID AND s.sourceID = x.masterObjID AND p.objID = x.slaveObjID AND x.distanceMins  " + 
            " IN  " + 
            " ( SELECT MIN(distanceMins) FROM lasSourceXDR5PhotoObj  " + 
            " WHERE masterObjID = x.masterObjID AND distanceMins < 2.0/60.0 ) AND (kPetroMag − ak) " +  
            " BETWEEN 0.0 AND 18.4 AND yPetroMag > 0 AND modelMag_u > 0 AND modelMag g > 0 AND  modelMag_r > 0 AND z.z BETWEEN 0.01 AND 0.15 AND z.zWarning=0 " 
        	);
            
        }    
    
    
    
    /**
     * test014
     */
    @Test
    public void test014()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT COUNT( * ) FROM lasSource WHERE ymj_1Pnt > 0.5 AND j_1mhPnt < 0.0 AND sourceID NOT IN " +
            " ( SELECT masterObjID FROM lasSourceXDR5PhotoObj AS x, BestDR5..PhotoObj AS p " +
            " WHERE p.objID = x.slaveObjID AND (psfMagErr_i < 0.5 OR psfMagErr_z < 0.5) AND x.distanceMins < 1.0/60.0 ) AND frameSetID IN " +
            " ( SELECT DISTINCT(frameSetID) FROM lasSource AS s, lasSourceXDR5PhotoObj AS x " +
            " WHERE s.sourceID = x.masterObjID )"  
            );
            
        }  
    
    /**
     * test015
     */
    @Test
    public void test015()
        {
        validate(
            Level.LEGACY,
            State.VALID,
            " SELECT dbo.fIAUnameLAS(ra,dec), yAperMag3, ymj_1Pnt,ymj_1PntErr ,j_1mhPnt,j_1mhPntErr FROM lasSource WHERE ymj_1Pnt > 0.5 AND j_1mhPnt < 0.0 AND sourceID NOT IN " +
    		 " ( SELECT masterObjID FROM lasSourceXDR5PhotoObj AS x, BestDR5..PhotoObj AS p " +
             " WHERE p.objID = x.slaveObjID AND (psfMagErr_i < 0.5 OR psfMagErr_z < 0.5) AND x.distanceMins < 1.0/60.0 ) AND frameSetID IN " +
             " ( SELECT DISTINCT(frameSetID) FROM lasSource AS s, lasSourceXDR5PhotoObj AS x " +
             " WHERE s.sourceID = x.masterObjID )"  
            
            );
            
        }    
    
    
    }
