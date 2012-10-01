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
package uk.ac.roe.wfau.firethorn.webapp.widgeon;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;

/**
 * Bean wrapper to enable the JSON converter to process a JdbcResource.  
 *
 */
@Slf4j
public class JdbcResourceBean
extends AbstractEntityBean<JdbcResource>
implements EntityBean<JdbcResource>
    {
    
    /**
     * The data type identifier.
     * 
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json"
        );
    
    /**
     * The target JdbcResource.
     * 
     */

    /**
     * 
     * Public constructor.
     * @param builder
     *      A UriBuilder for generating the service URI.
     * @param entity
     *      The target JdbcResource.
     *
     */
    public JdbcResourceBean(UriBuilder builder, JdbcResource entity)
        {
        super(
            TYPE_URI,
            builder,
            entity
            );
        }
    }
