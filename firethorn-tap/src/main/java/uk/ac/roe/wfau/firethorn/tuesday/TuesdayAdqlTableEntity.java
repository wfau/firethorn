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
package uk.ac.roe.wfau.firethorn.tuesday;

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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayAdqlTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayAdqlTable-select-parent",
            query = "FROM TuesdayAdqlTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlTable-select-parent.name",
            query = "FROM TuesdayAdqlTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayAdqlTable-search-parent.text",
            query = "FROM TuesdayAdqlTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayAdqlTableEntity
    extends TuesdayBaseTableEntity<TuesdayAdqlTable, AdqlColumn>
    implements TuesdayAdqlTable
    {
    /**
     * Hibernate database table name.
     *
     */
    protected static final String DB_TABLE_NAME = "TuesdayAdqlTableEntity";

    /**
     * Alias factory implementation.
     *
     */
    @Repository
    public static class AliasFactory
    implements TuesdayAdqlTable.AliasFactory
        {
        /**
         * The alias prefix for this type.
         *
         */
        protected static final String PREFIX = "ADQL_" ;

        @Override
        public String alias(final TuesdayAdqlTable table)
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
    extends TuesdayBaseTableEntity.Factory<TuesdayAdqlSchema, TuesdayAdqlTable>
    implements TuesdayAdqlTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayAdqlTableEntity.class ;
            }

        @Override
        @CascadeEntityMethod
        public TuesdayAdqlTable insert(final TuesdayAdqlTable entity)
            {
            super.insert(
                entity
                );
            for (final TuesdayBaseColumn<?> base : entity.base().columns().select())
                {
                entity.columns().create(
                    base
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlTable create(final TuesdayAdqlSchema parent, final TuesdayBaseTable<?, ?> base)
            {
            return this.insert(
                new TuesdayAdqlTableEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayAdqlTable create(final TuesdayAdqlSchema parent, final TuesdayBaseTable<?, ?> base, final String name)
            {
            return this.insert(
                new TuesdayAdqlTableEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayAdqlTable> select(final TuesdayAdqlSchema parent)
            {
            return super.list(
                super.query(
                    "TuesdayAdqlTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayAdqlTable select(final TuesdayAdqlSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayAdqlTable-select-parent.name"
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
        public Iterable<TuesdayAdqlTable> search(final TuesdayAdqlSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayAdqlTable-search-parent.text"
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
        protected TuesdayAdqlTable.IdentFactory idents;
        @Override
        public TuesdayAdqlTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected TuesdayAdqlTable.LinkFactory links;
        @Override
        public TuesdayAdqlTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected TuesdayAdqlTable.AliasFactory aliases;
        @Override
        public TuesdayAdqlTable.AliasFactory aliases()
            {
            return this.aliases;
            }
        }

    protected TuesdayAdqlTableEntity()
        {
        super();
        }

    protected TuesdayAdqlTableEntity(final TuesdayAdqlSchema schema, final TuesdayBaseTable<?, ?> base)
        {
        super(schema, base.name());
        this.base   = base;
        this.schema = schema;
        }

    protected TuesdayAdqlTableEntity(final TuesdayAdqlSchema schema, final TuesdayBaseTable<?, ?> base, final String name)
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
    @Override
    public String type()
        {
        if (super.type() == null)
            {
            return base().type();
            }
        else {
            return super.type();
            }
        }
    @Override
    public Integer size()
        {
        if (super.size() == null)
            {
            return base().size();
            }
        else {
            return super.size();
            }
        }
    @Override
    public String ucd()
        {
        if (super.ucd() == null)
            {
            return base().ucd();
            }
        else {
            return super.ucd();
            }
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayAdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayAdqlSchema schema;
    @Override
    public TuesdayAdqlSchema schema()
        {
        return this.schema;
        }
    @Override
    public void schema(final TuesdayAdqlSchema schema)
        {
        super.schema(schema);
        this.schema = schema;
        }
    @Override
    public TuesdayAdqlResource resource()
        {
        return this.schema.resource();
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByBase"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseTableEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayBaseTable<?,?> base ;
    @Override
    public TuesdayBaseTable<?,?> base()
        {
        return this.base ;
        }

    @Override
    public TuesdayOgsaTable<?,?> ogsa()
        {
        return base().ogsa();
        }

    @Override
    public TuesdayAdqlTable.Columns columns()
        {
        return new TuesdayAdqlTable.Columns()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                return factories().adql().columns().select(
                    TuesdayAdqlTableEntity.this
                    );
                }
            @Override
            public AdqlColumn select(final String name)
                {
                return factories().adql().columns().select(
                    TuesdayAdqlTableEntity.this,
                    name
                    );
                }
            @Override
            public AdqlColumn create(final TuesdayBaseColumn<?> base)
                {
                return factories().adql().columns().create(
                    TuesdayAdqlTableEntity.this,
                    base
                    );
                }
            @Override
            public Iterable<AdqlColumn> search(final String text)
                {
                return factories().adql().columns().search(
                    TuesdayAdqlTableEntity.this,
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
