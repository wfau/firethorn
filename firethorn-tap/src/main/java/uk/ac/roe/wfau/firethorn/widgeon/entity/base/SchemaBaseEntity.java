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
package uk.ac.roe.wfau.firethorn.widgeon.entity.base ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;

/**
 * Schema implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = SchemaBaseEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SchemaBaseEntity.DB_PARENT_COL,
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.schema-select-parent",
            query = "FROM SchemaBaseEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.schema-select-parent.name",
            query = "FROM SchemaBaseEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class SchemaBaseEntity
extends WidgeonStatusEntity
implements Widgeon.Base.Schema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_schema" ;

    /**
     * The persistence column name for our parent Widgeon.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Base.Schema>
    implements Widgeon.Base.Schema.Factory
        {

        @Override
        public Class etype()
            {
            return SchemaBaseEntity.class ;
            }

        /**
         * Insert a Schema into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.Base.Schema insert(final SchemaBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (Widgeon.View view : entity.parent().views().select())
                {
                this.views().cascade(
                    view,
                    entity
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base.Schema create(final Widgeon.Base parent, final String name)
            {
            return this.insert(
                new SchemaBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Base.Schema> select(final Widgeon.Base parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Base.Schema select(final Widgeon.Base parent, final String name)
        throws NameNotFoundException
            {
            Widgeon.Base.Schema result = this.search(
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
        public Widgeon.Base.Schema search(final Widgeon.Base parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.schema-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired View factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Schema.Factory views ;

        @Override
        public Widgeon.View.Schema.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected Widgeon.Base.Schema.Catalog.Factory catalogs ;

        @Override
        public Widgeon.Base.Schema.Catalog.Factory catalogs()
            {
            return this.catalogs ;
            }
        }

    @Override
    public Widgeon.Base.Schema.Views views()
        {
        return new Widgeon.Base.Schema.Views()
            {
            public Iterable<Widgeon.View.Schema> select()
                {
                return womble().widgeons().views().schemas().select(
                    SchemaBaseEntity.this
                    );
                }
            };
        }

    @Override
    public Widgeon.Base.Schema.Catalogs catalogs()
        {
        return new Widgeon.Base.Schema.Catalogs()
            {
            @Override
            public Widgeon.Base.Schema.Catalog create(String name)
                {
                return womble().widgeons().schemas().catalogs().create(
                    SchemaBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<Widgeon.Base.Schema.Catalog> select()
                {
                return womble().widgeons().schemas().catalogs().select(
                    SchemaBaseEntity.this
                    );
                }

            @Override
            public Widgeon.Base.Schema.Catalog select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().schemas().catalogs().select(
                    SchemaBaseEntity.this,
                    name
                    );
                }

            @Override
            public Widgeon.Base.Schema.Catalog search(String name)
                {
                return womble().widgeons().schemas().catalogs().search(
                    SchemaBaseEntity.this,
                    name
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected SchemaBaseEntity()
        {
        super();
        }

    /**
     * Create a new Schema.
     *
     */
    protected SchemaBaseEntity(final Widgeon.Base parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Widgeon.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WidgeonBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base parent ;

    @Override
    public Widgeon.Base parent()
        {
        return this.parent ;
        }

    @Override
    public Widgeon.Status status()
        {
        if (this.parent().status() == Widgeon.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }
    }

