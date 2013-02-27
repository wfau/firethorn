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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnInfo;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn.Info;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaColumn;

/**
 *
 *
 */
public interface JdbcColumn
extends BaseColumn<JdbcColumn>
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcColumn>
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
     * Column factory interface.
     *
     */
    public static interface Factory
    extends BaseColumn.Factory<JdbcTable, JdbcColumn>
        {
        /**
         * Create a new column.
         *
         */
        @Deprecated
        public JdbcColumn create(final JdbcTable parent, final String name);

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final JdbcTable parent, final String name, final int type, final int size);

        }

    @Override
    public JdbcTable table();
    @Override
    public JdbcSchema schema();
    @Override
    public JdbcResource resource();

    /**
     * Update the column metadata.
     * 
     */
    public void scan();

    /**
     * Access to the column metadata.
     * 
     */
    public interface Info
    extends BaseColumn.Info 
        {

        /**
         * The JDBC column metadata.
         * 
         */
        public JdbcColumnInfo jdbc();

        }
    
    @Override
    public Info info();

    }
