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
package uk.ac.roe.wfau.firethorn.meta.adql;

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
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.QueryParam;
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * {@link AdqlSchema} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlSchemaEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = AdqlSchemaEntity.DB_PARENT_COL
            ),
        @Index(
            columnList = AdqlSchemaEntity.DB_BASE_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AdqlSchemaEntity.DB_NAME_COL,
                AdqlSchemaEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlSchema-select-parent",
            query = "FROM AdqlSchemaEntity WHERE parent = :parent ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "AdqlSchema-select-parent.name",
            query = "FROM AdqlSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "AdqlSchema-search-parent.text",
            query = "FROM AdqlSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident asc"
            )
        }
    )
public class AdqlSchemaEntity
extends BaseSchemaEntity<AdqlSchema, AdqlTable>
implements AdqlSchema
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlSchemaEntity";

    /**
     * {@link AdqlSchema.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseSchemaEntity.EntityFactory<AdqlResource, AdqlSchema>
    implements AdqlSchema.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlSchemaEntity.class ;
            }

        @Override
        @CreateMethod
        public AdqlSchema create(final AdqlResource parent, final String name)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @CreateMethod
        public AdqlSchema create(final AdqlResource parent, final BaseSchema<?, ?> base)
            {
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                parent,
                base.name(),
                base
                );
            super.insert(
                schema
                );
            schema.realize();
            return schema;
            }

        @Override
        @CreateMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseSchema<?, ?> base)
            {
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                depth,
                parent,
                base.name(),
                base
                );
            super.insert(
                schema
                );
            schema.realize();
            return schema;
            }

        @Override
        @CreateMethod
		public AdqlSchema create(final AdqlResource parent, final String name, final BaseSchema<?, ?> base)
			{
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                parent,
                name,
                base                
                );
            super.insert(
                schema
                );
            schema.realize();
            return schema;
			}

        @Override
        @CreateMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name, final BaseSchema<?, ?> base)
            {
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                depth,
                parent,
                name,
                base
                );
            super.insert(
                schema
                );
            schema.realize();
            return schema;
            }

        @Override
        @CreateMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name, final BaseTable<?, ?> base)
            {
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                parent,
                name                
                );
            super.insert(
                schema
                );
            schema.realize(
                depth,
                base
                );
            return schema;
            }

        @Override
        @CreateMethod
        public AdqlSchema create(final AdqlResource parent, final String name, final BaseTable<?, ?> base)
            {
            final AdqlSchemaEntity schema = new AdqlSchemaEntity(
                parent,
                name                
                );
            super.insert(
                schema
                );
            schema.realize(
                base
                );
            return schema;
            }

        @Override
        @SelectMethod
        public Iterable<AdqlSchema> select(final AdqlResource parent)
            {
            return super.list(
                super.query(
                    "AdqlSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public AdqlSchema select(final AdqlResource parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "AdqlSchema-select-parent.name"
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
        public AdqlSchema search(final AdqlResource parent, final String name)
            {
            return super.first(
                super.query(
                    "AdqlSchema-select-parent.name"
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
    implements AdqlSchema.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AdqlSchemaEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AdqlSchemaEntity.EntityServices.instance ;
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
            if (AdqlSchemaEntity.EntityServices.instance == null)
                {
                AdqlSchemaEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AdqlSchema.IdentFactory idents;
        @Override
        public AdqlSchema.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlSchema.LinkFactory links;
        @Override
        public AdqlSchema.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlSchema.NameFactory names;
        @Override
        public AdqlSchema.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlSchema.EntityFactory entities;
        @Override
        public AdqlSchema.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
        private AdqlTable.EntityFactory tables;
		@Override
		public AdqlTable.EntityFactory tables()
			{
			return this.tables;
			}
        }

    @Override
    protected AdqlSchema.EntityFactory factory()
        {
        log.debug("factory()");
        return AdqlSchemaEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AdqlSchema.EntityServices services()
        {
        log.debug("services()");
        return AdqlSchemaEntity.EntityServices.instance() ; 
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
    protected AdqlSchemaEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlSchemaEntity(final AdqlResource resource, final String name)
        {
        this(
            CopyDepth.FULL,
            resource,
            name,
            null
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlSchemaEntity(final AdqlResource resource, final String name, final BaseSchema<?, ?> base)
        {
        this(
            CopyDepth.FULL,
            resource,
            name,
            base
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlSchemaEntity(final CopyDepth depth, final AdqlResource resource, final String name, final BaseSchema<?, ?> base)
        {
        super(
            depth,
            resource,
            name
            );
        this.base = base;
        this.resource = resource;
        }

    /**
     * Create a copy of a base table, with CopyDepth set to FULL. 
     *
     */
    protected void realize(final BaseTable<?, ?> base)
        {
        realize(
            CopyDepth.FULL,
            base
            );
        }

    /**
     * Create a copy of a base table.
     * @todo Delay the full scan until the data is actually requested.
     *
     */
    protected void realize(final CopyDepth depth, final BaseTable<?, ?> base)
        {
        log.debug("realize(CopyDepth, BaseTable) [{}][{}][{}][{}][{}]", ident(), name(), depth, base.ident(), base.name());
        factories().adql().tables().entities().create(
            depth,
            AdqlSchemaEntity.this,
            base
            );
        }

    /**
     * Convert this into a full copy.
     * @todo Nested full .. or thin ?
     * @todo Prevent this happening twice.
     * @todo Delay the full scan until the data is actually requested.
     * @todo THIN is fine .. once we fix scan() on JdbcTable
     *
     */
    protected void realize()
        {
        log.debug("realize() [{}][{}]", ident(), name());
        if (this.depth == CopyDepth.FULL)
            {
            if (this.base != null)
                {
                for (final BaseTable<?,?> table : base.tables().select())
                    {
                    log.debug("Importing base table [{}][{}]", table.ident(), table.name());
                    realize(
                        CopyDepth.FULL,
                        table
                        );
                    }
                }
            }
        }

    /**
     * Our base schema.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = BaseSchemaEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private BaseSchema<?, ?> base ;

    @Override
    public BaseSchema<?, ?> base()
        {
        if (this.base != null)
            {
            return this.base;
            }
        else {
            return this ;
            }
        }

    @Override
    public BaseSchema<?, ?> root()
        {
        if (this.base != null)
            {
            return this.base.root();
            }
        else {
            return this ;
            }
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private AdqlResource resource;
    @Override
    public AdqlResource resource()
        {
        return this.resource;
        }

    @Override
    public AdqlSchema.Tables tables()
        {
        log.debug("tables() for [{}][{}]", ident(), namebuilder());
        scan();
        return new AdqlSchema.Tables()
            {
            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlTable> select()
                {
                log.debug("tables().select() [{}][{}][{}][{}]", ident(), name(), depth(), base());
                if (depth() == CopyDepth.THIN)
                    {
                    return new AdqlTableProxy.ProxyIterable(
                        (Iterable<BaseTable<?,?>>) base().tables().select(),
                        AdqlSchemaEntity.this
                        );
                    }
                else {
                    return factories().adql().tables().entities().select(
                        AdqlSchemaEntity.this
                        );
                    }
                }

            @Override
            public AdqlTable search(final String name)
                {
                if (depth() == CopyDepth.THIN)
                    {
                    BaseTable<?,?> found = base().tables().search(
                        name
                        );
                    if (found != null)
                        {
                        return new AdqlTableProxy(
                            found,
                            AdqlSchemaEntity.this
                            );
                        }
                    else {
                        return null ;
                        }
                    }
                else {
                    return factories().adql().tables().entities().search(
                        AdqlSchemaEntity.this,
                        name
                        );
                    }
                }

            @Override
            public AdqlTable select(final String name)
            throws NameNotFoundException
                {
                if (depth() == CopyDepth.THIN)
                    {
                    return new AdqlTableProxy(
                        base().tables().select(
                            name
                            ),
                        AdqlSchemaEntity.this
                        );
                    }
                else {
                    return factories().adql().tables().entities().select(
                        AdqlSchemaEntity.this,
                        name
                        );
                    }
                }

            @Override
            public AdqlTable create(final CopyDepth depth, final BaseTable<?, ?> base)
                {
                return factories().adql().tables().entities().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base
                    );
                }

            @Override
            public AdqlTable create(final BaseTable<?,?> base)
                {
                return factories().adql().tables().entities().create(
                    AdqlSchemaEntity.this,
                    base
                    );
                }

            @Override
            public AdqlTable create(final CopyDepth depth, final BaseTable<?, ?> base, final String name)
                {
                return factories().adql().tables().entities().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                }

            @Override
            public AdqlTable create(final BaseTable<?,?> base, final String name)
                {
                return factories().adql().tables().entities().create(
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                }

            @Override
            public AdqlTable create(final AdqlQuery query)
                {
                return factories().adql().tables().entities().create(
                    AdqlSchemaEntity.this,
                    query
                    );
                }
            
            @Override
            public AdqlTable create(final CopyDepth depth, final BaseTable<?, ?> base, final BlueQuery bluequery)
                {
      
                return factories().adql().tables().entities().create(
                        depth,
                        AdqlSchemaEntity.this,
                        base,
                        bluequery
                        );
                }

            @Override
            public AdqlTable select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                log.debug("tables().select(Identifier) [{}] from [{}]", ident, ident());
                log.debug(" Schema depth [{}]", depth());
                if (depth() == CopyDepth.THIN)
                    {
                    if (ident instanceof ProxyIdentifier)
                        {
                        final ProxyIdentifier proxy = (ProxyIdentifier) ident;
                        log.debug("  Ident is a proxy");
                        log.debug("  Checking ident parent [{}]", proxy.parent());
                        if (ident().equals(proxy.parent()))
                            {
                            log.debug("  Parent is us :-)");
                            }
                        else {
                            log.error("  Parent is NOT us :-(");
                            }
                        log.debug("  Selecting [{}] from base [{}]", proxy.base(), base.ident());
                        return new AdqlTableProxy(
                            base().tables().select(
                                proxy.base()
                                ),
                            AdqlSchemaEntity.this
                            );
                        }
                    else {
                        log.debug("  Selecting [{}] from base [{}]", ident, base.ident());
                        return new AdqlTableProxy(
                            base().tables().select(
                                ident
                                ),
                            AdqlSchemaEntity.this
                            );
                        }
                    }
                else {
                    // TODO pass parent reference too.
                    return factories().adql().tables().entities().select(
                        ident
                        );
                    }
                }

            @Override
            public AdqlTable inport(final String name)
            throws NameNotFoundException
                {
                log.debug("tables().inport(String)");
                log.debug("  name [{}]", name);
                if ((depth() == CopyDepth.PARTIAL) || (depth() == CopyDepth.FULL))
                    {
                    AdqlTable table = search(
                        name
                        );
                    if (table != null)
                        {
                        log.debug("Found existing table [{}][{}]", table.ident(), table.name());
                        }
                    else {
                        table = create(
                            CopyDepth.PARTIAL,
                            base().tables().select(
                                name
                                )
                            );
                        log.debug("Created new table [{}][{}]", table.ident(), table.name());
                        }
                    return table ;
                    }
                else {
                    throw new UnsupportedOperationException(
                        "Import only available on FULL or PARTIAL nodes"
                        );
                    }
                }
            };
        }

    @Override
    public Queries queries()
        {
        return new Queries()
            {
            @Override
            public AdqlQuery create(final QueryParam param, final String query)
            throws QueryProcessingException
                {
                return factories().adql().queries().entities().create(
                    AdqlSchemaEntity.this,
                    param,
                    query
                    );
                }

            @Override
            public AdqlQuery create(final QueryParam param, final String query, final String name)
            throws QueryProcessingException
                {
                return factories().adql().queries().entities().create(
                    AdqlSchemaEntity.this,
                    param,
                    query,
                    name
                    );
                }

            @Override
            public Iterable<AdqlQuery> select()
                {
                return factories().adql().queries().entities().select(
                    AdqlSchemaEntity.this
                    );
                }
            };
        }

    @Override
    protected void scanimpl()
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        // TODO Auto-generated method stub
        }
    }
