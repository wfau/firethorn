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

import java.util.concurrent.Future;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskRunner.Updator;
import uk.ac.roe.wfau.firethorn.blue.BlueTaskEntity.TaskRunner.BaseUpdator;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateNestedMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;

/**
 * Generic job.
 *
 */
public interface BlueTask<TaskType extends BlueTask<?>>
extends NamedEntity
    {
    /**
     * Services interface.
     * 
     */
    public static interface Services<TaskType extends BlueTask<?>>
        {
        /**
         * IdentFactory instance.
         * 
         */
        public IdentFactory<TaskType> idents();

        /**
         * NameFactory instance.
         * 
         */
        public NameFactory<TaskType> names();

        /**
         * LinkFactory instance.
         * 
         */
        public LinkFactory<TaskType> links();
        
        /**
         * EntityFactory instance.
         * 
         */
        public EntityFactory<TaskType> entities();
        
        /**
         * Our {@link TaskRunner} service.
         * 
         */
        public TaskRunner runner(); 

        }

    /**
     * Reference to our {@link Services} instance.
     * 
    public BlueTask.Services<TaskType> services();
     */
    
    /**
     * TaskRunner interface.
     * 
     */
    public static interface TaskRunner<TaskType extends BlueTask<?>>
        {
        /**
         * Public interface for an update step.
         *
         */
        public interface Updator
            {
            /**
             * The task {@link Identifier}.
             *
             */
            public Identifier ident();

            /**
             * Execute the step.
             *
             */
            public StatusOne execute();
            }

        /**
         * Execute an {@link Updator} in a new {@link Thread}.
         * 
         */
        public StatusOne update(final Updator updator);

        /**
         * Execute an {@link Updator} in a {@link Future}.
         * 
         */
        public Future<StatusOne> execute(final Updator updator);
        
        /**
         * Public interface for a create step.
         *
         */
        public interface Creator<TaskType extends BlueTask<?>>
            {
            /**
             * Execute the step.
             *
             */
            public TaskType create();
            }

        /**
         * Execute an {@link Creator} in a new {@link Thread}.
         * 
         */
        public TaskType create(final Creator<TaskType> creator);

        /**
         * Execute an {@link Updator} in a {@link Future}.
         * 
         */
        public Future<TaskType> execute(final Creator<TaskType> creator);

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
         *
         */
        public Iterable<TaskType> select();

        /**
         * Update the {@link StatusOne} of a {@link BlueTask}.
         * 
         */
        public TaskType update(final Identifier ident, final StatusOne next)
        throws IdentifierNotFoundException;

        /**
         * Update the {@link StatusOne} of a {@link BlueTask}.
         * 
         */
        public TaskType update(final Identifier ident, final StatusOne next, long timeout)
        throws IdentifierNotFoundException;
        
        }

    /**
     * The primary task status.
     *
     */
    public enum StatusOne
        {
        EDITING(true),
        READY(true),
        PENDING(true),
        RUNNING(true),
        COMPLETED(false),
        CANCELLED(false),
        FAILED(false),
        ERROR(false);
        
        private StatusOne(boolean active)
            {
            this.active = active;
            }

        private boolean active ;
        public boolean active()
            {
            return this.active;
            }
        };

    /**
     * The primary task status.
     *
     */
    public StatusOne one();

    /**
     * User level state transitions. 
     * 
     */
    public void update(final StatusOne next);

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
         * Get the {@link Handle} {@link StatusOne}.
         *
         */
        public StatusOne one();

        /**
         * Set the {@link Handle} {@link StatusOne}.
         *
         */
        public void one(final StatusOne one);

        /**
         * Event listener interface.
         *
         */
        public static interface Listener
            {
            /**
             * Check an event.
             * @return true to continue waiting.
             *
             */
            public boolean check(Handle handle);

            /**
             * The elapsed time since this Listener started. 
             *
             */
            public long time();

            /**
             * The number of times this Listener has checked. 
             *
             */
            public long count();

            }

        /**
         * Listen for any event, with a time limit.
         *
         */
        public void listen(long limit);

        /**
         * Listen for a status change, with a time limit.
         *
         */
        public void listen(final StatusOne prev, long limit);

        /**
         * Listen with a {@link Listener} and time limit.
         *
         */
        public void listen(final Listener listener, long limit);

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
     *
     */
    public Handle handle();
    
    /**
     * The date/time the {@link BlueTask} was queued.
     *
     */
    public DateTime queued();

    /**
     * The date/time the {@link BlueTask} was started.
     *
     */
    public DateTime started();

    /**
     * The date/time the {@link BlueTask} was completed.
     *
     */
    public DateTime completed();

    }
