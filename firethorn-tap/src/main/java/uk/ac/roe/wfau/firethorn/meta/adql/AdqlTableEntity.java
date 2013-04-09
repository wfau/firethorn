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
import uk.ac.roe.wfau.firethorn.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "AdqlTable-select-parent",
            query = "FROM AdqlTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "AdqlTable-select-parent.name",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "AdqlTable-search-parent.text",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class AdqlTableEntity
    extends BaseTableEntity<AdqlTable, AdqlColumn>
    implements AdqlTable
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "AdqlTableEntity";

    /**
     * Alias factory implementation.
     *
     */
    @Repository
    public static class AliasFactory
    implements AdqlTable.AliasFactory
        {
        /**
         * The alias prefix for this type.
         *
         */
        protected static final String PREFIX = "ADQL_" ;

        @Override
        public String alias(final AdqlTable table)
            {
            return PREFIX + table.ident();
            }
        }

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends BaseTableEntity.Factory<AdqlSchema, AdqlTable>
    implements AdqlTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlTableEntity.class ;
            }

        @Override
        @CascadeEntityMethod
        public AdqlTable insert(final AdqlTable entity)
            {
            super.insert(
                entity
                );
            for (final BaseColumn<?> base : entity.base().columns().select())
                {
                entity.columns().create(
                    base
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?, ?> base)
            {
            return this.insert(
                new AdqlTableEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?, ?> base, final String name)
            {
            return this.insert(
                new AdqlTableEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
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
        @SelectEntityMethod
        public AdqlTable select(final AdqlSchema parent, final String name)
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

        @Override
        @SelectEntityMethod
        public Iterable<AdqlTable> search(final AdqlSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "AdqlTable-search-parent.text"
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
        protected AdqlColumn.Factory columns;
        @Override
        public AdqlColumn.Factory columns()
            {
            return this.columns;
            }

        @Autowired
        protected AdqlTable.IdentFactory idents;
        @Override
        public AdqlTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected AdqlTable.LinkFactory links;
        @Override
        public AdqlTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected AdqlTable.AliasFactory aliases;
        @Override
        public AdqlTable.AliasFactory aliases()
            {
            return this.aliases;
            }
        }

    protected AdqlTableEntity()
        {
        super();
        }

    protected AdqlTableEntity(final AdqlSchema schema, final BaseTable<?, ?> base)
        {
        super(schema, base.name());
        this.base   = base;
        this.schema = schema;
        }

    protected AdqlTableEntity(final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
        {
        super(schema, ((name != null) ? name : base.name()));
        this.base   = base;
        this.schema = schema;
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

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
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
    public void schema(final AdqlSchema schema)
        {
        super.schema(schema);
        this.schema = schema;
        }
    @Override
    public AdqlResource resource()
        {
        return this.schema.resource();
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByBase"
        )
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
        return base().root();
        }

    @Override
    public AdqlTable.Columns columns()
        {
        return new AdqlTable.Columns()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                return factories().adql().columns().select(
                    AdqlTableEntity.this
                    );
                }
            @Override
            public AdqlColumn select(final String name)
            throws NotFoundException
                {
                return factories().adql().columns().select(
                    AdqlTableEntity.this,
                    name
                    );
                }
            @Override
            public AdqlColumn create(final BaseColumn<?> base)
                {
                return factories().adql().columns().create(
                    AdqlTableEntity.this,
                    base
                    );
                }
            @Override
            public Iterable<AdqlColumn> search(final String text)
                {
                return factories().adql().columns().search(
                    AdqlTableEntity.this,
                    text
                    );
                }
            };
        }

    @Override
    public String link()
        {
        return factories().adql().tables().links().link(
            this
            );
        }

    @Override
    public String alias()
        {
        return factories().adql().tables().aliases().alias(
            this
            );
        }
    }
