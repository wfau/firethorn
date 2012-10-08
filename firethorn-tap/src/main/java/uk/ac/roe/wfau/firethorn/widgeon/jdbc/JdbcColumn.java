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
 * Public interface for a JDBC column.
 *
 */
public interface JdbcColumn
extends BaseResource.BaseColumn<JdbcTable>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcColumn>
        {
        }

    /**
     * Factory interface for accessing columns.
     *
     */
    public static interface Factory
    extends BaseResource.BaseColumn.Factory<JdbcTable, JdbcColumn>
        {

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final JdbcTable parent, final String name);

        }

    @Override
    public JdbcResource resource();

    @Override
    public JdbcCatalog catalog();

    @Override
    public JdbcSchema schema();

    @Override
    public JdbcTable table();

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