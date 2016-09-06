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
package uk.ac.roe.wfau.firethorn.widgeon.ogsa;

import java.net.URI;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaService;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;

/**
 * {@link Entity.IdentFactory} for {@link OgsaService}s.
 *
 */
@Component
public class OgsaServiceIdentFactory
extends WebappIdentFactory<OgsaService>
implements OgsaService.IdentFactory
    {
    /**
     * The type URI for this type.
     * TODO - Move to OgsaService interface.
     *
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/entity/ogsa-service-1.0.json"
        );
    }
