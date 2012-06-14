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

import java.util.Date;

/**
 * Common interface for a persistent Entity.
 *
 */
public interface Entity
    {

    /**
     * Common interface for an Entity factory.
     *
     */
    public interface Factory<EntityType extends Entity>
        {

        /**
         * Create an Identifier from a String.
         *
         */
        public Identifier ident(String string);

        /**
         * Select a specific Entity by Identifier.
         *
         */
        public EntityType select(final Identifier ident);

        /**
         * Select all of the Entities.
         *
         */
        public Iterable<EntityType> select();

        }

    /**
     * The unique Entity Identifier.
     *
     */
    public Identifier ident();

    /**
     * Get the Entity name.
     *
     */
    public String name();

    /**
     * Set the Entity name.
     *
     */
    public void name(String name);

    /**
     * The date/time when the Entity was created.
     *
     */
    public Date created();

    /**
     * The date/time when the Entity was last modified.
     *
     */
    public Date modified();

    /**
     * Update (store) this Entity in the database.
     *
     */
    public void update();

    /**
     * Delete this Entity from the database.
     *
     */
    public void delete();


    }

