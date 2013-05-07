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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

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
    extends AbstractFactory<AdqlSchema>
    implements AdqlSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlSchemaEntity.class ;
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
        public AdqlSchema create(final AdqlResourceEntity parent, final String name, final BaseTable<?, ?> base)
            {
            final AdqlSchema schema = this.create(
                parent,
                name
                );
            schema.tables().create(
                base
                );
            return schema;
            }

        @Override
		public AdqlSchema create(final AdqlResourceEntity parent, final String name, final BaseSchema<?, ?> base)
			{
			final AdqlSchema schema = this.create(
					parent,
					name
					);
			for (final BaseTable<?,?> table : base.tables().select())
				{
				schema.tables().create(
						table
						);
				}
			return schema;
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

    protected AdqlSchemaEntity(final AdqlResource resource, final String name)
        {
        super(resource, name);
        this.resource = resource;
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
            public Iterable<AdqlTable> select()
                {
                return factories().adql().tables().select(
                    AdqlSchemaEntity.this
                    );
                }
            @Override
            public AdqlTable select(final String name)
                {
                return factories().adql().tables().select(
                    AdqlSchemaEntity.this,
                    name
                    );
                }
            @Override
            public AdqlTable create(final BaseTable<?,?> base)
                {
                return factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base
                    );
                }
            @Override
            public AdqlTable create(final BaseTable<?,?> base, final String name)
                {
                return factories().adql().tables().create(
                    AdqlSchemaEntity.this,
                    base,
                    name
                    );
                }
            @Override
            public AdqlTable create(final AdqlQuery query)
                {
                return factories().adql().tables().create(
                    query
                    );
                }
            @Override
            public Iterable<AdqlTable> search(final String text)
                {
                return factories().adql().tables().search(
                    AdqlSchemaEntity.this,
                    text
                    );
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
            public AdqlQuery create(final JdbcSchema store, final String query)
                {
                return factories().adql().queries().create(
                    AdqlSchemaEntity.this,
                    store,
                    query
                    );
                }

            @Override
            public AdqlQuery create(final JdbcSchema store, final String query, final String name)
                {
                return factories().adql().queries().create(
                    AdqlSchemaEntity.this,
                    store,
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
    }
