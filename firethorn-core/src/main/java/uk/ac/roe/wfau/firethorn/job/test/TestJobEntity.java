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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.JobEntity;

//import uk.ac.roe.wfau.firethorn.annotations.Checkpoint;

/**
 *
 *
 */
@Slf4j
@Entity()
@Configurable()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TestJobEntity.DB_TABLE_NAME
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
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "TestJobEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_TEST_LENGTH_COL = "length";
    protected static final String DB_TEST_LIMIT_COL  = "timelimit";

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
        private TestJob.Factory factory;
        @Override
        public TestJob.Factory factory()
            {
            return this.factory;
            }

        @Autowired
        private Job.Executor executor;
        @Override
        public Job.Executor executor()
            {
            return this.executor;
            }
        }

    @Override
    public TestJob.Services services()
        {
        return factories().tests();
        }

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<TestJob>
    implements TestJob.Factory
        {
        @Override
        public Class<?> etype()
            {
            return TestJobEntity.class ;
            }

        @Override
        @CreateMethod
        public TestJob create(final String name, final Integer length)
            {
        	log.debug("create(String, Integer)");
            return this.insert(
                new TestJobEntity(
                    name,
                    length
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<TestJob> select()
            {
        	log.debug("select()");
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
    protected TestJobEntity(final String name, final Integer length)
        {
        super(name);
        this.length = length;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TEST_LENGTH_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private Integer length;
    @Override
    //@Checkpoint("select")
    public Integer length()
        {
        return this.length;
        }
    @Override
    public void length(final Integer pause)
        {
        this.length = pause ;
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
    public void limit(final Integer limit)
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

    @Override
    public Status prepare()
        {
        log.debug("prepare(Identifier)");
        log.debug("  TestJob [{}]", ident());
        if ((status() == Status.EDITING) || (status() == Status.READY))
            {
            final int length = length().intValue();
            if (length >= 100)
                {
                return status(
                    Status.ERROR
                    );
                }
            else if ((length >= 0) && (length < 100))
                {
                return status(
                    Status.READY
                    );
                }
            else {
                return status(
                    Status.EDITING
                    );
                }
            }
        else {
            return Status.ERROR;
            }
        }

    @Override
    public Status execute()
        {
        log.debug("execute()");
        log.debug("  TestJob [{}][{}]", ident());

        Status result = services().executor().status(
            ident(),
            Status.RUNNING
            );

        log.debug("-- TestJob running [{}]", ident());
        try {

            for (int i = 0 ; ((i < length().intValue()) && (result == Status.RUNNING)) ; i++)
                {
                log.debug("-- TestJob sleeping [{}][{}]", ident(), new Integer(i));
                Thread.sleep(
                    1000
                    );

                refresh();

                if ((limit() != null) && (i >= limit().intValue()))
                    {
                    // Executed in new Transaction.
                    // Entity loaded from database.
                    result = services().executor().status(
                        ident(),
                        Status.FAILED
                        );
                    }

                else {
                    // Executed in new Transaction.
                    // Entity loaded from database.
                    result = services().executor().status(
                        ident()
                        );
                    }
                }
            log.debug("-- TestJob finishing [{}][{}]", ident(), result);
            if (result == Status.RUNNING)
                {
                result = services().executor().status(
                    ident(),
                    Status.COMPLETED
                    );
                }
            }
        catch (final InterruptedException ouch)
            {
            log.debug("-- TestJob interrupted [{}][{}]", ident(), ouch.getMessage());
            result = services().executor().status(
                ident(),
                Status.CANCELLED
                );
            }

        return result;
        }

    @Autowired
    @Transient
    private TestJob.Factory factory;

    @Override
	public TestJob.Factory factory()
    	{
		return this.factory;
		}
    }
