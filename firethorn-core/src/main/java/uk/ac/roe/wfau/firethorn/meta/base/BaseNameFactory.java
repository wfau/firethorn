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
package uk.ac.roe.wfau.firethorn.meta.base;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import uk.ac.roe.wfau.firethorn.entity.AbstractNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * A generic JDBC, ADQL, SQL safe NameFactory
 * @todo Move to a separate package.
 *
 */
public class BaseNameFactory<EntityType extends Entity>
extends AbstractNameFactory<EntityType>
implements Entity.NameFactory<EntityType>
    {
    /**
     * Our SQL safe glue character.
     *  
     */
    public static final String GLUE_CHAR = "_" ;

    /**
     * Our SQL unsafe replacement pattern.
     *  
     */
    public static final String REPLACE_REGEX= "[^" + GLUE_CHAR + "\\p{Alnum}]+?" ;

    @Override
    public String name(String name)
        {
        return safe(
            name
            );
        }

    /**
     * Generate a JDBC, ADQL, SQL safe name.
     * 
     */
    public String safe(String name)
        {
        return name.trim().replaceAll(
            REPLACE_REGEX,
            GLUE_CHAR
            );
        }

    /**
     * Generate a JDBC, ADQL, SQL safe name.
     * http://stackoverflow.com/questions/3472663/replace-all-occurences-of-a-string-using-stringbuilder
     * http://stackoverflow.com/a/7837748
     * 
     */
    public String safe(StringBuilder builder)
        {
        return name(builder.toString());
        }

    /**
     * Date time formatter.
     *
     */
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd" + GLUE_CHAR + "HHmmssSSS");
    protected String format(final DateTime datetime)
        {
        return formatter.print(datetime) ;
        }

    /**
     * Generate a new date based name.
     *
     */
    protected String datename(final String prefix)
        {
        StringBuilder builder = new StringBuilder(prefix); 
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
    protected String datename(final String prefix, final Identifier ident)
        {
        StringBuilder builder = new StringBuilder(prefix); 
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
