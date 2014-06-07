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

import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;

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
    name = OperationEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public class OperationEntity
extends AbstractEntity
implements Operation
    {

    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "OperationEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_TARGET_COL  = "target"  ;
    protected static final String DB_METHOD_COL  = "method"  ;
    protected static final String DB_SOURCE_COL  = "source"  ;
    protected static final String DB_AUTH_COL    = "auth" ;

    @Component
    public static class EntityFactory
    extends AbstractEntityFactory<Operation>
    implements Operation.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return OperationEntity.class ;
            }

        @Override
        @CreateMethod
        public Operation create(final String target, final String method, final String source)
            {
            return current(
                this.insert(
                    new OperationEntity(
                        target,
                        method,
                        source
                        )
                    )
                );
            }

        @Autowired
        protected Operation.LinkFactory links;
        @Override
        public Operation.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected Operation.IdentFactory idents;
        @Override
        public Operation.IdentFactory idents()
            {
            return this.idents;
            }

        /*
         * ThreadLocal Operation.
         *
         */
        private final ThreadLocal<Operation> local = new ThreadLocal<Operation>();

        @Override
        public Operation current()
            {
            return local.get();
            }

        private Operation current(final Operation oper)
            {
// Error if one replaces another ?            
            local.set(
                oper
                );
            return oper;
            }
        }

    /**
     * Protected constructor.
     *
     */
    protected OperationEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     * @todo remove name
     *
     */
    protected OperationEntity(final String target, final String method, final String source)
        {
        super(true);
        this.target = target ;
        this.method = method ;
        this.source = source ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_TARGET_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String target ;
    @Override
    public String target()
        {
        return this.target ;
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

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SOURCE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String source ;
    @Override
    public String source ()
        {
        return this.source ;
        }

    /**
     * The primary Authentication for this Operation.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AuthenticationEntity.class
        )
    @JoinColumn(
        name = DB_AUTH_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Authentication primary ;

    private Authentication primary()
        {
        return this.primary;
        }

    private void primary(final Authentication auth)
        {
        if (this.primary == null)
            {
            this.primary = auth ;
            this.owner(
                auth.identity()
                ) ;
            }
        }

    /**
     * The set of Authentications for this Operation.
     *
     */
    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "operation",
        targetEntity = AuthenticationEntity.class
        )
    private final List<Authentication> authentications = new ArrayList<Authentication>();

    /**
     * Create a new Authentication for this Operation.
     *
     */
    private Authentication create(final Identity identity, final String method)
        {
        log.debug("create(Identity, String)");
        log.debug("  Identity [{}]", identity.name());
        log.debug("  Method   [{}]", method);
        final Authentication auth = factories().authentications().create(
            this,
            identity,
            method
            );
        authentications.add(
            auth
            );
        primary(
            auth
            );
        return auth;
        }

    @Override
    public Authentications auth()
        {
        return new Authentications()
            {
            @Override
            public Authentication primary()
                {
                return OperationEntity.this.primary();
                }

            @Override
            public Iterable<Authentication> select()
                {
                return authentications;
                }

            @Override
            public Authentication create(final Identity identity, final String method)
                {
                return OperationEntity.this.create(
                    identity,
                    method
                    );
                }
            };
        }

    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
