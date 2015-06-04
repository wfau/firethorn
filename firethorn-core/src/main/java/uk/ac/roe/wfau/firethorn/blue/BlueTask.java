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

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;

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
        }

    /**
     * Reference to our {@link Services} instance.
     * 
     */
    public BlueTask.Services<TaskType> services();

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

        }

    /**
     * The primary task status.
     *
     */
    public enum StatusOne
        {
        EDITING,
        READY,
        PENDING,
        RUNNING,
        COMPLETED,
        CANCELLED,
        FAILED,
        UNKNOWN
        };

    /**
     * The primary task status.
     *
     */
    public StatusOne one();

    /**
     * The primary task status.
     *
     */
    public StatusOne one(final StatusOne one);

    /**
     * An event notification handle.
     *
     */
    public static interface Handle
        {

        /**
         * The task {@link Identifier}.
         *
         */
        public Identifier ident();

        /**
         * Get the {@link Handle} {@link StatusOne}.
         *
         */
        public StatusOne one();

        /**
         * Set the {@link Handle} {@link StatusOne}.
         *
         */
        public StatusOne one(final StatusOne one);

        /**
         * Register this {@link Handler}.
         *
         */
        public void register();

        /**
         * Release this {@link Handler}.
         *
         */
        public void release();

        /**
         * Event listener interface.
         *
         */
        public static interface Listener
            {
            /**
             * Handle the first event.
             * @return true to continue waiting.
             *
             */
            public boolean start(Handle handle);

            /**
             * Handle a subsequent event.
             * @return true to continue waiting.
             *
             */
            public boolean event(Handle handle);

            /**
             * The elapsed time since this Listener started. 
             *
             */
            public long time();

            }

        /**
         * Listen for any event, no time limit.
         *
         */
        public void listen();

        /**
         * Listen for any event, for a specific time limit.
         *
         */
        public void listen(long limit);

        /**
         * Listen for events, with no time limit.
         *
         */
        public void listen(final Listener listener);

        /**
         * Listen for events, for a specific time limit.
         *
         */
        public void listen(long limit, final Listener listener);

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
