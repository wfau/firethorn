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
     * A UriBuilder for JDBC resources.
     *
     */
    public static interface JdbcUriBuilder
        {
        /**
         * Generate a URI for a Jdbc resource.
         * @param resource
         *      The target resource.
         * @return
         *      The URI for the resource.
         *
         */
        public URI uri(JdbcResource resource);

        /**
         * Generate a URI for a Jdbc catalog.
         * @param catalog
         *      The target catalog.
         * @return
         *      The URI for the catalog.
         *
         */
        public URI uri(JdbcResource.JdbcCatalog catalog);

        /**
         * Generate a URI for a Jdbc schema.
         * @param schema
         *      The target schema.
         * @return
         *      The URI for the schema.
         *
         */
        public URI uri(JdbcResource.JdbcSchema  schema);

        /**
         * Generate a URI for a Jdbc table.
         * @param table
         *      The target table.
         * @return
         *      The URI for the table.
         *
         */
        public URI uri(JdbcResource.JdbcTable   table);

        /**
         * Generate a URI for a Jdbc column.
         * @param column
         *      The target column.
         * @return
         *      The URI for the column.
         *
         */
        public URI uri(JdbcResource.JdbcColumn  column);

        }

    /**
     * A UriBuilder for JDBC resources.
     *
     */
    public JdbcUriBuilder jdbc();


    /**
     * A UriBuilder for ADQL resources.
     *
     */
    public static interface AdqlUriBuilder
        {
        /**
         * Generate a URI for an Adql resource.
         * @param resource
         *      The target resource.
         * @return
         *      The URI for the resource.
         *
         */
        public URI uri(AdqlResource resource);

        /**
         * Generate a URI for an Adql catalog.
         * @param catalog
         *      The target catalog.
         * @return
         *      The URI for the catalog.
         *
         */
        public URI uri(AdqlResource.AdqlCatalog catalog);

        /**
         * Generate a URI for an Adql schema.
         * @param schema
         *      The target schema.
         * @return
         *      The URI for the schema.
         *
         */
        public URI uri(AdqlResource.AdqlSchema  schema);

        /**
         * Generate a URI for an Adql table.
         * @param table
         *      The target table.
         * @return
         *      The URI for the table.
         *
         */
        public URI uri(AdqlResource.AdqlTable   table);

        /**
         * Generate a URI for an Adql column.
         * @param column
         *      The target column.
         * @return
         *      The URI for the column.
         *
         */
        public URI uri(JdbcResource.JdbcColumn  column);

        }

    /**
     * A UriBuilder for ADQL resources.
     *
     */
    public AdqlUriBuilder adql();

    /**
     * A UriBuilder for IVOA resources.
     *
     */
    public static interface IvoaUriBuilder
        {
        /**
         * Generate a URI for an Ivoa resource.
         * @param resource
         *      The target resource.
         * @return
         *      The URI for the resource.
         *
         */
        public URI uri(IvoaResource resource);
    
        }

    /**
     * A UriBuilder for IVOA resources.
     *
     */
    public IvoaUriBuilder ivoa();
    
    }
