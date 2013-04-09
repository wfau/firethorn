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
     * Column factory interface.
     *
     */
    public static interface Factory
    extends BaseColumn.Factory<AdqlTable, AdqlColumn>
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
     *
     *
     */
    public enum Type
        {
        BOOLEAN("boolean"),
        BIT("bit"),
        BYTE("unsignedByte"),
        CHAR("char"),
        UNICODE("unicodeChar"),
        SHORT("short"),
        INTEGER("int"),
        LONG("long"),
        FLOAT("float"),
        DOUBLE("double"),
        FLOATCOMPLEX("floatComplex"),
        DOUBLECOMPLEX("doubleComplex"),
        UNKNOWN("");

        /**
         * Private constructor.
         *
         */
        private Type(final String datatype)
            {
            this.datatype = datatype ;
            }

        /**
         * The VOTable dataype name.
         *
         */
        private String datatype;

        /**
         * The VOTable dataype name.
         *
         */
        public String datatype()
            {
            return this.datatype;
            }

        /**
         * Mapping from java.sql.Types to AdqlColumnType.
         * @see java.sql.Types
         *
         */
        public static Type jdbc(final int type)
            {
            switch(type)
                {
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

                case Types.SMALLINT :
                case Types.TINYINT :
                    return SHORT ;

                case Types.ARRAY :
                case Types.BINARY :
                case Types.BLOB :
                case Types.CLOB :
                case Types.DATALINK :
                case Types.DATE :
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
                case Types.TIME :
                case Types.TIMESTAMP :
                case Types.VARBINARY :
                default :
                    return UNKNOWN ;
                }
            }
        }

    /**
     * Access to the column metadata.
     *
     */
    public interface Info
        {
        /**
         * ADQL column metadata.
         * @todo Add UCD, utype etc ...
         *
         */
        public interface Meta
            {
            public Integer size();

            public void size(final Integer size);

            public Type type();

            public void type(final Type type);

            }

        /**
         * The ADQL column metadata.
         *
         */
        public Meta adql();

        }

    @Override
    public AdqlColumn.Info info();

    }
