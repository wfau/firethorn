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

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlService</code>.
 *
 */
public class AdqlServiceBean
extends AbstractEntityBeanImpl<AdqlService>
implements EntityBean<AdqlService>
    {

    /**
     * The data type identifier.
     * TODO Move to IdentFactory.
     * 
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json"
        );

    /**
     *
     * Public constructor.
     * @param builder
     *      A UriBuilder for generating the service URI.
     * @param entity
     *      The target AdqlService.
     *
     */
    public AdqlServiceBean(final AdqlService entity)
        {
        super(
            TYPE_URI,
            entity
            );
        }
    }
