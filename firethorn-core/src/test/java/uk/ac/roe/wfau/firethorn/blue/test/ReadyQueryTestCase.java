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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;

/**
 *
 *
 */
public class ReadyQueryTestCase
    extends BlueQueryTestBase
    {

    @Test
    public void testEditReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.EDITING
			);
    	fail("Invalid state request");
        }
    
    @Test
    public void testReadyReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.READY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
        }

    @Test
    public void testQueueReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.QUEUED
			);
    	fail("Invalid state request");
        }

    @Test
    public void testCompleteReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.COMPLETED
			);
		assertTrue(
			query.state().ordinal() >= TaskState.QUEUED.ordinal() 	
			);
        }

    @Test
    public void testCancelReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
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
    public void testFailReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.FAILED
			);
    	fail("Invalid state request");
        }

    @Test
    public void testErrorReady()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			ATLAS_QUERY
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
    	query.advance(
			TaskState.ERROR
			);
    	fail("Invalid state request");
        }
    }
