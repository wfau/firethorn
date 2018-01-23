/*
 *  Copyright (C) 2017 Royal Observatory, University of Edinburgh, UK
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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Public interface for a LogEntry.
 * 
 */
public interface BlueTaskLogEntry
    extends Entity
    {
    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends Entity.EntityServices<BlueTaskLogEntry>
        {
        /**
         * Our {@link BlueTaskLogEntry.EntityFactory} instance.
         *
         */
        public BlueTaskLogEntry.EntityFactory entities();

        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<BlueTaskLogEntry>
        {
        }

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<BlueTaskLogEntry>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<BlueTaskLogEntry>
        {
        /**
         * Create a new {@link BlueTaskLogEntry}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public BlueTaskLogEntry create(final BlueTask<?> task, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new {@link BlueTaskLogEntry}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         *  
         */
        public BlueTaskLogEntry create(final BlueTask<?> task, final BlueTask.TaskState state, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new {@link BlueTaskLogEntry}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         *  
         */
        public BlueTaskLogEntry create(final Object source, final BlueTask<?> task, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;

        /**
         * Create a new {@link BlueTaskLogEntry}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         *  
         */
        public BlueTaskLogEntry create(final Object source, final BlueTask<?> task, final BlueTask.TaskState state, final BlueTaskLogEntry.Level level, final String message)
        throws ProtectionException;
        
        /**
         * Select all the log entries for an {@link Entity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task)
        throws ProtectionException;

        /**
         * Select the most recent log entries for an {@link Entity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit)
        throws ProtectionException;

        /**
         * Select all the log entries with a specific level for an {@link Entity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final BlueTaskLogEntry.Level level)
        throws ProtectionException;

        /**
         * Select all the log entries with a specific level for an {@link Entity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit, final BlueTaskLogEntry.Level level)
        throws ProtectionException;

        }
    
    /**
     * The target {@link BlueTask} that this event applies to. 
     * 
     */
    public BlueTask<?> task();

    /**
     * The {@link TaskState} when the event occurred.
     * 
     */
    public TaskState state();

    /**
     * Public enum for event level.
     * 
     */
    public enum Level {

        DEBUG(),
        ERROR(),
        WARN(),
        INFO();
        
        }
    
    /**
     * The log event level.
     * 
     */
    public Level level();

    /**
     * The log message.
     * 
     */
    public String message();

    /**
     * The class name of the Object that created the event.
     * 
     */
    public String source(); 
    
    }
