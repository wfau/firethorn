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
package uk.ac.roe.wfau.firethorn.webapp.blue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Spring MVC controller base class for {@link BlueQuery} entities.
 * @todo Separate this to extend AbstractEntityModel<> rather than AbstractEntityController<>.
 * @todo Apply this separation to all of our controllers.  
 *
 */
@Slf4j
public abstract class BlueQueryModel
    extends AbstractEntityController<BlueQuery, BlueQueryBean>
    {
    /**
     * The token to match an identifier field.
     * 
    public static final String IDENT_FIELD = Entity.LinkFactory.IDENT_FIELD ;
     */

    /**
     * The token to match an identifier field.
     * 
    public static final String IDENT_TOKEN = Entity.LinkFactory.IDENT_TOKEN ;
     */
    
    /**
     * The URI path for a {@link BlueQuery} service, [{@value}].
     *
     */
    public static final String SERVICE_PATH = "/blue/query" ;

    /**
     * The URI path for a {@link BlueQuery} entity, [{@value}].
     *
     */
    public static final String ENTITY_PATH = SERVICE_PATH + "/" + Entity.LinkFactory.IDENT_TOKEN ;

    /**
     * The URI path for a {@link BlueQuery} callback, [{@value}].
     * @todo move this to the ogsadai activity common package.
     *
     */
    public static final String CALLBACK_PATH = "/callback/" + Entity.LinkFactory.IDENT_TOKEN ;
    
    /**
     * Request param name for the {@link BlueQuery} name, [{@value}].
     *
     */
    public static final String QUERY_NAME_PARAM = "adql.query.name" ;

    /**
     * Request param name for the {@link BlueQuery} input, [{@value}].
     *
     */
    public static final String QUERY_INPUT_PARAM = "adql.query.input" ;

    /**
     * Request param name for the previous {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String STATUS_PREV_PARAM = "adql.query.status.prev" ;

    /**
     * Request param name for the next {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String STATUS_NEXT_PARAM = "adql.query.status.next" ;

    /**
     * How long to wait for a {@link BlueTask.TaskState} change, [{@value}].
     *
     */
    public static final String STATUS_WAIT_PARAM = "adql.query.wait.time" ;

    /**
     * Request param name for the row limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_ROWS = "adql.query.limit.rows" ;

    /**
     * Request param name for the total cell limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_CELLS = "adql.query.limit.cells" ;

    /**
     * Request param name for the execution time limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_TIME = "adql.query.limit.time" ;

    /**
     * Request param name for the first row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_FIRST = "adql.query.delay.first" ;

    /**
     * Request param name for the every row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_EVERY = "adql.query.delay.every" ;

    /**
     * Request param name for the last row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_LAST = "adql.query.delay.last" ;

    /**
     * Request param name for the {@link BlueQuery.CallbackEvent} result state, [{@value}].
     *
     */
    public static final String QUERY_MODE = "adql.query.mode" ;

    /**
     * Request param name for the {@link AdqlQueryBase.Syntax.Level} syntax, [{@value}].
     *
     */
    public static final String QUERY_SYNTAX = "adql.query.syntax" ;
    
    /**
     * Request param name for the {@link BlueQuery.CallbackEvent} {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String CALLBACK_TASK_STATE = "adql.query.wait.status" ;

    /**
     * Request param name for the {@link BlueQuery.CallbackEvent} row count, [{@value}].
     *
     */
    public static final String CALLBACK_RESULT_COUNT = "adql.query.results.count" ;

    /**
     * Request param name for the {@link BlueQuery.CallbackEvent} results status, [{@value}].
     *
     */
    public static final String CALLBACK_RESULT_STATE = "adql.query.results.state" ;
        
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
        public String link(final BlueQuery query)
            {
            return link(
                ENTITY_PATH,
                query
                );
            }

        @Override
        public String callback(final BlueQuery query)
            {
            return link(
                CALLBACK_PATH,
                query
                );
            }

        @Autowired
        private BlueQuery.EntityServices services ;

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

    @Autowired
    private BlueQuery.EntityServices services;
    protected BlueQuery.EntityServices services()
        {
        return this.services;
        }
    
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
    }
