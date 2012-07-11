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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import adql.db.DBTable;
import adql.db.DBColumn;

import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView ;

/**
 * Implementation of the CDS DBTable interface, using data from a WidgeonView.Table.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
public class AdqlTableImpl
implements AdqlTable
    {

    /**
     * Factory implementation.
     *
     */
    @Component
    public static class Factory
    implements AdqlTable.Factory
        {

        /**
         * Create a new AdqlTable.
         *
         */
        public AdqlTableImpl create(WidgeonView.Table table)
            {
            return new AdqlTableImpl(
                table
                );
            }
        }

    /**
     * Our WidgeonView.Table metadata source.
     *
     */
    private WidgeonView.Table table ;

    @Override
    public WidgeonView.Table meta()
        {
        return this.table;
        }

    /**
     * Local ADQL name, if different to the original WidgeonView.Table.
     *
     */
    private String adqlName ;

    /**
     * Local JDBC name, if different to the original WidgeonBase.Table.
     *
     */
    private String jdbcName ;

    /**
     * Protected constructor.
     *
     */
    private AdqlTableImpl(WidgeonView.Table table)
        {
        this(
            table,
            null,
            null
            );
        }

    /**
     * Protected constructor, used by the copy method.
     *
     */
    private AdqlTableImpl(WidgeonView.Table table, String jdbcName, String adqlName)
        {
        this.table = table ;
        //
        // Only set the ADQL name if it is not the same as the WidgeonView.Table name.
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
        // Only set the JDBC name if it is not the same as the WidgeonBase.Table name.
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

    @Override
    /**
     * Make a copy of the AdqlTable, changing the ADQL and JDBC names.
     * @param jdbcName - The new JDBC name (optional). If this is null, then the new AdqlTable inherits its JDBC name, schema and catalog from the original.
     * @param adqlName - The new ADQL name (required). This can't be null or empty.
     *
     */
    public AdqlTable copy(String jdbcName, String adqlName)
        {
        if ((adqlName == null) || (adqlName.length() == 0))
            {
            throw new IllegalArgumentException(
                "AdqlName is required"
                );
            }
        return new AdqlTableImpl(
            this.table,
            jdbcName, 
            adqlName
            );
        }

    /**
     * Get the ADQL table name.
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
            return table.name();
            }
        }

    /**
     * Get the ADQL schema name.
     * If the ADQL table name was changed using copy, then the ADQL schema name will be null.
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
            return table.parent().name();
            }
        }

    /**
     * Get the ADQL catalog name.
     * If the ADQL table name was changed using copy, then the ADQL catalog name will be null.
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
            return table.parent().parent().name();
            }
        }

    /**
     * Get the JDBC table name.
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
            return table.base().name();
            }
        }

    /**
     * Get the JDBC schema name.
     * If the JDBC table name was changed using copy, then the JDBC schema name will be null.
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
            return table.base().parent().name();
            }
        }

    /**
     * Get the JDBC catalog name.
     * If the JDBC table name was changed using copy, then the JDBC catalog name will be null.
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
            return table.base().parent().parent().name();
            }
        }
    
    @Override
    public DBColumn getColumn(String name, boolean adql)
        {
        WidgeonView.Column column ;
        //
        // If 'name' is an ADQL name, then search the WidgeonView.
        if (adql)
            {
            column = table.columns().search(
                name
                );
            }
        //
        // If 'name' is not an ADQL name, then search the WidgeonBase.
        else {
            column = table.columns().search(
                table.base().columns().search(
                    name
                    )
                );
            }

        if (column != null)
            {
            return wrap(
                column
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

            private Iterator<WidgeonView.Column> iter = table.columns().select().iterator();

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
    public AdqlTable.Columns columns()
        {
        return new AdqlTable.Columns()
            {
            public Iterator<AdqlColumn> select()
                {
                return new Iterator<AdqlColumn>()
                    {

                    private Iterator<WidgeonView.Column> iter = table.columns().select().iterator();

                    @Override
                    public AdqlColumn next()
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
     * Create a new column metadata.
     *
     */
    private AdqlColumnImpl wrap(WidgeonView.Column column)
        {
        return new AdqlColumnImpl(
            column
            );
        }

    /**
     * Create a new column metadata.
     *
     */
    private AdqlColumnImpl wrap(WidgeonView.Column column, String jdbcName, String adqlName, DBTable parent)
        {
        return new AdqlColumnImpl(
            column,
            jdbcName,
            adqlName,
            parent
            );
        } 

    /**
     * Inner class to represent a table column.
     *
     */
    public class AdqlColumnImpl
    implements AdqlColumn
        {

        /**
         * Our WidgeonView.Column metadata source.
         *
         */
        private WidgeonView.Column column ;

        @Override
        public WidgeonView.Column meta()
            {
            return this.column ;
            }

        /**
         * Local parent table, if different to the original AdqlTable.
         *
         */
        private DBTable parent ;

        /**
         * Local ADQL name, if different to the original WidgeonView.Column.
         *
         */
        private String adqlName ;

        /**
         * Local JDBC name, if different to the original Widgeonbase.Column.
         *
         */
        private String jdbcName ;

        /**
         * Private constructor.
         *
         */
        private AdqlColumnImpl(WidgeonView.Column column)
            {
            this(
                column,
                null,
                null,
                null
                );
            }

        /**
         * Private constructor, used by the copy method.
         *
         */
        private AdqlColumnImpl(WidgeonView.Column column, String jdbcName, String adqlName, DBTable parent)
            {
            this.parent = parent ;
            this.column = column ;
            this.jdbcName = jdbcName;
            this.adqlName = adqlName;
            }

        @Override
        public AdqlColumnImpl copy(String dbName, String adqlName, DBTable parent)
            {
            return AdqlTableImpl.this.wrap(
                this.column,
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
                return column.name();
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
                return column.base().name();
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
                return AdqlTableImpl.this;
                }
            }
        }
    }

