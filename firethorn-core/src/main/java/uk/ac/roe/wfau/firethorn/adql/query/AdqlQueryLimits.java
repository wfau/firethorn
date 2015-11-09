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

/**
 * Embeddable implementation of the AdqlQuery.QueryLimits interface.
 * @todo Check that the Hibernate annotations don't have any side effects when used as a POJO
 * @todo combine BaseQueryLimits, AdqlQueryLimits and SimpleQueryLimits
 *
 */
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQueryLimits
extends BaseQueryLimits
implements AdqlQuery.Limits
    {
    
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
    public AdqlQueryLimits(final AdqlQuery.Limits limits)
        {
        this(
            ((limits != null) ? limits.rows()  : null), 
            ((limits != null) ? limits.cells() : null), 
            ((limits != null) ? limits.time()  : null) 
            );
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimits(final Long rows, final Long cells, final Long time)
        {
        this.ogsarows = rows  ;
        this.ogsacell = cells ;
        this.ogsatime = time  ;
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
    }
