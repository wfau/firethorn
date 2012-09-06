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

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseColumn;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseSchema;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.Catalogs;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.FactoryTemplate;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseCatalog.Schemas;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseSchema.Tables;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseTable.Columns;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource.BaseTable.Factory;


/**
 *
 *
 */
public interface JdbcResource
extends BaseResource
    {

    /**
     * Factory interface for accessing resources.
     *
     */
    public static interface Factory
    extends BaseResource.FactoryTemplate<JdbcResource>
        {
        /**
         * Create a new resource.
         * 
         */
        public JdbcResource create(String name);

        /**
         * Create a new resource.
         * 
         */
        public JdbcResource create(String name, DataSource source);

        /**
         * Access to our catalog factory.
         * 
         */
        public JdbcResource.JdbcCatalog.Factory catalogs();
        
        }

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs
    extends BaseResource.Catalogs<JdbcCatalog>
        {
        /**
         * Create a new catalog.
         *
         */
        public JdbcResource.JdbcCatalog create(String name);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(DatabaseMetaData metadata, boolean pull);

        }

    @Override
    public Catalogs catalogs();

    /**
     * Public interface for a catalog.
     *
     */
    public interface JdbcCatalog
    extends BaseResource.BaseCatalog<JdbcResource>
        {
        /**
         * Factory interface for accessing catalogs.
         *
         */
        public static interface Factory
        extends BaseResource.BaseCatalog.Factory<JdbcResource, JdbcResource.JdbcCatalog>
            {

            /**
             * Create a new catalog.
             *
             */
            public JdbcResource.JdbcCatalog create(JdbcResource parent, String name);

            /**
             * Access to our schema factory.
             * 
             */
            public JdbcResource.JdbcSchema.Factory schemas();

            }

        /**
         * Public interface for accessing a catalog's schemas.
         *
         */
        public interface Schemas
        extends BaseResource.BaseCatalog.Schemas<JdbcResource.JdbcSchema>
            {
            /**
             * Create a new schema.
             *
             */
            public JdbcResource.JdbcSchema create(String name);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(boolean pull);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param metadata The DatabaseMetaData to compare against.
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(DatabaseMetaData metadata, boolean pull);

            }

        @Override
        public Schemas schemas();

        @Override
        public JdbcResource resource();

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(DatabaseMetaData metadata, boolean pull);

        }
    
    /**
     * Public interface for a schema.
     *
     */
    public interface JdbcSchema
    extends BaseResource.BaseSchema<JdbcResource.JdbcCatalog>
        {

        /**
         * Factory interface for accessing schema.
         *
         */
        public static interface Factory
        extends BaseResource.BaseSchema.Factory<JdbcResource.JdbcCatalog, JdbcResource.JdbcSchema>
            {

            /**
             * Create a new schema.
             *
             */
            public JdbcResource.JdbcSchema create(JdbcResource.JdbcCatalog parent, String name);

            /**
             * Access to our table factory.
             * 
             */
            public JdbcResource.JdbcTable.Factory tables();

            }

        /**
         * Public interface for accessing a schema's tables.
         *
         */
        public interface Tables
        extends BaseResource.BaseSchema.Tables<JdbcResource.JdbcTable>
            {

            /**
             * Create a new table.
             *
             */
            public JdbcResource.JdbcTable create(String name);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(boolean pull);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param metadata The DatabaseMetaData to compare against.
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(DatabaseMetaData metadata, boolean pull);
            
            }

        @Override
        public Tables tables();

        @Override
        public JdbcResource resource();

        @Override
        public JdbcResource.JdbcCatalog catalog();

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(DatabaseMetaData metadata, boolean pull);
        
        }
    
    /**
     * Public interface for a table.
     *
     */
    public interface JdbcTable
    extends BaseResource.BaseTable<JdbcResource.JdbcSchema>
        {

        /**
         * Factory interface for accessing tables.
         *
         */
        public static interface Factory
        extends BaseResource.BaseTable.Factory<JdbcResource.JdbcSchema, JdbcResource.JdbcTable>
            {

            /**
             * Create a new table.
             *
             */
            public JdbcResource.JdbcTable create(JdbcResource.JdbcSchema parent, String name);

            /**
             * Access to our column factory.
             * 
             */
            public JdbcResource.JdbcColumn.Factory columns();

            }

        /**
         * Public interface for accessing a table's columns.
         *
         */
        public interface Columns
        extends BaseResource.BaseTable.Columns<JdbcResource.JdbcColumn>
            {

            /**
             * Create a new column.
             *
             */
            public JdbcResource.JdbcColumn create(String name);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(boolean pull);

            /**
             * Compare our data with DatabaseMetaData from our DataSource. 
             * @param metadata The DatabaseMetaData to compare against.
             * @param pull Update our data to match the DatabaseMetaData. 
             *
             */
            public void diff(DatabaseMetaData metadata, boolean pull);

            }

        @Override
        public Columns columns();

        @Override
        public JdbcResource resource();

        @Override
        public JdbcResource.JdbcCatalog catalog();

        @Override
        public JdbcResource.JdbcSchema schema();

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(DatabaseMetaData metadata, boolean pull);

        }
    
    /**
     * Public interface for a column.
     *
     */
    public interface JdbcColumn
    extends BaseResource.BaseColumn<JdbcResource.JdbcTable>
        {

        /**
         * Factory interface for accessing columns.
         *
         */
        public static interface Factory
        extends BaseResource.BaseColumn.Factory<JdbcResource.JdbcTable, JdbcResource.JdbcColumn>
            {

            /**
             * Create a new column.
             *
             */
            public JdbcResource.JdbcColumn create(JdbcResource.JdbcTable parent, String name);

            }

        @Override
        public JdbcResource resource();

        @Override
        public JdbcResource.JdbcCatalog catalog();

        @Override
        public JdbcResource.JdbcSchema schema();

        @Override
        public JdbcResource.JdbcTable table();

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(boolean pull);

        /**
         * Compare our data with DatabaseMetaData from our DataSource. 
         * @param metadata The DatabaseMetaData to compare against.
         * @param pull Update our data to match the DatabaseMetaData. 
         *
         */
        public void diff(DatabaseMetaData metadata, boolean pull);

        }

    /**
     * JDBC DatabaseMetaData column names.
     * @see DatabaseMetaData
     * @todo Move this to a local sub-interface.
     *  
     */
    public static final String JDBC_META_TABLE_CAT   = "TABLE_CAT" ;
    public static final String JDBC_META_TABLE_TYPE  = "TABLE_TYPE" ;
    public static final String JDBC_META_TABLE_NAME  = "TABLE_NAME" ;
    public static final String JDBC_META_TABLE_SCHEM = "TABLE_SCHEM" ;

    public static final String JDBC_META_TABLE_TYPE_VIEW  = "VIEW" ;
    public static final String JDBC_META_TABLE_TYPE_TABLE = "TABLE" ;

    public static final String JDBC_META_COLUMN_NAME      = "COLUMN_NAME" ;
    public static final String JDBC_META_COLUMN_TYPE_TYPE = "DATA_TYPE";
    public static final String JDBC_META_COLUMN_TYPE_NAME = "TYPE_NAME";
    public static final String JDBC_META_COLUMN_SIZE      = "COLUMN_SIZE";
    
    /**
     * Access to our underlying DataSource.
     * @todo Move this to a local sub-interface.
     *
     */
    public DataSource source(); 

    /**
     * Open a connection to ourDataSource.
     * @todo Move this to a local sub-interface.
     *
     */
    public Connection connect();

    /**
     * Get the DatabaseMetaData from our DataSource.
     * @todo Move this to a local sub-interface.
     *
     */
    public DatabaseMetaData metadata();
    
    /**
     * Compare our data with DatabaseMetaData from our DataSource. 
     * @param pull Update our data to match the DatabaseMetaData. 
     *
     */
    public void diff(boolean pull);

    /**
     * Compare our data with DatabaseMetaData from our DataSource. 
     * @param metadata The DatabaseMetaData to compare against.
     * @param pull Update our data to match the DatabaseMetaData. 
     *
     */
    public void diff(DatabaseMetaData metadata, boolean pull);

    }
