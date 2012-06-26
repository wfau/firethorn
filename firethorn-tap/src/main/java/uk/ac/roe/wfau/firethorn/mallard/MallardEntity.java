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

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.entity.view.WidgeonViewEntity;

/**
 * Mallard implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = MallardEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "mallard-select",
            query = "FROM MallardEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "mallard-select-name",
            query = "FROM MallardEntity WHERE name = :name ORDER BY ident desc"
            )
        }
    )
public class MallardEntity
extends AbstractEntity
implements Mallard
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "mallard_entity" ;

    /**
     * Our Entity Factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Mallard>
    implements Mallard.Factory
        {

        @Override
        public Class etype()
            {
            return MallardEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Mallard> select()
            {
            return super.iterable(
                super.query(
                    "mallard-select"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Mallard create(String name)
            {
            return super.insert(
                new MallardEntity(
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

        /**
         * Access to our Job factory.
         * 
         */
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
    protected MallardEntity()
        {
        super();
        }

    /**
     * Create a new MallardEntity.
     *
     */
    protected MallardEntity(String name)
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
                return womble().mallard().jobs().create(
                    MallardEntity.this,
                    name,
                    adql
                    ) ;
                }

            @Override
            public Iterable<Job> select()
                {
                return womble().mallard().jobs().select(
                    MallardEntity.this
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
        targetEntity = WidgeonViewEntity.class
        )
    @JoinTable(
        name="mallard_widgeons",
        joinColumns = @JoinColumn(
            name="mallard_x",
            nullable = false,
            updatable = false
            ),
        inverseJoinColumns = @JoinColumn(
            name="widgeon_x",
            nullable = false,
            updatable = false
            )
        )
    private Set<Widgeon> widgeons = new HashSet<Widgeon>(0);
    protected Set<Widgeon> getWidgeons()
        {
        return this.widgeons ;
        }
    protected void setWidgeons(Set<Widgeon> set)
        {
        this.widgeons = set ;
        }

    /**
     * Access to the resources used by this service.
     *
     */
    @Override
    public Widgeons widgeons()
        {
        return new Widgeons()
            {
            @Override
            public void insert(Widgeon widgeon)
                {
                widgeons.add(
                    (Widgeon) widgeon
                    );
                }
            @Override
            public Iterable<Widgeon> select()
                {
                return widgeons ;
                }
            };
        }
    }

