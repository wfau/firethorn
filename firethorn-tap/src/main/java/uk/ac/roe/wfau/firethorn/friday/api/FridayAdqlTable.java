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
package uk.ac.roe.wfau.firethorn.friday.api;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 *
 *
 */
public interface FridayAdqlTable
extends Entity
    {

    public String name() ;
    public String text() ;
    public String link() ;

    public String utype() ;
    public String ucd() ;

    public FridayAdqlSchema schema() ;
    public FridayAdqlWorkspace workspace() ;

    public interface Columns
        {
        public Iterable<FridayAdqlColumn> select();
        public FridayAdqlColumn select(String name);
        }

    public FridayOgsaTable ogsa();
    
    }
