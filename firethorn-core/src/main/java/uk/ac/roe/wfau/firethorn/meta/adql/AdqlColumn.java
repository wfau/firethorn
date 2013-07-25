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
package uk.ac.roe.wfau.firethorn.meta.adql;

import java.sql.Types;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;

/**
 *
 *
 */
public interface AdqlColumn
extends BaseColumn<AdqlColumn>
    {
    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<AdqlColumn>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlColumn>
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
    extends BaseColumn.AliasFactory<AdqlColumn>
        {
        }

    /**
     * Column factory interface.
     *
     */
    public static interface EntityFactory
    extends BaseColumn.EntityFactory<AdqlTable, AdqlColumn>
        {
        /**
         * Create a new column.
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base);

        /**
         * Create a new column.
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name);

        }

    @Override
    public AdqlTable table();
    @Override
    public AdqlSchema schema();
    @Override
    public AdqlResource resource();
    @Override
    public BaseColumn<?> base();

    /**
     * An enumeration of the VOTable data types, as defined in section 2.1 of the VOTable-1.2 specification.
     * @see <a href='http://www.ivoa.net/Documents/VOTable/20091130/'/>
     * Plus an extra ARRAY value to catch array data.
     * Plus DATE, TIME and TIMESTAMP.
     *
     */
    public enum Type
        {
        ARRAY(          0, "array",         JdbcColumn.Type.ARRAY),
        BOOLEAN(        1, "boolean",       JdbcColumn.Type.BOOLEAN),
        BIT(            1, "bit",           JdbcColumn.Type.BLOB),
        BYTE(           1, "unsignedByte",  JdbcColumn.Type.TINYINT),
        CHAR(           2, "char",          JdbcColumn.Type.CHAR),
        UNICODE(        2, "unicodeChar",   JdbcColumn.Type.CHAR),
        SHORT(          2, "short",         JdbcColumn.Type.SMALLINT),
        INTEGER(        4, "int",           JdbcColumn.Type.INTEGER),
        LONG(           8, "long",          JdbcColumn.Type.BIGINT),
        FLOAT(          4, "float",         JdbcColumn.Type.FLOAT),
        DOUBLE(         8, "double",        JdbcColumn.Type.DOUBLE),
        FLOATCOMPLEX(   8, "floatComplex",  JdbcColumn.Type.UNKNOWN),
        DOUBLECOMPLEX( 16, "doubleComplex", JdbcColumn.Type.UNKNOWN),

        DATE(          10, "char",          JdbcColumn.Type.DATE),      // YYYY-MM-DD
        TIME(          12, "char",          JdbcColumn.Type.TIME),      // HH:MM:SS.sss
        TIMESTAMP(     23, "char",          JdbcColumn.Type.TIMESTAMP), // YYYY-MM-DDTHH:MM:SS.sss

        UNKNOWN(        0, "unknown",       JdbcColumn.Type.UNKNOWN);

        private final int size ;
        public  int size()
            {
            return this.size;
            }

        private final String code ;
        public  String code()
            {
            return this.code;
            }

        private final JdbcColumn.Type jdbc ;
        public  JdbcColumn.Type jdbc()
            {
            return this.jdbc;
            }

        Type(final int size, final String code, final JdbcColumn.Type jdbc)
            {
            this.size = size ;
            this.code = code ;
            this.jdbc = jdbc ;
            }

        /**
         * Mapping from JdbcColumn.Type to AdqlColumn.Type.
         * @see JdbcColumn.Type
         *
         */
        public static AdqlColumn.Type type(final JdbcColumn.Type jdbc)
            {
            return type(
                jdbc.sqltype()
                );
            }

        /**
         * Mapping from java.sql.Types to AdqlColumnType.
         * @see java.sql.Types
         *
         */
        public static AdqlColumn.Type type(final int sql)
            {
            switch(sql)
                {
                case Types.ARRAY :
                    return ARRAY;

                case Types.BIGINT :
                    return LONG ;

                case Types.BIT :
                case Types.BOOLEAN :
                    return BOOLEAN ;

                case Types.LONGNVARCHAR :
                case Types.LONGVARCHAR :
                case Types.NVARCHAR :
                case Types.VARCHAR :
                case Types.NCHAR :
                case Types.CHAR :
                    return CHAR ;

                case Types.DOUBLE :
                    return DOUBLE ;

                case Types.REAL  :
                case Types.FLOAT :
                    return FLOAT ;

                case Types.INTEGER :
                    return INTEGER ;

                case Types.TINYINT :
                    return BYTE ;

                case Types.SMALLINT :
                    return SHORT ;

                case Types.DATE :
                    return DATE ;
                case Types.TIME :
                    return TIME ;
                case Types.TIMESTAMP :
                    return TIMESTAMP ;

                case Types.BINARY :
                case Types.BLOB :
                case Types.CLOB :
                case Types.DATALINK :
                case Types.DECIMAL :
                case Types.DISTINCT :
                case Types.JAVA_OBJECT :
                case Types.LONGVARBINARY :
                case Types.NCLOB :
                case Types.NULL :
                case Types.NUMERIC :
                case Types.OTHER :
                case Types.REF :
                case Types.ROWID :
                case Types.SQLXML :
                case Types.STRUCT :
                case Types.VARBINARY :
                default :
                    return UNKNOWN ;
                }
            }

        public static AdqlColumn.Type type(final String name)
            {
            if(name != null)
                {
                return AdqlColumn.Type.valueOf(
                    name.trim().toUpperCase()
                    );
                }
            else {
                return null ;
                }
            }
        }
    }
