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

import com.sun.swing.internal.plaf.synth.resources.synth;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectAtomicMethod;
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
    protected static final String DB_TEST_LIMIT_COL = "limit";
    
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
     * Our local service implementations.
     *
     */
    protected TestJob.Services services()
        {
        return factories().tests();
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
    extends AbstractFactory<TestJob>
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
        /**
         * Our local service implementation.
         *
         */
        private TestJob.Executor executor;

        /**
         * Our local service implementation.
         *
         */
        private TestJob.Executor executor()
            {
            if (this.executor == null)
                {
                this.executor = factories().tests().executor();
                }
            return this.executor ;
            }

        /**
         * Our local service implementation.
         *
         */
        private TestJob.Resolver resolver;

        /**
         * Our local service implementation.
         *
         */
        private TestJob.Resolver resolver()
            {
            if (this.resolver == null)
                {
                this.resolver = factories().tests().resolver();
                }
            return this.resolver ;
            }
        
        @Override
        @UpdateAtomicMethod
        public Status prepare(Identifier ident)
            {
            log.debug("---- ---- ---- ----");
            log.debug("prepare(Identifier)");
            log.debug("  TestJob [{}]", ident);
            try {
                TestJob job = resolver().select(
                    ident
                    );
                if ((job.status() == Status.EDITING) || (job.status() == Status.READY))
                    {
                    int pause = job.pause().intValue();
                    if (pause >= 100)
                        {
                        return job.status(
                            Status.ERROR
                            );
                        }
                    else if ((pause >= 0) && (pause < 100))
                        {
                        return job.status(
                            Status.READY
                            );
                        }
                    else {
                        return job.status(
                            Status.EDITING
                            );
                        }
                    }
                else {
                    return Status.ERROR;
                    }
                }
            catch (NotFoundException ouch)
                {
                log.error("Failed to prepare job [{}][{}]", ident, ouch.getMessage());
                return Status.ERROR;
                }
            finally {
                log.debug("---- ----");
                }
            }

        @Async
        @Override
        public Future<Status> execute(Identifier ident)
            {
            log.debug("---- ---- ---- ----");
            log.debug("execute()");
            log.debug("  TestJob [{}][{}]", ident);
            // Prepare is UpdateAtomic
            // Executed in new Transaction.
            // Entity loaded from database.
            Status result = executor().prepare(
                ident
                );

            if (result == Status.READY)
                {
                // Executed in new Transaction.
                // Entity loaded from database.
                result = executor().status(
                    ident,
                    Status.RUNNING
                    );

                if (result == Status.RUNNING)
                    {
                    log.debug("-- TestJob running [{}]", ident);
                    try {
                        // Executed in new Transaction.
                        // Entity loaded from database.
                        TestJob job = resolver().select(
                            ident
                            );

                        for (int i = 0 ; ((i < job.pause().intValue()) && (result == Status.RUNNING)) ; i++)
                            {
                            log.debug("-- TestJob sleeping [{}][{}]", ident, new Integer(i));
                            Thread.sleep(
                                1000
                                );

                            if ((job.limit() != null) && (i >= job.limit().intValue()))
                                {
                                // Executed in new Transaction.
                                // Entity loaded from database.
                                result = executor().status(
                                    ident,
                                    Status.FAILED
                                    );
                                }

                            else {
                                // Executed in new Transaction.
                                // Entity loaded from database.
                                result = executor().status(
                                    ident
                                    );
                                }
                            }
                        log.debug("-- TestJob finishing [{}][{}]", ident, result);
                        if (result == Status.RUNNING)
                            {
                            result = executor().status(
                                ident,
                                Status.COMPLETED
                                );
                            }
                        }
                    catch (InterruptedException ouch)
                        {
                        log.debug("-- TestJob interrupted [{}][{}]", ident, ouch.getMessage());
                        result = executor().status(
                            ident,
                            Status.CANCELLED
                            );
                        }
                    catch (NotFoundException ouch)
                        {
                        log.error("Failed to execute job [{}][{}]", ident, ouch.getMessage());
                        result = Status.ERROR;
                        }
                    }
                }
            return new AsyncResult<Status>(
                result
                );
            }
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
        updatable = true
        )
    private Integer pause;
    @Override
    public Integer pause()
        {
        return this.pause;
        }
    @Override
    public void pause(Integer pause)
        {
        this.pause = pause ;
        }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TEST_LIMIT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer limit;
    @Override
    public Integer limit()
        {
        return this.limit;
        }
    @Override
    public void limit(Integer limit)
        {
        this.limit = limit ;
        }
    
    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    }
