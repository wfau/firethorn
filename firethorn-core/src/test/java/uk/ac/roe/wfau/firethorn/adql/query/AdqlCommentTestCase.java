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
package uk.ac.roe.wfau.firethorn.adql.query;

import org.junit.Test;

/**
 *
 *
 */
public class AdqlCommentTestCase
    extends AtlasQueryTestBase
    {
    @Test
    public void test000()
    throws Exception
        {
        compare(
            "SELECT\n" +
            "    TOP 100\n" +
            "    ra,\n" +
            "    dec\n" +
            "FROM\n" +
            "    atlasSource\n" +
            "/*\n" +
            "WHERE\n" +
            "    uAperMag3 >0\n" +
            "AND\n" +
            "    gAperMag3 >0\n" +
            "*/" +
            "",

            "SELECT TOP 100 ATLASv20130426.dbo.atlasSource.ra AS ra , ATLASv20130426.dbo.atlasSource.dec AS dec FROM ATLASv20130426.dbo.atlasSource"
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        compare(
            "SELECT\n" +
            "    TOP 100\n" +
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

            "SELECT TOP 100 ATLASv20130426.dbo.atlasSource.ra AS ra , ATLASv20130426.dbo.atlasSource.dec AS dec FROM ATLASv20130426.dbo.atlasSource"
            );
        }
    }
