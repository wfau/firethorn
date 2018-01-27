/**
 *
 */
package uk.ac.roe.wfau.firethorn.entity;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

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
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String name();
    //throws ProtectionException;

    /**
     * Set the Entity name.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void name(final String name)
    throws ProtectionException, NameFormatException;

    /**
     * Get the Entity description.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String text()
    throws ProtectionException;

    /**
     * Set the Entity description.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public void text(final String text)
    throws ProtectionException;

	}
