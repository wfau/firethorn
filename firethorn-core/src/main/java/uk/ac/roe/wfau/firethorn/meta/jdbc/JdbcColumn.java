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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 *
 *
 */
public interface JdbcColumn
extends BaseColumn<JdbcColumn>
    {

    /**
     * Link factory interface.
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
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends BaseColumn.AliasFactory<JdbcColumn>
        {
        }

    /**
     * Column factory interface.
     *
     */
    public static interface EntityFactory
    extends BaseColumn.EntityFactory<JdbcTable, JdbcColumn>
        {
        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final JdbcTable parent, final AdqlQuery.SelectField field);

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
     * An enumeration of the JDBC data types.
     * The sizes are based on the size/precision for SQLServer.
     * @todo refactor the sizes as arraysize or precision
     * @todo read the sizes from the table metadata
     *
     */
    public enum Type
        {
        ARRAY(          Types.ARRAY,         VAR_ARRAY_SIZE),
        BIGINT(         Types.BIGINT,        19),
        BINARY(         Types.BINARY,        VAR_ARRAY_SIZE),
        BIT(            Types.BIT,            1),
        BLOB(           Types.BLOB,          VAR_ARRAY_SIZE),
        BOOLEAN(        Types.BOOLEAN,        1),
        CHAR(           Types.CHAR,          VAR_ARRAY_SIZE),
        CLOB(           Types.CLOB,          VAR_ARRAY_SIZE),
        DATALINK(       Types.DATALINK,      NON_ARRAY_SIZE),
        DATE(           Types.DATE,          10),
        DECIMAL(        Types.DECIMAL,       38),
        DISTINCT(       Types.DISTINCT,      NON_ARRAY_SIZE),
        DOUBLE(         Types.DOUBLE,        53),
        FLOAT(          Types.FLOAT,         53),
        INTEGER(        Types.INTEGER,       10),
        JAVA_OBJECT(    Types.JAVA_OBJECT,   VAR_ARRAY_SIZE),
        LONGNVARCHAR(   Types.LONGNVARCHAR,  VAR_ARRAY_SIZE),
        LONGVARBINARY(  Types.LONGVARBINARY, VAR_ARRAY_SIZE),
        LONGVARCHAR(    Types.LONGVARCHAR,   VAR_ARRAY_SIZE),
        NCHAR(          Types.NCHAR,         VAR_ARRAY_SIZE),
        NCLOB(          Types.NCLOB,         VAR_ARRAY_SIZE),
        NULL(           Types.NULL,          VAR_ARRAY_SIZE),
        NUMERIC(        Types.NUMERIC,       38),
        NVARCHAR(       Types.NVARCHAR,      VAR_ARRAY_SIZE),
        OTHER(          Types.OTHER,         NON_ARRAY_SIZE),
        REAL(           Types.REAL,          24),
        REF(            Types.REF,           NON_ARRAY_SIZE),
        ROWID(          Types.ROWID,         NON_ARRAY_SIZE),
        SMALLINT(       Types.SMALLINT,       5),
        SQLXML(         Types.SQLXML,        VAR_ARRAY_SIZE),
        STRUCT(         Types.STRUCT,        VAR_ARRAY_SIZE),
        TIME(           Types.TIME,          16),
        TIMESTAMP(      Types.TIMESTAMP,      8),
        TINYINT(        Types.TINYINT,        3),
        VARBINARY(      Types.VARBINARY,     VAR_ARRAY_SIZE),
        VARCHAR(        Types.VARCHAR,       VAR_ARRAY_SIZE),
        UNKNOWN(        Types.OTHER,         NON_ARRAY_SIZE);

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

        public AdqlColumn.Type adql()
            {
            return AdqlColumn.Type.type(
                this
                );
            }

        Type(final int type, final int size)
            {
            this.sqltype = type;
            this.sqlsize = size ;
            }

        /**
         * Mapping from java.sql.Types to JdbcColumn.Type.
         * @see java.sql.Types
         *
         */
        public static Type jdbc(final int sql)
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

    /**
     * Access to the column metadata.
     *
     */
    public interface Metadata
    extends AdqlColumn.Metadata
        {
        /**
         * The JDBC column metadata.
         *
         */
        public interface JdbcMeta
            {

            /**
             * The JDBC size.
             *
             */
            public Integer size();

            /**
             * Set the JDBC size.
             *
             */
            public void size(final Integer size);

            /**
             * The JDBC type.
             *
             */
            public Type type();

            /**
             * Set the JDBC type.
             *
             */
            public void type(final Type type);
            }
        /**
         * The JDBC column metadata.
         *
         */
        public JdbcMeta jdbc();

        }

    @Override
    public JdbcColumn.Metadata meta();

    /**
     * Update the column metadata.
     *
    public void scan();
     */

    }
