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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AtlasQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class AtlasSelectOperCase
    extends AtlasQueryTestBase
    {

    /**
     * Simple operations.
     *
     */
    @Test
    public void test001()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB", AdqlColumn.Type.FLOAT, 0)
                }
            );
        compare(
            query,
            "select top 10 atlas.umgpnt as umgpnt, atlas.umgpnt+atlas.umgpnterr as sum, atlas.umgpnt-atlas.umgpnterr as sub from atlasv20130426.dbo.atlassource as atlas"
            );
        }

    /**
     * Duplicate operations.
     *
     */
    @Test
    public void test002()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr,\n" +
            "    gmrPnt - gmrPntErr\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        // TODO Check the error message.
        }

    /**
     * Duplicate operations with aliases.
     *
     */
    @Test
    public void test003()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr AS gmrSUM,\n" +
            "    gmrPnt - gmrPntErr AS gmrSUB\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.Type.FLOAT, 0)
                }
            );
        compare(
            query,
            "select top 10 atlas.umgpnt as umgpnt, atlas.umgpnt+atlas.umgpnterr as sum, atlas.umgpnt-atlas.umgpnterr as sub, atlas.gmrpnt as gmrpnt, atlas.gmrpnt+atlas.gmrpnterr as gmrsum, atlas.gmrpnt-atlas.gmrpnterr as gmrsub from atlasv20130426.dbo.atlassource as atlas"
            );
        }

    /**
     * Case sensitive aliases.
     *
     */
    @Test
    public void test004()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr AS s1,\n" +
            "    umgPnt - umgPntErr AS s2,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr AS S1,\n" +
            "    gmrPnt - gmrPntErr AS S2\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    
    /**
     * Simple expression.
     *
     */
    @Test
    public void test005()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr)\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select top 10 atlas.umgpnt as umgpnt, atlas.umgpnt+atlas.umgpnterr as sum, atlas.umgpnt-atlas.umgpnterr as sub from atlasv20130426.dbo.atlassource as atlas"
            );
        }
    
    /**
     * Duplicate expresions.
     *
     */
    @Test
    public void test006()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr),\n" +
            "    (gmrPnt),\n" +
            "    (gmrPnt + gmrPntErr),\n" +
            "    (gmrPnt - gmrPntErr)\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        // TODO Check the error message.
        }

    /**
     * Duplicate expressions with aliases.
     *
     */
    @Test
    public void test007()
        {
        final AdqlQuery query = this.queryschema.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr),\n" +
            "    (gmrPnt),\n" +
            "    (gmrPnt + gmrPntErr) AS gmrSUM,\n" +
            "    (gmrPnt - gmrPntErr) AS gmrSUB\n" +
            "FROM\n" +
            "    atlasSource AS atlas\n" +
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.Type.FLOAT, 0)
                }
            );
        compare(
            query,
            "select top 10 atlas.umgpnt as umgpnt, atlas.umgpnt+atlas.umgpnterr as sum, atlas.umgpnt-atlas.umgpnterr as sub, atlas.gmrpnt as gmrpnt, atlas.gmrpnt+atlas.gmrpnterr as gmrsum, atlas.gmrpnt-atlas.gmrpnterr as gmrsub from atlasv20130426.dbo.atlassource as atlas"
            );
        }

    }
