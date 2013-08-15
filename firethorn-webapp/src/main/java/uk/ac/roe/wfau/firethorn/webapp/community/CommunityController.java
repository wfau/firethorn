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

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Community;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>Community</code>.
 *
 */
@Slf4j
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
     * Bean wrapper.
     *
     */
    public static class CommunityBean
    extends NamedEntityBeanImpl<Community>
    implements NamedEntityBean<Community>
        {
        public static class Iter
        extends AbstractEntityBeanIter<Community, CommunityBean>
            {
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

        public CommunityBean(final Community entity)
            {
            super(
                CommunityIdentFactory.TYPE_URI,
                entity
                );
            }

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
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_CONTENT)
    public CommunityBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException  {
        log.debug("JSON GET request");
        return new CommunityBean(
            factories().communities().select(
                factories().communities().idents().ident(
                    ident
                    )
                )
            );
        }
    }
