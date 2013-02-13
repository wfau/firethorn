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

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.identity.Identity;
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
     * Our ogsa component factories.
     *
     */
    public OgsaFactories ogsa();

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
     * Our local Identity factory.
     *
     */
    public Identity.Factory identities();

    /**
     * The current Identity context.
     *
     */
    public Identity.Context context();

    /**
     * Our local ConfigProperty factory.
     *
     */
    public ConfigProperty.Factory config();

    }
