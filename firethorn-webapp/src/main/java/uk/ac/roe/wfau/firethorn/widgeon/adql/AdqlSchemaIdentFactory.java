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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import java.net.URI;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;

/**
 * An {@link AdqlSchema.IdentFactory} implementation.
 * @todo Use PURLs.
 *
 */
@Component
public class AdqlSchemaIdentFactory
extends WebappIdentFactory
implements AdqlSchema.IdentFactory
    {
    /**
     * The type URI for this type.
     * @todo Move this to entity type interface.
     * @todo Use PURLs.
     *
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/entity/adql-schema-1.0.json"
        );
    }
