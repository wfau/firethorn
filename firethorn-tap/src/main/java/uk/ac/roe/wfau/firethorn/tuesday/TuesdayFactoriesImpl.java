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

/**
 * Our component factories.
 *
 */
@Slf4j
@Component("factories")
public class TuesdayFactoriesImpl
    implements TuesdayFactories
    {
    /**
     * Our global singleton instance.
     *
     */
    static TuesdayFactories singleton = null ;

    /**
     * Access to our singleton instance.
     *
     */
    public static TuesdayFactories factories()
        {
        return singleton ;
        }

    /**
     * Initialise our singleton instance.
     *
     */
    public static void factories(final TuesdayFactories factories)
        {
        if (singleton == null)
            {
            if (factories != null)
                {
                singleton = factories;
                }
            else {
                log.warn("Null value for TuesdayFactories initialiser");
                throw new IllegalArgumentException(
                    "Null value for TuesdayFactories initialiser"
                    );
                }
            }
        else {
            log.warn("TuesdayFactories instance is already set [{}]", singleton);
            throw new IllegalStateException(
                "Setting TuesdayFactories instance more than once"
                );
            }
        }

    /**
     * Private constructor.
     *
     */
    private TuesdayFactoriesImpl()
        {
        }
    
    @Autowired
    private TuesdaySpringThings spring;
    @Override
    public TuesdaySpringThings spring()
        {
        return this.spring;
        }

    @Autowired
    private TuesdayHibernateThings hibernate;
    @Override
    public TuesdayHibernateThings hibernate()
        {
        return this.hibernate;
        }

    @Autowired
    private TuesdayAdqlFactories adql;
    @Override
    public TuesdayAdqlFactories adql()
        {
        return this.adql;
        }

    @Autowired
    private TuesdayIvoaFactories ivoa;
    @Override
    public TuesdayIvoaFactories ivoa()
        {
        return this.ivoa;
        }

    @Autowired
    private TuesdayJdbcFactories jdbc;
    @Override
    public TuesdayJdbcFactories jdbc()
        {
        return this.jdbc;
        }
    }
