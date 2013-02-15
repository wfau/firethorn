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
package uk.ac.roe.wfau.firethorn.adql.query;

/**
 * ADQL query syntax validation status.
 *
 *
 */
public interface AdqlQuerySyntax
    {
    public enum Status
        {
        /**
         * The query has been parsed and is valid ADQL.
         * 
         */
        VALID(),
        
        /**
         * A parser error in the ADQL query.
         * 
         */
        PARSE_ERROR(),

        /**
         * A translation error processing the query.
         * 
         */
        TRANS_ERROR(),

        /**
         * Unknown state - the query hasn't been parsed yet.
         * 
         */
        UNKNOWN();
        }

    /**
     * The syntax validation status.
     * 
     */
    public Status status();

    /**
     * The parser error message.
     * 
     */
    public String error();

    /**
     * A user friendly message.
     * 
     */
    public String friendly();
    
    }
