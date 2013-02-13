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
package uk.ac.roe.wfau.firethorn.adql ;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.tuesday.AdqlColumn;
import uk.ac.roe.wfau.firethorn.tuesday.AdqlQuery;
import uk.ac.roe.wfau.firethorn.tuesday.AdqlTable;
import adql.db.DBColumn;
import adql.db.DBTable;


/**
 * Local extension of the CDS DBTable interface.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
public interface AdqlDBTable
extends DBTable
    {

    /**
     * Factory interface.
     *
     */
    public static interface Factory
        {
        /**
         * Create a new AdqlDBTable.
         *
         */
        public AdqlDBTable create(final AdqlQuery.Mode mode, final AdqlTable table);

        }

    /**
     * The query mode.
     *
     */
    public AdqlQuery.Mode mode();

    /**
     * Our underlying AdqlTable.
     *
     */
    public AdqlTable table();

    /**
     * Access to our columns as AdqlColumns.
     *
     */
    public interface Columns
        {

        public Iterator<AdqlDBColumn> select();

        }

    /**
     * Access to our columns as AdqlDBColumns.
     *
     */
    public Columns columns();

    /**
     * Local extension of the CDS DBColumn interface.
     *
     */
    public interface AdqlDBColumn
    extends DBColumn
        {

        /**
         * Access to our AdqlColumn metadata.
         *
         */
        public AdqlColumn column();

        }
    }

