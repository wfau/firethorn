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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface JdbcResource
extends BaseResource<JdbcSchema>
    {
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
         *
         */
        public JdbcResource create(final String ogsaid, final String name, final String url);

        /**
         * Create a new {@link JdbcResource}.
         *
         */
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url);

        /**
         * Create a new {@link JdbcResource}.
         *
         */
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass);

        /**
         * Our local {@link JdbcSchema.EntityFactory} implementation.
         * @todo - move to services
         *
         */
        public JdbcSchema.EntityFactory schemas();

        /**
         * Select the default 'userdata' Resource.
         * @todo Move this to a data space interface.
         *
         */
        public JdbcResource userdata();

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
         * Select the {@link JdbcSchema} owned by an Identity.
         * @todo Move this to a data space interface.
         *
        public Iterable<JdbcSchema> select(final Identity identity);
         */

        /**
         * Scan the JDBC metadata.
         * @todo Move this out of the interface.
         *
         */
        public void scan();

        /**
         * The default catalog/schema for this resource.
         *
         */
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
     * Get the JDBC type for an ADQL type.
     * Do we need this here ?
     *
    public JdbcColumn.OldJdbcType jdbctype(final AdqlColumn.OldAdqlType type);
     */

    /**
     * Get the JDBC size/precision for an ADQL type.
     * Do we need this here ?
     *
    public Integer jdbcsize(final AdqlColumn.OldAdqlType type);
     */

    /**
     * Get the JDBC size/precision for a JDBC type.
     * Do we need this here ?
     *
    public Integer jdbcsize(final JdbcColumn.OldJdbcType type);
     */

    /**
     * The {@link JdbcResource} metadata.
     *
     */
    public interface Metadata
    extends BaseResource.Metadata
        {
        /**
         * The JDBC metadata.
         * 
         */
        public interface Jdbc
            {
            }
        /**
         * The JDBC metadata.
         * 
         */
        public Jdbc jdbc();
        }

    @Override
    public JdbcResource.Metadata meta();
    
    }
