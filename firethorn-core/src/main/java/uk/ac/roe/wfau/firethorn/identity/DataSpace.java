/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.identity;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Public interface for space in a database
 * @todo
 *   promote to a full Entity
 *   with an owner ..
 *   and quota etc.
 *
 */
public interface DataSpace
//extends Entity
    {

    /**
     * The JdbcSchema where the data is stored.
     * 
     */
    public JdbcSchema jdbc();

    /**
     * The AdqlSchema where the data appears.
     * 
     */
    public AdqlSchema adql();

    }