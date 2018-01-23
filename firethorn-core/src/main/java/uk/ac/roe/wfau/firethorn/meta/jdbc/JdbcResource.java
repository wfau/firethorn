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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaJdbcResource;

/**
 * Public interface for a local JDBC resource.
 *
 */
public interface JdbcResource
extends BaseResource<JdbcSchema>
    {
    /**
     * JDBC driver interface.
     *
     */
    public static interface JdbcDriver
    extends JdbcSchema.JdbcDriver
        {

        /*
         * Modify (this) resource ?
         *  
         */
        
        }

    /**
     * The {@link JdbcDriver} for this {@link JdbcResource}.
     *
     */
    public JdbcDriver jdbcdriver();

    /**
     * {@link BaseResource.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcResource>
        {
        }

    /**
     * {@link BaseResource.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<JdbcResource>
        {
        }

    /**
     * {@link BaseResource.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseResource.LinkFactory<JdbcResource>
        {
        }

    /**
     * {@link BaseResource.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseResource.EntityFactory<JdbcResource>
        {

        /**
         * Create a new {@link JdbcResource}.
         * @throws ProtectionException 
         *
         */
        public JdbcResource create(final String name, final String url)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcResource}.
         * @throws ProtectionException 
         *
         */
        public JdbcResource create(final String catalog, final String name, final String url)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcResource}.
         * @throws ProtectionException 
         *
         */
        public JdbcResource create(final String catalog, final String name, final String url, final String user, final String pass)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcResource}.
         * @throws ProtectionException 
         *
         */
        public JdbcResource create(final String catalog, final String name, final String url, final String user, final String pass, final String driver)
        throws ProtectionException;

        /**
         * Select the default 'userdata' Resource.
         * @todo Move this to a data space interface.
         * @throws ProtectionException 
         *
         */
        @Deprecated
        public JdbcResource userdata()
        throws ProtectionException;

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<JdbcResource>
        {
        /**
         * Our {@link JdbcResource.EntityFactory} instance.
         *
         */
        public JdbcResource.EntityFactory entities();

        /**
         * Our {@link JdbcSchema.EntityFactory} instance.
         *
         */
        public JdbcSchema.EntityFactory schemas();

        }

    /**
     * Our resource {@link JdbcSchema schema}.
     *
     */
    public interface Schemas extends BaseResource.Schemas<JdbcSchema>
        {
        /**
         * Create a new {@link JdbcSchema}.
         *
         */
        public JdbcSchema create(final JdbcSchema.Metadata meta);

        /**
         * Create a new {@link JdbcSchema}.
         *
         */
        @Deprecated
        public JdbcSchema create(final String catalog, final String schema);

        /**
         * Create a new {@link JdbcSchema} owned by an Identity.
         * @todo Move this to a data space interface.
         *
         */
        public JdbcSchema create(final Identity identity);

        /**
         * Select a {@link JdbcSchema} by catalog name and schema name.
         *
         */
        public JdbcSchema select(final String catalog, final String schema)
        throws NameNotFoundException;

        /**
         * Search for a {@link JdbcSchema} by catalog name and schema name.
         *
         */
        public JdbcSchema search(final String catalog, final String schema);

        /**
         * The default catalog/schema for this resource.
         * Only used by simple form of Identity space.
         *
         */
        @Deprecated
        public JdbcSchema simple()
        throws EntityNotFoundException;

        /**
         * Create a {@link JdbcSchema.Builder}.
         *
         */
        public JdbcSchema.Builder builder();  
        
        }

    @Override
    public Schemas schemas();

    /**
     * Access to our JDBC connection.
     *
     */
    public JdbcConnector connection();

    /**
     * The 'wildcard' catalog name.
     *
     */
    public static final String ALL_CATALOGS = "*" ;

    /**
     * The primary catalog name.
     *
     */
    public String catalog();

    /**
     * The primary catalog name.
     *
     */
    public void catalog(final String catalog);

    /**
     * The {@link JdbcResource} metadata.
     *
     */
    public interface Metadata
    extends BaseResource.Metadata
        {
        /**
         * JDBC specific metadata.
         * 
         */
        public interface Jdbc
            {}

        /**
         * Access to the JDBC specific metadata.
         * 
         */
        public Jdbc jdbc();
        }

    @Override
    public JdbcResource.Metadata meta();
    
    /**
     * Interface to access the {@link OgsaJdbcResource} OGSA-DAI resources for this {@link JdbcResource}.
     * 
     */
    public interface OgsaJdbcResources
    extends OgsaBaseResources
        {
        /**
         * Select the primary {@link OgsaJdbcResource} OGSA-DAI resource.
         * 
         */
        public OgsaJdbcResource primary();
        
        /**
         * Select all the {@link OgsaJdbcResource} OGSA-DAI resources for this {@link JdbcResource}.
         * 
         */
        public Iterable<OgsaJdbcResource> select();

        }

    /**
     * Access to the {@link OgsaJdbcResource} OGSA-DAI resources for this {@link JdbcResource}.
     * 
     */
    public OgsaJdbcResources ogsa();

    }
