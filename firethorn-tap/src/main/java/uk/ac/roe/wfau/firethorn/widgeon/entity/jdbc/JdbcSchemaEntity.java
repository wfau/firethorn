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
package uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc ;

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
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceStatus;

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
    name = JdbcSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                JdbcSchemaEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.schema-select-parent",
            query = "FROM JdbcSchemaEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "jdbc.schema-select-parent.name",
            query = "FROM JdbcSchemaEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class JdbcSchemaEntity
extends DataResourceEntity
implements DataResourceBase.Schema
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "jdbc_schema" ;

    /**
     * The persistence column name for our parent Catalog.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<DataResourceBase.Schema>
    implements DataResourceBase.Schema.Factory
        {

        @Override
        public Class etype()
            {
            return JdbcSchemaEntity.class ;
            }

        /**
         * Insert a Schema into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected DataResourceBase.Schema insert(final JdbcSchemaEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceView.Catalog view : entity.parent().views().select())
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
        public DataResourceBase.Schema create(final DataResourceBase.Catalog parent, final String name)
            {
            return this.insert(
                new JdbcSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataResourceBase.Schema> select(final DataResourceBase.Catalog parent)
            {
            return super.iterable(
                super.query(
                    "jdbc.schema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceBase.Schema select(final DataResourceBase.Catalog parent, final String name)
        throws NameNotFoundException
            {
            DataResourceBase.Schema result = this.search(
                parent,
                name
                );
            if (result != null)
                {
                return result;
                }
            else {
                throw new NameNotFoundException(
                    name
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public DataResourceBase.Schema search(final DataResourceBase.Catalog parent, final String name)
            {
            return super.first(
                super.query(
                    "jdbc.schema-select-parent.name"
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
        protected DataResourceView.Schema.Factory views ;

        @Override
        public DataResourceView.Schema.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Table factory.
         * 
         */
        @Autowired
        protected DataResourceBase.Table.Factory tables ;

        @Override
        public DataResourceBase.Table.Factory tables()
            {
            return this.tables ;
            }
        }

    @Override
    public DataResourceBase.Schema.Views views()
        {
        return new DataResourceBase.Schema.Views()
            {
            @Override
            public Iterable<DataResourceView.Schema> select()
                {
                return womble().resources().views().catalogs().schemas().select(
                    JdbcSchemaEntity.this
                    );
                }

            @Override
            public DataResourceView.Schema search(DataResourceView.Catalog parent)
                {
                return womble().resources().views().catalogs().schemas().search(
                    parent,
                    JdbcSchemaEntity.this
                    );
                }
            };
        }

    @Override
    public DataResourceBase.Schema.Tables tables()
        {
        return new DataResourceBase.Schema.Tables()
            {
            @Override
            public DataResourceBase.Table create(String name)
                {
                return womble().resources().catalogs().schemas().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<DataResourceBase.Table> select()
                {
                return womble().resources().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this
                    ) ;
                }

            @Override
            public DataResourceBase.Table select(String name)
            throws NameNotFoundException
                {
                return womble().resources().catalogs().schemas().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceBase.Table search(String name)
                {
                return womble().resources().catalogs().schemas().tables().search(
                    JdbcSchemaEntity.this,
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
    protected JdbcSchemaEntity()
        {
        super();
        }

    /**
     * Create a new Catalog.
     *
     */
    protected JdbcSchemaEntity(final DataResourceBase.Catalog parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Catalog.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcCatalogEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DataResourceBase.Catalog parent ;

    @Override
    public DataResourceBase.Catalog parent()
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
        return this.parent.widgeon();
        }

    @Override
    public DataResourceBase.Catalog catalog()
        {
        return this.parent;
        }
    }

