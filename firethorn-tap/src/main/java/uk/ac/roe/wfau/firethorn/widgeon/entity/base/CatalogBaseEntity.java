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
import uk.ac.roe.wfau.firethorn.widgeon.entity.AbstractDataResourceEntity;

/**
 * Catalog implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = CatalogBaseEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                CatalogBaseEntity.DB_PARENT_COL,
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.base.catalog-select-parent",
            query = "FROM CatalogBaseEntity WHERE (parent = :parent) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.base.catalog-select-parent.name",
            query = "FROM CatalogBaseEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class CatalogBaseEntity
extends AbstractDataResourceEntity
implements DataResourceBase.Catalog
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_base_catalog" ;

    /**
     * The persistence column name for our parent DataResource.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<DataResourceBase.Catalog>
    implements DataResourceBase.Catalog.Factory
        {

        @Override
        public Class etype()
            {
            return CatalogBaseEntity.class ;
            }

        /**
         * Insert a Catalog into the database and update all the parent views.
         *
         */
        @CascadeEntityMethod
        protected DataResourceBase.Catalog insert(final CatalogBaseEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceView view : entity.parent().views().select())
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
        public DataResourceBase.Catalog create(final DataResourceBase parent, final String name)
            {
            return this.insert(
                new CatalogBaseEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataResourceBase.Catalog> select(final DataResourceBase parent)
            {
            return super.iterable(
                super.query(
                    "widgeon.base.catalog-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceBase.Catalog select(final DataResourceBase parent, final String name)
        throws NameNotFoundException
            {
            DataResourceBase.Catalog result = this.search(
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
        public DataResourceBase.Catalog search(final DataResourceBase parent, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.base.catalog-select-parent.name"
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
        protected DataResourceView.Catalog.Factory views ;

        @Override
        public DataResourceView.Catalog.Factory views()
            {
            return this.views ;
            }

        /**
         * Our Autowired Schema factory.
         * 
         */
        @Autowired
        protected DataResourceBase.Schema.Factory schemas ;

        @Override
        public DataResourceBase.Schema.Factory schemas()
            {
            return this.schemas ;
            }
        }

    @Override
    public DataResourceBase.Catalog.Views views()
        {
        return new DataResourceBase.Catalog.Views()
            {
            @Override
            public Iterable<DataResourceView.Catalog> select()
                {
                return womble().resources().views().catalogs().select(
                    CatalogBaseEntity.this
                    );
                }

            @Override
            public DataResourceView.Catalog search(DataResourceView parent)
                {
                return womble().resources().views().catalogs().search(
                    parent,
                    CatalogBaseEntity.this
                    );
                }
            };
        }

    @Override
    public DataResourceBase.Catalog.Schemas schemas()
        {
        return new DataResourceBase.Catalog.Schemas()
            {
            @Override
            public DataResourceBase.Schema create(String name)
                {
                return womble().resources().catalogs().schemas().create(
                    CatalogBaseEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<DataResourceBase.Schema> select()
                {
                return womble().resources().catalogs().schemas().select(
                    CatalogBaseEntity.this
                    );
                }

            @Override
            public DataResourceBase.Schema select(String name)
            throws NameNotFoundException
                {
                return womble().resources().catalogs().schemas().select(
                    CatalogBaseEntity.this,
                    name
                    );
                }

            @Override
            public DataResourceBase.Schema search(String name)
                {
                return womble().resources().catalogs().schemas().search(
                    CatalogBaseEntity.this,
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
    protected CatalogBaseEntity()
        {
        super();
        }

    /**
     * Create a new Catalog.
     *
     */
    protected CatalogBaseEntity(final DataResourceBase parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent DataResource.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = DataResourceBaseEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DataResourceBase parent ;

    @Override
    public DataResourceBase parent()
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
        return this.parent;
        }
    }

