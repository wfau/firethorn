/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.error ;

/**
 * 
 *
 */
public class FireThornException
extends RuntimeException
    {
    /**
     * Public constructor.
     *
     */
    public FireThornException()
        {
        super();
        }

    /**
     * Public constructor.
     *
     */
    public FireThornException(String message)
        {
        super(message);
        }

    /**
     * Public constructor.
     *
     */
    public FireThornException(String message, Throwable cause)
        {
        super(message, cause);
        }

    /**
     * Public constructor.
     *
     */
    public FireThornException(Throwable cause)
        {
        super(cause);
        }

    }

