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
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalogEntity;

/**
 * Hibernate based <code>AdqlCatalog</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlCatalogEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                AdqlCatalogEntity.DB_PARENT_COL
                }
            ),
        @UniqueConstraint(
            columnNames = {
                AdqlCatalogEntity.DB_BASE_COL,
                AdqlCatalogEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.catalog-select-parent",
            query = "FROM AdqlCatalogEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.catalog-select-parent.name",
            query = "FROM AdqlCatalogEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.catalog-select-base",
            query = "FROM AdqlCatalogEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.catalog-select-parent.base",
            query = "FROM AdqlCatalogEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.catalog-search-parent.text",
            query = "FROM AdqlCatalogEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name LIKE :text)) OR ((name IS null) AND (base.name LIKE :text)))) ORDER BY ident desc"
            )
        }
    )
public class AdqlCatalogEntity
extends DataComponentImpl
implements AdqlCatalog
    {

    /**
     * Our persistence table name.
     *
     */
    public static final String DB_TABLE_NAME = "adql_catalog" ;

    /**
     * The persistence column name for our parent resource.
     *
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base catalog.
     *
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlCatalog>
    implements AdqlCatalog.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlCatalogEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected AdqlCatalog insert(final AdqlCatalogEntity entity)
            {
            super.insert(
                entity
                );
            for (final BaseSchema<?> schema : entity.base().schemas().select())
                {
                this.schemas().cascade(
                    entity,
                    schema
                    );
                }
            return entity ;
            }

        /**
         * Create a default view of a catalog.
         *
         */
        @CascadeEntityMethod
        protected AdqlCatalog create(final AdqlResource parent, final BaseCatalog<?> base)
            {
            return this.insert(
                new AdqlCatalogEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlCatalog select(final AdqlResource parent, final BaseCatalog<?> base)
            {
            return super.first(
                super.query(
                    "adql.catalog-select-parent.base"
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
        public AdqlCatalog cascade(final AdqlResource parent, final BaseCatalog<?> base)
            {
            AdqlCatalog result = this.select(
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
        public AdqlCatalog create(final AdqlResource parent, final BaseCatalog<?> base, final String name)
            {
            return this.insert(
                new AdqlCatalogEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlCatalog> select(final AdqlResource parent)
            {
            return super.iterable(
                super.query(
                    "adql.catalog-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlCatalog select(final AdqlResource parent, final String name)
            {
            return super.first(
                super.query(
                    "adql.catalog-select-parent.name"
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
        public Iterable<AdqlCatalog> search(final AdqlResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "adql.catalog-search-parent.text"
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
        public Iterable<AdqlCatalog> select(final BaseCatalog<?> base)
            {
            return super.iterable(
                super.query(
                    "adql.catalog-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        @Autowired
        protected AdqlSchema.Factory adqlSchemas ;

        @Override
        public AdqlSchema.Factory schemas()
            {
            return this.adqlSchemas ;
            }

        @Autowired
        protected AdqlCatalog.IdentFactory identifiers ;

        @Override
        public AdqlCatalog.IdentFactory identifiers()
            {
            return this.identifiers;
            }
        }

    @Override
    public AdqlCatalog.Schemas schemas()
        {
        return new AdqlCatalog.Schemas()
            {

            @Override
            public Iterable<AdqlSchema> select()
                {
                //return womble().resources().base().views().catalogs().schemas().select(
                return womble().resources().adql().catalogs().schemas().select(
                    AdqlCatalogEntity.this
                    ) ;
                }

            @Override
            public AdqlSchema select(final String name)
                {
                //return womble().resources().base().views().catalogs().schemas().select(
                return womble().resources().adql().catalogs().schemas().select(
                    AdqlCatalogEntity.this,
                    name
                    ) ;
                }

            @Override
            public Iterable<AdqlSchema> search(final String text)
                {
                //return womble().resources().base().views().catalogs().schemas().search(
                return womble().resources().adql().catalogs().schemas().search(
                    AdqlCatalogEntity.this,
                    text
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlCatalogEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected AdqlCatalogEntity(final AdqlResource parent, final BaseCatalog<?> base)
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
    protected AdqlCatalogEntity(final AdqlResource parent, final BaseCatalog<?> base, final String name)
        {
        super(
            name
            );
        log.debug("new([{}]", name);
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent resource.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource parent ;

    @Override
    public AdqlResource parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying base catalog.
     * @todo BaseCatalogEntity.class
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcCatalogEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private BaseCatalog<?> base ;

    @Override
    public BaseCatalog<?> base()
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
    public DataResource.Status status()
        {
        if (this.parent().status() == DataResource.Status.ENABLED)
            {
            if (this.base().status() == DataResource.Status.ENABLED)
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
        return this.parent;
        }

    @Override
    public String link()
        {
        return null;
        }
    }

