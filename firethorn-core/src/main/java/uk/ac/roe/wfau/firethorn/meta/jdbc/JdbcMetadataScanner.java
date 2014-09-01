/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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

import java.sql.SQLException;


/**
 * An abstraction of JDBC database metadata.
 *
 */
public interface JdbcMetadataScanner
    {

    public Catalogs catalogs();
    public interface Catalogs
        {
        public Iterable<Catalog> select()
        throws SQLException;
        public Catalog select(final String name)
        throws SQLException;
        }

    public interface Catalog
        {
        public String name();
        public Schemas schema();
        public interface Schemas
            {
            public Iterable<Schema> select()
            throws SQLException;
            public Schema select(final String name)
            throws SQLException;
            }
        }

    public interface Schema
        {
        public Catalog parent();
        public String name();
        public Tables tables();
        public interface Tables
            {
            public Iterable<Table> select()
            throws SQLException;
            public Table select(final String name)
            throws SQLException;
            }
        }

    public interface Table
        {
        public Schema parent();
        public String name();
        public Columns columns();
        public interface Columns
            {
            public Iterable<Column> select()
            throws SQLException;
            public Column select(final String name)
            throws SQLException;
            }
        }

    public interface Column
        {
        public Table parent();
        public String name();

        public Integer strlen();
        public JdbcColumn.JdbcType type();

        }
    }
