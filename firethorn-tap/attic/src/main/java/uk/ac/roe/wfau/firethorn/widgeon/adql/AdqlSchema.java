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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataSchema;

/**
 * Public interface for an ADQL schema.
 *
 */
public interface AdqlSchema
extends DataSchema<AdqlCatalog>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlSchema>
        {
        }

    /**
     * Factory interface for creating and selecting schemas.
     *
     */
    public static interface Factory
    extends DataSchema.Factory<AdqlCatalog, AdqlSchema>
        {

        /**
         * Find an existing ADQL schema, or create a new one.
         *
        public AdqlSchema cascade(final AdqlCatalog parent, final BaseSchema<?> base);
         */

        /**
         * Create a new ADQL schema.
         *
         */
        public AdqlSchema create(final AdqlCatalog parent, final String name);

        /**
         * Create a new ADQL schema.
         *
         */
        public AdqlSchema create(final AdqlCatalog parent, final BaseSchema<?> base, final String name);
        
        /**
         * Access to our table factory.
         *
         */
        public AdqlTable.Factory tables();

        }

    /**
     * Public interface for accessing a schemas tables.
     *
     */
    public interface Tables
    extends DataSchema.Tables<AdqlTable>
        {
        /**
         * Create a new ADQL table.
         * 
         */
        public AdqlTable create(final BaseTable<?> base);

        /**
         * Create a new ADQL table.
         * 
         */
        public AdqlTable create(final BaseTable<?> base, final String name);

        }

    /**
     * Access to this schemas tables.
     *
     */
    @Override
    public AdqlSchema.Tables tables();

    /**
     * Access to our parent resource.
     *
     */
    public AdqlResource resource();

    /**
     * Access to our parent catalog.
     *
     */
    public AdqlCatalog catalog();

    }