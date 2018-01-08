/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.identity;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.exception.NotImplementedException;

/**
 *
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = AuthenticatedEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public class AuthenticatedEntity
extends AbstractEntity
implements Authenticated
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AuthenticationEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_METHOD_COL    = "method"    ;
    protected static final String DB_IDENTITY_COL  = "actor"     ;
    protected static final String DB_OPERATION_COL = "operation" ;

    @Component
    public static class EntityFactory
    extends AbstractEntityFactory<Authenticated>
    implements Authenticated.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AuthenticatedEntity.class ;
            }

        @Override
        @CreateMethod
        public Authenticated create(final Operation operation, final Identity identity, final String method)
           {
           log.debug("create(Operation, Identity, String)");
           log.debug("  Operation [{}]", operation.method());
           log.debug("  Identity  [{}]", identity.name());
           log.debug("  Method    [{}]", method);
           return this.insert(
                new AuthenticatedEntity(
                    operation,
                    identity,
                    method
                    )
                );
            }

        @Override
        public Authenticated current()
            {
            throw new NotImplementedException();
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements Authenticated.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AuthenticatedEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AuthenticatedEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (AuthenticatedEntity.EntityServices.instance == null)
                {
                AuthenticatedEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private Authenticated.IdentFactory idents;
        @Override
        public Authenticated.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private Authenticated.LinkFactory links;
        @Override
        public Authenticated.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Authenticated.EntityFactory entities;
        @Override
        public Authenticated.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected Authenticated.EntityFactory factory()
        {
        log.debug("factory()");
        return AuthenticatedEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Authenticated.EntityServices services()
        {
        log.debug("services()");
        return AuthenticatedEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected AuthenticatedEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected AuthenticatedEntity(final Operation operation, final Identity identity, final String method)
        {
        super(
            true
            );

        log.debug("AuthenticationEntity(Operation, Identity, String)");
        log.debug("  Operation [{}]", operation.method());
        log.debug("  Identity  [{}]", identity.name());
        log.debug("  Method    [{}]", method);

        this.method    = method    ;
        this.identity  = identity  ;
        this.operation = operation ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_METHOD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String method ;
    @Override
    public Method method()
        {
        return new Method()
            {
            @Override
            public String urn()
                {
                return AuthenticatedEntity.this.method;
                }
            };
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = IdentityEntity.class
        )
    @JoinColumn(
        name = DB_IDENTITY_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Identity identity ;
    @Override
    public Identity identity()
        {
        return this.identity ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = OperationEntity.class
        )
    @JoinColumn(
        name = DB_OPERATION_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Operation operation ;
    public Operation operation()
        {
        return this.operation;
        }
    }
