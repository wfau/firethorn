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
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataComponentImpl;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;

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
            query = "FROM AdqlCatalogEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.catalog-search-parent.text",
            query = "FROM AdqlCatalogEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
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
         */

        @Override
        @CreateEntityMethod
        public AdqlCatalog create(final AdqlResource parent, String name)
            {
            return this.insert(
                new AdqlCatalogEntity(
                    parent,
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

        @Autowired
        protected AdqlSchema.Factory schemas ;

        @Override
        public AdqlSchema.Factory schemas()
            {
            return this.schemas ;
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
            public AdqlSchema create(String name)
                {
                return womble().adql().catalogs().schemas().create(
                    AdqlCatalogEntity.this,
                    name
                    ) ;
                }
            @Override
            public Iterable<AdqlSchema> select()
                {
                return womble().adql().catalogs().schemas().select(
                    AdqlCatalogEntity.this
                    ) ;
                }
            @Override
            public AdqlSchema select(final String name)
                {
                return womble().adql().catalogs().schemas().select(
                    AdqlCatalogEntity.this,
                    name
                    ) ;
                }
            @Override
            public Iterable<AdqlSchema> search(final String text)
                {
                return womble().adql().catalogs().schemas().search(
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
     * Create a new ADQL catalog.
     *
     */
    protected AdqlCatalogEntity(final AdqlResource parent)
        {
        this(
            parent,
            null
            );
        }

    /**
     * Create a new ADQL catalog.
     *
     */
    protected AdqlCatalogEntity(final AdqlResource parent, final String name)
        {
        super(
            name
            );
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

    @Override
    public DataResource.Status status()
        {
        if (this.parent().status() == DataResource.Status.ENABLED)
            {
            return super.status() ;
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

