/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import java.util.Date;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

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

