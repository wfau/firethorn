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

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
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

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;
import uk.ac.roe.wfau.firethorn.widgeon.entity.view.DataResourceViewEntity;

/**
 * DataService implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = DataServiceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "mallard-select-all",
            query = "FROM DataServiceEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard-select-name",
            query = "FROM DataServiceEntity WHERE name = :name ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard-search-text",
            query = "FROM DataServiceEntity WHERE name LIKE :text ORDER BY ident desc"
            )
        }
    )
public class DataServiceEntity
extends AbstractEntity
implements DataService
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
    extends AbstractFactory<DataService>
    implements DataService.Factory
        {

        @Override
        public Class etype()
            {
            return DataServiceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataService> select()
            {
            return super.iterable(
                super.query(
                    "mallard-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<DataService> select(String name)
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
        public Iterable<DataService> search(String text)
            {
            //
            // Using wildcards in a HQL query with named parameters.
            // http://www.stpe.se/2008/07/hibernate-hql-like-query-named-parameters/
            String match = new StringBuilder(text).append("%").toString();
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
        public DataService create(String name)
            {
            return super.insert(
                new DataServiceEntity(
                    name
                    )
                );
            }

        /**
         * Our Autowired Job factory.
         * 
         */
        @Autowired
        protected Job.Factory jobs ;

        @Override
        public Job.Factory jobs()
            {
            return this.jobs ;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected DataServiceEntity()
        {
        super();
        }

    /**
     * Create a new DataServiceEntity.
     *
     */
    protected DataServiceEntity(String name)
        {
        super(name);
        }

    @Override
    public Jobs jobs()
        {
        return new Jobs()
            {
            @Override
            public Job create(String name, String adql)
                {
                return womble().services().jobs().create(
                    DataServiceEntity.this,
                    name,
                    adql
                    ) ;
                }

            @Override
            public Iterable<Job> select()
                {
                return womble().services().jobs().select(
                    DataServiceEntity.this
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
        targetEntity = DataResourceViewEntity.class
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
    private Set<DataResourceView> resources = new HashSet<DataResourceView>(0);
    protected Set<DataResourceView> getResources()
        {
        return this.resources ;
        }
    protected void setResources(Set<DataResourceView> resources)
        {
        this.resources = resources ;
        }

    @Override
    public Resources resources()
        {
        return new Resources()
            {
            @Override
            public void insert(DataResourceView resource)
                {
                resources.add(
                    resource
                    );
                }
            @Override
            public Iterable<DataResourceView> select()
                {
                return resources ;
                }
            };
        }
    }

