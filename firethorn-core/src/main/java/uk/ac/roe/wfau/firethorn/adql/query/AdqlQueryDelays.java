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
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Delays;

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
implements AdqlQuery.Delays
    {
    /**
     * Factory implementation.
     * 
     */
    @Component
    public static class Factory
    implements AdqlQuery.Delays.Factory
        {
        @Override
        public AdqlQuery.Delays create(final Integer first, final Integer every, final Integer last)
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
        log.debug("AdqlQueryDelays()");
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryDelays(final AdqlQuery.Delays delays)
        {
        if (delays != null)
            {
            this.first = delays.first();
            this.every = delays.every();
            this.last  = delays.last();
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
    public void first(Integer value)
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
    public void every(Integer value)
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
    public void last(Integer value)
        {
        last = value;
        }
    }
