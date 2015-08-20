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

import lombok.extern.slf4j.Slf4j;

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
     * Public constructor.
     * 
     */
    public AdqlQueryDelays()
        {
        log.debug("AdqlQueryDelays()");
        }

    /**
     * Protected constructor.
     * 
     */
    protected AdqlQueryDelays(final Integer first, final Integer every, final Integer last)
        {
        log.debug("AdqlQueryDelays(Integer, Integer, Integer)");
        this.ogsafirst = first ;
        this.ogsaevery = every ;
        this.ogsalast  = last  ;
        }
    
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OGSA_DELAY_FIRST_COL = "ogsadelayfirst";
    protected static final String DB_OGSA_DELAY_LAST_COL  = "ogsadelaylast";
    protected static final String DB_OGSA_DELAY_EVERY_COL = "ogsadelayevery";

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_DELAY_FIRST_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer ogsafirst;

    @Override
    public Integer first()
        {
        return ogsafirst;
        }
    @Override
    public void first(Integer value)
        {
        ogsafirst = value;
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
    private Integer ogsalast;
    
    @Override
    public Integer last()
        {
        return ogsalast;
        }
    @Override
    public void last(Integer value)
        {
        ogsalast = value;
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
    private Integer ogsaevery;

    @Override
    public Integer every()
        {
        return ogsaevery;
        }
    @Override
    public void every(Integer value)
        {
        ogsaevery = value ;
        }
    }
