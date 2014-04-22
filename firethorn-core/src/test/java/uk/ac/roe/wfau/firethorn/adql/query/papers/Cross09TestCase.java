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
public class Cross09TestCase
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
            " SELECT v.sourceID,v.kMeanMag,v.kMagRms " +
            " FROM atlasSource AS s,atlasVariability AS v, atlasVarFrameSetInfo AS i " +
            " WHERE s.sourceID=v.sourceID AND v.frameSetID=i.frameSetID AND s.mergedClass=-1 AND v.variableClass=1 AND v.kMeanMag<(i.kexpML-3.) AND v.kMeanMag>0. AND s.priOrSec=0 " 

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
            " SELECT m.mjdObs,d.aperMag3,d.aperMag3Err, d.ppErrBits,x.flag " +
            " FROM atlasSourceXDetectionBestMatch AS x, atlasDetection AS d,Multiframe AS m " +
            " WHERE x.sourceID=123 AND x.multiframeID= d.multiframeID AND x.extNum=d.extNum AND x.seqNum=d.seqNum AND x.multiframeID= m.multiframeID AND d.filterID=5 " +
            " ORDER BY m.mjdObs "

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
             " SELECT ml.meanMjdObs,e.kaperMag3,e.kaperMag3Err e.kppErrBits,x.flag " + 
             " FROM atlasSourceXSynopticSourceBestMatch AS x, atlasSynopticSource AS e, atlasSynopticMergeLog AS ml " +
             " WHERE x.sourceID=123 AND x.synFrameSetID= e.synFrameSetID AND x.synSeqNum=e.synSeqNum AND e.synFrameSetID=ml.synFrameSetID " +
             " ORDER BY ml.meanMjdObs "
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
             " SELECT ml.meanMjdObs,e.japerMag3,e.japerMag3Err, e.jppErrBits,e.kaperMag3,e.kaperMag3Err, e.kppErrBits,x.flag " +
             " FROM atlasSourceXSynopticSourceBestMatch AS x, atlasSynopticSource AS e, atlasSynopticMergeLog AS ml " +
             " WHERE x.sourceID=123 AND x.synFrameSetID= e.synFrameSetID AND x.synSeqNum=e.synSeqNum AND e.synFrameSetID=ml.synFrameSetID " +
             " ORDER BY ml.meanMjdObs "

             );
         }
       
    
    }
