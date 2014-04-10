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
 * Implementation of the query limits.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQueryLimits
implements AdqlQuery.Limits
    {
    /**
     * Protected constructor.
     * 
     */
    protected AdqlQueryLimits()
        {
        log.debug("AdqlQueryLimits()");
        }

    /**
     * Protected constructor.
     * 
     */
    protected AdqlQueryLimits(final Long rows, final Long cell, final Long time)
        {
        log.debug("AdqlQueryLimits(Long, Long, Long)");
        this.ogsarows = rows ;
        this.ogsacell = cell ;
        this.ogsatime = time ;
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
    private Long ogsarows;
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_LIMIT_CELL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long ogsacell;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_OGSA_LIMIT_TIME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long ogsatime;

    @Override
    public OgsaLimits ogsa()
        {
        return new OgsaLimits()
            {
            @Override
            public Long rows()
                {
                return ogsarows;
                }
            @Override
            public void rows(Long value)
                {
                ogsarows = value;
                }
            @Override
            public Long cells()
                {
                return ogsacell;
                }
            @Override
            public void cells(Long value)
                {
                ogsacell = value;
                }
            @Override
            public Long time()
                {
                return ogsatime;
                }
            @Override
            public void time(Long value)
                {
                ogsatime = value ;
                }
            };
        }
    }
