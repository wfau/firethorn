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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.InvalidStateTransitionException;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;

/**
 *
 *
 */
public class InvalidQueryTestCase
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
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.EDITING
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }
    
    @Test
    public void testReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.READY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }

    @Test
    public void testQueued()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	exception.expect(InvalidStateTransitionException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
			TaskState.QUEUED
			);
        }

    @Test
    // TODO Should this FAIL ?
    public void testRunning()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.COMPLETED
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }

    @Test
    // TODO Should this FAIL ?
    public void testCompleted()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.COMPLETED
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }
    
    @Test
    public void testCancelled()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.CANCELLED,
			0
			);
    	assertEquals(
			TaskState.CANCELLED,
			query.state()
			);
        }
    
    @Test
    public void testFailed()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	exception.expect(InvalidStateTransitionException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
			TaskState.FAILED
			);
        }

    @Test
    public void testError()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			INVALID_QUERY
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	exception.expect(InvalidStateTransitionException.class);
    	exception.expectMessage("Invalid state transition");
    	query.advance(
			TaskState.ERROR
			);
        }
    }
