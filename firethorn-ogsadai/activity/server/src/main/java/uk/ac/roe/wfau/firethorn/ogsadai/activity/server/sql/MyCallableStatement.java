/*
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ----------------------------------------------------------------------
 * Original-licence
 *
 * Copyright (c) The University of Edinburgh,  2007-2008.
 *
 * LICENCE-START
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * LICENCE-END
 * 
 */
//package uk.org.ogsadai.activity.sql;
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.sql;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Callable;

/**
 * Simple class to allow concurrency utilities to handle background 
 * execution of SQL query execution via JDBC.
 *
 * @author The OGSA-DAI Project Team.
 */
public class MyCallableStatement implements Callable<ResultSet>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2008.";

    /** JBDC statement - executes query. */
    private Statement mStatement;
    /** SQL query to run. */
    private String mQuery;

    /**
     * Constructor.
     *
     * @param statement
     *     JDBC statement to execute query.
     * @param query
     *     Query to execute.
     */
    public MyCallableStatement(Statement statement, String query)
    {
        mStatement = statement;
        mQuery = query;
    }

    /**
     * {@inheritDoc}
     *
     * Runs query and results result set.
     *
     * @return <code>ResultSet</code>.
     * @throws Exception
     *     If any problems arise.
     */
    public ResultSet call() throws Exception
    {
        return mStatement.executeQuery(mQuery);
    }
}
