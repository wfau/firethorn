/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

public interface AdqlColumnModel
    {

    /**
     * MVC property for the {@link AdqlColumn} name, [{@value}].
     *
     */
    public static final String COLUMN_NAME_PARAM = "adql.column.name" ;

    /**
     * MVC property for the {@link AdqlColumn} {@link Identifier}, [{@value}].
     *
     */
    public static final String COLUMN_IDENT_PARAM = "adql.column.ident" ;

    }
