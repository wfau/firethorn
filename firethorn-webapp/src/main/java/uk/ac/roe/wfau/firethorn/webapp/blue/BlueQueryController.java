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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery.ResultState;
import uk.ac.roe.wfau.firethorn.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.blue.InvalidStateTransitionException;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.CallbackParam;
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
     * Request param name for the {@link BlueQuery} identifier, [{@value}].
     *
     */
    public static final String QUERY_IDENT_PARAM = BlueQuery.LinkFactory.IDENT_FIELD ;

    /**
     * Request param name for the {@link BlueQuery} input, [{@value}].
     *
     */
    public static final String QUERY_INPUT_PARAM = "blue.query.input" ;

    /**
     * Request param name for the previous {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String PREV_STATUS_PARAM = "blue.query.prev.status" ;

    /**
     * Request param name for the next {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String NEXT_STATUS_PARAM = "blue.query.next.status" ;
    
    /**
     * Request param name for the row limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_ROWS = "blue.query.limit.rows" ;

    /**
     * Request param name for the total cell limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_CELLS = "blue.query.limit.cells" ;

    /**
     * Request param name for the execution time limit, [{@value}].
     *
     */
    public static final String QUERY_LIMT_TIME = "blue.query.limit.time" ;

    /**
     * Request param name for the first row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_FIRST = "blue.query.delay.first" ;

    /**
     * Request param name for the every row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_EVERY = "blue.query.delay.every" ;

    /**
     * Request param name for the last row delay, [{@value}].
     *
     */
    public static final String QUERY_DELAY_LAST = "blue.query.delay.last" ;

    /**
     * Request param name for the request wait, [{@value}].
     *
     */
    public static final String REQUEST_WAIT_PARAM = "blue.query.wait" ;

    /**
     * Request param name for the {@link BlueQuery.Callback} {@link BlueTask.TaskState}, [{@value}].
     *
     */
    public static final String CALLBACK_TASK_STATE = "blue.query.status" ;

    /**
     * Request param name for the {@link BlueQuery.Callback} row count, [{@value}].
     *
     */
    public static final String CALLBACK_RESULT_COUNT = "blue.query.results.count" ;

    /**
     * Request param name for the {@link BlueQuery.Callback} result state, [{@value}].
     *
     */
    public static final String CALLBACK_RESULT_STATE = "blue.query.results.state" ;

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

    @Override
    public Path path()
        {
        return path(
            BlueQuery.LinkFactory.SERVICE_PATH
            );
        }

    @Autowired
    private BlueQuery.EntityServices services;
    
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
     * {@link RequestMethod#GET} request to select all the {@link BlueQuery}s.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#SELECT_PATH}]
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
     * {@link RequestMethod#GET} request to select a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
    @ResponseBody
    @RequestMapping(value=WebappLinkFactory.IDENT_TOKEN, method=RequestMethod.GET, produces=JSON_MIME)
    public BlueQueryBean select(
        @PathVariable(value=IDENT_PARAM_NAME)
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
     */

    /**
     * {@link RequestMethod#GET} request to select a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * 
     * 
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=WebappLinkFactory.IDENT_TOKEN, method=RequestMethod.GET, produces=JSON_MIME)
    public BlueQueryBean select(
        @PathVariable(value=QUERY_IDENT_PARAM)
        final String ident,
        @RequestParam(value=PREV_STATUS_PARAM, required=false)
        final TaskState prev,
        @RequestParam(value=NEXT_STATUS_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=REQUEST_WAIT_PARAM, required=false)
        final Long wait
        ) throws IdentifierNotFoundException {
        log.debug("select(String, TaskStatus, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  prev  [{}]", next);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);
        return bean(
            services.entities().select(
                services.idents().ident(
                    ident
                    ),
                prev,
                next,
                wait
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to update the {@TaskState} of a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].

     * @param prev The previous {@link TaskState} to wait from.
     * @param next The next {@link TaskState} to wait for.
     * @param wait The wait timeout, zero for no wait, null for maximum wait.

     * @return The updated {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=WebappLinkFactory.IDENT_TOKEN, method=RequestMethod.POST, produces=JSON_MIME)
    public BlueQueryBean update(
        @PathVariable(value=QUERY_IDENT_PARAM)
        final String ident,
        @RequestParam(value=QUERY_INPUT_PARAM, required=false)
        final String input,

        @RequestParam(value=PREV_STATUS_PARAM, required=false)
        final TaskState prev,
        @RequestParam(value=NEXT_STATUS_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=REQUEST_WAIT_PARAM, required=false)
        final Long wait,

        @RequestParam(value=QUERY_LIMT_ROWS, required=false)
        final Long rows,
        @RequestParam(value=QUERY_LIMT_CELLS, required=false)
        final Long cells,
        @RequestParam(value=QUERY_LIMT_TIME, required=false)
        final Long time,

        @RequestParam(value=QUERY_DELAY_FIRST, required=false)
        final Integer first,
        @RequestParam(value=QUERY_DELAY_EVERY, required=false)
        final Integer every,
        @RequestParam(value=QUERY_DELAY_LAST, required=false)
        final Integer last

        ) throws
            IdentifierNotFoundException,
            IdentifierFormatException,
            InvalidRequestException,
            InternalServerErrorException
        {
        log.debug("update(String, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  prev  [{}]", prev);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);
        return bean(
            services.entities().update(
                services.idents().ident(
                    ident
                    ),
                input,
                factories().blues().limits().create(
                    rows,
                    cells,
                    time
                    ),
                factories().blues().delays().create(
                    first,
                    every,
                    last
                    ),
                prev,
                next,
                wait
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to send a {@link BlueQuery.Callback} message.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#CALLBACK_PATH}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @param next The next {@link BlueTask} {@link TaskState} status, [{@value }].
     * @param count The {@link BlueQuery.Callback} rowcount, [{@value }].
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=BlueQuery.LinkFactory.CALLBACK_PATH, method=RequestMethod.POST, consumes=FORM_MIME, produces=JSON_MIME)
    public BlueQueryBean formCallback(
        @PathVariable(value=QUERY_IDENT_PARAM)
        final String ident,
        @RequestParam(value=CALLBACK_TASK_STATE, required=false)
        final TaskState next,
        @RequestParam(value=CALLBACK_RESULT_COUNT, required=false)
        final Long resultcount,
        @RequestParam(value=CALLBACK_RESULT_STATE, required=false)
        final BlueQuery.ResultState resultstate
        ) throws
            IdentifierNotFoundException,
            IdentifierFormatException,
            InvalidRequestException,
            InternalServerErrorException
        {
        log.debug("callback(String, TaskState, Long, ResultState)");
        log.debug("  ident [{}]", ident);
        log.debug("  next  [{}]", next);
        log.debug("  count [{}]", resultcount);
        log.debug("  state [{}]", resultstate);
        return bean(
            services.entities().callback(
                services.idents().ident(
                    ident
                    ),
                new BlueQuery.Callback()
                    {
                    @Override
                    public TaskState state()
                        {
                        return next;
                        }
                    @Override
                    public Results results()
                        {
                        return new Results()
                            {
                            @Override
                            public Long count()
                                {
                                return resultcount;
                                }
                            @Override
                            public ResultState state()
                                {
                                return resultstate;
                                }
                            };
                        }
                    }
                )
            );
        }

    /**
     * JSON callback bean.
     * Annotated to ignore unknown fields. 
     * http://codingexplained.com/coding/java/ignoring-unrecognized-json-fields-spring-jackson
     * 
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestBean
    implements CallbackParam.RequestBean
    	{
        protected RequestBean()
        	{
        	}

    	private String ident;
		@Override
        public String getIdent()
            {
            return this.ident;
            }
        public void setIdent(final String value)
            {
            this.ident = value;
            }

    	private String status;
		@Override
        public String getState()
            {
            return this.status;
            }
        public void setState(final String value)
            {
            this.status = value;
            }

        private Long resultcount;
		@Override
		public Long getResultCount()
			{
			return this.resultcount;
			}
        public void setResultCount(final Long value)
            {
            this.resultcount = value;
            }

        private String resultstate;
        @Override
        public String getResultState()
            {
            return this.resultstate;
            }
        public void setResultState(final String value)
            {
            this.resultstate = value;
            }
    	}
    
    /**
     * {@link RequestMethod#POST} request to send a {@link BlueQuery.Callback} message.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#CALLBACK_PATH}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * 
     */
    @ResponseBody
    @RequestMapping(value=BlueQuery.LinkFactory.CALLBACK_PATH, method=RequestMethod.POST, consumes=JSON_MIME, produces=JSON_MIME)
    public BlueQueryBean jsonCallback(
        @PathVariable(value=QUERY_IDENT_PARAM)
        final String ident,
        @RequestBody
        final RequestBean bean 
        ) throws
            IdentifierNotFoundException,
            IdentifierFormatException,
            InvalidRequestException,
            InternalServerErrorException
        {
        log.debug("callback(String, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  next  [{}]", bean.getState());
        return bean(
            services.entities().callback(
                services.idents().ident(
                    ident
                    ),
                new BlueQuery.Callback()
                    {
                    @Override
                    public TaskState state()
                        {
                        return TaskState.parse(
                    		bean.getState()
                    		);
                        }
                    @Override
                    public Results results()
                        {
                        return new Results()
                            {
                            @Override
                            public Long count()
                                {
                                return bean.getResultCount();
                                }
                            @Override
                            public ResultState state()
                                {
                                return ResultState.parse(
                                    bean.getResultState()
                                    );
                                }
                            };
                        }
                    }
                )
            );
        }
    }