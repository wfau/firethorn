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
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class BinaryOperatorsTestCase
    extends AtlasQueryTestBase
    {
    /**
     * Binary OR is not supported in ADQL.
     *
     */
    @Test
    public void test001S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    iPetroMag,\n" + 
            "    rmiExt\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass=1\n" + 
            "AND\n" + 
            "    iPetroMag>-9.99995e+8\n" + 
            "AND\n" + 
            "    rmiExt>-9.99995e+8\n" + 
            "AND\n" + 
            "    (rppErrBits | ippErrBits) < 65536\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }

    /**
     * Binary OR is supported in LEGACY mode.
     * (rppErrBits | ippErrBits)
     *
     */
    @Test
    public void test001L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    iPetroMag,\n" + 
            "    rmiExt\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass=1\n" + 
            "AND\n" + 
            "    iPetroMag>-9.99995e+8\n" + 
            "AND\n" + 
            "    rmiExt>-9.99995e+8\n" + 
            "AND\n" + 
            "    (rppErrBits | ippErrBits) < 65536\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("iPetroMag", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("rmiExt",    AdqlColumn.Type.FLOAT, 0),
                }
            );
        compare(
            query,
            "select atlasdr1.dbo.atlassource.ipetromag as ipetromag, atlasdr1.dbo.atlassource.rmiext as rmiext from atlasdr1.dbo.atlassource where atlasdr1.dbo.atlassource.mergedclass = 1 and atlasdr1.dbo.atlassource.ipetromag > -9.99995e+8 and atlasdr1.dbo.atlassource.rmiext > -9.99995e+8 and atlasdr1.dbo.atlassource.rpperrbits | atlasdr1.dbo.atlassource.ipperrbits < 65536"
            );
        }

    /**
     * Binary OR is not supported in ADQL.
     *
     */
    //@Test
    public void test002S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }
    
    /**
     * Binary OR is supported in LEGACY mode.
     * (rppErrBits | ippErrBits)
     *
     */
    @Test
    public void test002L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("BINARY_OR", AdqlColumn.Type.INTEGER, 0)
                }
            );
        compare(
            query,
            "select atlasdr1.dbo.atlassource.rpperrbits | atlasdr1.dbo.atlassource.ipperrbits as binary_or from atlasdr1.dbo.atlassource where atlasdr1.dbo.atlassource.mergedclass = 1"
            );
        }


    /**
     * Binary AND is not supported in ADQL.
     *
     */
    //@Test
    public void test003S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }
    
    /**
     * Binary AND is supported in LEGACY mode.
     * (rppErrBits | ippErrBits)
     *
     */
    @Test
    public void test003L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    rppErrBits & ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("BINARY_AND", AdqlColumn.Type.INTEGER, 0)
                }
            );
        compare(
            query,
            "select atlasdr1.dbo.atlassource.rpperrbits & atlasdr1.dbo.atlassource.ipperrbits as binary_and from atlasdr1.dbo.atlassource where atlasdr1.dbo.atlassource.mergedclass = 1"
            );
        }

    /**
     * Binary XOR is not supported in ADQL.
     *
     */
    //@Test
    public void test004S()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.STRICT
                ),
            "SELECT\n" + 
            "    rppErrBits ^ ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.PARSE_ERROR,
            query.syntax().state()
            );
        }
    
    /**
     * Binary XOR is supported in LEGACY mode.
     * (rppErrBits | ippErrBits)
     *
     */
    @Test
    public void test004L()
        {
        final AdqlQuery query = this.queryspace.queries().create(
            factories().queries().params().param(
                Level.LEGACY
                ),
            "SELECT\n" + 
            "    rppErrBits ^ ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
            );
        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("BINARY_XOR", AdqlColumn.Type.INTEGER, 0)
                }
            );
        compare(
            query,
            "select atlasdr1.dbo.atlassource.rpperrbits ^ atlasdr1.dbo.atlassource.ipperrbits as binary_xor from atlasdr1.dbo.atlassource where atlasdr1.dbo.atlassource.mergedclass = 1"
            );
        }
    }
