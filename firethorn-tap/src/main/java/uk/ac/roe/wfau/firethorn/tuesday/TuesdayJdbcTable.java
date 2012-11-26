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

/**
 *
 *
 */
public interface TuesdayJdbcTable
extends TuesdayOgsaTable<TuesdayJdbcColumn>
    {
    @Override
    public TuesdayJdbcResource resource();
    @Override
    public TuesdayJdbcCatalog catalog();
    @Override
    public TuesdayJdbcSchema schema();

    public interface Columns extends TuesdayOgsaTable.Columns<TuesdayJdbcColumn> 
        {
        @Override
        public Iterable<TuesdayJdbcColumn> select();
        @Override
        public TuesdayJdbcColumn select(String name);
        } 
    @Override
    public Columns columns();

    }
