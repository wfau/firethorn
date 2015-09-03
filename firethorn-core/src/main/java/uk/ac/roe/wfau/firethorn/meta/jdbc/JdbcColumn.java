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

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable.JdbcDriver;

/**
 * Public interface for a local JDBC column.
 *
 */
public interface JdbcColumn
extends BaseColumn<JdbcColumn>
    {
    /**
     * Physical JDBC driver interface.
     *
     */
    public static interface JdbcDriver
        {
        /**
         * Create a JDBC column.
         *
         */
        public void create(final JdbcColumn column);

        /**
         * Delete (DROP) a JDBC column.
         *
         */
        public void drop(final JdbcColumn column);
        
        }

    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<JdbcColumn, JdbcColumn.Metadata>
        {
        /**
         * Create or update a column.
         *
         */
        public JdbcColumn build(final JdbcColumn.Metadata param)
        throws DuplicateEntityException;
        }

    /**
     * {@link BaseColumn.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcColumn>
        {
        }
    
    /**
     * {@link BaseColumn.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<JdbcColumn>
        {
        }
    
    /**
     * {@link BaseColumn.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseColumn.AliasFactory<JdbcColumn>
        {
        }

    /**
     * {@link BaseColumn.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcColumn>
        {
        }

    /**
     * {@link BaseColumn.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseColumn.EntityFactory<JdbcTable, JdbcColumn>
        {
        /**
         * Create a new {@link JdbcColumn}.
         * Used by JdbcColumn.Builder
         *
         */
        public JdbcColumn create(final JdbcTable parent, final JdbcColumn.Metadata meta);

        /**
         * Create a new {@link JdbcColumn}.
         * Used by JdbcTableEntity.
         *
         */
        public JdbcColumn create(final JdbcTable parent, final String name, final JdbcColumn.JdbcType type, final Integer size);

        //TODO - move to services
        @Override
        public JdbcColumn.IdentFactory idents();

        //TODO - move to services
        //@Override
        //public JdbcColumn.NameFactory names();

        //TODO - move to services
        @Override
        public JdbcColumn.AliasFactory aliases();
        
        //TODO - move to services
        @Override
        public JdbcColumn.LinkFactory links();

        }

    @Override
    public JdbcTable table();
    @Override
    public JdbcSchema schema();
    @Override
    public JdbcResource resource();

    /**
     * An enumeration of the JDBC data types.
     * The sizes are based on the size/precision for SQLServer.
     * @todo refactor the sizes as arraysize or precision
     * @todo read the sizes from the table metadata
     *
    @Deprecated
    public enum OldJdbcType
        {
        ARRAY(          Types.ARRAY,         0,     VAR_ARRAY_SIZE),
        BIGINT(         Types.BIGINT,        0,     19),
        BINARY(         Types.BINARY,        0,     VAR_ARRAY_SIZE),
        BIT(            Types.BIT,           0,     1),
        BLOB(           Types.BLOB,          0,     VAR_ARRAY_SIZE),
        BOOLEAN(        Types.BOOLEAN,       0,     1),
        CHAR(           Types.CHAR,          0,     VAR_ARRAY_SIZE),
        CLOB(           Types.CLOB,          0,     VAR_ARRAY_SIZE),
        DATALINK(       Types.DATALINK,      0,     NON_ARRAY_SIZE),
        DATE(           Types.DATE,          0,     10),
        DECIMAL(        Types.DECIMAL,       0,     38),
        DISTINCT(       Types.DISTINCT,      0,     NON_ARRAY_SIZE),
        DOUBLE(         Types.DOUBLE,        0,     53),
        FLOAT(          Types.FLOAT,         0,     53),
        INTEGER(        Types.INTEGER,       0,     10),
        JAVA_OBJECT(    Types.JAVA_OBJECT,   0,     VAR_ARRAY_SIZE),
        LONGNVARCHAR(   Types.LONGNVARCHAR,  0,     VAR_ARRAY_SIZE),
        LONGVARBINARY(  Types.LONGVARBINARY, 0,     VAR_ARRAY_SIZE),
        LONGVARCHAR(    Types.LONGVARCHAR,   0,     VAR_ARRAY_SIZE),
        NCHAR(          Types.NCHAR,         0,     VAR_ARRAY_SIZE),
        NCLOB(          Types.NCLOB,         0,     VAR_ARRAY_SIZE),
        NULL(           Types.NULL,          0,     VAR_ARRAY_SIZE),
        NUMERIC(        Types.NUMERIC,       0,     38),
        NVARCHAR(       Types.NVARCHAR,      0,     VAR_ARRAY_SIZE),
        OTHER(          Types.OTHER,         0,     NON_ARRAY_SIZE),
        REAL(           Types.REAL,          0,     24),
        REF(            Types.REF,           0,     NON_ARRAY_SIZE),
        ROWID(          Types.ROWID,         0,     NON_ARRAY_SIZE),
        SMALLINT(       Types.SMALLINT,      0,     5),
        SQLXML(         Types.SQLXML,        0,     VAR_ARRAY_SIZE),
        STRUCT(         Types.STRUCT,        0,     VAR_ARRAY_SIZE),
        TIME(           Types.TIME,          0,     16),
        TIMESTAMP(      Types.TIMESTAMP,     0,     8),
        TINYINT(        Types.TINYINT,       0,     3),
        VARBINARY(      Types.VARBINARY,     0,     VAR_ARRAY_SIZE),
        VARCHAR(        Types.VARCHAR,       0,     VAR_ARRAY_SIZE),
        UNKNOWN(        Types.OTHER,         0,     NON_ARRAY_SIZE);

        private final int sqltype ;
        public int sqltype()
            {
            return this.sqltype;
            }

        private final int sqlsize ;
        public int sqlsize()
            {
            return this.sqlsize ;
            }

        private final int strsize ;
        public int strsize()
            {
            return this.strsize ;
            }

        public AdqlColumn.OldAdqlType adql()
            {
            return AdqlColumn.OldAdqlType.type(
                this
                );
            }

        OldJdbcType(final int sqltype, final int sqlsize, final int strsize)
            {
            this.sqltype = sqltype ;
            this.sqlsize = sqlsize ;
            this.strsize = strsize ;
            }

        **
         * Mapping from java.sql.Types to JdbcColumn.Type.
         * @see java.sql.Types
         *
         *
        public static OldJdbcType jdbc(final int sql)
            {
            switch(sql)
                {
                case Types.BIGINT :
                    return BIGINT ;

                case Types.BIT :
                    return BIT ;

                case Types.BOOLEAN :
                    return BOOLEAN ;

                case Types.LONGNVARCHAR :
                    return LONGNVARCHAR ;

                case Types.LONGVARCHAR :
                    return LONGVARCHAR ;

                case Types.NVARCHAR :
                    return NVARCHAR ;

                case Types.VARCHAR :
                    return VARCHAR ;

                case Types.NCHAR :
                    return NCHAR ;

                case Types.CHAR :
                    return CHAR ;

                case Types.DOUBLE :
                    return DOUBLE ;

                case Types.REAL  :
                    return REAL  ;

                case Types.FLOAT :
                    return FLOAT ;

                case Types.INTEGER :
                    return INTEGER ;

                case Types.TINYINT :
                    return TINYINT ;

                case Types.SMALLINT :
                    return SMALLINT ;

                case Types.ARRAY :
                    return ARRAY ;

                case Types.BINARY :
                    return BINARY ;

                case Types.BLOB :
                    return BLOB ;

                case Types.CLOB :
                    return CLOB ;

                case Types.DATALINK :
                    return DATALINK ;

                case Types.DATE :
                    return DATE ;

                case Types.DECIMAL :
                    return DECIMAL ;

                case Types.DISTINCT :
                    return DISTINCT ;

                case Types.JAVA_OBJECT :
                    return JAVA_OBJECT ;

                case Types.LONGVARBINARY :
                    return LONGVARBINARY ;

                case Types.NCLOB :
                    return NCLOB ;

                case Types.NULL :
                    return NULL ;

                case Types.NUMERIC :
                    return NUMERIC ;

                case Types.OTHER :
                    return OTHER ;

                case Types.REF :
                    return REF ;

                case Types.ROWID :
                    return ROWID ;

                case Types.SQLXML :
                    return SQLXML ;

                case Types.STRUCT :
                    return STRUCT ;

                case Types.TIME :
                    return TIME ;

                case Types.TIMESTAMP :
                    return TIMESTAMP ;

                case Types.VARBINARY :
                    return VARBINARY ;

                default :
                    return UNKNOWN ;
                }
            }
        }
     */

    
    /**
     * An enumeration of the {@link java.sql.Types} constants. 
     *
     */
    public enum JdbcType
        {
        ARRAY(          Types.ARRAY,        true,   true),
        BIGINT(         Types.BIGINT,       true,   false),
        BINARY(         Types.BINARY,       false,  true),
        BIT(            Types.BIT,          true,   false),
        BLOB(           Types.BLOB,         false,  false),
        BOOLEAN(        Types.BOOLEAN,      true,   false),
        CHAR(           Types.CHAR,         true,   true),
        CLOB(           Types.CLOB,         false,  false),
        DATALINK(       Types.DATALINK,     false,  false),
        DATE(           Types.DATE,         true,   false),
        DATETIME(       Types.DATE,         true,   false),
        DECIMAL(        Types.DECIMAL,      false,  false),
        DISTINCT(       Types.DISTINCT,     false,  false),
        DOUBLE(         Types.DOUBLE,       true,   false),
        FLOAT(          Types.FLOAT,        true,   false),
        INTEGER(        Types.INTEGER,      true,   false),
        JAVA_OBJECT(    Types.JAVA_OBJECT,  false,  false),
        LONGNVARCHAR(   Types.LONGNVARCHAR, false,  false),
        LONGVARBINARY(  Types.LONGVARBINARY,false,  false),
        LONGVARCHAR(    Types.LONGVARCHAR,  false,  false),
        NCHAR(          Types.NCHAR,        false,  false),
        NCLOB(          Types.NCLOB,        false,  false),
        NULL(           Types.NULL,         false,  false),
        NUMERIC(        Types.NUMERIC,      false,  false),
        NVARCHAR(       Types.NVARCHAR,     false,  false),
        OTHER(          Types.OTHER,        false,  false),
        REAL(           Types.REAL,         true,   false),
        REF(            Types.REF,          false,  false),
        ROWID(          Types.ROWID,        false,  false),
        SMALLINT(       Types.SMALLINT,     true,   false),
        SQLXML(         Types.SQLXML,       false,  false),
        STRUCT(         Types.STRUCT,       false,  false),
        TIME(           Types.TIME,         true,   false),
        TIMESTAMP(      Types.TIMESTAMP,    true,   false),
        TINYINT(        Types.TINYINT,      true,   false),
        VARBINARY(      Types.VARBINARY,    false,  false),
        VARCHAR(        Types.VARCHAR,      true,   true),
        UNKNOWN(        Types.OTHER,        false,  false);

        private JdbcType(final Integer sqltype, final boolean isvalid, final boolean isarray)
            {
            this.sqltype  = sqltype;
            this.isvalid  = isvalid;
            this.isarray  = isarray;
            }

        private Integer sqltype ;
        public Integer sqltype()
            {
            return this.sqltype;
            }

        private boolean isarray ;
        public boolean isarray()
            {
            return this.isarray;
            }

        private boolean isvalid ;
        public boolean isvalid()
            {
            return this.isvalid;
            }

        /**
         * Resolve our corresponding {@link AdqlColumn.AdqlType} 
         *
         */
        public AdqlColumn.AdqlType adqltype()
            {
            return AdqlColumn.AdqlType.resolve(
                this
                );
            }

        /**
         * Resolve a {@link AdqlColumn.AdqlType} into a {@link JdbcColumn.JdbcType}.
         * 
         */
        public static JdbcColumn.JdbcType resolve(final AdqlColumn.AdqlType adql)
            {
            switch(adql)
                {
                case ARRAY :
                    return JdbcColumn.JdbcType.ARRAY;
                
                case BOOLEAN :
                    return JdbcColumn.JdbcType.BOOLEAN;
                
                case BIT :
                    return JdbcColumn.JdbcType.BLOB;
                
                case BYTE :
                    return JdbcColumn.JdbcType.TINYINT;
                
                case CHAR :
                    return JdbcColumn.JdbcType.CHAR;
                
                case UNICODE :
                    return JdbcColumn.JdbcType.CHAR;
                
                case SHORT :
                    return JdbcColumn.JdbcType.SMALLINT;
                
                case INTEGER :
                    return JdbcColumn.JdbcType.INTEGER;
                
                case LONG :
                    return JdbcColumn.JdbcType.BIGINT;
                
                case FLOAT :
                    return JdbcColumn.JdbcType.FLOAT;
                
                case DOUBLE :
                    return JdbcColumn.JdbcType.DOUBLE;
                
                case FLOATCOMPLEX :
                    return JdbcColumn.JdbcType.UNKNOWN;
                
                case DOUBLECOMPLEX :
                    return JdbcColumn.JdbcType.UNKNOWN;
                
                case DATE :
                    return JdbcColumn.JdbcType.DATE;
                
                case TIME :
                    return JdbcColumn.JdbcType.TIME;
                
                case DATETIME :
                    return JdbcColumn.JdbcType.DATE;
                
                case USER :
                    return JdbcColumn.JdbcType.UNKNOWN;

                case UNKNOWN :
                    return JdbcColumn.JdbcType.UNKNOWN;
                
                default :
                    return JdbcColumn.JdbcType.UNKNOWN;
                }
            }

        
        private static Map<Integer, JdbcType> sqlmap = new HashMap<Integer, JdbcType>();
        static {
            for(JdbcType type : JdbcType.values())
                {
                sqlmap.put(
                    type.sqltype(),
                    type
                    );
                }
            }

        /**
         * Resolve a {@link java.sql.Type} into a {@link JdbcColumn.JdbcType}.
         * 
         */
        public static JdbcType sqltype(final Integer sqltype)
            {
            JdbcType found = sqlmap.get(
                sqltype
                );
            if (found != null)
                {
                return found;
                }
            else {
                return JdbcColumn.JdbcType.UNKNOWN;
                }
            }
        }
    
    /**
     * The column metadata.
     *
     */
    public interface Metadata
    extends AdqlColumn.Metadata
        {
        /**
         * The JDBC metadata.
         *
         */
        public interface Jdbc
            {
            /**
             * The column name.
             *
             */
            public String name();
            
            /**
             * The column type.
             * 
             */
            public JdbcType jdbctype();

            /**
             * The column array size (element count).
             * 
             */
            public Integer arraysize();

            
            
            /**
             * The column size.
             *
            @Deprecated
            public Integer size();
             */

            /**
             * Set the column size.
             *
            @Deprecated
            public void size(final Integer size);
             */

            /**
             * Get the JDBC type.
             *
            @Deprecated
            public OldJdbcType type();
             */

            /**
             * Set the JDBC type.
             *
            @Deprecated
            public void type(final OldJdbcType type);
             */

            /**
             * The corresponding SQL 'CREATE COLUMN' fields.
             *
             */
            public interface CreateSql
                {
                public String name();
                public String type();
                }

            /**
             * The corresponding SQL 'CREATE COLUMN' fields.
             *
             */
            public CreateSql create();

            }
        /**
         * The JDBC metadata.
         *
         */
        public Jdbc jdbc();

        }

    @Override
    public JdbcColumn.Metadata meta();

    /**
     * Update the column properties.
     * 
     */
    public void update(final JdbcColumn.Metadata meta);

    }
