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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

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
            query = "FROM TableViewEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.table-select-parent.name",
            query = "FROM TableViewEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class TableViewEntity
extends WidgeonStatusEntity
implements Widgeon.View.Schema.Catalog.Table
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_table" ;

    /**
     * The persistence column name for our parent Catalog.
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
    extends AbstractFactory<Widgeon.View.Schema.Catalog.Table>
    implements Widgeon.View.Schema.Catalog.Table.Factory
        {

        @Override
        public Class etype()
            {
            return TableViewEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public Widgeon.View.Schema.Catalog.Table create(final Widgeon.View.Schema.Catalog parent, final Widgeon.Base.Schema.Catalog.Table base, final String name)
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
        public Iterable<Widgeon.View.Schema.Catalog.Table> select(final Widgeon.View.Schema.Catalog parent)
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
        public Widgeon.View.Schema.Catalog.Table select(final Widgeon.View.Schema.Catalog parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
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
            catch(EntityNotFoundException ouch)
                {
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public Widgeon.View.Schema.Catalog.Table search(final Widgeon.View.Schema.Catalog parent, final String name)
            {
            return null ;
            }

        /**
         * Our Autowired Column factory.
         * 
         */
        @Autowired
        protected Widgeon.View.Schema.Catalog.Table.Column.Factory columns ;

        @Override
        public Widgeon.View.Schema.Catalog.Table.Column.Factory columns()
            {
            return this.columns ;
            }

        }

    @Override
    public Widgeon.View.Schema.Catalog.Table.Columns columns()
        {
        return new Widgeon.View.Schema.Catalog.Table.Columns()
            {

            @Override
            public Iterable<Widgeon.View.Schema.Catalog.Table.Column> select()
                {
                return null ;
                }

            @Override
            public Widgeon.View.Schema.Catalog.Table.Column select(String name)
            throws NameNotFoundException
                {
                return null ;
                }

            @Override
            public Widgeon.View.Schema.Catalog.Table.Column search(String name)
                {
                return null ;
                }

            };
        }

    /**
     * Check a view name, using the base name if the given name is null or empty.
     *
     */
    private static String name(final Widgeon.Base.Schema.Catalog.Table base, final String name)
        {
        if ((name == null) || (name.trim().length() == 0))
            {
            return base.name();
            }
        else {
            return name.trim() ;
            }
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
    protected TableViewEntity(final Widgeon.View.Schema.Catalog parent, final Widgeon.Base.Schema.Catalog.Table base)
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
    protected TableViewEntity(final Widgeon.View.Schema.Catalog parent, final Widgeon.Base.Schema.Catalog.Table base, final String name)
        {
        super(
            name(
                base,
                name
                )
            );
        this.base = base ;
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
    private Widgeon.View.Schema.Catalog parent ;

    @Override
    public Widgeon.View.Schema.Catalog parent()
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
    private Widgeon.Base.Schema.Catalog.Table base ;

    @Override
    public Widgeon.Base.Schema.Catalog.Table base()
        {
        return this.base ;
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

