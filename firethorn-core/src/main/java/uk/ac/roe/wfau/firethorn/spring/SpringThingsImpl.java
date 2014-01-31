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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateMethod;


/**
 * SpringThings implementation.
 *
 *
 */
@Component
public class SpringThingsImpl
    implements SpringThings
    {
    /**
     * Autowired Spring ApplicationContext.
     *
     */
    @Autowired
    private ApplicationContext context ;

    @Override
    public ApplicationContext context()
        {
        return this.context ;
        }

    /**
     * TransactionWrapper implementation.
     *
     */
    @Component
    public static class TransactionWrapper
        implements SpringThings.TransactionWrapper
        {
        @Override
        @SelectMethod
        public void select(final Runnable runnable)
            {
            runnable.run();
            }

        @Override
        @UpdateMethod
        public void update(final Runnable runnable)
            {
            runnable.run();
            }
        }
    
    /**
     * Autowired TransactionWrapper instance.
     *
     */
    @Autowired
    private SpringThings.TransactionWrapper transactor;

    @Override
    public SpringThings.TransactionWrapper transactor()
        {
        return this.transactor;
        }
    }
