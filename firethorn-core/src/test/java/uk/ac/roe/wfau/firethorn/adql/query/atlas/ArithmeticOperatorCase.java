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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class ArithmeticOperatorCase
    extends AtlasQueryTestBase
    {

    /**
     * Simple arithmetic operations.
     *
     */
    @Test
    public void test001S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 10" +
            "    umgPnt," +
            "    umgPnt + umgPntErr," +
            "    umgPnt - umgPntErr," +
            "    umgPnt * umgPntErr," +
            "    umgPnt / umgPntErr" +
            " FROM" +
            "    atlasSource AS atlas",

            " SELECT TOP 10" +
            "    atlas.umgpnt AS umgpnt," +
            "    atlas.umgpnt + atlas.umgpnterr AS SUM," +
            "    atlas.umgpnt - atlas.umgpnterr AS SUB," +
            "    atlas.umgpnt * atlas.umgpnterr AS MULT," +
            "    atlas.umgpnt / atlas.umgpnterr AS DIV" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource as atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("MULT",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("DIV",    AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }

    /**
     * Duplicate operations.
     *
     */
    @Test
    public void test002S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    umgPnt," +
            "    umgPnt + umgPntErr," +
            "    umgPnt - umgPntErr," +
            "    gmrPnt," +
            "    gmrPnt + gmrPntErr," +
            "    gmrPnt - gmrPntErr" +
            " FROM" +
            "    atlasSource AS atlas"
            );
        // TODO Check the error message.
        }

    /**
     * Duplicate operations with aliases.
     *
     */
    @Test
    public void test003S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 10" +
            "    umgPnt," +
            "    umgPnt + umgPntErr," +
            "    umgPnt - umgPntErr," +
            "    gmrPnt," +
            "    gmrPnt + gmrPntErr AS gmrSUM," +
            "    gmrPnt - gmrPntErr AS gmrSUB" +
            " FROM" +
            "    atlasSource AS atlas",

            " SELECT TOP 10" +
            "    atlas.umgpnt AS umgpnt," +
            "    atlas.umgpnt + atlas.umgpnterr AS sum," +
            "    atlas.umgpnt - atlas.umgpnterr AS sub," +
            "    atlas.gmrpnt AS gmrpnt," +
            "    atlas.gmrpnt + atlas.gmrpnterr AS gmrsum," +
            "    atlas.gmrpnt - atlas.gmrpnterr AS gmrsub" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource AS atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }

    /**
     * Case sensitive aliases.
     *
     */
    @Test
    public void test004S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    umgPnt," +
            "    umgPnt + umgPntErr AS s1," +
            "    umgPnt - umgPntErr AS s2," +
            "    gmrPnt," +
            "    gmrPnt + gmrPntErr AS S1," +
            "    gmrPnt - gmrPntErr AS S2" +
            " FROM" +
            "    atlasSource AS atlas"
            );
        }

    /**
     * Simple bracketed expressions.
     *
     */
    @Test
    public void test005S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 10" +
            "    (umgPnt)," +
            "    (umgPnt + umgPntErr)," +
            "    (umgPnt - umgPntErr)," +
            "    (umgPnt * umgPntErr)," +
            "    (umgPnt / umgPntErr)" +
            " FROM" +
            "    atlasSource AS atlas",

            " SELECT TOP 10" +
            "    (atlas.umgpnt) AS umgpnt," +
            "    (atlas.umgpnt + atlas.umgpnterr) AS SUM," +
            "    (atlas.umgpnt - atlas.umgpnterr) AS SUB," +
            "    (atlas.umgpnt * atlas.umgpnterr) AS MULT," +
            "    (atlas.umgpnt / atlas.umgpnterr) AS DIV" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource as atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("MULT",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("DIV",    AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }

    /**
     * Duplicate bracketed expressions.
     *
     */
    @Test
    public void test006S()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 10" +
            "    (umgPnt)," +
            "    (umgPnt + umgPntErr)," +
            "    (umgPnt - umgPntErr)," +
            "    (gmrPnt)," +
            "    (gmrPnt + gmrPntErr)," +
            "    (gmrPnt - gmrPntErr)" +
            " FROM" +
            "    atlasSource AS atlas"
            );
        // TODO Check the error message.
        }

    /**
     * Multiple expressions without aliases.
     * Known to fail, waiting for default names.
     * http://redmine.roe.ac.uk/issues/488
     *
     */
    @Test
    public void test007()
    throws QueryProcessingException, InvalidRequestException, InternalServerErrorException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 10" +
            "    (umgPnt)," +
            "    (umgPnt + umgPntErr)," +
            "    (umgPnt - umgPntErr)," +
            "    (gmrPnt)," +
            "    (gmrPnt + gmrPntErr) AS gmrSUM," +
            "    (gmrPnt - gmrPntErr) AS gmrSUB" +
            " FROM" +
            "    atlasSource AS atlas",

            " SELECT TOP 10" +
            "    (atlas.umgpnt) AS umgpnt," +
            "    (atlas.umgpnt + atlas.umgpnterr) AS sum," +
            "    (atlas.umgpnt - atlas.umgpnterr) AS sub," +
            "    (atlas.gmrpnt) AS gmrpnt," +
            "    (atlas.gmrpnt + atlas.gmrpnterr) AS gmrsum," +
            "    (atlas.gmrpnt - atlas.gmrpnterr) AS gmrsub" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource AS atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.AdqlType.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }
    }
