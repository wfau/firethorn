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
package uk.ac.roe.wfau.firethorn.webapp.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link Community} entities.
 * <br/>Controller path : [{@value CommunityLinkFactory#ENTITY_PATH}]
 *
 */
@Controller
@RequestMapping(CommunityLinkFactory.ENTITY_PATH)
public class CommunityController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            CommunityLinkFactory.ENTITY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public CommunityController()
        {
        super();
        }

    /**
     * An {@link EntityBean} wrapper for a {@link Community}.
     *
     */
    public static class CommunityBean
    extends NamedEntityBeanImpl<Community>
    implements NamedEntityBean<Community>
        {
        /**
         * An {@link EntityBean.Iter} wrapper for a {@link Community} {@link Iterable}.
         *
         */
        public static class Iter
        extends AbstractEntityBeanIter<Community, CommunityBean>
            {
            /**
             * Public constructor.
             * @param iterable The {@link Community} {@link Iterable} to wrap.
             *
             */
            public Iter(final Iterable<Community> iterable)
                {
                super(
                    iterable
                    );
                }
            @Override
            public CommunityBean bean(final Community entity)
                {
                return new CommunityBean(
                    entity
                    );
                }
            }

        /**
         * Public constructor.
         * @param entity The {@link Community} to wrap.
         *
         */
        public CommunityBean(final Community entity)
            {
            super(
                CommunityIdentFactory.TYPE_URI,
                entity
                );
            }

        /**
         * Get a URL for the {@link Community} space.
         * @return A URL for the {@link Community} space, or null if it does not have space allocated.
         * @see Community#space()
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
        }

    /**
     * HTTP GET request for a {@link Community}.
     * <br/>Request path : [{@value CommunityLinkFactory#ENTITY_PATH}]
     * @param ident The {@link Community} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return A {@link CommunityBean} wrapping the {@link Community}.
     * @throws EntityNotFoundException If the {@link Community} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public CommunityBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException  {
        return new CommunityBean(
            factories().communities().entities().select(
                factories().communities().idents().ident(
                    ident
                    )
                )
            );
        }
    }
