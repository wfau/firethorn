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
package uk.ac.roe.wfau.firethorn.widgeon.entity.adql ;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CascadeEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.ResourceStatusEntity;
import uk.ac.roe.wfau.firethorn.widgeon.entity.jdbc.JdbcResourceEntity;

/**
 * AdqlResource implementations.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlResourceEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                AdqlResourceEntity.DB_BASE_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.resource-select-base",
            query = "FROM AdqlResourceEntity WHERE (base = :base) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.resource-select-base.name",
            query = "FROM AdqlResourceEntity WHERE ((base = :base) AND (name = :name)) ORDER BY ident desc"
            )
        }
    )
public class AdqlResourceEntity
extends ResourceStatusEntity
implements AdqlResource
    {

    /**
     * Our persistence table name.
     * 
     */
    public static final String DB_TABLE_NAME = "adql_resource" ;

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
    extends AbstractFactory<AdqlResource>
    implements AdqlResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return AdqlResourceEntity.class ;
            }

        /**
         * Insert a View into the database and create views for each child.
         *
         */
        @CascadeEntityMethod
        protected AdqlResource insert(AdqlResourceEntity entity)
            {
            super.insert(
                entity
                );
            for (BaseResource.BaseCatalog<?> catalog : entity.base().catalogs().select())
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
        public AdqlResource create(final BaseResource base, final String name)
            {
            return this.insert(
                new AdqlResourceEntity(
                    base,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlResource> select(final BaseResource base)
            {
            return super.iterable(
                super.query(
                    "adql.resource-select-base"
                    ).setEntity(
                        "base",
                        base
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public AdqlResource select(final BaseResource base, final String name)
        throws NameNotFoundException
            {
            AdqlResource result = this.search(
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
        public AdqlResource search(final BaseResource base, final String name)
            {
            return super.first(
                super.query(
                    "adql.resource-select-base.name"
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
         * Our Autowired catalog factory.
         * 
         */
        @Autowired
        protected AdqlResource.AdqlCatalog.Factory catalogs ;

        @Override
        public AdqlResource.AdqlCatalog.Factory catalogs()
            {
            return catalogs ;
            }
        }

    @Override
    public AdqlResource.Catalogs catalogs()
        {
        return new AdqlResource.Catalogs()
            {
            @Override
            public Iterable<AdqlResource.AdqlCatalog> select()
                {
                return womble().resources().base().views().catalogs().select(
                    AdqlResourceEntity.this
                    ) ;
                }

            @Override
            public AdqlResource.AdqlCatalog select(final String name)
            throws NameNotFoundException
                {
                return womble().resources().base().views().catalogs().select(
                    AdqlResourceEntity.this,
                    name
                    ) ;
                }

            @Override
            public AdqlResource.AdqlCatalog search(final String name)
                {
                return womble().resources().base().views().catalogs().search(
                    AdqlResourceEntity.this,
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
    protected AdqlResourceEntity()
        {
        super();
        }

    /**
     * Create a new view of a resource.
     *
    private AdqlResourceEntity(final BaseResource base)
        {
        this(
            base,
            null
            );
        }
     */

    /**
     * Create a new view of a resource.
     *
     */
    private AdqlResourceEntity(final BaseResource base, final String name)
        {
        super(
            name
            );
        log.debug("new([{}]", name);
        this.base = base ;
        }

    /**
     * Our underlying base resource.
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
    private BaseResource base ;

    @Override
    public BaseResource base()
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

