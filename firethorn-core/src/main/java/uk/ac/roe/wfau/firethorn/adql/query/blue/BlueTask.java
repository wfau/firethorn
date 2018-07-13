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

import java.util.Map;
import java.util.concurrent.Future;

import org.hibernate.Session;
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.identity.Operation;
import uk.ac.roe.wfau.firethorn.spring.Context;

/**
 * Generic task interface.
 *
 */
public interface BlueTask<TaskType extends BlueTask<?>>
extends NamedEntity
    {

    /**
     * EntityServices interface.
     * 
     */
    public static interface EntityServices<TaskType extends BlueTask<?>>
    extends Entity.EntityServices<TaskType>
        {
        /**
         * Our {@link BlueTask.EntityFactory} instance.
         *
         */
        public BlueTask.EntityFactory<TaskType> entities();

        /**
         * Our {@link BlueTask.TaskRunner} instance.
         * 
         */
        public BlueTask.TaskRunner<TaskType> runner(); 
        
        /**
         * Our {@link Context.Factory} instance.
         * 
         */
        public Context.Factory contexts();        
        }

    /**
     * TaskRunner interface.
     * 
     */
    public static interface TaskRunner<TaskType extends BlueTask<?>>
        {
        /**
         * Public interface for a {@link BlueTask} operator.
         *
         */
        public interface Operator
            {
            /**
             * Get the {@link Operation} for this {@link Thread}.
             * 
             */
            public Operation operation();

            /**
             * Set the {@link Operation} for this {@link Thread}.
             * 
             */
            public void operation(final Operation oper);
            }

        /**
         * Public interface for a {@link BlueTask} updator.
         *
         */
        public interface Updator<TaskType extends BlueTask<?>>
        extends Operator
            {
            /**
             * The {@link BlueTask} {@link Identifier}.
             *
             */
            public Identifier ident();

            /**
             * Execute the step.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public TaskState update()
            throws ProtectionException;
            }

        /**
         * Execute an {@link TaskRunner.Updator} in a new {@link Thread}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public TaskState thread(final Updator<?> updator)
        throws ProtectionException;

        /**
         * Execute an {@link TaskRunner.Updator} in a {@link Future}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Future<TaskState> future(final Updator<?> updator)
        throws ProtectionException;
        
        /**
         * Public interface for a {@link BlueTask} creator.
         *
         */
        public interface Creator<TaskType extends BlueTask<?>>
        extends Operator
            {
            /**
             * Execute the step.
             * @throws HibernateConvertException
             * @throws InvalidStateTransitionException
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public TaskType create()
            throws ProtectionException, InvalidStateTransitionException, HibernateConvertException;

            }

        /**
         * Execute an {@link TaskRunner.Creator} in a new {@link Thread}.
         * <br/> 
         * Running the {@link TaskRunner.Creator} in a new {@link Thread} means that it is run in
         * a new Hibernate {@link Session}, which gets committed to the database when
         * the {@link TaskRunner.Creator} completes its operation.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public TaskType thread(final Creator<TaskType> creator)
        throws InvalidStateTransitionException, ProtectionException;

        /**
         * Execute an {@link TaskRunner.Creator} in a {@link Future}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public Future<TaskType> future(final Creator<TaskType> creator)
        throws ProtectionException ;

        }

    /**
     * EntityFactory interface.
     * 
     */
    public static interface EntityFactory<TaskType extends BlueTask<?>>
    extends Entity.EntityFactory<TaskType>
        {
        /**
         * Select all the available tasks.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<TaskType> select()
        throws ProtectionException;

        /**
         * Advance the {@link TaskState} of a {@link BlueTask}.
         * @param ident The {@link BlueTask} {@link Identifier}. 
         * @param prev The current/previous {@link TaskState}. 
         * @param next The next {@link TaskState} to move to. 
         * @param wait The blocking time in milliseconds. 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public TaskType advance(final Identifier ident, final TaskState prev, final TaskState next, long wait)
        throws ProtectionException, IdentifierNotFoundException, InvalidStateException;
       
        }

    /**
     * The primary {@link BlueTask} status.
     *
     */
    public enum TaskState
    implements Comparable<TaskState>
        {
        EDITING(false),
        READY(false),
        QUEUED(true),
        SENDING(true),
        RUNNING(true),
        COMPLETED(false),
        CANCELLED(false),
        FAILED(false),
        ERROR(false);
        
        private TaskState(boolean active)
            {
            this.active = active;
            }

        private boolean active ;

        /**
         * Check if this is an active state. 
         * @return true if this is an active state.
         * 
         */
        public boolean active()
            {
            return this.active;
            }

        /**
         * Get the next {@link TaskState} in the sequence. 
         * @return The next {@link TaskState} in the sequence.
         * 
         */
        public TaskState next()
        	{
        	return next(this);
        	}

        /**
         * Get the next {@link TaskState} in the sequence. 
         * @param prev The current {@link TaskState}.
         * @return The next {@link TaskState} in the sequence.
         * 
         */
        public static TaskState next(final TaskState prev)
    		{
        	switch(prev)
        		{
        		case EDITING:
        			return READY;
        		case READY:
        			return QUEUED;
        		case QUEUED:
        			return RUNNING;
        		case RUNNING:
        			return COMPLETED;
    			default:
    				return prev;
        		}
    		}
        
        /**
         * Null friendly String parser.
         * 
         */
        public static TaskState parse(final String string)
            {
            if (string == null)
                {
                return null ;
                }
            else {
                return TaskState.valueOf(
                    string
                    );
                }
            }
        };

    /**
     * The task {@link TaskState}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public TaskState state()
    throws ProtectionException;

    /**
     * Advance to the next {@link TaskState},
     * called in response to a user action.
     * @param prev The current/previous {@link TaskState}. 
     * @param next The next {@link TaskState} to move to. 
     * @param wait The blocking time in milliseconds. 
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void advance(final TaskState prev, final TaskState next, final Long wait)
    throws ProtectionException, InvalidStateException;

    /**
     * Wait for a state {@link TaskState} change.
     * @param prev The current/previous {@link TaskState}. 
     * @param prev The next {@link TaskState} to move to. 
     * @param wait The blocking time in milliseconds. 
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void waitfor(final TaskState prev, final TaskState next, final Long wait)
    throws ProtectionException;

    /**
     * An event notification handle.
     * Hopefully abstract enough to enable us to use something like Hazelcast if we needed to.
     * http://hazelcast.com/use-cases/application-scaling/
     *
     */
    public static interface Handle
        {

        /**
         * The task identifier.
         *
         */
        public String ident();

        /**
         * Get the {@link Handle} {@link TaskState}.
         *
         */
        public TaskState state();

        /**
         * Check if this {@link Handle} is sticky.
         * 
         */
        public boolean sticky();

        /**
         * Update our {@link Handle} with a {@link TaskState} event.
         *
         */
        public void event(final TaskState state);

        /**
         * Update our {@link Handle} with a {@link TaskState} event.
         *
         */
        public void event(final TaskState state, final boolean activate);

        /**
         * Event listener interface.
         *
         */
        public static interface Listener
            {
            /**
             * Wait for an event from a {@link Handle}. 
             *
             */
            public void waitfor(final Handle handle);

            }

        /**
         * Handle resolver.
         *
         */
        public static interface Resolver
            {
            public Handle select(final Identifier ident);
            } 
        }

    /**
     * Our {@link BlueTask.Handle}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Handle handle()
    throws ProtectionException;

    /**
     * Get the {@link Entity} instance linked to the current {@link Thread}.
     * @todo Move to a generic base class. 
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public TaskType rebase()
	throws HibernateConvertException;
    
    /**
     * The date/time the {@link BlueTask} was queued.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public DateTime queued()
    throws ProtectionException;

    /**
     * The date/time the {@link BlueTask} was started.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public DateTime started()
    throws ProtectionException;

    /**
     * The date/time the {@link BlueTask} was completed.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public DateTime completed()
    throws ProtectionException;

    /**
     *  Public interface for the task parameters.
     *  
     */
    public interface Param
        {
        /**
         * A {@link Map} of task parameters.
         * 
         */
        public Map<String, String> map()
        throws ProtectionException;

        }

    /**
     *  Access to the {@link BlueTask} parameters.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *  
     */
    public Param param()
    throws ProtectionException;

    /**
     * Public interface for the {@link BlueTask} history.
     * 
     */
    public interface History
        {
        /**
         * Create a new entry.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public BlueTaskLogEntry create(final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new entry.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public BlueTaskLogEntry create(final BlueTask.TaskState state, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new entry.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public BlueTaskLogEntry create(final Object source, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new entry.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public BlueTaskLogEntry create(final Object source, final BlueTask.TaskState state, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Select all the entries for this {@link BlueTask}.
         * @Deprecated Use select(Integer count).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        @Deprecated
        public Iterable<BlueTaskLogEntry> select()
        throws ProtectionException;

        /**
         * Select the most recent entries.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final Integer limit)
        throws ProtectionException;

        /**
         * Select all the entries with a specific level.
         * @Deprecated Use select(Integer count, Level level).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        @Deprecated
        public Iterable<BlueTaskLogEntry> select(final BlueTaskLogEntry.Level level)
        throws ProtectionException;

        /**
         * Select the most recent entries with a specific level.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final Integer limit, final BlueTaskLogEntry.Level level)
        throws ProtectionException;

        }

    /**
     * Access to the {@link History} for this {@link BlueTask}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public History history()
    throws ProtectionException;

    }
