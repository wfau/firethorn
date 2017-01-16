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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;

/**
 * {@link AdqlTable} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlTableEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = AdqlTableEntity.DB_BASE_COL
            ),
        @Index(
            columnList = AdqlTableEntity.DB_PARENT_COL 
            ),
        @Index(
            columnList = AdqlTableEntity.DB_GREEN_QUERY_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AdqlTableEntity.DB_NAME_COL,
                AdqlTableEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlTable-select-parent",
            query = "FROM AdqlTableEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlTable-select-parent.name",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "AdqlTable-search-parent.text",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident asc"
            )
        }
    )
public class AdqlTableEntity
    extends BaseTableEntity<AdqlTable, AdqlColumn>
    implements AdqlTable
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlTableEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_GREEN_QUERY_COL = "adqlquery" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_BLUE_QUERY_COL = "bluequery" ;
    /**
     * The default name prefix, {@value}.
     * 
     */
    protected static final String NAME_PREFIX = "ADQL_TABLE";

    /**
     * The default alias prefix, {@value}.
     * 
     */
    protected static final String ALIAS_PREFIX = "ADQL_TABLE_";

    /**
     * {@link AdqlTable.NameFactory} implementation.
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<AdqlTable>
    implements AdqlTable.NameFactory
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
     * {@link AdqlTable.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements AdqlTable.AliasFactory
        {

        @Override
        public String alias(final AdqlTable entity)
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
        public AdqlTable resolve(String alias)
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
         * Our {@link AdqlTable.IdentFactory}.
         * 
         */
        @Autowired
        private AdqlTable.IdentFactory idents ;

        /**
         * Our {@link AdqlTable.EntityFactory}.
         * 
         */
        @Autowired
        private AdqlTable.EntityFactory entities;

        }
    
    /**
     * {@link AdqlTable.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseTableEntity.EntityFactory<AdqlSchema, AdqlTable>
    implements AdqlTable.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AdqlTableEntity.class ;
            }

        @Autowired
        private AdqlSchema.EntityFactory schemas;

        @Override
        @SelectMethod
        public AdqlTable select(final Identifier ident)
        throws IdentifierNotFoundException
            {
            log.debug("select(Identifier) [{}]", ident);
            if (ident instanceof ProxyIdentifier)
                {
                log.debug("-- proxy identifier");
                final ProxyIdentifier proxy = (ProxyIdentifier) ident;

                log.debug("-- parent schema");
                final AdqlSchema schema = schemas.select(
                    proxy.parent()
                    );

                log.debug("-- proxy table");
                final AdqlTable table = schema.tables().select(
                    proxy.base()
                    );
                return table;
                }
            else {
                return super.select(
                    ident
                    );
                }
            }

        @Override
        @CreateMethod
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base)
            {
            return create(
                CopyDepth.FULL,
                schema,
                base,
                base.name()
                );
            }

        @Override
        @CreateMethod
        public AdqlTable create(final CopyDepth depth, final AdqlSchema schema, final BaseTable<?, ?> base)
            {
            return create(
                depth,
                schema,
                base,
                base.name()
                );
            }

        @Override
        @CreateMethod
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
            {
            return create(
                CopyDepth.FULL,
                schema,
                base,
                name
                );
            }

        @Override
        @CreateMethod
        public AdqlTable create(final CopyDepth depth, final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
            {
            final AdqlTableEntity table = new AdqlTableEntity(
                depth,
                (BlueQuery) null,
                schema,
                base,
                name
                );
            super.insert(
                table
                );
            table.realize();
            return table ;
            }

        @Override
        @CreateMethod
        public AdqlTable create(final CopyDepth depth, final AdqlSchema schema, final BaseTable<?, ?> base, final BlueQuery query)
            {
            final AdqlTableEntity table = new AdqlTableEntity(
                depth,
                query,
                schema,
                base,
                query.name()
                );
            super.insert(
                table
                );
            table.realize();
            return table ;
            }


        @Override
        @SelectMethod
        public Iterable<AdqlTable> select(final AdqlSchema parent)
            {
            return super.list(
                super.query(
                    "AdqlTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public AdqlTable select(final AdqlSchema parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "AdqlTable-select-parent.name"
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
        public AdqlTable search(final AdqlSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "AdqlTable-select-parent.name"
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
    implements AdqlTable.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AdqlTableEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AdqlTableEntity.EntityServices.instance ;
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
            if (AdqlTableEntity.EntityServices.instance == null)
                {
                AdqlTableEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AdqlTable.IdentFactory idents;
        @Override
        public AdqlTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AdqlTable.LinkFactory links;
        @Override
        public AdqlTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AdqlTable.NameFactory names;
        @Override
        public AdqlTable.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private AdqlTable.EntityFactory entities;
        @Override
        public AdqlTable.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private AdqlTable.AliasFactory aliases;
		@Override
		public AdqlTable.AliasFactory aliases()
			{
			return this.aliases;
			}

        @Autowired
		private AdqlColumn.EntityFactory columns;
		@Override
		public AdqlColumn.EntityFactory columns()
			{
			return this.columns;
			}
        }

    @Override
    protected AdqlTable.EntityFactory factory()
        {
        log.debug("factory()");
        return AdqlTableEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AdqlTable.EntityServices services()
        {
        log.debug("services()");
        return AdqlTableEntity.EntityServices.instance() ; 
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
    protected AdqlTableEntity()
        {
        super();
        }
    
    /**
     * Protected constructor.
     *
     */
    protected AdqlTableEntity(final CopyDepth type, final BlueQuery query, final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
        {
        super(
            type,
            schema,
            name
            );
        this.bluequery  = query;
        this.base   = base;
        this.schema = schema;
        }
    /**
     * Create a copy of a base column.
     * @todo Delay the full scan until the data is actually requested.
     *
     */
    protected void realize(final BaseColumn<?> base)
        {
        log.debug("realize(CopyDepth, BaseColumn) [{}][{}][{}][{}]", ident(), name(), base.ident(), base.name());
        factories().adql().columns().entities().create(
            AdqlTableEntity.this,
            base
            );
        }

    /**
     * Convert this into a full copy.
     * @todo Delay the full scan until the data is requested.
     *
     */
    protected void realize()
        {
        log.debug("realize() [{}][{}]", ident(), name());
        if (this.depth == CopyDepth.FULL)
            {
            if (this.base != null)
                {
                for (final BaseColumn<?> column : base.columns().select())
                    {
                    log.debug("Importing base column [{}][{}]", column.ident(), column.name());
                    realize(
                        column
                        );
                    }
                }
            }
        }

    @Override
    public String text()
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private AdqlSchema schema;
    @Override
    public AdqlSchema schema()
        {
        return this.schema;
        }
    @Override
    public AdqlResource resource()
        {
        return this.schema.resource();
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = BaseTableEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseTable<?,?> base ;
    @Override
    public BaseTable<?,?> base()
        {
        return this.base ;
        }
    @Override
    public BaseTable<?,?> root()
        {
        return this.base.root();
        }

    @Override
    public AdqlTable.Columns columns()
        {
        log.debug("columns() for [{}][{}]", ident(), namebuilder());
        scan();
        return new AdqlTable.Columns()
            {
            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlColumn> select()
                {
                if (depth() == CopyDepth.THIN)
                    {
                    return new AdqlColumnProxy.ProxyIterable(
                        (Iterable<BaseColumn<?>>) base().columns().select(),
                        AdqlTableEntity.this
                        );
                    }
                else {
                    return factories().adql().columns().entities().select(
                        AdqlTableEntity.this
                        );
                    }
                }

            @Override
            public AdqlColumn search(final String name)
                {
                try {
                    return select(
                        name
                        );
                    }
                catch (final NameNotFoundException ouch)
                    {
                    return null ;
                    }
                }

            @Override
            public AdqlColumn select(final String name)
            throws NameNotFoundException
                {
                if (depth() == CopyDepth.THIN)
                    {
                    return new AdqlColumnProxy(
                        base.columns().select(
                            name
                            ),
                        AdqlTableEntity.this
                        );
                    }
                else {
                    return factories().adql().columns().entities().select(
                        AdqlTableEntity.this,
                        name
                        );
                    }
                }

            @Override
            public AdqlColumn create(final BaseColumn<?> base)
                {
                realize();
                return factories().adql().columns().entities().create(
                    AdqlTableEntity.this,
                    base
                    );
                }

            @Override
            public AdqlColumn create(final BaseColumn<?> base, final String name)
                {
                realize();
                return factories().adql().columns().entities().create(
                    AdqlTableEntity.this,
                    base,
                    name
                    );
                }

            @Override
            public AdqlColumn select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                log.debug("columns().select(Identifier) [{}] from [{}]", ident, ident());
                log.debug(" Table depth [{}]", depth());
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
                        return new AdqlColumnProxy(
                            base().columns().select(
                                proxy.base()
                                ),
                            AdqlTableEntity.this
                            );
                        }
                    else {
                        log.debug("  Ident is plain");
                        log.debug("  Selecting [{}] from base [{}]", ident, base.ident());
                        return new AdqlColumnProxy(
                            base().columns().select(
                                ident
                                ),
                            AdqlTableEntity.this
                            );
                        }
                    }
                else {
                    // TODO pass parent reference.
                    return factories().adql().columns().entities().select(
                        ident
                        );
                    }
                }

            @Override
            public AdqlColumn inport(final String name)
            throws NameNotFoundException
                {
                log.debug("columns().inport(String)");
                log.debug("  name [{}]", name);
                if ((depth() == CopyDepth.PARTIAL) || (depth() == CopyDepth.FULL))
                    {
                    AdqlColumn column = search(
                        name
                        );
                    if (column != null)
                        {
                        log.debug("Found ADQL column [{}][{}]", column.ident(), column.name());
                        }
                    else {
                        try {
                            column = create(
                                base().columns().select(
                                    name
                                    )
                                );
                            }
                        catch (final NameNotFoundException ouch)
                            {
                            log.error("Unable to locate base column [{}]", name);
                            throw ouch;
                            }
                        log.debug("Created new ADQL column [{}][{}]", column.ident(), column.name());
                        }
                    return column;
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
    protected void scanimpl()
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        // TODO Auto-generated method stub
        }

    /*
     * TODO This should be part of JdbcTableEntity
     * TODO This should just return root().bluequery()
     * 
     */
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = BlueQueryEntity.class
        )
    @JoinColumn(
        name = DB_BLUE_QUERY_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private BlueQuery bluequery;
    @Override
    public BlueQuery bluequery()
        {
        return this.bluequery;
        }
    }
