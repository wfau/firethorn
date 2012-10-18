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
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcColumnEntity;

/**
 * Hibernate based <code>AdqlColumn</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlColumnEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                AdqlColumnEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.column-select-parent",
            query = "FROM AdqlColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-parent.name",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-base",
            query = "FROM AdqlColumnEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-parent.base",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-parent.parent.base",
            query = "FROM AdqlColumnEntity WHERE ((parent.parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-parent.parent.parent.base",
            query = "FROM AdqlColumnEntity WHERE ((parent.parent.parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-select-parent.parent.parent.parent.base",
            query = "FROM AdqlColumnEntity WHERE ((parent.parent.parent.parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.column-search-parent.text",
            query = "FROM AdqlColumnEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name LIKE :text)) OR ((name IS null) AND (base.name LIKE :text)))) ORDER BY ident desc"
            )
        }
    )
public class AdqlColumnEntity
extends DataComponentImpl
implements AdqlColumn
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "adql_column" ;

    /**
     * The persistence column name for our parent table.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base column.
     *
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlColumn>
    implements AdqlColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlColumnEntity.class ;
            }

        /**
         * Insert a column into the database.
         *
         */
        @CascadeEntityMethod
        protected AdqlColumn insert(final AdqlColumnEntity entity)
            {
            super.insert(
                entity
                );
/*
 * When we have children ...
            for (BaseResource.BaseColumn column : entity.base().columns().select())
                {
                this.columns().cascade(
                    entity,
                    column
                    );
                }
 */
            return entity ;
            }

        /**
         * Create a default view of a base column.
         *
         */
        @CascadeEntityMethod
        protected AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base)
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlColumn select(final AdqlResource parent, final BaseColumn<?> base)
            {
            return super.first(
                super.query(
                    "adql.column-select-parent.parent.parent.parent.base"
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
        public AdqlColumn select(final AdqlCatalog parent, final BaseColumn<?> base)
            {
            return super.first(
                super.query(
                    "adql.column-select-parent.parent.parent.base"
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
        public AdqlColumn select(final AdqlSchema parent, final BaseColumn<?> base)
            {
            return super.first(
                super.query(
                    "adql.column-select-parent.parent.base"
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
        public AdqlColumn select(final AdqlTable parent, final BaseColumn<?> base)
            {
            return super.first(
                super.query(
                    "adql.column-select-parent.base"
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
        public AdqlColumn cascade(final AdqlTable parent, final BaseColumn<?> base)
            {
            AdqlColumn result = this.select(
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
        @CreateEntityMethod
        public AdqlColumn create(final AdqlTable parent, final BaseColumn<?> base, final String name)
            {
            return this.insert(
                new AdqlColumnEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlColumn> select(final AdqlTable parent)
            {
            return super.iterable(
                super.query(
                    "adql.column-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlColumn select(final AdqlTable parent, final String name)
            {
            return super.first(
                super.query(
                    "adql.column-select-parent.name"
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
        public Iterable<AdqlColumn> search(final AdqlTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "adql.column-search-parent.text"
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
        public Iterable<AdqlColumn> select(final BaseColumn<?> base)
            {
            return super.iterable(
                super.query(
                    "adql.column-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        @Autowired
        protected AdqlColumn.IdentFactory identifiers ;

        @Override
        public AdqlColumn.IdentFactory identifiers()
            {
            return this.identifiers;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlColumnEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlColumnEntity(final AdqlTable parent, final BaseColumn<?> base)
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
    protected AdqlColumnEntity(final AdqlTable parent, final BaseColumn<?> base, final String name)
        {
        super(
            name
            );
        log.debug("new([{}]", name);
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent table.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlTable parent ;

    @Override
    public AdqlTable parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying base column.
     * @todo BaseColumnEntity.class
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcColumnEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseColumn<?> base ;

    @Override
    public BaseColumn<?> base()
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
        return this.parent.schema().catalog().resource();
        }

    @Override
    public AdqlCatalog catalog()
        {
        return this.parent.schema().catalog();
        }

    @Override
    public AdqlSchema schema()
        {
        return this.parent.schema();
        }

    @Override
    public AdqlTable table()
        {
        return this.parent;
        }

    @Override
    public String link()
        {
        return null;
        }
    }

