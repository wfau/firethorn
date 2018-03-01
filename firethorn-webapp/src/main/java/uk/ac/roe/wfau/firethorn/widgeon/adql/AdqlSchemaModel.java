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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

public interface AdqlSchemaModel
    {
    /**
     * MVC property for the {@link AdqlSchema}, [{@value}].
     *
     */
    public static final String TARGET_ENTITY = "adql.schema.entity" ;
    
    /**
     * MVC property for the {@link AdqlSchema} name, [{@value}].
     *
     */
    public static final String SCHEMA_NAME_PARAM = "adql.schema.name" ;

    /**
     * MVC property for the {@link AdqlSchema} {@Identifier}, [{@value}].
     *
     */
    public static final String SCHEMA_IDENT_PARAM = "adql.schema.ident" ;

    /**
     * MVC property for the {@Identifier} of the {@link BaseTable} to copy, [{@value}].
     * 
     */
    public static final String BASE_TABLE_PARAM = "adql.table.base" ;


    }
