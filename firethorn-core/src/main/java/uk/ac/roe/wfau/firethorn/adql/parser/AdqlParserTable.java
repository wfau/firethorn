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
package uk.ac.roe.wfau.firethorn.adql.parser ;

import java.util.Iterator;

import adql.db.DBColumn;
import adql.db.DBTable;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;


/**
 * Local extension of the CDS DBTable interface.
 * See http://cdsportal.u-strasbg.fr/adqltuto/gettingstarted.html
 *
 */
public interface AdqlParserTable
extends DBTable
    {

    /**
     * Factory interface.
     *
     */
    public static interface Factory
        {
        /**
         * Create a new AdqlParserTable.
         * @throws ProtectionException 
         *
         */
        public AdqlParserTable create(final AdqlQueryBase.Mode mode, final AdqlTable table)
        throws ProtectionException;

        }

    /**
     * The query mode.
     *
     */
    public AdqlQueryBase.Mode mode();

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

        public Iterator<AdqlDBColumn> select() throws ProtectionException;

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

