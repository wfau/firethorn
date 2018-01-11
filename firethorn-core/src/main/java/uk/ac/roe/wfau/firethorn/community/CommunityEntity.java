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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
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
        public Class<?> etype()
            {
            return CommunityEntity.class;
            }

        @Override
        @CreateMethod
        public Community create(final String name)
            {
            log.debug("create(String) [{}]", name);
            return create(
                name,
                factories().jdbc().resources().entities().userdata()
                );
            }

        @Override
        @CreateMethod
        public Community create(final String name, final JdbcResource space)
            {
            log.debug("create(String, JdbcResource) [{}][{}][{}]", name, space);
            final Community found = this.search(
                name
                );
            if (found != null)
                {
                log.debug("Found existing community [{}][{}]", name, found.ident());
                return found ;
                }
            else {
                log.debug("Creating new community [{}]", name);
                return super.insert(
                    new CommunityEntity(
                        name,
                        space
                        )
                    );
                }
            }

        @Override
        @SelectMethod
        public Community select(final String name)
        throws EntityNotFoundException
            {
            log.debug("select(String) [{}]", name);
            final Community found = search(name);
            if (found != null)
                {
                return found;
                }
            else {
                throw new EntityNotFoundException(); 
                }
            }

        @SelectMethod
        public Community search(final String name)
            {
            log.debug("search (String) [{}]", name);
            return super.first(
                super.query(
                    "Comunity-select-name"
                    ).setString(
                        "name",
                        name
                    )
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
            log.debug("init()");
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
        log.debug("factory()");
        return CommunityEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected Community.EntityServices services()
        {
        log.debug("services()");
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
     * Create a new Community.
     *
     */
    protected CommunityEntity(final String name, final JdbcResource space)
        {
        super(
            name
            );
        log.debug("CommunityEntity(String, JdbcResource) [{}][{}]", name, space);
        this.space = space ;
        }

    /*
     * 
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_URI_COL,
        unique = true,
        nullable = false,
        updatable = false
        )
    private String uri;
    @Override
    public String uri()
        {
        return this.uri;
        }
     * 
     */
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_AUTOCREATE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Boolean autocreate = false;
    public Boolean autocreate()
        {
        return this.autocreate;
        }

    @Override
    public Identity login(final String name, final String pass)
    throws UnauthorizedException
        {
        final Identity found = members().search(name);
        if (found != null)
            {
            return found ;
            }
        else if (autocreate)
            {
            return members().create(name);
            }
        else {
            throw new UnauthorizedException();
            }
        }
    
    @Override
    public Members members()
        {
        return new Members()
            {
            @Override
            public Identity create(final String name)
                {
                return services().identities().create(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity select(final String name)
                {
                return services().identities().select(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity search(String name)
                {
                // TODO Auto-generated method stub
                return null;
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
        {
        log.debug("space(boolean) [{}]", create);
        if ((create) && (this.space == null))
            {
            this.space = factories().jdbc().resources().entities().userdata() ;
            }
        return this.space;
        }
    }
