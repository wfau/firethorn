package uk.org.ogsadai.dqp.lqp.exceptions;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if a condition token is not supported.
 *
 * @author The OGSA-DAI Project Team.
 */
public class UnsupportedTokenException extends DAIException
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.UNSUPPORTED_TOKEN");
    
    /**
     * Constructor.
     * 
     * @param token
     *            the token that is not supported
     */
    public UnsupportedTokenException(String token)
    {
        super(ERROR_ID, new Object[] {token});
    }
    
}
