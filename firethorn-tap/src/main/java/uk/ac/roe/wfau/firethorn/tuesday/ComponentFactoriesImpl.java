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
package uk.ac.roe.wfau.firethorn.tuesday;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Identity.Context;
import uk.ac.roe.wfau.firethorn.identity.Identity.Factory;

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
    private OgsaFactories ogsa;
    @Override
    public OgsaFactories ogsa()
        {
        return this.ogsa;
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
     */
    @Autowired
    protected Identity.Factory identities ;
    @Override
    public Factory identities()
        {
        return this.identities;
        }

    /**
     * Our Autowired Identity context factory.
     *
     */
    @Autowired
    protected Identity.Context.Factory contexts ;
    @Override
    public Context context()
        {
        return this.contexts.context();
        }

    /**
     * Our Autowired ConfigProperty factory.
     *
     */
    @Autowired
    protected ConfigProperty.Factory config ;
    @Override
    public ConfigProperty.Factory config()
        {
        return this.config ;
        }
    }
