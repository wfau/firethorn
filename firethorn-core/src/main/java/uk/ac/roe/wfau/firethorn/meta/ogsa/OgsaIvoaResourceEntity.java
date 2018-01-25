/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import java.net.MalformedURLException;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource.Endpoint;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResourceEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ivoa.IvoaCreateResourceWorkflow;

/**
 * {@link OgsaIvoaResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaIvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaIvoaResource-select-all",
            query = "FROM OgsaIvoaResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-service",
            query = "FROM OgsaIvoaResourceEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-resource",
            query = "FROM OgsaIvoaResourceEntity WHERE resource = :resource ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaIvoaResource-select-service-resource",
            query = "FROM OgsaIvoaResourceEntity WHERE service = :service AND resource = :resource ORDER BY ident desc"
            ),
        }
    )
public class OgsaIvoaResourceEntity
    extends OgsaBaseResourceEntity<OgsaIvoaResource>
    implements OgsaIvoaResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaIvoaResourceEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_RESOURCE_COL = "resource";
    
    /**
     * {@link OgsaIvoaResource.EntityFactory} implementation.
     *
     */
    @Component
    @Repository
    public static class OgsaIvoaResourceEntityFactory
    extends OgsaBaseResourceEntity.EntityFactory<OgsaIvoaResource>
    implements OgsaIvoaResource.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }

        @Override
        public Class<?> etype()
            {
            return OgsaIvoaResourceEntity.class;
            }

        @Override
        @SelectMethod
        public Iterable<OgsaIvoaResource> select()
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaIvoaResource-select-all"
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<OgsaIvoaResource> select(final OgsaService service)
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaIvoaResource-select-service"
                    ).setEntity(
                        "service",
                        service
                        )
                );
            }

        @Override
        public Iterable<OgsaIvoaResource> select(final IvoaResource resource)
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaIvoaResource-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<OgsaIvoaResource> select(final OgsaService service, final IvoaResource resource)
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "OgsaIvoaResource-select-service-resource"
                    ).setEntity(
                        "service",
                        service
                    ).setEntity(
                        "resource",
                        resource
                    )
                );
            }

        @Override
        @CreateMethod
        public OgsaIvoaResource create(final OgsaService service, final IvoaResource resource)
        throws ProtectionException
            {
            return super.insert(
                new OgsaIvoaResourceEntity(
                    service,
                    resource
                    )
                );
            }

        @Override
        @CreateMethod
        public OgsaIvoaResource primary(IvoaResource resource)
        throws ProtectionException
            {
            return this.primary(
                factories().ogsa().services().entities().primary(),
                resource
                );
            }

        @Override
        @CreateMethod
        public OgsaIvoaResource primary(OgsaService service, IvoaResource resource)
        throws ProtectionException
            {
            // Really really simple - just get the first. 
            OgsaIvoaResource found = super.first(
                super.query(
                    "OgsaIvoaResource-select-service-resource"
                    ).setEntity(
                        "service",
                        service
                    ).setEntity(
                        "resource",
                        resource
                    )
                );
            // If we found one, check it works.
            if (found != null)
                {
                log.debug("Found primary OgsaIvoaResource [{}]", found.ident());
// Temp fix to force a scan.
// TODO Add a verify method ?
                log.debug("Checking ogsaid ...");
                final String ogsaid = found.ogsaid();
                log.debug("Found ogsaid [{}][{}]", ogsaid, found.ogstatus());
                if (found.ogstatus() == OgsaStatus.ACTIVE)
                    {
                    return found ;
                    }
                else {
// Assume this is because the scan failed.
// TODO Better error handling.
// TODO Prevent runaway errors creating a new resource a on every call
                    log.debug("Primary OgsaIvoaResource failed ping test, creating a new one");
                    return create(
                        service,
                        resource
                        );
                    }
                }
            // If we didn't find one, create a new one.
            else {
                log.debug("No primary OgsaIvoaResource, creating a new one");
                return create(
                    service,
                    resource
                    );
                }
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements OgsaIvoaResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static OgsaIvoaResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return OgsaIvoaResourceEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (OgsaIvoaResourceEntity.EntityServices.instance == null)
                {
                OgsaIvoaResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private OgsaIvoaResource.IdentFactory idents;
        @Override
        public OgsaIvoaResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaIvoaResource.LinkFactory links;
        @Override
        public OgsaIvoaResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private OgsaIvoaResource.EntityFactory entities;
        @Override
        public OgsaIvoaResource.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected OgsaIvoaResource.EntityFactory factory()
        {
        log.debug("factory()");
        return OgsaIvoaResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected OgsaIvoaResource.EntityServices services()
        {
        log.debug("services()");
        return OgsaIvoaResourceEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * Protected constructor. 
     *
     */
    protected OgsaIvoaResourceEntity()
        {
        super();
        }

   /**
     *
     * Public constructor.
     * @param service The parent {@link OgsaService}
     * @param resource  The source {@link IvoaResource}
     *
     */
    public OgsaIvoaResourceEntity(final OgsaService service, final IvoaResource resource)
        {
        super(
            service
            );
        this.resource = resource  ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaResource resource;
    @Override
    public IvoaResource resource()
        {
        return this.resource;
        }

    @Override
    public OgsaStatus init()
    throws ProtectionException
        {
        log.debug("init()");
        log.debug("  name   [{}]", this.name());
        log.debug("  ident  [{}]", this.ident());
        log.debug("  ogsaid [{}]", this.ogsaid);
        //
        // If we already have an ODSA-DAI resource ID.
        if (this.ogsaid != null)
            {
            log.debug("Using existing OGSA id [{}]", this.ogsaid);
            return ogstatus() ;
            }
        //
        // If we don't have an ODSA-DAI resource ID.
        else {
            //
            // Get the TAP endpoint.
            final Endpoint endpoint = resource().endpoint();
            //
            // If the TAP endpoint is valid.
            if ((endpoint != null) && (endpoint.string() != null))
                {
                log.debug("Using TAP endpoint [{}]", endpoint.string());
                //
                // Create our OGSA-DAI resource.
                IvoaCreateResourceWorkflow workflow = null;
                try {
                    workflow = new IvoaCreateResourceWorkflow(
                        service().endpoint()
                        );
                    }
                catch (MalformedURLException ouch)
                    {
                    return ogstatus(
                        OgsaStatus.ERROR
                        );
                    }
    
                final ResourceWorkflowResult response = workflow.execute(
                    new IvoaCreateResourceWorkflow.Param()
                        {
                        @Override
                        public String endpoint()
                            {
                            return endpoint.string();
                            }
    
                        @Override
                        public Boolean quickstart()
                            {
                            return Boolean.FALSE;
                            }
    
                        @Override
                        public Integer interval()
                            {
                            return new Integer(10);
                            }
    
                        @Override
                        public Integer timeout()
                            {
                            return new Integer(300);
                            }
                        }
                    );

                log.debug("Status  [{}]", response.status());
                log.debug("Created [{}]", response.result());
        
                if (response.status() == WorkflowResult.Status.COMPLETED)
                    {
                    return ogsaid(
                        OgsaStatus.ACTIVE,
                        response.result().toString()
                        );
                    }
        
                else {
                    return ogstatus(
                        OgsaStatus.ERROR
                        );
                    }
                }
            else {
                log.debug("TAP endpoint is null [{}]", endpoint);
                return ogstatus(
                    OgsaStatus.ERROR
                    );
                }
            }
        }
    }
