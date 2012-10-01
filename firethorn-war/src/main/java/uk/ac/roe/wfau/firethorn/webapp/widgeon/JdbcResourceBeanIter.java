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

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;

/**
 * Bean wrapper to enable the JSON converter to process list of a DataServices.  
 *
 */
public class JdbcResourceBeanIter
extends AbstractEntityBeanIter<JdbcResource>
    {

    /**
     * The URI builder to generate entity URIs.
     * 
     */
    protected UriBuilder builder ;
    
    /**
     * Public constructor.
     *
     */
    public JdbcResourceBeanIter(UriBuilder builder, Iterable<JdbcResource> iterable)
        {
        super(
            iterable
            );
        this.builder = builder ;
        }

    @Override
    public EntityBean<JdbcResource> bean(JdbcResource entity)
        {
        return new JdbcResourceBean(
            builder,
            entity
            );
        }
    }