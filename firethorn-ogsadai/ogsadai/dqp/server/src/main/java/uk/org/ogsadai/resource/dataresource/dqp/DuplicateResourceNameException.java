//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.resource.dataresource.dqp;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised when the same resource name is used for different remote resources.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DuplicateResourceNameException extends DAIException
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";

    /** Error ID. */
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.dqp.DUPLICATE_RESOURCE_NAME_ERROR");

    /**
     * Creates a new exception for the given resource name.
     * 
     * @param alias
     *            name that is duplicated
     */
    public DuplicateResourceNameException(String alias)
    {
        super(ERROR_ID, new Object[] {alias});
    }

}
