/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

import java.io.Serializable;

/**
 *
 */
public abstract class AbstractIdent<T extends Serializable>
implements Identifier
    {

    public AbstractIdent(T value)
        {
        this.value = value ;
        }

    private T value ;

    @Override
    public Serializable value()
        {
        return this.value ;
        }

    @Override
    public boolean equals(Object that)
        {
        if (that != null)
            {
            if (this == that)
                {
                return true ;
                }                            
            if (that instanceof Identifier)
                {
                return this.value().equals(
                    ((Identifier)that).value()
                    );
                }
            }
        return false ;
        }

    @Override
    public String toString()
        {
        if (null != this.value())
            {
            return this.value().toString();
            }
        else {
            return null ;
            }
        }

    }

