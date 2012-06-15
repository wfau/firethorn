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
package uk.ac.roe.wfau.firethorn.identity ;

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

/**
 * Identity implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = IdentityEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "identity-select",
            query = "FROM IdentityEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "identity-select-name",
            query = "FROM IdentityEntity WHERE name = :name ORDER BY ident desc"
            )
        }
    )
public class IdentityEntity
extends AbstractEntity
implements Identity
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "identity_entity" ;

    /**
     * Identity context.
     *
     */
    public static class Context
    implements Identity.Context
        {
        /**
         * Autowired Identity Factory.
         *
         */
        @Autowired
        private Identity.Factory factory ;

        /**
         * ThreadLocal storage for the current Identity.
         *
         */
        private final ThreadLocal<Identity> tracker = new ThreadLocal<Identity>()
            {
            @Override
            protected Identity initialValue()
                {
                log.debug("initial()");
                return factory.create(
                    "anon-identity"
                    );
                }            
            };

        @Override
        public Identity current()
            {
            log.debug("current()");
            return tracker.get();
            }
        }

    /**
     * Identity factory.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Identity>
    implements Identity.Factory
        {
        @Override
        public Class etype()
            {
            return IdentityEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Identity> select()
            {
            return super.iterable(
                super.query(
                    "identity-select"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Identity create(String name)
            {
            return super.insert(
                new IdentityEntity(
                    name
                    )
                );
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected IdentityEntity()
        {
        super();
        }

    /**
     * Create a new IdentityEntity.
     *
     */
    protected IdentityEntity(String name)
        {
        super(name);
        }
    }

