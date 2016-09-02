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
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcCreateResourceWorkflow;

/**
 * {@link OgsaJdbcResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaJdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaJdbcResource-select-all",
            query = "FROM OgsaJdbcResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-service",
            query = "FROM OgsaJdbcResourceEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-resource",
            query = "FROM OgsaJdbcResourceEntity WHERE resource = :resource ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-resource-status",
            query = "FROM OgsaJdbcResourceEntity WHERE resource = :resource AND ogstatus = :ogstatus ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-service-resource",
            query = "FROM OgsaJdbcResourceEntity WHERE service = :service AND resource = :resource ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaJdbcResource-select-service-resource-status",
            query = "FROM OgsaJdbcResourceEntity WHERE service = :service AND resource = :resource AND ogstatus = :ogstatus ORDER BY ident desc"
            )
        }
    )
public class OgsaJdbcResourceEntity
extends OgsaBaseResourceEntity<OgsaJdbcResource>
implements OgsaJdbcResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaJdbcResourceEntity";
    
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_RESOURCE_RESOURCE_COL = "resource";
    
    /**
     * {@link OgsaJdbcResource.EntityFactory} implementation.
     *
     */
    @Slf4j
    @Repository
    public static class EntityFactory
    extends OgsaBaseResourceEntity.EntityFactory<OgsaJdbcResource>
    implements OgsaJdbcResource.EntityFactory
        {
/*
        @Override
        @Value("${firethorn.ogsa.jdbc.resource.scan:PT1M}")
        public void scanperiod(final String value)
            {
            log.debug("scanperiod(String)");
            log.debug("  value      [{}]", value);
            super.scanperiod(
                value
                );
            }
 */
        @Override
        public Class<?> etype()
            {
            return OgsaJdbcResourceEntity.class;
            }

        @Override
        @SelectMethod
        public Iterable<OgsaJdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "OgsaJdbcResource-select-all"
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<OgsaJdbcResource> select(final OgsaService service)
            {
            return super.iterable(
                super.query(
                    "OgsaJdbcResource-select-service"
                    ).setEntity(
                        "service",
                        service
                        )
                );
            }

        @Override
        @SelectMethod
        public Iterable<OgsaJdbcResource> select(final JdbcResource resource)
            {
            return super.iterable(
                super.query(
                    "OgsaJdbcResource-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                    )
                );
            }

        @Override
        @SelectMethod
        public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource resource)
            {
            return super.iterable(
                super.query(
                    "OgsaJdbcResource-select-service-resource"
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
        public OgsaJdbcResource create(final JdbcResource resource)
            {
            log.debug("create(JdbcResource) [{}]", resource.ident());
            return create(
                factories().ogsa().services().entities().primary(),
                resource
                );
            }
        
        @Override
        @CreateMethod
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource resource)
            {
            log.debug("create(OgsaService, JdbcResource) [{}][{}]", service.ident(), resource.ident());
            return super.insert(
                new OgsaJdbcResourceEntity(
                    service,
                    resource
                    )
                );
            }
        
        @Override
        @CreateMethod
        public OgsaJdbcResource primary(final JdbcResource resource)
            {
            log.debug("primary(JdbcResource) [{}]", resource.ident());
            return primary(
                factories().ogsa().services().entities().primary(),
                resource
                );
            }

        @Override
        @CreateMethod
        public OgsaJdbcResource primary(OgsaService service, JdbcResource resource)
            {
            log.debug("primary(OgsaService, JdbcResource) [{}][{}]", service.ident(), resource.ident());
            // Really really simple - just get the first. 
            OgsaJdbcResource found = super.first(
                super.query(
                    "OgsaJdbcResource-select-service-resource-status"
                    ).setEntity(
                        "service",
                        service
                    ).setEntity(
                        "resource",
                        resource
                    ).setString(
                        "ogstatus",
                        OgsaStatus.ACTIVE.name()
                    )
                );
            if (found != null)
                {
                log.debug("Found primary OgsaJdbcResource [{}]", found.ident());

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
	                log.debug("Primary OgsaJdbcResource failed ping test, creating a new one");
	                return create(
	                    service,
	                    resource
	                    );
                	}
                }
            else {
                log.debug("No primary OgsaJdbcResource, creating a new one");
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
    implements OgsaJdbcResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static OgsaJdbcResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return OgsaJdbcResourceEntity.EntityServices.instance ;
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
            if (OgsaJdbcResourceEntity.EntityServices.instance == null)
                {
                OgsaJdbcResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private OgsaJdbcResource.IdentFactory idents;
        @Override
        public OgsaJdbcResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaJdbcResource.LinkFactory links;
        @Override
        public OgsaJdbcResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private OgsaJdbcResource.NameFactory names;
        @Override
        public OgsaJdbcResource.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private OgsaJdbcResource.EntityFactory entities;
        @Override
        public OgsaJdbcResource.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected OgsaJdbcResource.EntityFactory factory()
        {
        log.debug("factory()");
        return OgsaJdbcResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected OgsaJdbcResource.EntityServices services()
        {
        log.debug("services()");
        return OgsaJdbcResourceEntity.EntityServices.instance() ; 
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
    public OgsaJdbcResourceEntity()
        {
        super();
        }

    /**
     *
     * Public constructor.
     * @param service The parent {@link OgsaService}
     * @param resource  The source {@link JdbcResource}
     *
     */
    public OgsaJdbcResourceEntity(final OgsaService service, final JdbcResource resource)
        {
        super(
            service
            );
        this.resource = resource  ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource resource;
    @Override
    public JdbcResource resource()
        {
        return this.resource;
        }

    @Override
    protected OgsaStatus init()
        {
        log.debug("init()");
        log.debug("  name   [{}]", this.name());
        log.debug("  ident  [{}]", this.ident());
        log.debug("  ogsaid [{}]", this.ogsaid);
        JdbcCreateResourceWorkflow workflow = null;
        try {
            workflow = new JdbcCreateResourceWorkflow(
                service().endpoint()
                );
            }
        catch (MalformedURLException ouch)
            {
            return ogstatus(
                OgsaStatus.ERROR
                );
            }

        log.debug("Creating OGSA-DAI JDBC resource");
        log.debug("Executing JdbcCreateResourceWorkflow");

        final ResourceWorkflowResult response = workflow.execute(
            new JdbcCreateResourceWorkflow.Param()
                {
                @Override
                public String jdbcurl()
                    {
                    return resource.connection().uri();
                    }
                @Override
                public String username()
                    {
                    return resource.connection().user();
                    }
                @Override
                public String password()
                    {
                    return resource.connection().pass();
                    }
                @Override
                public String driver()
                    {
                    return resource.connection().driver();
                    }
                @Override
                public boolean writable()
                    {
                    return false;
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
    }
        
