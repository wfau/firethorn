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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidStateRequestException;

/**
 *
 *
 */
public class SimpleQueryTestCase
    extends BlueQueryTestBase
    {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
    @Test
    public void testEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	exception.expect(InvalidStateRequestException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
            null,
			TaskState.EDITING,
            null
			);
        }
    
    @Test
    public void testReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
            null,
			TaskState.READY,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
        }

    //@Test
    public void testQueued()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	exception.expect(InvalidStateRequestException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
            null,
			TaskState.QUEUED,
            null
			);
        }

    @Test
    public void testCompleted()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
            null,
			TaskState.COMPLETED,
            null
			);
		assertTrue(
			query.state().ordinal() >= TaskState.QUEUED.ordinal() 	
			);
        }

    //@Test
    public void testCancelled()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
            null,
			TaskState.CANCELLED,
            null
			);
    	assertEquals(
			TaskState.CANCELLED,
			query.state()
			);
        }

    //@Test
    public void testFailed()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	exception.expect(InvalidStateRequestException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
            null,
			TaskState.FAILED,
            null
			);
        }

    //@Test
    public void testError()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY,
            null,
            null,
            null,
            null,
            null,
            null
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	exception.expect(InvalidStateRequestException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
            null,
			TaskState.ERROR,
            null
			);
        }
    }
