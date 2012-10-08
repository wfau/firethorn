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
package uk.ac.roe.wfau.firethorn.common.entity ;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;



/**
 * Common interface for a persistent Entity.
 *
 */
public interface Entity
    {

    /**
     * Common interface for an Identifier factory.
     *
     */
    public interface IdentFactory<EntityType extends Entity>
        {
        /**
         * Create an Identifier from a String.
         *
         */
        public Identifier ident(final String string);

        /**
         * Create an Entity URI (as a string).
         *
        public String link(final Identifier ident);
         */

        /**
         * Create an Entity URI (as a string).
         *
         */
        public String link(final EntityType entity);

        }

    /**
     * Common interface for an Entity factory.
     *
     */
    public interface Factory<EntityType extends Entity>
    extends IdentFactory<EntityType>
        {
        /**
         * Select a specific Entity by Identifier.
         *
         */
        public EntityType select(final Identifier ident)
        throws IdentifierNotFoundException;

        }

    /**
     * The Entity Identifier.
     *
     */
    public Identifier ident();

    /**
     * The Entity URI (as a string).
     *
     */
    public String link();

    /**
     * Get the Entity name.
     *
     */
    public String name();

    /**
     * Set the Entity name.
     *
     */
    public void name(final String name)
    throws NameFormatException;

    /**
     * Get the Entity owner.
     *
     */
    public Identity owner();

    /**
     * The date/time when the Entity was created.
     *
     */
    public DateTime created();

    /**
     * The date/time when the Entity was last modified.
     *
     */
    public DateTime modified();

    /**
     * Update (store) this Entity in the database.
     *
    public void update();
     */

    /**
     * Delete this Entity from the database.
     *
     */
    public void delete();


    }

