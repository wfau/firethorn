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
package uk.ac.roe.wfau.firethorn.widgeon ;

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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Widgeon Schema implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = SchemaBase.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                SchemaBase.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.schema.base-select-parent",
            query = "FROM SchemaBase WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.schema.base-select-parent.name",
            query = "FROM SchemaBase WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class SchemaBase
extends AbstractEntity
implements Widgeon.Schema, Widgeon.Base.Schema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_schema_base" ;

    /**
     * The persistence column name for our parent Widgeon.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /*
     * The persistence column name for our status enum.
     * 
     */
    public static final String DB_STATUS_COL = "status" ;

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
            return SchemaBase.class ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Base.Schema create(final Widgeon.Base parent, final String name)
            {
            return super.insert(
                new SchemaBase(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema> select(final Widgeon.Base parent)
            {
            return null ;
/*
            return super.iterable(
                super.query(
                    "widgeon.schema.base-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
 */
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Base.Schema select(final Widgeon.Base parent, final String name)
            {
            return super.single(
                super.query(
                    "widgeon.schema.base-select-parent.name"
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
         * Our Autowired Catalog factory.
         * 
        @Autowired
        protected Widgeon.Base.Schema.Catalog.Factory catalogs ;
         */

        /**
         * Access to our Catalog factory.
         * 
        @Override
        public Widgeon.Base.Schema.Catalog.Factory catalogs()
            {
            return null ;
            }
         */
        }

    @Override
    public Catalogs catalogs()
        {
        return new Catalogs()
            {
            //@Override
            public Catalog create(String name)
                {
/*
                return womble().widgeon().schemas().catalogs().create(
                    SchemaBase.this,
                    name
                    ) ;
 */
                return null ;
                }

            @Override
            public Catalog select(String name)
                {
/*
                return womble().widgeon().schemas().catalogs().select(
                    SchemaBase.this,
                    name
                    ) ;
 */
                return null ;
                }

            @Override
            public Iterable<Catalog> select()
                {
/*
                return womble().widgeon().schemas().catalogs().select(
                    SchemaBase.this
                    ) ;
 */
                return null ;
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected SchemaBase()
        {
        super();
        }

    /**
     * Create a new Schema.
     *
     */
    protected SchemaBase(final Widgeon.Base parent, final String name)
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
        targetEntity = WidgeonBase.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base parent ;

    /**
     * Access to our parent Widgeon.
     *
     */
    @Override
    public Widgeon.Base parent()
        {
        return this.parent ;
        }

    /**
     * The status of this Schema.
     *
     */
    @Column(
        name = DB_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Widgeon.Status status = Widgeon.Status.CREATED ;

    @Override
    public Widgeon.Status status()
        {
        if (this.parent.status() == Widgeon.Status.ENABLED)
            {
            return this.status;
            }
        else {
            return this.parent.status();
            }
        }

    @Override
    public void status(Widgeon.Status status)
        {
        this.status = status ;
        }

    }

