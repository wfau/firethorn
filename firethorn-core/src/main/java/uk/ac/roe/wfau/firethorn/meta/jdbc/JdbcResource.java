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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface JdbcResource
extends BaseResource<JdbcSchema>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcResource>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface Factory
    extends BaseResource.Factory<JdbcResource>
        {

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String ogsaid, final String name, final String url);

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url);

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass);

        /**
         * Our schema factory.
         *
         */
        public JdbcSchema.Factory schemas();

        /**
         * Select the default 'userdata' Resource.
         *
         */
        public JdbcResource userdata();
        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends BaseResource.Schemas<JdbcSchema>
        {
        /**
         * Create a new schema.
         *
         */
        public JdbcSchema create(final String catalog, final String schema);

        /**
         * Select a schema.
         *
         */
        public JdbcSchema select(final String catalog, final String schema);

        /**
         * Create a new schema owned by an Identity.
         *
         */
        public JdbcSchema create(final Identity identity);
        
        /**
         * Select the schemas owned by an Identity.
         *
         */
        public Iterable<JdbcSchema> select(final Identity identity);
        
        /**
         * Update the list of schemas from the JDBC metadata.
         *
         */
        public void scan();

        }

    @Override
    public Schemas schemas();

    /**
     * Access to our JDBC connection.
     *
     */
    public JdbcConnection connection();

    /**
     * The 'wildcard' catalog name.
     *
     */
    public static final String ALL_CATALOGS = "*" ;

    /**
     * The resource catalog name.
     *
     */
    public String catalog();

    /**
     * The resource catalog name.
     *
     */
    public void catalog(final String catalog);

    /**
     * Update the resource.
     *
     */
    public void scan();

    }
