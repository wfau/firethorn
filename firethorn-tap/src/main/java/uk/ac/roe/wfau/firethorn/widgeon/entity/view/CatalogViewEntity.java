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
package uk.ac.roe.wfau.firethorn.widgeon.entity.view ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.exception.*;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.*;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.CatalogBaseEntity;

/**
 * Catalog View implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = CatalogViewEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                CatalogViewEntity.DB_PARENT_COL
                }
            ),
        @UniqueConstraint(
            columnNames = {
                CatalogViewEntity.DB_BASE_COL,
                CatalogViewEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.catalog-select-parent",
            query = "FROM CatalogViewEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.catalog-select-parent.name",
            query = "FROM CatalogViewEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.catalog-select-base",
            query = "FROM CatalogViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.catalog-select-parent.base",
            query = "FROM CatalogViewEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            )
        }
    )
public class CatalogViewEntity
extends WidgeonStatusEntity
implements WidgeonView.Catalog
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_catalog" ;

    /**
     * The persistence column name for our parent Widgeon.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base Catalog.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonView.Catalog>
    implements WidgeonView.Catalog.Factory
        {

        @Override
        public Class etype()
            {
            return CatalogViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected WidgeonView.Catalog insert(CatalogViewEntity entity)
            {
            super.insert(
                entity
                );
            for (WidgeonBase.Schema schema : entity.base().schemas().select())
                {
                this.schemas().cascade(
                    entity,
                    schema
                    );
                }
            return entity ;
            }

        /**
         * Create a default View of a Catalog.
         *
         */
        @CascadeEntityMethod
        protected WidgeonView.Catalog create(final WidgeonView parent, final WidgeonBase.Catalog base)
            {
            return this.insert(
                new CatalogViewEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonView.Catalog search(final WidgeonView parent, final WidgeonBase.Catalog base)
            {
            return super.first(
                super.query(
                    "widgeon.view.catalog-select-parent.base"
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
        public WidgeonView.Catalog cascade(final WidgeonView parent, final WidgeonBase.Catalog base)
            {
            WidgeonView.Catalog result = this.search(
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
        public WidgeonView.Catalog create(final WidgeonView parent, final WidgeonBase.Catalog base, final String name)
            {
            return this.insert(
                new CatalogViewEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonView.Catalog> select(final WidgeonView parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.catalog-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonView.Catalog select(final WidgeonView parent, final String name)
        throws NameNotFoundException
            {
            WidgeonView.Catalog result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result ;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public WidgeonView.Catalog search(final WidgeonView parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.catalog-select-parent.name"
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
        public Iterable<WidgeonView.Catalog> select(final WidgeonBase.Catalog base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.catalog-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        /**
         * Our Autowired Schema factory.
         * 
         */
        @Autowired
        protected WidgeonView.Schema.Factory schemas ;

        @Override
        public WidgeonView.Schema.Factory schemas()
            {
            return this.schemas ;
            }
        }

    @Override
    public WidgeonView.Catalog.Schemas schemas()
        {
        return new WidgeonView.Catalog.Schemas()
            {

            @Override
            public Iterable<WidgeonView.Schema> select()
                {
                return womble().widgeons().views().catalogs().schemas().select(
                    CatalogViewEntity.this
                    ) ;
                }

            @Override
            public WidgeonView.Schema select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().catalogs().schemas().select(
                    CatalogViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public WidgeonView.Schema search(String name)
                {
                return womble().widgeons().views().catalogs().schemas().search(
                    CatalogViewEntity.this,
                    name
                    ) ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected CatalogViewEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected CatalogViewEntity(final WidgeonView parent, final WidgeonBase.Catalog base)
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
    protected CatalogViewEntity(final WidgeonView parent, final WidgeonBase.Catalog base, final String name)
        {
        super(
            name
            );
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WidgeonViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonView parent ;

    @Override
    public WidgeonView parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = CatalogBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonBase.Catalog base ;

    @Override
    public WidgeonBase.Catalog base()
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
    public Widgeon.Status status()
        {
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            if (this.base().status() == Widgeon.Status.ENABLED)
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
    public WidgeonView widgeon()
        {
        return this.parent;
        }
    }

