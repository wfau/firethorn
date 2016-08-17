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

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Delays;

/**
 * Implementation of the query delays.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQueryDelays
implements Delays
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
    implements Delays.Factory
        {
        @Override
        public Delays create(final Integer first, final Integer every, final Integer last)
            {
            return new AdqlQueryDelays(
                first,
                every,
                last
                );
            }
        }
    
    /**
     * Public constructor.
     * 
     */
    public AdqlQueryDelays()
        {
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryDelays(final Delays that)
        {
        if (that != null)
            {
            this.first = that.first();
            this.every = that.every();
            this.last  = that.last();
            }
        }

    /**
     * Protected constructor.
     * 
     */
    protected AdqlQueryDelays(final Integer first, final Integer every, final Integer last)
        {
        log.debug("AdqlQueryDelays(Integer, Integer, Integer)");
        log.debug("  first [{}]", first);
        log.debug("  every [{}]", every);
        log.debug("  last  [{}]", last);
        this.first = first ;
        this.every = every ;
        this.last  = last  ;
        }
    
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OGSA_DELAY_FIRST_COL = "ogsadelayfirst";
    protected static final String DB_OGSA_DELAY_EVERY_COL = "ogsadelayevery";
    protected static final String DB_OGSA_DELAY_LAST_COL  = "ogsadelaylast";

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_DELAY_FIRST_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer first;

    @Override
    public Integer first()
        {
        return first;
        }
    @Override
    public void first(final Integer value)
        {
        first = value;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_DELAY_EVERY_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer every;

    @Override
    public Integer every()
        {
        return every;
        }
    @Override
    public void every(final Integer value)
        {
        every = value ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_DELAY_LAST_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer last;
    
    @Override
    public Integer last()
        {
        return last;
        }
    @Override
    public void last(final Integer value)
        {
        last = value;
        }

    /**
     * Update this {@link Delays} with the non-null values from another {@link Delays}.
     *
     */
    public void update(final Delays that)
        {
        if (that != null)
            {
            if (that.first() != null)
                {
                this.first = that.first();
                }
            if (that.every() != null)
                {
                this.every = that.every();
                }
            if (that.last() != null)
                {
                this.last= that.last();
                }
            }
        }
    }
