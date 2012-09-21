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
package uk.ac.roe.wfau.firethorn.config;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;

/**
 * Interface for system configuration.
 * 
 */
public interface UriBuilder
    {

    /**
     * Get the base service URI.
     * @return
     *      The base service URI.
     *
     */
    public URI uri();

    /**
     * Generate a URI for a JdbcResource.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(JdbcResource target);
    public URI uri(JdbcResource.JdbcCatalog target);
    public URI uri(JdbcResource.JdbcSchema  target);
    public URI uri(JdbcResource.JdbcTable   target);
    public URI uri(JdbcResource.JdbcColumn  target);
    
    /**
     * Generate a URI for an AdqlResource.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(AdqlResource target);

    /**
     * Generate a URI for an AdqlCatalog.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(AdqlResource.AdqlCatalog target);

    /**
     * Generate a URI for an AdqlSchema.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(AdqlResource.AdqlSchema target);

    /**
     * Generate a URI for an AdqlTable.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(AdqlResource.AdqlTable target);

    /**
     * Generate a URI for an AdqlCatalog.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(AdqlResource.AdqlColumn target);

    /**
     * Generate a URI for an IvoaResource.
     * @return
     *      The URI for the resource.
     *
     */
    public URI uri(IvoaResource target);

    }
