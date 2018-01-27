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
package uk.ac.roe.wfau.firethorn.widgeon.name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>IvoaTable</code>.
 * TODO
 *
 */
@Component
public class IvoaTableLinkFactory
extends WebappLinkFactory<IvoaTable>
implements IvoaTable.LinkFactory
    {
    protected IvoaTableLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the tables service.
     * TODO Move these to a model class.
     *
     */
    public static final String SERVICE_PATH = "/ivoa/table";

    /**
     * The URI path for individual tables.
     * TODO Move these to a model class.
     *
     */
    public static final String TABLE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for table columns.
     * TODO Move these to a model class.
     *
     */
    public static final String TABLE_COLUMN_PATH = TABLE_PATH + "/columns" ;

    /**
     * The URI path for the VOTable representation.
     * TODO Move these to a model class.
     *
     */
    public static final String VOTABLE_NAME = "/votable";

    /**
     * The URI path for the VOTable representation.
     * TODO Move these to a model class.
     *
     */
    public static final String VOTABLE_PATH = TABLE_PATH + "/" + VOTABLE_NAME;

    /**
     * The URI path for the DataTable representation.
     * TODO Move these to a model class.
     *
     */
    public static final String DATATABLE_NAME = "/datatable";

    /**
     * The URI path for the DataTable representation.
     * TODO Move these to a model class.
     *
     */
    public static final String DATATABLE_PATH = TABLE_PATH + "/" + DATATABLE_NAME;

    @Override
    public String link(final IvoaTable entity)
        {
        return this.link(
            TABLE_PATH,
            entity
            );
        }

    @Autowired
    private IvoaTable.EntityFactory factory ;
    @Override
    public IvoaTable resolve(String link)
    throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException, ProtectionException
        {
        if (this.matches(link))
            {
            return factory.select(
                this.ident(
                    link
                    )
                );
            }
        else {
            throw new EntityNotFoundException(
                "Unable to resolve [" + link + "]"
                );
            }
        }
    }
