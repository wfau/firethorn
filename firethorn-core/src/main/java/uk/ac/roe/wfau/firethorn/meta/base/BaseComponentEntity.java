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
import javax.persistence.Transient;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.exception.FirethornUncheckedException;

/**
 * {@link BaseComponent} implementation.
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

    protected static final String DB_SCAN_DATE_COL   = "scandate";
    protected static final String DB_SCAN_PERIOD_COL = "scanperiod";

    /**
     * {@link BaseComponent.EntityFactory} implementation.
     *
     */
    @Slf4j
    @Repository
    public static abstract class EntityFactory<ComponentType extends BaseComponent>
    extends AbstractEntityFactory<ComponentType>
    implements BaseComponent.EntityFactory<ComponentType>
        {
        
        /**
         * The default re-scan interval, as string.
         * Initialised from a configuration property.
         * 
         */
        @Value("${firethorn.meta.scan:PT24H}")
        private String scanstring ;

        /**
         * The default re-scan interval, as a time period.
         * 
         */
        private Period scanperiod ;

        @Override
        public Period scanperiod()
            {
            if (this.scanperiod == null)
                {
                if (this.scanstring != null)
                    {
                    this.scanperiod = new Period(
                        this.scanstring
                        );
                    }
                }
            log.debug("scanperiod()");
            log.debug("  scanstring [{}]", scanstring);
            log.debug("  scanperiod [{}]", scanperiod);
            return scanperiod ;
            }
        }

    /**
     * Reference to our parent {@link BaseComponent.EntityFactory}.
     * 
    @Transient
    protected BaseComponent.EntityFactory<ComponentType> factory;

    @Override
    public BaseComponent.EntityFactory<ComponentType> factory()
        {
        return null;
        }
     */
    
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
     * The lock timeout.
     * 
     */
    protected static final long LOCK_TIMEOUT = 500 ;
    
    /**
     * The data/time of the last scan.
     * 
     */
    @Column(
        name = DB_SCAN_DATE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime scandate ;
    protected DateTime scandate()
        {
        return this.scandate;
        }
    protected void scandate(final DateTime date)
        {
        this.scandate = date;
        }
    
    /**
     * The scan refresh period.
     * 
     */
    @Column(
        name = DB_SCAN_PERIOD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Period scanperiod ;
    protected Period scanperiod()
        {
        if (this.scanperiod != null)
            {
            return this.scanperiod;
            }
        else {
            BaseComponent.EntityFactory<?> factory = (BaseComponent.EntityFactory<?>) this.factory();
            if (factory != null)
                {
                return factory.scanperiod();
                }
            else {
                return null ;
                }
            }
        }
    protected void scanperiod(final Period period)
        {
        this.scanperiod = period;
        }
    
    /**
     * Check the date of the last scan.
     * If the last scan was before the scan period, then initiate a new scan. 
     *
     */
    protected void scantest()
        {
        log.debug("scantest for [{}][{}]", this.ident(), this.name());

        DateTime prev   = scandate()   ; 
        Period   period = scanperiod() ;

        log.debug("prevscan   [{}]", prev);
        log.debug("scanperiod [{}]", period);

        if (prev == null)
            {
            log.debug("prev scan is null - scanning");
            scanlock();
            }
        else if (period == null)
            {
            log.debug("scan period is null - skipping");
            }
        else if (prev.plus(period).isBeforeNow())
            {
            log.debug("scan period expired - scanning");
            scanlock();
            }
        else {
            log.debug("prev scan is recent - skipping");
            }
        }
    
    /**
     * Check for a lock and start a scan.
     *
     */
    protected void scanlock()
        {
        log.debug("scanlock for [{}][{}]", this.ident(), this.name());

        Object lock = lock(true);
        if (lock == null)
            {
            try {
                log.debug("Running scan [{}][{}]", this.ident(), this.name());
                scanimpl();
                }
            finally {
                scandate = new DateTime();
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
    
    /**
     * Metadata scan implementation.
     *
     */
    protected abstract void scanimpl();

    }
