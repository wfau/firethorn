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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;

public interface IvoaTableModel
    {

    /**
     * MVC property for the {@link IvoaTable} entity.
     *
     */
    public static final String TARGET_ENTITY = "ivoa.table.entity" ;

    /**
     * MVC property for the {@link IvoaTable} name.
     *
     */
    public static final String TABLE_NAME_PARAM = "ivoa.table.name" ;

    /**
     * MVC property for the {@link IvoaTable} {@link Identifier}.
     *
     */
    public static final String TABLE_IDENT_PARAM = "ivoa.table.ident" ;

    /**
     * POST param for the JDBC status.
     *
    public static final String JDBC_STATUS_PARAM = "ivoa.table.jdbc.status" ;
     */

    /**
     * POST param for the ADQL status.
     *
    public static final String ADQL_STATUS_PARAM = "ivoa.table.adql.status" ;
     */

    
    }
