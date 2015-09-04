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

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 * {@link OgsaExecResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = OgsaExecResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "OgsaExecResource-select-all",
            query = "FROM OgsaExecResourceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaExecResource-select-service",
            query = "FROM OgsaExecResourceEntity WHERE service = :service ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "OgsaExecResource-select-service-status",
            query = "FROM OgsaExecResourceEntity WHERE service = :service AND ogstatus = :ogstatus ORDER BY ident desc"
            )
        }
    )
public class OgsaExecResourceEntity
extends OgsaBaseResourceEntity<OgsaExecResource>
implements OgsaExecResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OgsaExecResourceEntity";
    
    /**
     * {@link OgsaExecResource.Services} implementation.
     * 
     */
    @Slf4j
    @Repository
    public static class Services
    implements OgsaExecResource.Services
        {

        /**
         * Our singleton instance.
         * 
         */
        private static Services instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static Services instance()
            {
            log.debug("instance()");
            return OgsaExecResourceEntity.Services.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected Services()
            {
            log.debug("Services()");
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (OgsaExecResourceEntity.Services.instance == null)
                {
                OgsaExecResourceEntity.Services.instance = this ;
                }
            else {
                log.error("Setting Services.instance more than once");
                throw new IllegalStateException(
                    "Setting Services.instance more than once"
                    );
                }
            }
        
        @Autowired
        private OgsaExecResource.IdentFactory idents;
        @Override
        public OgsaExecResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaExecResource.LinkFactory links;
        @Override
        public OgsaExecResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private OgsaExecResource.EntityFactory entities;
        @Override
        public OgsaExecResource.EntityFactory entities()
            {
            return this.entities;
            }
        }
    
    /**
     * {@link OgsaExecResource.EntityFactory} implementation.
     *
     */
    @Slf4j
    @Repository
    public static class EntityFactory
    extends OgsaBaseResourceEntity.EntityFactory<OgsaExecResource>
    implements OgsaExecResource.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return OgsaExecResourceEntity.class;
            }

        @Autowired
        private OgsaExecResource.IdentFactory idents;
        @Override
        public OgsaExecResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private OgsaExecResource.LinkFactory links;
        @Override
        public OgsaExecResource.LinkFactory links()
            {
            return this.links;
            }
        
        @Override
        @SelectMethod
        public Iterable<OgsaExecResource> select()
            {
            return super.iterable(
                super.query(
                    "OgsaExecResource-select-all"
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<OgsaExecResource> select(final OgsaService service)
            {
            return super.iterable(
                super.query(
                    "OgsaExecResource-select-service"
                    ).setEntity(
                        "service",
                        service
                        )
                );
            }
        
        @Override
        @CreateMethod
        public OgsaExecResource create(final OgsaService service)
            {
            log.debug("create(OgsaService) [{}]", service.ident());
            return super.insert(
                new OgsaExecResourceEntity(
                    service
                    )
                );
            }
        
        @Override
        @CreateMethod
        public OgsaExecResource primary(OgsaService service)
            {
            log.debug("primary(OgsaService) [{}]", service.ident());
            // Really really simple - just get the first. 
            OgsaExecResource found = super.first(
                super.query(
                    "OgsaExecResource-select-service-status"
                    ).setEntity(
                        "service",
                        service
                    ).setString(
                        "ogstatus",
                        OgsaStatus.ACTIVE.name()
                    )
                );
            if (found != null)
                {
                log.debug("Found primary OgsaExecResource [{}]", found.ident());
                return found ;
                }
            else {
                log.debug("No primary OgsaExecResource, creating a new one");
                return create(
                    service
                    );
                }
            }
        }

    @Override
    public OgsaExecResource.EntityFactory factory()
        {
        log.debug("factory()");
        return OgsaExecResourceEntity.Services.instance().entities() ; 
        }
    
    /**
     * Protected constructor. 
     *
     */
    public OgsaExecResourceEntity()
        {
        super();
        }

    /**
     *
     * Public constructor.
     * @param service The parent {@link OgsaService}
     *
     */
    public OgsaExecResourceEntity(final OgsaService service)
        {
        super(
            service
            );
        }

    @Override
    public String link()
        {
        return factories().ogsa().factories().exec().links().link(
            this
            );
        }

    @Override
	public BaseResource<?> resource()
		{
		return null;
		}

    @Override
    protected OgsaStatus init()
        {
        log.debug("init()");
        log.debug("  name   [{}]", this.name());
        log.debug("  ident  [{}]", this.ident());
        log.debug("  ogsaid [{}]", this.ogsaid);
        throw new NotImplementedException();
        }

    }
        
