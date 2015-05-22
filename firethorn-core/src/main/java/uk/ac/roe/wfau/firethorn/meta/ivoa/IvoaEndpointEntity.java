package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;

/**
 * {@link IvoaResource.Endpoint} implementation.
 *
 */
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaEndpointEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        }
    )
public class IvoaEndpointEntity
extends AbstractEntity
implements IvoaResource.Endpoint
    {
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX  + "IvoaEndpointEntity";

    protected static final String DB_ENDPOINT_COL  = "endpoint";
    protected static final String DB_RESOURCE_COL  = "resource";

    /**
     * Reference to our parent.
     * 
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaResource resource ;
    public IvoaResource resource()
        {
        return this.resource;
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaEndpointEntity(final IvoaResource resource, final String url)
        {
        this.url = url ;
        this.resource = resource;
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
    public String endpoint()
        {
        return this.url;
        }

    @Override
    public String link()
        {
        return null;
        }
    }