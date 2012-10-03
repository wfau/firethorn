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
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;

/**
 * Public interface for a table.
 *
 */
public interface JdbcTable
extends BaseResource.BaseTable<JdbcSchema>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcTable>
        {
        }

    /**
     * Factory interface for accessing tables.
     *
     */
    public static interface Factory
    extends BaseResource.BaseTable.Factory<JdbcSchema, JdbcTable>
        {

        /**
         * Create a new table.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final String name);

        /**
         * Access to our column factory.
         *
         */
        public JdbcColumn.Factory columns();

        }

    /**
     * Public interface for accessing a table's columns.
     *
     */
    public interface Columns
    extends BaseResource.BaseTable.Columns<JdbcColumn>
        {

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final String name);

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
    public JdbcTable.Columns columns();

    @Override
    public JdbcResource resource();

    @Override
    public JdbcCatalog catalog();

    @Override
    public JdbcSchema schema();

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