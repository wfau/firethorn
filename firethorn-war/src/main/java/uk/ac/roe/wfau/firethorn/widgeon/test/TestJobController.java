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
package uk.ac.roe.wfau.firethorn.widgeon.test;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>JdbcTables</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(TestJobLinkFactory.TEST_PATH)
public class TestJobController
    extends AbstractController
    {

    @Override
    public Path path()
        {
        return path(
            TestJobLinkFactory.TEST_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public TestJobController()
        {
        super();
        }

    /**
     * MVC property for the target entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:test.job.entity" ;

    /**
     * MVC property for updating the name.
     *
     */
    public static final String UPDATE_NAME = "test.job.update.name" ;

    /**
     * MVC property for updating the pause.
     *
     */
    public static final String UPDATE_PAUSE = "test.job.update.pause" ;

    /**
     * MVC property for updating the status.
     *
     */
    public static final String UPDATE_STATUS = "test.job.update.status" ;

    /**
     * MVC property for the timelimit.
     *
     */
    public static final String UPDATE_TIMEOUT = "test.job.update.timeout" ;

    /**
     * Bean interface.
     *  
     */
    public static interface Bean
    extends EntityBean<TestJob>
        {
        /**
         * The test duration in seconds.
         * 
         */
        public Integer getPause();

        /**
         * The job status.
         * todo Move this to a JobBean base class ?
         * 
         */
        public Job.Status getStatus(); 

        }

    /**
     * Bean implementation.
     *  
     */
    protected static class BeanImpl
    extends AbstractEntityBeanImpl<TestJob>
    implements Bean
        {
        /**
         * Protected constructor.
         * 
         */
        protected BeanImpl(final TestJob job)
            {
            super(
                TestJobIdentFactory.TYPE_URI,
                job
                );
            }
        @Override
        public Integer getPause()
            {
            return entity().pause();
            }
        @Override
        public Status getStatus()
            {
            return entity().status();
            }
        }

    /**
     * Wrap an entity as a bean.
     *
     */
    public static Bean bean(
        final TestJob job
        ){
        return new BeanImpl(
            job
            );
        }

    /**
     * Iterable bean interface.
     *  
     */
    public static interface Iter
    extends Iterable<Bean>
        {
        }
    
    /**
     * Iterable bean implementation.
     *
     */
    public static Iter bean(
        final Iterable<TestJob> inner
        ){
        return new Iter()
            {
            @Override
            public Iterator<Bean> iterator()
                {
                return new Iterator<Bean>()
                    {
                    Iterator<TestJob> iter = inner.iterator(); 
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public Bean next()
                        {
                        return bean(
                            this.iter.next()
                            );
                        }
                    @Override
                    public void remove()
                        {
                        this.iter.remove();
                        }
                    };
                }
            };
        }
    
    /**
     * JSON GET request.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public Bean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        return bean(
            factories().tests().resolver().select(
                factories().tests().idents().ident(
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
    public Bean update(
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_PAUSE, required=false)
        final Integer pause,
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        return bean(
            this.helper.update(
                factories().tests().idents().ident(
                    ident
                    ),
                name,
                pause
                )
            );
        }
    
    /**
     * JSON POST update.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MAPPING, params=UPDATE_STATUS)
    public Bean update(
        @RequestParam(value=UPDATE_STATUS, required=true)
        final Job.Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout,
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws NotFoundException {
        return bean(
            this.helper.update(
                factories().tests().idents().ident(
                    ident
                    ),
                status,
                timeout
                )
            );
        }
    
    /**
     * Transactional helper.
     * 
     */
    public static interface Helper
        {
        /**
         * Transactional update.
         *
         */
        public TestJob update(final Identifier ident, final String  name, final Integer pause)
        throws NotFoundException;

        /**
         * Transactional update.
         *
         */
        public TestJob update(final Identifier ident, final Job.Status next, final Integer timeout)
        throws NotFoundException;

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
        public TestJob update(
            final Identifier ident,
            final String  name,
            final Integer pause
            )
        throws NotFoundException
            {
            log.debug("-----------------------------------");
            log.debug("update(Identifier, String, Integer)");
            final TestJob entity = factories().tests().resolver().select(
                ident
                );
            if (name != null)
                {
                if (name.length() > 0)
                    {
                    entity.name(
                        name
                        );
                    }
                }
            if (pause != null)
                {
                entity.pause(
                    pause
                    );
                }
            return entity;
            }

        @Override
        @SelectEntityMethod
        public TestJob update(
            final Identifier ident,
            final Job.Status next,
            final Integer timeout
            )
        throws NotFoundException
            {
            log.debug("-----------------------------------");
            log.debug("update(Identifier, Status, Integer)");

            Integer pause  = ((timeout != null) ? timeout : Integer.valueOf(2));
            Status  result = factories().tests().executor().status(
                ident
                );

            if ((next != null) && ( next != result))
                {
                if (next == Status.READY)
                    {
                    result = factories().tests().executor().prepare(
                        ident
                        );
                    }
                else if (next == Status.RUNNING)
                    {
                    try {
                        Future<Status> future = factories().tests().executor().execute(ident);
                        log.debug("Checking future");
                        result = future.get(
                            pause.intValue(),
                            TimeUnit.SECONDS
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
                    }
                else if (next == Status.CANCELLED)
                    {
                    result = factories().tests().executor().status(
                        ident,
                        Status.CANCELLED
                        );
                    }
                else {
                    result = Status.ERROR;
                    }
                }
            //
            // Return the updated state.
            return factories().tests().resolver().select(
                ident
                );
            }
        }
    }
