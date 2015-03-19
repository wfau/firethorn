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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Period;

import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.exception.FirethornUncheckedException;

/**
 * Base class for a metadata component.
 *
 *
 */
@Slf4j
@Access(
    AccessType.FIELD
    )
@MappedSuperclass
public abstract class BaseComponentEntity<ComponentType extends BaseComponent>
extends AbstractNamedEntity
implements BaseComponent
    {
    /**
     * Hibernate column mapping.
     *
     */
    public static final String DB_STATUS_COL = "status";

    protected static final String DB_SCAN_TIME_COL   = "scantime";
    protected static final String DB_SCAN_PERIOD_COL = "scanperiod";
    
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
        this(
            name,
            DEFAULT_SCAN_PERIOD
            );
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected BaseComponentEntity(final String name, final Period scanperiod)
        {
        super(
            name
            );
        this.scanperiod = scanperiod;
        log.debug("BaseComponentEntity(String, Period)");
        log.debug("  Name  [{}]", name);
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

    /**
     * Exception thrown if loading self() fails.
     * 
     */
    public static class SelfException
    extends FirethornUncheckedException
        {
        /**
         * Generated serial version UID.
         *
         */
        private static final long serialVersionUID = -8936523546194517863L;

        /**
         * Protected constructor.
         * 
         */
        protected SelfException(final Class<?> clazz, final Identifier ident)
            {
            this.clazz = clazz;
            this.ident = ident;
            }

        /**
         * The Entity class.
         * 
         */
        private Class<?> clazz ;

        /**
         * The Entity class.
         * 
         */
        public Class<?> clazz()
            {
            return this.clazz;
            }

        /**
         * The Entity Identifier.
         * 
         */
        private Identifier ident ;

        /**
         * The Entity Identifier.
         * 
         */
        public Identifier ident()
            {
            return this.ident;
            }
        }
    
    /**
     * Load a persistent reference for this Entity.
     * @return The persistent instance or proxy for the entity.
     * @see Session#load(Class, java.io.Serializable)
     * @todo Check for recursion.
     *
     */
    protected ComponentType self()
        {
        log.trace("Loading proxy for self");
        @SuppressWarnings("unchecked")
        final ComponentType entity = (ComponentType) factories().hibernate().session().load(
            this.getClass(),
            this.ident().value()
            );
        if (entity != null)
            {
            return entity ;
            }
        else {
            throw new SelfException(
                this.getClass(),
                this.ident()
                );
            }
        }

    /**
     * Synchronized Map of scan locks.
     *
     */
    private static Map<Identifier, Object> locks = new HashMap<Identifier, Object>();

    /**
     * Check for an existing lock, or create a new one.
     *
     */
    protected Object lock(boolean create)
        {
        log.debug("Checking for existing lock [{}][{}]", this.ident(), this.name());
        synchronized (locks)
            {
            Object lock = locks.get(
                this.ident()
                );
            if (lock != null)
                {
                log.debug("Found existing lock [{}][{}][{}]", this.ident(), this.name(), lock);
                return lock;
                }
            else {
                log.debug("No existing lock found [{}][{}]", this.ident(), this.name());
                if (create)
                    {
                    lock = new DateTime();
                    log.debug("Adding new lock [{}][{}][{}]", this.ident(), this.name(), lock);
                    locks.put(
                        this.ident(),
                        lock
                        );
                    }
                return null ;
                }
            }
        }

    /**
     * Release waiting locks.
     * 
     */
    protected void unlock()
        {
        log.debug("Releasing locks [{}][{}]", this.ident(), this.name());
        synchronized (locks)
            {
            Object lock = locks.get(
                this.ident()
                );
            if (lock != null)
                {
                log.debug("Found existing lock [{}][{}][{}]", this.ident(), this.name(), lock);
                log.debug("Removing ....");
                locks.remove(this.ident());
                log.debug("Notifying ....");
                synchronized (lock)
                    {
                    lock.notifyAll();
                    }
                }
            else {
                log.debug("No locks found [{}][{}]", this.ident(), this.name());
                }
            }
        }

    /**
     * The default re-scan interval.
     * 
     */
    protected static final Period DEFAULT_SCAN_PERIOD = new Period(0, 10, 0, 0);

    /**
     * The lock timeout.
     * 
     */
    protected static final long LOCK_TIMEOUT = 500 ;
    
    /**
     * The data/time of the last scan.
     * 
     */
    @Column(
        name = DB_SCAN_TIME_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime scantime ;
    protected DateTime scantime()
        {
        return this.scantime;
        }
    protected void scandate(final DateTime time)
        {
        this.scantime = time;
        }
    
    /**
     * The scan refresh period.
     * 
     */
    @Column(
        name = DB_SCAN_PERIOD_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private Period scanperiod = DEFAULT_SCAN_PERIOD ;
    protected Period scanperiod()
        {
        return this.scanperiod;
        }
    protected void scanperiod(final Period period)
        {
        this.scanperiod = period;
        }
    
    /**
     * Check the scan criteria and scan.
     *
     */
    protected void scantest()
        {
        log.debug("scantest for [{}][{}]", this.ident(), this.name());
        log.debug("scandate [{}]", scantime);
        if ((scantime == null) || (scantime.plus(scanperiod).isBeforeNow()))
            {
            log.debug("scandate is in the past");
            Object lock = lock(true);
            if (lock == null)
                {
                try {
                    log.debug("Running scan [{}][{}]", this.ident(), this.name());
                    scanimpl();
                    }
                finally {
                    scantime = new DateTime();
                    unlock();
                    }
                }
            else {
                log.debug("Found lock [{}][{}][{}]", this.ident(), this.name(), lock);
                while (lock != null)
                    {
                    try {
                        log.debug("Waiting on lock [{}][{}][{}]", this.ident(), this.name(), lock);
                        synchronized (lock)
                            {
                            lock.wait(LOCK_TIMEOUT);
                            }
                        }
                    catch (Exception ouch)
                        {
                        log.debug("Interrupted [{}][{}][{}][{}]", this.ident(), this.name(), lock, ouch.getMessage());
                        }
                    lock = lock(false);
                    }
                log.debug("No more locks [{}][{}]", this.ident(), this.name());
                }
            }
        else {
            log.debug("scandate is recent [{}][{}]", this.ident(), this.name());
            }
        }

    /**
     * Metadata scan implementation.
     *
     */
    protected abstract void scanimpl();

    }
