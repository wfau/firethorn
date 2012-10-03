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
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResourceEntity;

/**
 * AdqlService implementation.
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
            name  = "mallard-select-all",
            query = "FROM AdqlServiceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard-select-name",
            query = "FROM AdqlServiceEntity WHERE name = :name ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard-search-text",
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
                    "mallard-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<AdqlService> select(final String name)
            {
            return super.iterable(
                super.query(
                    "mallard-select-name"
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
            //
            // Using wildcards in a HQL query with named parameters.
            // http://www.stpe.se/2008/07/hibernate-hql-like-query-named-parameters/
            final String match = new StringBuilder(text).append("%").toString();
            log.debug("search(String)");
            log.debug("  Match [{}]", match);
            return super.iterable(
                super.query(
                    "mallard-search-text"
                    ).setString(
                        "text",
                        match
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public AdqlService create(final String name)
            {
            return super.insert(
                new AdqlServiceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected AdqlJob.Factory adqlJobs ;

        @Override
        public AdqlJob.Factory adqlJobs()
            {
            return this.adqlJobs ;
            }

        @Autowired
        protected AdqlService.IdentFactory identFactory ;

        @Override
        public String link(AdqlService entity)
            {
            return identFactory.link(
                entity.ident()
                );
            }
        @Override
        public String link(Identifier ident)
            {
            return identFactory.link(
                ident
                );
            }
        @Override
        public Identifier ident(final String string)
            {
            return identFactory.ident(
                string
                );
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
     * Create a new AdqlServiceEntity.
     *
     */
    protected AdqlServiceEntity(final String name)
        {
        super(name);
        }

    @Override
    public Jobs jobs()
        {
        return new Jobs()
            {
            @Override
            public AdqlJob create(final String name, final String adql)
                {
                return womble().services().adqlJobs().create(
                    AdqlServiceEntity.this,
                    name,
                    adql
                    ) ;
                }

            @Override
            public Iterable<AdqlJob> select()
                {
                return womble().services().adqlJobs().select(
                    AdqlServiceEntity.this
                    ) ;
                }
            };
        }

    /**
     * The collection of resources published by this service.
     *
     */
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinTable(
        name="service_resources",
        joinColumns = @JoinColumn(
            name="service",
            nullable = false,
            updatable = false
            ),
        inverseJoinColumns = @JoinColumn(
            name="resource",
            nullable = false,
            updatable = false
            )
        )
    private Set<AdqlResource> resources = new HashSet<AdqlResource>(0);
    protected Set<AdqlResource> getResources()
        {
        return this.resources ;
        }
    protected void setResources(final Set<AdqlResource> resources)
        {
        this.resources = resources ;
        }

    @Override
    public Resources resources()
        {
        return new Resources()
            {
            @Override
            public void insert(final AdqlResource resource)
                {
                resources.add(
                    resource
                    );
                }
            @Override
            public Iterable<AdqlResource> select()
                {
                return resources ;
                }
            };
        }

    @Override
    public String link()
        {
        return null;
        }
    }

