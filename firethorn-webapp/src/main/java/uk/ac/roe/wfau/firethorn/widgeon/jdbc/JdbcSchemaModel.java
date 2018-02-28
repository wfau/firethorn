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

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Public interface for the {@link JdbcSchema} model.
 * 
 */
public interface JdbcSchemaModel
    {
    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "jdbc.schema.entity" ;

    /**
     * MVC property for the {@link JdbcSchema} ident.
     *
     */
    public static final String SCHEMA_IDENT_PARAM = "jdbc.schema.ident" ;
    
    /**
     * MVC property for the {@link JdbcSchema} name.
     *
     */
    public static final String SCHEMA_NAME_PARAM = "jdbc.schema.schema" ;

    /**
     * MVC property for the catalog name.
     *
     */
    public static final String CATALOG_NAME_PARAM = "jdbc.schema.catalog" ;

    
    }
