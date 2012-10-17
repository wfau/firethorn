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

import java.sql.DatabaseMetaData;
import java.util.List;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseCatalog;

/**
 * Public interface for a JDBC catalog.
 *
 */
public interface JdbcCatalog
extends BaseCatalog<JdbcResource>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcCatalog>
        {
        }

    /**
     * Factory interface for accessing catalogs.
     *
     */
    public static interface Factory
    extends BaseCatalog.Factory<JdbcResource, JdbcCatalog>
        {

        /**
         * Create a new catalog.
         *
         */
        public JdbcCatalog create(final JdbcResource parent, final String name);

        /**
         * Access to our schema factory.
         *
         */
        public JdbcSchema.Factory schemas();

        }

    /**
     * Public interface for accessing a catalog's schemas.
     *
     */
    public interface Schemas
    extends BaseCatalog.Schemas<JdbcSchema>
        {
        /**
         * Create a new schema.
         *
         */
        public JdbcSchema create(final String name);

        /**
         * Compare our data with DatabaseMetaData from our DataSource.
         * @param pull Update our metadata to match the DatabaseMetaData.
         * @param push Update our database to match our metadata.
         *
         */
        public List<JdbcDiference> diff(final boolean push, final boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource.
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our metadata to match the DatabaseMetaData.
         * @param push Update our database to match our metadata.
         *
         */
        public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull);

        }

    @Override
    public JdbcCatalog.Schemas schemas();

    @Override
    public JdbcResource resource();

    /**
     * Compare our data with DatabaseMetaData from our DataSource.
     * @param pull Update our metadata to match the DatabaseMetaData.
     * @param push Update our database to match our metadata.
     *
     */
    public List<JdbcDiference> diff(final boolean push, final boolean pull);

    /**
     * Compare our data with DatabaseMetaData from our DataSource.
     * @param metadata The DatabaseMetaData to compare against.
     * @param pull Update our metadata to match the DatabaseMetaData.
     * @param push Update our database to match our metadata.
     *
     */
    public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull);

    }