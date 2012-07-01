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
 * Column implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = ColumnBaseEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                ColumnBaseEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.column-select-parent",
            query = "FROM ColumnBaseEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.column-select-parent.name",
            query = "FROM ColumnBaseEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class ColumnBaseEntity
extends WidgeonStatusEntity
implements Widgeon.Base.Schema.Catalog.Table.Column
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_column" ;

    /**
     * The persistence column name for our parent Table.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Base.Schema.Catalog.Table.Column>
    implements Widgeon.Base.Schema.Catalog.Table.Column.Factory
        {

        @Override
        public Class etype()
            {
            return ColumnBaseEntity.class ;
            }

        /**
         * Insert a Column into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.Base.Schema.Catalog.Table.Column insert(final ColumnBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (Widgeon.View.Schema.Catalog.Table view : entity.parent().views().select())
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
        public Widgeon.Base.Schema.Catalog.Table.Column create(final Widgeon.Base.Schema.Catalog.Table parent, final String name)
            {
            return this.insert(
                new ColumnBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Base.Schema.Catalog.Table.Column> select(final Widgeon.Base.Schema.Catalog.Table parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.column-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Base.Schema.Catalog.Table.Column select(final Widgeon.Base.Schema.Catalog.Table parent, final String name)
        throws NameNotFoundException
            {
            Widgeon.Base.Schema.Catalog.Table.Column result = this.search(
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
        public Widgeon.Base.Schema.Catalog.Table.Column search(final Widgeon.Base.Schema.Catalog.Table parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.column-select-parent.name"
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
        protected Widgeon.View.Schema.Catalog.Table.Column.Factory views ;

        @Override
        public Widgeon.View.Schema.Catalog.Table.Column.Factory views()
            {
            return this.views ;
            }
        }

    @Override
    public Widgeon.Base.Schema.Catalog.Table.Column.Views views()
        {
        return new Widgeon.Base.Schema.Catalog.Table.Column.Views()
            {
            public Iterable<Widgeon.View.Schema.Catalog.Table.Column> select()
                {
                return womble().widgeons().views().schemas().catalogs().tables().columns().select(
                    ColumnBaseEntity.this
                    );
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected ColumnBaseEntity()
        {
        super();
        }

    /**
     * Create a new Schema.
     *
     */
    protected ColumnBaseEntity(final Widgeon.Base.Schema.Catalog.Table parent, final String name)
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
        targetEntity = TableBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base.Schema.Catalog.Table parent ;

    @Override
    public Widgeon.Base.Schema.Catalog.Table parent()
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

