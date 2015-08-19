/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.blue;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link BlueQuery} entities.
 * <br/>Controller path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(BlueQuery.LinkFactory.SERVICE_PATH)
public class BlueQueryController
    extends AbstractEntityController<BlueQuery, BlueQueryBean>
    {
    /**
     * Request param name for the {@link BlueQuery} input, [{@value}].
     *
     */
    public static final String INPUT_PARAM_NAME = "blue.query.input" ;

    /**
     * Request param name for the {@link BlueTask} status, [{@value}].
     *
     */
    public static final String STATUS_PARAM_NAME = "blue.query.status" ;

    /**
     * Request param name for the wait, [{@value}].
     *
     */
    public static final String WAIT_PARAM_NAME = "blue.query.wait" ;

    /**
     * Our{@link BlueQuery.IdentFactory} implementation.
     *
     */
    @Component
    public static class IdentFactory
    extends WebappIdentFactory<BlueQuery>
    implements BlueQuery.IdentFactory
        {
        }

    /**
     * Our{@link BlueQuery.NameFactory} implementation.
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<BlueQuery>
    implements BlueQuery.NameFactory
        {
        @Override
        public String name()
            {
            return datename();
            }
        }

    /**
     * Our{@link BlueQuery.LinkFactory} implementation.
     *
     */
    @Component
    public static class LinkFactory
    extends WebappLinkFactory<BlueQuery>
    implements BlueQuery.LinkFactory
        {
        protected LinkFactory()
            {
            super(
                SERVICE_PATH
                );
            }

        @Override
        public String link(final BlueQuery entity)
            {
            return link(
                ENTITY_PATH,
                entity
                );
            }

        @Autowired
        private BlueQuery.Services services ;
        @Override
        public BlueQuery resolve(final String link)
            throws IdentifierFormatException, IdentifierNotFoundException,
            EntityNotFoundException
            {
            if (this.matches(link))
                {
                return services.entities().select(
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

    @Override
    public Path path()
        {
        return path(
            BlueQuery.LinkFactory.SERVICE_PATH
            );
        }

    @Autowired
    private BlueQuery.Services services;
    
    @Override
    public BlueQueryBean bean(BlueQuery entity)
        {
        return new BlueQueryBean(
            entity
            );
        }

    @Override
    public Iterable<BlueQueryBean> bean(Iterable<BlueQuery> iter)
        {
        return new BlueQueryBean.Iter(
            iter
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=WebappLinkFactory.IDENT_TOKEN, method=RequestMethod.GET, produces=JSON_MIME)
    public BlueQueryBean select(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {
        log.debug("select(String) [{}]", ident);
        return bean(
            services.entities().select(
                services.idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<BlueQueryBean> select()
        {
        log.debug("select()");
        return bean(
            services.entities().select()
            );
        }
    
    /**
     * {@link RequestMethod#POST} request to advance the {@TaskState} of a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @param state The {@link BlueTask} {@link TaskState} status, [{@value }].
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=WebappLinkFactory.IDENT_TOKEN, params={STATUS_PARAM_NAME}, method=RequestMethod.POST, produces=JSON_MIME)
    public BlueQueryBean advance(
        @PathVariable("ident")
        final String ident,
        @RequestParam(value=STATUS_PARAM_NAME, required=true)
        final TaskState status
        ) throws IdentifierNotFoundException {
        log.debug("update(String, StatusOne) [{}]", ident, status.name());

        return bean(
            services.entities().advance(
                services.idents().ident(
                    ident
                    ),
                status,
                30000L
                )
            );
        }
    }
