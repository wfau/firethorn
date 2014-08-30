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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
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
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlColumn>
        {
        }

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<AdqlColumn>
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
    @Deprecated
    public enum OldAdqlType
        {
        ARRAY(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-array-1.0.json",     "array",         JdbcColumn.OldJdbcType.ARRAY),
        BOOLEAN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-boolean-1.0.json",   "boolean",       JdbcColumn.OldJdbcType.BOOLEAN),
        BIT(            "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-bit-1.0.json",       "bit",           JdbcColumn.OldJdbcType.BLOB),
        BYTE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-byte-1.0.json",      "unsignedByte",  JdbcColumn.OldJdbcType.TINYINT),
        CHAR(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-char-1.0.json",      "char",          JdbcColumn.OldJdbcType.CHAR),
        UNICODE(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unicode-1.0.json",   "unicodeChar",   JdbcColumn.OldJdbcType.CHAR),
        SHORT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int16-1.0.json",     "short",         JdbcColumn.OldJdbcType.SMALLINT),
        INTEGER(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int32-1.0.json",     "int",           JdbcColumn.OldJdbcType.INTEGER),
        LONG(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int64-1.0.json",     "long",          JdbcColumn.OldJdbcType.BIGINT),
        FLOAT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float32-1.0.json",   "float",         JdbcColumn.OldJdbcType.FLOAT),
        DOUBLE(         "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float64-1.0.json",   "double",        JdbcColumn.OldJdbcType.DOUBLE),
        FLOATCOMPLEX(   "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex32-1.0.json", "floatComplex",  JdbcColumn.OldJdbcType.UNKNOWN),
        DOUBLECOMPLEX(  "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex64-1.0.json", "doubleComplex", JdbcColumn.OldJdbcType.UNKNOWN),
        DATE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-date-1.0.json",      "char",          JdbcColumn.OldJdbcType.DATE),      // YYYY-MM-DD
        TIME(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-time-1.0.json",      "char",          JdbcColumn.OldJdbcType.TIME),      // HH:MM:SS.sss
        DATETIME(       "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-datetime-1.0.json",  "char",          JdbcColumn.OldJdbcType.DATE),      // YYYY-MM-DDTHH:MM:SS.sss
        UNKNOWN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unknown-1.0.json",   "unknown",       JdbcColumn.OldJdbcType.UNKNOWN);

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

        private final JdbcColumn.OldJdbcType jdbc ;
        public  JdbcColumn.OldJdbcType jdbc()
            {
            return this.jdbc;
            }

        OldAdqlType(final String xtype, final String votype, final JdbcColumn.OldJdbcType jdbc)
            {
            this.xtype = xtype ;
            this.votype = votype ;
            this.jdbc = jdbc ;
            }

        /**
         * Mapping from JdbcColumn.Type to AdqlColumn.Type.
         * @see JdbcColumn.OldJdbcType
         *
         *
        public static AdqlColumn.OldAdqlType type(final JdbcColumn.OldJdbcType jdbc)
            {
            return type(
                jdbc.sqltype()
                );
            }

        /**
         * Mapping from java.sql.Types to AdqlColumnType.
         * @see java.sql.Types
         *
         *
        public static AdqlColumn.OldAdqlType type(final int sql)
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

        public static AdqlColumn.OldAdqlType type(final String name)
            {
            if(name != null)
                {
                return AdqlColumn.OldAdqlType.valueOf(
                    name.trim().toUpperCase()
                    );
                }
            else {
                return null ;
                }
            }
        }
     */

    /**
     * An enumeration of the VOTable data types, as defined in section 2.1 of the VOTable-1.2 specification.
     * @see <a href='http://www.ivoa.net/Documents/VOTable/20091130/'/>
     * Plus ARRAY to handle array data.
     * Plus USER  to handle user defined types.
     * Plus DATE, TIME and TIMESTAMP.
     *
     */
    public enum AdqlType
        {
        ARRAY(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-array-1.0.json",     "array",         null,      true),
        BOOLEAN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-boolean-1.0.json",   "boolean",       null,      false),
        BIT(            "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-bit-1.0.json",       "bit",           null,      false),
        BYTE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-byte-1.0.json",      "unsignedByte",  null,      false),
        CHAR(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-char-1.0.json",      "char",          null,      true),
        UNICODE(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unicode-1.0.json",   "unicodeChar",   null,      true),
        SHORT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int16-1.0.json",     "short",         null,      false),
        INTEGER(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int32-1.0.json",     "int",           null,      false),
        LONG(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-int64-1.0.json",     "long",          null,      false),
        FLOAT(          "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float32-1.0.json",   "float",         null,      false),
        DOUBLE(         "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-float64-1.0.json",   "double",        null,      false),
        FLOATCOMPLEX(   "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex32-1.0.json", "floatComplex",  null,      false),
        DOUBLECOMPLEX(  "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-complex64-1.0.json", "doubleComplex", null,      false),
        DATE(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-date-1.0.json",      "char",          null,      true),      // YYYY-MM-DD
        TIME(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-time-1.0.json",      "char",          null,      true),      // HH:MM:SS.sss
        DATETIME(       "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-datetime-1.0.json",  "char",          null,      true),      // YYYY-MM-DDTHH:MM:SS.sss
        USER(           "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-user-1.0.json",      "user",          null,      false),
        UNKNOWN(        "http://data.metagrid.co.uk/wfau/firethorn/types/xtype/xtype-unknown-1.0.json",   "unknown",       null,      false);

        private AdqlType(final String purl, final String votype, final String xtype, final boolean isarray)
            {
            this.purl     = purl ;
            this.xtype    = xtype ;
            this.votype   = votype ;
            this.isarray  = isarray ;
            }

        private final String purl ;
        public  String purl()
            {
            return this.purl;
            }

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

        private final boolean isarray ;
        public  boolean isarray()
            {
            return this.isarray;
            }

        /**
         * Resolve our corresponding {@link JdbcColumn.JdbcType} 
         * 
         */
        public JdbcColumn.JdbcType jdbctype()
            {
            return JdbcColumn.JdbcType.resolve(
                this
                );
            }
        
        /**
         * Resolve a {@link JdbcColumn.JdbcType} into a {@link AdqlColumn.AdqlType}.
         * 
         */
        public static AdqlColumn.AdqlType resolve(final JdbcColumn.JdbcType jdbc)
            {
            switch (jdbc)
                {
                case CHAR    :
                case VARCHAR :
                    return AdqlColumn.AdqlType.CHAR;

                case ARRAY :
                    return AdqlColumn.AdqlType.ARRAY;

                case BIGINT :
                    return AdqlColumn.AdqlType.LONG;
                case BIT :
                    return AdqlColumn.AdqlType.BIT;
                case BOOLEAN :
                    return AdqlColumn.AdqlType.BOOLEAN;
                case DATE :
                    return AdqlColumn.AdqlType.DATE;
                case DOUBLE :
                    return AdqlColumn.AdqlType.DOUBLE;
                case FLOAT :
                    return AdqlColumn.AdqlType.FLOAT;
                case INTEGER :
                    return AdqlColumn.AdqlType.INTEGER;
                case REAL :
                    return AdqlColumn.AdqlType.FLOAT;
                case SMALLINT :
                    return AdqlColumn.AdqlType.SHORT;
                case TIME :
                    return AdqlColumn.AdqlType.TIME;
                case TIMESTAMP :
                    return AdqlColumn.AdqlType.DATETIME;
                case TINYINT :
                    return AdqlColumn.AdqlType.BYTE;

                case BINARY :
                case BLOB :
                case CLOB :
                case DATALINK :
                case DECIMAL :
                case DISTINCT :
                case JAVA_OBJECT :
                case LONGNVARCHAR :
                case LONGVARBINARY :
                case LONGVARCHAR :
                case NCHAR :
                case NCLOB :
                case NULL :
                case NUMERIC :
                case NVARCHAR :
                case OTHER :
                case REF :
                case ROWID :
                case SQLXML :
                case STRUCT :
                case VARBINARY :
                case UNKNOWN :
                    return AdqlColumn.AdqlType.UNKNOWN ;

                default :
                    return AdqlColumn.AdqlType.UNKNOWN;
                }
            }
        
        /**
         * Resolve a {@link AdqlColumn.AdqlType} name into a {@link AdqlColumn.AdqlType}.
         *
         */
        @Deprecated
        public static AdqlColumn.AdqlType ename(final String ename)
            {
            if (ename != null)
                {
                return AdqlColumn.AdqlType.valueOf(
                    ename.trim().toUpperCase()
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
             * The array size (element count).
             *
             */
            public Integer arraysize();

            /**
             * The array size (element count).
             *
             */
            public void arraysize(final Integer size);
            
            /**
             * The ADQL type.
             *
             */
            public AdqlColumn.AdqlType type();

            /**
             * Set the ADQL type.
             *
             */
            public void type(final AdqlColumn.AdqlType type);

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
