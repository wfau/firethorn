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

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller to handle {@link AdqlQuery} entities.
 * <br/>Controller path : [{@value AdqlQueryLinkFactory#ENTITY_PATH}]
 * @todo Add an entity() load method.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.ENTITY_PATH)
public class AdqlQueryController
extends AbstractEntityController<AdqlQuery, AdqlQueryBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlQueryLinkFactory.ENTITY_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryController()
        {
        super();
        }

    /**
     * MVC property for the {@link AdqlQuery}, [{@value}].
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.query.entity" ;

    /**
     * MVC property for the {@link AdqlQuery} name, [{@value}].
     * @see AdqlQuery#name(String)
     *
     */
    public static final String UPDATE_NAME = "adql.query.update.name" ;

    /**
     * MVC property for the {@link AdqlQuery} input, [{@value}].
     * @see AdqlQuery#input(String)
     *
     */
    public static final String UPDATE_INPUT = "adql.query.update.query" ;

    /**
     * MVC property for the {@link Job.Status}, [{@value}].
     * @see AdqlQuery#status(Status)
     *
     */
    public static final String UPDATE_STATUS = "adql.query.update.status" ;

    /**
     * MVC property for the {@link HttpRequest} timeout, [{@value}].
     * The number of seconds to wait for a {@link Job.Status} change before returning a response.
     *
     */
    public static final String UPDATE_TIMEOUT = "adql.query.update.timeout" ;

    /**
     * MVC property for the delay before the first row, [{@value}].
     * @see AdqlQuery.Delays#first(Integer)
     *
     */
    public static final String UPDATE_DELAY_FIRST = "adql.query.update.delay.first" ;
    
    /**
     * MVC property for the delay between every row, [{@value}].
     * @see AdqlQuery.Delays#every(Integer)
     *
     */
    public static final String UPDATE_DELAY_EVERY = "adql.query.update.delay.every" ;

    /**
     * MVC property for the delay after the last row, [{@value}].
     * @see AdqlQuery.Delays#last(Integer)
     *
     */
    public static final String UPDATE_DELAY_LAST = "adql.query.update.delay.last" ;

    /**
     * MVC property for the row limit, [{@value}].
     * @see AdqlQuery.ModifiableLimits#rows(Long)
     *
     */
    public static final String UPDATE_LIMT_ROWS = "adql.query.update.limit.rows" ;

    /**
     * MVC property for the cell limit, [{@value}].
     * @see AdqlQuery.ModifiableLimits#cells(Long)
     *
     */
    public static final String UPDATE_LIMT_CELLS = "adql.query.update.limit.cells" ;

    /**
     * MVC property for the time limit, [{@value}].
     * @see AdqlQuery.ModifiableLimits#time(Long)
     *
     */
    public static final String UPDATE_LIMT_TIME = "adql.query.update.limit.time" ;
    
    @Override
    public AdqlQueryBean bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<AdqlQueryBean> bean(final Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * {@link RequestMethod#GET} request to select a specific {@link AdqlQuery}.
     * <br/>Request path : [{@value AdqlQueryLinkFactory#ENTITY_PATH}]
     * @param ident The {@link AdqlQuery} {@Identifier} from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * @return The selected {@link AdqlQuery} wrapped in a {@link AdqlQueryBean}.
     * @throws EntityNotFoundException If the {@link AdqlQuery} could not be found.
     * 
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public AdqlQueryBean select(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {
        return bean(
            factories().adql().queries().select(
                factories().adql().queries().idents().ident(
                    ident
                    )
                )
            );
        }

    /**
     * {@link RequestMethod#POST} request to update a specific {@link AdqlQuery}.
     * <br/>Request path : [{@value AdqlQueryLinkFactory#ENTITY_PATH}]
     * @param ident The {@link AdqlQuery} identifier from the URL path, [{@value WebappLinkFactory.IDENT_FIELD}].
     * <br/>Optional {@link AdqlQuery} params :
     * @param name  The {@link AdqlQuery} name, [{@value #UPDATE_NAME}].
     * @param input The {@link AdqlQuery} input, [{@value #UPDATE_INPUT}].
     * <br/>Optional {@link AdqlQuery.Delays} params :
     * @param first The {@link AdqlQuery.Delays} delay before the first row, [{@value #UPDATE_DELAY_FIRST}].
     * @param every The {@link AdqlQuery.Delays} delay between the every row, [{@value #UPDATE_DELAY_EVERY}].
     * @param last  The {@link AdqlQuery.Delays} delay after the last row, [{@value #UPDATE_DELAY_LAST}].
     * <br/>Optional {@link AdqlQuery.Limits} params :
     * @param limitrows  The {@link AdqlQuery.Limits} row limit, [{@value #UPDATE_LIMT_ROWS}].
     * @param limitcells The {@link AdqlQuery.Limits} cell limit, [{@value #UPDATE_LIMT_CELLS}].
     * @param limittime  The {@link AdqlQuery.Limits} time limit, [{@value #UPDATE_LIMT_TIME}].
     * <br/>Optional {@link Job.Status} params :
     * @param status  The {@link AdqlQuery} {@link Job.Status}, [{@value #UPDATE_STATUS}].
     * @param timeout The timeout to wait for a status change, [{@value #UPDATE_TIMEOUT}].
     * @return The updated {@link AdqlQuery} wrapped in a {@link AdqlQueryBean}.
     * @throws EntityNotFoundException If the {@link AdqlQuery} could not be found.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public AdqlQueryBean update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_INPUT, required=false)
        final String input,
        @RequestParam(value=UPDATE_DELAY_FIRST, required=false)
        final Integer first,
        @RequestParam(value=UPDATE_DELAY_EVERY, required=false)
        final Integer every,
        @RequestParam(value=UPDATE_DELAY_LAST, required=false)
        final Integer last,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout,
        @RequestParam(value=UPDATE_LIMT_ROWS, required=false)
        final Long limitrows,
        @RequestParam(value=UPDATE_LIMT_CELLS, required=false)
        final Long limitcells,
        @RequestParam(value=UPDATE_LIMT_TIME, required=false)
        final Long limittime,
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {

        //TODO idents().resolve(String)
        final AdqlQuery query = factories().queries().factory().select(
            factories().queries().idents().ident(
                ident
                )
            );

        if ((name != null) || (input != null))
            {
            update(
                new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        if (name != null)
                            {
                            if (name.length() > 0)
                                {
                                query.name(
                                    name
                                    );
                                }
                            }
                        if (input != null)
                            {
                            query.input(
                                input
                                );
                            }
                        }
                    }
                );
            }

        if ((first != null) || (every != null) || (last != null))
            {
            update(
                new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        if (first != null)
                            {
                            query.delays().first(
                                first
                                );
                            }
                        if (every != null)
                            {
                            query.delays().every(
                                every
                                );
                            }
                        if (last != null)
                            {
                            query.delays().last(
                                last
                                );
                            }
                        }
                    }
                );
            }

        if ((limitrows != null) || (limitcells != null) || (limittime != null))
            {
            update(
                new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        query.limits(
                            limitrows,
                            limitcells,
                            limittime
                            );
                        }
                    }
                );
            }

        if (status != null)
            {
            factories().queries().executor().update(
                query.ident(),
                status,
                timeout
                );
            }

        return bean(
            query
            ) ;
        }
    }
