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
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;

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
extends AbstractNamedEntity
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
    protected static final String DB_SCAN_TIME_COL = "scantime";


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
    @Deprecated
    protected DateTime scandate()
        {
        return this.scandate;
        }
    @Deprecated
    protected void scandate(final DateTime date)
        {
        this.scandate = date;
        }

    @Column(
        name = DB_SCAN_TIME_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private long scantime ;
    protected long scantime()
        {
        return this.scantime;
        }
    protected void scantime(final long time)
        {
        this.scantime = time;
        }
    /**
     * The interval between scans.
     * Set to 60 seconds for now .. should be much higher and configurable.
     *
     */
    private static final long SCAN_INTERVAL = 1000 * 60 ;
    protected boolean scandue()
        {
        return (this.scantime < (System.currentTimeMillis() - SCAN_INTERVAL));
        }

    /**
     * Scan our metadata.
     *
     */
    public void scantest()
        {
        log.debug("scantest()");
        if (scandue())
            {
            scansync();
            }
        }

    /**
     * Synchronised set of Identifiers for active scans.
     *
     */
    private static Set<String> scanset = Collections.synchronizedSet(
        new HashSet<String>()
        );

    /**
     * Synchronise our metadata scans.
     *
     */
    public void scansync()
        {
        log.debug("scansync()");

        boolean doscan = false ;
        final String ident = this.ident().toString();
        //
        // Sync on the set and check for active scans.
        synchronized (scanset)
            {
            //
            // If we are already scanning, wait for notification.
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
            // If not already scanning, add our Identifier to the Set and run a scan.
            else {
                log.debug("Adding new scan [{}]", ident);
                doscan = true ;
                scanset.add(
                    ident
                    );
                }
            }
        //
        // If we are due to run a scan.
        if (doscan)
            {
            //
            // Run our scan ...
            try {
                log.debug("Running scan [{}]", ident);
                scanimpl();
                scantime(
                    System.currentTimeMillis()
                    );
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

    /**
     * Metadata scan implementation.
     *
     */
    protected abstract void scanimpl();

    }
