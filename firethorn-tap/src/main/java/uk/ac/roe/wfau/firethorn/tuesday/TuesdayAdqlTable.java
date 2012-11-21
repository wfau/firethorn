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
public interface TuesdayAdqlTable
extends TuesdayBaseTable
    {
    public TuesdayBaseTable base();

    @Override
    public TuesdayAdqlSchema schema();
    public void schema(TuesdayAdqlSchema schema);
    @Override
    public TuesdayAdqlResource resource();

    interface Columns
        {
        public Iterable<TuesdayAdqlColumn> select();
        public TuesdayAdqlColumn select(String name);
        } 
    public Columns columns();

    }
