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
package uk.ac.roe.wfau.firethorn.adql.query.redmine;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/628
 *
 */
public class RedmineBug628TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/628
     * Known to fail
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            "select g.centralra as gRA, g.centraldec as gDec,  r.centralRa as rRA, "
            + " r.centralDec as RDec,60.0*  dbo.fgreatcircledist(g.centralra,g.centraldec,r.centralra,"
            + " r.centraldec)  as sepArcsec  from atlasmergelog as l,  CurrentAstrometry as g, "
            + " CurrentAstrometry as r  where g.multiframeID=gmfid and g.extNum=genum  and "
            + " r.multiframeID=rmfid and r.extNum=renum  and gmfid>0 and rmfid>0  "
            + " order by  dbo.fgreatcircledist(g.centralra,g.centraldec,r.centralra,r.centraldec)  desc  "
            + " /*select s.ra as radeg,s.dec as decdeg,gAperMag3 ,rAperMag3/*,gmfid,genum*/ "
            + " from atlassource as s,atlasMergeLog as l  "
            + " where gAperMag3 between 0 and 21  and l.frameSetID=s.frameSetID and "
            + " (priOrSec = 0 or priOrSec=s.frameSetID) */" 

          );
        }

 
    }
