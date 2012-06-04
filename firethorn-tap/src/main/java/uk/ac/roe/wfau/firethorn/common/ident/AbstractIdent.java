/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.ident ;

/**
 *
 */
public abstract class AbstractIdent
implements Identifier
    {

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

