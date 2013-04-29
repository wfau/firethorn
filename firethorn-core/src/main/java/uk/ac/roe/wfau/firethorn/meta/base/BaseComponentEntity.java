/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.base;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.ReadablePeriod;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 *
 *
 */
@Slf4j
@MappedSuperclass
@Access(
    AccessType.FIELD
    )
public abstract class BaseComponentEntity
extends AbstractEntity
    implements BaseComponent
    {
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_BASE_COL   = "base";
    protected static final String DB_PARENT_COL = "parent";
    protected static final String DB_STATUS_COL = "status";
    protected static final String DB_ALIAS_COL  = "alias";

    protected static final String DB_NAME_IDX        = "IndexByName";
    protected static final String DB_PARENT_IDX      = "IndexByParent";
    protected static final String DB_PARENT_NAME_IDX = "IndexByParentAndName";

    protected static final String DB_RESOURCE_COL = "resource";
    protected static final String DB_SCHEMA_COL   = "schema";
    protected static final String DB_TABLE_COL    = "table";
    protected static final String DB_COLUMN_COL   = "column";

    protected static final String DB_SCAN_DATE_COL = "scandate";
    protected static final String DB_SCAN_FLAG_COL = "scanflag";


    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected BaseComponentEntity()
        {
        super();
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected BaseComponentEntity(final String name)
        {
        super(
            name
            );
        }

    /**
     * The component status.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.CREATED ;

    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public void status(final Status status)
        {
        this.status = status;
        }

    @Column(
        name = DB_SCAN_DATE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime scandate ;
    public DateTime scandate()
        {
        return this.scandate;
        }
    public void scandate(final DateTime date)
        {
        this.scandate = date;
        }

    @Column(
        name = DB_SCAN_FLAG_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private boolean scanflag = true ;
    public boolean scanflag()
        {
        return this.scanflag;
        }
    public void scanflag(final boolean flag)
        {
        this.scanflag = flag;
        }

    private static final ReadablePeriod interval = Hours.hours(2);

    /**
     * Test if we need to scan our metadata.
     *
     */
    public void scan()
        {
        log.debug("scan()");
        scan(true);
        }

    /**
     * Test if we need to scan our metadata.
     *
     */
    public void scan(final boolean force)
        {
        log.debug("scan({})", force);
        this.scanflag |= force ;

        if (this.scanflag)
            {
            this.scansync();
            }
        if (this.scandate == null)
            {
            this.scansync();
            }
        else if (this.scandate.isBefore(this.scandate.minus(interval)))
            {
            this.scansync();
            }
        }

    /**
     * Synchronised set of Identifiers for active scans.
     *
     */
    private static Set<Identifier> scanset = Collections.synchronizedSet(
        new HashSet<Identifier>()
        );

    /**
     * Synchronise our metadata scans.
     *
     */
    protected void scansync()
        {
        log.debug("scansync()");
        boolean    todo  = false ;
        final Identifier ident = this.ident();
        synchronized (scanset)
            {
            //
            // If we are already scanning, wait.
            log.debug("Checking for existing scan [{}]", ident);
            if (scanset.contains(ident))
                {
                do {
                    try {
                        log.debug("Scan wait start [{}]", ident);
                        scanset.wait(1000);
                        log.debug("Scan wait woken [{}]", ident);
                        }
                    catch (final Exception ouch)
                        {
                        log.warn("Scan wait interrupted [{}][{}]", ident, ouch.getMessage());
                        }
                    }
                while (scanset.contains(ident));
                log.debug("Scan wait done [{}]", ident);
                }
            //
            // If we are not already scanning, add our Identifier to the Set and run a scan.
            else {
                log.debug("Adding new scan [{}]", ident);
                todo = true ;
                scanset.add(
                    ident
                    );
                }
            }
        //
        // If we are due to run a scan.
        if (todo)
            {
            //
            // Run our scan ...
            try {
                log.debug("Running scan [{}][{}]", ident);
                scanflag(true);
                scanimpl();
                }
            //
            // Remove ourselves from the Set and notify anyone else waiting.
            finally {
                log.debug("Completed scan [{}]", ident);
                synchronized (scanset)
                    {
                    scanset.remove(
                        ident
                        );
                    scanset.notifyAll();
                    }
                }
            }
        }

    protected void scanimpl()
        {
        log.debug("scanimpl() - stub");
        }
    }
