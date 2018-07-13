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
    name = AuthMethodEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public class AuthMethodEntity
extends AbstractEntity
implements AuthMethod
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "AuthMethodEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_OPERATION_COL = "operation" ;
    protected static final String DB_METHOD_COL    = "method"    ;
    protected static final String DB_IDENTITY_COL  = "identity"  ;

    @Component
    public static class EntityFactory
    extends AbstractEntityFactory<AuthMethod>
    implements AuthMethod.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAllowCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return AuthMethodEntity.class ;
            }

        @Override
        @CreateMethod
        public AuthMethod create(final Operation operation, final Identity identity, final String method)
           {
           log.debug("create() [{}][{}][{}][{}]",
               operation.ident(),
               identity.ident(),
               identity.name(),
               method
               );
           return this.insert(
                new AuthMethodEntity(
                    operation,
                    identity,
                    method
                    )
                );
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements AuthMethod.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AuthMethodEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AuthMethodEntity.EntityServices.instance ;
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
            if (AuthMethodEntity.EntityServices.instance == null)
                {
                AuthMethodEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private AuthMethod.IdentFactory idents;
        @Override
        public AuthMethod.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private AuthMethod.LinkFactory links;
        @Override
        public AuthMethod.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private AuthMethod.EntityFactory entities;
        @Override
        public AuthMethod.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected AuthMethod.EntityFactory factory()
        {
        return AuthMethodEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected AuthMethod.EntityServices services()
        {
        return AuthMethodEntity.EntityServices.instance() ; 
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
    protected AuthMethodEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected AuthMethodEntity(final Operation operation, final Identity identity, final String method)
        {
        super(
            true
            );
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
                return AuthMethodEntity.this.method;
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
    protected Operation operation()
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
