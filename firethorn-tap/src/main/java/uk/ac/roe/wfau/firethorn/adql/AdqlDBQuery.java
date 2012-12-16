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
package uk.ac.roe.wfau.firethorn.adql;

import java.util.Set;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlTable;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayOgsaResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery;

/**
 *
 *
 */
public interface AdqlDBQuery
    {

    /**
     * The initial AQDL input.
     * 
     */
    public String input();

    /**
     * The query mode.
     * 
     */
    public TuesdayAdqlQuery.Mode mode();

    /**
     * Set the query mode.
     * 
     */
    public void mode(TuesdayAdqlQuery.Mode mode);

    /**
     * The query status.
     * 
     */
    public TuesdayAdqlQuery.Status status();

    /**
     * Set the query status.
     * 
     */
    public void status(TuesdayAdqlQuery.Status status);

    /**
     * Set the processed ADQL query.
     * 
     */
    public void adql(String adql);

    /**
     * Set the processed SQL query we pass to OGSA-DAI.
     * 
     */
    public void ogsa(String ogsa);

    /**
     * Add an AdqlColumn.
     * 
     */
    public void add(TuesdayAdqlColumn column);

    /**
     * Add an AdqlTable.
     * 
     */
    public void add(TuesdayAdqlTable table);

    }
