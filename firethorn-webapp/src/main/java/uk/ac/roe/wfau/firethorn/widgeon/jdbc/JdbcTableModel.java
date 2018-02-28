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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * Public interface for the {@link JdbcTable} model.
 * 
 */
public interface JdbcTableModel
    {
    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "jdbc.table.entity" ;

    /**
     * POST param for the {@link JdbcTable} name.
     *
     */
    public static final String TABLE_NAME_PARAM = "jdbc.table.name" ;

    /**
     * POST param for the {@link JdbcTable} {@link Identifier}.
     *
     */
    public static final String TABLE_IDENT_PARAM = "jdbc.table.ident" ;

    /**
     * POST param for the {@link JdbcTable} status.
     *
     */
    public static final String JDBC_STATUS_PARAM = "urn:jdbc.table.jdbc.status" ;

    /**
     * POST param for the {@link AdqlTable} status.
     *
     */
    public static final String ADQL_STATUS_PARAM = "urn:jdbc.table.adql.status" ;

    }
