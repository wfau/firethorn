/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query.atlas ;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
public class UserFunctionTestCase
extends AtlasQueryTestBase
    {

	/**
	 * UDF should have fully qualified JDBC name.
	 * Known to fail.
	 * http://redmine.roe.ac.uk/issues/496
	 *
	 */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

			" SELECT TOP 5" +
            "    fHMS(ra)," +
            "    fDMS(dec)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.fHMS({ATLAS_VERSION}.dbo.atlasSource.ra)  AS fhms," +
            "    {ATLAS_VERSION}.dbo.fDMS({ATLAS_VERSION}.dbo.atlasSource.dec) AS fdms" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("fHMS", AdqlColumn.AdqlType.CHAR, 0),
                new ExpectedField("fDMS", AdqlColumn.AdqlType.CHAR, 0)
                }
            );
        }


	/**
	 * UDF should have fully qualified JDBC name.
	 * Known to fail.
	 * http://redmine.roe.ac.uk/issues/496
	 *
	 */
    @Test
    public void test002()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    fHMS(ra)  AS fra," +
            "    fDMS(dec) AS fdec" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    {ATLAS_VERSION}.dbo.fHMS({ATLAS_VERSION}.dbo.atlasSource.ra)  AS fra," +
            "    {ATLAS_VERSION}.dbo.fDMS({ATLAS_VERSION}.dbo.atlasSource.dec) AS fdec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("fra",  AdqlColumn.AdqlType.CHAR, 0),
                new ExpectedField("fdec", AdqlColumn.AdqlType.CHAR, 0)
                }
            );
        }
    }

