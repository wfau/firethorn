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

import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>JdbcTable</code>.
 *
 */
@Component
public class JdbcTableLinkFactory
extends WebappLinkFactory<JdbcTable>
implements JdbcTable.LinkFactory
    {
    protected JdbcTableLinkFactory()
        {
        super(
            BASE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    public static final String BASE_PATH = "/jdbc/table";

    /**
     * The URI path for individual tables.
     *
     */
    public static final String ENTITY_PATH = BASE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for table columns.
     *
     */
    public static final String COLUMN_PATH = ENTITY_PATH + "/columns" ;

    /**
     * The URI path for the VOTable representation.
     *
     */
    public static final String VOTABLE_NAME = "/votable";

    /**
     * The URI path for the VOTable representation.
     *
     */
    public static final String VOTABLE_PATH = ENTITY_PATH + VOTABLE_NAME;

    /**
     * The URI path for the DataTable representation.
     *
     */
    public static final String DATATABLE_NAME = "/datatable";

    /**
     * The URI path for the DataTable representation.
     *
     */
    public static final String DATATABLE_PATH = ENTITY_PATH + DATATABLE_NAME;
    
    @Override
    public String link(final JdbcTable entity)
        {
        return this.link(
            ENTITY_PATH,
            entity
            );
        }

    @Autowired
    private JdbcTable.EntityFactory factory ;
    @Override
    public JdbcTable resolve(String link)
    throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException
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
