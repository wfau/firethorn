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
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.SchemaBaseEntity;

/**
 * Schema View implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = SchemaViewEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SchemaViewEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.schema-select-parent",
            query = "FROM SchemaViewEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.schema-select-parent.name",
            query = "FROM SchemaViewEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.schema-select-base",
            query = "FROM SchemaViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.schema-select-parent.base",
            query = "FROM SchemaViewEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            )
        }
    )
public class SchemaViewEntity
extends WidgeonStatusEntity
implements WidgeonView.Schema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_schema" ;

    /**
     * The persistence column name for our parent Catalog.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base Schema.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonView.Schema>
    implements WidgeonView.Schema.Factory
        {

        @Override
        public Class etype()
            {
            return SchemaViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected WidgeonView.Schema insert(SchemaViewEntity entity)
            {
            super.insert(
                entity
                );
            for (WidgeonBase.Table table : entity.base().tables().select())
                {
                this.tables().cascade(
                    entity,
                    table
                    );
                }
            return entity ;
            }

        /**
         * Create a default View of a Schema.
         *
         */
        @CreateEntityMethod
        protected WidgeonView.Schema create(final WidgeonView.Catalog parent, final WidgeonBase.Schema base)
            {
            return this.insert(
                new SchemaViewEntity(
                    parent,
                    base
                    )
                );
            }

        /**
         * Search for an existing View of a Schema.
         *
         */
        @SelectEntityMethod
        protected WidgeonView.Schema search(final WidgeonView.Catalog parent, final WidgeonBase.Schema base)
            {
            return super.first(
                super.query(
                    "widgeon.view.schema-select-parent.base"
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
        @CreateEntityMethod
        public WidgeonView.Schema cascade(final WidgeonView.Catalog parent, final WidgeonBase.Schema base)
            {
            WidgeonView.Schema result = this.search(
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
        public WidgeonView.Schema create(final WidgeonView.Catalog parent, final WidgeonBase.Schema base, final String name)
            {
            return this.insert(
                new SchemaViewEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonView.Schema> select(final WidgeonView.Catalog parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonView.Schema select(final WidgeonView.Catalog parent, final String name)
        throws NameNotFoundException
            {
            WidgeonView.Schema result = this.search(
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

        /**
         * Search for a named Schema in a Catalog.
         *
         */
        @SelectEntityMethod
        protected WidgeonView.Schema search(final WidgeonView.Catalog parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.schema-select-parent.name"
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
        public Iterable<WidgeonView.Schema> select(final WidgeonBase.Schema base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.schema-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        /**
         * Our Autowired Table factory.
         * 
         */
        @Autowired
        protected WidgeonView.Table.Factory tables ;

        @Override
        public WidgeonView.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public WidgeonView.Schema.Tables tables()
        {
        return new WidgeonView.Schema.Tables()
            {

            @Override
            public Iterable<WidgeonView.Table> select()
                {
                return womble().widgeons().views().catalogs().schemas().tables().select(
                    SchemaViewEntity.this
                    ) ;
                }

            @Override
            public WidgeonView.Table select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().catalogs().schemas().tables().select(
                    SchemaViewEntity.this,
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
    protected SchemaViewEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected SchemaViewEntity(final WidgeonView.Catalog parent, final WidgeonBase.Schema base)
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
    protected SchemaViewEntity(final WidgeonView.Catalog parent, final WidgeonBase.Schema base, final String name)
        {
        super(
            name
            );
        this.base   = base   ;
        this.parent = parent ;
        }

    /**
     * Our parent Catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = CatalogViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonView.Catalog parent ;

    @Override
    public WidgeonView.Catalog parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Schema.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = SchemaBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonBase.Schema base ;

    @Override
    public WidgeonBase.Schema base()
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
    }

