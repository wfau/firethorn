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
package uk.ac.roe.wfau.firethorn.webapp.paths;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 *
 */
@Deprecated
public interface UriBuilder
    {

    /**
     * Generate a path for an entity.
     *
     */
    public String str(final Entity entity);

    /**
     * Generate a path for an entity.
     *
     */
    public String str(final Identifier ident);

    /**
     * Generate a URI for an entity.
     *
     */
    public URI uri(final Entity entity);

    /**
     * Generate a URI for an entity.
     *
     */
    public URI uri(final Identifier ident);

    }
