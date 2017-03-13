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
package uk.ac.roe.wfau.firethorn.adql.parser ;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import adql.db.DBColumn;
import adql.db.DBTable;
import adql.db.DBType;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;


/**
 * Implementation of the CDS DBTable interface, using data from a AdqlResource.AdqlTable.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
@Slf4j
public class AdqlParserTableImpl
implements AdqlParserTable
    {

    /**
     * Factory implementation.
     *
     */
    @Component
    public static class Factory
    implements AdqlParserTable.Factory
        {
        @Override
        public AdqlParserTableImpl create(final AdqlQueryBase.Mode mode, final AdqlTable table)
            {
            return new AdqlParserTableImpl(
                mode,
                table
                );
            }
        }

    private final AdqlQueryBase.Mode mode;
    @Override
    public AdqlQueryBase.Mode mode()
        {
        return this.mode;
        }

    /**
     * Our underlying AdqlTable.
     *
     */
    private final AdqlTable table ;
    @Override
    public AdqlTable table()
        {
        return this.table;
        }

    /**
     * The ADQL name, if different to the original AdqlTable.
     *
     */
    private String adqlName ;

    /**
     * The base name, if different to the original BaseTable.
     *
     */
    private String baseName ;

    /**
     * Protected constructor.
     *
     */
    private AdqlParserTableImpl(final AdqlQueryBase.Mode mode, final AdqlTable table)
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
    private AdqlParserTableImpl(final AdqlQueryBase.Mode mode, final AdqlTable table, final String baseName, final String adqlName)
        {
        log.debug("AdqlParserTableImpl(AdqlQuery.Mode mode, AdqlTable, String, String)");
        log.debug("real name [{}]", table.name());
        log.debug("BASE name [{}]", baseName);
        log.debug("ADQL name [{}]", adqlName);
        this.mode  = mode  ;
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
        if (baseName != null)
            {
            if (baseName.length() > 0)
                {
                if (baseName.equals(table.root().name()) == false)
                    {
                    this.baseName = baseName;
                    }
                }
            }
        }

    /**
     * Make a copy of the AdqlParserTable, changing the ADQL and BASE names.
     * @param baseName - The new BASE name (optional). If this is null, then the new AdqlParserTable inherits its BASE name, schema and catalog from the original.
     * @param adqlName - The new ADQL name (required). This can't be null or empty.
     *
     */
    @Override
    public AdqlParserTable copy(final String baseName, final String adqlName)
        {
        log.debug("copy(String, String)");
        log.debug("BASE name [{}]", baseName);
        log.debug("ADQL name [{}]", adqlName);
        if ((adqlName == null) || (adqlName.length() == 0))
            {
            throw new IllegalArgumentException(
                "AdqlName is required"
                );
            }
        return new AdqlParserTableImpl(
            this.mode,
            this.table,
            baseName,
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
     * If the ADQL table name was changed using copy, then the ADQL catalog name will be null.
     *
     */
    @Override
    public String getADQLCatalogName()
        {
        return null ;
        }

    /**
     * Get the BASE table name.
     *
     */
    @Override
    public String getDBName()
        {
        if (this.baseName != null)
            {
            return this.baseName ;
            }
        else {
            if (this.mode() == AdqlQueryBase.Mode.DISTRIBUTED)
                {
                return this.table.root().alias();
                }
            else {
                return this.table.root().name();
                }
            }
        }

    /**
     * Get the BASE schema name.
     * If the base table name was changed using copy, then the BASE schema name will be null.
     *
     */
    @Override
    public String getDBSchemaName()
        {
        if (this.baseName != null)
            {
            return null ;
            }
        else {
            if (this.mode() == AdqlQueryBase.Mode.DISTRIBUTED)
                {
                return null ;
                }
            else {
                if (this.table.root() instanceof JdbcTable)
                    {
                    return ((JdbcTable)this.table.root()).schema().schema();
                    }
                else {
                    return this.table.root().schema().name();
                    }
                }
            }
        }

    /**
     * Get the BASE catalog name.
     * If the BASE table name was changed using copy, then the BASE catalog name will be null.
     *
     */
    @Override
    public String getDBCatalogName()
        {
        if (this.baseName != null)
            {
            return null ;
            }
        else {
            if (this.mode() == AdqlQueryBase.Mode.DISTRIBUTED)
                {
                return null ;
                }
            else {
                if (this.table.root() instanceof JdbcTable)
                    {
                    return ((JdbcTable)this.table.root()).schema().catalog();
                    }
                else {
                    return null ;
                    }
                }
            }
        }

    @Override
    public DBColumn getColumn(final String name, final boolean adql)
        {
        AdqlColumn adqlColumn = null ;
        //
        // If 'name' is an ADQL name, then search the AdqlTable.
        if (adql)
            {
            try {
                adqlColumn = this.table.columns().select(
                    name
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                adqlColumn = null ;
                }
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
        log.debug("Iterator<DBColumn> iterator()");
        return new Iterator<DBColumn>()
            {

            private final Iterator<AdqlColumn> iter = table().columns().select().iterator();

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
                this.iter.remove();
                }
            };
        }

    /**
     * Access to our columns as AdqlColumns rather than DBColumns.
     *
     */
    @Override
    public AdqlParserTable.Columns columns()
        {
        log.debug("AdqlParserTable.Columns columns()");
        return new AdqlParserTable.Columns()
            {
            @Override
            public Iterator<AdqlDBColumn> select()
                {
                return new Iterator<AdqlDBColumn>()
                    {

                    private final Iterator<AdqlColumn> iter = table().columns().select().iterator();

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
    private AdqlColumnImpl wrap(final AdqlColumn adqlColumn)
        {
        return new AdqlColumnImpl(
            adqlColumn
            );
        }

    /**
     * Create a new AdqlColumn wrapper.
     *
     */
    private AdqlColumnImpl wrap(final AdqlColumn adqlColumn, final String jdbcName, final String adqlName, final DBTable parent)
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
        private final AdqlColumn column ;

        @Override
        public AdqlColumn column()
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
         * Local BASE name, if different to the original.
         *
         */
        private final String baseName ;

        /**
         * Private constructor.
         *
         */
        private AdqlColumnImpl(final AdqlColumn adqlColumn)
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
        private AdqlColumnImpl(final AdqlColumn adqlColumn, final String baseName, final String adqlName, final DBTable parent)
            {
            log.trace("AdqlColumnImpl(AdqlColumn, String, String, DBTable)");
            log.trace("real name [{}]", adqlColumn.name());
            log.trace("BASE name [{}]", baseName);
            log.trace("ADQL name [{}]", adqlName);
            this.parent = parent ;
            this.column = adqlColumn ;
            this.baseName = baseName;
            this.adqlName = adqlName;
            }

        @Override
        public AdqlColumnImpl copy(final String dbName, final String adqlName, final DBTable parent)
            {
            log.trace("copy(String, String, DBTable)");
            log.trace("DB   name [{}]", dbName);
            log.trace("ADQL name [{}]", adqlName);
            return AdqlParserTableImpl.this.wrap(
                this.column,
                this.baseName,
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
            log.trace("getDBName() [{}][{}]", this.baseName, this.column.root().name());
            String result = this.baseName ;
            if (result == null)
                {
                //result = this.column.root().alias();
                result = this.column.root().name();
                }
            return result ;
            }

        @Override
        public DBTable getTable()
            {
            if (this.parent != null)
                {
                return this.parent ;
                }
            else {
                return AdqlParserTableImpl.this;
                }
            }

		@Override
		public DBType getDatatype() {
			// TODO Auto-generated method stub
			return null;
		}
        }
    }
