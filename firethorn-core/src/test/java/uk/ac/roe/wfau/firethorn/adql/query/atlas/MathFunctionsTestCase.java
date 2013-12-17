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

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
@Slf4j
public class MathFunctionsTestCase
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
        
            "SELECT pi()", 

            "SELECT PI() AS PI", 

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
        
            "SELECT\n" + 
            "    pi()\n" + 
            "FROM\n" + 
            "    atlassource", 

            "SELECT\n" + 
            "    PI() AS PI\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    power(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource",
            
            "SELECT TOP 5\n" + 
            "    POWER({ATLAS_VERSION}.dbo.atlassource.ra, 2) AS POWER\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    log(ra)\n" + 
            "FROM\n" + 
            "    atlasSource",

            "SELECT TOP 5\n" + 
            "    LOG({ATLAS_VERSION}.dbo.atlassource.ra) AS LOG\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    log10(ra)\n" + 
            "FROM\n" + 
            "    atlasSource",

            "SELECT TOP 5\n" + 
            "    LOG10({ATLAS_VERSION}.dbo.atlassource.ra) AS LOG10\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    round(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    round(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource",

            "SELECT TOP 5\n" + 
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.ra, 2, 0) AS ROUND\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    truncate(ra)\n" + 
            "FROM\n" + 
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
        
            "SELECT TOP 5\n" + 
            "    truncate(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource",

            "SELECT TOP 5\n" + 
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.ra, 2, 1) AS TRUNCATE\n" + 
            "FROM\n" + 
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

            "SELECT\n" + 
            "    rand()\n" + 
            "FROM\n" + 
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

            "SELECT\n" + 
            "    rand(2)\n" + 
            "FROM\n" + 
            "    atlassource", 

            "SELECT\n" + 
            "    RAND(2) AS RAND\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("RAND", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    
    /**
     * sign() in STRICT mode.
     *
     */
    @Test
    public void test007S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            "SELECT\n" + 
            "    sign(ra)\n" + 
            "FROM\n" + 
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

            "SELECT\n" + 
            "    sign(ra)\n" + 
            "FROM\n" + 
            "    atlassource",
            
            "SELECT\n" + 
            "    SIGN(2) AS SIGN\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource", 

            new ExpectedField[] {
                new ExpectedField("SIGN", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }
    }
