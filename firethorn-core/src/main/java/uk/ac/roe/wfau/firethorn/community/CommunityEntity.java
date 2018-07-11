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
package uk.ac.roe.wfau.firethorn.community;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.access.ProtectionError;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResourceEntity;

/**
 * Hibernate based entity implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = CommunityEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name = "Comunity-select-all",
            query = "FROM CommunityEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name = "Comunity-select-name",
            query = "FROM CommunityEntity WHERE name = :name ORDER BY ident desc"
            )
        }
    )
public class CommunityEntity
extends AbstractNamedEntity
implements Community
    {
    /**
     * Hibernate table mapping.
     *
     */
    public static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "CommunityEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_URI_COL    = "uri" ;
    protected static final String DB_SPACE_COL  = "space" ;
    protected static final String DB_AUTOCREATE_COL = "autocreate" ;
    protected static final String DB_USERCREATE_COL = "usercreate" ;
    
    /**
     * EntityFactory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<Community>
    implements Community.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }
        
        /**
         * Private search method that doesn't check the {@link Protector}.
         * 
         */
        private Community find(final String name)
            {
            return super.first(
                super.query(
                    "Comunity-select-name"
                    ).setString(
                        "name",
                        name
                    )
                );
            }
        /**
         * Private create method that doesn't check the {@link Protector}.
         * 
         */
        private Community make(final String name, final JdbcResource space)
            {
            return make(
                name,
                space,
                false,
                false
                );
            }
        /**
         * Private create method that doesn't check the {@link Protector}.
         * 
         */
        private Community make(final String name, final JdbcResource space, boolean autocreate, boolean usercreate)
            {
            return super.insert(
                new CommunityEntity(
                    name,
                    space,
                    autocreate,
                    usercreate
                    )
                );
            }

        /**
         * The 'system' {@link Community) name.
         * 
         */
        @Value("${firethorn.admin.community:admin}")
        protected String adminname ;

        @Override
        @CreateMethod
        public synchronized Community admins()
        throws ProtectionException
            {
            log.trace("admins()");
            final Community found = find(
                adminname
                );
            if (found != null)
                {
                log.trace("  found [{}][{}]", found.ident(), found.name());
                return found;
                }
            else {
                try {
                    final Community created = make(
                        adminname,
                        factories().jdbc().resources().entities().userdata(),
                        false,
                        false
                        );
                    log.trace("  created [{}][{}]", created.ident(), created.name());
                    return created ;
                    }
                // Only needed because finding the default space may throw an exception. 
                catch (final ProtectionException ouch)
                    {
                    log.error("ProtectionException creating admin community");
                    throw new ProtectionError(
                        "ProtectionException creating admin community",
                        ouch
                        );
                    }
                }
            }

        /**
         * The 'guest' {@link Community) name.
         * 
         */
        @Value("${firethorn.guest.community:guests}")
        protected String guestname ;

        @Override
        @CreateMethod
        public synchronized Community guests()
        throws ProtectionException
            {
            log.trace("guests()");
            final Community found = find(
                guestname
                );
            if (found != null)
                {
                log.trace("  found [{}][{}]", found.ident(), found.name());
                return found;
                }
            else {
                try {
                    final Community created = make(
                        guestname,
                        factories().jdbc().resources().entities().userdata(),
                        true,
                        true
                        );
                    log.trace("  created [{}][{}]", created.ident(), created.name());
                    return created ;
                    }
                // Only needed because finding the default space may throw an exception. 
                catch (final ProtectionException ouch)
                    {
                    log.error("ProtectionException creating guest community");
                    throw new ProtectionError(
                        "ProtectionException creating guest community",
                        ouch
                        );
                    }
                }
            }
        
        @Override
        public Class<?> etype()
            {
            return CommunityEntity.class;
            }

        @Override
        @CreateMethod
        public Community create(final String name)
        throws DuplicateEntityException, ProtectionException
            {
            protector().affirm(Action.create);
            return create(
                name,
                factories().jdbc().resources().entities().userdata()
                );
            }

        @Override
        @CreateMethod
        public Community create(final String name, final JdbcResource space)
        throws DuplicateEntityException, ProtectionException
            {
            log.debug("create() [{}][{}][{}]", name, space.ident(), space.name());
            protector().affirm(Action.create);
            final Community found = find(
                name
                );
            if (found == null)
                {
                log.debug("Creating new community [{}]", name);
                return make(
                    name,
                    space
                    );
                }
            else {
                log.error("Duplicate community [{}][{}]", name, found.ident());
                throw new DuplicateEntityException(found);
                }
            }
        
        @Override
        @SelectMethod
        public Community select(final String name)
        throws NameNotFoundException, ProtectionException
            {
            protector().affirm(Action.select);
            final Community found = find(
                name
                );
            if (found != null)
                {
                return found;
                }
            else {
                throw new NameNotFoundException(
                    name
                    ); 
                }
            }

        @SelectMethod
        public Community search(final String name) throws ProtectionException
            {
            protector().affirm(Action.select);
            return find(
                name
                );
            }

        @Override
        @CreateMethod
        public Identity login(final String comm, final String name, final String pass)
        throws UnauthorizedException, ProtectionException
            {
            log.debug("login() [{}][{}]", name, ((pass != null) ? "####" : null));
            //
            // Load the guest Community for comparison.
            final Community guests = factories().communities().entities().guests();
            log.trace("Guest Community [{}][{}]", guests.ident(), guests.name());
            //
            // Load the system Identity for comparison.
            final Identity system = factories().identities().entities().admin();
            log.trace("System Identity  [{}][{}]", system.ident(), system.name());
            log.trace("System Community [{}][{}]", system.community().ident(), system.community().name());

            //
            // Check the Community.
            Community community;
            if (comm != null)
                {
                community = find(
                    comm
                    );
                }
            else {
                community = guests;
                }

            if (community == null)
                {
                log.warn("Login failed - unknown community [{}]", comm);
                throw new UnauthorizedException();
                }
            if (name == null)
                {
                log.warn("Login failed - null username");
                throw new UnauthorizedException();
                }

            return community.login(
                name,
                pass
                );
            }
        }
    
    /**
     * {@link AbstractThing.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements Community.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return CommunityEntity.EntityServices.instance ;
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
            if (CommunityEntity.EntityServices.instance == null)
                {
                CommunityEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private Community.IdentFactory idents;
        @Override
        public Community.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private Community.LinkFactory links;
        @Override
        public Community.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Community.NameFactory names;
        @Override
        public Community.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private Community.EntityFactory entities;
        @Override
        public Community.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
        protected Identity.EntityFactory identities;
        @Override
        public Identity.EntityFactory identities()
            {
            return identities;
            }
        }

    @Override
    protected Community.EntityFactory factory()
        {
        return CommunityEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Community.EntityServices services()
        {
        return CommunityEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected CommunityEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected CommunityEntity(final String name, final JdbcResource space)
        {
    	this(
			name,
			space,
			false,
			false
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected CommunityEntity(final String name, final JdbcResource space, boolean autocreate, boolean usercreate)
        {
        super(
            name
            );
        log.trace("CommunityEntity() [{}][{}][{}][{}]",
            name,
            space,
            autocreate,
            usercreate
            );
        this.space = space ;
        this.autocreate = autocreate;
        this.usercreate = usercreate;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_AUTOCREATE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private Boolean autocreate = false;
    @Override
    public Boolean autocreate()
        {
        return this.autocreate;
        }
    @Override
    public void autocreate(final Boolean value)
        {
        this.autocreate = value;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_USERCREATE_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private Boolean usercreate = false;
    @Override
    public Boolean usercreate()
        {
        return this.usercreate;
        }
    @Override
    public void usercreate(final Boolean value)
        {
        this.usercreate = value;
        }
    
    @Override
    public Identity login(final String name, final String pass)
    throws UnauthorizedException, ProtectionException
        {
        log.debug("login() [{}][{}]", name, ((pass != null) ? "####" : null));
        return members().login(name, pass);
        }
    
    @Override
    public Members members()
        {
        return new Members()
            {
            @Override
            public Identity create()
            throws ProtectionException
                {
                return services().identities().create(
                    CommunityEntity.this
                    );
                }

            @Override
            public Identity create(final String name)
            throws ProtectionException, DuplicateEntityException
                {
                return services().identities().create(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity create(final String name, final String pass)
            throws ProtectionException, DuplicateEntityException
                {
                return services().identities().create(
                    CommunityEntity.this,
                    name,
                    pass
                    );
                }

            @Override
            public Identity select(final String name)
            throws ProtectionException, NameNotFoundException
                {
                return services().identities().select(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity search(String name, boolean create)
            throws ProtectionException
                {
                return services().identities().search(
                    CommunityEntity.this,
                    name,
                    create
                    );
                }

            @Override
            public Identity search(String name)
            throws ProtectionException
                {
                return services().identities().search(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity login(String name, String pass)
            throws ProtectionException, UnauthorizedException
                {
                log.debug("login() [{}][{}]", name, ((pass != null) ? "####" : null));
            	return services().identities().login(
                    CommunityEntity.this,
                    name,
                    pass
                    );
                }
            };
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_SPACE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcResource space  ;
    @Override
    public JdbcResource space()
        {
        return this.space;
        }
    @Override
    public JdbcResource space(final boolean create)
    throws ProtectionException
        {
        log.debug("space(boolean) [{}]", create);
        if ((create) && (this.space == null))
            {
            this.space = factories().jdbc().resources().entities().userdata() ;
            }
        return this.space;
        }
    }
