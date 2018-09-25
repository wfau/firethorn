/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.adql.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * 
 */
public class TableNameModifierTest
    {

    /**
     * Public constructor.
     * 
     */
    public TableNameModifierTest()
        {
        }

    /**
     * Test a normal name.
     *  
     */
    @Test
    public void testNormal()
        {
        TableNameModifier modifier = new TableNameModifier();
        assertEquals(
            "albert",
            modifier.process(
                "albert"
                )
            );
        }

    /**
     * Test a reserved name.
     *  
     */
    @Test
    public void testReserved()
        {
        TableNameModifier modifier = new TableNameModifier();
        assertEquals(
            "\"region\"",
            modifier.process(
                "region"
                )
            );
        }
    
    /**
     * Test a name with a quote in it.
     *  
     */
    @Test
    public void testQuote()
        {
        TableNameModifier modifier = new TableNameModifier();
        assertEquals(
            "\"quote\\\"in\\\"name\"",
            modifier.process(
                "quote\"in\"name"
                )
            );
        }

    /**
     * Test a name with a dot in it.
     *  
     */
    @Test
    public void testDot()
        {
        TableNameModifier modifier = new TableNameModifier();
        assertEquals(
            "\"dots.in.name\"",
            modifier.process(
                "dots.in.name"
                )
            );
        }

    /**
     * Test a name with an underscore in it.
     *  
     */
    @Test
    public void testUnderscore()
        {
        TableNameModifier modifier = new TableNameModifier();
        assertEquals(
            "under_scored",
            modifier.process(
                "under_scored"
                )
            );
        }

    }
