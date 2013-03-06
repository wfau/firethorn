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
package uk.ac.roe.wfau.firethorn.job.test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.JobEntity;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.Job.Executor.Executable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

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
    name = TestJobEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TestJob-select-all",
            query = "FROM TestJobEntity ORDER BY name asc, ident desc"
            )
        }
    )
public class TestJobEntity
extends JobEntity
implements TestJob
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "TestJobEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_TEST_PAUSE_COL = "pause";
    
    /**
     * Local service implementations.
     * 
     */
    @Component
    public static class Services
    implements TestJob.Services
        {
        @Autowired
        private TestJob.LinkFactory links;
        @Override
        public TestJob.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private TestJob.IdentFactory idents;
        @Override
        public TestJob.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private TestJob.Resolver resolver;
        @Override
        public TestJob.Resolver resolver()
            {
            return this.resolver;
            }

        @Autowired
        private TestJob.Factory factory;
        @Override
        public TestJob.Factory factory()
            {
            return this.factory;
            }

        @Autowired
        private TestJob.Executor executor;
        @Override
        public TestJob.Executor executor()
            {
            return this.executor;
            }
        }

    /**
     * Resolver implementation.
     * @todo Fix this so that it can extend JobEntity.Resolver<TestJob> 
     *
     */
    @Repository
    public static class Resolver
    extends AbstractFactory<TestJob>
    implements TestJob.Resolver
        {
        @Override
        public Class<?> etype()
            {
            return TestJobEntity.class ;
            }

        @Autowired
        protected TestJob.LinkFactory links;
        @Override
        public TestJob.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected TestJob.IdentFactory idents;
        @Override
        public TestJob.IdentFactory idents()
            {
            return this.idents;
            }
        }

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends JobEntity.Factory<TestJob>
    implements TestJob.Factory
        {
        @Override
        public Class<?> etype()
            {
            return TestJobEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TestJob create(final String name, final Integer delay)
            {
            return this.insert(
                new TestJobEntity(
                    name,
                    delay
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TestJob> select()
            {
            return super.iterable(
                super.query(
                    "TestJob-select-all"
                    )
                );
            }

        @Autowired
        protected TestJob.LinkFactory links;
        @Override
        public TestJob.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected TestJob.IdentFactory idents;
        @Override
        public TestJob.IdentFactory idents()
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
    extends JobEntity.Executor
    implements TestJob.Executor
        {
        }
    
    /**
     * Protected constructor.
     * 
     */
    protected TestJobEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * 
     */
    protected TestJobEntity(final String name, final Integer pause)
        {
        super(
            name
            );
        this.pause = pause;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TEST_PAUSE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Integer pause;
    @Override
    public Integer pause()
        {
        return this.pause;
        }

    /**
     * Our local service implementations.
     *
     */
    protected TestJob.Services services()
        {
        return factories().tests();
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    @Override
    public Status prepare()
        {
        try {
            return services().executor().prepare(
                new Executable()
                    {
                    @Override
                    public Status execute()
                        {
                        log.debug("prepare.Executable.execute()");
                        log.debug("  Job [{}]", name());
                        return status(
                            Status.READY
                            );
                        }
                    }
                );
            }
        catch (Exception ouch)
            {
            log.error("Failed to execute Job.prepare() [{}][{}]", ident(), ouch.getMessage());
            return Status.ERROR;
            }
        }

    @Override
    public Future<Status> execute()
        {
        return services().executor().execute(
            new Executable()
                {
                @Override
                public Status execute()
                    {
                    log.debug("execute.Executable.execute()");
                    log.debug("  Job [{}]", name());

                    log.debug("-- TestJob starting [{}]");
                    if (update(Status.RUNNING) == Status.ERROR)
                        {
                        return Status.ERROR;
                        }
                   
                    log.debug("-- TestJob running [{}]");
                    try {
                        for (int i = 0 ; i < pause().intValue() ; i++)
                            {
                            log.debug("-- TestJob sleeping [{}]", new Integer(i));
                            Thread.sleep(
                                1000
                                );
                            }
                        log.debug("-- TestJob finishing [{}]", status());
                        if (update(Status.COMPLETED) == Status.ERROR)
                            {
                            return Status.ERROR;
                            }
                        }
                    catch (InterruptedException ouch)
                        {
                        log.debug("-- TestJob interrupted [{}]", ouch.getMessage());
                        if (update(Status.CANCELLED) == Status.ERROR)
                            {
                            return Status.ERROR;
                            }
                        }
                    return status();
                    }
                }
            );
        }
    }
