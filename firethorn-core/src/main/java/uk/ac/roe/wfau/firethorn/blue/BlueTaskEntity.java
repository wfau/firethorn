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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.ThreadConversionException;

/**
 * {@link BlueTask} implementation. 
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
    @Service
    public static abstract class Services<TaskType extends BlueTask<?>>
        implements BlueTask.Services<TaskType>
        {
        }

    /**
     * Our {@link BlueTask.Services} instance.
     *
     */
    protected abstract BlueTaskEntity.Services<TaskType> services();

    /**
     * {@link BlueTask.EntityFactory} implementation.
     * 
     */
    @Slf4j
    @Repository
    public abstract static class EntityFactory<TaskType extends BlueTask<?>>
        extends AbstractEntityFactory<TaskType>
        implements BlueTask.EntityFactory<TaskType>
        {

        @Override
        @SelectAtomicMethod
        public TaskType advance(final Identifier ident, final TaskState next)
        throws IdentifierNotFoundException
            {
            return advance(
                ident,
                next,
                0L
                );
            }
        
        @Override
        @SelectAtomicMethod
        public TaskType advance(final Identifier ident, final TaskState next, long timeout)
        throws IdentifierNotFoundException
            {
            log.debug("advance(Identifier, TaskState, long)");
            log.debug("  ident [{}]", ident);
            log.debug("  next  [{}]", next);
            log.debug("  wait  [{}]", timeout);
            TaskType task = select(
        		ident
            	);
            task.advance(
        		next,
        		timeout
        		); 
            return task ;
            }
        }

    /**
     * Our {@link BlueTask.EntityFactory} instance.
     * 
     *
     */
    @Override
    protected abstract BlueTask.EntityFactory<TaskType> factory();

    /**
     * Base class for {@link BlueTaskEntity} task runners.
     * 
     */
    @Slf4j
    @Component
    public static class TaskRunner<TaskType extends BlueTask<?>>
    implements BlueTask.TaskRunner<TaskType>
        {

        @Autowired
        private Services<TaskType> services ;
        protected Services<TaskType> services()
        	{
        	return this.services;
        	}

        @Override
        @UpdateAtomicMethod
        public TaskState thread(final Updator updator)
            {
            log.debug("update(Updator)");
            log.debug("  ident [{}]", updator.ident());
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());

            log.debug("Before execute()");
            final Future<TaskState> future = services.runner().future(
                updator
                );
            log.debug("After execute()");
            
            try {
                log.debug("Before future()");
                final TaskState result = future.get();
                log.debug("After future()");
                log.debug("  result [{}]", result);
                return result ;
                }
//TODO Much better error handling.
            catch (ExecutionException ouch)
                {
                final Throwable cause = ouch.getCause();
                log.error("ExecutionException executing Future [{}][{}]", cause.getClass().getName(), cause.getMessage());
                return TaskState.ERROR;
                }
            catch (InterruptedException ouch)
            	{
                log.error("Interrupted waiting for Future [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                return TaskState.ERROR;
            	}
            }

        @Async
        @Override
        @UpdateAtomicMethod
        public Future<TaskState> future(final Updator updator)
            {
            log.debug("execute(Updator)");
            log.debug("  ident [{}]", updator.ident());
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            return new AsyncResult<TaskState>(
                updator.execute()
                );
            }

        @Override
        @UpdateAtomicMethod
        public TaskType thread(final Creator<TaskType> creator)
            {
            log.debug("create(Creator)");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());

            log.debug("Before execute()");
            final Future<TaskType> future = services.runner().future(
                creator
                );
            log.debug("After execute()");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            
            try {
                log.debug("Before future()");
                final TaskType initial = future.get();
                log.debug("After future()");
                log.debug("  initial [{}]", initial);
                // Convert the entity to the current thread/session
            	final TaskType result = (TaskType) initial.current();
                log.debug("After select()");
                log.debug("  result [{}]", result);
                return result ;
                }
// TODO Much better error handling
            catch (ExecutionException ouch)
                {
                final Throwable cause = ouch.getCause();
                log.error("ExecutionException executing Creator [{}][{}]", cause.getClass().getName(), cause.getMessage());
                return null;
                }
            catch (InterruptedException ouch)
        	    {
                log.error("Interrupted waiting for Creator [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                return null;
        	    }
            catch (ThreadConversionException ouch)
	    	    {
	            log.error("ThreadConversionException [{}]");
	            return null;
	    	    }
            }

        @Async
        @Override
        @UpdateAtomicMethod
        public Future<TaskType> future(final Creator<TaskType> creator)
            {
            log.debug("execute(Creator)");
            log.debug("  thread [{}][{}]", Thread.currentThread().getId(), Thread.currentThread().getName());
            return new AsyncResult<TaskType>(
                creator.create()
                );
// TODO Much better error handling
            }
        }

    /**
     * Our {@link BlueTaskEntity.TaskRunner} instance.
     * 
     */
    protected abstract BlueTask.TaskRunner<TaskType> runner();

    /**
     * {@link Updator} base class.
     * 
     */
    public abstract static class Updator<TaskType extends BlueTaskEntity<?>>
    implements TaskRunner.Updator 
        {
        /**
         * Our initial {@link BlueTask} entity.
         * 
         */
        private TaskType initial;
        
        /**
         * Protected constructor.
         *
         */
        protected Updator(final TaskType initial)
            {
        	this.initial = initial;
            }

        @Override
        public Identifier ident()
            {
            return initial.ident();
            }
        }

    /**
     * Get the entity linked to the current thread.
     * @throws IdentifierNotFoundException 
     *
     */
    @Override
    public TaskType current()
    throws ThreadConversionException
    	{
        log.debug("Selecting current task [{}]", ident());
        try {
			return services().entities().select(
			    ident()
			    );
        	}
        catch (final IdentifierNotFoundException ouch)
        	{
        	log.error("IdentifierNotFound selecting current instance [{}]", ident());
        	throw new ThreadConversionException(
    			ident(),
    			ouch
    			);
        	}
        }

    // TODO Move this to base class
    protected void refresh()
    	{
        log.debug("Refreshing entity [{}]", ident());
        factories().hibernate().refresh(
    		this
    		);    	
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
        this.state = TaskState.EDITING;
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
    private TaskState state;

    @Override
    public TaskState state()
        {
        return this.state;
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

    /**
     * {@link BlueTask.Handle} implementation.
     * 
     */
    protected static class Handle
    implements BlueTask.Handle
        {
        /**
         * Protected constructor.
         * 
         */
        protected Handle(final BlueTask<?> task)
            {
            this.state = task.state();
            this.ident = task.ident().toString();
            }

        private String ident;
        @Override
        public String ident()
            {
            return this.ident;
            }

        private TaskState state;
        @Override
        public TaskState state()
            {
            return this.state;
            }

        /**
         * Update our {@link TaskState} and notify our listeners.
         *
         */
        @Override
        public synchronized void event(final TaskState next)
            {
            log.debug("event(TaskState)");
            log.debug("  state [{}][{}]", this.state, next);
            this.state = next;
            event();
            }

        /**
         * Notify our listeners, and release this handle if completed.
         * 
         */
        protected synchronized void event()
            {
            log.debug("event()");
            log.debug("notify start");
            this.notifyAll();
            log.debug("notify done");
            if (this.state.active() == false)
                {
                log.debug("State not active, removing Handler");
                handles.remove(
                    this.ident
                    );
                }
            }
        }
        
    /**
     * Base class for {@link Listener}. 
     *
     */
    public static abstract class BaseEventListener
    implements BlueTask.Handle.Listener
        {
        /**
         * The maximum Thread wait.
         *  
         */
        private static final long MAX_WAIT = 5000 ;

    	public BaseEventListener()
            {
        	this(
        		MAX_WAIT
        		);
            }

    	public BaseEventListener(long timeout)
            {
        	this.timeout = timeout;
            }

        protected long count = 0 ;
        protected long count()
            {
            return this.count;
            }

        @Override
        public void waitfor(final BlueTask.Handle handle)
            {
            log.debug("waitfor(Handle)");
            log.debug("  state [{}]", handle.state());

            synchronized(handle)
                {
				if (handle.state().active())
    				{
                	while (this.done(handle) == false)
                        {
                        try {
                            log.debug("wait start [{}]", remaining());
	                		this.count++;
                            this.wait(
                        		remaining()
                        		);
                            log.debug("wait done");
                            }
                        catch (Exception ouch)
                            {
                            log.debug("Exception during wait [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                            }
                        }
                    }
                }
            }
        
        private long started = System.currentTimeMillis();
        protected long started()
            {
            return started ;
            }
        
        private long timeout ;
        protected long timeout()
        	{
        	return this.timeout;
        	}

        protected long elapsed()
            {
            return System.currentTimeMillis() - started ;
            }

        protected long remaining()
            {
            return timeout() - elapsed();
            }

        protected boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  elapsed [{}]", elapsed());
            log.debug("  timeout [{}]", timeout());
            if (elapsed() >= timeout())
            	{
            	log.debug("done (elapsed >= timeout)");
            	return true ;
            	}
            else {
            	return false ;
            	}
            }
        }

    public static class AnyEventListener
    extends BaseEventListener
        {
    	public AnyEventListener(long timeout)
            {
            super(
        		timeout
        		);
            }

        @Override
        public boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  count [{}]", count);
            // Skip the first test.
        	if (count != 0)
        		{
            	log.debug("done (count != 0)");
        		return true ;
        		}
            // Check the timeout.
        	else {
        		return super.done(
    				handle
    				);
        		}
            }
        }

    public static class StatusEventListener
    extends BaseEventListener
        {
        public StatusEventListener(final Handle handle, final TaskState next, long timeout)
            {
            this(
                handle.state(),
                next,
    			timeout
                );
            }
       
        public StatusEventListener(final TaskState prev, final TaskState next, long timeout)
            {
            super(
        		timeout
            	);
            this.prev = prev;
            this.next = next;
            }

        protected TaskState prev; 
        protected TaskState next; 

        @Override
        protected boolean done(final BlueTask.Handle handle)
            {
            log.debug("done()");
            log.debug("  prev  [{}]", prev);
            log.debug("  state [{}]", handle.state());
            log.debug("  next  [{}]", next);
            // If the current state is not active
            if (handle.state().active() == false)
        		{
            	log.debug("done - handle state is not active");
        		return true ;
            	}
            // If the state has changed.
            if ((prev != null) && (handle.state() != prev))
        		{
            	log.debug("done - prev state has changed");
        		return true ;
            	}
            // If the next state has been reached. 
            if ((next != null) && (handle.state().ordinal() >= next.ordinal()))
        		{
            	log.debug("done - next state reached");
        		return true ;
            	}
            // Check the timeout.
            else {
            	return super.done(
            		handle
            		);
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
    public static Handle handle(final Identifier ident)
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
        if (ident() != null)
            {
            synchronized (handles)
                {
                final String key = ident().toString();
                final Handle found = handle(key);
                if (found != null)
                    {
                    log.debug("Found existing Handle [{}]", key);
                    return found;
                    }
                else {
                    // Only create a new handle if we are active.
					if (state().active())
						{
	                	log.debug("State is active - Creating new Handle [{}]", key);
	                    final Handle created = newhandle();
	                    handles.put(
	                        created.ident,
	                        created
	                        );
	                    return created;
						}
					else {
	                	log.debug("State is not active - no handle");
	                    return null ;
						}
                    }
                }
            }
        //
        // If created but not saved, ident is null.
        else {
        	log.error("Ident is null - no handle");
            return null ;
            }
        }

    /**
     * Internal state machine transitions.
     * 
     */
    protected void transition(final TaskState next)
        {
        final TaskState prev = this.state;
        log.debug("transition(TaskState)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), next.name());

        if (prev == next)
            {
            log.debug("No-op status change [{}]", next);
            }

        else if (prev == TaskState.EDITING)
            {
            switch (next)
                {
                case READY :
                    accept(next);
                    break ;

                case CANCELLED:
                case FAILED:
                case ERROR:
                    accept(next);
                    break ;

                default :
                    invalid(prev, next);
                }
            }
        
        else if (prev == TaskState.READY)
            {
            switch (next)
                {
                case EDITING:
                    accept(next);
                    break ;

                case QUEUED:
                    accept(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    accept(next);
                    break ;
    
                default :
                    invalid(prev, next);
                }
            }

        else if (prev == TaskState.QUEUED)
            {
            switch (next)
                {
                case RUNNING:
                    accept(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    accept(next);
                    break ;
    
                default :
                    invalid(prev, next);
                }
            }

        else if (prev == TaskState.RUNNING)
            {
            switch (next)
                {
                case COMPLETED:
                    accept(next);
                    break ;
    
                case CANCELLED:
                case FAILED:
                case ERROR:
                    accept(next);
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
     * Accept a valid state transition.
     * 
     */
    private void accept(final TaskState next)
        {
        log.debug("accept(TaskState)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", state().name(), next.name());
        this.state = next ;
        }

    /**
     * Reject an invalid state transition.
     * 
     */
    private void invalid(final TaskState prev, final TaskState next)
        {
        this.state = TaskState.ERROR;
        // TODO Do we notify our listners ?
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        throw new IllegalStateException(
            "Invalid status change [" + prev.name() + "][" + next.name() + "]"
            );
        }

    @Override
    public void advance(final TaskState next)
        {
    	advance(
			next,
			0L
			);
        }

    @Override
    public void advance(final TaskState next, long timeout)
        {
        final TaskState prev = this.state();
        log.debug("advance(TaskState, long)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), next.name());
        log.debug("  timeout [{}]", timeout);

        if (prev == next)
            {
            log.debug("No-op status change [{}]", next);
            }

        else if (prev == TaskState.EDITING)
            {
            switch (next)
                {
                case READY :
                    ready();
                    break ;

                case RUNNING:
                case COMPLETED:
                    running();
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;

                default :
                    reject(prev, next);
                    break;
                }
            }
        
        else if (prev == TaskState.READY)
            {
            switch (next)
                {
                case RUNNING:
                case COMPLETED:
                    running();
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;

                default :
                    reject(prev, next);
                }
            }

        else if (prev == TaskState.QUEUED)
            {
            switch (next)
                {
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else if (prev == TaskState.RUNNING)
            {
            switch (next)
                {
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else {
            reject(prev, next);
            }
        //
        // Wait for the state change to happen.
        this.waitfor(
    		null,
    		next,
    		timeout
    		);
        //
        // Update our Handle and notify any Listeners.
    	this.event(
    		this.state
            );
        }

    /**
     * Update our Handle and notify any Listeners.
     * 
     */
    protected void event()
    	{
    	this.event(
    		this.state
            );
    	}

    /**
     * Update our Handle and notify any Listeners.
     * 
     */
    protected void event(final TaskState state)
    	{
        final Handle handle = this.handle();
        if (handle != null)
            {
            handle.event(
        		state
                );
            }
    	}

    // Probably becomes part of the API
    protected void waitfor(final TaskState prev, final TaskState next, long timeout)
    	{
        log.debug("waitfor(TaskState, long)");
        log.debug("  ident [{}]", ident());
        log.debug("  prev  [{}]", prev);
        log.debug("  next  [{}]", next);
        log.debug("  timeout [{}]", timeout);

		if (timeout <= 0)
			{
			log.debug("Timeout is zero - no wait");
			}
		else {
            //
            // Get the task handle.
            log.debug("Getting task handle");
            final BlueTaskEntity.Handle handle = handle();
            log.debug("  ident [{}]", handle.ident());
            log.debug("  state [{}]", handle.state());

            log.debug("Before waitfor()");
            log.debug("  ident [{}]", this.ident());
            log.debug("  ident [{}]", handle.ident());
            log.debug("  prev  [{}]", prev);
            log.debug("  state [{}]", this.state());
            log.debug("  state [{}]", handle.state());
            log.debug("  next  [{}]", next);
            // Wait until the next state is achieved.
            Handle.Listener listener = new StatusEventListener(
    			prev,
    			next,
    			timeout
                );
            listener.waitfor(
        		handle
        		);
            log.debug("After waitfor()");
            log.debug("  ident [{}]", this.ident());
            log.debug("  ident [{}]", handle.ident());
            log.debug("  prev  [{}]", prev);
            log.debug("  state [{}]", this.state());
            log.debug("  state [{}]", handle.state());
            log.debug("  next  [{}]", next);

            //
            // Update our entity from the DB.
            log.debug("Before refresh()");
            log.debug("  ident [{}]", this.ident());
            log.debug("  state [{}]", this.state());
            factories().hibernate().refresh(
        		this
                );
            log.debug("After refresh()");
            log.debug("  ident [{}]", this.ident());
            log.debug("  state [{}]", this.state());

			}
    	}
    
    /**
     * Prepare our task.
     * Calling {@link #prepare()} in a new {@link Thread} performs the operation in a separate Hibernate {@link Session}.
     * 
     */
    protected void ready()
    	{
        log.debug("Starting ready()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTaskEntity<?>>(this)
                {
                @Override
                public TaskState execute()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) current();
	                    log.debug("Before prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.prepare();
	                    log.debug("After prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
                    	}
                    catch (ThreadConversionException ouch)
	    	    	    {
	    	            log.error("ThreadConversionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());

        log.debug("Refreshing state");
        this.refresh();

        log.debug("Finished ready()");
        log.debug("  state [{}]", state().name());
    	}

    /**
     * Run our task in a new {@link Thread}.
     * 
     */
    protected void running()
        {
        log.debug("Starting running()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTaskEntity<?>>(this)
                {
                @Override
                public TaskState execute()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) current();
	                    log.debug("Before prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.prepare();
	                    log.debug("After prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    //
	                    // If the task is ready.
	                    if (task.state() == TaskState.READY)
	                        {
	                        log.debug("Before execute()");
	                        log.debug("  state [{}]", task.state().name());
	                        task.execute();
	                        log.debug("After execute()");
	                        log.debug("  state [{}]", task.state().name());
	                        }
	                    else {
	//TODO better error handling 
	                    	log.debug("Not READY, skipping execute()");
	                        }
	                    return task.state();
	                    }
                    catch (ThreadConversionException ouch)
	    	    	    {
	    	            log.error("ThreadConversionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());

        log.debug("Refreshing state");
        this.refresh();
        
        log.debug("Finished running()");
        log.debug("  state [{}]", state().name());
        }

    /**
     * Finish our task.
     * 
     */
    protected void finish(final TaskState next)
        {
        log.debug("Starting finish()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", state().name(), next.name());
        services().runner().thread(
            new Updator<BlueTaskEntity<?>>(this)
                {
                @Override
                public TaskState execute()
                    {
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) current();
	                    log.debug("Before change()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.transition(
	                        next
	                        );
	                    log.debug("After change()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
	                    }
                    catch (ThreadConversionException ouch)
	    	    	    {
	    	            log.error("ThreadConversionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    }
                }
            );
        log.debug("Finished thread()");
        log.debug("  state [{}]", state().name());
// TODO we want to merge existing changes 
// TODO we don't want to loose any other changes.
        log.debug("Refreshing state");
        this.refresh();
        
        log.debug("Finished finish()");
        log.debug("  state [{}]", state().name());
        }

    /**
     * Reject an invalid status change.
     * 
     */
    private void reject(final TaskState prev, final TaskState next)
        {
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        // Do nothing.
        }

    }
