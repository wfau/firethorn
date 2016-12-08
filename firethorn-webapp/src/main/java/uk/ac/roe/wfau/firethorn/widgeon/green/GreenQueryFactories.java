/*
 *  Copyright (C) 2016 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.widgeon.green;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseNameFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * The {@link GreenQuery} ident, name and link factories. 
 * 
 */
@Deprecated
public class GreenQueryFactories
    {
    /**
     * An {@link GreenQuery.NameFactory} implementation.
     *
     *
     */
    @Component
    @Deprecated
    public static class NameFactory
    extends BaseNameFactory<GreenQuery>
    implements GreenQuery.NameFactory
        {
        @Override
        public String name(final String name)
            {
            if (name != null)
                {
                return super.name(
                    name
                    );
                }
            else {
                return name();
                }
            }
        @Override
        public String name()
            {
            return datename(
                "GREEN-QUERY"
                );
            }
        }
    
    /**
     * An {@link GreenQuery.IdentFactory} implementation.
     * TODO Use PURLs.
     *
     */
    @Component
    @Deprecated
    public static class IdentFactory
    extends WebappIdentFactory<GreenQuery>
    implements GreenQuery.IdentFactory
        {
        /**
         * The type URI for this type.
         * TODO Move this to entity type interface.
         * TODO Use PURLs.
         *
         */
        public static final URI TYPE_URI = URI.create(
            "http://data.metagrid.co.uk/wfau/firethorn/types/entity/green-query-1.0.json"
            );
        }

    /**
     * A {@link GreenQuery.LinkFactory} implementation.
     *
     */
    @Component
    @Deprecated
    public static class LinkFactory
    extends WebappLinkFactory<GreenQuery>
    implements GreenQuery.LinkFactory
        {
        /**
         * Protected constructor.
         *
         */
        protected LinkFactory()
            {
            super(
                SERVICE_PATH
                );
            }

        /**
         * The URI path for the service, [{@value}].
         *
         */
        protected static final String SERVICE_PATH = "/green/query" ;

        /**
         * The URI path for an {@link GreenQuery}, [{@value}].
         *
         */
        public static final String ENTITY_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

        /**
         * The URI path for the VOTable representation, [{@value}].
         *
         */
        @Deprecated
        public static final String VOTABLE_NAME = "/votable";
        
        /**
         * The URI path for the VOTable representation, [{@value}].
         *
         */
        @Deprecated
        public static final String VOTABLE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN + VOTABLE_NAME;

        /**
         * The URI path for the DataTable representation, [{@value}].
         *
         */
        @Deprecated
        public static final String DATATABLE_NAME = "/datatable";
        
        /**
         * The URI path for the DataTable representation, [{@value}].
         *
         */
        @Deprecated
        public static final String DATATABLE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN + DATATABLE_NAME;

        @Override
        public String link(final GreenQuery entity)
            {
            return link(
                ENTITY_PATH,
                entity
                );
            }

        @Autowired
        private GreenQuery.EntityFactory factory ;
        @Override
        public GreenQuery resolve(String link)
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
    }