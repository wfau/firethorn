package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import org.hibernate.annotations.Parent;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link IvoaResource.Endpoint} implementation.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class IvoaEndpointEntity
implements IvoaResource.Endpoint
    {

    protected static final String DB_ENDPOINT_COL  = "endpoint";

    /**
     * Reference to our parent resource.
     * 
     */
    @Parent
    protected IvoaResourceEntity resource ;
    protected IvoaResourceEntity getResource()
        {
        return this.resource;
        }
    protected void setResource(final IvoaResourceEntity resource)
        {
        this.resource = resource;
        }
    @Override
    public IvoaResource resource()
        {
        return this.resource;
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaEndpointEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaEndpointEntity(final IvoaResourceEntity resource, final String url)
        {
        this.resource = resource;
        this.url = url ;
        }
    
    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ENDPOINT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String url;
    @Override
    public String string()
        {
        return this.url;
        }
    }