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
package uk.ac.roe.wfau.firethorn.adql.parser;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuerySyntax;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 * Internal interface used by the AdqlParser during processing. 
 *
 */
public interface AdqlParserQuery
    {

    /**
     * Get the input text.
     *
     */
    public String input();

    /**
     * Reset the query state.
     *
     */
    public void reset(AdqlQuery.Mode mode);

    /**
     * Set the processed ADQL query.
     *
     */
    public void adql(final String adql);

    /**
     * Set the processed SQL query.
     *
     */
    public void osql(final String ogsa);

    /**
     * Add an AdqlColumn.
     *
     */
    public void add(final AdqlColumn column);

    /**
     * Add an AdqlTable.
     *
     */
    public void add(final AdqlTable table);

    /**
     * Set the ADQL syntax status.
     * 
     */
    public void syntax(AdqlQuerySyntax.Status status);

    /**
     * Set the ADQL syntax status.
     * 
     */
    public void syntax(AdqlQuerySyntax.Status status, String message);

    }
