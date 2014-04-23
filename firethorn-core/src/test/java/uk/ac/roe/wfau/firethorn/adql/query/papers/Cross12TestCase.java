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
public class Cross12TestCase
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
            " SELECT s.sourceID, s.RA, s.Dec, (sdPho.psfMag_g-s.jAperMag3) AS gmjPnt, jmksPnt, ksAperMag3 " +
            " FROM vhsSource as s, vhsSourceXDR7PhotoObjAll as x, BESTDR7..PhotoObjAll as sdPho  " +
            " WHERE s.sourceID = x.masterObjID AND x.slaveObjID=sdPho.objID AND x.distanceMins<=0.0333 AND x.distanceMins IN  " +
            " (SELECT MIN(distanceMins) FROM vhsSourceXDR7PhotoObjAll WHERE masterObjID=x.masterObjID)  " +
            " AND x.sdssPrimary=1 and x.sdssType=6 AND jppErrBits=0 AND ksppErrBits=0 AND mergedClass IN (-1,-2)  " +
            " AND sdPho.psfMag_g>0. AND s.jAperMag3>0. AND s.ksAperMag3>0.  "
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
            " SELECT v.sourceID, s.RA, s.Dec, /* select some useful attributes, pointing info, number of observations, min, medium, maximum, variable class, and star/galaxy class */ " +
            " v.framesetID, ksnGoodObs, ksMinMag, ksMedianMag, ksMaxMag, variableClass, mergedClass, (ksMedianMag-ksMinMag) as ksFlareMag, COUNT(*) AS nBrightDetections /* from atlasVariability and atlasSource */" + 
            " FROM atlasVariability as v,atlasSource as s, atlasSourceXDetectionBestMatch as b, atlasDetection as d /* first join the tables */ WHERE s.sourceID=v.sourceID " +
            " AND b.sourceID= v.sourceID AND b.multiframeID=d.multiframeID AND b.extNum=d.extNum AND b.seqNum= d.seqNum AND " +
            " /* select the magnitude range, brighter than Ks=17 and not default. */ ksmedianMag<18. and ksmedianMag>0. AND /* at least 5 observations */ ksnGoodObs>=5 AND ksbestAper=5 " +
            " AND /* Min mag is at least 2 magnitudes brighter than median mag(but minMag is not default) */ (ksmedianMag-ksminMag)>2. and ksMinMag>0. " +
            " AND /* Only good Ks band detections in same aperture as statistics are calculated in*/ d.seqNum>0 AND d.ppErrBits IN (0,16) AND d.filterID=5 AND d.aperMag5>0 AND d.aperMag5<(ksMedianMag-0.5) " +
            " /* Group detections */ GROUP BY v.sourceID, s.ra, s.dec, v.framesetID, ksnGoodObs, ksMinMag, ksMedianMag, ksMaxMag, variableClass, mergedClass " +
            " HAVING COUNT(*)>2 /* Order by largest change in magnitude first.*/ ORDER BY ksMedianMag-ksMinMag DESC "
            
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
            " SELECT /* select information necessary to create bi-variate brightness distribution, extinction corrected Petrosian magnitudes put into AB system and seeing corrected, semi-major axis size, PLUS SDSS colours and redshifts (spectroscopic and photometric) */" +
            " s.sourceID,s.RA,s.Dec,s.frameSetID, (s.hPetroMag-s.aH+fh.VegaToAB) AS hPetroAB, s.hHlCorSMjRadAs,(s.hPetroMag+ 2.5*log10(2.*3.14159*s.hHlCorSMjRadAs* s.hHlCorSMjRadAs)-s.aH+fh.VegaToAB) AS hSurfBright," +
            " (s.ksPetroMag-s.aKs+ fks.VegaToAB) as ksPetroAB, s.ksHlCorSMjRadAs,(s.ksPetroMag+ 2.5*log10(2.*3.14159*s.ksHlCorSMjRadAs* s.ksHlCorSMjRadAs)-s.aKs+fks.VegaToAB) AS ksSurfBright, dr7spec.objID as sdssID, " +
            " ((dr7spec.modelMag_u-dr7spec.extinction_u)- (dr7spec.modelMag_g-dr7spec.extinction_g)) as umgModel,z,zErr,zConf,zStatus,specClass /* from vikingSource, Filter (one for each filter for VegaToAB), SDSS-DR7 neighbour table, SDSS SpecPhoto table */ FROM vikingSource AS s,Filter AS fh,Filter AS fks, " +
            " vikingSourceXDR7PhotoObjAll AS xdr7, BESTDR7..SpecPhotoAll as dr7spec /* First join tables, */ WHERE xdr7.masterObjID=s.sourceID AND fh.filterID=4 AND fks.filterID=5 AND dr7spec.objID=xdr7.slaveObjID AND /* select VIKING primary sources matched to SDSS primary sources */ " +
            " (priOrSec=0 OR priOrSec=frameSetID) AND sdssPrimary=1 AND dr7spec.sciencePrimary=1 AND /* within 2 of nearest match */ xdr7.distanceMins<0.03333 AND xdr7.distanceMins IN ( SELECT MIN(distanceMins) FROM vikingSourceXDR7PhotoObjAll WHERE masterObjID=xdr7.masterObjID AND sdssPrimary=1) AND " +
            " /* for objects classified as galaxies or probable galaxies in VIKING */ mergedClass IN (1,-3) AND /* h and ks size is 0.7<sma<=10. arcsec */ ksHlCorSMjRadAs>0.7 AND hHlCorSMjRadAs>0.7 AND ksHlCorSMjRadAs<=10.0 AND hHlCorSMjRadAs<=10.0 AND /* good quality data in VIKING h and ks */ hppErrBits=0 AND" + 
            " ksppErrBits=0 AND /* ks extinction corrected AB mag < 20.5 */ (ksPetroMag-aKs+fks.VegaToAB)<20.5 AND /* RA and Dec range to restrict to where SDSS is */ s.RA>100. AND s.RA<250. AND s.Dec>-5. AND /* z>=0.002 */ dr7spec.z>=0.002 /* Add in ones which do not have SDSS spectra using UNION */ " +
            " UNION SELECT /* select information necessary to create bi-variate brightness distribution, extinction corrected Petrosian magnitudes put into AB system and seeing corrected, semi-major axis size AND SDSS matches to PhotoObj table and photoz table */ s.sourceID,s.RA,s.Dec,s.frameSetID, " +
            " (s.hPetroMag-s.aH+fh.VegaToAB) AS hPetroAB, s.hHlCorSMjRadAs,(s.hPetroMag+ 2.5*log10(2.*3.14159*s.hHlCorSMjRadAs* s.hHlCorSMjRadAs)-s.aH+fh.VegaToAB) AS hSurfBright, (s.ksPetroMag-s.aKs+fks.VegaToAB) as ksPetroAB,s.ksHlCorSMjRadAs," +
            " (s.ksPetroMag+ 2.5*log10(2.*3.14159*s.ksHlCorSMjRadAs* s.ksHlCorSMjRadAs)-s.aKs+fks.VegaToAB) AS ksSurfBright, dr7phot.objID as sdssID, ((dr7phot.modelMag_u-dr7phot.extinction_u)- (dr7phot.modelMag_g-dr7phot.extinction_g)) as umgModel, " +
            " photz.z as z,photz.zErr as zErr, -9.9999 as zConf,-9 as zStatus,-9 as specClass /* from vikingSource, Filter (one for each filter for VegaToAB), SDSS-DR7 neighbour table, */ FROM vikingSource AS s,Filter AS fh,Filter " +
            " AS fks,vikingSourceXDR7PhotoObjAll AS xdr7, BESTDR7..PhotoObjAll as dr7phot, BESTDR7..photoz as photz /* First join tables, */ WHERE xdr7.masterObjID=s.sourceID AND fh.filterID=4 AND fks.filterID=5 AND dr7phot.objID=xdr7.slaveObjID " +
            " AND photz.objID= dr7phot.objID AND dr7phot.objID NOT IN ( SELECT dr7spec.objID FROM BESTDR7..SpecPhotoAll as dr7spec WHERE dr7spec.objID=xdr7.slaveObjID AND dr7spec.sciencePrimary=1) AND " +
            " /* select VIKING primary sources matched to SDSS primary sources */ (priOrSec=0 OR priOrSec=frameSetID) AND sdssPrimary=1 AND /* within 2 of nearest match */ xdr7.distanceMins<0.03333 AND xdr7.distanceMins IN " +
            " (SELECT MIN(distanceMins) FROM vikingSourceXDR7PhotoObjAll WHERE masterObjID=xdr7.masterObjID AND sdssPrimary=1) AND /* for objects classified as galaxies or probable galaxies in VIKING */ mergedClass IN (1,-3) AND " +
            " /* h and ks size is 0.7<sma<=10. arcsec */ ksHlCorSMjRadAs>0.7 AND hHlCorSMjRadAs>0.7 AND ksHlCorSMjRadAs<=10.0 AND hHlCorSMjRadAs<=10.0 /* good quality data in VIKING h and ks */ AND hppErrBits=0 AND ksppErrBits=0 AND " +
            " /* ks extinction corrected AB mag < 20.5 */ (ksPetroMag-aKs+fks.VegaToAB)<20.5 AND /* RA and Dec range to restrict to SDSS */ s.RA>100. AND s.RA<250. AND s.Dec>-5. AND /* z>=0.002 */ photz.z>=0.002 "
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
            " SELECT s.sourceID,s.ra,s.dec,v.frameSetID, v.zMedianMag,v.zMagRms,v.znGoodObs,v.zSkewness, (v.zMaxMag-v.zMinMag) AS zRange,v.yMedianMag, v.yMagRms,v.ynGoodObs,v.ySkewness, (v.yMaxMag-v.yMinMag) AS yRange, " +
            " v.jMedianMag, v.jMagRms,v.jnGoodObs,v.jSkewness, (v.jMaxMag-v.jMinMag) AS jRange,v.hMedianMag, v.hMagRms,v.hnGoodObs,v.hSkewness, (v.hMaxMag-v.hMinMag) AS hRange,v.ksMedianMag, v.ksMagRms,v.ksnGoodObs,v.ksSkewness, " +
            " (v.ksMaxMag-v.ksMinMag) AS ksRange FROM videoVariability AS v,videoSource AS s /* join tables */ WHERE v.sourceID=s.sourceID AND /* point source variables */ s.mergedClass IN (-1,-2) AND v.variableClass=1 AND " +
            " /* delta mag in > 0.1 in ANY filter, with at least 5 good obs in that filter */ (((zMaxMag-zMinMag)>0.1 AND zMinMag>0. AND znGoodObs>=5) OR ((yMaxMag-yMinMag)>0.1 AND yMinMag>0. AND ynGoodObs>=5) OR ((jMaxMag-jMinMag)>0.1 " +
            " AND jMinMag>0. AND jnGoodObs>=5) OR ((hMaxMag-hMinMag)>0.1 AND hMinMag>0. AND hnGoodObs>=5) OR ((ksMaxMag-ksMinMag)>0.1 AND ksMinMag>0. AND ksnGoodObs>=5))  "          
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
            " SELECT /* Select time, filter, magnitude, magnitude error and flags */ d.mjd,d.filterID,d.aperMag3,d.aperMag3Err, d.ppErrBits "
            + "/* From BestMatch table to link all observations of the same source and videoDetection for each observation */ "
            + "FROM videoDetection as d, videoSourceXDetectionBestMatch as b /* First join tables */ WHERE b.multiframeID=d.multiframeID AND b.extNum=d.extNum AND b.seqNum=d.seqNum "
            + "/* then select only detections and sourceID equal to object in previous selection which has a Ks-band RMS>0.4 mag */ AND d.seqNum>0 AND b.sourceID IN "
            + "( SELECT s.sourceID FROM videoVariability AS v,videoSource AS s /* join tables */ WHERE v.sourceID=s.sourceID AND /* point source variables */ "
            + "s.mergedClass IN (-1,-2) AND v.variableClass=1 AND /* delta mag in > 0.1 in ANY filter, with at least 5 good obs in that filter */ "
            + "(((zMaxMag-zMinMag)>0.1 AND zMinMag>0. AND znGoodObs>=5) OR ((yMaxMag-yMinMag)>0.1 AND yMinMag>0. AND ynGoodObs>=5) OR ((jMaxMag-jMinMag)>0.1 AND jMinMag>0. AND "
            + "jnGoodObs>=5) OR ((hMaxMag-hMinMag)>0.1 AND hMinMag>0. AND hnGoodObs>=5) OR ((ksMaxMag-ksMinMag)>0.1 AND ksMinMag>0. AND ksnGoodObs>=5)) /* Ks-band RMS >0.4 mag */ "
            + "AND ksMagRms>0.4 AND s.frameSetID=644245094401) /* order by time */ ORDER BY d.mjd "
            );
        }
      
    }
