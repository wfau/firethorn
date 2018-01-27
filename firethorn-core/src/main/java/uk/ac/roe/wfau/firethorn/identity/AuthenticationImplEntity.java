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
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
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
    name = AuthenticationImplEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public class AuthenticationImplEntity
extends AbstractEntity
implements AuthenticationImpl
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
    protected static final String DB_OPERATION_COL = "operation" ;
    protected static final String DB_METHOD_COL    = "method"    ;
    protected static final String DB_IDENTITY_COL  = "identity"  ;

    @Component
    public static class EntityFactory
    extends AbstractEntityFactory<AuthenticationImpl>
    implements AuthenticationImpl.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAllowCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return AuthenticationImplEntity.class ;
            }

        @Override
        @CreateMethod
        public AuthenticationImpl create(final Operation operation, final Identity identity, final String method)
           {
           log.debug("create(Operation, Identity, String)");
           log.debug("  Operation [{}]", operation.method());
           log.debug("  Identity  [{}]", identity.name());
           log.debug("  Method    [{}]", method);
           return this.insert(
                new AuthenticationImplEntity(
                    operation,
                    identity,
                    method
                    )
                );
            }

        @Override
        public AuthenticationImpl current()
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
    implements AuthenticationImpl.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AuthenticationImplEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AuthenticationImplEntity.EntityServices.instance ;
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
            if (AuthenticationImplEntity.EntityServices.instance == null)
                {
                AuthenticationImplEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AuthenticationImpl.IdentFactory idents;
        @Override
        public AuthenticationImpl.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AuthenticationImpl.LinkFactory links;
        @Override
        public AuthenticationImpl.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AuthenticationImpl.EntityFactory entities;
        @Override
        public AuthenticationImpl.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected AuthenticationImpl.EntityFactory factory()
        {
        log.debug("factory()");
        return AuthenticationImplEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AuthenticationImpl.EntityServices services()
        {
        log.debug("services()");
        return AuthenticationImplEntity.EntityServices.instance() ; 
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
    protected AuthenticationImplEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected AuthenticationImplEntity(final Operation operation, final Identity identity, final String method)
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
                return AuthenticationImplEntity.this.method;
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

    @Override
    public String toString()
        {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("Authentication[");
            buffer.append("Ident[");
            buffer.append(ident());
            buffer.append("]");
    
            buffer.append("Identity[");
            buffer.append(identity().name());
            buffer.append("]");
            
            buffer.append("Method[");
            buffer.append(method);
            buffer.append("]");
        buffer.append("]");
       
        return buffer.toString();
        }
    }
