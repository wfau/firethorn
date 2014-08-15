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
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.identity.Authentication;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlFactories;
import uk.ac.roe.wfau.firethorn.meta.base.BaseFactories;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaFactories;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcFactories;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaFactories;

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

    @Autowired
    protected OgsaFactories ogsa;
    @Override
    public OgsaFactories ogsa()
        {
        return this.ogsa;
        }

    @Autowired
    protected Community.EntityFactory communities;
    @Override
    public Community.EntityFactory communities()
        {
        return this.communities;
        }

    @Autowired
    protected Identity.EntityFactory identities;
    @Override
    public Identity.EntityFactory identities()
        {
        return this.identities;
        }

    @Autowired
    protected ConfigProperty.EntityFactory config ;
    @Override
    public ConfigProperty.EntityFactory config()
        {
        return this.config ;
        }

    @Autowired
    protected Job.Services jobs;
    @Override
    public Job.Services jobs()
        {
        return this.jobs;
        }

    @Autowired
    protected TestJob.Services tests;
    @Override
    public TestJob.Services tests()
        {
        return this.tests;
        }

    @Autowired
    protected AdqlQuery.Services queries;
    @Override
    public AdqlQuery.Services queries()
        {
        return this.queries;
        }

    @Autowired
    protected Operation.EntityFactory operations;
    @Override
	public Operation.EntityFactory operations()
        {
        return this.operations ;
        }

    @Autowired
    protected Authentication.EntityFactory authentications;
    @Override
	public Authentication.EntityFactory authentications()
        {
        return this.authentications;
        }

    @Autowired
    protected Context.Factory contexts;
    @Override
    public Context.Factory contexts()
        {
        return this.contexts;
        }
    }
