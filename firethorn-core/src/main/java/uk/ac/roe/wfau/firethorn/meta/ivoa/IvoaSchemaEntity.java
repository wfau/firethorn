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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

/**
 * {@link IvoaSchema} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaSchemaEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = IvoaSchemaEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                IvoaSchemaEntity.DB_NAME_COL,
                IvoaSchemaEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaSchema-select-parent",
            query = "FROM IvoaSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "IvoaSchema-select-parent.name",
            query = "FROM IvoaSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "IvoaSchema-search-parent.text",
            query = "FROM IvoaSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class IvoaSchemaEntity
    extends BaseSchemaEntity<IvoaSchema, IvoaTable>
    implements IvoaSchema
    {
    /**
     * Hibernate table mapping, {@value}.
     * 
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaSchemaEntity";

    /**
     * {@link IvoaSchema.Builder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<IvoaSchema, IvoaSchema.Metadata>
    implements IvoaSchema.Builder
        {
        /**
         * Public constructor.
         *
         */
        public Builder(final Iterable<IvoaSchema> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(IvoaSchema.Metadata meta)
            {
            return meta.name();
            }

        @Override
        protected void update(final IvoaSchema schema, final IvoaSchema.Metadata meta)
            {
            schema.update(
                meta
                );
            }
        }

    /**
     * {@link Entity.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseSchemaEntity.EntityFactory<IvoaResource, IvoaSchema>
    implements IvoaSchema.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaSchemaEntity.class ;
            }

        @Override
        @CreateMethod
        public IvoaSchema create(final IvoaResource parent, final IvoaSchema.Metadata meta)
            {
            return this.insert(
                new IvoaSchemaEntity(
                    parent,
                    meta
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<IvoaSchema> select(final IvoaResource parent)
            {
            return super.list(
                super.query(
                    "IvoaSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public IvoaSchema select(final IvoaResource parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "IvoaSchema-select-parent.name"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "name",
                            name
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate schema [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public IvoaSchema search(final IvoaResource parent, final String name)
            {
            return super.first(
                super.query(
                    "IvoaSchema-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
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
    implements IvoaSchema.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static IvoaSchemaEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return IvoaSchemaEntity.EntityServices.instance ;
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
            if (IvoaSchemaEntity.EntityServices.instance == null)
                {
                IvoaSchemaEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private IvoaSchema.IdentFactory idents;
        @Override
        public IvoaSchema.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private IvoaSchema.LinkFactory links;
        @Override
        public IvoaSchema.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private IvoaSchema.NameFactory names;
        @Override
        public IvoaSchema.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private IvoaSchema.EntityFactory entities;
        @Override
        public IvoaSchema.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private IvoaTable.EntityFactory tables;
		@Override
		public IvoaTable.EntityFactory tables()
			{
			return this.tables;
			}
        }

    @Override
    protected IvoaSchema.EntityFactory factory()
        {
        log.debug("factory()");
        return IvoaSchemaEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected IvoaSchema.EntityServices services()
        {
        log.debug("services()");
        return IvoaSchemaEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    protected IvoaSchemaEntity()
        {
        super();
        }

    protected IvoaSchemaEntity(final IvoaResource resource, IvoaSchema.Metadata meta)
        {
        super(
            resource,
            meta.name()
            );
        this.resource = resource;
        this.update(
            meta
            );
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
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
    public BaseSchema<?, ?> base()
        {
        return self();
        }
    @Override
    public BaseSchema<?, ?> root()
        {
        return self();
        }

    @Override
    public IvoaSchema.Tables tables()
        {
        log.debug("tables() for [{}][{}]", ident(), namebuilder());
        scan();
        return new IvoaSchema.Tables()
            {
            @Override
            public Iterable<IvoaTable> select()
                {
                return factories().ivoa().tables().entities().select(
                    IvoaSchemaEntity.this
                    );
                }
            @Override
            public IvoaTable select(final String name)
            throws NameNotFoundException
                {
                return factories().ivoa().tables().entities().select(
                    IvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public IvoaTable search(final String name)
                {
                return factories().ivoa().tables().entities().search(
                    IvoaSchemaEntity.this,
                    name
                    );
                }
            @Override
            public IvoaTable select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                return factories().ivoa().tables().entities().select(
                    ident
                    );
                }

            @Override
            public IvoaTable.Builder builder()
                {
                return new IvoaTableEntity.Builder(this.select())
                    {
                    @Override
                    public IvoaTable create(final IvoaTable.Metadata meta)
                    throws DuplicateEntityException
                        {
                        return factories().ivoa().tables().entities().create(
                            IvoaSchemaEntity.this,
                            meta
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
        // TODO Auto-generated method stub
        }

    /**
     * Generate the IVOA metadata.
     * 
     */
    protected IvoaSchema.Metadata.Ivoa ivoameta()
        {
        return new IvoaSchema.Metadata.Ivoa()
            {
            @Override
            public String name()
                {
                return IvoaSchemaEntity.this.name();
                }

            @Override
            public String title()
                {
                return IvoaSchemaEntity.this.name();
                }

            @Override
            public String text()
                {
                return IvoaSchemaEntity.this.text();
                }

            @Override
            public String utype()
                {
                return IvoaSchemaEntity.this.adqlutype();
                }
            };
        }
    
    @Override
    public IvoaSchema.Metadata meta()
        {
        return new IvoaSchema.Metadata()
            {
            @Override
            public String name()
                {
                return IvoaSchemaEntity.this.name();
                }

            @Override
            public Adql adql()
                {
                return adqlmeta();
                }

            @Override
            public Ivoa ivoa()
                {
                return ivoameta();
                }
            };
        }

    @Override
    public void update(final IvoaSchema.Metadata meta)
        {
        if (meta.ivoa() != null)
            {
            this.update(
                meta.ivoa()
                );
            }
        }

    @Override
    public void update(final IvoaSchema.Metadata.Ivoa ivoa)
        {
        if (ivoa.text() != null)
            {
            this.text(ivoa.text());
            }
        if (ivoa.utype() != null)
            {
            this.adqlutype(ivoa.utype());
            }
        }
    }
