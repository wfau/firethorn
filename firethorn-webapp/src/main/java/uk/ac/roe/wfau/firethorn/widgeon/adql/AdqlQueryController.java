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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>AdqlQuery</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.QUERY_PATH)
public class AdqlQueryController
extends AbstractEntityController<AdqlQuery, AdqlQueryBean>
    {

    @Override
    public Path path()
        {
        return path(
            AdqlQueryLinkFactory.QUERY_PATH
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
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.query.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "adql.query.update.name" ;

    /**
     * MVC property for updating the query.
     *
     */
    public static final String UPDATE_QUERY = "adql.query.update.query" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "adql.query.update.status" ;

    /**
     * MVC property for the timelimit.
     *
     */
    public static final String UPDATE_TIMEOUT = "adql.query.update.timeout" ;

    
    /**
     * MVC property for the first delay.
     *
     */
    public static final String UPDATE_DELAY_FIRST = "adql.query.update.delay.first" ;
    
    /**
     * MVC property for the row delay.
     *
     */
    public static final String UPDATE_DELAY_EVERY = "adql.query.update.delay.every" ;

    /**
     * MVC property for the last delay.
     *
     */
    public static final String UPDATE_DELAY_LAST = "adql.query.update.delay.last" ;
    
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
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_CONTENT)
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
     * JSON POST update.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_CONTENT)
    public AdqlQueryBean update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_QUERY, required=false)
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
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException {

        final AdqlQuery query = factories().queries().resolver().select(
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
            log.debug("Delays [{}][{}][{}]",first, every, last);
            update(
                new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        if (first != null)
                            {
                            query.delays().ogsa().first(
                                first
                                );
                            }
                        if (every != null)
                            {
                            query.delays().ogsa().every(
                                every
                                );
                            }
                        if (last != null)
                            {
                            query.delays().ogsa().last(
                                last
                                );
                            }
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
