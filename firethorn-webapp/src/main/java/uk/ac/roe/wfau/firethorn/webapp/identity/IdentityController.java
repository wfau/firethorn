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
package uk.ac.roe.wfau.firethorn.webapp.identity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link Identity} entities.
 * <br/>Controller path : [{@value IdentityLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(IdentityLinkFactory.ENTITY_PATH)
public class IdentityController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            IdentityLinkFactory.ENTITY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public IdentityController()
        {
        super();
        }

    /**
     * MVC property for the {@link Identity}, [{@value}].
     *
     */
    public static final String TARGET_ENTITY = "urn:identity.entity" ;
    
    /**
     * An {@link EntityBean} wrapper for an {@link Identity}.
     *
     */
    public static class IdentityBean
    extends NamedEntityBeanImpl<Identity>
    implements NamedEntityBean<Identity>
        {
        /**
         * An {@link EntityBean.Iter} wrapper for an {@link Identity} {@link Iterable}.
         *
         */
        public static class Iter
        extends AbstractEntityBeanIter<Identity, IdentityBean>
            {
            /**
             * Public constructor.
             * @param iterable The {@link Identity} {@link Iterable} to wrap.
             *
             */
            public Iter(final Iterable<Identity> iterable)
                {
                super(
                    iterable
                    );
                }
            @Override
            public IdentityBean bean(final Identity entity)
                {
                return new IdentityBean(
                    entity
                    );
                }
            }

        /**
         * Public constructor.
         * @param identity The {@link Identity} to wrap.
         *
         */
        public IdentityBean(final Identity entity)
            {
            super(
                IdentityIdentFactory.TYPE_URI,
                entity
                );
            }

        /**
         * Get a URL for the {@Link Identity} {@link Community}
         * @return A URL for the {@link Community}.
         * @see Identity#community
         *
         */
        public String getCommunity()
            {
            if (entity().community() != null)
                {
                return entity().community().link();
                }
            else {
                return null;
                }
            }

        /**
         * Get a URL for the {@link Identity} space.
         * @return A URL for the {@link Identity} space, or null if it does not have space allocated.
         * @see Identity#space()
         *
         */
        public String getSpace()
            {
            if (entity().space() != null)
                {
                return entity().space().link();
                }
            else {
                return null ;
                }
            }

        /**
         * Get a URL for the queries owned by this {@Link Identity}
         * @return A URL for the list of {@link AdqlQuery}s.
         * @see Identity#queries
         *
         */
        public String getQueries()
        	{
        	return entity().link().concat("/queries/select");
        	}
        }

    /**
     * HTTP GET request for an {@link Identity}.
     * <br/>Request path : [{@value IdentityLinkFactory#ENTITY_PATH}]
     * @param ident The {@link Identity} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return An {@link IdentityBean} wrapping the {@link Identity}.
     * @throws EntityNotFoundException If the {@link Identity} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public IdentityBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException  {
        return new IdentityBean(
            factories().identities().select(
                factories().identities().idents().ident(
                    ident
                    )
                )
            );
        }
    }
