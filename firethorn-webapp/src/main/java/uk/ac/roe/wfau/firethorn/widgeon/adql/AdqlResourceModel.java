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

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 * Public interface for the {@link AdqlResource} model.
 * 
 */
public interface AdqlResourceModel
    {
    
    /**
     * MVC property for the {@link AdqlResource} name, [{@value}].
     *
     */
    public static final String RESOURCE_NAME_PARAM = "adql.resource.name" ;

    /**
     * MVC property for the {@link AdqlResource} {@link Identifier}, [{@value}].
     *
     */
    public static final String RESOURCE_IDENT_PARAM = "adql.resource.ident" ;
    
    /**
     * MVC property of the {@link BaseSchema} {@Identifier} to import, [{@value}].
     * 
     */
    public static final String SCHEMA_IMPORT_BASE_PARAM = "adql.schema.base" ;
    
    }
