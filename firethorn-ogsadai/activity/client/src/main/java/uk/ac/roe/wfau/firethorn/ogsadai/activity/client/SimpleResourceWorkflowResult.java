package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Workflow result with a ResourceID.
 * 
 */
public class SimpleResourceWorkflowResult
extends SimpleWorkflowResult
implements ResourceWorkflowResult
    {
    /**
     * Public constructor.
     * 
     */
    public SimpleResourceWorkflowResult(final RequestResource request, final ResourceID result)
        {
        super(
            request
            );
        this.result = result;
        }

    /**
     * Public constructor.
     * 
     */
    public SimpleResourceWorkflowResult(final Throwable cause)
        {
        super(
            cause
            );
        }

    /**
     * Public constructor.
     * 
     */
    public SimpleResourceWorkflowResult(final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        }

    /**
     * The result resource ID.
     * 
     */
    private ResourceID result ;

    @Override
    public ResourceID result()
        {
        return this.result;
        }
    }