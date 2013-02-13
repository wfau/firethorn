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
package uk.ac.roe.wfau.firethorn.mallard ;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcColumnEntity;

/**
 * Hibernate based <code>AdqlService</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AdqlServiceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "adql.service-select-all",
            query = "FROM AdqlServiceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.service-select-resource",
            query = "FROM AdqlServiceEntity WHERE resource = :resource ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.service-select-name",
            query = "FROM AdqlServiceEntity WHERE name = :name ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "adql.service-search-text",
            query = "FROM AdqlServiceEntity WHERE name LIKE :text ORDER BY ident desc"
            )
        }
    )
public class AdqlServiceEntity
extends AbstractEntity
implements AdqlService
    {

    /**
     * Our database table name.
     *
     */
    public static final String DB_TABLE_NAME = "service_entity" ;

    /**
     * The persistence column name for our target resource.
     *
     */
    public static final String DB_RESOURCE_COL = "resource" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<AdqlService>
    implements AdqlService.Factory
        {

        @Override
        public Class<AdqlServiceEntity> etype()
            {
            return AdqlServiceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlService> select()
            {
            return super.iterable(
                super.query(
                    "adql.service-select-all"
                    )
                );
            }
		@Override
        @SelectEntityMethod
		public Iterable<AdqlService> select(final AdqlResource resource)
			{
            return super.iterable(
                super.query(
                    "adql.service-select-resource"
                    ).setEntity(
                        "resource",
                        resource
                        )
                );
			}

        @Override
        @SelectEntityMethod
        public Iterable<AdqlService> select(final String name)
            {
            return super.iterable(
                super.query(
                    "adql.service-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlService> search(final String text)
            {
            return super.iterable(
                super.query(
                    "adql.service-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlService create(final AdqlResource resource, final String name)
            {
            return super.insert(
                new AdqlServiceEntity(
                		resource,
                		name
                    )
                );
            }

        @Autowired
        protected AdqlJob.Factory jobs ;

        @Override
        public AdqlJob.Factory jobs()
            {
            return this.jobs ;
            }

        @Autowired
        protected AdqlService.IdentFactory identifiers ;

        @Override
        public AdqlService.IdentFactory identifiers()
            {
            return this.identifiers;
            }

        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected AdqlServiceEntity()
        {
        super();
        }

    /**
     * Create a new AdqlServiceEntity, using the target AdqlResource name.
     *
     */
    protected AdqlServiceEntity(final AdqlResource resource)
        {
        this(
			resource,
			resource.name()
        	);
        }

    /**
     * Create a new AdqlServiceEntity.
     *
     */
    protected AdqlServiceEntity(final AdqlResource resource, final String name)
        {
        super(name);
        this.resource = resource ;
        }


    /**
     * The target resource published by this service.
     *
     */
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_RESOURCE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private AdqlResource resource ;
    
    @Override
    public AdqlResource resource()
        {
        return this.resource ;
        }

    
    @Override
    public Jobs jobs()
        {
        return new Jobs()
            {
            @Override
            public AdqlJob create(final String name, final String adql)
                {
                return womble().adql().services().jobs().create(
                    AdqlServiceEntity.this,
                    name,
                    adql
                    ) ;
                }

            @Override
            public Iterable<AdqlJob> select()
                {
                return womble().adql().services().jobs().select(
                    AdqlServiceEntity.this
                    ) ;
                }
            };
        }
    
    @Override
    public String link()
        {
        return womble().adql().services().link(
            this
            );
        }
    }

