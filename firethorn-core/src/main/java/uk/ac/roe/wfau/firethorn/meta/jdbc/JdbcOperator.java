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

package uk.ac.roe.wfau.firethorn.meta.jdbc;

import java.sql.Driver;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Public interface for operations on a JDBC database.
 *
 */
public interface JdbcOperator
    {
    /**
     * Generate the JDBC connection url for this database.
     * 
     */
    public String url();
    
    /**
     * Create (CREATE) a JDBC schema.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void create(final JdbcSchema schema)
    throws ProtectionException;

    /**
     * Delete (DROP) a JDBC schema.
     * @todo Should this be part of JdbcResource.JdbcDriver ?
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void drop(final JdbcSchema schema)
    throws ProtectionException;

    /**
     * Create (CREATE) a JDBC table.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void create(final JdbcTable table)
    throws ProtectionException;

    /**
     * Delete (DELETE) the contents of JDBC data.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void delete(final JdbcTable table)
    throws ProtectionException;

    /**
     * Delete (DROP) a JDBC table.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void drop(final JdbcTable table)
    throws ProtectionException;
    
    /**
     * Create (CREATE) a JDBC column.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void create(final JdbcColumn column)
    throws ProtectionException;

    /**
     * Delete (DROP) this {@link JdbcColumn}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void drop(final JdbcColumn column)
    throws ProtectionException;

    /**
     * Access to the JDBC driver for this database type.
     * 
     */
    public Driver driver();
    
    }