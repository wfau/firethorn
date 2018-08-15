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

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory.FactoryAllowCreateProtector;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;
import uk.ac.roe.wfau.firethorn.util.GenericIterable;

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
    protected static final String DB_HTTP_URL_COL    = "http_url" ;
    protected static final String DB_HTTP_METHOD_COL = "http_method" ;
    protected static final String DB_SOURCE_ADDR_COL = "source_addr" ;
    protected static final String DB_SERVER_PORT_COL = "server_port" ;
    protected static final String DB_AUTH_METHOD_COL = "auth_method" ;

    @Component
    public static class EntityFactory
    extends AbstractEntityFactory<Operation>
    implements Operation.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAllowCreateProtector();
            }
        
        @Override
        public Class<?> etype()
            {
            return OperationEntity.class ;
            }

        @Override
        @CreateMethod
        public Operation create(final String path, final String method, final String source, final int port)
            {
            return current(
                this.insert(
                    new OperationEntity(
                        path,
                        method,
                        source,
                        port
                        )
                    )
                );
            }

        /*
         * A {@link ThreadLocal} store for the current {@link Operation}.
         *
         */
        private final ThreadLocal<Operation> local = new ThreadLocal<Operation>();

        @Override
        public Operation current()
            {
            return local.get();
            }

        @Override
        public Operation current(final Operation oper)
            {
            local.set(
                oper
                );
            return local.get();
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements Operation.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static OperationEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return OperationEntity.EntityServices.instance ;
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
            if (OperationEntity.EntityServices.instance == null)
                {
                OperationEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private Operation.IdentFactory idents;
        @Override
        public Operation.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private Operation.LinkFactory links;
        @Override
        public Operation.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Operation.EntityFactory entities;
        @Override
        public Operation.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected Operation.EntityFactory factory()
        {
        return OperationEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Operation.EntityServices services()
        {
        return OperationEntity.EntityServices.instance() ; 
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
    protected OperationEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected OperationEntity(final String path, final String method, final String source, final int port)
        {
        super(true);
	    // Question - how can Operation get an owner, when we haven't handled the authentication yet. 
        // Because the ThreadLocal Operation does not get cleared when a Thread from a ThreadPool is re-used ? 
        this.port = port ;
        this.url = path ;
        this.method = method ;
        this.source = source ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_HTTP_URL_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private String url ;
    @Override
    public String target()
        {
        return this.url ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_HTTP_METHOD_COL,
        unique = false,
        nullable = true,
        updatable = false
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
        name = DB_SOURCE_ADDR_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private String source ;
    @Override
    public String source ()
        {
        return this.source ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_SERVER_PORT_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private Integer port ;
    @Override
    public Integer port()
        {
        return this.port;
        }

    /**
     * The primary Authentication for this Operation.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AuthMethodEntity.class
        )
    @JoinColumn(
        name = DB_AUTH_METHOD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AuthMethod primary ;

    private AuthMethod primary()
        {
        return this.primary;
        }

    private void primary(final AuthMethod auth)
        {
        log.debug("primary(Authentication)");
        log.debug("  Authentication [{}]", auth);
        log.debug("  Identity [{}]", auth.identity());
        this.primary = auth ;
        this.owner(
            auth.identity()
            ) ;
        }

    /**
     * The set of Authentications for this Operation.
     *
     */
    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "operation",
        targetEntity = AuthMethodEntity.class
        )
    private final List<AuthMethod> authmethods = new ArrayList<AuthMethod>();

    /**
     * Create a new Authentication for this Operation.
     *
     */
    private AuthMethod authenticate(final Identity identity, final String method)
        {
        log.debug("authenticate() [{}][{}][{}][{}]", this.ident(), identity.ident(), identity.name(), method);
        final AuthMethod auth = factories().authentication().entities().create(
            this,
            identity,
            method
            );
        authmethods.add(
            auth
            );
        primary(
            auth
            );
        return auth;
        }

    @Override
    public Authentications authentications()
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
                return new GenericIterable<Authentication, AuthMethod>(
                    OperationEntity.this.authmethods
                    );
                }

            @Override
            public Authentication create(final Identity identity, final String method)
                {
                return OperationEntity.this.authenticate(
                    identity,
                    method
                    );
                }
            };
        }

	@Override
	public Identities identities()
		{
		return new Identities()
			{
			@Override
			public Identity primary()
				{
				final AuthMethod auth = OperationEntity.this.primary();
				if (auth != null)
					{
					return auth.identity();
					}
				else {
					return null;
					}
				}
			};
		}
    
    /**
     * Get the corresponding Hibernate entity for the current thread.
     * @throws HibernateConvertException 
     * @todo Move to a generic base class. 
     *
     */
    @Override
    public Operation rebase()
    throws HibernateConvertException
    	{
        log.trace("rebase [{}]", ident());
        try {
			return services().entities().select(
			    ident()
			    );
        	}
        catch (final IdentifierNotFoundException ouch)
        	{
        	log.error("IdentifierNotFound selecting instance [{}][{}]", this.getClass().getName(), ident());
        	throw new HibernateConvertException(
    			ident(),
    			ouch
    			);
        	}
        catch (final ProtectionException ouch)
            {
            log.error("ProtectionException [{}][{}]", this.ident(), ouch.getMessage());
        	throw new HibernateConvertException(
    			ident(),
    			ouch
    			);
        	}
        }

    @Embedded
    private MonkeyParamEntity monkey;
    
    @Override
    public MonkeyParamEntity monkey()
        {
        return this.monkey;
        }

    @Override
    public void monkey(final String name, final Object data)
        {
        this.monkey = new MonkeyParamEntity(
            name,
            data
            );
        }
    }
