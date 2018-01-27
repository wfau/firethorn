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

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTaskLogEntry;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateThings;
import uk.ac.roe.wfau.firethorn.identity.AuthenticationImpl;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlFactories;
import uk.ac.roe.wfau.firethorn.meta.base.BaseFactories;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaFactories;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcFactories;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaFactories;


/**
 * Our component factories.
 *
 */
public interface ComponentFactories
    {
    /**
     * Our Spring related things.
     *
     */
    public SpringThings spring();

    /**
     * Our Hibernate related things.
     *
     */
    public HibernateThings hibernate();

    /**
     * Our base component factories.
     *
     */
    public BaseFactories base();

    /**
     * Our ADQL component factories.
     *
     */
    public AdqlFactories adql();

    /**
     * Our IVOA component factories.
     *
     */
    public IvoaFactories ivoa();

    /**
     * Our JDBC component factories.
     *
     */
    public JdbcFactories jdbc();

    /**
     * Our OGSA-DAI component factories.
     *
     */
    public OgsaFactories ogsa();
    
    /**
     * Our {@link Identity.EntityServices} instance.
     *
     */
    public Identity.EntityServices identities();

    /**
     * Our {@link Community.EntityServices} instance.
     *
     */
    public Community.EntityServices communities();

    /**
     * Our {@link Operation.EntityServices} instance.
     *
     */
    public Operation.EntityServices operations();

    /**
     * Our {@link AuthenticationImpl.EntityServices} instance.
     *
     */
    public AuthenticationImpl.EntityServices authentication();

    /**
     * Our {@link ConfigProperty.EntityServices} instance.
     *
     */
    public ConfigProperty.EntityServices config();

    /**
     * Our {@link BlueQuery.EntityServices} instance.
     *
     */
    public BlueQuery.EntityServices blues();
    
    /**
     * Access to the current Context factory.
     *
     */
    public Context.Factory contexts();

    /**
     * Our {@link BlueTaskLogEntry.EntityServices} instance.
     *
     */
    public BlueTaskLogEntry.EntityServices logger();

    }
