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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcTableEntity;

/**
 * Hibernate based <code>AdqlTable</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlTableEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                AdqlTableEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.table-select-parent",
            query = "FROM AdqlTableEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-select-parent.name",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-select-base",
            query = "FROM AdqlTableEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-select-parent.base",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-select-parent.parent.base",
            query = "FROM AdqlTableEntity WHERE ((parent.parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-select-parent.parent.parent.base",
            query = "FROM AdqlTableEntity WHERE ((parent.parent.parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.table-search-parent.text",
            query = "FROM AdqlTableEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name LIKE :text)) OR ((name IS null) AND (base.name LIKE :text)))) ORDER BY ident desc"
            )
        }
    )
public class AdqlTableEntity
extends DataComponentImpl
implements AdqlTable
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "adql_table" ;

    /**
     * The persistence column name for our parent schema.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base table.
     *
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlTable>
    implements AdqlTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlTableEntity.class ;
            }

        /**
         * Insert a view into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected AdqlTable insert(final AdqlTableEntity entity)
            {
            super.insert(
                entity
                );
            for (final BaseColumn<?> column : entity.base().columns().select())
                {
                this.adqlColumns().cascade(
                    entity,
                    column
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?> base)
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
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?> base, String name)
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
        public AdqlTable select(final AdqlResource parent, final BaseTable<?> base)
            {
            return super.first(
                super.query(
                    "adql.table-select-parent.parent.parent.base"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlTable select(final AdqlCatalog parent, final BaseTable<?> base)
            {
            return super.first(
                super.query(
                    "adql.table-select-parent.parent-base"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlTable select(final AdqlSchema parent, final BaseTable<?> base)
            {
            return super.first(
                super.query(
                    "adql.table-select-parent.base"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @CascadeEntityMethod
        public AdqlTable cascade(final AdqlSchema parent, final BaseTable<?> base)
            {
            AdqlTable result = this.select(
                parent,
                base
                );
            if (result == null)
                {
                result = this.create(
                    parent,
                    base
                    );
                }
            return result ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlTable> select(final AdqlSchema parent)
            {
            return super.iterable(
                super.query(
                    "adql.table-select-parent"
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
                    "adql.table-select-parent.name"
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
                    "adql.table-search-parent.text"
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

        @Override
        @SelectEntityMethod
        public Iterable<AdqlTable> select(final BaseTable<?> base)
            {
            return super.iterable(
                super.query(
                    "adql.table-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        @Autowired
        protected AdqlColumn.Factory columns ;

        @Override
        public AdqlColumn.Factory adqlColumns()
            {
            return this.columns ;
            }

        @Autowired
        protected AdqlTable.IdentFactory identifiers ;

        @Override
        public AdqlTable.IdentFactory identifiers()
            {
            return this.identifiers;
            }
        }

    @Override
    public AdqlTable.Columns columns()
        {
        return new AdqlTable.Columns()
            {
            @Override
            public Iterable<AdqlColumn> select()
                {
                return womble().adql().catalogs().schemas().tables().adqlColumns().select(
                    AdqlTableEntity.this
                    ) ;
                }

            @Override
            public AdqlColumn select(final String name)
                {
                return womble().adql().catalogs().schemas().tables().adqlColumns().select(
                    AdqlTableEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<AdqlColumn> search(final String text)
                {
                return womble().adql().catalogs().schemas().tables().adqlColumns().search(
                    AdqlTableEntity.this,
                    text
                    ) ;
                }

            @Override
            public AdqlColumn select(final BaseColumn<?> base)
                {
                return womble().adql().catalogs().schemas().tables().adqlColumns().select(
                    AdqlTableEntity.this,
                    base
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlTableEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlTableEntity(final AdqlSchema parent, final BaseTable<?> base)
        {
        this(
            parent,
            base,
            null
            );
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlTableEntity(final AdqlSchema parent, final BaseTable<?> base, final String name)
        {
        super(
            name
            );
        log.debug("new([{}]", name);
        this.base = base ;
        this.parent = parent ;
        }

    /**
     * Our parent schema.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlSchema parent ;

    @Override
    public AdqlSchema parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying table.
     * @todo BaseTableEntity.class
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseTable<?> base ;

    @Override
    public BaseTable<?> base()
        {
        return this.base ;
        }

    @Override
    public String name()
        {
        if (this.name != null)
            {
            return this.name ;
            }
        else {
            return base.name() ;
            }
        }

    @Override
    public Status status()
        {
        if (this.parent().status() == Status.ENABLED)
            {
            if (this.base().status() == Status.ENABLED)
                {
                return super.status() ;
                }
            else {
                return this.base().status();
                }
            }
        else {
            return this.parent().status();
            }
        }

    @Override
    public AdqlResource resource()
        {
        return this.parent.catalog().resource();
        }

    @Override
    public AdqlCatalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public AdqlSchema schema()
        {
        return this.parent;
        }

    @Override
    public String link()
        {
        return null;
        }
    }

