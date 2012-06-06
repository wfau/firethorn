/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

/**
 *
 */
public interface EntitySelector<T extends Entity>
    {
    public Iterable<T> select();
    }

