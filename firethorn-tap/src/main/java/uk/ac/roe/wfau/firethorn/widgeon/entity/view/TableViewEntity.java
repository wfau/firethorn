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
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.WidgeonStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.base.TableBaseEntity;


/**
 * Table View implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TableViewEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                TableViewEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.table-select-parent",
            query = "FROM TableViewEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.table-select-parent.name",
            query = "FROM TableViewEntity WHERE ((parent = :parent) AND (((name IS NOT null) AND (name = :name)) OR ((name IS null) AND (base.name = :name)))) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.table-select-base",
            query = "FROM TableViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.table-select-parent.base",
            query = "FROM TableViewEntity WHERE ((parent = :parent) AND (base = :base)) ORDER BY ident desc"
            )
        }
    )
public class TableViewEntity
extends WidgeonStatusEntity
implements Widgeon.View.Sparrow.Warbler.Table
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_table" ;

    /**
     * The persistence column name for our parent Warbler.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * The persistence column name for our base Table.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.View.Sparrow.Warbler.Table>
    implements Widgeon.View.Sparrow.Warbler.Table.Factory
        {

        @Override
        public Class etype()
            {
            return TableViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.View.Sparrow.Warbler.Table insert(TableViewEntity entity)
            {
            super.insert(
                entity
                );
            for (Widgeon.Base.Sparrow.Warbler.Table.Column column : entity.base().columns().select())
                {
                this.columns().cascade(
                    entity,
                    column
                    );
                }
            return entity ;
            }

        /**
         * Create a default View of a Table.
         *
         */
        @CascadeEntityMethod
        protected Widgeon.View.Sparrow.Warbler.Table create(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base)
            {
            return this.insert(
                new TableViewEntity(
                    parent,
                    base
                    )
                );
            }

        /**
         * Search for an existing View of a Table.
         *
         */
        @SelectEntityMethod
        protected Widgeon.View.Sparrow.Warbler.Table search(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base)
            {
            return super.first(
                super.query(
                    "widgeon.view.table-select-parent.base"
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
        public Widgeon.View.Sparrow.Warbler.Table cascade(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base)
            {
            Widgeon.View.Sparrow.Warbler.Table result = this.search(
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
        public Widgeon.View.Sparrow.Warbler.Table create(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base, final String name)
            {
            return super.insert(
                new TableViewEntity(
                    parent,
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.View.Sparrow.Warbler.Table> select(final Widgeon.View.Sparrow.Warbler parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.table-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.View.Sparrow.Warbler.Table select(final Widgeon.View.Sparrow.Warbler parent, final String name)
        throws NameNotFoundException
            {
            Widgeon.View.Sparrow.Warbler.Table result = this.search(
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
        public Widgeon.View.Sparrow.Warbler.Table search(final Widgeon.View.Sparrow.Warbler parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.table-select-parent.name"
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
        public Iterable<Widgeon.View.Sparrow.Warbler.Table> select(final Widgeon.Base.Sparrow.Warbler.Table base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.table-select-base"
                    ).setEntity(
                        "base",
                        base
                        )
                );
            }

        /**
         * Our Autowired Column factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Sparrow.Warbler.Table.Column.Factory columns ;

        @Override
        public Widgeon.View.Sparrow.Warbler.Table.Column.Factory columns()
            {
            return this.columns ;
            }

        }

    @Override
    public Widgeon.View.Sparrow.Warbler.Table.Columns columns()
        {
        return new Widgeon.View.Sparrow.Warbler.Table.Columns()
            {
            @Override
            public Iterable<Widgeon.View.Sparrow.Warbler.Table.Column> select()
                {
                return womble().widgeons().views().sparrows().warblers().tables().columns().select(
                    TableViewEntity.this
                    ) ;
                }

            @Override
            public Widgeon.View.Sparrow.Warbler.Table.Column select(String name)
            throws NameNotFoundException
                {
                return womble().widgeons().views().sparrows().warblers().tables().columns().select(
                    TableViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public Widgeon.View.Sparrow.Warbler.Table.Column search(String name)
                {
                return womble().widgeons().views().sparrows().warblers().tables().columns().search(
                    TableViewEntity.this,
                    name
                    ) ;
                }
/*
            @Override
            public Widgeon.View.Sparrow.Warbler.Table.Column cascade(Widgeon.Base.Sparrow.Warbler.Table.Column base)
                {
                return womble().widgeons().views().sparrows().warblers().tables().columns().cascade(
                    TableViewEntity.this,
                    base
                    ) ;
                }
 */
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected TableViewEntity()
        {
        super();
        }

    /**
     * Create a new view.
     *
     */
    protected TableViewEntity(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base)
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
    protected TableViewEntity(final Widgeon.View.Sparrow.Warbler parent, final Widgeon.Base.Sparrow.Warbler.Table base, final String name)
        {
        super(
            name
            );
        this.base = base ;
        this.parent = parent ;
        }

    /**
     * Our parent Warbler.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = WarblerViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.View.Sparrow.Warbler parent ;

    @Override
    public Widgeon.View.Sparrow.Warbler parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Table.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TableBaseEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Base.Sparrow.Warbler.Table base ;

    @Override
    public Widgeon.Base.Sparrow.Warbler.Table base()
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

