package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Workflow results.
 * 
 */
public class CreateResourceResult
extends SimpleWorkflowResult
    {
    public CreateResourceResult(final RequestResource request, ResourceID created)
        {
        super(
            request
            );
        this.resource = created;
        }

    /**
     * Public constructor.
     * 
     */
    public CreateResourceResult(final Throwable cause)
        {
        super(
            cause
            );
        }

    /**
     * Public constructor.
     * 
     */
    public CreateResourceResult(final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        }

    /**
     * The created resource ID.
     * 
     */
    private ResourceID resource ;

    /**
     * The created resource ID.
     * 
     */
    public ResourceID resource()
        {
        return this.resource;
        }
    }