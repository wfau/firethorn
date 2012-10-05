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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalog;

/**
 * Bean wrapper for an Entity.  
 *
 */
@Slf4j
public class JdbcCatalogBean
extends AbstractEntityBean<JdbcCatalog>
implements EntityBean<JdbcCatalog>
    {
    /**
     * Public constructor.
     *
     */
    public JdbcCatalogBean(JdbcCatalog entity)
        {
        super(
            JdbcCatalogIdentFactory.TYPE_URI,
            entity
            );
        }
    }
