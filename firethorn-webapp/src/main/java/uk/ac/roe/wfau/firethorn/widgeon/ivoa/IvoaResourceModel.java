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

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;

/**
 * Public interface for the {@link IvoaResource} model.
 * 
 */
public interface IvoaResourceModel
    {

    /**
     * MVC property for the target resource, [{@value}}].
     *
     */
    public static final String TARGET_ENTITY = "ivoa.resource.entity" ;
    
    /**
     * MVC property for the {@link IvoaResource} name, [{@value}}].
     *
     */
    public static final String RESOURCE_NAME_PARAM = "ivoa.resource.name" ;

    /**
     * MVC property for the {@link IvoaResource} {@link Identifier}, [{@value}}].
     *
     */
    public static final String RESOURCE_IDENT_PARAM = "ivoa.resource.ident" ;

    /**
     * MVC property for the {@link IvoaResource} status, [{@value}}].
     *
     */
    public static final String RESOURCE_STATUS_PARAM = "ivoa.resource.status" ;

    /**
     * MVC property for the {@link IvoaResource} endpoint, [{@value}}].
     *
     */
    public static final String RESOURCE_ENDPOINT_PARAM = "ivoa.resource.endpoint" ;


    }
