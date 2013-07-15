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

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponent.CopyDepth;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlSchemaEntity.DB_TABLE_NAME
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
    protected static final String DB_TABLE_NAME = "AdqlSchemaEntity";

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
        public AdqlSchema select(UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
        
        @Override
        @CreateEntityMethod
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
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    depth,
                    parent,
                    name
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlSchema create(final AdqlResource parent, final BaseSchema<?, ?> base)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    base.name(),
                    base
                    )
                );
            }

        @Override
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseSchema<?, ?> base)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    depth,
                    parent,
                    base.name(),
                    base
                    )
                );
            }
        
        @Override
        @CreateEntityMethod
		public AdqlSchema create(final AdqlResource parent, final String name, final BaseSchema<?, ?> base)
			{
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    name,
                    base
                    )
                );
			}

        @Override
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name, final BaseSchema<?, ?> base)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    depth,
                    parent,
                    name,
                    base
                    )
                );
            }
        
        @Override
        @CreateEntityMethod
        public AdqlSchema create(final AdqlResource parent, final String name, final BaseTable<?, ?> base)
            {
            return this.insert(
                new AdqlSchemaEntity(
                    parent,
                    name,
                    base
                    )
                );
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

        @Override
        @SelectEntityMethod
        public Iterable<AdqlSchema> search(final AdqlResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "AdqlSchema-search-parent.text"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
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

    protected AdqlSchemaEntity(final AdqlResource resource, final String name, final BaseTable<?, ?> base)
        {
        this(
            CopyDepth.FULL,
            resource,
            name
            );
        realize(
            base
            );
        }

    protected AdqlSchemaEntity(final AdqlResource resource, final String name, final BaseSchema<?, ?> base)
        {
        this(
            CopyDepth.FULL,
            resource,
            name,
            base
            );
        }

    protected AdqlSchemaEntity(final CopyDepth depth, final AdqlResource resource, final String name, final BaseSchema<?, ?> base)
        {
        this(
            depth,
            resource,
            name
            );
        realize(
            depth,
            base
            );
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
     * Convert this into a full copy.
     * 
     */
    protected void realize(final BaseTable<?, ?> base)
        {
        if (base != null)
            {
            tables().create(
                base
                );
            this.depth = CopyDepth.FULL ;
            }
        }
    
    /**
     * Convert this into a full copy.
     * 
     */
    protected void realize(final CopyDepth depth, final BaseSchema<?, ?> base)
        {
        if ((base != null) && (depth == CopyDepth.FULL))
            {
            if (this.depth != CopyDepth.FULL)
                {
                for (final BaseTable<?,?> table : base.tables().select())
                    {
                    tables().create(
                        table
                        );
                    }
                }
            this.base  = base  ;
            this.depth = depth ;
            }
        }

    protected void realize(final BaseSchema<?, ?> base)
        {
        realize(
            CopyDepth.FULL,
            base
            );
        }

    protected void realize()
        {
        realize(this.base);
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

    /**
     * Our base schema.
     * 
     */
    public BaseSchema<?, ?> base()
        {
        return this.base ;
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

    @Override
    public AdqlSchema.Tables tables()
        {
        return new AdqlSchema.Tables()
            {
            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlTable> select()
                {
                if (depth() == CopyDepth.FULL)
                    {
                    return factories().adql().tables().select(
                        AdqlSchemaEntity.this
                        );
                    }
                else {
                    return new AdqlTableProxy.ProxyIterable(
                        (Iterable<BaseTable<?,?>>) base().tables().select(),
                        AdqlSchemaEntity.this
                        );
                    }
                }

            @Override
            public AdqlTable select(final String name)
                {
                if (depth() == CopyDepth.FULL)
                    {
                    return factories().adql().tables().select(
                        AdqlSchemaEntity.this,
                        name
                        );
                    }
                else {
                    return new AdqlTableProxy(
                        base().tables().select(name),
                        AdqlSchemaEntity.this
                        );
                    }
                }

            
            @Override
            public AdqlTable create(final CopyDepth depth, BaseTable<?, ?> base)
                {
                if (depth() != CopyDepth.FULL)
                    {
                    realize();
                    }
                return factories().adql().tables().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base
                    );
                }

           
            @Override
            public AdqlTable create(final BaseTable<?,?> base)
                {
                if (depth() != CopyDepth.FULL)
                    {
                    realize();
                    }
                return factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base
                    );
                }

            @Override
            public AdqlTable create(final CopyDepth depth, BaseTable<?, ?> base, String name)
                {
                if (depth() != CopyDepth.FULL)
                    {
                    realize();
                    }
                return factories().adql().tables().create(
                    depth,
                    AdqlSchemaEntity.this,
                    base
                    );
                }

            @Override
            public AdqlTable create(final BaseTable<?,?> base, final String name)
                {
                if (depth() != CopyDepth.FULL)
                    {
                    realize();
                    }
                return factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                }

            @Override
            public AdqlTable create(final AdqlQuery query)
                {
                if (depth() != CopyDepth.FULL)
                    {
                    realize();
                    }
                return factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    query
                    );
                }

            @Override
            @SuppressWarnings("unchecked")
            public Iterable<AdqlTable> search(final String text)
                {
                if (depth() == CopyDepth.FULL)
                    {
                    return factories().adql().tables().search(
                        AdqlSchemaEntity.this,
                        text
                        );
                    }
                else {
                    return new AdqlTableProxy.ProxyIterable(
                        (Iterable<BaseTable<?,?>>) base.tables().search(
                            text
                            ),
                        AdqlSchemaEntity.this
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

            @Override
            public Iterable<AdqlQuery> search(final String text)
                {
                return factories().adql().queries().search(
                    AdqlSchemaEntity.this,
                    text
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
