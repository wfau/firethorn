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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.ProxyIdentifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateNameException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
 *
    @UniqueConstraint(
        columnNames = {
            BaseComponentEntity.DB_NAME_COL,
            BaseComponentEntity.DB_PARENT_COL
            }
        )
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints={
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
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AdqlSchemaEntity";

    /**
     * Schema factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<AdqlSchema>
    implements AdqlSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public AdqlSchemaEntity create(final AdqlResource parent, final String name)
            {
            return create(
                CopyDepth.THIN,
                parent,
                name
                );
            }

        @CreateEntityMethod
        public AdqlSchemaEntity create(final CopyDepth depth, final AdqlResource parent, final String name)
            {
            final AdqlSchema found = search(
                parent,
                name
                ); 
            if (found != null)
                {
                throw new DuplicateNameException(
                    name
                    );
                }
            else {
                final AdqlSchemaEntity created = new AdqlSchemaEntity(
                    depth,
                    parent,
                    name
                    );
                super.insert(
                    created
                    );
                return created ;
                }
            }

        @Override
        @CreateEntityMethod
        public AdqlSchema create(final AdqlResource parent, final BaseSchema<?, ?> schema)
            {
            return create(
                parent,
                schema,
                schema.name()
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlSchema create(final AdqlResource parent, final BaseSchema<?, ?> schema, final String name)
            {
            return create(
                CopyDepth.THIN,
                parent,
                schema,
                name
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseSchema<?, ?> schema)
            {
            return create(
                depth,
                parent,
                schema,
                schema.name()
                );
            }
        
        @Override
        @CreateEntityMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseSchema<?, ?> schema, final String name)
            {
            final AdqlSchemaEntity created = create(
                depth,
                parent,
                name
                );
            created.realize(
                depth,
                schema
                );
            return created;
            }

        
        @Override
        @CreateEntityMethod
        public AdqlSchema create(final AdqlResource parent, final BaseTable<?, ?> table, final String name)
            {
            return create(
                CopyDepth.THIN,
                parent,
                table,
                name
                );
            }
        
        @Override
        @CreateEntityMethod
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseTable<?, ?> table, final String name)
            {
            final AdqlSchemaEntity created = create(
                parent,
                name
                );
            created.realize(
                depth,
                table
                );
            return created;
            }

        @Override
        @SelectEntityMethod
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
        @SelectEntityMethod
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
            catch (final NotFoundException ouch)
                {
                log.debug("Unable to locate schema [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectEntityMethod
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

        @Autowired
        protected AdqlTable.Factory tables;
        @Override
        public AdqlTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected AdqlSchema.IdentFactory idents ;
        @Override
        public AdqlSchema.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected AdqlSchema.LinkFactory links;
        @Override
        public AdqlSchema.LinkFactory links()
            {
            return this.links;
            }

        }

    protected AdqlSchemaEntity()
        {
        super();
        }

    protected AdqlSchemaEntity(final AdqlResource resource, final String name)
        {
        this(
            CopyDepth.FULL,
            resource,
            name
            );
        }

    protected AdqlSchemaEntity(final CopyDepth depth, final AdqlResource resource, final String name)
        {
        super(
            depth,
            resource,
            name
            );
        this.resource = resource;
        }

    /**
     * Create a copy of a base table.
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
/*
 * Check for a duplicate
 * final AdqlTable found = select(AdqlSchemaEntity.this, name)
 *
 * Check for a conflict
 * found.base() != base
 * 
 */
        final AdqlTable table = factories().adql().tables().create(
            depth,
            AdqlSchemaEntity.this,
            base
            );
        /*
         * HibernateCollections
        children.put(
            table.name(),
            table
            );
         *
         */
        }

    protected void realize(final BaseSchema<?, ?> base)
        {
        realize(
            CopyDepth.FULL,
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
    protected void realize(final CopyDepth depth, final BaseSchema<?, ?> base)
        {
        this.base = base ;  // TODO Check this.base == null
        this.depth = depth; // TODO check this

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
    @Index(
        name=DB_TABLE_NAME + "IndexByBase"
        )
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

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
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

    /*
     * HibernateCollections
    @OrderBy(
        "name ASC"
        )
    @MapKey(
        name="name"
        )
    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "schema",
        targetEntity = AdqlTableEntity.class
        )
    private Map<String, AdqlTable> children = new LinkedHashMap<String, AdqlTable>();
     *
     */

    @Override
    public AdqlSchema.Tables tables()
        {
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
                    return factories().adql().tables().select(
                        AdqlSchemaEntity.this
                        );
                    /*
                     * HibernateCollections
                    return children.values();
                     *
                     */
                    }
                }

            @Override
            public AdqlTable search(final String name)
                {
                try
                    {
                    return select(name);
                    }
                catch (final NameNotFoundException ouch)
                    {
                    return null ;
                    }
                }

            @Override
            public AdqlTable select(final String name)
            throws NameNotFoundException
                {
                log.debug("tables().select(String) [{}][{}][{}][{}]", ident(), name(), depth(), base());
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
                    return factories().adql().tables().select(
                        AdqlSchemaEntity.this,
                        name
                        );
                    /*
                     * HibernateCollections
                    AdqlTable table = children.get(name);
                    if (table != null)
                        {
                        return table;
                        }
                    else {
                        throw new NotFoundException(
                            name
                            );
                        }
                     *
                     */
                    }
                }

            @Override
            public AdqlTable create(final CopyDepth depth, final BaseTable<?, ?> base)
                {
                final AdqlTable table = factories().adql().tables().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base
                    );
                /*
                 * HibernateCollections
                children.put(
                    table.name(),
                    table
                    );
                 *
                 */
                return table ;
                }

            @Override
            public AdqlTable create(final BaseTable<?,?> base)
                {
                final AdqlTable table = factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base
                    );
                /*
                 * HibernateCollections
                children.put(
                    table.name(),
                    table
                    );
                 *
                 */
                return table ;
                }

            @Override
            public AdqlTable create(final CopyDepth depth, final BaseTable<?, ?> base, final String name)
                {
                final AdqlTable table = factories().adql().tables().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                /*
                 * HibernateCollections
                children.put(
                    table.name(),
                    table
                    );
                 *
                 */
                return table ;
                }

            @Override
            public AdqlTable create(final BaseTable<?,?> base, final String name)
                {
                final AdqlTable table = factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                /*
                 * HibernateCollections
                children.put(
                    table.name(),
                    table
                    );
                 *
                 */
                return table ;
                }

            @Override
            public AdqlTable create(final AdqlQuery query)
                {
                //realize();
                final AdqlTable table = factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    query
                    );
                /*
                 * HibernateCollections
                children.put(
                    table.name(),
                    table
                    );
                 *
                 */
                return table ;
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
                    // TODO pass reference to this schema too.
                    return factories().adql().tables().select(
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
    public String link()
        {
        return factories().adql().schemas().links().link(
            this
            );
        }

    @Override
    public Queries queries()
        {
        return new Queries()
            {
            @Override
            public AdqlQuery create(final String query)
                {
                return factories().adql().queries().create(
                    AdqlSchemaEntity.this,
                    query
                    );
                }

            @Override
            public AdqlQuery create(final String query, final String name)
                {
                return factories().adql().queries().create(
                    AdqlSchemaEntity.this,
                    query,
                    name
                    );
                }

            @Override
            public Iterable<AdqlQuery> select()
                {
                return factories().adql().queries().select(
                    AdqlSchemaEntity.this
                    );
                }
            };
        }

    @Override
    protected void scanimpl()
        {
        // TODO Auto-generated method stub

        }
    }
