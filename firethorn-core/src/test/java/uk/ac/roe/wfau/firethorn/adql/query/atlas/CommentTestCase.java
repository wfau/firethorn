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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
public class CommentTestCase
    extends AtlasQueryTestBase
    {
    @Test
    public void test000S()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,
            
            "SELECT TOP 100\n" +
            "    ra,\n" +
            "    dec\n" +
            "FROM\n" +
            "    atlasSource\n" +
            "/*\n" +
            "WHERE\n" +
            "    uAperMag3 >0\n" +
            "AND\n" +
            "    gAperMag3 >0\n" +
            "*/",

            "SELECT TOP 100\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra,\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource",
            
            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dec", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }

    @Test
    public void test001S()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            "SELECT TOP 100\n" +
            "    ra,\n" +
            "    dec\n" +
            "    /*cx,*/\n" +
            "    /*cy,*/\n" +
            "    /*cz*/\n" +
            "FROM\n" +
            "    atlasSource\n" +
            "/*\n" +
            "WHERE\n" +
            "    uAperMag3 >0\n" +
            "AND\n" +
            "    gAperMag3 >0\n" +
            "*/" +
            "",

            "SELECT TOP 100\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource.ra AS ra,\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("dec", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    }
