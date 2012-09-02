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

import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

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

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

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
import uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc.JdbcResourceEntity;

/**
 * DataResource View implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = DataResourceViewEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                DataResourceViewEntity.DB_BASE_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.view.entity-select-base",
            query = "FROM DataResourceViewEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.view.entity-select-base.name",
            query = "FROM DataResourceViewEntity WHERE ((base = :base) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class DataResourceViewEntity
extends DataResourceEntity
implements DataResourceView
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_view_entity" ;

    /**
     * The persistence column name for our base DataResource.
     * 
     */
    public static final String DB_BASE_COL = "base" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<DataResourceView>
    implements DataResourceView.Factory
        {

        @Override
        public Class etype()
            {
            return DataResourceViewEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected DataResourceView insert(DataResourceViewEntity entity)
            {
            super.insert(
                entity
                );
            for (DataResourceBase.Catalog catalog : entity.base().catalogs().select())
                {
                this.catalogs().cascade(
                    entity,
                    catalog
                    );
                }
            return entity ;
            }

        @Override
        @CreateEntityMethod
        public DataResourceView create(final DataResourceBase base, final String name)
            {
            return this.insert(
                new DataResourceViewEntity(
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataResourceView> select(final DataResourceBase base)
            {
            return super.iterable(
                super.query(
                    "widgeon.view.entity-select-base"
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public DataResourceView select(final DataResourceBase base, final String name)
        throws NameNotFoundException
            {
            DataResourceView result = this.search(
                base,
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
        public DataResourceView search(final DataResourceBase base, final String name)
            {
            return super.first(
                super.query(
                    "widgeon.view.entity-select-base.name"
                    ).setEntity(
                        "base",
                        base
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        /**
         * Our Autowired Catalog factory.
         * 
         */
        @Autowired
        protected DataResourceView.Catalog.Factory catalogs ;

        @Override
        public DataResourceView.Catalog.Factory catalogs()
            {
            return catalogs ;
            }
        }

    @Override
    public DataResourceView.Catalogs catalogs()
        {
        return new DataResourceView.Catalogs()
            {
            @Override
            public Iterable<DataResourceView.Catalog> select()
                {
                return womble().resources().views().catalogs().select(
                    DataResourceViewEntity.this
                    ) ;
                }

            @Override
            public DataResourceView.Catalog select(final String name)
            throws NameNotFoundException
                {
                return womble().resources().views().catalogs().select(
                    DataResourceViewEntity.this,
                    name
                    ) ;
                }

            @Override
            public DataResourceView.Catalog search(final String name)
                {
                return womble().resources().views().catalogs().search(
                    DataResourceViewEntity.this,
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
    protected DataResourceViewEntity()
        {
        super();
        }

    /**
     * Create a new view of a DataResource.
     *
     */
    private DataResourceViewEntity(final DataResourceBase base)
        {
        this(
            base,
            null
            );
        }

    /**
     * Create a new view of a DataResource.
     *
     */
    private DataResourceViewEntity(final DataResourceBase base, final String name)
        {
        super(
            name
            );
        this.base = base ;
        }

    /**
     * Our underlying DataResource.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private DataResourceBase base ;

    @Override
    public DataResourceBase base()
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
        if (this.base().status() == DataResource.Status.ENABLED)
            {
            return super.status();
            }
        else {
            return this.base().status();
            }
        }
    }

