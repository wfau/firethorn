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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp.CreateFireThornDQPClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp.CreateFireThornDQPWorkflow;

/**
 * {@link OgsaDQPResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaDQPResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaDQPResource-select-all",
            query = "FROM OgsaDQPResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaDQPResource-select-service",
            query = "FROM OgsaDQPResourceEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaDQPResource-select-service-active",
            query = "FROM OgsaDQPResourceEntity WHERE service = :service AND ogstatus = 'ACTIVE' ORDER BY ident desc"
            )
        }
    )
public class OgsaDQPResourceEntity
    extends OgsaBaseResourceEntity<OgsaDQPResource>
    implements OgsaDQPResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaDQPResourceEntity";

    /**
     * {@link OgsaDQPResource.EntityFactory} implementation.
     *
     */
    @Component
    @Repository
    public static class OgsaDQPResourceEntityFactory
    extends OgsaBaseResourceEntity.EntityFactory<OgsaDQPResource>
    implements OgsaDQPResource.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return OgsaDQPResourceEntity.class;
            }

        @Override
        @SelectMethod
        public Iterable<OgsaDQPResource> select()
            {
            return super.iterable(
                super.query(
                    "OgsaDQPResource-select-all"
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<OgsaDQPResource> select(final OgsaService service)
            {
            return super.iterable(
                super.query(
                    "OgsaDQPResource-select-service"
                    ).setEntity(
                        "service",
                        service
                        )
                );
            }

        @Override
        @CreateMethod
        public OgsaDQPResource create(final OgsaService service)
            {
            return super.insert(
                new OgsaDQPResourceEntity(
                    service
                    )
                );
            }

        @Override
        @CreateMethod
        public OgsaDQPResource primary(OgsaService service)
        throws ProtectionException
            {
            // TODO Do something similar for the other resource types.
            // Get the list of active resources from the database.
            log.debug("Requesting list of [ACTIVE] DQP resources.");
            Iterable<OgsaDQPResource> iter = super.iterable(
                super.query(
                    "OgsaDQPResource-select-service-active"
                    ).setEntity(
                        "service",
                        service
                    )
                );
            //
            // Return the first active one we find.
            log.debug("Processing list of [ACTIVE] DQP resources.");
            for (OgsaDQPResource next : iter)
                {
                log.debug("Checking [ACTIVE] DQP resource [{}][{}]", next.ident(), next.ogstatus());
                // Calling ogsaid() triggers a scan.
                // TODO Do something similar for the other resource types.
                next.ogsaid();
                if (next.ogstatus() == OgsaStatus.ACTIVE)
                    {
                    log.debug("Found [ACTIVE] DQP resource [{}][{}]", next.ident(), next.ogstatus());
                    return next ;
                    }
                }
            //
            // If we didn't find an active one, create a new one.
            log.debug("Creating new DQP resource");
            return create(
                service
                );
            }

        @Autowired
        private OgsaService.EntityFactory services ;

        @Override
        public OgsaDQPResource primary()
        throws ProtectionException
            {
            return primary(
                services.primary()
                );
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements OgsaDQPResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static OgsaDQPResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return OgsaDQPResourceEntity.EntityServices.instance ;
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
            if (OgsaDQPResourceEntity.EntityServices.instance == null)
                {
                OgsaDQPResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private OgsaDQPResource.IdentFactory idents;
        @Override
        public OgsaDQPResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaDQPResource.LinkFactory links;
        @Override
        public OgsaDQPResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private OgsaDQPResource.EntityFactory entities;
        @Override
        public OgsaDQPResource.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected OgsaDQPResource.EntityFactory factory()
        {
        log.debug("factory()");
        return OgsaDQPResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected OgsaDQPResource.EntityServices services()
        {
        log.debug("services()");
        return OgsaDQPResourceEntity.EntityServices.instance() ; 
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
    protected OgsaDQPResourceEntity()
        {
        super();
        }

   /**
     *
     * Public constructor.
     * @param service The parent {@link OgsaService}
     *
     */
    public OgsaDQPResourceEntity(final OgsaService service)
        {
        super(
            service
            );
        }

    @Override
    public BaseResource<?> resource()
        {
        // TODO Remove this from the base class.
        // A DQP is not linked to a BaseResource resource.
        throw new NotImplementedException();
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
            // Create our OGSA-DAI resource.
            CreateFireThornDQPWorkflow workflow = null;
            try {
                workflow = new CreateFireThornDQPWorkflow(
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
                new CreateFireThornDQPClient.Param()
                    {
                    @Override
                    public String target()
                        { 
                        // TODO Auto-generated method stub
                        return "anon-resource";
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
    }
