/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

/**
 * Selects a single Entity based on unique name.
 *
 */
public interface NameSelector<T extends GenericEntity>
extends IdentSelector<T>
    {
    public T select(String name);
    }

