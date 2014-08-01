/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.entity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A generic date based {@link NamedEntity.NameFactory}
 *
 */
public abstract class DateNameFactory<EntityType extends NamedEntity>
extends SafeNameFactory<EntityType>
    {

    /**
     * Date time formatter.
     *
     */
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd" + GLUE_CHAR + "HHmmssSSS");
    protected String format(final DateTime datetime)
        {
        return formatter.print(
            datetime
            ) ;
        }

    /**
     * Generate a new date based name.
     *
     */
    protected String datename()
        {
        return formatter.print(
            new DateTime()
            );
        }

    /**
     * Generate a new date based name.
     *
     */
    protected String datename(final String name)
        {
        final StringBuilder builder = new StringBuilder(name);
        builder.append(
            GLUE_CHAR
            );
        builder.append(
            formatter.print(
                new DateTime()
                )
            );
        return safe(
            builder
            );
        }

    /**
     * Generate a new date based name.
     *
     */
    protected String datename(final String name, final Identifier ident)
        {
        final StringBuilder builder = new StringBuilder(name);
        builder.append(
            GLUE_CHAR
            );
        builder.append(
            ident.toString()
            );
        builder.append(
            GLUE_CHAR
            );
        builder.append(
            formatter.print(
                new DateTime()
                )
            );
        return safe(
            builder
            );
        }
    }
