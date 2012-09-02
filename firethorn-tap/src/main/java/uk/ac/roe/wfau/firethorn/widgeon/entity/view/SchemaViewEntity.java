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

import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.AbstractDataResourceEntity;
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
extends AbstractDataResourceEntity
implements DataResourceView.Schema
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
    extends AbstractFactory<DataResourceView.Schema>
    implements DataResourceView.Schema.Factory
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
        protected DataResourceView.Schema insert(SchemaViewEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceBase.Table table : entity.base().tables().select())
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
        protected DataResourceView.Schema create(final DataResourceView.Catalog parent, final DataResourceBase.Schema base)
            {
            return this.insert(
                new SchemaViewEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceView.Schema search(final DataResourceView.Catalog parent, final DataResourceBase.Schema base)
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
        public DataResourceView.Schema cascade(final DataResourceView.Catalog parent, final DataResourceBase.Schema base)
            {
            DataResourceView.Schema result = this.search(
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
        public DataResourceView.Schema create(final DataResourceView.Catalog parent, final DataResourceBase.Schema base, final String name)
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
        public Iterable<DataResourceView.Schema> select(final DataResourceView.Catalog parent)
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
        public DataResourceView.Schema select(final DataResourceView.Catalog parent, final String name)
        throws NameNotFoundException
            {
            DataResourceView.Schema result = this.search(
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
        public DataResourceView.Schema search(final DataResourceView.Catalog parent, final String name)
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
        public Iterable<DataResourceView.Schema> select(final DataResourceBase.Schema base)
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
        protected DataResourceView.Table.Factory tables ;

        @Override
        public DataResourceView.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public DataResourceView.Schema.Tables tables()
        {
        return new DataResourceView.Schema.Tables()
            {

            @Override
            public Iterable<DataResourceView.Table> select()
                {
                return womble().resources().views().catalogs().schemas().tables().select(
                    SchemaViewEntity.this
                    ) ;
                }

            @Override
            public DataResourceView.Table select(String name)
            throws NameNotFoundException
                {
                return womble().resources().views().catalogs().schemas().tables().select(
                    SchemaViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceView.Table search(String name)
                {
                return womble().resources().views().catalogs().schemas().tables().search(
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
    protected SchemaViewEntity(final DataResourceView.Catalog parent, final DataResourceBase.Schema base)
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
    protected SchemaViewEntity(final DataResourceView.Catalog parent, final DataResourceBase.Schema base, final String name)
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
    private DataResourceView.Catalog parent ;

    @Override
    public DataResourceView.Catalog parent()
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
    private DataResourceBase.Schema base ;

    @Override
    public DataResourceBase.Schema base()
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
    public DataResourceView widgeon()
        {
        return this.parent.widgeon();
        }

    @Override
    public DataResourceView.Catalog catalog()
        {
        return this.parent;
        }
    }

