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
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * An {@link AdqlSchema.LinkFactory} implementation.
 *
 */
@Component
public class AdqlSchemaLinkFactory
extends WebappLinkFactory<AdqlSchema>
implements AdqlSchema.LinkFactory
    {
    /**
     * Protected constructor.
     *
     */
    protected AdqlSchemaLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service, [{@value}].
     *
     */
    public static final String SERVICE_PATH = "/adql/schema";

    /**
     * The URI path for an {@link AdqlSchema}, [{@value}].
     *
     */
    public static final String SCHEMA_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for the {@link AdqlSchema} tables, [{@value}].
     *
     */
    public static final String SCHEMA_TABLE_PATH = SCHEMA_PATH + "/tables" ;

    /**
     * The URI path for the {@link AdqlSchema} {@link BlueQuery} queries, [{@value}].
     *
     */
    @Deprecated
    public static final String SCHEMA_BLUE_PATH = SCHEMA_PATH + "/queries" ;

    @Override
    public String link(final AdqlSchema entity)
        {
        return link(
            SCHEMA_PATH,
            entity
            );
        }

    @Autowired
    private AdqlSchema.EntityFactory factory ;
    @Override
    public AdqlSchema resolve(String link)
    throws IdentifierFormatException, IdentifierNotFoundException,EntityNotFoundException, ProtectionException
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
