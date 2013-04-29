/*
 * Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.roe.wfau.firethorn.identity;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Identity context implementation.
 * This implementation assumes a one to one match between the current Thread and
 * a Hibernate Session.
 * A more complete implementation should check that the Identity Entity is part
 * of the current Hibernate Session
 *
 */
@Slf4j
public class SimpleContext
implements Identity.Context
    {

    /**
     * Factory implementation.
     *
     */
    @Component
    public static class Factory
    implements Identity.Context.Factory
        {

        /**
         * Autowired Identity Factory.
         *
         */
        @Autowired
        protected Identity.Factory factory;

        /**
         * ThreadLocal storage for the current Identity.
         *
         */
        private final ThreadLocal<Identity> tracker =
            new ThreadLocal<Identity>()
                {
                @Override
                protected Identity initialValue()
                    {
                    log.debug("initial()");
                    return Factory.this.factory.create(
                        "anon-identity"
                        );
                    }
                };

        @Override
        public SimpleContext context()
            {
            //log.debug("Identity.Context.Factory.context()");
            return new SimpleContext(
                this.tracker.get()
                );
            }
        }

    /**
     * Protected constructor.
     *
     */
    protected SimpleContext(final Identity identity)
        {
        //log.debug("SimpleContext()");
        this.identity = identity;
        }

    /**
     * The current identity.
     *
     */
    private final Identity identity;

    @Override
    public Identity identity()
        {
        //log.debug("SimpleContext.identity()");
        return this.identity;
        }
    }
