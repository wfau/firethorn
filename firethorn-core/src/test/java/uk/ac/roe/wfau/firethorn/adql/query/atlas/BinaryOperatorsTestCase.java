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
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

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
            "    (rppErrBits | ippErrBits) < 65536" 
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
        validate(
            Level.LEGACY,
            State.VALID,

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
            "    (rppErrBits | ippErrBits) < 65536", 

            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.ipetromag AS ipetromag,\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rmiext AS rmiext\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource\n" + 
            "WHERE\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.mergedclass = 1\n" + 
            "AND\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.ipetromag > -9.99995e+8\n" + 
            "AND\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rmiext > -9.99995e+8\n" + 
            "AND\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits | {ATLAS_VERSION}.dbo.atlassource.ipperrbits < 65536", 

            new ExpectedField[] {
                new ExpectedField("iPetroMag", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("rmiExt",    AdqlColumn.Type.FLOAT, 0),
                }
            );
        }

    /**
     * Binary OR is not supported in ADQL.
     * TODO
     * 
     */
    public void test002S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1" 
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
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1", 

            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits | {ATLAS_VERSION}.dbo.atlassource.ipperrbits AS BIT_OR\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource\n" + 
            "WHERE\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.mergedclass = 1",
            
            new ExpectedField[] {
                new ExpectedField("BIT_OR", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }


    /**
     * Binary AND is not supported in ADQL.
     * TODO
     *
     */
    public void test003S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            "SELECT\n" + 
            "    rppErrBits | ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1"
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
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    rppErrBits & ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1",

            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits & {ATLAS_VERSION}.dbo.atlassource.ipperrbits AS BIT_AND\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource\n" + 
            "WHERE\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.mergedclass = 1",

            new ExpectedField[] {
                new ExpectedField("BIT_AND", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * Binary XOR is not supported in ADQL.
     * TODO
     *
     */
    public void test004S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            "SELECT\n" + 
            "    rppErrBits ^ ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1\n" + 
            ""
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
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    rppErrBits ^ ippErrBits\n" + 
            "FROM\n" + 
            "    atlasSource\n" + 
            "WHERE\n" + 
            "    mergedClass = 1",
            
            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits ^ {ATLAS_VERSION}.dbo.atlassource.ipperrbits AS BIT_XOR\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource\n" + 
            "WHERE\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.mergedclass = 1",

            new ExpectedField[] {
                new ExpectedField("BIT_XOR", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * Binary AND with decimal integer.
     *
     */
    @Test
    public void test005D()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    rppErrBits & 4\n" + 
            "FROM\n" + 
            "    atlasSource", 

            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits & 4 AS BIT_AND\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("BIT_AND", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * Binary OR with hexadecimal integer.
     *
     */
    @Test
    public void test005H()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT\n" + 
            "    rppErrBits & 0x04\n" + 
            "FROM\n" + 
            "    atlasSource", 

            "SELECT\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource.rpperrbits & 0x04 AS BIT_AND\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("BIT_AND", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }
    }
