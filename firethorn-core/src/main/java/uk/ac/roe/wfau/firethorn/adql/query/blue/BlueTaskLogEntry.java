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

import uk.ac.roe.wfau.firethorn.entity.Entity;

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
         * 
         */
        public BlueTaskLogEntry create(final BlueTask<?> task, final BlueTaskLogEntry.Level level, final String message);

        /**
         * Create a new {@link BlueTaskLogEntry}.
         * 
         */
        public BlueTaskLogEntry create(final Object source, final BlueTask<?> task, final BlueTaskLogEntry.Level level, final String message);

        
        /**
         * Select all the log entries for an {@link Entity}.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task);

        /**
         * Select the most recent log entries for an {@link Entity}.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit);

        /**
         * Select all the log entries with a specific level for an {@link Entity}.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final BlueTaskLogEntry.Level level);

        /**
         * Select all the log entries with a specific level for an {@link Entity}.
         * 
         */
        public Iterable<BlueTaskLogEntry> select(final BlueTask<?> task, final Integer limit, final BlueTaskLogEntry.Level level);

        }
    
    /**
     * The target {@link BlueTask} that this event applies to. 
     * 
     */
    public BlueTask<?> task();
    
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
     * The event level.
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
