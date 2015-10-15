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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 * Test hangs in execution thread - ignore.
 *
 */
public class ExecuteQueryTestCase
    extends AtlasQueryTestBase
    {
    
    //@Test
    public void notTest000()
    throws Exception
        {
        final AdqlQuery query = testschema().queries().create(
            factories().queries().params().create(
                Level.STRICT,
                Mode.AUTO
                ),
            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource"
            );

        factories().queries().executor().update(
            query.ident(),
            Status.RUNNING,
            10
            );
        }
    }
