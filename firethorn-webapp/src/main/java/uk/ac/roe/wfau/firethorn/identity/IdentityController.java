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
package uk.ac.roe.wfau.firethorn.identity;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcResource</code>.
 * TODO better exception handling.
 * http://blog.newsplore.com/2010/08/04/restful-error-handling-with-tomcat-springmvc
 * http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/util/AntPathMatcher.html
 *
 */
@Slf4j
@Controller
@RequestMapping(IdentityLinkFactory.SERVICE_PATH)
public class IdentityController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            IdentityLinkFactory.IDENTITY_PATH
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
     * Bean wrapper.
     *
     */
    public static class IdentityBean
    extends NamedEntityBeanImpl<Identity>
    implements NamedEntityBean<Identity>
        {
        public static class Iter
        extends AbstractEntityBeanIter<Identity>
            {
            public Iter(final Iterable<Identity> iterable)
                {
                super(
                    iterable
                    );
                }
            @Override
            public NamedEntityBean<Identity> bean(final Identity entity)
                {
                return new IdentityBean(
                    entity
                    );
                }
            }

        public IdentityBean(final Identity entity)
            {
            super(
                IdentityIdentFactory.TYPE_URI,
                entity
                );
            }
        }
    
    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public IdentityBean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException  {
        log.debug("JSON GET request");
        try {
            return new IdentityBean(
                factories().identities().select(
                    factories().identities().idents().ident(
                        ident
                        )
                    )
                );
            }
        catch (final Exception ouch)
            {
            log.error("Ouch ...", ouch);
            return null ;
            }
        }
    }
