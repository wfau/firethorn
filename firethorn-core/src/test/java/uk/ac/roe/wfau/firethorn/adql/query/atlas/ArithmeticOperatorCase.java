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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
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
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr,\n" +
            "    umgPnt * umgPntErr,\n" +
            "    umgPnt / umgPntErr\n" +
            "FROM\n" +
            "    atlasSource AS atlas",

            "SELECT TOP 10\n" + 
            "    atlas.umgpnt AS umgpnt,\n" + 
            "    atlas.umgpnt + atlas.umgpnterr AS SUM,\n" + 
            "    atlas.umgpnt - atlas.umgpnterr AS SUB,\n" + 
            "    atlas.umgpnt * atlas.umgpnterr AS MUL,\n" + 
            "    atlas.umgpnt / atlas.umgpnterr AS DIV\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource as atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("MUL",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("DIV",    AdqlColumn.Type.FLOAT, 0)
                }
            );
        }

    /**
     * Duplicate operations.
     *
     */
    @Test
    public void test002S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,
        
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr,\n" +
            "    gmrPnt - gmrPntErr\n" +
            "FROM\n" +
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
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr,\n" +
            "    umgPnt - umgPntErr,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr AS gmrSUM,\n" +
            "    gmrPnt - gmrPntErr AS gmrSUB\n" +
            "FROM\n" +
            "    atlasSource AS atlas",
            
            "SELECT TOP 10\n" + 
            "    atlas.umgpnt AS umgpnt,\n" + 
            "    atlas.umgpnt + atlas.umgpnterr AS sum,\n" + 
            "    atlas.umgpnt - atlas.umgpnterr AS sub,\n" + 
            "    atlas.gmrpnt AS gmrpnt,\n" + 
            "    atlas.gmrpnt + atlas.gmrpnterr AS gmrsum,\n" + 
            "    atlas.gmrpnt - atlas.gmrpnterr AS gmrsub\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource AS atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }

    /**
     * Case sensitive aliases.
     *
     */
    @Test
    public void test004S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,
        
            "SELECT TOP 10\n" +
            "    umgPnt,\n" +
            "    umgPnt + umgPntErr AS s1,\n" +
            "    umgPnt - umgPntErr AS s2,\n" +
            "    gmrPnt,\n" +
            "    gmrPnt + gmrPntErr AS S1,\n" +
            "    gmrPnt - gmrPntErr AS S2\n" +
            "FROM\n" +
            "    atlasSource AS atlas"
            );
        }
    
    /**
     * Simple bracketed expressions.
     *
     */
    @Test
    public void test005S()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr),\n" +
            "    (umgPnt * umgPntErr),\n" +
            "    (umgPnt / umgPntErr)\n" +
            "FROM\n" +
            "    atlasSource AS atlas",
            
            "SELECT TOP 10\n" + 
            "    atlas.umgpnt AS umgpnt,\n" + 
            "    atlas.umgpnt + atlas.umgpnterr AS SUM,\n" + 
            "    atlas.umgpnt - atlas.umgpnterr AS SUB,\n" + 
            "    atlas.umgpnt * atlas.umgpnterr AS MUL,\n" + 
            "    atlas.umgpnt / atlas.umgpnterr AS DIV,\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource as atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("MUL",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("DIV",    AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    
    /**
     * Duplicate bracketed expressions.
     *
     */
    @Test
    public void test006S()
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,
        
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr),\n" +
            "    (gmrPnt),\n" +
            "    (gmrPnt + gmrPntErr),\n" +
            "    (gmrPnt - gmrPntErr)\n" +
            "FROM\n" +
            "    atlasSource AS atlas"
            );
        // TODO Check the error message.
        }

    /**
     * Duplicate bracketed expressions with aliases.
     *
     */
    @Test
    public void test007()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            "SELECT TOP 10\n" +
            "    (umgPnt),\n" +
            "    (umgPnt + umgPntErr),\n" +
            "    (umgPnt - umgPntErr),\n" +
            "    (gmrPnt),\n" +
            "    (gmrPnt + gmrPntErr) AS gmrSUM,\n" +
            "    (gmrPnt - gmrPntErr) AS gmrSUB\n" +
            "FROM\n" +
            "    atlasSource AS atlas",
            
            "SELECT TOP 10\n" + 
            "    atlas.umgpnt AS umgpnt,\n" + 
            "    atlas.umgpnt + atlas.umgpnterr AS sum,\n" + 
            "    atlas.umgpnt - atlas.umgpnterr AS sub,\n" + 
            "    atlas.gmrpnt AS gmrpnt,\n" + 
            "    atlas.gmrpnt + atlas.gmrpnterr AS gmrsum,\n" + 
            "    atlas.gmrpnt - atlas.gmrpnterr AS gmrsub\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlassource AS atlas",

            new ExpectedField[] {
                new ExpectedField("umgPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUM",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("SUB",    AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrPnt", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUM", AdqlColumn.Type.FLOAT, 0),
                new ExpectedField("gmrSUB", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    }
