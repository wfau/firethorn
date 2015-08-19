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
public class EditingQueryTestCase
    extends BlueQueryTestBase
    {

	@Test
    public void testEditEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
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
    public void testReadyEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
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
    public void testQueueEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.QUEUED
			);
    	fail("Invalid state request");
        }

    @Test
    public void testCompleteEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.COMPLETED
			);
    	assertEquals(
			TaskState.FAILED,
			query.state()
			);
        }
    
    @Test
    public void testCancelEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
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
    public void testFailEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.FAILED
			);
    	fail("Invalid state request");
        }

    @Test
    public void testErrorEditing()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
    	query.advance(
			TaskState.ERROR
			);
    	fail("Invalid state request");
        }
    }
