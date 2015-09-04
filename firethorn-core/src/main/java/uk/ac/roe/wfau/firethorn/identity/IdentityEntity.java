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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.community.CommunityEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
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

    /**
     * Factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<Identity>
    implements Identity.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return IdentityEntity.class;
            }

        @Override
        @CreateMethod
        public Identity create(final Community community, final String name)
            {
            log.debug("create(Community, String) [{}][{}]", community.uri(), name);
            final Identity member = this.select(
                community,
                name
                );
            if (member != null)
                {
                return member ;
                }
            else {
                return super.insert(
                    new IdentityEntity(
                        community,
                        name
                        )
                    );
                }
            }

        @Override
        @SelectMethod
        public Identity select(final Community community, final String name)
            {
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
     * Create a new IdentityEntity, setting the owner to null.
     *
     */
    protected IdentityEntity(final Community community, final String name)
        {
        super(name);
        this.community = community;
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
        if (super.owner() != null)
            {
            return super.owner();
            }
        else
            {
            return this;
            }
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
        {
        log.debug("adqlschema()");
        if (this.adqlschema == null)
        	{
        	if (this.adqlresource == null)
        		{
// TODO better name generator.
        		this.adqlresource = factories().adql().resources().create(
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
		{
		return new Spaces()
			{
			@Override
			public AdqlSpaces adql()
				{
				return new AdqlSpaces()
					{
					@Override
					public Iterable<AdqlSchema> select()
						{
						// TODO .. 
						return new EmptyIterable<AdqlSchema>();
						}
					@Override
					public AdqlSchema current()
						{
						return adqlschema();
						}
					};
				}

			@Override
			public JdbcSpaces jdbc()
				{
				return new JdbcSpaces()
					{
					@Override
					public Iterable<JdbcSchema> select()
						{
						// TODO .. 
						return new EmptyIterable<JdbcSchema>();
						}

					@Override
					public JdbcSchema current()
						{
						return jdbcschema();
						}
					};
				}
			};
		}
    }

