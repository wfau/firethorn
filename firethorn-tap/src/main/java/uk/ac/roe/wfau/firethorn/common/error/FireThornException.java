/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.error ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

