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
package uk.ac.roe.wfau.firethorn.tuesday;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 *
 *
 */
public interface TuesdayBaseTable<TableType extends TuesdayBaseTable<TableType, ColumnType>, ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBase
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayBaseTable<?,?>>
        {
        }

    /**
     * Table factory interface.
     *
     */
    public static interface Factory<SchemaType extends TuesdayBaseSchema<SchemaType, TableType>, TableType extends TuesdayBaseTable<TableType,?>>
    extends Entity.Factory<TableType>
        {
        /**
         * Select all the tables from a schema.
         *
         */
        public Iterable<TableType> select(final SchemaType parent);

        /**
         * Select a named table from a schema.
         *
         */
        public TableType select(final SchemaType parent, final String name);

        /**
         * Text search for tables (name starts with).
         *
         */
        public Iterable<TableType> search(final SchemaType parent, final String text);

        }
    
    public String type();
    public void type(String type);
    
    public Integer size();
    public void size(Integer size);
    
    public String ucd();
    public void ucd(String ucd);

    public String alias();    //"table_ident"
    public StringBuilder fullname(); //"catalog.schema.table"

    public TuesdayOgsaTable<?, ?> ogsa();

    public TuesdayBaseSchema<?,TableType> schema();
    public TuesdayBaseResource<?> resource();

    /**
     * The table columns.
     *
     */
    public interface Columns<ColumnType extends TuesdayBaseColumn<ColumnType>>
        {
        public Iterable<ColumnType> select();
        public ColumnType select(String name);
        } 

    /**
     * The table columns.
     *
     */
    public Columns<ColumnType> columns();

    /**
     * The tables linked to this table.
     *
     */
    interface Linked
        {
        public Iterable<TuesdayAdqlTable> select();
        }

    /**
     * The tables linked to this table.
     *
     */
    public Linked linked();

    }
