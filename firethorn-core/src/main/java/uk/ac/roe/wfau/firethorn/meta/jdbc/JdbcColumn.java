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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 * Public interface for a local JDBC column.
 *
 */
public interface JdbcColumn
extends BaseColumn<JdbcColumn>
    {

    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<JdbcColumn, JdbcColumn.Metadata>
        {
        /**
         * Create or update a column.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcColumn build(final JdbcColumn.Metadata param)
        throws ProtectionException, DuplicateEntityException;
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcColumn create(final JdbcTable parent, final JdbcColumn.Metadata meta)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcColumn}.
         * Used by JdbcTableEntity.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcColumn create(final JdbcTable parent, final String name, final JdbcColumn.JdbcType type, final Integer size)
        throws ProtectionException;

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<JdbcColumn>
        {
        /**
         * Our {@link JdbcColumn.EntityFactory} instance.
         *
         */
        public JdbcColumn.EntityFactory entities();

        /**
         * Our {@link JdbcColumn.AliasFactory} instance.
         *
         */
        public JdbcColumn.AliasFactory aliases();
        }
    
    @Override
    public JdbcTable table()
    throws ProtectionException;

    @Override
    public JdbcSchema schema()
    throws ProtectionException;

    @Override
    public JdbcResource resource()
    throws ProtectionException;
    
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
        BLOB(           Types.BLOB,         false,  true),
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
        VARBINARY(      Types.VARBINARY,    false,  true),
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
                    return JdbcColumn.JdbcType.BIT;
                
                case BYTE :
                    return JdbcColumn.JdbcType.VARBINARY;
                
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
     * The {@link JdbcColumn} metadata interface.
     *
     */
    public interface Metadata
    extends AdqlColumn.Metadata
        {
        /**
         * The JDBC metadata interface.
         *
         */
        public interface Jdbc
            {
            /**
             * The column name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;
            
            /**
             * The column type.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public JdbcType jdbctype()
            throws ProtectionException;

            /**
             * The column array size (element count).
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public Integer arraysize()
            throws ProtectionException;

            }
        /**
         * The JDBC metadata.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Jdbc jdbc()
        throws ProtectionException;

        }
    
    /**
     * The {@link JdbcColumn} modifier interface.
     *
     */
    public interface Modifier
    extends AdqlColumn.Modifier
        {
        /**
         * The JDBC modifier interface.
         *
         */
        public interface Jdbc
        extends JdbcColumn.Metadata.Jdbc
            {
            }

        /**
         * The JDBC modifier.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Jdbc jdbc()
        throws ProtectionException;
        
        }
    
    @Override
    public JdbcColumn.Modifier meta()
    throws ProtectionException;

    /**
     * Update the {@link JdbcColumn} properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final JdbcColumn.Metadata.Jdbc meta)
    throws ProtectionException;
    
    }
