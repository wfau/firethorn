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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlQuery.Status;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlSchema</code>.
 *
 */
public class AdqlQueryBean
extends AbstractEntityBeanImpl<TuesdayAdqlQuery>
implements EntityBean<TuesdayAdqlQuery>
    {
    public static class Iter
    extends AbstractEntityBeanIter<TuesdayAdqlQuery>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<TuesdayAdqlQuery> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public EntityBean<TuesdayAdqlQuery> bean(final TuesdayAdqlQuery entity)
            {
            return new AdqlQueryBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public AdqlQueryBean(final TuesdayAdqlQuery entity)
        {
        super(
            AdqlQueryIdentFactory.TYPE_URI,
            entity
            );
        }

    public URI getResource()
        {
        try {
            return new URI(
                entity().resource().link()
                );
            }
        catch (final URISyntaxException ouch)
            {
            throw new RuntimeException(
                ouch
                );
            }
        }

    public String getInput()
        {
        return entity().query();
        }

    public Mode getMode()
        {
        return entity().mode();
        }

    public Status getStatus()
        {
        return entity().status();
        }

    public String getAdql()
        {
        return entity().adql();
        }

    public String getOgsa()
        {
        return entity().ogsa();
        }
    }
