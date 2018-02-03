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

package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnection;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * Public interface for the {@link JdbcResource} model.
 * 
 */
public interface JdbcResourceModel
    {
    /**
     * MVC property for the {@link JdbcResource} name.
     *
     */
    public static final String RESOURCE_NAME_PARAM = "jdbc.resource.name" ;

    /**
     * MVC property for the {@link JdbcResource} status.
     *
     */
    public static final String RESOURCE_STATUS_PARAM = "jdbc.resource.status" ;

    /**
     * MVC property for the {@link JdbcConnection} type, {@value}.
     *
     */
    public static final String CONNECTION_TYPE_PARAM = "jdbc.resource.connection.type" ;

    /**
     * MVC property for the {@link JdbcConnection} host name, {@value}.
     *
     */
    public static final String CONNECTION_HOST_PARAM = "jdbc.resource.connection.host" ;

    /**
     * MVC property for the {@link JdbcConnection} port, {@value}.
     *
     */
    public static final String CONNECTION_PORT_PARAM = "jdbc.resource.connection.port" ;

    /**
     * MVC property for the {@link JdbcConnection} database, {@value}.
     *
     */
    public static final String CONNECTION_DATABASE_PARAM = "jdbc.resource.connection.database" ;

    /**
     * MVC property for the {@link JdbcConnection} catalog, {@value}.
     *
     */
    public static final String CONNECTION_CATALOG_PARAM = "jdbc.resource.connection.catalog" ;
    
    /**
     * MVC property for the {@link JdbcConnection} user name, {@value}.
     *
     */
    public static final String CONNECTION_USER_PARAM = "jdbc.resource.connection.user" ;

    /**
     * MVC property for the {@link JdbcConnection} password, {@value}.
     *
     */
    public static final String CONNECTION_PASS_PARAM = "jdbc.resource.connection.pass" ;

    }
