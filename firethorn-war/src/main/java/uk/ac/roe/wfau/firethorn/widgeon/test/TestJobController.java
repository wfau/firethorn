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
     * MVC property for updating the limit.
     *
     */
    public static final String UPDATE_LIMIT = "test.job.update.limit" ;

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
     * Iterable bean.
     *
     */
    public static Iterable<Bean> bean(
        final Iterable<TestJob> inner
        ){
        return new Iterable<Bean>()
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
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_PAUSE, required=false)
        final Integer pause,
        @RequestParam(value=UPDATE_LIMIT, required=false)
        final Integer limit,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final Job.Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout
        ) throws NotFoundException {
        try {
        log.debug("---- ---- ---- ----");
        log.debug("JSON update(String, Integer, Status, Integer)");
        
        TestJob job = factories().tests().resolver().select(
            factories().tests().idents().ident(
                ident
                )
            );

        if ((UPDATE_NAME != null) || (UPDATE_PAUSE != null) || (UPDATE_LIMIT != null) )
            {
            this.helper.update(
                job,
                name,
                pause,
                limit
                );
            }

        if (status != null)
            {
            this.helper.update(
                job,
                status,
                timeout
                );
            }

        return bean(
            job
            ) ;
        }
    finally {
        log.debug("---- ----");
        }
    }
    
    /**
     * Transactional helper.
     * 
     */
    public static interface Helper
        {

        /**
         * Update the properties in a new transaction.
         *
         */
        public void update(final TestJob  job, final String  name, final Integer pause, final Integer limit)
        throws NotFoundException;

        /**
         * Update the status.
         *
         */
        public Status update(final TestJob  job, final Job.Status next, final Integer timeout)
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
        public void update(
            final TestJob  job,
            final String  name,
            final Integer pause,
            final Integer limit
            )
        throws NotFoundException
            {
            log.debug("---- ---- ---- ----");
            log.debug("update(Identifier, String, Integer)");
            log.debug("  Name  [{}]", name);
            log.debug("  Pause [{}]", pause);
            log.debug("  Limit [{}]", limit);
            if (name != null)
                {
                log.debug("Setting name");
                if (name.length() > 0)
                    {
                    job.name(
                        name
                        );
                    }
                }
            if (pause != null)
                {
                log.debug("Setting pause");
                job.pause(
                    pause
                    );
                }
            if (limit != null)
                {
                log.debug("Setting limit");
                job.limit(
                    limit
                    );
                }
            log.debug("---- ----");
            }

        @Override
        public Status update(
            final TestJob  job,
            final Job.Status next,
            final Integer timeout
            )
        throws NotFoundException
            {
            log.debug("---- ---- ---- ----");
            log.debug("update(Identifier, Status, Integer)");
/*            
            log.debug("Selecting job [{}]", ident);
            // Small transaction 
            // Result is a cached object
            TestJob job = factories().tests().resolver().select(
                ident
                );
 */                
            //log.debug("Refreshing job [{}][{}]", job.ident(), job.modified());
            // Main transaction 
            // Refresh queries database.
            // Result is updated object
            // job.refresh();
/*
            // New transaction << - do we need this ?
            // Result is a cached object
            Status result = factories().tests().executor().status(
                ident
                );
 */
            Status result = job.status(true);

            Integer pause  = ((timeout != null) ? timeout : Integer.valueOf(1));

            if (next == null)
                {
                }

            else if (next == Status.READY)
                {
                log.debug("Preparing job");
                result = factories().tests().executor().prepare(
                    job.ident()
                    );
                }

            else if (next == Status.RUNNING)
                {
                try {
                    log.debug("Executing job");
                    // Structures created, nothing executed yet
                    Future<Status> future = factories().tests().executor().execute(
                        job.ident()
                        );

                    log.debug("Checking future");
                    // New Thread started, execute()
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

                //job.refresh();
                result = job.status(true);

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
/*
            //
            // Return the refreshed state.
            log.debug("Refreshing job [{}][{}]", job.ident(), job.modified());
            // refresh() happens in main transaction.
            // Entity loaded from database.
            job.refresh();
 */
            log.debug("---- ----");
            return result ;
            }
        }
    }
