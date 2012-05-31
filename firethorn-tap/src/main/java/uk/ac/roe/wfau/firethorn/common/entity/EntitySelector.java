/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

/**
 *
 */
public interface EntitySelector<T extends GenericEntity>
    {
    public Iterable<T> select();
    }

