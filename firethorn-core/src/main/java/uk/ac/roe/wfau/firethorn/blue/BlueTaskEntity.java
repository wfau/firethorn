/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.blue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.blue.BlueTask.StatusOne;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateNestedMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateThings;

/**
 *
 *
 */
/**
*
*
*/
@Slf4j
@Entity()
@Access(
   AccessType.FIELD
   )
@Table(
   name = BlueTaskEntity.DB_TABLE_NAME
   )
@Inheritance(
   strategy = InheritanceType.JOINED
   )
@NamedQueries(
       {
       }
   )
public abstract class BlueTaskEntity<TaskType extends BlueTask<?>>
extends AbstractNamedEntity
implements BlueTask<TaskType>
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "BlueTaskEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JOBSTATUS_COL = "jobstatus";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_QUEUED_COL = "queued";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_STARTED_COL = "started";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_COMPLETED_COL = "completed";

    /**
     * {@link BlueTask.Services} implementation.
     * 
     */
    @Component
    public abstract static class Services<TaskType extends BlueTask<?>>
        implements BlueTask.Services<TaskType>
        {
        /**
         * Our {@link BlueTaskEntity.TaskRunner} service.
         * 
         */
        @Autowired
        private BlueTaskEntity.TaskRunner runner;  

        /**
         * Our {@link BlueTaskEntity.TaskRunner} service.
         * 
         */
        protected BlueTaskEntity.TaskRunner runner()
            {
            return this.runner;
            }

        }

    /**
     * Our {@link BlueTask.Services} instance.
     *
     */
    protected abstract BlueTaskEntity.Services<TaskType > services();

    /**
     * {@link BlueTask.EntityFactory} implementation.
     * 
     */
    @Slf4j
    @Component
    public abstract static class EntityFactory<TaskType extends BlueTask<?>>
        extends AbstractEntityFactory<TaskType>
        implements BlueTask.EntityFactory<TaskType>
        {

        @Autowired
        private BlueTaskEntity.TaskRunner runner;  

        @Autowired
        private HibernateThings hibernate ;

        @Override
        @SelectMethod
        public TaskType update(final Identifier ident, final StatusOne next)
        throws IdentifierNotFoundException
            {
            return update(
                ident,
                next,
                0L
                );
            }
        
        @Override
        @SelectMethod
        public TaskType update(final Identifier ident, final StatusOne next, long limit)
        throws IdentifierNotFoundException
            {
            log.debug("update(Identifier, StatusOne, long ");
            log.debug("  ident [{}]", ident);
            log.debug("  state [{}]", next);
            log.debug("  limit [{}]", limit);

            //
            // Load the task.
            log.debug("loading task");
            final TaskType task = select(ident);

            //
            // Run the update in a nested transaction.
            log.debug("Before nested()");
            log.debug("  ident [{}]", task.ident());
            log.debug("  state [{}]", task.one().name());
            runner.nested(
                new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        log.debug("Before update()");
                        log.debug("  ident [{}]", task.ident());
                        log.debug("  state [{}]", task.one().name());
                        task.update(next);
                        log.debug("After update()");
                        log.debug("  ident [{}]", task.ident());
                        log.debug("  state [{}]", task.one().name());
                        }
                    }
                );
            log.debug("After nested()");
            log.debug("  ident [{}]", task.ident());
            log.debug("  state [{}]", task.one().name());

            //
            // IF the task is active.
            if ((task.one().active()) && (limit > 0))
                {
                log.debug("Task is active and limit is > 0");
                log.debug("Getting handle()");
                final BlueTask.Handle handle = task.handle();
    
                log.debug("Before listen()");
                log.debug("  ident [{}]", task.ident());
                log.debug("  state [{}]", task.one().name());
                handle.listen(
                    next,
                    limit
                    );
                log.debug("After listen()");
                log.debug("  ident [{}]", task.ident());
                log.debug("  state [{}]", task.one().name());
                //
                // Update the task.
                log.debug("Before refresh()");
                log.debug("  ident [{}]", task.ident());
                log.debug("  state [{}]", task.one().name());
                hibernate.refresh(
                    task
                    );
                log.debug("After refresh()");
                log.debug("  ident [{}]", task.ident());
                log.debug("  state [{}]", task.one().name());
                }
            else {
                log.debug("Skipping wait");
                }
            //
            // Return the task.
            return task ;
            }

        }

    /**
     * Our {@link BlueTask.EntityFactory} instance.
     *
     */
    @Override
    protected abstract BlueTask.EntityFactory<TaskType > factory();

    /**
     * A nested transaction task runner service.
     * 
     */
    @Slf4j
    @Component
    public static class TaskRunner
    implements BlueTask.TaskRunner
        {
        @Override
        @UpdateAtomicMethod
        public void update(final Runnable task)
            {
            task.run();
            }

        @Override
        @UpdateNestedMethod
        public void nested(final Runnable task)
            {
            task.run();
            }

        /**
         * Our singleton instance.
         * 
         */
        private static TaskRunner instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static TaskRunner instance()
            {
            log.debug("instance()");
            return instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected TaskRunner()
            {
            log.debug("TaskRunner()");
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (BlueTaskEntity.TaskRunner.instance == null)
                {
                BlueTaskEntity.TaskRunner.instance = this ;
                }
            else {
                log.error("Setting TaskRunner.instance more than once");
                throw new IllegalStateException(
                    "Setting TaskRunner.instance more than once"
                    );
                }
            }
        }

    /**
     * Our {@link BlueTaskEntity.TaskRunner} instance.
     * 
     */
    protected static BlueTaskEntity.TaskRunner runner()
        {
        return BlueTaskEntity.TaskRunner.instance;
        }

    /**
     * Protected constructor.
     * 
     */
    protected BlueTaskEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * 
     */
    protected BlueTaskEntity(final String name)
        {
        super(
            name
            );
        this.one = StatusOne.EDITING;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_JOBSTATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private StatusOne one;

    @Override
    public StatusOne one()
        {
        return this.one;
        }

    @Column(
        name = DB_QUEUED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime queued ;
    @Override
    public DateTime queued()
        {
        return this.queued ;
        }

    @Column(
        name = DB_STARTED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime started ;
    @Override
    public DateTime started()
        {
        return this.started ;
        }

    @Column(
        name = DB_COMPLETED_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private DateTime completed ;
    @Override
    public DateTime completed()
        {
        return this.completed ;
        }

    /**
     * Prepare our task.
     * 
     */
    protected abstract void prepare();

    /**
     * Execute our task.
     * 
     */
    protected abstract void execute();

    protected static class Handle
    implements BlueTask.Handle
        {
        /**
         * Protected constructor.
         * 
         */
        protected Handle(final BlueTask<?> task)
            {
            this.one = task.one();
            this.ident = task.ident().toString();
            }

        private String ident;
        @Override
        public String ident()
            {
            return this.ident;
            }

        private StatusOne one;
        @Override
        public StatusOne one()
            {
            return this.one;
            }

        /**
         * Update our status.
         * Notifies all our listeners if this is a change.
         *
         */
        @Override
        public synchronized void one(final StatusOne next)
            {
            log.debug("one(StatusOne)");
            log.debug("  state [{}][{}]", this.one.name(), next.name());
            if (this.one != next)
                {
                this.one = next;
                event();
                }
            }

        /**
         * Notify our listeners, and release this handle if completed.
         * 
         */
        protected synchronized void event()
            {
            log.debug("event()");
            log.debug("notify start");
            notifyAll();
            log.debug("notify done");
            if (this.one.active() == false)
                {
                log.debug("State not active, removing Handler");
                handles.remove(
                    this.ident
                    );
                }
            }
        
        @Override
        public void listen()
            {
            log.debug("listen()");
            listen(
                new AnyEventListener(
                    this
                    )
                );
            }

        @Override
        public void listen(long limit)
            {
            log.debug("listen(long)");
            listen(
                new AnyEventListener(
                    this
                    ),
                limit
                );
            }

        @Override
        public void listen(final StatusOne prev, long limit)
            {
            log.debug("listen(StatusOne, long)");
            listen(
                new StatusEventListener(
                    prev
                    ),
                limit
                );
            }

        @Override
        public synchronized void listen(final Listener listener)
            {
            log.debug("listen(Listener)");
            while (listener.check(this))
                {
                try {
                    log.debug("wait start");
                    wait();                    
                    log.debug("wait done");
                    }
                catch (Exception ouch)
                    {
                    log.debug("Exception during wait [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                    }
                }
            }

        @Override
        public void listen(final Listener listener, long limit)
            {
            log.debug("listen(Listener, long)");
            while (listener.check(this))
                {
                try {
                    log.debug("wait start");
                    wait(limit);                    
                    log.debug("wait done");
                    }
                catch (Exception ouch)
                    {
                    log.debug("Exception during wait [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                    }
                }
            }

        protected static abstract class BaseListener
        implements Listener
            {
            protected BaseListener()
                {
                }

            protected long count = 0 ;
            @Override
            public long count()
                {
                return this.count;
                }

            private long start = System.currentTimeMillis();
            @Override
            public long time()
                {
                return System.currentTimeMillis() - start ;
                }
            }

        protected static class AnyEventListener
        extends BaseListener
        implements Listener
            {
            protected AnyEventListener(Handle handle)
                {
                super();
                }

            @Override
            public boolean check(BlueTask.Handle handle)
                {
                log.debug("check(BlueTask.Handle)");
                log.debug("  count [{}]", count);
                this.count++;
                return (this.count <= 1);
                }
            }

        protected static class StatusEventListener
        extends BaseListener
        implements Listener
            {
            protected StatusEventListener(final Handle handle)
                {
                this(
                    handle.one()
                    );
                }

            protected StatusEventListener(final StatusOne prev)
                {
                super();
                this.prev = prev;
                }
            protected StatusOne prev; 

            @Override
            public boolean check(BlueTask.Handle handle)
                {
                log.debug("check(BlueTask.Handle)");
                log.debug("  count [{}]", count);
                log.debug("  state [{}][{}]", this.prev.name(), handle.one().name());
                this.count++;
                return (handle.one() == this.prev);
                }
            }
        }
    
    /**
     * Our map of active {@link Handle}s.
     * 
     */
    protected static Map<String, BlueTaskEntity.Handle> handles = new HashMap<String, BlueTaskEntity.Handle>();

    /**
     * Resolve an active {@link Handle}.
     * 
     */
    public static BlueTaskEntity.Handle handle(final Identifier ident)
        {
        return handle(
            ident.toString()
            );
        }

    /**
     * Resolve an active {@link Handle}.
     * 
     */
    public static Handle handle(final String key)
        {
        return handles.get(
            key
            );
        }

    /**
     * Create a {@link Handle} for this task.
     * 
     */
    protected Handle newhandle()
        {
        return new Handle(
            this
            );
        }

    @Override
    public Handle handle()
        {
        log.debug("handle()");
        log.debug("  ident [{}]", ident());
        //
        // If created but not saved, ident is null.
        if (ident() != null)
            {
            // Check if we are active, else return null.
            synchronized (handles)
                {
                final String key = ident().toString();
                final Handle found = handles.get(key);
                if (found != null)
                    {
                    log.debug("Found existing Handle [{}]", key);
                    return found;
                    }
                else {
                    log.debug("Creating new Handle [{}]", key);
                    final Handle created = newhandle();
                    handles.put(
                        created.ident,
                        created
                        );
                    return created;
                    }
                }
            }
        else {
            return null ;
            }
        }

    /**
     * Internal state machine transitions.
     * 
     */
    protected synchronized void change(final StatusOne next)
        {
        final StatusOne prev = this.one;
        log.debug("change(StatusOne)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), next.name());

        if (prev == next)
            {
            log.debug("No-op status change [{}]", next);
            }

        else if (prev == StatusOne.EDITING)
            {
            switch (next)
                {
                case READY :
                    valid(next);
                    break ;

                case CANCELLED:
                case FAILED:
                case ERROR:
                    valid(next);
                    break ;

                default :
                    invalid(prev, next);
                }
            }
        
        else if (prev == StatusOne.READY)
            {
            switch (next)
                {
                case EDITING:
                    valid(next);
                    break ;

                case PENDING:
                    valid(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    valid(next);
                    break ;
    
                default :
                    invalid(prev, next);
                }
            }

        else if (prev == StatusOne.PENDING)
            {
            switch (next)
                {
                case RUNNING:
                    valid(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    valid(next);
                    break ;
    
                default :
                    invalid(prev, next);
                }
            }

        else if (prev == StatusOne.RUNNING)
            {
            switch (next)
                {
                case COMPLETED:
                    valid(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    valid(next);
                    break ;
    
                default :
                    invalid(prev, next);
                }
            }

        else {
            invalid(prev, next);
            }
        }

    /**
     * Accept a valid status change.
     * 
     */
    private void valid(final StatusOne next)
        {
        log.debug("valid(StatusOne)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", one().name(), next.name());
        this.one = next ;
        final Handle handle = this.handle();
        if (handle != null)
            {
            handle.one(next);
            }
        }

    /**
     * Reject an invalid status change.
     * 
     */
    private void invalid(final StatusOne prev, final StatusOne next)
        {
        this.one = StatusOne.ERROR;
        // Do we notify our listners ?
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        throw new IllegalStateException(
            "Invalid status change [" + prev.name() + "][" + next.name() + "]"
            );
        }

    @Override
    public void update(final StatusOne next)
        {
        final StatusOne prev = this.one;
        log.debug("update(StatusOne)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), next.name());

        if (prev == next)
            {
            log.debug("No-op status change [{}]", next);
            }

        else if (prev == StatusOne.EDITING)
            {
            switch (next)
                {
                case READY :
                    prepare();
                    break ;

                case RUNNING:
                    running();
                    break ;

                case COMPLETED:
                case CANCELLED:
                case FAILED:
                    cancel(next);
                    break ;

                default :
                    reject(prev, next);
                }
            }
        
        else if (prev == StatusOne.READY)
            {
            switch (next)
                {
                case RUNNING:
                    running();
                    break ;

                case COMPLETED:
                case CANCELLED:
                case FAILED:
                    cancel(next);
                    break ;

                default :
                    reject(prev, next);
                }
            }

        else if (prev == StatusOne.PENDING)
            {
            switch (next)
                {
                case COMPLETED:
                case CANCELLED:
                case FAILED:
                    cancel(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else if (prev == StatusOne.RUNNING)
            {
            switch (next)
                {
                case COMPLETED:
                case CANCELLED:
                case FAILED:
                    cancel(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else {
            reject(prev, next);
            }
        }

    /**
     * Reject an invalid status change.
     * 
     */
    private void reject(final StatusOne prev, final StatusOne next)
        {
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        // Do nothing.
        }

    /**
     * Prepare our task.
     * 
     */
    protected void ready()
        {
        log.debug("ready()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", one().name());
        log.debug("Before prepare()");
        runner().update(
            new Runnable(){
                @Override
                public void run()
                    {
                    log.debug("Calling prepare inside Runnable()");
                    prepare();
                    }
                }
            );            
        log.debug("After prepare()");
        log.debug("  state [{}]", one().name());
        }

    /**
     * Run our task.
     * 
     */
    protected void running()
        {
        log.debug("running()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", one().name());

        // Prepare our task.
        log.debug("Before prepare()");
        runner().nested(
            new Runnable(){
                @Override
                public void run()
                    {
                    log.debug("Calling prepare() inside Runnable()");
                    prepare();
                    }
                }
            );            
        log.debug("After prepare()");
        log.debug("  state [{}]", one().name());
        //
        // If the task is ready.
        if (this.one == StatusOne.READY)
            {
            log.debug("Before execute()");
            runner().nested(
                new Runnable(){
                    @Override
                    public void run()
                        {
                        log.debug("Calling execute() inside Runnable()");
                        execute();
                        }
                    }
                );            
            log.debug("After execute()");
            log.debug("  state [{}]", one().name());
            }
        else {
            log.debug("Not READY, skipping execute()");
            }
        }

    /**
     * Cancel our task.
     * 
     */
    protected void cancel(final StatusOne next)
        {
        log.debug("cancel()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", one().name(), next.name());

        log.debug("Before cancel()");
        runner().update(
            new Runnable(){
                @Override
                public void run()
                    {
                    log.debug("Changing status inside Runnable()");
                    change(
                        next
                        );
                    }
                }
            );            
        log.debug("After cancel()");
        log.debug("  state [{}]", one().name());
        }

    }
