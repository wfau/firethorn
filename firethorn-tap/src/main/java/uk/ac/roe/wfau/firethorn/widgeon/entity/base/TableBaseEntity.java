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
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;

/**
 * Table implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TableBaseEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                TableBaseEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.table-select-parent",
            query = "FROM TableBaseEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.table-select-parent.name",
            query = "FROM TableBaseEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class TableBaseEntity
extends WidgeonStatusEntity
implements WidgeonBase.Table
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_table" ;

    /**
     * The persistence column name for our parent Schema.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<WidgeonBase.Table>
    implements WidgeonBase.Table.Factory
        {

        @Override
        public Class etype()
            {
            return TableBaseEntity.class ;
            }

        /**
         * Insert a Table into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected WidgeonBase.Table insert(final TableBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (WidgeonView.Schema view : entity.parent().views().select())
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
        public WidgeonBase.Table create(final WidgeonBase.Schema parent, final String name)
            {
            return this.insert(
                new TableBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<WidgeonBase.Table> select(final WidgeonBase.Schema parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public WidgeonBase.Table select(final WidgeonBase.Schema parent, final String name)
        throws NameNotFoundException
            {
            WidgeonBase.Table result = this.search(
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
        public WidgeonBase.Table search(final WidgeonBase.Schema parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.table-select-parent.name"
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
        protected WidgeonView.Table.Factory views ;

        @Override
        public WidgeonView.Table.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Column factory.
         * 
         */
        @Autowired
        protected WidgeonBase.Column.Factory columns ;

        @Override
        public WidgeonBase.Column.Factory columns()
            {
            return this.columns ;
            }
        }

    @Override
    public WidgeonBase.Table.Views views()
        {
        return new WidgeonBase.Table.Views()
            {
            @Override
            public Iterable<WidgeonView.Table> select()
                {
                return womble().widgeons().views().catalogs().schemas().tables().select(
                    TableBaseEntity.this
                    );
                }

            @Override
            public WidgeonView.Table search(WidgeonView.Schema parent)
                {
                return womble().widgeons().views().catalogs().schemas().tables().search(
                    parent,
                    TableBaseEntity.this
                    );
                }
            };
        }

    @Override
    public WidgeonBase.Table.Columns columns()
        {
        return new WidgeonBase.Table.Columns()
            {
            @Override
            public WidgeonBase.Column create(String name)
                {
                return womble().widgeons().catalogs().schemas().tables().columns().create(
                    TableBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<WidgeonBase.Column> select()
                {
                return womble().widgeons().catalogs().schemas().tables().columns().select(
                    TableBaseEntity.this
                    ) ;
                }

            @Override
            public WidgeonBase.Column select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().catalogs().schemas().tables().columns().select(
                    TableBaseEntity.this,
                    name
                    ) ;
                }

            @Override
            public WidgeonBase.Column search(String name)
                {
                return womble().widgeons().catalogs().schemas().tables().columns().search(
                    TableBaseEntity.this,
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
    protected TableBaseEntity()
        {
        super();
        }

    /**
     * Create a new Catalog.
     *
     */
    protected TableBaseEntity(final WidgeonBase.Schema parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Column.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = SchemaBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private WidgeonBase.Schema parent ;

    @Override
    public WidgeonBase.Schema parent()
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

    @Override
    public WidgeonBase widgeon()
        {
        return this.parent.catalog().widgeon();
        }

    @Override
    public WidgeonBase.Catalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public WidgeonBase.Schema schema()
        {
        return this.parent;
        }
    }

