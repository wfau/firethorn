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

import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.DataResourceEntityBase;

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
extends DataResourceEntityBase
implements DataResourceBase.Table
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
    extends AbstractFactory<DataResourceBase.Table>
    implements DataResourceBase.Table.Factory
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
        protected DataResourceBase.Table insert(final TableBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceView.Schema view : entity.parent().views().select())
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
        public DataResourceBase.Table create(final DataResourceBase.Schema parent, final String name)
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
        public Iterable<DataResourceBase.Table> select(final DataResourceBase.Schema parent)
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
        public DataResourceBase.Table select(final DataResourceBase.Schema parent, final String name)
        throws NameNotFoundException
            {
            DataResourceBase.Table result = this.search(
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
        public DataResourceBase.Table search(final DataResourceBase.Schema parent, final String name)
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
        protected DataResourceView.Table.Factory views ;

        @Override
        public DataResourceView.Table.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Column factory.
         * 
         */
        @Autowired
        protected DataResourceBase.Column.Factory columns ;

        @Override
        public DataResourceBase.Column.Factory columns()
            {
            return this.columns ;
            }
        }

    @Override
    public DataResourceBase.Table.Views views()
        {
        return new DataResourceBase.Table.Views()
            {
            @Override
            public Iterable<DataResourceView.Table> select()
                {
                return womble().resources().views().catalogs().schemas().tables().select(
                    TableBaseEntity.this
                    );
                }

            @Override
            public DataResourceView.Table search(DataResourceView.Schema parent)
                {
                return womble().resources().views().catalogs().schemas().tables().search(
                    parent,
                    TableBaseEntity.this
                    );
                }
            };
        }

    @Override
    public DataResourceBase.Table.Columns columns()
        {
        return new DataResourceBase.Table.Columns()
            {
            @Override
            public DataResourceBase.Column create(String name)
                {
                return womble().resources().catalogs().schemas().tables().columns().create(
                    TableBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<DataResourceBase.Column> select()
                {
                return womble().resources().catalogs().schemas().tables().columns().select(
                    TableBaseEntity.this
                    ) ;
                }

            @Override
            public DataResourceBase.Column select(String name)
            throws NameNotFoundException
                {
                return womble().resources().catalogs().schemas().tables().columns().select(
                    TableBaseEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceBase.Column search(String name)
                {
                return womble().resources().catalogs().schemas().tables().columns().search(
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
    protected TableBaseEntity(final DataResourceBase.Schema parent, final String name)
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
    private DataResourceBase.Schema parent ;

    @Override
    public DataResourceBase.Schema parent()
        {
        return this.parent ;
        }

    @Override
    public DataResource.Status status()
        {
        if (this.parent().status() == DataResource.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.parent().status();
            }
        }

    @Override
    public DataResourceBase widgeon()
        {
        return this.parent.catalog().widgeon();
        }

    @Override
    public DataResourceBase.Catalog catalog()
        {
        return this.parent.catalog();
        }

    @Override
    public DataResourceBase.Schema schema()
        {
        return this.parent;
        }
    }

