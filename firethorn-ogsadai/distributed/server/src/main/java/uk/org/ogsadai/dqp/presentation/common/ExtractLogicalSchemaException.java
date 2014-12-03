//Copyright (c) The University of Edinburgh 2008.

package uk.org.ogsadai.dqp.presentation.common;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Raised if an error occurred when the remote logical schemas are fetched from
 * a resource. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class ExtractLogicalSchemaException extends DAIException
{

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Error ID. */
    private static final ErrorID ERROR_ID = 
        new ErrorID("uk.org.ogsadai.EXTRACT_TABLE_SCHEMA_ERROR");

    /**
     * Constructs a new exception with the given cause.
     * 
     * @param e
     *            chained error
     */
    public ExtractLogicalSchemaException(Throwable e)
    {
        super(ERROR_ID);
        initCause(e);
    }

}
