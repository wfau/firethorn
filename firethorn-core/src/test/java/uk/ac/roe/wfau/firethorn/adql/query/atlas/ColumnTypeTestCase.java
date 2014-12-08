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

import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class ColumnTypeTestCase
    extends AtlasQueryTestBase
    {

    /**
     * SHORT
     * SHORT # INT.
     * SHORT # LONG.
     * SHORT # FLOAT.
     * SHORT # DOUBLE.
     *
     */
    @Test
    public void test001S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 10" +
            "    uClass," +
            "    uClass + cuEventID,"  +
            "    uClass - frameSetID," +
            "    uClass * umgPnt," +
            "    uClass / lambda" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 10" +
            "    {ATLAS_VERSION}.dbo.atlassource.uclass AS uclass," +
            "    {ATLAS_VERSION}.dbo.atlassource.uclass + {ATLAS_VERSION}.dbo.atlassource.cueventid  AS SUM," +
            "    {ATLAS_VERSION}.dbo.atlassource.uclass - {ATLAS_VERSION}.dbo.atlassource.framesetid AS SUB," +
            "    {ATLAS_VERSION}.dbo.atlassource.uclass * {ATLAS_VERSION}.dbo.atlassource.umgpnt     AS MULT," +
            "    {ATLAS_VERSION}.dbo.atlassource.uclass / {ATLAS_VERSION}.dbo.atlassource.lambda     AS DIV" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("uClass", AdqlColumn.AdqlType.SHORT,   0),
                new ExpectedField("SUM",    AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("SUB",    AdqlColumn.AdqlType.LONG,    0),
                new ExpectedField("MULT",    AdqlColumn.AdqlType.FLOAT,   0),
                new ExpectedField("DIV",    AdqlColumn.AdqlType.DOUBLE,  0)
                }
            );
        }

    /*
     * Testable fields in ArchiveCurationHistory
     *
     */

    }
