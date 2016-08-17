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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Delays;

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
public class AdqlQueryLimitEntity
extends BaseQueryLimits
implements AdqlQuery.Limits
    {
    
    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimitEntity()
        {
        }

    /**
     * Public constructor.
     * 
     */
    public AdqlQueryLimitEntity(final AdqlQuery.Limits that)
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
    public AdqlQueryLimitEntity(final Long rows, final Long cells, final Long time)
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
    public void update(final AdqlQuery.Limits that)
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
    }
