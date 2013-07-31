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

import java.util.UUID;

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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
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
            name = "Comunity-select-uri",
            query = "FROM CommunityEntity WHERE uri = :uri ORDER BY ident desc"
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
    protected static final String DB_URI_COL   = "uri" ;
    protected static final String DB_SPACE_COL = "space" ;

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
        @CreateEntityMethod
        public Community create(final String uri)
            {
            return create(
                uri,
                uri
                );
            }

        @Override
        @CreateEntityMethod
        public Community create(final String name, final String uri)
            {
            log.debug("create(String, String) [{}][{}]", name, uri);
            return create(
                factories().jdbc().resources().userdata(),
                name,
                uri
                );
            }

        @Override
        @CreateEntityMethod
        public Community create(final JdbcResource space, final String name, final String uri)
            {
            log.debug("create(JdbcResource, String, String) [{}][{}][{}]", space, name, uri);
            final Community community = this.select(
                uri
                );
            if (community != null)
                {
                return community ;
                }
            else {
                return super.insert(
                    new CommunityEntity(
                        space,
                        name,
                        uri
                        )
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public Community select(final String uri)
            {
            log.debug("select(String) [{}]", uri);
            return super.first(
                super.query(
                    "Comunity-select-uri"
                    ).setString(
                        "uri",
                        uri
                    )
                );
            }

        @Autowired
        protected Community.IdentFactory idents;
        @Override
        public Community.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected Community.LinkFactory links;
        @Override
        public Community.LinkFactory links()
            {
            return this.links;
            }
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
    protected CommunityEntity(final JdbcResource space, final String name, final String uri)
        {
        super(
            name
            );
        log.debug("CommunityEntity(JdbcResource, String, String) [{}][{}]", uri, name);
        this.space = space ;
        this.uri = uri ;
        }

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

    @Override
    public Identities identities()
        {
        return new Identities()
            {
            @Override
            public Identity create(final String name)
                {
                return factories().identities().create(
                    CommunityEntity.this,
                    name
                    );
                }

            @Override
            public Identity select(final String name)
                {
                return factories().identities().select(
                    CommunityEntity.this,
                    name
                    );
                }
            };
        }

    @Override
    public String link()
        {
        return factories().communities().links().link(
            this
            );
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
        if ((create) && (this.space == null))
            {
            this.space = factories().jdbc().resources().userdata() ;
            }
        return this.space;
        }
    }
