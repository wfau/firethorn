/*
 * Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.roe.wfau.firethorn.identity;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionError;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.community.CommunityEntity;
import uk.ac.roe.wfau.firethorn.community.UnauthorizedException;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.UniqueNamefactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResourceEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchemaEntity;
import uk.ac.roe.wfau.firethorn.util.EmptyIterable;

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
    name = IdentityEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList=IdentityEntity.DB_COMMUNITY_COL
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name = "Identity-select-all",
            query = "FROM IdentityEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name = "Identity-select-community.name",
            query = "FROM IdentityEntity WHERE community = :community AND name = :name ORDER BY ident desc"
            ),
        @NamedQuery(
            name = "Identity-select-name",
            query = "FROM IdentityEntity WHERE name = :name ORDER BY ident desc"
            )
        }
    )
public class IdentityEntity
extends AbstractNamedEntity
implements Identity
    {

    /**
     * Hibernate table mapping.
     *
     */
    public static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "IdentityEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_COMMUNITY_COL = "community" ;
    protected static final String DB_JDBC_SCHEMA_COL = "jdbcschema" ;

    protected static final String DB_ADQL_SCHEMA_COL = "adqlschema" ;
    protected static final String DB_ADQL_RESOURCE_COL = "adqlresource" ;

    protected static final String DB_PASSHASH_COL = "passhash" ;

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<Identity>
    implements Identity.EntityFactory
        {
        /**
         * The 'system' {@link Identity) name.
         * 
         */
        @Value("${firethorn.admin.user.name:admin}")
        protected String ADMIN_IDENTITY_NAME ;

        /**
         * The 'system' {@link Identity) password.
         * 
         */
        @Value("${firethorn.admin.user.pass:admin}")
        protected String ADMIN_IDENTITY_PASS ;

        @Override
        @CreateMethod
        public synchronized Identity admin()
            {
            log.debug("admin()");

            final Identity found = this.search(
                ADMIN_IDENTITY_NAME
                );
            if (found != null)
                {
                log.debug("  found [{}]", found);
                return found ;
                }
            else {
                try {
                    final Community admins = factories().communities().entities().admins();
                    final Identity  created = super.insert(
                        new IdentityEntity(
                            admins,
                            ADMIN_IDENTITY_NAME,
                            ADMIN_IDENTITY_PASS
                            )
                        );
                    log.debug("  created [{}]", created);
                    return created ;
                    }
                catch (final ProtectionException ouch)
                    {
                    log.error("ProtectionException loading admin community");
                    throw new ProtectionError(
                        "ProtectionException loading admin community",
                        ouch
                        );
                    }
                }
            }
        
        @Override
        public Class<?> etype()
            {
            return IdentityEntity.class;
            }

        @Override
        @CreateMethod
        public Identity create(final Community community)
            {
            log.debug("create(Community) [{}]", community.name());
            return super.insert(
                new IdentityEntity(
                    community,
                    null,
                    null
                    )
                );
            }
        
        @Override
        @CreateMethod
        public Identity create(final Community community, final String name)
        throws DuplicateEntityException
            {
            log.debug("create(Community, String) [{}][{}]", community.name(), name);
            return create(
                community,
                name,
                null
                );
            }

        @Override
        @CreateMethod
        public Identity create(final Community community, final String name, final String pass)
        throws DuplicateEntityException
            {
            log.debug("create(Community, String, String) [{}][{}]", community.name(), name);
            final Identity found = search(
                community,
                name
                );
            if (found == null)
                {
                return super.insert(
                    new IdentityEntity(
                        community,
                        name,
                        pass
                        )
                    );
                }
            else {
                log.error("Duplicate Identity [{}][{}]", community.name(), name);
                throw new DuplicateEntityException(
                    found
                    );
                }
            }

        @Override
        @SelectMethod
        public Identity select(final Community community, final String name)
        throws NameNotFoundException
            {
            log.debug("select(Community, String) [{}][{}]", community.name(), name);
            final Identity found = search(
                community,
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

        @Override
        public Identity search(Community community, String name)
            {
            log.debug("search(Community, String) [{}][{}]", community.name(), name);
            return super.first(
                super.query(
                    "Identity-select-community.name"
                    ).setEntity(
                        "community",
                        community
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        
        @Override
        @SelectMethod
        public Identity select(final String name)
        throws NameNotFoundException
            {
            log.debug("select(String) [{}][{}]", name);
            final Identity found = search(
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

        @Override
        @SelectMethod
        public Identity search(String name)
            {
            log.debug("search(String) [{}][{}]", name);
            return super.first(
                super.query(
                    "Identity-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @CreateMethod
        public Identity search(final Community community, final String name, boolean create)
            {
            log.debug("select(Community, String, boolean) [{}][{}]", community.name(), name, create);

            final Identity found = search(
                community,
                name
                );
            if (found != null)
                {
                log.debug("Found matching Identity [{}][{}]", found.ident(), found.name());
                return found;
                }
            else {
                log.debug("Identity not found [{}]", name);
                if (create)
                    {
                    log.debug("Creating new Identity [{}]", name);
                    return super.insert(
                        new IdentityEntity(
                            community,
                            name,
                            null
                            )
                        );
                    }
                else {
                    log.debug("Null Identity");
                    return null ;
                    }
                }
            }

        @Override
        @CreateMethod
        public Identity login(final Community community, String name, String pass)
		throws UnauthorizedException
            {
            log.debug("login(Community, String, String) [{}][{}]", community.name(), name);

            log.debug("Checking for identity [{}][{}]", community.name(), name);
            final Identity found = super.first(
                super.query(
                    "Identity-select-community.name"
                    ).setEntity(
                        "community",
                        community
                    ).setString(
                        "name",
                        name
                        )
                );

            if (found != null)
            	{
                log.debug("Identity found [{}][{}]", community.name(), name);
            	found.login(name, pass);
            	return found;
                }
            else {
                log.debug("Identity not found [{}][{}]", community.name(), name);
            	if (community.autocreate())
	            	{
	                log.debug("Auto-create new identity [{}][{}]", community.name(), name);
	            	final Identity created = super.insert(
	                    new IdentityEntity(
	                        community,
	                        name,
	                        pass
	                        )
	                    );
	            	created.login(name, pass);
	            	return created;
	            	}
            	else {
                    log.warn("FAIL : Identity not found [{}][{}]", community.name(), name);
            		throw new UnauthorizedException();
            		}
                }
            }
        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements Identity.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static IdentityEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return IdentityEntity.EntityServices.instance ;
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
            if (IdentityEntity.EntityServices.instance == null)
                {
                IdentityEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private Identity.IdentFactory idents;
        @Override
        public Identity.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private Identity.LinkFactory links;
        @Override
        public Identity.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private Identity.NameFactory names;
        @Override
        public Identity.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private Identity.EntityFactory entities;
        @Override
        public Identity.EntityFactory entities()
            {
            return this.entities;
            }
        }

    @Override
    protected Identity.EntityFactory factory()
        {
        log.debug("factory()");
        return IdentityEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Identity.EntityServices services()
        {
        log.debug("services()");
        return IdentityEntity.EntityServices.instance() ; 
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
     * Protected constructor.
     *
     */
    protected IdentityEntity(final Community community, final String name, final String pass)
        {
        super(name);
        this.owner(this) ;
    	this.passhash  = this.hashpass(pass);
        this.community = community;
        }

    /**
     * Static name factory.
     * 
     */
    static final UniqueNamefactory names = new UniqueNamefactory(
		"anon",
		"-"
		);

    @Override
    protected void init(final String name)
    	{
        if (name != null)
            {
            this.name = name;
            }
        else {
            this.name = names.name(
        		this
        		);
        	}
    	}

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }
    
    /**
     * Return this Identity as the owner.
     *
     */
    @Override
    public Identity owner()
        {
        return this;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = CommunityEntity.class
        )
    @JoinColumn(
        name = DB_COMMUNITY_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Community community ;
    @Override
    public Community community()
        {
        return this.community ;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlResourceEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_RESOURCE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AdqlResource adqlresource ;
    
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_SCHEMA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private AdqlSchema adqlschema ;

    protected AdqlSchema adqlschema()
    throws ProtectionException
        {
        log.debug("adqlschema()");
        if (this.adqlschema == null)
        	{
        	if (this.adqlresource == null)
        		{
// TODO better name generator.
        		this.adqlresource = factories().adql().resources().entities().create(
        			"user space"
        			);        	
        		}
// TODO better name generator.
        	this.adqlschema = adqlresource.schemas().create(
    			"temp"
    			);
        	}
        return this.adqlschema;
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_JDBC_SCHEMA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcSchema jdbcschema ;

    protected JdbcSchema jdbcschema()
    throws ProtectionException
        {
        log.debug("jdbcschema()");
        if (this.jdbcschema == null)
            {
            if (community() != null)
                {
                if (community().space(true) != null)
                    {
/*
 * Create a separate schema for this member.
 * Requires CREATE SCHEMA
 * Depends on replacing Liquibase
                    this.jdbcschema = community().space().schemas().create(
                        this
                        );
 */
                    try {
                        this.jdbcschema = community().space().schemas().simple();
                        }
                    catch (final EntityNotFoundException ouch)
                        {
                        log.error("Failed to find user space []", ouch.getMessage());
                        }
                    }
                }
            }
        return this.jdbcschema;
        }

	@Override
	public Spaces spaces()
    throws ProtectionException
		{
		return new Spaces()
			{
			@Override
			public AdqlSpaces adql()
	        throws ProtectionException
				{
				return new AdqlSpaces()
					{
					@Override
					public Iterable<AdqlSchema> select()
                    throws ProtectionException
						{
						// TODO .. 
						return new EmptyIterable<AdqlSchema>();
						}
					@Override
					public AdqlSchema current()
                    throws ProtectionException
						{
						return adqlschema();
						}
					};
				}

			@Override
			public JdbcSpaces jdbc()
	        throws ProtectionException
				{
				return new JdbcSpaces()
					{
					@Override
					public Iterable<JdbcSchema> select()
                    throws ProtectionException
						{
						// TODO .. 
						return new EmptyIterable<JdbcSchema>();
						}

					@Override
					public JdbcSchema current()
			        throws ProtectionException
						{
						return jdbcschema();
						}
					};
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
    public Identity rebase()
    throws HibernateConvertException
    	{
        log.debug("Converting current instance [{}]", ident());
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
            log.error("ProtectionException selecting instance [{}][{}]", this.getClass().getName(), ident());
            throw new HibernateConvertException(
                ident(),
                ouch
                );
            }
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_PASSHASH_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String passhash = null ;

    /*
     * 
            
    https://crackstation.net/hashing-security.htm
    https://github.com/defuse/password-hashing
    https://github.com/jedisct1/libsodium
    https://download.libsodium.org/doc/
    https://www.gitbook.com/book/jedisct1/libsodium/details
    https://download.libsodium.org/doc/bindings_for_other_languages/
    https://github.com/joshjdevl/libsodium-jni/blob/master/example/Sodium/app/src/main/java/android/alex/com/sodium/MainActivity.java
    https://github.com/naphaso/jsodium/tree/master/native
    https://download.libsodium.org/doc/bindings_for_other_languages/index.html

    https://github.com/abstractj/kalium/blob/master/src/main/java/org/abstractj/kalium/crypto/Password.java
    https://github.com/abstractj/kalium/blob/master/src/test/java/org/abstractj/kalium/crypto/PasswordTest.java

     * 
     */
    
    protected void passhash(final String oldpass, final String newpass)
    throws UnauthorizedException
	    {
    	log.debug("passhash(String, String)");
    	log.debug("  Identity [{}][{}]", this.ident(), this.name());
    	log.debug("  Password [{}][{}]", oldpass, newpass);

    	if (this.passhash != null)
    		{
	    	if (passtest(oldpass))
			    {
		    	this.passhash = hashpass(newpass);
			    }
		    else {
	    		throw new UnauthorizedException();
			    }
		    }
	    }

    private boolean nametest(final String name)
    throws UnauthorizedException
        {
        log.debug("nametest(String)");
        log.debug("  Identity [{}][{}]", this.ident(), this.name());
        log.debug("  Username [{}]", name);

        if (name != null)
            {
            if (this.name().equals(name))
                {
                return true ;
                }
            else {
                log.debug("FAIL - wrong username");;
                throw new UnauthorizedException();
                }
            }
        else {
            log.debug("FAIL - null username");;
            throw new UnauthorizedException();
            }
        }

    private boolean passtest(final String pass)
    throws UnauthorizedException
    	{
    	log.debug("passtest(String)");
    	log.debug("  Identity [{}][{}]", this.ident(), this.name());
        log.debug("  Password [{}]", pass);
        log.debug("  Passhash [{}]", this.passhash);
    	if ((null == this.passhash) && (null == pass))
    		{
    		log.debug("PASS - no password");
    		return true ;
    		}
    	else if (null == pass)
    		{
    		log.debug("FAIL - null password");;
    		throw new UnauthorizedException();
    		}
        else if (null == this.passhash)
            {
            log.debug("FAIL - null passhash");;
            throw new UnauthorizedException();
            }
    	else if (this.passhash.equals(hashpass(pass)))	
			{
       		log.debug("PASS - password match");;
    		return true ;
			}
		else {
    		log.debug("FAIL - wrong password");;
    		throw new UnauthorizedException();
			}
    	}

    protected String hashpass(final String pass)
    	{
    	if (pass == null)
    	    {
    	    return null ;
    	    }
    	else {
    	    return "abc-" + pass + "-xyz";
    	    }
    	}

    @Override
    public void login(final String name, final String pass)
	throws UnauthorizedException
        {
        log.debug("login(String, String)");
        log.debug("  Identity [{}][{}]", this.ident(), this.name());

        nametest(name);
        passtest(pass);

        }
    }

