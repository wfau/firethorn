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
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;

/**
 * Implementation of the query timing statistics.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AdqlQueryTimings
implements AdqlQuery.Timings
    {

    /**
     * Protected constructor.
     * 
     */
    protected AdqlQueryTimings()
        {
        log.debug("AdqlQueryStats()");
        }

    /**
     * Protected constructor.
     * @param init A flag to distinguish this from the default constructor.
     * 
     */
    protected AdqlQueryTimings(boolean init)
        {
        log.debug("AdqlQueryStats(boolean)");
        if (init)
            {
            start();
            }
        }

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_TIMING_START_COL = "timingstart" ;
    protected static final String DB_TIMING_TOTAL_COL = "timingtotal" ;
    protected static final String DB_TIMING_ADQL_COL  = "timingadql"  ;
    protected static final String DB_TIMING_JDBC_COL  = "timingjdbc"  ;
    protected static final String DB_TIMING_OGSA_COL  = "timingogsa"  ;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TIMING_START_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long querystart;
    @Override
    public Long start()
        {
        return this.querystart ;
        }
    public void querystart()
        {
        log.info("TIMING - querystart()");
        this.querystart = System.currentTimeMillis();
        this.querydone  = null;
        this.querytime  = null;
        }

    @Transient
    private Long querydone;
    public void querydone()
        {
        log.info("TIMING - querydone()");
        if (this.querystart != null)
            {
            this.querydone = System.currentTimeMillis();
            this.querytime = this.querydone - this.querystart ;
            }
        else {
            this.querydone = null;
            this.querytime = null;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TIMING_TOTAL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long querytime;
    @Override
    public Long total()
        {
        return this.querytime;
        }
    
    @Transient
    private Long adqlstart;
    public void adqlstart()
        {
        log.info("TIMING - adqlstart()");
        this.adqlstart = System.currentTimeMillis();
        this.adqldone  = null ;
        this.adqltime  = null ;
        }

    @Transient
    private Long adqldone;
    public void adqldone()
        {
        log.info("TIMING - adqldone()");
        if (this.adqlstart != null)
            {
            this.adqldone = System.currentTimeMillis();
            this.adqltime = this.adqldone - this.adqlstart ;
            }
        else {
            this.adqldone = null ;
            this.adqltime = null ;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TIMING_ADQL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long adqltime;
    @Override
    public Long adql()
        {
        return this.adqltime;
        }
    
    @Transient
    private Long jdbcstart;
    public void jdbcstart()
        {
        log.info("TIMING - jdbcstart()");
        this.jdbcstart = System.currentTimeMillis();
        this.jdbcdone  = null ;
        this.jdbctime  = null ;
        }

    @Transient
    private Long jdbcdone;
    public void jdbcdone()
        {
        log.info("TIMING - jdbcdone()");
        if (this.jdbcstart != null)
            {
            this.jdbcdone = System.currentTimeMillis();
            this.jdbctime = this.jdbcdone - this.jdbcstart ;
            }
        else {
            this.jdbcdone = null ;
            this.jdbctime = null ;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TIMING_JDBC_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long jdbctime;
    @Override
    public Long jdbc()
        {
        return this.jdbctime;
        }

    @Transient
    private Long ogsastart;
    public void ogsastart()
        {
        log.info("TIMING - ogsastart()");
        this.ogsastart = System.currentTimeMillis();
        this.ogsadone  = null ;
        this.ogsatime  = null ;
        }

    @Transient
    private Long ogsadone;
    public void ogsadone()
        {
        log.info("TIMING - ogsadone()");
        if (ogsastart != null)
            {
            this.ogsadone = System.currentTimeMillis();
            this.ogsatime = this.ogsadone - this.ogsastart ;
            }
        else {
            this.ogsadone = null ;
            this.ogsatime = null ;
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TIMING_OGSA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long ogsatime;
    @Override
    public Long ogsa()
        {
        return this.ogsatime;
        }
    }
