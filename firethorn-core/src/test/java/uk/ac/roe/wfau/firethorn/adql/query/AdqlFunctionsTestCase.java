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
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
@Slf4j
public class AdqlFunctionsTestCase
    extends AtlasQueryTestBase
    {
    /**
     * log10() in strict mode.
     *
     */
    @Test
    public void test001S()
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
        compare(
            query,
            "select top 5 log10({ATLAS_VERSION}.dbo.atlassource.ra) as log10 from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * log10() in legacy mode.
     *
     */
    @Test
    public void test001L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
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
        compare(
            query,
            "select top 5 log10({ATLAS_VERSION}.dbo.atlassource.ra) as log10 from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * log() in strict mode.
     *
     */
    @Test
    public void test002S()
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
        compare(
            query,
            "select top 5 log({ATLAS_VERSION}.dbo.atlassource.ra) as log from {ATLAS_VERSION}.dbo.atlassource"
            );
        }

    /**
     * log() in legacy mode.
     *
     */
    @Test
    public void test002L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
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
        compare(
            query,
            "select top 5 log({ATLAS_VERSION}.dbo.atlassource.ra) as log from {ATLAS_VERSION}.dbo.atlassource"
            );
        }
    }
