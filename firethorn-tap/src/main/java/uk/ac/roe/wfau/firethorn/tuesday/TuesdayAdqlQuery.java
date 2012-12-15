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

import java.util.Set;

/**
 *
 *
 */
public interface TuesdayAdqlQuery
    {

    /**
     * The query status.
     * 
     */
    public enum Status
        {
        /**
         * The query is being edited.
         * 
         */
        EDITING(),

        /**
         * The query has an (ADQL) error.
         * 
         */
        ERROR(),

        /**
         * The query is running.
         * 
         */
        RUNNING(),

        /**
         * The query completed.
         * 
         */
        COMPLETED(),

        /**
         * The query failed to execute.
         * 
         */
        FAILED();

        }

    /**
     * OGSA-DAI query mode.
     * 
     */
    public enum Mode
        {
        /**
         * Direct query to a single resource.
         * 
         */
        DIRECT(),

        /**
         * Distributed query handled by the OGSA-DAI DQP.
         * 
         */
        DISTRIBUTED();

        }
    
    /**
     * The query mode.
     * 
     */
    public Mode mode();

    /**
     * The original input string.
     * 
     */
    public String input();

    /**
     * The processed ADQL query.
     * 
     */
    public String adql();

    /**
     * The processed SQL query we pass to OGSA-DAI.
     * 
     */
    public String ogsa();

    /**
     * A list of the AdqlColumns used by the query.
     * 
     */
    public Iterable<TuesdayAdqlColumn> columns();

    /**
     * A list of the AdqlTables used by the query.
     * 
     */
    public Iterable<TuesdayAdqlTable> tables();

    /**
     * A list of the OGSA-DAI resources used by the query.
     * 
     */
    public Iterable<TuesdayOgsaResource<?>> resources();

    }
