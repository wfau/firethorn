/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

import java.io.Serializable;

/**
 *
 */
public class SimpleIdent<T extends Serializable>
extends AbstractIdent
implements Identifier
    {
    public SimpleIdent(T value)
        {
        this.value = value ;
        }

    private T value ;

    public Serializable value()
        {
        return this.value ;
        }
    }

