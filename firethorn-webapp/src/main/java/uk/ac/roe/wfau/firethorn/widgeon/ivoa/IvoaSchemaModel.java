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

public interface IvoaSchemaModel
    {
    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "ivoa.schema.entity" ;

    /**
     * MVC property for the schema name.
     *
     */
    public static final String SCHEMA_NAME_PARAM = "ivoa.schema.name" ;

    /**
     * MVC property for the schema {@link Identifier}.
     *
     */
    public static final String SCHEMA_IDENT_PARAM = "ivoa.schema.ident" ;
    
    }
