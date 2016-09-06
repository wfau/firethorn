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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;

/**
 * Embeddable implementation of the AdqlQuery.QueryLimits interface.
 *
 */
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQueryLimits
implements AdqlQueryBase.Limits
    {
    /**
     * Value used to indicate no limit, {@value}.
     * 
     */
    protected static final Long NO_VALUE = null;

    /**
     * Factory implementation.
     * 
     */
    @Component
    public static class Factory
    implements AdqlQueryBase.Limits.Factory
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

        private AdqlQueryBase.Limits defaults = new AdqlQueryLimits()
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
        public AdqlQueryBase.Limits defaults()
            {
            return defaults;
            }

        private AdqlQueryBase.Limits absolutes = new AdqlQueryLimits()
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
        public AdqlQueryBase.Limits absolute()
            {
            return absolutes;
            }

        @Override
        public AdqlQueryBase.Limits defaults(final AdqlQueryBase.Limits that)
            {
            return AdqlQueryLimits.combine(
                that,
                defaults
                );
            }

        @Override
        public AdqlQueryBase.Limits absolute(final AdqlQueryBase.Limits that)
            {
            return AdqlQueryLimits.lowest(
                that,
                absolutes
                );
            }

        @Override
        public AdqlQueryBase.Limits runtime(final AdqlQueryBase.Limits that)
            {
            return AdqlQueryLimits.lowest(
                AdqlQueryLimits.combine(
                    that,
                    defaults
                    ),
                absolutes
                );
            }

        @Override
        public AdqlQueryBase.Limits create(final Long rows, final Long cells, final Long time)
            {
            return new AdqlQueryLimits(
                rows,
                cells,
                time
                );
            }

        @Override
        public AdqlQueryBase.Limits validate(final AdqlQueryBase.Limits that) throws ValidationException
            {
            return that;
            }
        }
    
    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimits()
        {
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimits(final AdqlQueryBase.Limits that)
        {
        if (that != null)
            {
            this.rows = that.rows();
            this.cell = that.cells();
            this.time = that.time();
            }
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimits(final Long rows, final Long cells, final Long time)
        {
        this.rows = rows  ;
        this.cell = cells ;
        this.time = time  ;
        }
    
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OGSA_LIMIT_ROWS_COL = "ogsalimitrows";
    protected static final String DB_OGSA_LIMIT_CELL_COL = "ogsalimitcell";
    protected static final String DB_OGSA_LIMIT_TIME_COL = "ogsalimittime";

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_LIMIT_ROWS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long rows;
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_LIMIT_CELL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long cell;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_LIMIT_TIME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long time;

    @Override
    public Long rows()
        {
        return rows;
        }
    @Override
    public void rows(Long value)
        {
        rows = value;
        }
    @Override
    public Long cells()
        {
        return cell;
        }
    @Override
    public void cells(Long value)
        {
        cell = value;
        }
    @Override
    public Long time()
        {
        return time;
        }
    @Override
    public void time(Long value)
        {
        time = value ;
        }

    /**
     * Update this {@link Limits} with the non-null values from another {@link Limits}.
     * @todo Better null/zero handling.
     *
     */
    public void update(final AdqlQueryBase.Limits that)
        {
        if (that != null)
            {
            if (that.rows() != null)
                {
                this.rows = that.rows();
                }
            if (that.cells() != null)
                {
                this.cell = that.cells();
                }
            if (that.time() != null)
                {
                this.time = that.time();
                }
            }
        }

    @Override
    public AdqlQueryBase.Limits lowest(final AdqlQueryBase.Limits that)
        {
        return lowest(
            this,
            that
            );
        }

    @Override
    public AdqlQueryBase.Limits combine(final AdqlQueryBase.Limits that)
        {
        return combine(
            this,
            that
            );
        }

    /**
     * Create a new Limits containing the lowest set of values from two Limits.
     * If the value from both Limits are not null and not zero, then the lowest (non-zero) value is chosen.
     * If the value from one of the Limits is null or zero and the other is not null or zero, then the non-null non-zero value is chosen.
     * If the values from both of the Limits are null or zero, then the result is {@link NO_VALUE}.
     * @param left  One of the Limits to  compare.
     * @param right One of the Limits to compare.
     * @return A new Limits containing a combination of the lowest values from the two Limits.
     * 
     */
    public static AdqlQueryBase.Limits lowest(final AdqlQueryBase.Limits left, final AdqlQueryBase.Limits right)
        {
        return new AdqlQueryLimits()
            {
            @Override
            public Long rows()
                {
                if ((left != null) && (left.rows() != null) )
                    {
                    if ((right != null) && (right.rows() != null) )
                        {
                        if (left.rows() <=  right.rows())
                            {
                            return left.rows();
                            }
                        else {
                            return right.rows();
                            }
                        }
                    else {
                        return left.rows();
                        }
                    }
                else {
                    if ((right != null) && (right.rows() != null))
                        {
                        return right.rows();
                        }
                    else {
                        return NO_VALUE ;
                        }
                    }
                }

            @Override
            public Long cells()
                {
                if ((left != null) && (left.cells() != null) && (left.cells() != 0L))
                    {
                    if ((right != null) && (right.cells() != null) && (right.cells() != 0L))
                        {
                        if (left.cells() <=  right.cells())
                            {
                            return left.cells();
                            }
                        else {
                            return right.cells();
                            }
                        }
                    else {
                        return left.cells();
                        }
                    }
                else {
                    if ((right != null) && (right.cells() != null))
                        {
                        return right.cells();
                        }
                    else {
                        return NO_VALUE ;
                        }
                    }
                }

            @Override
            public Long time()
                {
                if ((left != null) && (left.time() != null) && (left.time() != 0L))
                    {
                    if ((right != null) && (right.time() != null) && (right.time() != 0L))
                        {
                        if (left.time() <=  right.time())
                            {
                            return left.time();
                            }
                        else {
                            return right.time();
                            }
                        }
                    else {
                        return left.time();
                        }
                    }
                else {
                    if ((right != null) && (right.time() != null))
                        {
                        return right.time();
                        }
                    else {
                        return NO_VALUE ;
                        }
                    }
                }
            };
        }

    /**
     * Create a new Limits containing a combination of values from two Limits.
     * If the value from the first Limits is not null and not zero, then the value from the first Limits is used.
     * If the value from the first Limits is null or zero, then the value from the second Limits is used.
     * If the values from both Limits are null or zero, then the value is {@link NO_VALUE}.
     * @param left  The first Limits to  compare.
     * @param right The second Limits to compare.
     * @return A new Limits containing a combination of the values from the two Limits.
     * 
     */
    public static AdqlQueryBase.Limits combine(final AdqlQueryBase.Limits left, final AdqlQueryBase.Limits right)
        {
        return new AdqlQueryLimits()
            {
            @Override
            public Long rows()
                {
                if ((left != null) && (left.rows() != null))
                    {
                    return left.rows();
                    }
                else if ((right != null) && (right.rows() != null))
                    {
                    return right.rows();
                    }
                else {
                    return NO_VALUE ;
                    }
                }

            @Override
            public Long cells()
                {
                if ((left != null) && (left.cells() != null) && (left.cells() != 0L))
                    {
                    return left.cells();
                    }
                else if ((right != null) && (right.cells() != null))
                    {
                    return right.cells();
                    }
                else {
                    return NO_VALUE ;
                    }
                }

            @Override
            public Long time()
                {
                if ((left != null) && (left.time() != null) && (left.time() != 0L))
                    {
                    return left.time();
                    }
                else if ((right != null) && (right.time() != null))
                    {
                    return right.time();
                    }
                else {
                    return NO_VALUE ;
                    }
                }
            };
        }
    }
