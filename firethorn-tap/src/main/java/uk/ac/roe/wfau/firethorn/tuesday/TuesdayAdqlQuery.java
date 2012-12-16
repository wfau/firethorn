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
public interface TuesdayAdqlQuery
    {

    /**
     * Factory interface.
     * 
     */
    public static interface Factory
        {
        /**
         * Create a new query.
         * 
         */
        public TuesdayAdqlQuery create(final TuesdayAdqlResource workspace, final String query);

        /**
         * Create a new query.
         * 
         */
        public TuesdayAdqlQuery create(final TuesdayAdqlResource workspace, final String name, final String query);

        }
    
    /**
     * Query status indicator.
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
     * The ADQL workspace this query applies to. 
     * 
     */
    public TuesdayAdqlResource workspace();

    /**
     * The query mode.
     * 
     */
    public Mode mode();

    /**
     * The query status.
     * 
     */
    public Status status();

    /**
     * The input query.
     * 
     */
    public String input();

    /**
     * The input query.
     * 
     */
    public void input(String input);

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
     * The set of the AdqlColumns used by the query.
     * 
     */
    public Iterable<TuesdayAdqlColumn> columns();

    /**
     * The set of the AdqlTables used by the query.
     * 
     */
    public Iterable<TuesdayAdqlTable> tables();

    /**
     * The set of the OGSA-DAI resources used by the query.
     * 
     */
    public Iterable<TuesdayOgsaResource<?>> resources();

    /**
     * Parse the query and update our properties.
     * 
     */
    public void parse();

    }
