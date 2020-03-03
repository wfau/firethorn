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

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;

/**
 * Public interface for an abstract ADQL column.
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base)
        throws ProtectionException;

        /**
         * Create a new {@link AdqlColumn}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name)
        throws ProtectionException;
        
        /**
         * Create a new {@link AdqlColumn}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final AdqlColumn.Metadata meta)
        throws ProtectionException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<AdqlColumn>
        {
        /**
         * Our {@link AdqlColumn.EntityFactory} instance.
         *
         */
        public AdqlColumn.EntityFactory entities();

        /**
         * {@link AdqlColumn.AliasFactory} instance.
         *
         */
        public AdqlColumn.AliasFactory aliases();

        }
    
    @Override
    public AdqlTable table()
    throws ProtectionException;

    @Override
    public AdqlSchema schema()
    throws ProtectionException;

    @Override
    public AdqlResource resource()
    throws ProtectionException;

    @Override
    public BaseColumn<?> base()
    throws ProtectionException;

    /**
     * An enumeration of the VOTable data types, as defined in section 2.1 of the VOTable-1.2 specification.
     * @see <a href='http://www.ivoa.net/Documents/VOTable/20091130/'/>
     * Plus ARRAY to handle array data.
     * Plus USER  to handle user defined types.
     * Plus DATE, TIME and TIMESTAMP.
     *
     */
    @Slf4j
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
                case CHAR  :
                case NCHAR :
                case NCLOB :
                case VARCHAR :
                case LONGVARCHAR :
                case NVARCHAR :
                case LONGNVARCHAR :
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
                case DATETIME :
                    return AdqlColumn.AdqlType.DATETIME;
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
                    return AdqlColumn.AdqlType.INTEGER;


                case BLOB :
                case BINARY :
                case VARBINARY :
                case LONGVARBINARY :
                    return AdqlColumn.AdqlType.BYTE;

                case CLOB :
                case DATALINK :
                case DECIMAL :
                case DISTINCT :
                case JAVA_OBJECT :
                case NULL :
                case NUMERIC :
                case OTHER :
                case REF :
                case ROWID :
                case SQLXML :
                case STRUCT :
                case UNKNOWN :
                    return AdqlColumn.AdqlType.UNKNOWN ;

                default :
                    return AdqlColumn.AdqlType.UNKNOWN;
                }
            }

        /**
         * Our dictionary of {@link AdqlColumn.AdqlType}s.
         *
         */
        private static final Map<String, AdqlType> map = new HashMap<String, AdqlType>();
        static {
            //
            // Add all the ADQL type names. 
            for (AdqlType type : AdqlType.values())
                {
                map.put(
                    type.name().toUpperCase(),
                    type
                    );
                }
            //
            // Add all the JDBC type names. 
            for (JdbcColumn.JdbcType type : JdbcColumn.JdbcType.values())
                {
                map.put(
                    type.name().toUpperCase(),
                    resolve(
                        type
                        )
                    );
                }
            }

        /**
         * Resolve a {@link String} into a {@link AdqlColumn.AdqlType}.
         *
         */
        public static AdqlType resolve(final String name)
            {
            log.debug("resolve(String) [{}]", name);
            final AdqlType type = map.get(
                name.toUpperCase()
                );
            if (type != null)
                {
                return type ;
                }
            else {
                return AdqlType.UNKNOWN;
                }
            }
        }

    /**
     * The {@link AdqlColumn} metadata interface.
     *
     */
    public interface Metadata
    extends BaseColumn.Metadata
        {
        /**
         * The ADQL metadata interface.
         *
         */
        public interface Adql
            {
            /**
             * The column name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;

            /**
             * The column description.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String text()
            throws ProtectionException;
            
            /**
             * The array size (element count).
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public Integer arraysize()
            throws ProtectionException;

            /**
             * The ADQL type.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public AdqlColumn.AdqlType type()
            throws ProtectionException;

            /**
             * The ADQL units.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String units()
            throws ProtectionException;

            /**
             * The ADQL utype.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String utype()
            throws ProtectionException;

            /**
             * The column UCD.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String ucd()
            throws ProtectionException;

            }
        /**
         * The ADQL metadata.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Adql adql()
        throws ProtectionException;

        }

    /**
     * The {@link AdqlColumn} modifier interface.
     *
     */
    public interface Modifier
    extends AdqlColumn.Metadata
        {
        /**
         * The ADQL modifier interface.
         *
         */
        public interface Adql
        extends AdqlColumn.Metadata.Adql
            {
            /**
             * The array size (element count).
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void arraysize(final Integer size)
            throws ProtectionException;
            
            /**
             * Set the ADQL type.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void type(final AdqlColumn.AdqlType type)
            throws ProtectionException;

            /**
             * Set the ADQL type.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void type(final String dtype)
            throws ProtectionException;

            /**
             * Set the ADQL utype.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void utype(final String utype)
            throws ProtectionException;

            /**
             * Set the ADQL units.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void units(final String unit)
            throws ProtectionException;

            /**
             * Set the column UCD.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void ucd(final String value)
            throws ProtectionException;

            }

        /**
         * The ADQL modifier.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Adql adql()
        throws ProtectionException;

        }

    @Override
    public AdqlColumn.Modifier meta()
    throws ProtectionException;

    /**
     * Update the {@link AdqlColumn} properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final AdqlColumn.Metadata.Adql meta)
    throws ProtectionException;

    }
