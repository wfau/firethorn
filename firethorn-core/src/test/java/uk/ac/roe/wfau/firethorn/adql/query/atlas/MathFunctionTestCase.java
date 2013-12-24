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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
@Slf4j
public class MathFunctionTestCase
    extends AtlasQueryTestBase
    {

    /**
     * pi() with no FROM
     * TODO
     *
     */
    public void test000SA()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT pi()",

            " SELECT PI() AS PI",

            new ExpectedField[] {
                new ExpectedField("PI", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * pi()
     *
     */
    @Test
    public void test000SB()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    pi()" +
            " FROM" +
            "    atlassource",

            " SELECT" +
            "    PI() AS PI" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("PI", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * power()
     *
     *
     */
    @Test
    public void test001S()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    power(ra, 2)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    POWER({ATLAS_VERSION}.dbo.atlassource.ra, 2) AS POWER" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("POWER", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * log()
     *
     */
    @Test
    public void test002S()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    log(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    LOG({ATLAS_VERSION}.dbo.atlassource.ra) AS LOG" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("LOG", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * log10()
     *
     */
    @Test
    public void test003S()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    log10(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    LOG10({ATLAS_VERSION}.dbo.atlassource.ra) AS LOG10" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("LOG10", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * round() without length.
     *
     */
    @Test
    public void test004SA()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 5" +
            "    round(ra)" +
            " FROM" +
            "    atlasSource" +
            ""
            );
        }

    /**
     * round() with length.
     *
     */
    @Test
    public void test004SB()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    round(ra, 2)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.ra, 2, 0) AS ROUND" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("ROUND", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * truncate() without length.
     *
     */
    @Test
    public void test005SA()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT TOP 5" +
            "    truncate(ra)" +
            " FROM" +
            "    atlasSource"
            );
        }

    /**
     * truncate() with length.
     *
     */
    @Test
    public void test005SB()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    truncate(ra, 2)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.ra, 2, 1) AS TRUNCATE" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("TRUNCATE", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * rand() without seed param
     *
     */
    @Test
    public void test06SA()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT" +
            "    rand()" +
            " FROM" +
            "    atlassource"
            );
        }

    /**
     * rand() with seed param
     *
     */
    @Test
    public void test06SB()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    rand(2)" +
            " FROM" +
            "    atlassource",

            " SELECT" +
            "    RAND(2) AS RAND" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("RAND", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * sign() in STRICT mode.
     * TODO
     *
     */
    public void test007S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT" +
            "    sign(ra)" +
            " FROM" +
            "    atlassource"
            );
        }

    /**
     * sign() in LEGACY mode.
     * TODO
     *
     */
    public void test007L()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    sign(ra)" +
            " FROM" +
            "    atlassource",

            " SELECT" +
            "    SIGN({ATLAS_VERSION}.dbo.atlassource.ra) AS SIGN" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("SIGN", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * square() in STRICT mode.
     * TODO
     *
     */
    @Test
    public void test008S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT" +
            "    square(ra)" +
            " FROM" +
            "    atlassource"
            );
        }

    /**
     * square() in LEGACY mode.
     * TODO
     *
     */
    public void test008L()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    square(ra)" +
            " FROM" +
            "    atlassource",

            " SELECT" +
            "    SQUARE({ATLAS_VERSION}.dbo.atlassource.ra) AS SIGN" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("SQUARE", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }
    }
