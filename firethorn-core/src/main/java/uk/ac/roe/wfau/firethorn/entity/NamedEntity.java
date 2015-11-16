/**
 *
 */
package uk.ac.roe.wfau.firethorn.entity;

import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;

/**
 *
 *
 */
public interface NamedEntity
extends Entity
	{
    /**
     * Common interface for a name factory.
     *
     */
	@Deprecated
    public interface NameFactory<EntityType extends NamedEntity>
        {
        /**
         * Generate a new name.
         *
         */
        @Deprecated
        public String name();

        /**
         * Generate a new name.
         * @param name An initial name to base the new name on. 
         *
         */
        @Deprecated
        public String name(final String name);

        }

    /**
     * EntityServices interface.
     * 
     */
    public static interface EntityServices<EntityType extends NamedEntity>
    extends Entity.EntityServices<EntityType>
        {
        /**
         * Our {@link NamedEntity.NameFactory} instance.
         *
         */
        public NamedEntity.NameFactory<EntityType> names();
        }

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
     * Get the Entity description.
     *
     */
    public String text();

    /**
     * Set the Entity description.
     *
     */
    public void text(final String text);

	}
