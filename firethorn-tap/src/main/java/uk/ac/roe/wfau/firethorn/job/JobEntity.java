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

import java.util.concurrent.Future;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;

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
extends AbstractEntity
    implements Job
    {
    /**
     * Hibernate table mapping.
     * 
     */
    protected static final String DB_TABLE_NAME = "JobEntity";

    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_JOB_STATUS_COL = "jobstatus" ;

    /**
     * Local service implementations.
     * @todo Use this as a template for the other classes.
     * @todo Separate Entity Resolver and Factory interfaces.
     * 
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
     */

    /**
     * Resolver implementation.
     * 
     */
    @Repository
    public static class Resolver
    extends AbstractFactory<Job>
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
     * Factory implementation.
     * 
     */
    @Repository
    public static abstract class Factory<JobType extends Job>
    extends AbstractFactory<JobType>
    implements Job.Factory<JobType>
        {
        }

    /**
     * Executor implementation.
     * 
     */
    @Component
    public static abstract class Executor
    implements Job.Executor
        {
        @Override
        @UpdateEntityMethod
        public Status update(Job.Executor.Update update)
            {
            log.debug("Job.Executor.update(Update)");
            return update.update();
            }

        @Override
        @UpdateEntityMethod
        public Status prepare(Job.Executor.Executable executable)
            {
            log.debug("Job.Executor.prepare(Executable)");
            return executable.execute();
            }

        @Async
        @Override
        public Future<Status> execute(Job.Executor.Executable executable)
            {
            log.debug("Job.Executor.execute(Executable)");
            return new AsyncResult<Status>(
                executable.execute()
                );
            }
        }

    /**
     * Protected constructor.
     *
     */
    protected JobEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected JobEntity(String name)
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
        name = DB_JOB_STATUS_COL,
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

    protected Status status(Status next)
        {
        log.debug("status(Status) [{}]", next);
        this.status = next;
        return this.status;
        }

    protected Status update(final Status next)
        {
        log.debug("update(Status) [{}]", next);
        try {
            return factories().jobs().executor().update(
                new Job.Executor.Update()
                    {
                    @Override
                    public Status update()
                        {
                        return status(
                            next
                            );
                        }
                    }
                );
            }
        catch (Exception ouch)
            {
            log.error("Failed to update job status [{}][{}][{}]", ident(), next, ouch.getMessage());
            return Status.ERROR;
            }
        }
    
    @Override
    public Status cancel()
        {
        return update(
            Status.CANCELLED
            );
        }
    }
