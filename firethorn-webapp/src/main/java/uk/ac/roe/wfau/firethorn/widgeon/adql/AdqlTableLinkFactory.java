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

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * An {@link AdqlTable.LinkFactory} implementation.
 *
 */
@Component
public class AdqlTableLinkFactory
extends WebappLinkFactory<AdqlTable>
implements AdqlTable.LinkFactory
    {
    /**
     * Protected constructor.
     *
     */
    protected AdqlTableLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service, [{@value}].
     *
     */
    public static final String SERVICE_PATH = "/adql/table";

    /**
     * The URI path for an {@link AdqlTable}, [{@value}].
     *
     */
    public static final String TABLE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for the {@link AdqlTable} columns, [{@value}].
     *
     */
    public static final String TABLE_COLUMN_PATH = TABLE_PATH + "/columns" ;

    /**
     * The URI path for the {@link AdqlTable} VOTable representation, [{@value}].
     *
     */
    public static final String VOTABLE_NAME = "/votable";

    /**
     * The URI path for the {@link AdqlTable} VOTable representation, [{@value}].
     *
     */
    public static final String VOTABLE_PATH = TABLE_PATH + "/" + VOTABLE_NAME;

    /**
     * The URI path for the {@link AdqlTable} DataTable representation, [{@value}].
     *
     */
    public static final String DATATABLE_NAME = "/datatable";

    /**
     * The URI path for the {@link AdqlTable} DataTable representation, [{@value}].
     *
     */
    public static final String DATATABLE_PATH = TABLE_PATH + "/" + DATATABLE_NAME;
    
    @Override
    public String link(final AdqlTable entity)
        {
        return this.link(
            TABLE_PATH,
            entity
            );
        }
    }
