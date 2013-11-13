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
package uk.ac.roe.wfau.firethorn.entity ;

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Common interface for a persistent Entity.
 *
 */
public interface Entity
    {
    /**
     * Common interface for a name factory.
     *
     */
    public interface NameFactory<EntityType extends Entity>
        {
        /**
         * Generate a new name.
         *
         */
        public String name(final String name);
        }

    /**
     * Common interface for an alias factory.
     *
     */
    public interface AliasFactory<EntityType extends Entity>
        {
        /**
         * Generate an alias.
         *
         */
        public String alias(final EntityType entity);
        }

    /**
     * Common interface for a link factory.
     *
     */
    public interface LinkFactory<EntityType extends Entity>
        {
        /**
         * Create an Entity URI (as a string).
         *
         */
        public String link(final EntityType entity);

        /**
         * Parse a link into an Identifier.
         *
         */
        public Identifier ident(final String string);
        }

    /**
     * Common interface for an Identifier factory.
     *
     */
    public interface IdentFactory
        {
        /**
         * Create an Identifier from a String.
         *
         */
        public Identifier ident(final String string);
        }

    /**
     * Common interface for an Entity factory.
     * @todo Separate Entity Resolver and Factory interfaces.
     *
     */
    public interface EntityFactory<EntityType extends Entity>
        {
        /**
         * Select a specific Entity by Identifier.
         *
         */
        public EntityType select(final Identifier ident)
        throws IdentifierNotFoundException;

        /**
         * Our local Identifier factory.
         *
         */
        public IdentFactory idents();

        /**
         * Our local link factory.
         *
         */
        public LinkFactory<EntityType> links();

        /**
         * Our 'empty' entity instance.
         * This can be used to represent things like 'nobody', 'no results', or an empty resource, schema, table or column.
         *
         */
        public EntityType empty();

        /**
         * Wrap a runnable operation in a write transaction.
         *
         */
        @Deprecated
        public void createEntity(final Runnable oper);

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
     * Refresh (fetch) this Entity from the database.
     *
     */
    public void refresh();

    /**
     * Delete this Entity from the database.
     *
     */
    public void delete();

    /**
     * The Entity Protector.
     * 
     */
    public EntityProtector protector();
    
    }

