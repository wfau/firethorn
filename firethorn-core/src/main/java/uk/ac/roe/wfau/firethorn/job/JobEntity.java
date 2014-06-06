/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractComponent;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;

/**
 *
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JobEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class JobEntity
extends AbstractNamedEntity
implements Job
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JobEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JOBSTATUS_COL = "jobstatus" ;
    protected static final String DB_QUEUED_COL    = "queued"    ;
    protected static final String DB_STARTED_COL   = "started"   ;
    protected static final String DB_COMPLETED_COL = "completed" ;

    /**
     * Local service implementations.
     * @todo Use this as a template for the other classes.
     * @todo Separate Entity Resolver and Factory interfaces.
     *
     */
    @Component
    public static class Services
    implements Job.Services
        {
        @Autowired
        public Job.LinkFactory links;
        @Override
        public Job.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        public Job.IdentFactory idents;
        @Override
        public Job.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        public Job.Resolver resolver;
        @Override
        public Job.Resolver resolver()
            {
            return this.resolver;
            }

        @Autowired
        private Job.Executor executor;
        @Override
        public Job.Executor executor()
            {
            return this.executor;
            }
        }

    /**
     * Resolver implementation.
     *
     */
    @Repository
    public static class Resolver
    extends AbstractEntityFactory<Job>
    implements Job.Resolver
        {
        @Override
        public Class<?> etype()
            {
            return JobEntity.class;
            }

        @Autowired
        private Job.LinkFactory links;
        @Override
        public Job.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Job.IdentFactory idents;
        @Override
        public Job.IdentFactory idents()
            {
            return this.idents;
            }
        }

    /**
     * Executor implementation.
     *
     */
    @Component
    public static class Executor
    extends AbstractComponent
    implements Job.Executor
        {
        /**
         * Our service implementation.
         *
         */
        private Job.Executor executor;

        /**
         * Our service implementation.
         * @todo simplify what is essentially 'this'.
         * @autowired fails to resolve the recursive reference.
         *
         */
        protected synchronized Job.Executor executor()
            {
            if (this.executor == null)
                {
                this.executor = factories().jobs().executor();
                }
            return this.executor ;
            }

        /**
         * Our local service implementation.
         *
         */
        @Autowired
        private Job.Resolver resolver;

        /**
         * Our local service implementation.
         *
         */
        protected Job.Resolver resolver()
            {
            return this.resolver ;
            }


        @Override
        @SelectAtomicMethod
        public Status status(final Identifier ident)
            {
            log.debug("status(Identifier)");
            log.debug("  ident [{}]", ident);
            try {
                return resolver().select(
                    ident
                    ).status();
                }
            catch (final EntityNotFoundException ouch)
                {
                log.error("Failed to get job status [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            }

        @Override
        @UpdateAtomicMethod
        public Status status(final Identifier ident, final Status status)
            {
            log.debug("status(Identifier, Status)");
            log.debug("  ident  [{}]", ident);
            log.debug("  status [{}]", status);
            try {
                final Job job = resolver().select(
                    ident
                    );
                log.debug("Found [{}]", ((job != null ? job.ident() : null)));
                return job.status(
                        status
                        );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.error("Failed to set job status [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            catch (final Exception ouch)
                {
                log.error("Failed to set job status [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            }

        @Override
        public Status update(final Identifier ident, final Status next, final Integer timeout)
            {
            log.debug("update(Identifier, Status, Integer)");
            log.debug("  ident  [{}]", ident);
            log.debug("  status [{}]", next);
            log.debug("  wait   [{}]", timeout);

            Status result = executor().status(
                ident
                );

            if (next == Status.READY)
                {
                log.debug("Preparing job");
                result = executor().prepare(
                    ident
                    );
                }

            else if (next == Status.RUNNING)
                {
                log.debug("Preparing job");
                result = executor().prepare(
                    ident
                    );

                if (result == Status.READY)
                    {
                    log.debug("Queuing job");
                    result = executor().status(
                        ident,
                        Status.PENDING
                        );

                    if (result == Status.PENDING)
                        {
                        try {
                            log.debug("Executing query");
                            final Future<Status> future = executor().execute(
                                ident
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
                        catch (final TimeoutException ouch)
                            {
                            log.debug("TimeoutException");
                            }
                        catch (final InterruptedException ouch)
                            {
                            log.debug("InterruptedException [{}]", ouch.getMessage());
                            }
                        catch (final ExecutionException ouch)
                            {
                            log.debug("ExecutionException [{}]", ouch.getMessage());
                            }

                        result = factories().jobs().executor().status(
                            ident
                            );

                        }
                    }
                }

            else if (next == Status.CANCELLED)
                {
                result = executor().status(
                    ident,
                    Status.CANCELLED
                    );
                }

            else {
                result = Status.ERROR;
                }
            log.debug("---- ----");
            return result ;
            }

        @Override
        @UpdateAtomicMethod
        public Status prepare(final Identifier ident)
            {
            log.debug("prepare(Identifier)");
            log.debug("  Ident [{}]", ident);
            try {
                final Job job = resolver().select(
                    ident
                    );
if (job != null)
    {
    log.debug("Found [{}][{}]", job.ident(), job.getClass().getName());
    return job.prepare();
    }
else {
    log.debug("NOT FOUND");
    return Status.ERROR;
    }
                }
            catch (final EntityNotFoundException ouch)
                {
                log.error("Failed to prepare job [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            catch (final Exception ouch)
                {
                log.error("Failed to prepare job [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            }

        @Async
        @Override
        @SelectMethod
        public Future<Status> execute(final Identifier ident)
            {
            log.debug("execute(Identifier)");
            log.debug("  Ident [{}]", ident);
            try {
                return new AsyncResult<Status>(
                    resolver().select(
                        ident
                        ).execute()
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.error("Failed to execute job [{}][{}]", ident, ouch.getMessage());
                return new AsyncResult<Status>(
                    Status.ERROR
                    );
                }
            }

        }

    /**
     * Protected constructor.
     *
     */
    protected JobEntity()
        {
    	super();
        }

    /**
     * Protected constructor.
     *
     */
    protected JobEntity(final String name)
    throws NameFormatException
        {
        super(
            name
            );
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_JOBSTATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Status status = Status.EDITING;
    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public Status status(final boolean refresh)
        {
        log.debug("status(boolean) [{}][{}]", ident(), new Boolean(refresh));
        if (refresh)
            {
            refresh();
            }
        return status();
        }

    @Override
    public Status status(final Status next)
        {
        log.debug("status(Status) [{}][{}][{}]", ident(), this.status, next);

        if (next == null)
            {
            log.error("Unexpected status value [{}]", next);
            return Status.ERROR;
            }

        else if (next == this.status)
            {
            return this.status;
            }

        else if (next == Status.EDITING)
            {
            if (this.status == Status.READY)
                {
                this.status = Status.EDITING;
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.READY)
            {
            if (this.status == Status.EDITING)
                {
                this.status = Status.READY;
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.PENDING)
            {
            if (this.status == Status.READY)
                {
                this.status = Status.PENDING;
                this.queued = new DateTime();
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.RUNNING)
            {
            if ((this.status == Status.READY) || (this.status == Status.PENDING))
                {
                this.status  = Status.RUNNING;
                this.started = new DateTime();
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.COMPLETED)
            {
            if ((this.status == Status.EDITING) || (this.status == Status.READY) || (this.status == Status.PENDING) || (this.status == Status.RUNNING))
                {
                this.status   = Status.COMPLETED;
                this.finished = new DateTime();
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.CANCELLED)
            {
            if ((this.status == Status.EDITING) || (this.status == Status.READY) || (this.status == Status.PENDING) || (this.status == Status.RUNNING))
                {
                this.status   = Status.CANCELLED;
                this.finished = new DateTime();
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.FAILED)
            {
            if ((this.status == Status.EDITING) || (this.status == Status.READY) || (this.status == Status.PENDING) || (this.status == Status.RUNNING))
                {
                this.status   = Status.FAILED;
                this.finished = new DateTime();
                }
            else {
                log.debug("Unexpected status value [{}]", next);
                return Status.ERROR;
                }
            }

        else if (next == Status.ERROR)
            {
            this.status = Status.ERROR;
            }

        else {
            log.error("Unexpected status value [{}]", next);
            this.status = Status.ERROR;
            }

        return this.status;
        }

    /**
     * The date/time the Job was queued.
     *
     */
    @Column(
        name = DB_QUEUED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime queued ;
    @Override
    public DateTime queued()
        {
        return this.queued ;
        }

    /**
     * The date/time the Job was started.
     *
     */
    @Column(
        name = DB_STARTED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime started ;
    @Override
    public DateTime started()
        {
        return this.started ;
        }

    /**
     * The date/time the Job was finished.
     *
     */
    @Column(
        name = DB_COMPLETED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime finished ;
    @Override
    public DateTime finished()
        {
        return this.finished ;
        }

    /**
     * Refresh (fetch) this Entity from the database.
     * @todo Remove this if possible.
     *
     */
    public void refresh()
        {
        log.debug("---- ---- ---- ----");
        log.debug("refresh()");
        factories().hibernate().refresh(
            this
            );
        log.debug("---- ----");
        }
    }
