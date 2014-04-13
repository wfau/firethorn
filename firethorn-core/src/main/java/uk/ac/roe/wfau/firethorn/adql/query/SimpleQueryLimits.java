/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Simple implementation of the AdqlQuery.QueryLimits interface.
 *
 */
public class SimpleQueryLimits
extends BaseQueryLimits
implements AdqlQuery.Limits
    {
    /**
     * Factory implementation.
     * 
     */
    @Component
    public static class Factory
    implements AdqlQuery.Limits.Factory
        {
        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.rows.default
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.rows.default:0}")
        private Long defaultrows;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.rows.absolute
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.rows.absolute:0}")
        private Long absoluterows;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.cells.default
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.cells.default:0}")
        private Long defaultcells;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.cells.absolute
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.cells.absolute:0}")
        private Long absolutecells;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.time.default
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.time.default:0}")
        private Long defaulttime;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * Property : firethorn.limits.time.absolute
         * Default  : 0
         * 
         */
        @Value("${firethorn.limits.time.absolute:0}")
        private Long absolutetime;

        private AdqlQuery.Limits defaults = new BaseQueryLimits()
            {
            @Override
            public Long rows()
                {
                return defaultrows;
                }

            @Override
            public Long cells()
                {
                return defaultcells;
                }

            @Override
            public Long time()
                {
                return defaulttime;
                }
            };
        
        @Override
        public AdqlQuery.Limits defaults()
            {
            return defaults;
            }

        private AdqlQuery.Limits absolutes = new BaseQueryLimits()
            {
            @Override
            public Long rows()
                {
                return absoluterows;
                }

            @Override
            public Long cells()
                {
                return absolutecells;
                }

            @Override
            public Long time()
                {
                return absolutetime;
                }
            };

        @Override
        public AdqlQuery.Limits absolute()
            {
            return absolutes;
            }

        @Override
        public AdqlQuery.Limits defaults(final AdqlQuery.Limits that)
            {
            return SimpleQueryLimits.combine(
                that,
                defaults
                );
            }

        @Override
        public AdqlQuery.Limits absolute(final AdqlQuery.Limits that)
            {
            return SimpleQueryLimits.lowest(
                that,
                absolutes
                );
            }

        @Override
        public AdqlQuery.Limits runtime(final AdqlQuery.Limits that)
            {
            return SimpleQueryLimits.lowest(
                SimpleQueryLimits.combine(
                    that,
                    defaults
                    ),
                absolutes
                );
            }
        }
    
    /**
     * Protected constructor.
     * 
     */
    protected SimpleQueryLimits()
        {
        }

    /**
     * Protected constructor.
     * 
     */
    protected SimpleQueryLimits(final AdqlQuery.Limits limits)
        {
        this(
            ((limits != null) ? limits.rows()  : null), 
            ((limits != null) ? limits.cells() : null), 
            ((limits != null) ? limits.time()  : null) 
            );
        }

    /**
     * Protected constructor.
     * 
     */
    protected SimpleQueryLimits(final Long rows, final Long cells, final Long time)
        {
        this.rows  = rows  ;
        this.cells = cells ;
        this.time  = time  ;
        }
    
    private Long rows;

    @Override
    public Long rows()
        {
        return rows;
        }

    private Long cells;

    @Override
    public Long cells()
        {
        return cells;
        }

    private Long time;

    @Override
    public Long time()
        {
        return time;
        }
    }
