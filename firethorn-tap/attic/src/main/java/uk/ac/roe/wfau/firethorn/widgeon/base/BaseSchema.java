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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataSchema;

/**
 * Public interface for a schema.
 *
 */
public interface BaseSchema<CatalogType extends BaseCatalog<?>>
extends DataSchema<CatalogType>
    {
    /**
     * Factory interface for accessing schemas.
     *
     */
    public static interface Factory<CatalogType extends BaseCatalog<?>, SchemaType extends BaseSchema<CatalogType>>
    extends DataSchema.Factory<CatalogType, SchemaType>
        {
        /**
         * Access to our View factory.
         *
         */
        public AdqlSchema.Factory views();

        }

    /**
     * Public interface for accessing a schemas tables.
     *
     */
    public interface Tables<TableType extends BaseTable<?>>
    extends DataSchema.Tables<TableType>
        {
        }

    @Override
    public BaseSchema.Tables<?> tables();

    /**
     * Access to our parent resource.
     *
     */
    public BaseResource resource();

    /**
     * Access to our parent catalog.
     *
     */
    public CatalogType catalog();

    }