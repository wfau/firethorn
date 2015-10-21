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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    name = AuthenticationEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public class AuthenticationEntity
extends AbstractEntity
implements Authentication
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
    extends AbstractEntityFactory<Authentication>
    implements Authentication.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return AuthenticationEntity.class ;
            }

        @Override
        @CreateMethod
        public Authentication create(final Operation operation, final Identity identity, final String method)
           {
           log.debug("create(Operation, Identity, String)");
           log.debug("  Operation [{}]", operation.method());
           log.debug("  Identity  [{}]", identity.name());
           log.debug("  Method    [{}]", method);
           return this.insert(
                new AuthenticationEntity(
                    operation,
                    identity,
                    method
                    )
                );
            }

        @Override
        public Authentication current()
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
    implements Authentication.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static AuthenticationEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return AuthenticationEntity.EntityServices.instance ;
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
            if (AuthenticationEntity.EntityServices.instance == null)
                {
                AuthenticationEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private Authentication.IdentFactory idents;
        @Override
        public Authentication.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private Authentication.LinkFactory links;
        @Override
        public Authentication.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Authentication.EntityFactory entities;
        @Override
        public Authentication.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected Authentication.EntityFactory factory()
        {
        log.debug("factory()");
        return AuthenticationEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Authentication.EntityServices services()
        {
        log.debug("services()");
        return AuthenticationEntity.EntityServices.instance() ; 
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
    protected AuthenticationEntity()
        {
        }

    /**
     * Protected constructor.
     * @todo remove name
     *
     */
    protected AuthenticationEntity(final Operation operation, final Identity identity, final String method)
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
    public String method()
        {
        return this.method ;
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
    @Override
    public Operation operation()
        {
        return this.operation;
        }
    }
