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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;

/**
 * {@link IvoaTable} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = IvoaTableEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = IvoaTableEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                IvoaTableEntity.DB_NAME_COL,
                IvoaTableEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "IvoaTable-select-parent",
            query = "FROM IvoaTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaTable-select-parent.name",
            query = "FROM IvoaTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "IvoaTable-search-parent.text",
            query = "FROM IvoaTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class IvoaTableEntity
    extends BaseTableEntity<IvoaTable, IvoaColumn>
    implements IvoaTable
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IvoaTableEntity";

    /**
     * {@link IvoaTable.Builder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<IvoaTable, IvoaTable.Metadata>
    implements IvoaTable.Builder
        {
        /**
         * Public constructor.
         *
         */
        public Builder(final Iterable<IvoaTable> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(IvoaTable.Metadata meta)
            {
            return meta.name();
            }

        @Override
        protected void update(final IvoaTable table, final IvoaTable.Metadata meta)
            {
            table.update(
                meta
                );
            }
        }

    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "IVOA_TABLE";

    /**
     * The default alias prefix, {@value}.
     * 
     */
    protected static final String ALIAS_PREFIX = "IVOA_TABLE_";
    
    /**
     * {@link IvoaTable.NameFactory} implementation.
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<IvoaTable>
    implements IvoaTable.NameFactory
        {
        @Override
        public String name()
            {
            return datename(
                NAME_PREFIX
                );
            }
        }
    
    /**
     * {@link IvoaTable.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements IvoaTable.AliasFactory
        {

        @Override
        public String alias(final IvoaTable entity)
            {
            return ALIAS_PREFIX.concat(
                entity.ident().toString()
                );
            }

        @Override
        public boolean matches(String alias)
            {
            return alias.startsWith(
                ALIAS_PREFIX
                );
            }
        
        @Override
        public IvoaTable resolve(String alias)
            throws EntityNotFoundException
            {
            return entities.select(
                idents.ident(
                    alias.substring(
                        ALIAS_PREFIX.length()
                        )
                    )
                );
            }

        /**
         * Our {@link IvoaTable.IdentFactory}.
         * 
         */
        @Autowired
        private IvoaTable.IdentFactory idents ;

        /**
         * Our {@link IvoaTable.EntityFactory}.
         * 
         */
        @Autowired
        private IvoaTable.EntityFactory entities;
        
        }

    /**
     * {@link Entity.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseTableEntity.EntityFactory<IvoaSchema, IvoaTable>
    implements IvoaTable.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return IvoaTableEntity.class ;
            }

        @Override
        @CreateMethod
        public IvoaTable create(final IvoaSchema parent, final IvoaTable.Metadata meta)
            {
            return this.insert(
                new IvoaTableEntity(
                    parent,
                    meta
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<IvoaTable> select(final IvoaSchema parent)
            {
            return super.list(
                super.query(
                    "IvoaTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public IvoaTable select(final IvoaSchema parent, final String name)
        throws NameNotFoundException
            {
            try
                {
                return super.single(
                    super.query(
                        "IvoaTable-select-parent.name"
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
                log.debug("Unable to locate table [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public IvoaTable search(final IvoaSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "IvoaTable-select-parent.name"
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
    implements IvoaTable.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static IvoaTableEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return IvoaTableEntity.EntityServices.instance ;
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
            if (IvoaTableEntity.EntityServices.instance == null)
                {
                IvoaTableEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private IvoaTable.IdentFactory idents;
        @Override
        public IvoaTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private IvoaTable.LinkFactory links;
        @Override
        public IvoaTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private IvoaTable.NameFactory names;
        @Override
        public IvoaTable.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private IvoaTable.EntityFactory entities;
        @Override
        public IvoaTable.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private IvoaTable.AliasFactory aliases;
		@Override
		public IvoaTable.AliasFactory aliases()
			{
			return this.aliases;
			}

        @Autowired
		private IvoaColumn.EntityFactory columns;
		@Override
		public IvoaColumn.EntityFactory columns()
			{
			return this.columns;
			}
        }

    @Override
    protected IvoaTable.EntityFactory factory()
        {
        log.debug("factory()");
        return IvoaTableEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected IvoaTable.EntityServices services()
        {
        log.debug("services()");
        return IvoaTableEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    @Override
    public String alias()
        {
        return services().aliases().alias(
            this
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaTableEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected IvoaTableEntity(final IvoaSchema schema, final IvoaTable.Metadata meta)
        {
        super(
            schema, 
            meta.name()
            );
        this.schema = schema;
        this.update(
            meta
            );
        }
    
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IvoaSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private IvoaSchema schema;
    @Override
    public IvoaSchema schema()
        {
        return this.schema;
        }
    @Override
    public IvoaResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public IvoaTable base()
        {
        return self();
        }
    @Override
    public IvoaTable root()
        {
        return self();
        }

    @Override
    public IvoaTable.Columns columns()
        {
        log.debug("columns() for [{}][{}]", ident(), namebuilder());
        scan();
        return new IvoaTable.Columns()
            {
            @Override
            public Iterable<IvoaColumn> select()
                {
                return factories().ivoa().columns().entities().select(
                    IvoaTableEntity.this
                    );
                }

            @Override
            public IvoaColumn select(final String name)
            throws NameNotFoundException
                {
                return factories().ivoa().columns().entities().select(
                    IvoaTableEntity.this,
                    name
                    );
                }

            @Override
            public IvoaColumn search(final String name)
                {
                return factories().ivoa().columns().entities().search(
                    IvoaTableEntity.this,
                    name
                    );
                }

            @Override
            public IvoaColumn select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                //TODO Add the parent reference.
                return factories().ivoa().columns().entities().select(
                    ident
                    );
                }

            @Override
            public IvoaColumn.Builder builder()
                {
                return new IvoaColumnEntity.Builder(this.select())
                    {
                    @Override
                    protected IvoaColumn create(final IvoaColumn.Metadata meta)
                        throws DuplicateEntityException
                        {
                        return factories().ivoa().columns().entities().create(
                            IvoaTableEntity.this,
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
    protected IvoaTable.Metadata.Ivoa ivoameta()
        {
        return new IvoaTable.Metadata.Ivoa()
            {
            @Override
            public String name()
                {
                return IvoaTableEntity.this.name();
                }

            @Override
            public String title()
                {
                return IvoaTableEntity.this.name();
                }

            @Override
            public String text()
                {
                return IvoaTableEntity.this.text();
                }

            @Override
            public String utype()
                {
                return IvoaTableEntity.this.adqlutype();
                }
            };
        }
    
    @Override
    public IvoaTable.Metadata meta()
        {
        return new IvoaTable.Metadata()
            {
            @Override
            public String name()
                {
                return IvoaTableEntity.this.name();
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
    public void update(final IvoaTable.Metadata meta)
        {
        if (meta.ivoa() != null)
            {
            this.update(
                meta.ivoa()
                );
            }
        }

    @Override
    public  void update(final IvoaTable.Metadata.Ivoa ivoa)
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

    @Override
    @Deprecated
    public GreenQuery greenquery()
        {
        return null;
        }

    @Override
    public BlueQuery bluequery()
        {
        return null;
        }
    }
