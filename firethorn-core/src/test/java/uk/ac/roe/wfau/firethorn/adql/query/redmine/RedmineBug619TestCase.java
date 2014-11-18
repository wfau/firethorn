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

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/619
 *
 */
public class RedmineBug619TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/619
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " select 'imcopy '''+ filename+'['+CAST(ienum-1 as varchar(2))+']'' '+'i/'+CAST(ienum-1 as varchar(2))+ '_'+SUBSTRING(filename,charindex('/o2',filename)+1,200)"
            + " from atlasMergeLog as l, Multiframe as m"
            + " where gmfID>0 and rmfID>0 and imfID >0 and imfID=m.multiframeID order by filename,ienum"
            );
        }
    }
