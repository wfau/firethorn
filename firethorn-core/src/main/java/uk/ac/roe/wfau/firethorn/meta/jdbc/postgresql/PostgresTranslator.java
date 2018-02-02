/*
 *  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql;

import adql.translator.PostgreSQLTranslator;
import uk.ac.roe.wfau.firethorn.adql.parser.BaseTranslator;

/**
 * ADQL translator for Postgres.
 * 
 */
public class PostgresTranslator
extends PostgreSQLTranslator
implements BaseTranslator
    {

    /**
     * Public constructor.
     * 
     */
    public PostgresTranslator()
        {
        super();
        }

    /**
     * Public constructor.
     * 
     */
    public PostgresTranslator(boolean allCaseSensitive)
        {
        super(allCaseSensitive);
        }

    /**
     * Public constructor.
     * 
     */
    public PostgresTranslator(boolean catalog, boolean schema, boolean table, boolean column)
        {
        super(catalog, schema, table, column);
        }
    }
