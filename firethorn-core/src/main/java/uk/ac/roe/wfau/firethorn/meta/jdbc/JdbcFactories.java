/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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

/**
 * Our JDBC component factories
 *
 */
public interface JdbcFactories
    {
    /**
     * Our {@link JdbcResource.EntityServices} instance.
     *
     */
    public JdbcResource.EntityServices resources();

    /**
     * Our {@link JdbcSchema.EntityServices} instance.
     *
     */
    public JdbcSchema.EntityServices schemas();

    /**
     * Our {@link JdbcTable.EntityServices} instance.
     *
     */
    public JdbcTable.EntityServices tables();

    /**
     * Our {@link JdbcColumn.EntityServices} instance.
     *
     */
    public JdbcColumn.EntityServices columns();

    }
