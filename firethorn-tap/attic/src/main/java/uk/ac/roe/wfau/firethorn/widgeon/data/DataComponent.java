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
package uk.ac.roe.wfau.firethorn.widgeon.data ;

import uk.ac.roe.wfau.firethorn.entity.Entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Public interface for a component in the metadata tree with a status.
 *
 */
public interface DataComponent
extends Entity
    {

    /**
     * Enum representing the status.
     *
     */
    public enum Status
        {
        ENABLED(true),
        DISABLED(false),
        MISSING(false),
        CREATED(false),
        DELETED(false);

        /**
         * Private constructor.
         *
         */
        private Status(final boolean enabled)
            {
            this.enabled = enabled ;
            }

        /**
         * Private enabled flag.
         *
         */
        private final boolean enabled ;

        /**
         * Check if this status means the component is enabled.
         *
         */
        public boolean enabled()
            {
            return this.enabled ;
            }

        }

    /**
     * The component status.
     *
     */
    public Status status();

    /**
     * Set the component status.
     *
     */
    public void status(final Status status)
    throws InvalidStatusException ;

    /**
     * Set the component status.
     *
     */
    public void status(final Status status, String message)
    throws InvalidStatusException ;
    
    /**
     * Exception to describe an invalid status.
     *
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class InvalidStatusException
    extends RuntimeException
        {

        /**
         * Eclipse generated serial version ID.
         *
         */
        private static final long serialVersionUID = 6595169286734884311L;

        /**
         * Default message for simple constructor.
         *
         */
        public static final String DEFAULT_MESSAGE = "Invalid status [:status:]" ;

        /**
         * Create a default message.
         *
         */
        public static String message(final String status)
            {
            return DEFAULT_MESSAGE.replace(":status:", status);
            }

        /**
         * Create a default message.
         *
         */
        public static String message(final DataComponent.Status status)
            {
            return message(
                status.toString()
                );
            }

        /**
         * Public constructor, using default message.
         *
         */
        public InvalidStatusException(final DataComponent.Status status)
            {
            this(
                status,
                message(
                    status
                    )
                );
            }

        /**
         * Public constructor, with specific message.
         *
         */
        public InvalidStatusException(final DataComponent.Status status, final String message)
            {
            super(
                message
                );
            this.status = status ;
            }

        private final DataComponent.Status status;

        public DataComponent.Status status()
            {
            return this.status;
            }
        }
    }

