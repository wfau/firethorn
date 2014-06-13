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

import java.net.URI;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

/**
 * Spring MVC controller for <code>TestJob</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(TestJobController.LinkFactory.TEST_PATH)
public class TestJobController
    extends AbstractController
    {
	@Component
	public static class IdentFactory
	extends WebappIdentFactory
	implements TestJob.IdentFactory
	    {
	    public static final URI TYPE_URI = URI.create(
	        "http://data.metagrid.co.uk/wfau/firethorn/types/test-job-1.0.json"
	        );
	    }

	@Component
	public static class LinkFactory
	extends WebappLinkFactory<TestJob>
	implements TestJob.LinkFactory
	    {
	    protected LinkFactory()
	        {
	        super(
	            SERVICE_PATH
	            );
	        }

	    /**
	     * The URI path for the service.
	     *
	     */
	    protected static final String SERVICE_PATH = "/test/job" ;

	    /**
	     * The URI path for individual columns.
	     *
	     */
	    public static final String TEST_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

	    @Override
	    public String link(final TestJob entity)
	        {
	        return link(
	            TEST_PATH,
	            entity
	            );
	        }

        @Override
        public TestJob resolve(String link)
            throws IdentifierFormatException, IdentifierNotFoundException,
            EntityNotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
	    }

    @Override
    public Path path()
        {
        return path(
            LinkFactory.TEST_PATH
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
     * MVC property for updating the length.
     *
     */
    public static final String UPDATE_LENGTH = "test.job.update.length" ;

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
    extends NamedEntityBean<TestJob>
        {
        /**
         * The test duration in seconds.
         *
         */
        public Integer getLength();

        /**
         * The job status.
         * @todo Move this to a JobBean base class
         *
         */
        public Job.Status getStatus();

        /**
         * The date/time the Job was queued.
         * @todo Move this to a JobBean base class
         *
         */
        public String getQueued();

        /**
         * The date/time the Job was started.
         * @todo Move this to a JobBean base class
         *
         */
        public String getStarted();

        /**
         * The date/time the Job was finished.
         * @todo Move this to a JobBean base class
         *
         */
        public String getFinished();

        }

    /**
     * Bean implementation.
     *
     */
    protected static class BeanImpl
    extends NamedEntityBeanImpl<TestJob>
    implements Bean
        {
        protected BeanImpl(final TestJob job)
            {
            super(
                IdentFactory.TYPE_URI,
                job
                );
            }
        @Override
        public Integer getLength()
            {
            return entity().length();
            }
        @Override
        public Status getStatus()
            {
            return entity().status();
            }
        @Override
        public String getQueued()
            {
            if (entity().queued() != null)
                {
                return dateformat(
                    entity().queued()
                    );
                }
            else {
                return null ;
                }
            }
        @Override
        public String getStarted()
            {
            if (entity().started() != null)
                {
                return dateformat(
                    entity().started()
                    );
                }
            else {
                return null ;
                }
            }
        @Override
        public String getFinished()
            {
            if (entity().finished() != null)
                {
                return dateformat(
                    entity().finished()
                    );
                }
            else {
                return null ;
                }
            }
        }

    /**
     * Wrap an entity as a Bean.
     *
     */
    public static Bean bean(
        final TestJob inner
        ){
        return new BeanImpl(
            inner
            );
        }

    /**
     * Wrap an Iterable as an Iterable of Beans.
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
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MIME)
    public Bean select(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident
        ) throws EntityNotFoundException {
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
    @RequestMapping(method=RequestMethod.POST, produces=JSON_MIME)
    public Bean update(
        @PathVariable(WebappLinkFactory.IDENT_FIELD)
        final String ident,
        @RequestParam(value=UPDATE_NAME, required=false)
        final String name,
        @RequestParam(value=UPDATE_LENGTH, required=false)
        final Integer length,
        @RequestParam(value=UPDATE_LIMIT, required=false)
        final Integer limit,
        @RequestParam(value=UPDATE_STATUS, required=false)
        final Job.Status status,
        @RequestParam(value=UPDATE_TIMEOUT, required=false)
        final Integer timeout
        ) throws EntityNotFoundException {

        log.debug("---- ---- ---- ----");
        log.debug("JSON update(String, Integer, Status, Integer)");

        final TestJob job = factories().tests().resolver().select(
            factories().tests().idents().ident(
                ident
                )
            );

        if ((name != null) || (length != null) || (limit != null))
            {
            this.helper.update(
                job,
                name,
                length,
                limit
                );
            }

        if (status != null)
            {
            factories().tests().executor().update(
                job.ident(),
                status,
                timeout
                );
            }

        return bean(
            job
            ) ;
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
        public void update(final TestJob  job, final String  name, final Integer length, final Integer limit)
        throws EntityNotFoundException;

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
            final Integer length,
            final Integer limit
            )
        throws EntityNotFoundException
            {
            log.debug("---- ---- ---- ----");
            log.debug("update(TestJob, String, Integer)");
            log.debug("  Name   [{}]", name);
            log.debug("  Length [{}]", length);
            log.debug("  Limit  [{}]", limit);
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
            if (length != null)
                {
                log.debug("Setting length");
                job.length(
                    length
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

        }
    }
