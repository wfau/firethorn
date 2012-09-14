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
package uk.ac.roe.wfau.firethorn.adql ;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;

import adql.db.DBColumn;
import adql.db.DBTable;


/**
 * Implementation of the CDS DBTable interface, using data from a AdqlResource.AdqlTable.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
public class AdqlDBTableImpl
implements AdqlDBTable
    {

    /**
     * Factory implementation.
     *
     */
    @Component
    public static class Factory
    implements AdqlDBTable.Factory
        {

        /**
         * Create a new AdqlDBTable.
         *
         */
        @Override
        public AdqlDBTableImpl create(final AdqlResource.AdqlTable adqlTable)
            {
            return new AdqlDBTableImpl(
                adqlTable
                );
            }
        }

    /**
     * Our AdqlResource.AdqlTable metadata source.
     *
     */
    private final AdqlResource.AdqlTable adqlTable ;

    @Override
    public AdqlResource.AdqlTable meta()
        {
        return this.adqlTable;
        }

    /**
     * Local ADQL name, if different to the original AdqlResource.AdqlTable.
     *
     */
    private String adqlName ;

    /**
     * Local JDBC name, if different to the original BaseResource.BaseTable.
     *
     */
    private String jdbcName ;

    /**
     * Protected constructor.
     *
     */
    private AdqlDBTableImpl(final AdqlResource.AdqlTable adqlTable)
        {
        this(
            adqlTable,
            null,
            null
            );
        }

    /**
     * Protected constructor, used by the copy method.
     *
     */
    private AdqlDBTableImpl(final AdqlResource.AdqlTable adqlTable, final String jdbcName, final String adqlName)
        {
        this.adqlTable = adqlTable ;
        //
        // Only set the ADQL name if it is not the same as the AdqlResource.AdqlTable name.
        if (adqlName != null)
            {
            if (adqlName.length() > 0)
                {
                if (adqlName.equals(adqlTable.name()) == false)
                    {
                    this.adqlName = adqlName;
                    }
                }
            }
        //
        // Only set the JDBC name if it is not the same as the BaseResource.BaseTable name.
        if (jdbcName != null)
            {
            if (jdbcName.length() > 0)
                {
                if (jdbcName.equals(adqlTable.base().name()) == false)
                    {
                    this.jdbcName = jdbcName;
                    }
                }
            }
        }

    @Override
    /**
     * Make a copy of the AdqlDBTable, changing the ADQL and JDBC names.
     * @param jdbcName - The new JDBC name (optional). If this is null, then the new AdqlDBTable inherits its JDBC name, schema and catalog from the original.
     * @param adqlName - The new ADQL name (required). This can't be null or empty.
     *
     */
    public AdqlDBTable copy(final String jdbcName, final String adqlName)
        {
        if ((adqlName == null) || (adqlName.length() == 0))
            {
            throw new IllegalArgumentException(
                "AdqlName is required"
                );
            }
        return new AdqlDBTableImpl(
            this.adqlTable,
            jdbcName,
            adqlName
            );
        }

    /**
     * Get the ADQL adqlTable name.
     *
     */
    @Override
    public String getADQLName()
        {
        if (this.adqlName != null)
            {
            return this.adqlName ;
            }
        else {
            return adqlTable.name();
            }
        }

    /**
     * Get the ADQL schema name.
     * If the ADQL adqlTable name was changed using copy, then the ADQL schema name will be null.
     *
     */
    @Override
    public String getADQLSchemaName()
        {
        if (this.adqlName != null)
            {
            return null ;
            }
        else {
            return adqlTable.parent().name();
            }
        }

    /**
     * Get the ADQL catalog name.
     * If the ADQL adqlTable name was changed using copy, then the ADQL catalog name will be null.
     *
     */
    @Override
    public String getADQLCatalogName()
        {
        if (this.adqlName != null)
            {
            return null ;
            }
        else {
            return adqlTable.parent().parent().name();
            }
        }

    /**
     * Get the JDBC adqlTable name.
     *
     */
    @Override
    public String getDBName()
        {
        if (this.jdbcName != null)
            {
            return this.jdbcName ;
            }
        else {
            return adqlTable.base().name();
            }
        }

    /**
     * Get the JDBC schema name.
     * If the JDBC adqlTable name was changed using copy, then the JDBC schema name will be null.
     *
     */
    @Override
    public String getDBSchemaName()
        {
        if (this.jdbcName != null)
            {
            return null ;
            }
        else {
            return adqlTable.base().parent().name();
            }
        }

    /**
     * Get the JDBC catalog name.
     * If the JDBC adqlTable name was changed using copy, then the JDBC catalog name will be null.
     *
     */
    @Override
    public String getDBCatalogName()
        {
        if (this.jdbcName != null)
            {
            return null ;
            }
        else {
            return adqlTable.base().parent().parent().name();
            }
        }

    @Override
    public DBColumn getColumn(final String name, final boolean adql)
        {
        AdqlResource.AdqlColumn adqlColumn ;
        //
        // If 'name' is an ADQL name, then search the AdqlResource.
        if (adql)
            {
            adqlColumn = adqlTable.columns().search(
                name
                );
            }
        //
        // If 'name' is not an ADQL name, then search the BaseResource.
        else {
            adqlColumn = adqlTable.columns().search(
                adqlTable.base().columns().search(
                    name
                    )
                );
            }

        if (adqlColumn != null)
            {
            return wrap(
                adqlColumn
                );
            }
        else {
            return null ;
            }
        }

    @Override
    public Iterator<DBColumn> iterator()
        {
        return new Iterator<DBColumn>()
            {

            private final Iterator<AdqlResource.AdqlColumn> iter = adqlTable.columns().select().iterator();

            @Override
            public DBColumn next()
                {
                return wrap(
                    iter.next()
                    );
                }

            @Override
            public boolean hasNext()
                {
                return iter.hasNext();
                }

            @Override
            public void remove()
                {
                throw new UnsupportedOperationException(
                    "Iterator.remove() is not supported"
                    );
                }
            };
        }

    /**
     * Access to our columns as AdqlColumns rather than DBColumns.
     *
     */
    @Override
    public AdqlDBTable.Columns columns()
        {
        return new AdqlDBTable.Columns()
            {
            @Override
            public Iterator<AdqlDBColumn> select()
                {
                return new Iterator<AdqlDBColumn>()
                    {

                    private final Iterator<AdqlResource.AdqlColumn> iter = adqlTable.columns().select().iterator();

                    @Override
                    public AdqlDBColumn next()
                        {
                        return wrap(
                            iter.next()
                            );
                        }

                    @Override
                    public boolean hasNext()
                        {
                        return iter.hasNext();
                        }

                    @Override
                    public void remove()
                        {
                        throw new UnsupportedOperationException(
                            "Iterator.remove() is not supported"
                            );
                        }
                    };
                }
            };
        }

    /**
     * Create a new adqlColumn metadata.
     *
     */
    private AdqlColumnImpl wrap(final AdqlResource.AdqlColumn adqlColumn)
        {
        return new AdqlColumnImpl(
            adqlColumn
            );
        }

    /**
     * Create a new adqlColumn metadata.
     *
     */
    private AdqlColumnImpl wrap(final AdqlResource.AdqlColumn adqlColumn, final String jdbcName, final String adqlName, final DBTable parent)
        {
        return new AdqlColumnImpl(
            adqlColumn,
            jdbcName,
            adqlName,
            parent
            );
        }

    /**
     * Inner class to represent a adqlTable adqlColumn.
     *
     */
    public class AdqlColumnImpl
    implements AdqlDBColumn
        {

        /**
         * Our AdqlResource.AdqlColumn metadata source.
         *
         */
        private final AdqlResource.AdqlColumn adqlColumn ;

        @Override
        public AdqlResource.AdqlColumn meta()
            {
            return this.adqlColumn ;
            }

        /**
         * Local parent adqlTable, if different to the original AdqlDBTable.
         *
         */
        private final DBTable parent ;

        /**
         * Local ADQL name, if different to the original AdqlResource.AdqlColumn.
         *
         */
        private final String adqlName ;

        /**
         * Local JDBC name, if different to the original Widgeonbase.Column.
         *
         */
        private final String jdbcName ;

        /**
         * Private constructor.
         *
         */
        private AdqlColumnImpl(final AdqlResource.AdqlColumn adqlColumn)
            {
            this(
                adqlColumn,
                null,
                null,
                null
                );
            }

        /**
         * Private constructor, used by the copy method.
         *
         */
        private AdqlColumnImpl(final AdqlResource.AdqlColumn adqlColumn, final String jdbcName, final String adqlName, final DBTable parent)
            {
            this.parent = parent ;
            this.adqlColumn = adqlColumn ;
            this.jdbcName = jdbcName;
            this.adqlName = adqlName;
            }

        @Override
        public AdqlColumnImpl copy(final String dbName, final String adqlName, final DBTable parent)
            {
            return AdqlDBTableImpl.this.wrap(
                this.adqlColumn,
                jdbcName,
                adqlName,
                parent
                );
            }

        @Override
        public String getADQLName()
            {
            if (this.adqlName != null)
                {
                return this.adqlName ;
                }
            else {
                return adqlColumn.name();
                }
            }

        @Override
        public String getDBName()
            {
            if (this.jdbcName != null)
                {
                return this.jdbcName ;
                }
            else {
                return adqlColumn.base().name();
                }
            }

        @Override
        public DBTable getTable()
            {
            if (this.parent != null)
                {
                return this.parent ;
                }
            else {
                return AdqlDBTableImpl.this;
                }
            }
        }
    }

