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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import adql.db.DBTable;
import adql.db.DBColumn;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView ;

/**
 * DataResourceView.Table based extension of the DBTable interface.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
public interface AdqlTable
extends DBTable
    {

    /**
     * Factory interface.
     *
     */
    public static interface Factory
        {
        /**
         * Create a new AdqlTable.
         *
         */
        public AdqlTable create(DataResourceView.Table meta);

        }

    /**
     * Access to our DataResourceView.Table metadata.
     *
     */
    public DataResourceView.Table meta();

    /**
     * Access to our columns as AdqlColumns.
     *
     */
    public interface Columns
        {

        public Iterator<AdqlColumn> select();

        }

    /**
     * Access to our columns as AdqlColumns rather than DBColumns.
     *
     */
    public Columns columns();

    /**
     * DataResourceView.Column based extension of the DBColumn interface.
     *
     */
    public interface AdqlColumn
    extends DBColumn
        {

        /**
         * Access to our DataResourceView.Column metadata.
         *
         */
        public DataResourceView.Column meta();

        }
    }

