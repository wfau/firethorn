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
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;

/**
 *
 *
 */
@Slf4j
public class BlueQueryTestCase
    extends BlueQueryTestBase
    {
	protected static final String SIMPLE_QUERY = "SELECT TOP 10 ra, dec FROM atlasSource" ;
	
	/**
     * Simple test query ...
     *
     */
    //@Test
    public void simple()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT" +
            "    iPetroMag," +
            "    rmiExt" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    mergedClass=1" +
            " AND" +
            "    iPetroMag>-9.99995e+8" +
            " AND" +
            "    rmiExt>-9.99995e+8" +
            " AND" +
            "    (rppErrBits | ippErrBits) < 65536"
            );
        }

    @Test
    public void test001()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace()
			);
    	debug(
			query
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }

    @Test
    public void test002()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY
			);
    	debug(
			query
			);
    	assertEquals(
			TaskState.READY,
			query.state()
			);
        }

    //@Test
    // Known to fail - query isn't parsed.
    public void test003()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			"not valid SQL"
			);
    	debug(
			query
			);
    	assertEquals(
			TaskState.EDITING,
			query.state()
			);
        }

    @Test
    public void test004()
    throws Exception
        {
    	final BlueQuery query = factories().blues().entities().create(
			testspace(),
			SIMPLE_QUERY
			);
    	query.advance(
			TaskState.CANCELLED,
			0
			);
    	
    	debug(
			query
			);
    	assertEquals(
			TaskState.CANCELLED,
			query.state()
			);
        }
    
    
    public void debug(final BlueQuery query)
    	{
    	log.debug("debug(BlueQuery)");
    	log.debug("  ident [{}]", query.ident());
    	log.debug("  state [{}]", query.state());

    	}
    }
