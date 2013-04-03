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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityController;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.widgeon.test.TestJobController.Helper;
import uk.ac.roe.wfau.firethorn.widgeon.test.TestJobController.HelperImpl;

/**
 * Spring MVC controller for <code>AdqlQuery</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlQueryLinkFactory.QUERY_PATH)
public class AdqlQueryController
extends AbstractEntityController<AdqlQuery>
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

    @Override
    public EntityBean<AdqlQuery> bean(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    @Override
    public Iterable<EntityBean<AdqlQuery>> bean(Iterable<AdqlQuery> iter)
        {
        return new AdqlQueryBean.Iter(
            iter
            );
        }

    /**
     * Get the target entity based on the ident in the path.
     * @throws NotFoundException
     *
    @ModelAttribute(TARGET_ENTITY)
    public AdqlQuery entity(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
        return factories().adql().queries().select(
            factories().adql().queries().idents().ident(
                ident
                )
            );
        }
     */

    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public EntityBean<AdqlQuery> select(
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {
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
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING)
    public EntityBean<AdqlQuery> update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_QUERY, required=false)
        final String input,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout,
        @PathVariable("ident")
        final String ident
        ) throws NotFoundException {

        final AdqlQuery query = factories().queries().resolver().select(
            factories().queries().idents().ident(
                ident
                )
            );

        if ((name != null) || (input != null))
            {
            this.helper.update(
                query,
                name,
                input
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

    /**
     * Transactional helper.
     * 
     */
    public static interface Helper
        {
        
        /**
         * Update the properties.
         *
         */
        public void update(final AdqlQuery query, final String  name, final String input)
        throws NotFoundException;

        /**
         * Update the status.
         *
        public Status update(final Job job, final Job.Status next, final Integer timeout)
        throws NotFoundException;
         */
        
        }

    /**
     * Transactional helper.
     * 
     */
    @Autowired
    private Helper helper ;

    /**
     * Transactional helper.
     * 
     */
    @Slf4j
    @Component
    public static class HelperImpl
    extends AbstractComponent
    implements Helper
        {
        @Override
        @UpdateAtomicMethod
        public void update(
            final AdqlQuery query,
            final String  name,
            final String input
            )
        throws NotFoundException
            {
            log.debug("---- ---- ---- ----");
            log.debug("update(AdqlQuery, String, String)");
            log.debug("  Name  [{}]", name);
            log.debug("  Input [{}]", input);
            if (name != null)
                {
                log.debug("Setting name");
                if (name.length() > 0)
                    {
                    query.name(
                        name
                        );
                    }
                }
            if (input != null)
                {
                log.debug("Setting input");
                query.input(
                    input
                    );
                }
            log.debug("---- ----");
            }

        /*
        public static final int MINIMUM_TIMEOUT =  5 ;
        public static final int DEFAULT_TIMEOUT = 10 ;

        @Override
        public Status update(
            final Job job,
            final Job.Status next,
            final Integer timeout
            )
        throws NotFoundException
            {
            log.debug("---- ---- ---- ----");
            log.debug("update(AdqlQuery, Status, Integer)");

            Status result = job.status(true);
            
            if (next == Status.READY)
                {
                log.debug("Preparing query");
                result = factories().queries().executor().prepare(
                    job.ident()
                    );
                }

            else if (next == Status.RUNNING)
                {
                log.debug("Preparing job");
                result = factories().queries().executor().prepare(
                    job.ident()
                    );

                if (result == Status.READY)
                    {
                    log.debug("Queuing job");
                    result = factories().queries().executor().status(
                        job.ident(),
                        Status.PENDING
                        );
                    
                    if (result == Status.PENDING)
                        {
                        try {
                            log.debug("Executing query");
                            Future<Status> future = factories().queries().executor().execute(
                                job.ident()
                                );

                            int waitlimit = DEFAULT_TIMEOUT;
                            if (timeout != null)
                                {
                                if (timeout.intValue() < MINIMUM_TIMEOUT)
                                    {
                                    waitlimit = MINIMUM_TIMEOUT;
                                    }
                                else {
                                    waitlimit = timeout.intValue() ;
                                    }
                                }

                            log.debug("Checking future");
                            result = future.get(
                                waitlimit,
                                TimeUnit.MILLISECONDS
                                );
                            log.debug("Result [{}]", result);
                            }
                        catch (TimeoutException ouch)
                            {
                            log.debug("TimeoutException");
                            }
                        catch (InterruptedException ouch)
                            {
                            log.debug("InterruptedException [{}]", ouch.getMessage());
                            }
                        catch (ExecutionException ouch)
                            {
                            log.debug("ExecutionException [{}]", ouch.getMessage());
                            }
        
                        result = job.status(
                            true
                            );
                        }
                    }
                }

            else if (next == Status.CANCELLED)
                {
                result = factories().tests().executor().status(
                    job.ident(),
                    Status.CANCELLED
                    );
                }

            else {
                result = Status.ERROR;
                }
            log.debug("---- ----");
            return result ;
            }
         */
        }
    }
