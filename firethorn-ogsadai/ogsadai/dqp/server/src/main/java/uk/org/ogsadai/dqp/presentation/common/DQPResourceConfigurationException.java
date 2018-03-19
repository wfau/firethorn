//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.dqp.presentation.common;

import uk.org.ogsadai.exception.DAIUncheckedException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if an error occurred during initialisation of a DQP resource. The
 * error chain contains more information as to the cause of the error.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DQPResourceConfigurationException extends DAIUncheckedException
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Error ID. */
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.DQP_RESOURCE_CONFIGURATION_ERROR");

    /**
     * Constructs a new configuration exception with the given cause.
     * 
     * @param cause
     *            chained error
     */
    public DQPResourceConfigurationException(Throwable cause)
    {
        super(ERROR_ID);
        initCause(cause);
    }

}
