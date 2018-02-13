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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery.ResultState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidStateTransitionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.blue.CallbackParam;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link BlueQuery} entities.
 * <br/>Controller path : [{@value BlueQueryModel.SERVICE_PATH}]
 *
 */
@Slf4j
@Controller
@RequestMapping(BlueQueryModel.SERVICE_PATH)
public class BlueQueryController
    extends BlueQueryModel
    {

    @Override
    public Path path()
        {
        return path(
            BlueQueryModel.SERVICE_PATH
            );
        }

    /**
     * {@link RequestMethod#GET} request to select all the {@link BlueQuery}s.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#SELECT_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws ProtectionException 
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MIME)
    public Iterable<BlueQueryBean> select()
    throws ProtectionException
        {
        log.debug("select()");
        return bean(
            services().entities().select()
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a {@link BlueQuery}.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#ENTITY_PATH}]
     * <br/>Content type : [{@value #JSON_MIME}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value BlueQueryModel.IDENT_FIELD}].
     * 
     * @param prev The {@link TaskState} to wait from.
     * @param next The next {@link TaskState} to wait for.
     * @param wait The wait timeout, zero for no wait, null for maximum wait.
     * 
     * @return The target {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws ProtectionException 
     * @throws IdentifierFormatException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=Entity.LinkFactory.IDENT_TOKEN, method=RequestMethod.GET, produces=JSON_MIME)
    public BlueQueryBean select(
        @PathVariable(value=Entity.LinkFactory.IDENT_FIELD)
        final String ident,

        @RequestParam(value=STATUS_PREV_PARAM, required=false)
        final TaskState prev,
        @RequestParam(value=STATUS_NEXT_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=STATUS_WAIT_PARAM, required=false)
        final Long wait
        )
    throws IdentifierNotFoundException, IdentifierFormatException, ProtectionException
        {
        log.debug("select(String, TaskStatus, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  prev  [{}]", next);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);
        return bean(
            services().entities().select(
                services().idents().ident(
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

     * @param prev The {@link TaskState} to wait from.
     * @param next The next {@link TaskState} to wait for.
     * @param wait The wait timeout, zero for no wait, null for maximum wait.

     * @return The updated {@link BlueQuery} wrapped in a {@link BlueQueryBean}.
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=Entity.LinkFactory.IDENT_TOKEN, method=RequestMethod.POST, produces=JSON_MIME)
    public BlueQueryBean update(
        @PathVariable(value=Entity.LinkFactory.IDENT_FIELD)
        final String ident,

        @RequestParam(value=QUERY_INPUT_PARAM, required=false)
        final String input,

        @RequestParam(value=STATUS_PREV_PARAM, required=false)
        final TaskState prev,
        @RequestParam(value=STATUS_NEXT_PARAM, required=false)
        final TaskState next,
        @RequestParam(value=STATUS_WAIT_PARAM, required=false)
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

        )
    throws IdentifierNotFoundException, IdentifierFormatException, InvalidRequestException, InternalServerErrorException, ProtectionException
        {
        log.debug("update(String, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  prev  [{}]", prev);
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);
        return bean(
            services().entities().update(
                services().idents().ident(
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
     * {@link RequestMethod#POST} request to POST a {@link BlueQuery.CallbackEvent} message.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#CALLBACK_PATH}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value}].
     * @param state The next {@link BlueTask} {@link TaskState} status, [{@value}].
     * @param resultcount The {@link BlueQuery.CallbackEvent} result count, [{@value}].
     * @param resultstate The {@link BlueQuery.CallbackEvent} result status, [{@value}].
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws InvalidStateTransitionException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=BlueQueryModel.CALLBACK_PATH, method=RequestMethod.POST, consumes=FORM_MIME, produces=JSON_MIME)
    public BlueQueryBean formpost(
        @PathVariable(value=Entity.LinkFactory.IDENT_FIELD)
        final String ident,
        @RequestParam(value=CALLBACK_TASK_STATE, required=false)
        final TaskState state,
        @RequestParam(value=CALLBACK_MESSAGE, required=false)
        final String message,
        @RequestParam(value=CALLBACK_RESULT_COUNT, required=false)
        final Long resultcount,
        @RequestParam(value=CALLBACK_RESULT_STATE, required=false)
        final BlueQuery.ResultState resultstate
        )
    throws IdentifierNotFoundException, IdentifierFormatException, InvalidRequestException, InternalServerErrorException, ProtectionException
        {
        log.debug("callback(String, TaskState, Long, ResultState)");
        log.debug("  ident [{}]", ident);
        log.debug("  state [{}]", state);
        log.debug("  count [{}]", resultcount);
        log.debug("  state [{}]", resultstate);
        return bean(
            services().entities().callback(
                services().idents().ident(
                    ident
                    ),
                new BlueQuery.CallbackEvent()
                    {
                    @Override
                    public TaskState state()
                        {
                        return state;
                        }
                    @Override
                    public String message()
                        {
                        return message;
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

        private String message;
        @Override
        public String getMessage()
            {
            return this.message;
            }
        public void setMessage(final String value)
            {
            this.message = value;
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
     * {@link RequestMethod#POST} request to POST a {@link BlueQuery.CallbackEvent} message.
     * <br/>Request path : [{@value BlueQuery.LinkFactory#CALLBACK_PATH}]
     * @param ident The {@link BlueQuery} {@link Identifier} from the URL path, [{@value}].
     * @param bean A JSON {@link CallbackParam.RequestBean}
     * @throws IdentifierNotFoundException If the {@link BlueQuery} could not be found.
     * @throws InvalidStateTransitionException 
     * @throws InternalServerErrorException 
     * @throws IdentifierFormatException 
     * @throws ProtectionException 
     * 
     */
    @ResponseBody
    @RequestMapping(value=BlueQueryModel.CALLBACK_PATH, method=RequestMethod.POST, consumes=JSON_MIME, produces=JSON_MIME)
    public BlueQueryBean jsonCallback(
        @PathVariable(value=Entity.LinkFactory.IDENT_FIELD)
        final String ident,
        @RequestBody
        final RequestBean bean 
        )
    throws IdentifierNotFoundException, IdentifierFormatException, InvalidRequestException, InternalServerErrorException, ProtectionException
        {
        log.debug("callback(String, TaskStatus, Long)");
        log.debug("  ident [{}]", ident);
        log.debug("  next  [{}]", bean.getState());
        return bean(
//
// TODO return a smaller CallbackBean - without the history
//
                services().entities().callback(
                services().idents().ident(
                    ident
                    ),
                new BlueQuery.CallbackEvent()
                    {
                    @Override
                    public TaskState state()
                        {
                        return TaskState.parse(
                    		bean.getState()
                    		);
                        }
                    @Override
                    public String message()
                        {
                        return bean.getMessage();
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
