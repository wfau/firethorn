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

import uk.ac.roe.wfau.firethorn.adql.AdqlDBTable.Mode;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
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
        public AdqlDBTableImpl create(final ModeApi mode, final TuesdayAdqlTable table)
            {
            return new AdqlDBTableImpl(
                mode,
                table
                );
            }
        }

    /**
     * The mapping mode for table references.
     * 
     */
    private ModeApi mode;
    @Override
    public Mode mode()
        {
        return this.mode.mode();
        }

    /**
     * Our underlying AdqlTable.
     *
     */
    private final TuesdayAdqlTable table ;
    @Override
    public TuesdayAdqlTable table()
        {
        return this.table;
        }

    /**
     * The local ADQL name, if different to the original AdqlTable.
     *
     */
    private String adqlName ;

    /**
     * The local JDBC name, if different to the original JdbcTable.
     *
     */
    private String jdbcName ;

    /**
     * Protected constructor.
     *
     */
    private AdqlDBTableImpl(final ModeApi mode, final TuesdayAdqlTable table)
        {
        this(
            mode,
            table,
            null,
            null
            );
        }

    /**
     * Protected constructor, used by the copy method.
     *
     */
    private AdqlDBTableImpl(final ModeApi mode, final TuesdayAdqlTable table, final String jdbcName, final String adqlName)
        {
        this.mode  = mode ;
        this.table = table ;
        //
        // Only set the ADQL name if it is not the same as the original.
        if (adqlName != null)
            {
            if (adqlName.length() > 0)
                {
                if (adqlName.equals(table.name()) == false)
                    {
                    this.adqlName = adqlName;
                    }
                }
            }
        //
        // Only set the JDBC name if it is not the same as the original.
        if (jdbcName != null)
            {
            if (jdbcName.length() > 0)
                {
                if (jdbcName.equals(table.base().name()) == false)
                    {
                    this.jdbcName = jdbcName;
                    }
                }
            }
        }

    /**
     * Make a copy of the AdqlDBTable, changing the ADQL and JDBC names.
     * @param jdbcName - The new JDBC name (optional). If this is null, then the new AdqlDBTable inherits its JDBC name, schema and catalog from the original.
     * @param adqlName - The new ADQL name (required). This can't be null or empty.
     *
     */
    @Override
    public AdqlDBTable copy(final String jdbcName, final String adqlName)
        {
        if ((adqlName == null) || (adqlName.length() == 0))
            {
            throw new IllegalArgumentException(
                "AdqlName is required"
                );
            }
        return new AdqlDBTableImpl(
            this.mode,
            this.table,
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
            return this.table.name();
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
            return this.table.schema().name();
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
        return null ;
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
            if (this.mode() == AdqlDBTable.Mode.ALIASED)
                {
                return this.table.base().alias();
                }
            else {
                return this.table.base().name();
                }
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
            if (this.mode() == AdqlDBTable.Mode.ALIASED)
                {
                return null ;
                }
            else {
                return this.table.base().schema().name();
                }
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
            if (this.mode() == AdqlDBTable.Mode.ALIASED)
                {
                return null ;
                }
            else {
                //return this.table.base().catalog().name();
                return null ;
                }
            }
        }

    @Override
    public DBColumn getColumn(final String name, final boolean adql)
        {
        TuesdayAdqlColumn adqlColumn = null ;
        //
        // If 'name' is an ADQL name, then search the AdqlTable.
        if (adql)
            {
            adqlColumn = this.table.columns().select(
                name
                );
            }
        //
        // If 'name' is a SQL name, then search the BaseTable.
        else {
/*
 * Search base columns by name, and then find a matching column in our adql table.
 *
            adqlColumn = this.adqlTable.columns().select(
                this.adqlTable.base().columns().select(
                    name
                    )
                );
 */

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

            private final Iterator<TuesdayAdqlColumn> iter = table().columns().select().iterator();

            @Override
            public DBColumn next()
                {
                return wrap(
                    this.iter.next()
                    );
                }

            @Override
            public boolean hasNext()
                {
                return this.iter.hasNext();
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

                    private final Iterator<TuesdayAdqlColumn> iter = table().columns().select().iterator();

                    @Override
                    public AdqlDBColumn next()
                        {
                        return wrap(
                            this.iter.next()
                            );
                        }

                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
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
     * Create a new AdqlColumn wrapper.
     *
     */
    private AdqlColumnImpl wrap(final TuesdayAdqlColumn adqlColumn)
        {
        return new AdqlColumnImpl(
            adqlColumn
            );
        }

    /**
     * Create a new AdqlColumn wrapper.
     *
     */
    private AdqlColumnImpl wrap(final TuesdayAdqlColumn adqlColumn, final String jdbcName, final String adqlName, final DBTable parent)
        {
        return new AdqlColumnImpl(
            adqlColumn,
            jdbcName,
            adqlName,
            parent
            );
        }

    /**
     * Inner class to represent an AdqlColumn.
     *
     */
    public class AdqlColumnImpl
    implements AdqlDBColumn
        {

        /**
         * Our underlying AdqlColumn.
         *
         */
        private final TuesdayAdqlColumn column ;

        @Override
        public TuesdayAdqlColumn column()
            {
            return this.column ;
            }

        /**
         * Local parent adqlTable, if different to the original.
         *
         */
        private final DBTable parent ;

        /**
         * Local ADQL name, if different to the original.
         *
         */
        private final String adqlName ;

        /**
         * Local JDBC name, if different to the original.
         *
         */
        private final String jdbcName ;

        /**
         * Private constructor.
         *
         */
        private AdqlColumnImpl(final TuesdayAdqlColumn adqlColumn)
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
        private AdqlColumnImpl(final TuesdayAdqlColumn adqlColumn, final String jdbcName, final String adqlName, final DBTable parent)
            {
            this.parent = parent ;
            this.column = adqlColumn ;
            this.jdbcName = jdbcName;
            this.adqlName = adqlName;
            }

        @Override
        public AdqlColumnImpl copy(final String dbName, final String adqlName, final DBTable parent)
            {
            return AdqlDBTableImpl.this.wrap(
                this.column,
                this.jdbcName,
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
                return this.column.name();
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
                return this.column.base().alias();
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
