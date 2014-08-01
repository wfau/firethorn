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

/**
 * A generic JDBC, ADQL, SQL safe {@link NamedEntity.NameFactory}
 *
 */
public abstract class SafeNameFactory<EntityType extends NamedEntity>
extends AbstractComponent
implements NamedEntity.NameFactory<EntityType>
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

    /**
     * Generate a JDBC, ADQL, SQL safe name.
     *
     */
    public String safe(final String name)
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
    public String safe(final StringBuilder builder)
        {
        return safe(
            builder.toString()
            );
        }

    @Override
    public String name(final String name)
        {
        return safe(
            name
            );
        }
    }
