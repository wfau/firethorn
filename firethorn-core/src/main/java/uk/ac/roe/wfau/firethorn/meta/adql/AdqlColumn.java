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
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;

/**
 *
 *
 */
public interface AdqlColumn
extends BaseColumn<AdqlColumn>
    {
    /**
     * {@link EntityBuilder} interface.
     * 
    public static interface Builder
    extends EntityBuilder<IvoaColumn, IvoaColumn.Metadata>
        {
        /**
         * Create or update a column.
         *
         * /
        public IvoaColumn build(final IvoaColumn.Metadata param)
        throws DuplicateEntityException;
        }
     */

    /**
     * {@link BaseColumn.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseColumn.IdentFactory
        {
        }

    /**
     * {@link BaseColumn.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseColumn.NameFactory<AdqlColumn>
        {
        }
   
    /**
     * {@link BaseColumn.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseColumn.AliasFactory<AdqlColumn>
        {
        }
    
    /**
     * {@link BaseColumn.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseColumn.LinkFactory<AdqlColumn>
        {
        }

    /**
     * {@link BaseColumn.EntityResolver} interface.
     *
     */
    public static interface EntityResolver
    extends BaseColumn.EntityResolver<AdqlTable, AdqlColumn>
        {
        }

    /**
     * {@link BaseColumn.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseColumn.EntityFactory<AdqlTable, AdqlColumn>
        {
        /**
         * Create a new {@link AdqlColumn}.
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base);

        /**
         * Create a new {@link AdqlColumn}.
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name);

        
        //TODO - move to services
        @Override
        public AdqlColumn.IdentFactory idents();

        //TODO - move to services
        //@Override
        //public AdqlColumn.NameFactory names();

        //TODO - move to services
        @Override
        public AdqlColumn.AliasFactory aliases();
        
        //TODO - move to services
        @Override
        public AdqlColumn.LinkFactory links();
        
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
     * @todo Convert this to an interface to allow user defined types.
     *
     */
    public enum Type
        {
        ARRAY(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-array-1.0.json",     "array",         JdbcColumn.Type.ARRAY),
        BOOLEAN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-boolean-1.0.json",   "boolean",       JdbcColumn.Type.BOOLEAN),
        BIT(            "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-bit-1.0.json",       "bit",           JdbcColumn.Type.BLOB),
        BYTE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-byte-1.0.json",      "unsignedByte",  JdbcColumn.Type.TINYINT),
        CHAR(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-char-1.0.json",      "char",          JdbcColumn.Type.CHAR),
        UNICODE(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unicode-1.0.json",   "unicodeChar",   JdbcColumn.Type.CHAR),
        SHORT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int16-1.0.json",     "short",         JdbcColumn.Type.SMALLINT),
        INTEGER(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int32-1.0.json",     "int",           JdbcColumn.Type.INTEGER),
        LONG(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int64-1.0.json",     "long",          JdbcColumn.Type.BIGINT),
        FLOAT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float32-1.0.json",   "float",         JdbcColumn.Type.FLOAT),
        DOUBLE(         "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float64-1.0.json",   "double",        JdbcColumn.Type.DOUBLE),
        FLOATCOMPLEX(   "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex32-1.0.json", "floatComplex",  JdbcColumn.Type.UNKNOWN),
        DOUBLECOMPLEX(  "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex64-1.0.json", "doubleComplex", JdbcColumn.Type.UNKNOWN),
        DATE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-date-1.0.json",      "char",          JdbcColumn.Type.DATE),      // YYYY-MM-DD
        TIME(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-time-1.0.json",      "char",          JdbcColumn.Type.TIME),      // HH:MM:SS.sss
        DATETIME(       "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-datetime-1.0.json",  "char",          JdbcColumn.Type.DATE),      // YYYY-MM-DDTHH:MM:SS.sss
        UNKNOWN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unknown-1.0.json",   "unknown",       JdbcColumn.Type.UNKNOWN);

        private final String votype ;
        public  String votype()
            {
            return this.votype;
            }

        private final String xtype ;
        public  String xtype()
            {
            return this.xtype;
            }

        private final JdbcColumn.Type jdbc ;
        public  JdbcColumn.Type jdbc()
            {
            return this.jdbc;
            }

        Type(final String xtype, final String votype, final JdbcColumn.Type jdbc)
            {
            this.xtype = xtype ;
            this.votype = votype ;
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
                    return DATETIME ;

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

    /**
     * The {@link AdqlColumn} metadata.
     *
     */
    public interface Metadata
    extends BaseColumn.Metadata
        {
        /**
         * The ADQL metadata.
         *
         */
        public interface Adql
            {
            /**
             * The column name.
             *
             */
            public String name();

            /**
             * The column description.
             * 
             */
            public String text();
            
            /**
             * The array size, or null if this is not an array.
             *
             */
            public Integer arraysize();

            /**
             * Set the array size.
             *
             */
            public void arraysize(final Integer size);

            /**
             * The ADQL type.
             *
             */
            public AdqlColumn.Type type();

            /**
             * Set the ADQL type.
             *
             */
            public void type(final AdqlColumn.Type type);

            /**
             * The ADQL units.
             *
             */
            public String units();

            /**
             * Set the ADQL units.
             *
             */
            public void units(final String unit);

            /**
             * The ADQL utype.
             *
             */
            public String utype();

            /**
             * Set the ADQL utype.
             *
             */
            public void utype(final String utype);

            /**
             * The ADQL dtype.
             *
             */
            public String dtype();

            /**
             * Set the ADQL dtype.
             *
             */
            public void dtype(final String dtype);

            /**
             * The column UCD.
             *
             */
            public String ucd();

            /**
             * Set the column UCD.
             *
             */
            public void ucd(final String value);

            /**
             * Set the column UCD.
             *
             */
            @Deprecated
            public void ucd(final String type, final String value);
            
            }
        /**
         * The ADQL metadata.
         *
         */
        public Adql adql();
        }

    @Override
    public AdqlColumn.Metadata meta();
    }
