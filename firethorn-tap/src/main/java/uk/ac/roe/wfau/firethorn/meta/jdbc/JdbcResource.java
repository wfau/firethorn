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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.Entity;
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
        @Override
        public JdbcResource create(final String name);

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String name, final String url);

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String name, final String url, final String user, final String pass);

        /**
         * Our schema factory.
         *
         */
        public JdbcSchema.Factory schemas();

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
         * Update the schemas.
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
    public void catalog(String catalog);

    /**
     * Update the resource.
     * 
     */
    public void scan();

    }
