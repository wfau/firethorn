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
 * http://redmine.roe.ac.uk/issues/657
 *
 */
public class RedmineBug657TestCase
    extends AtlasQueryTestBase
    {

    /**
     * 
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " Select ls.sourceid, ls.framesetid, rtrim( substring( " +
            "     mfy.filename, " +
            "     charindex( " +
            "         'w2', " +
            "          mfy.filename,1 " +
            "         ), " +
            "    32 " +
            "     ) " +
            "   ) as yfilename, " +
            " from " +
            " LasSource as ls, " +
            "  Lasmergelog as lml, " +
            "   Multiframe as mfh, " +
            "   Multiframe as mfk " +
            " where " +
            "   ls.framesetid=lml.framesetid" 
            
            );
        

        }
    }