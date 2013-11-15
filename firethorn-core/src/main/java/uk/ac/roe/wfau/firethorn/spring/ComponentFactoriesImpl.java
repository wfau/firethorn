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
package uk.ac.roe.wfau.firethorn.spring;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Community;
import uk.ac.roe.wfau.firethorn.identity.CommunityMember;
import uk.ac.roe.wfau.firethorn.identity.DataSpace;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlFactories;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseFactories;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaFactories;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcFactories;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Our component factories.
 *
 */
@Slf4j
@Component("monday")
public class ComponentFactoriesImpl
    implements ComponentFactories
    {
    /**
     * Our global singleton instance.
     *
     */
    protected static ComponentFactories instance = null ;

    /**
     * Access to our singleton instance.
     *
     */
    public static ComponentFactories instance()
        {
        if (ComponentFactoriesImpl.instance == null)
            {
            throw new IllegalStateException(
                "ComponentFactories instance is null"
                );
            }
        return ComponentFactoriesImpl.instance;
        }

    /**
     * Initialise our singleton instance.
     *
     */
    public static void instance(final ComponentFactories monday)
        {
        if (ComponentFactoriesImpl.instance == null)
            {
            if (monday != null)
                {
                ComponentFactoriesImpl.instance = monday;
                }
            else {
                log.warn("Null value for ComponentFactories initialiser");
                throw new IllegalArgumentException(
                    "Null value for ComponentFactories initialiser"
                    );
                }
            }
        else {
            log.warn("ComponentFactories instance is already set [{}]", ComponentFactoriesImpl.instance);
            throw new IllegalStateException(
                "Setting ComponentFactories instance more than once"
                );
            }
        }

    /**
     * Private constructor.
     *
     */
    private ComponentFactoriesImpl()
        {
        ComponentFactoriesImpl.instance(
            this
            );
        }

    @Autowired
    private SpringThings spring;
    @Override
    public SpringThings spring()
        {
        return this.spring;
        }

    @Autowired
    private HibernateThings hibernate;
    @Override
    public HibernateThings hibernate()
        {
        return this.hibernate;
        }

    @Autowired
    protected BaseFactories base;
    @Override
    public BaseFactories base()
        {
        return this.base;
        }

    @Autowired
    private AdqlFactories adql;
    @Override
    public AdqlFactories adql()
        {
        return this.adql;
        }

    @Autowired
    private IvoaFactories ivoa;
    @Override
    public IvoaFactories ivoa()
        {
        return this.ivoa;
        }

    @Autowired
    private JdbcFactories jdbc;
    @Override
    public JdbcFactories jdbc()
        {
        return this.jdbc;
        }

    /**
     * Our Autowired Identity factory.
     *
    @Autowired
    protected Identity.EntityFactory identities ;
    @Override
    public Identity.EntityFactory identities()
        {
        return this.identities;
        }
     */

    /**
     * Our Autowired Identity context factory.
     *
     */
    @Autowired
    protected Community.EntityFactory communities;
    @Override
    public Community.EntityFactory communities()
        {
        return this.communities;
        }

    /**
     * Our Autowired ConfigProperty factory.
     *
     */
    @Autowired
    protected ConfigProperty.EntityFactory config ;
    @Override
    public ConfigProperty.EntityFactory config()
        {
        return this.config ;
        }

    /**
     * Our Autowired Job factory.
     *
     */
    @Autowired
    protected Job.Services jobs;
    @Override
    public Job.Services jobs()
        {
        return this.jobs;
        }

    /**
     * Our Autowired Test factory.
     *
     */
    @Autowired
    protected TestJob.Services tests;
    @Override
    public TestJob.Services tests()
        {
        return this.tests;
        }

    /**
     * Our Autowired Query factory.
     *
     */
    @Autowired
    protected AdqlQuery.Services queries;
    @Override
    public AdqlQuery.Services queries()
        {
        return this.queries;
        }

    /**
     * Our Autowired operation factory.
     *
     */
    @Autowired
    protected Operation.EntityFactory operations;
    @Override
	public Operation.EntityFactory operations()
        {
        return this.operations ;
        }

    /**
     * Our Autowired authentication factory.
     *
     */
    @Autowired
    protected Authentication.EntityFactory authentications;
    @Override
	public Authentication.EntityFactory authentications()
        {
        return this.authentications;
        }

    @Override
    public Context context()
        {
        return new Context()
            {
            @Override
            public Operation oper()
                {
                return operations().current();
                }

            @Override
            public Authentication auth()
                {
                Operation oper = oper();
                if (oper != null)
                    {
                    return oper.auth().primary();
                    }
                return null ;
                }

            @Override
            public Identity identity()
                {
                Authentication auth = auth();
                if (auth != null)
                    {
                    return auth.identity();
                    }
                return null ;
                }

            @Override
            public DataSpace space()
                {
                return new DataSpace()
                    {
                    @Override
                    public JdbcSchema jdbc()
                        {
                        Identity identity = identity();
                        if (identity != null)
                            {
                            if (identity instanceof CommunityMember)
                                {
                                return ((CommunityMember)identity).space(
                                    true
                                    ); 
                                }
                            }
                        return null;
                        }
                    @Override
                    public AdqlSchema adql()
                        {
                        return null;
                        }
                    };
                }
            };
        }
    }
