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
