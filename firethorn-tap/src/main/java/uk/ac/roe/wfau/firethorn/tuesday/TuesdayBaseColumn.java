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
public interface TuesdayBaseColumn<ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBaseComponent
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayBaseColumn<?>>
        {
        }

    /**
     * Column factory interface.
     *
     */
    public static interface Factory<TableType extends TuesdayBaseTable<TableType, ColumnType>, ColumnType extends TuesdayBaseColumn<ColumnType>>
    extends Entity.Factory<ColumnType>
        {
        /**
         * Select all the tables from a table.
         *
         */
        public Iterable<ColumnType> select(final TableType parent);

        /**
         * Select a named table from a schema.
         *
         */
        public ColumnType select(final TableType parent, final String name);

        /**
         * Text search for tables (name starts with).
         *
         */
        public Iterable<ColumnType> search(final TableType parent, final String text);

        }

    public TuesdayOgsaColumn<?> ogsa();

    public TuesdayBaseTable<?,?> table();
    public TuesdayBaseSchema<?,?> schema();
    public TuesdayBaseResource<?> resource();

    public String type();
    public void type(String type);
    
    public Integer size();
    public void size(Integer size);
    
    public String ucd();
    public void ucd(String ucd);

    public String alias();    //"column_ident"
    public StringBuilder fullname(); //"catalog.schema.table.column"

    interface Linked
        {
        public Iterable<TuesdayAdqlColumn> select();
        }
    public Linked linked();

    }
