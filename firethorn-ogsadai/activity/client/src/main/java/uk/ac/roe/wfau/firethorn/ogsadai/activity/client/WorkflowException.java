package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

/**
 * Exception to indicate an error during workflow.
 * 
 */
public class WorkflowException
extends Exception
    {
    /**
     * Serial version ID, {@value}.
     *
     */
    private static final long serialVersionUID = -7079747902589137753L;

    /**
     * Public constructor.
     *
     */
    public WorkflowException()
        {
        super();
        }

    /**
     * Public constructor.
     *
     */
    public WorkflowException(final String message)
        {
        super(message);
        }

    /**
     * Public constructor.
     *
     */
    public WorkflowException(final String message, final Throwable cause)
        {
        super(message, cause);
        }
    }