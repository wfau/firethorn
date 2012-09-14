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

import adql.db.DBTable;
import adql.db.DBColumn;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;


/**
 * AdqlResource.AdqlTable based extension of the DBTable interface.
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
        public AdqlDBTable create(final AdqlResource.AdqlTable meta);

        }

    /**
     * Access to our AdqlResource.AdqlTable metadata.
     *
     */
    public AdqlResource.AdqlTable meta();

    /**
     * Access to our columns as AdqlColumns.
     *
     */
    public interface Columns
        {

        public Iterator<AdqlDBColumn> select();

        }

    /**
     * Access to our columns as AdqlColumns rather than DBColumns.
     *
     */
    public Columns columns();

    /**
     * AdqlResource.AdqlColumn based extension of the DBColumn interface.
     *
     */
    public interface AdqlDBColumn
    extends DBColumn
        {

        /**
         * Access to our AdqlResource.AdqlColumn metadata.
         *
         */
        public AdqlResource.AdqlColumn meta();

        }
    }

