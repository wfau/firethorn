/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.adql.parser.BaseTranslator;
import uk.ac.roe.wfau.firethorn.adql.parser.TAPServiceTranslator;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;

/**
 * {@link IvoaResource} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaResource-select-all",
            query = "FROM IvoaResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "IvoaResource-select-ivoaid",
            query = "FROM IvoaResourceEntity WHERE ivoaid = :ivoaid ORDER BY name asc, ident desc"
            )
        }
    )
public class IvoaResourceEntity
    extends BaseResourceEntity<IvoaResource, IvoaSchema>
    implements IvoaResource
    {
    /**
     * Hibernate table mapping, {@value}.
     * 
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaResourceEntity";

    /**
     * {@link IvoaResource.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseResourceEntity.EntityFactory<IvoaResource>
    implements IvoaResource.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }

        @Override
        public Class<?> etype()
            {
            return IvoaResourceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<IvoaResource> select()
            {
            return super.iterable(
                super.query(
                    "IvoaResource-select-all"
                    )
                );
            }

        @Override
        @CreateMethod
        public IvoaResource create(final String endpoint)
            {
            return super.insert(
                new IvoaResourceEntity(
                    null,
                    endpoint
                    )
                );
            }

        @Override
        @CreateMethod
        public IvoaResource create(final String name, final String endpoint)
            {
            return super.insert(
                new IvoaResourceEntity(
                    name,
                    endpoint
                    )
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
    implements IvoaResource.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static IvoaResourceEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return IvoaResourceEntity.EntityServices.instance ;
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
            if (IvoaResourceEntity.EntityServices.instance == null)
                {
                IvoaResourceEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private IvoaResource.IdentFactory idents;
        @Override
        public IvoaResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private IvoaResource.LinkFactory links;
        @Override
        public IvoaResource.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private IvoaResource.NameFactory names;
        @Override
        public IvoaResource.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private IvoaResource.EntityFactory entities;
        @Override
        public IvoaResource.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private IvoaSchema.EntityFactory schemas;
		@Override
		public IvoaSchema.EntityFactory schemas()
			{
			return this.schemas;
			}
        }

    @Override
    protected IvoaResource.EntityFactory factory()
        {
        log.debug("factory()");
        return IvoaResourceEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected IvoaResource.EntityServices services()
        {
        log.debug("services()");
        return IvoaResourceEntity.EntityServices.instance() ; 
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
    protected IvoaResourceEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaResourceEntity(final String name, final String url)
        {
        super(
            name
            );
        if (url!= null)
            {
            this.endpoint = new IvoaEndpointEntity(
                this, 
                url
                );
            }
        }

    @Override
    public IvoaResource.Schemas schemas()
    throws ProtectionException
        {
        log.debug("schemas() for [{}][{}]", ident(), namebuilder());
        scan();
        return new IvoaResource.Schemas()
            {
            @Override
            public Iterable<IvoaSchema> select()
            throws ProtectionException
                {
                return factories().ivoa().schemas().entities().select(
                    IvoaResourceEntity.this
                    );
                }

            @Override
            public IvoaSchema select(String name)
            throws ProtectionException, NameNotFoundException
                {
                return factories().ivoa().schemas().entities().select(
                    IvoaResourceEntity.this,
                    name
                    );
                }

            @Override
            public IvoaSchema search(final String name)
            throws ProtectionException
                {
                return factories().ivoa().schemas().entities().search(
                    IvoaResourceEntity.this,
                    name
                    );
                }

            @Override
            public IvoaSchema.Builder builder()
            throws ProtectionException
                {
                return new IvoaSchemaEntity.Builder(this.select())
                    {
                    @Override
                    protected IvoaSchema create(final IvoaSchema.Metadata param)
                    throws DuplicateEntityException, ProtectionException
                        {
                        return factories().ivoa().schemas().entities().create(
                            IvoaResourceEntity.this,
                            param
                            );
                        }
                    };
                }
            };
        }

    @Override
    protected void scanimpl()
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        log.debug("do nothing ..");
        // TODO Auto-generated method stub
        }
   
    @Embedded
    private IvoaEndpointEntity endpoint ;

    @Override
    public Endpoint endpoint()
        {
        return this.endpoint;
        }

    @Override
    public IvoaResource.Metadata meta()
        {
        return new IvoaResource.Metadata()
            {
            @Override
            public IvoaResource.Metadata.Ivoa ivoa()
                {
                return new Ivoa()
                    {
                    };
                }
            };
        }

    @Override
    public OgsaIvoaResources ogsa()
        {
        return new OgsaIvoaResources()
            {
            @Override
            public OgsaIvoaResource primary()
            throws ProtectionException
                {
                return factories().ogsa().ivoa().entities().primary(
                    IvoaResourceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select()
            throws ProtectionException
                {
                return factories().ogsa().ivoa().entities().select(
                    IvoaResourceEntity.this
                    );
                }
            };
        }

    @Override
    public BaseTranslator translator()
        {
        return new TAPServiceTranslator();
        }
    }

