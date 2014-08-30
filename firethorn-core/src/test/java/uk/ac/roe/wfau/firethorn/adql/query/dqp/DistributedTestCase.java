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
package uk.ac.roe.wfau.firethorn.adql.query.dqp;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
public class DistributedTestCase
extends DistributedQueryTestBase
    {
    @Before
    public void loadTestResources()
    throws Exception
        {
        replace(
            "{ATLAS_SOURCE}",
            adqlResource(
                "atlas.adql.resource"
                ).schemas().select(
                    ATLAS_VERSION
                    ).tables().select(
                        "atlasSource"
                        ).base().alias()
            );

        replace(
            "{ATLAS_CROSS}",
            adqlResource(
                "atlas.adql.resource"
                ).schemas().select(
                    ATLAS_VERSION
                    ).tables().select(
                        "atlasSourceXtwomass_psc"
                        ).base().alias()
            );

        replace(
            "{TWOMASS_SOURCE}",
            adqlResource(
                "twomass.adql.resource"
                ).schemas().select(
                    "remote_twomass"
                    ).tables().select(
                        "twomass_psc"
                        ).base().alias()
            );
        }

    @Test
    public void test000()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 100" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra  AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    local_twomass.twomass_psc",

            " SELECT TOP 100" +
            "    TWOMASS.dbo.twomass_psc.ra  AS ra," +
            "    TWOMASS.dbo.twomass_psc.dec AS dec" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    remote_twomass.twomass_psc",

            " SELECT TOP 100" +
            "    TWOMASS.dbo.twomass_psc.ra  AS ra," +
            "    TWOMASS.dbo.twomass_psc.dec AS dec" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " JOIN" +
            "    atlasSourceXtwomass_psc" +
            " ON" +
            "    atlasSource.sourceID = atlasSourceXtwomass_psc.masterObjID" +
            " JOIN" +
            "    local_twomass.twomass_psc" +
            " ON" +
            "    local_twomass.twomass_psc.pts_key = atlasSourceXtwomass_psc.slaveObjID" +
            "",

            " SELECT TOP 100" + 
            "    {ATLAS_VERSION}.dbo.atlasSource.ra  AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource" + 
            " INNER JOIN" + 
            "    {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc" + 
            " ON" +
            "    {ATLAS_VERSION}.dbo.atlasSource.sourceid = {ATLAS_VERSION}.dbo.atlasSourceXtwomass_psc.masterObjID" + 
            " INNER JOIN" + 
            "    TWOMASS.dbo.twomass_psc" + 
            " ON" +
            "    TWOMASS.dbo.twomass_psc.pts_key = {ATLAS_VERSION}.dbo.atlassourcextwomass_psc.slaveObjID" + 
            "",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " JOIN" +
            "    atlasSourceXtwomass_psc" +
            " ON" +
            "    atlasSource.sourceID = atlasSourceXtwomass_psc.masterObjID" +
            " JOIN" +
            "    remote_twomass.twomass_psc" +
            " ON" +
            "    remote_twomass.twomass_psc.pts_key = atlasSourceXtwomass_psc.slaveObjID" +
            "",

            " SELECT TOP 100" + 
            "    {ATLAS_SOURCE}.ra  AS ra," +
            "    {ATLAS_SOURCE}.dec AS dec" +
            " FROM" +
            "    {ATLAS_SOURCE}" + 
            " INNER JOIN" + 
            "    {ATLAS_CROSS}" + 
            " ON" +
            "    {ATLAS_SOURCE}.sourceid = {ATLAS_CROSS}.masterObjID" + 
            " INNER JOIN" + 
            "    {TWOMASS_SOURCE}" + 
            " ON" +
            "    {TWOMASS_SOURCE}.pts_key = {ATLAS_CROSS}.slaveObjID" +
            "",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }
    }
