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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Limits;


/**
 * Simple implementation of the AdqlQuery.QueryLimits interface.
 * @todo combine BaseQueryLimits, AdqlQueryLimits and SimpleQueryLimits
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
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.rows.default
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.rows.default:#{null}}")
        private Long defaultrows;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.rows.absolute
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.rows.absolute:#{null}}")
        private Long absoluterows;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.cells.default
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.cells.default:#{null}}")
        private Long defaultcells;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.cells.absolute
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.cells.absolute:#{null}}")
        private Long absolutecells;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.time.default
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.time.default:#{null}}")
        private Long defaulttime;

        /*
         * @Value properties with defaults.
         * http://forum.spring.io/forum/spring-projects/container/78556-default-values-for-configuration-with-value
         * http://stackoverflow.com/a/17470732
         * Property : firethorn.limits.time.absolute
         * Default  : null
         * 
         */
        @Value("${firethorn.limits.time.absolute:#{null}}")
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

        @Override
        public Limits create(final Long rows, final Long cells, final Long time)
            {
            return new SimpleQueryLimits(
                rows,
                cells,
                time
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
