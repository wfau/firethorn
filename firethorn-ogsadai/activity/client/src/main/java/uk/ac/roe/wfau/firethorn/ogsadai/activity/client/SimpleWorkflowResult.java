package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import lombok.extern.slf4j.Slf4j;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientServerCompatibilityException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 * A simple Result class.
 * 
 */
@Slf4j
public class SimpleWorkflowResult
implements WorkflowResult
    {
    /**
     * Public constructor.
     * 
     */
    public SimpleWorkflowResult(final RequestResource request)
        {
        this.request = request.getResourceID();
        try {
            final RequestExecutionStatus result = request.getRequestExecutionStatus();
            log.debug("RequestExecutionStatus [{}]", result);
            if (result == null)
                {
                this.status = WorkflowResult.Status.FAILED;
                this.message = "Null RequestExecutionStatus";
                }
            else if (result.equals(RequestExecutionStatus.PROCESSING))
                {
                this.status = WorkflowResult.Status.RUNNING;
                }
            else if (result.equals(RequestExecutionStatus.PROCESSING_WITH_ERROR))
                {
                this.status = WorkflowResult.Status.RUNNING;
                }
            else if (result.equals(RequestExecutionStatus.COMPLETED))
                {
                this.status = WorkflowResult.Status.COMPLETED;
                }
            else if (result.equals(RequestExecutionStatus.COMPLETED_WITH_ERROR))
                {
                this.status = WorkflowResult.Status.FAILED;
                }
            else if (result.equals(RequestExecutionStatus.TERMINATED))
                {
                this.status = WorkflowResult.Status.CANCELLED;
                }
            else if (result.equals(RequestExecutionStatus.ERROR))
                {
                this.status = WorkflowResult.Status.FAILED;
                }
            else {
                this.status  = WorkflowResult.Status.UNKNOWN;
                this.message = "Unknown RequestExecutionStatus [" + result + "]";
                }
            }
        catch (ResourceUnknownException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ResourceUnknownException [" + ouch.getMessage() + "]" ;
            }
        catch (ServerCommsException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ServerCommsException [" + ouch.getMessage() + "]" ;
            }
        catch (ServerException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ServerException [" + ouch.getMessage() + "]" ;
            }
        catch (ClientServerCompatibilityException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ClientServerCompatibilityException [" + ouch.getMessage() + "]" ;
            }
        catch (ClientToolkitException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ClientToolkitException [" + ouch.getMessage() + "]" ;
            }
        catch (ClientException ouch)
            {
            log.warn("Exception processing query [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            this.cause   = ouch ;
            this.status  = WorkflowResult.Status.FAILED;
            this.message = "ClientException [" + ouch.getMessage() + "]" ;
            }
        }

    private Status status;
    @Override
    public Status status()
        {
        return this.status;
        }

    private ResourceID request;
    @Override
    public ResourceID request()
        {
        return this.request;
        }

    private String message;
    @Override
    public String message()
        {
        return this.message;
        }

    /**
     * Public constructor.
     * 
     */
    public SimpleWorkflowResult(final Throwable cause)
        {
        this.cause = cause ;
        this.status = WorkflowResult.Status.FAILED;
        }

    /**
     * Public constructor.
     * 
     */
    public SimpleWorkflowResult(final String message, final Throwable cause)
        {
        this.cause   = cause ;
        this.status  = WorkflowResult.Status.FAILED;
        this.message = message;
        }

    private Throwable cause;
    @Override
    public Throwable cause()
        {
        return this.cause;
        }
    }