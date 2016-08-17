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
package uk.ac.roe.wfau.firethorn.blue.test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;

/**
 *
 *
 */
public class BlueQueryTestCase
    extends BlueQueryTestBase
    {

    @Test
    public void testNoQuery()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
            null,
            null,
			null
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }

    @Test
    public void testValidQuery()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
        }

    @Test
    public void testInvalidQuery()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY,
            null,
            null
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }
    }
