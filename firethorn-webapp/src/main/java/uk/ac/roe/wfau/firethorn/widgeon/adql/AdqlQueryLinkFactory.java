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
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>AdqlQuery</code>.
 *
 */
@Component
public class AdqlQueryLinkFactory
extends WebappLinkFactory<AdqlQuery>
implements AdqlQuery.LinkFactory
    {
    protected AdqlQueryLinkFactory()
        {
        super(
            BASE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    protected static final String BASE_PATH = "/adql/query" ;

    /**
     * The URI path for individual queries.
     *
     */
    public static final String QUERY_PATH = BASE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for the VOTable representation.
     *
     */
    public static final String VOTABLE_NAME = "/votable";

    /**
     * The URI path for the VOTable representation.
     *
     */
    public static final String VOTABLE_PATH = BASE_PATH + "/" + IDENT_TOKEN + VOTABLE_NAME;

    @Override
    public String link(final AdqlQuery entity)
        {
        return link(
            QUERY_PATH,
            entity
            );
        }
    }
