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
 * 
 *
 */
public class RedmineBug454TestCase
extends AtlasQueryTestBase
    {

    /**
     * Not sure what the correct response should be ....
     * Known to fail.
     * http://redmine.roe.ac.uk/issues/454
     *
     */
    @Test
    public void test001()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    3000" +
            " FROM" +
            "    atlasSource",

            "unknown" +

            new ExpectedField[] {
                new ExpectedField("unknown",  AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    }
