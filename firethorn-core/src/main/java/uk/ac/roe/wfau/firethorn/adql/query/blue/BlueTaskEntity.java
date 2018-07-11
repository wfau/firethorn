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
package uk.ac.roe.wfau.firethorn.adql.query.blue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.NamedQueries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.Handle.Listener;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTaskLogEntry.Level;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;

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
    protected static final String DB_STATE_COL = "state";

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
        @UpdateAtomicMethod
        public TaskType advance(final Identifier ident, final TaskState prev, final TaskState next, long wait)
        throws IdentifierNotFoundException, InvalidStateException, ProtectionException
            {
            log.debug("advance() [{}][{}][{}][{}]",
                ident,
                prev,
                next,
                wait
                );
            TaskType task = select(
        		ident
            	);
            task.advance(
        		prev,
        		next,
        		wait
        		); 
            return task ;
            }
        }

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
        private Operation.EntityFactory operations ;        
        protected Operation.EntityFactory operations()
            {
            return this.operations;
            }
        
        @Autowired
        private BlueTask.EntityServices<TaskType> services ;
        protected BlueTask.EntityServices<TaskType> services()
        	{
        	return this.services;
        	}

        @Override
        @UpdateAtomicMethod
        public TaskState thread(final Updator<?> updator)
        throws ProtectionException
            {
            log.debug("thread(Updator) [{}]",
                updator.ident()
                );
            updator.operation(
                operations().current()
                );            
            log.trace("Before future() [{}]",
                    updator.ident()
                    );
            final Future<TaskState> future = services.runner().future(
                updator
                );
            log.trace("Afterfuture() [{}]",
                updator.ident()
                );
            
            try {
                log.trace("Before future.get() [{}]",
                    updator.ident()
                    );
                final TaskState result = future.get();
                log.trace("After future.get() [{}][{}]",
                    updator.ident(),
                    result.name()
                    );
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
        public Future<TaskState> future(final Updator<?> updator)
        throws ProtectionException
            {
            log.debug("future(Updator) [{}]",
                updator.ident()
                );
            log.trace("Current operation [{}]", ident(operations().current()));
            operations().current(
                updator.operation()
                );            
            log.trace("Current operation [{}]", ident(operations().current()));
            try {
                return AsyncResult.forValue(
                    updator.update()
                    );
                }
            catch(final Throwable ouch)
                {
                return AsyncResult.forExecutionException(
                    ouch
                    );
                }
            }

        @Override
        @UpdateAtomicMethod
        public TaskType thread(final Creator<TaskType> creator)
        throws InvalidStateTransitionException, ProtectionException
            {
            log.debug("thread(Creator)");
            creator.operation(
                operations().current()
                );            
            log.trace("Before future() [{}]");
            final Future<TaskType> future = services.runner().future(
                creator
                );
            log.trace("After future()");
            try {
                log.trace("Before future.get()");
                final TaskType result = future.get();
                log.trace("After future.get() [{}]", result.ident());
                return result ;
                }
// TODO Much better error handling
            catch (final ExecutionException ouch)
                {
                final Throwable cause = ouch.getCause();
                log.error("ExecutionException executing Creator [{}][{}]", cause.getClass().getName(), cause.getMessage());
				if (cause instanceof InvalidStateTransitionException)
					{
					throw (InvalidStateTransitionException) cause ;
					}
				else {
				    // TODO Creator shouldn't return null ..
					return null;
					}
                }
            catch (final InterruptedException ouch)
        	    {
                log.error("Interrupted waiting for Creator [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                // TODO Creator shouldn't return null ..
                return null;
        	    }
            }

        @Async
        @Override
        @UpdateAtomicMethod
        public Future<TaskType> future(final Creator<TaskType> creator)
        throws ProtectionException
            {
            log.debug("future(Creator)");

            log.trace("Current operation [{}]", ident(operations().current()));
            operations().current(
                creator.operation()
                );            
            log.trace("Current operation [{}]", ident(operations().current()));
            // TODO Much better error handling
            try {
            	return AsyncResult.forValue(
                    creator.create()
        			);
            	}
            catch(final Throwable ouch)
            	{
            	return AsyncResult.forExecutionException(
                    ouch
        			);
            	}
            }
        }

    /**
     * Operator base class.
     * 
     */
    public static abstract class Operator
    implements BlueTask.TaskRunner.Operator
        {
        /**
         * Protected constructor.
         *
         */
        protected Operator()
            {
            }

        /**
         * The {@link Operation} for this {@link Thread}.
         * 
         */
        protected Operation oper ;

        @Override
        public Operation operation()
            {
            return this.oper;
            }

        @Override
        public void operation(Operation oper)
            {
            this.oper = oper;
            }
        }

    /**
     * {@link Creator} base class.
     * 
     */
    public static abstract class Creator<Creatable extends BlueTask<?>>
    extends Operator
    implements BlueTask.TaskRunner.Creator<Creatable>
        {
        /**
         * Protected constructor.
         *
         */
        protected Creator()
            {
            super();
            }
        }
    
    /**
     * {@link Updator} base class.
     * 
     */
    public static abstract class Updator<Updatable extends BlueTask<?>>
    extends Operator
    implements BlueTask.TaskRunner.Updator<Updatable>
        {
        /**
         * Protected constructor.
         *
         */
        protected Updator(final Updatable initial)
            {
            super();
        	this.initial = initial ;
            }

        /**
         * Our initial {@link BlueTask} entity.
         * 
         */
        private Updatable initial;
        
        @Override
        public Identifier ident()
            {
            return initial.ident();
            }
        }
    
    // TODO Move this to base class
    protected void refresh()
    	{
        log.trace("refresh() [{}]", this.ident());
        factories().hibernate().refresh(
    		this
    		);    	
    	}

    // TODO Move this to base class
    protected void flush()
        {
        log.trace("flush() [{}]", this.ident());
        factories().hibernate().flush();
        }
    
    protected abstract BlueTask.TaskRunner<TaskType>     runner();
    protected abstract BlueTask.EntityFactory<TaskType>  factory();
    protected abstract BlueTask.EntityServices<TaskType> services();

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
     * @param owner 
     * 
     */
    protected BlueTaskEntity(final Identity owner)
        {
        this(
            owner,
            null
            );
        }

    /**
     * Protected constructor.
     * @param owner 
     * 
     */
    protected BlueTaskEntity(final Identity owner, final String name)
        {
        super(
    		owner,
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
        name = DB_STATE_COL,
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
    protected void state(final TaskState value)
        {
        if (value.compareTo(this.state) > 0)
            {
            log.trace("Forward state change [{}][{}]->[{}]", this.ident(), this.state(), value);
            this.state = value;
            }
        else {
            log.error("Backward state change [{}][{}]->[{}]", this.ident(), this.state(), value);
            }
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
    protected abstract void prepare()
    throws InvalidStateTransitionException;

    /**
     * Execute our task.
     * 
     */
    protected abstract void execute()
    throws ProtectionException, InvalidStateTransitionException;

    /**
     * {@link BlueTask.Handle} implementation.
     * 
     */
    @Slf4j
    protected static class Handle
    implements BlueTask.Handle
        {
        /**
         * Protected constructor.
         * 
         */
        protected Handle(final BlueTask<?> task)
        throws ProtectionException        
            {
            this.state = task.state();
            this.ident = task.ident().toString();
            }

        private boolean sticky = false ;
        @Override
        public boolean sticky()
            {
            return this.sticky;
            }

        private String ident;
        @Override
        public String ident()
            {
            return this.ident;
            }

        protected TaskState state;
        @Override
        public TaskState state()
            {
            return this.state;
            }

        @Override
        public synchronized void event(final TaskState next)
            {
            this.event(
                next,
                false
                );
            }
        
        @Override
        public synchronized void event(final TaskState next, final boolean sticky)
            {
            log.debug("event() [{}]:[{}]->[{}]:[{}]->[{}]",
                    this.ident,
                    this.state,
                    next,
                    this.sticky,
                    sticky
                    );
            if (next.compareTo(this.state) > 0)
                {
                log.trace("Accepting forward state change [{}]:[{}]->[{}]",
                    this.ident,
                    this.state,
                    next
                    );
                this.state = next;
                }
            else if (next.compareTo(this.state) == 0)
                {
                log.trace("No state change [{}]:[{}]->[{}]",
                    this.ident,
                    this.state,
                    next
                    );
                }
            else {
                log.error("Ignoring backward state change [{}]:[{}]->[{}]",
                    this.ident,
                    this.state,
                    next
                    );
                }
            this.sticky |= sticky ;

            log.trace("Before notify [{}][{}]",
                this.ident,
                this.state
                );
            this.notifyAll();
            log.trace("After notify [{}][{}]",
                this.ident,
                this.state
                );

// Two states, controller and processor.
// Processor updates via callback.
// Controller sets state directly.
// Processor callback releases handle IF controller has completed its processing.

// Controller sets flag to indicate handle can be released.
            
// Controller sets the state to sending
// Controller sends the request
// Callback might happen, running, completed or error.
// Controller checks state and IF still active, updates state to running.
// Controller releases handle IF no longer active..
            
            log.trace("Checking Handler [{}][{}][{}]",
                this.ident,
                this.state,
                this.sticky
                );
            if (this.state.active())
                {
                log.trace("TaskState is active, keeping Handler");
                }
            else {
                log.trace("TaskState is inactive, checking stickiness [{}]", this.sticky);
                if (this.sticky)
                    {
                    log.trace("Handler is sticky, retaining");
                    }
                else {
                    log.trace("Handler is not sticky, releasing");
                    handles.remove(
                        this.ident
                        );
                    }
                }
            }
        }
        
    /**
     * Base class for {@link Listener}. 
     *
     */
    @Slf4j
    public static abstract class BaseEventListener
    implements BlueTask.Handle.Listener
        {
        /**
         * The default timeout.
         * TODO Make this configurable.
         * TODO Provide settings for default and absolute values.
         *  
         */
        private static final long DEFAULT_TIMEOUT = 5000 ;

    	public BaseEventListener()
            {
        	this(
        		DEFAULT_TIMEOUT
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
            log.debug("waitfor(Handle) [{}][{}]",
                handle.ident(),
                handle.state()
                );
            synchronized(handle)
                {
				if (handle.state().active())
    				{
				    log.trace("Handle state is active, starting loop");
    				while (this.test(handle) == false)
                        {
                        log.trace("Handle wait loop [{}][{}][{}]",
                            handle.ident(),
                            handle.state(),
                            this.count
                            );
                        try {
                            this.count++;
                            long time = remaining();
                            if (time == Long.MAX_VALUE)
                                {
                                log.trace("starting wait, no time");
                                handle.wait();
                                }
                            else {
                                log.trace("starting wait [{}]", time);
                                handle.wait(
                                    time
                                    );
                                }
                            }
                        catch (Exception ouch)
                            {
                            log.debug("Exception during wait [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                            }
                        log.trace("Handle wait done [{}][{}][{}]",
                            handle.ident(),
                            handle.state(),
                            this.count
                            );
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
            if (timeout() == Long.MAX_VALUE)
                {
                return Long.MAX_VALUE ;
                }
            else {
                return timeout() - elapsed();
                }
            }

        protected boolean test(final BlueTask.Handle handle)
            {
            log.debug("test() [{}][{}][{}]",
                handle.ident(),
                elapsed(),
                timeout()
                );
            if (timeout() == Long.MAX_VALUE)
                {
                log.trace("timeout is MAX_VALUE [false]");
                return false ;
                }
            else {
                if (elapsed() >= timeout())
                	{
                	log.trace("(elapsed >= timeout) [true]");
                	return true ;
                	}
                else {
                    log.trace("(elapsed < timeout) [false]");
                	return false ;
                	}
                }
            }
        }

    @Slf4j
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
        public boolean test(final BlueTask.Handle handle)
            {
            log.debug("test() [{}][{}]",
                handle.ident(),
                count
                );
            // Skip the first test.
        	if (count != 0)
        		{
            	log.trace("done (count != 0)");
        		return true ;
        		}
            // Check the timeout.
        	else {
        		return super.test(
    				handle
    				);
        		}
            }
        }

    @Slf4j
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
        protected boolean test(final BlueTask.Handle handle)
            {
            log.debug("test() [{}][{}]->[{}]->[{}]",
                handle.ident(),
                this.prev,
                handle.state(),
                this.next
                );
            // If the current state is not active
            if (handle.state().active() == false)
        		{
            	log.trace("Handle state is not active [true]");
        		return true ;
            	}
            else {
                log.trace("Handle state is active, checking for change");
                // If the state has changed.
                if ((prev != null) && (handle.state() != prev))
        		    {
                    log.trace("Current state has changed [true]");
        		    return true ;
            	    }
                // If the next state has been reached. 
                else if ((next != null) && (handle.state().ordinal() >= next.ordinal()))
        		    {
                    log.trace("Current state is after next [true]");
        		    return true ;
            	    }
                // Check the timeout.
                else {
                    return super.test(
            	    	handle
            		    );
                	}
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
     * @throws ProtectionException 
     * 
     */
    protected Handle newhandle()
    throws ProtectionException
        {
        return new Handle(
            this
            );
        }

    @Override
    public Handle handle()
    throws ProtectionException
        {
        log.debug("handle() []", ident());

        if (ident() != null)
            {
            synchronized (handles)
                {
                final String key = ident().toString();
                log.trace("Checking for existing handle [{}]", key);
                final Handle found = handle(key);
                if (found != null)
                    {
                    log.trace("Found existing Handle [{}][{}][{}]",
                        key,
                        found.ident(),
                        found.state()
                        );
                    return found;
                    }
                else {
                    log.trace("No Handle found, checking state [{}]", key);
                    // Only create a new handle if the current state is active.
					if (state().active())
						{
	                	log.trace("State is active, creating new Handle [{}]", key);
	                    final Handle created = newhandle();
	                    handles.put(
	                        created.ident,
	                        created
	                        );
	                    return created;
						}
					else {
	                	log.trace("State is not active, not creating a new Handle");
	                    return null ;
						}
                    }
                }
            }
        //
        // If created but not saved, ident is null.
        else {
        	log.error("Ident is null - no Handle");
            return null ;
            }
        }

    /**
     * Internal state machine transitions.
     * 
     */
    protected void transition(final TaskState next)
    throws InvalidStateTransitionException
        {
        final TaskState prev = this.state;
        log.debug("transition(TaskState)");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", prev.name(), (next != null) ? next.name() : null);

        if (next == null)
            {
            log.debug("Null TaskState, no change");
            }
        else {
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
                    case SENDING:
                    case RUNNING:
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
    
            else if (prev == TaskState.QUEUED)
                {
                switch (next)
                    {
                    case SENDING:
                    case RUNNING:
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
    
            else if (prev == TaskState.SENDING)
                {
                switch (next)
                    {
                    case RUNNING:
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
        }

    /**
     * Accept a valid state transition.
     * 
     */
    protected void accept(final TaskState next)
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
    throws InvalidStateTransitionException
        {
        // this.state = TaskState.ERROR;
        // TODO Should we throw an Exception, or just ignore the transition ?
        throw new InvalidStateTransitionException(
            this,
            prev,
            next
            );
        }
    
    @Override
    public void advance(final TaskState prev, final TaskState next, final Long wait)
    throws InvalidStateException, ProtectionException
        {
        log.debug("advance(TaskState, TaskState, Long)");
        log.debug("  ident [{}]", ident());
        log.debug("  prev  [{}]", prev);
        log.debug("  curr  [{}]", this.state());
        log.debug("  next  [{}]", next);
        log.debug("  wait  [{}]", wait);

        final TaskState current = this.state(); 
        if (current == next)
            {
            log.debug("No-op status change [{}][{}]", current, next);
            }

        else if (current == TaskState.EDITING)
            {
            switch (next)
                {
                case READY :
                    ready();
                    break ;

                case SENDING:
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
        
        else if (current == TaskState.READY)
            {
            switch (next)
                {
                case SENDING:
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
                }
            }

        else if (current == TaskState.QUEUED)
            {
            switch (next)
                {
                case SENDING:
                case COMPLETED:
                    break ;

                case CANCELLED:
                    finish(next);
                    break ;
    
                default :
                    reject(prev, next);
                }
            }

        else if (current == TaskState.SENDING)
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

        else if (current == TaskState.RUNNING)
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
            reject(current, next);
            }
        //
        // Wait for the next state change.
// BUG This keeps a write transaction open for the duration of the wait.
// This should be outside the transaction.
/*
 * 
        this.waitfor(
    		prev,
    		next,
    		wait
    		);
        //
        // Update this instance with the result of the wait.
        this.refresh();
 * 
 */            
        }

    /**
     * Update our Handle and notify any Listeners.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
     * 
     */
    protected void event()
    throws ProtectionException
    	{
    	this.event(
    		false
            );
    	}

    /**
     * Update our Handle and notify any Listeners.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
     * 
     */
    protected void event(boolean sticky)
    throws ProtectionException
    	{
        final Handle handle = this.handle();
        if (handle != null)
            {
            handle.event(
        		this.state,
                sticky
                );
            }
    	}

    @Override
    public void waitfor(final TaskState prev, final TaskState next, final Long wait)
    throws ProtectionException
    	{
        log.debug("waitfor() [{}]:[{}]->[{}]->[{}]:[{}]",
            ident(),
            prev,
            state(),
            next,
            wait
            );
        if (wait == null)
            {
            log.trace("Wait is null - skipping wait");
            }
        else if (wait <= 0)
			{
			// TODO Do we need an absolute max value of -1 for sync TAP requests ?
			log.trace("Wait is <= zero - skipping wait");
			}
		else {
			if (this.state().active())
				{
	            log.trace("State is active - getting handle");
	            final BlueTaskEntity.Handle handle = handle();
	            if (handle != null)
	            	{
                    log.trace("Before listener.waitfor() [{}][{}]:[{}]->[{}][{}]->[{}]",
                        this.ident(),
                        handle.ident(),
                        prev,
                        this.state(),
                        handle.state(),
                        next
                        );
		            //
		            // Wait for a state change event.
					if ((prev != null) || (next != null))
						{
			            log.trace("Waiting for state change event [{}]:[{}]->[{}]:[{}]",
		                    this.ident(),
		                    prev,
		                    next,
		                    wait
			                );
					    final Handle.Listener listener = new StatusEventListener(
							prev,
							next,
							wait
					        );
					    listener.waitfor(
							handle
							);
						}
		            //
		            // Wait any event.
					else {
		            	log.trace("Waiting for any event");
					    final Handle.Listener listener = new AnyEventListener(
							wait
					        );
					    listener.waitfor(
							handle
							);
						}
					log.trace("After listener.waitfor() [{}][{}]:[{}]->[{}][{}]->[{}]",
				        this.ident(),
				        handle.ident(),
				        prev,
				        this.state(),
				        handle.state(),
				        next
				        );
					//
		            // Update our state from the Handle.
		            update(handle);
//
//TODO Remove the sticky flag and possibly release the Handle.
//

    				}
	            else {
	                log.trace("Null handle - skipping wait");
	            	}
				}
			else {
				log.trace("State is inactive - skipping wait");
				}
			}
    	}
    
    /**
     * Update our BlueTask from a Handle.
     * 
     */
    public void update(final BlueTaskEntity.Handle handle)
        {
        log.debug("update(Handle) [{}][{}]:[{}][{}]",
            this.ident(),
            handle.ident(),
            this.state(),
            handle.state()
            );
        if (handle != null)
            {
            log.debug("  handle state [{}]", handle.state());
            if (handle.state().compareTo(this.state()) > 0)
                {
                log.debug("Adopting Handle state [{}][{}]", this.state(), handle.state());
                try {
                    transition(
                        handle.state()
                        );
                    }
                catch (InvalidStateTransitionException ouch)
                    {
                    log.warn("Ignoring invalid state transition [{}][{}]", this.state(), handle.state());
                    } 
                }
            }
        }

    /**
     * Prepare our task.
     * Calling {@link #prepare()} in a new {@link Thread} performs the operation in a separate Hibernate {@link Session}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
     * 
     */
    protected void ready()
    throws ProtectionException
    	{
        log.debug("-- ahtae3Ma eibo4Zoh [{}]", this.ident());
        log.debug("Starting ready()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTaskEntity<TaskType>>(this)
                {
                @Override
                public TaskState update()
                throws ProtectionException
                    {
                    log.debug("-- Fi1Fahpo yui5EiNa [{}]", ident());
                    log.debug("ready.Updator.update()");
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
	                    log.debug("Before prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.prepare();
	                    log.debug("After prepare()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
                    	}
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
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
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
     * 
     */
    protected void running()
    throws ProtectionException
        {
        log.debug("-- Sheejoh7 Xu7zooyo [{}]", this.ident());
        log.debug("Starting running()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}]", state().name());
        services().runner().thread(
            new Updator<BlueTaskEntity<TaskType>>(this)
                {
                @Override
                public TaskState update()
                throws ProtectionException
                    {
                    log.debug("-- yae9iTao adoh9ooW [{}]", ident());
                    log.debug("running.Updator.update()");
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
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
	                    //
	                    // If the task is not ready.
	                    else {
	                    	log.debug("Task is not READY, execute FAILED");
	                        task.transition(
                                TaskState.FAILED
                                );
	                    	}
	                    return task.state();
	                    }
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (final HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
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
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
     * 
     */
    protected void finish(final TaskState next) throws ProtectionException
        {
        log.debug("-- Vavae7Ba cie1oZoh [{}]", this.ident());
        log.debug("Starting finish()");
        log.debug("  ident [{}]", ident());
        log.debug("  state [{}][{}]", state().name(), next.name());
        services().runner().thread(
            new Updator<BlueTaskEntity<TaskType>>(this)
                {
                @Override
                public TaskState update()
                throws ProtectionException
                    {
                    log.debug("-- chieKee1 apePaiy1 [{}]", ident());
                    log.debug("finish.Updator.update()");
                	try {
	                    BlueTaskEntity<?> task = (BlueTaskEntity<?>) rebase();
	                    log.debug("Before change()");
	                    log.debug("  state [{}]", task.state().name());
	                    task.transition(
	                        next
	                        );
	                    log.debug("After change()");
	                    log.debug("  state [{}]", task.state().name());
	                    return task.state();
	                    }
                	catch (final InvalidStateTransitionException ouch)
    	    	    	{
	    	            log.error("InvalidStateTransitionException [{}]", BlueTaskEntity.this.ident());
                		return TaskState.ERROR;
	    	    	    }
                    catch (HibernateConvertException ouch)
	    	    	    {
	    	            log.error("HibernateConvertException [{}]", BlueTaskEntity.this.ident());
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
    throws InvalidStateException
        {
        log.warn("Invalid status change [{}][{}]", prev.name(), next.name());
        throw new InvalidStateException(
    		this,
    		prev,
    		next
    		);
        }

    /**
     * Hibernate embedded map of Strings.
     * http://stackoverflow.com/questions/3393649/storing-a-mapstring-string-using-jpa
     *
     */
    @ElementCollection(
        fetch = FetchType.EAGER
        )
    @MapKeyColumn(
        name="name"
        )
    @Column(
        name="value"
        )
    @CollectionTable(
        name=DB_TABLE_PREFIX + "BlueTaskParam",
        joinColumns= @JoinColumn(name="task")
        )
    private Map<String, String> params = new HashMap<String, String>();
    
    @Override
    public BlueTask.Param param()
        {
        return new BlueTask.Param()
            {
            @Override
            public Map<String, String> map()
                {
                return BlueTaskEntity.this.params;
                }
            };
        }

    @Override
    public History history()
        {
        return new History()
            {
            @Override
            public BlueTaskLogEntry create(Level level, String message)
            throws ProtectionException
                {
                return factories().logger().entities().create(
                    BlueTaskEntity.this,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(Object source, Level level, String message)
            throws ProtectionException
                {
                return factories().logger().entities().create(
                    source,
                    BlueTaskEntity.this,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(final BlueTask.TaskState state, final Level level, final String message)
            throws ProtectionException
                {
                return factories().logger().entities().create(
                    BlueTaskEntity.this,
                    state,
                    level,
                    message
                    );
                }

            @Override
            public BlueTaskLogEntry create(final Object source, final BlueTask.TaskState state, final Level level, final String message)
            throws ProtectionException
                {
                return factories().logger().entities().create(
                    source,
                    BlueTaskEntity.this,
                    state,
                    level,
                    message
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select()
            throws ProtectionException
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Integer limit)
            throws ProtectionException
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    limit
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Level level)
            throws ProtectionException
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    level
                    );
                }

            @Override
            public Iterable<BlueTaskLogEntry> select(final Integer limit, final Level level)
            throws ProtectionException
                {
                return factories().logger().entities().select(
                    BlueTaskEntity.this,
                    limit,
                    level
                    );
                }
            };
        }
    }
