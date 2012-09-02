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
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceStatus;
import uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc.JdbcTableEntity;


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
            ),
        @NamedQuery(
            name  = "widgeon.view.table-select-view.base",
            query = "FROM TableViewEntity WHERE ((parent.parent.parent = :view) AND (base = :base)) ORDER BY ident desc"
            )
        }
    )
public class TableViewEntity
extends DataResourceEntity
implements DataResourceView.Table
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_table" ;

    /**
     * The persistence column name for our parent Schema.
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
    extends AbstractFactory<DataResourceView.Table>
    implements DataResourceView.Table.Factory
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
        protected DataResourceView.Table insert(TableViewEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceBase.Column column : entity.base().columns().select())
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
        protected DataResourceView.Table create(final DataResourceView.Schema parent, final DataResourceBase.Table base)
            {
            return this.insert(
                new TableViewEntity(
                    parent,
                    base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceView.Table search(final DataResourceView.Schema parent, final DataResourceBase.Table base)
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
        public DataResourceView.Table cascade(final DataResourceView.Schema parent, final DataResourceBase.Table base)
            {
            DataResourceView.Table result = this.search(
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
        public DataResourceView.Table create(final DataResourceView.Schema parent, final DataResourceBase.Table base, final String name)
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
        public Iterable<DataResourceView.Table> select(final DataResourceView.Schema parent)
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
        public DataResourceView.Table select(final DataResourceView.Schema parent, final String name)
        throws NameNotFoundException
            {
            DataResourceView.Table result = this.search(
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
        public DataResourceView.Table search(final DataResourceView.Schema parent, final String name)
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
        public Iterable<DataResourceView.Table> select(final DataResourceBase.Table base)
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
        protected DataResourceView.Column.Factory columns ;

        @Override
        public DataResourceView.Column.Factory columns()
            {
            return this.columns ;
            }
        }

    @Override
    public DataResourceView.Table.Columns columns()
        {
        return new DataResourceView.Table.Columns()
            {
            @Override
            public Iterable<DataResourceView.Column> select()
                {
                return womble().resources().views().catalogs().schemas().tables().columns().select(
                    TableViewEntity.this
                    ) ;
                }

            @Override
            public DataResourceView.Column select(String name)
            throws NameNotFoundException
                {
                return womble().resources().views().catalogs().schemas().tables().columns().select(
                    TableViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceView.Column search(String name)
                {
                return womble().resources().views().catalogs().schemas().tables().columns().search(
                    TableViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceView.Column search(DataResourceBase.Column base)
                {
                return womble().resources().views().catalogs().schemas().tables().columns().search(
                    TableViewEntity.this,
                    base
                    );
                }
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
    protected TableViewEntity(final DataResourceView.Schema parent, final DataResourceBase.Table base)
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
    protected TableViewEntity(final DataResourceView.Schema parent, final DataResourceBase.Table base, final String name)
        {
        super(
            name
            );
        this.base = base ;
        this.parent = parent ;
        }

    /**
     * Our parent Schema.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = SchemaViewEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DataResourceView.Schema parent ;

    @Override
    public DataResourceView.Schema parent()
        {
        return this.parent ;
        }

    /**
     * Our underlying Table.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DataResourceBase.Table base ;

    @Override
    public DataResourceBase.Table base()
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
        return this.parent.catalog().widgeon();
        }

    @Override
    public DataResourceView.Catalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public DataResourceView.Schema schema()
        {
        return this.parent;
        }
    }

