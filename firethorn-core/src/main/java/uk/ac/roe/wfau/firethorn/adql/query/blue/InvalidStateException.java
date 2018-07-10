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
package uk.ac.roe.wfau.firethorn.adql.query.blue ;

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * Exception thrown in response to a request that causes an invalid {@link TaskState} transition.
 *
 */
//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStateException
extends InvalidRequestException
    {
    /**
     * Serialzable version UID.
     *
     */
	private static final long serialVersionUID = 5046974592933830046L;

	/**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Invalid state transition [:prev:]->[:next:] for [:ident:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(final BlueTask<?> task, final TaskState prev, final TaskState next)
        {
    	String message = DEFAULT_MESSAGE.replace(":ident:", task.ident().toString());
    	message = message.replace(":prev:", ((prev != null) ? prev.name() : "null"));
    	message = message.replace(":next:", ((next != null) ? next.name() : "null"));
    	return message ;
        }

    /**
     * Public constructor, with default message.
     *
     */
    public InvalidStateException(final BlueTask<?> task, final TaskState prev, final TaskState next)
        {
        this(
            task,
            message(
                task,
                prev,
                next
                )
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public InvalidStateException(final BlueTask<?> task, final String message)
        {
        super(message);
        this.ident = task.ident();
        }

    private final Identifier ident;

    public Identifier ident()
        {
        return this.ident;
        }
    }

