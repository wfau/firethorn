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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;
import uk.ac.roe.wfau.firethorn.util.GenericIterable;

/**
 *
 *
 */
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
     * Hibernate column mapping, {@value}.
     * 
     */
    protected static final String DB_IVOAID_COL = "ivoaid";

    /**
     * {@link Entity.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<IvoaResource>
    implements IvoaResource.EntityFactory
        {

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
        public IvoaResource create(final String ivoaid)
            {
            return super.insert(
                new IvoaResourceEntity(
                    ivoaid,
                    ivoaid
                    )
                );
            }

        @Override
        @CreateMethod
        public IvoaResource create(final String ivoaid, final String name)
            {
            return super.insert(
                new IvoaResourceEntity(
                    ivoaid,
                    name
                    )
                );
            }

        @Autowired
        protected IvoaSchema.EntityFactory schemas;
        @Override
        public IvoaSchema.EntityFactory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected IvoaResource.IdentFactory idents;
        @Override
        public IvoaResource.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected IvoaResource.LinkFactory links;
        @Override
        public IvoaResource.LinkFactory links()
            {
            return this.links;
            }
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
    protected IvoaResourceEntity(final String ivoaid, final String name)
        {
        super(
            name
            );
        this.ivoaid = ivoaid;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_IVOAID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ivoaid;
    @Override
    public String ivoaid()
        {
        return this.ivoaid;
        }
    @Override
    public void ivoaid(final String ivoaid)
        {
        this.ivoaid = ivoaid ;
        }

    @Override
    public IvoaResource.Schemas schemas()
        {
        return new IvoaResource.Schemas()
            {
            @Override
            public Iterable<IvoaSchema> select()
                {
                return factories().ivoa().schemas().select(
                    IvoaResourceEntity.this
                    );
                }

            @Override
            public IvoaSchema select(String name)
            throws NameNotFoundException
                {
                return factories().ivoa().schemas().select(
                    IvoaResourceEntity.this,
                    name
                    );
                }

            @Override
            public IvoaSchema search(final String name)
                {
                return factories().ivoa().schemas().search(
                    IvoaResourceEntity.this,
                    name
                    );
                }

            @Override
            public IvoaSchema.Builder builder()
                {
                return new IvoaSchemaEntity.Builder(this.select())
                    {
                    @Override
                    protected IvoaSchema create(final IvoaSchema.Metadata param)
                        throws DuplicateEntityException
                        {
                        return factories().ivoa().schemas().create(
                            IvoaResourceEntity.this,
                            param
                            );
                        }
                    };
                }
            };
        }

    @Override
    public String link()
        {
        return factories().ivoa().resources().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub
        }

    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "resource",
        targetEntity = IvoaEndpointEntity.class
        )
    private Set<Endpoint> endpoints = new HashSet<Endpoint>();
    
    @Override
    public Endpoints endpoints()
        {
        return new Endpoints()
            {
            @Override
            public Endpoint create(String url)
                {
                Endpoint endpoint = new IvoaEndpointEntity(
                    IvoaResourceEntity.this,
                    url
                    );
                 endpoints.add(
                     endpoint
                    );
                return endpoint;
                }
            @Override
            public Iterable<IvoaResource.Endpoint> select()
                {
                return new GenericIterable<IvoaResource.Endpoint, Endpoint>(
                    endpoints
                    ); 
                }
            };
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
                {
                return factories().ogsa().factories().ivoa().primary(
                    IvoaResourceEntity.this
                    );
                }

            @Override
            public Iterable<OgsaIvoaResource> select()
                {
                return factories().ogsa().factories().ivoa().select(
                    IvoaResourceEntity.this
                    );
                }
            };
        }
    }

