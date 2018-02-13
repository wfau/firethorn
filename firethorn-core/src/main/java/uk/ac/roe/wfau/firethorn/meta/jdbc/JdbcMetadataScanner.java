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

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;

/**
 * An abstraction of the database metadata.
 *
 */
public interface JdbcMetadataScanner
extends JdbcExceptionHandler
    {
    
    /**
     * Access to our {@link JdbcConnection}. 
     * 
     */
    public JdbcConnection connector();

    /**
     * Public interface for the database catalogs.
     * 
     */
    public interface Catalogs
        {

        /**
         * Iterate through the available {@link Catalog}s.
         * @return An {@link Iterable<>} interface for the {@link Catalog}s.
         *
         */
        public Iterable<Catalog> select()
        throws SQLException, MetadataException;

        /**
         * Select a specific {@link Catalog} by name.
         * @return The target {@link Catalog}.
         *
         */
        public Catalog select(final String name)
        throws SQLException, MetadataException;

        }

    /**
     * Access to the {@link Catalogs} interface.
     * 
     */
    public Catalogs catalogs();

    /**
     * Public interface for a database catalog.
     * 
     */
    public interface Catalog
    extends JdbcExceptionHandler
        {
        @Deprecated
        public JdbcMetadataScanner scanner();

        /**
         * The schema name.
         * 
         */
        public String name();

        /**
         * Interface to access the {@link Catalog} {@link Schema}s. 
         * 
         */
        public interface Schemas
            {
            
            /**
             * Iterate through the {@link Catalog} {@link Schema}s.
             * @return An {@link Iterable<>} interface for the {@link Catalog} {@link Schema}s.
             *
             */
            public Iterable<Schema> select()
            throws SQLException;

            /**
             * Select a specific {@link Schema} by name.
             * @return The target {@link Schema}.
             *
             */
            public Schema select(final String name)
            throws SQLException;
            
            }

        /**
         * Access to the {@link Catalog} {@link Schemas}.
         * 
         */
        public Schemas schemas();
        
        }

    /**
     * Public interface for a database schema.
     * 
     */
    public interface Schema
    extends JdbcExceptionHandler
        {
        
        /**
         * The parent {@link Catalog}.
         * 
         */
        public Catalog catalog();

        /**
         * The schema name.
         * 
         */
        public String name();

        /**
         * Interface to access the {@link Schema} {@Table}s. 
         * 
         */
        public interface Tables
            {
            
            /**
             * Iterate through the {@link Schema} {@link Table}s.
             * @return An {@link Iterable<>} interface for the {@link Schema} {@link Table}s.
             *
             */
            public Iterable<Table> select()
            throws SQLException;

            /**
             * Select a specific {@link Table} by name.
             * @return The target {@link Table}.
             *
             */
            public Table select(final String name)
            throws SQLException;
            
            }

        /**
         * Access to the {@link Schema} {@link Tables}.
         * 
         */
        public Tables tables();

        }

    /**
     * Public interface for a database table.
     * 
     */
    public interface Table
    extends JdbcExceptionHandler
        {
        
        /**
         * The parent {@link Schema}.
         * 
         */
        public Schema schema();

        /**
         * The table name.
         * 
         */
        public String name();

        /**
         * Interface to access the {@link Table} {@link Column}s. 
         * 
         */
        public interface Columns
            {
            
            /**
             * Iterate through the {@link Table} {@link Column}s.
             * @return An {@link Iterable<>} interface for the {@link Table} {@link Column}s.
             *
             */
            public Iterable<Column> select()
            throws SQLException;

            /**
             * Select a specific {@link Column} by name.
             * @return The target {@link Column}.
             *
             */
            public Column select(final String name)
            throws SQLException;

            }
        
        /**
         * Access to the {@link Table} {@link Columns}.
         * 
         */
        public Columns columns();

        }

    /**
     * Public interface for a database column.
     * 
     */
    public interface Column
    extends JdbcExceptionHandler
        {

        /**
         * The parent {@link Table}.
         * 
         */
        public Table table();

        /**
         * The column name.
         * 
         */
        public String name();

        /**
         * The length of a CHAR column.
         * 
         */
        public Integer strlen();

        /**
         * The column {@link JdbcColumn.JdbcType}.
         * 
         */
        public JdbcColumn.JdbcType type();

        }
    }
