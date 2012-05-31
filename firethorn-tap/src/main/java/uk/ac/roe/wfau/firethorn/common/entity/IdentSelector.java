/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

/**
 * Selects a single Entity based on unique Identifer.
 *
 */
public interface IdentSelector<T extends GenericEntity>
extends EntitySelector<T>
    {
    public T select(Identifier ident);
    }

