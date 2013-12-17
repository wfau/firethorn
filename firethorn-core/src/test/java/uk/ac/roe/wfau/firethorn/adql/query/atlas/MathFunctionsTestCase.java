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
     * max()
     *
     */
    @Test
    public void test001()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    max(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("MAX", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 max({ATLAS_VERSION}.dbo.atlassource.ra) as MAX from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    @Test
    public void test002()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    min(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("MIN", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 min({ATLAS_VERSION}.dbo.atlassource.ra) as min from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    @Test
    public void test003()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    sum(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 sum({ATLAS_VERSION}.dbo.atlassource.ra) as sum from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    @Test
    public void test004()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    power(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("POWER", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 power({ATLAS_VERSION}.dbo.atlassource.ra, 2) as power from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * log()
     *
     */
    @Test
    public void test005()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    log(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("LOG", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 log({ATLAS_VERSION}.dbo.atlassource.ra) as LOG from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * log10()
     *
     */
    @Test
    public void test006()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    log10(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("LOG10", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 log10({ATLAS_VERSION}.dbo.atlassource.ra) as LOG10 from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * round() without length.
     *
     */
    @Test
    public void test007A()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    round(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * round() with length.
     *
     */
    @Test
    public void test007B()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    round(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("ROUND", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 round({ATLAS_VERSION}.dbo.atlassource.ra, 2, 0) as ROUND from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * truncate() without length.
     *
     */
    @Test
    public void test008A()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    truncate(ra)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * truncate() with length.
     *
     */
    @Test
    public void test008B()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT TOP 5\n" + 
            "    truncate(ra, 2)\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("TRUNCATE", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select top 5 round({ATLAS_VERSION}.dbo.atlassource.ra, 2, 1) as TRUNCATE from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * pi()
     *
     */
    @Test
    public void test009()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    pi()\n" + 
            "FROM\n" + 
            "    atlassource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("PI", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select pi() as pi from atlasdr1.dbo.atlassource"
            );
        }

    /**
     * rand() without seed param
     *
     */
    @Test
    public void test010A()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    rand()\n" + 
            "FROM\n" + 
            "    atlassource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * rand() with seed param
     *
     */
    @Test
    public void test010B()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    rand(2)\n" + 
            "FROM\n" + 
            "    atlassource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("RAND", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        validate(
            query,
            "select rand(2) as rand from atlasdr1.dbo.atlassource"
            );
        }
    
    /**
     * sign() in STRICT mode.
     *
     */
    @Test
    public void test011S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    sign(2)\n" + 
            "FROM\n" + 
            "    atlassource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }
    
    /**
     * sign() in LEGACY mode.
     * TODO
     *
     */
    public void test011L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    sign(2)\n" + 
            "FROM\n" + 
            "    atlassource\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("SIGN", AdqlColumn.Type.INTEGER, 0)
                }
            );
        validate(
            query,
            "select sign(2) as sign from atlasdr1.dbo.atlassource"
            );
        }
    }
