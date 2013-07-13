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

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchemaEntity;

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
    name = IdentityEntity.DB_TABLE_NAME
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
    public static final String DB_TABLE_NAME = "IdentityEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_COMMUNITY_COL = "community" ;
    protected static final String DB_CURRENT_SCHEMA_COL = "currentschema" ;

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
        public Identity current()
            {
            final Operation oper = factories().operations().current() ;
            if (oper != null)
                {
                if (oper.authentications().primary() != null)
                    {
                    return oper.authentications().primary().identity();
                    }
                }
            return null ;
            }

        @Override
        @CreateEntityMethod
        public Identity create(final Community community, final String name)
            {
            log.debug("create(Community, String) [{}][{}]", community.uri(), name);
            final Identity identity = this.select(
                community,
                name
                );
            if (identity != null)
                {
                return identity ;
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
        @SelectEntityMethod
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

        @Autowired
        protected Identity.IdentFactory idents;
        @Override
        public Identity.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected Identity.LinkFactory links;
        @Override
        public Identity.LinkFactory links()
            {
            return this.links;
            }

        @Override
        public Identity select(UUID uuid) throws NotFoundException
            {
            // TODO Auto-generated method stub
            return null;
            }
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

    /**
     * Return this Identity as the owner.
     *
     */
    @Override
    public Identity owner()
        {
        if (super.owner() == null)
            {
            return this;
            }
        else
            {
            return super.owner();
            }
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByCommunity"
        )
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

    @Override
    public String link()
        {
        return factories().identities().links().link(
            this
            );
        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_CURRENT_SCHEMA_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcSchema jdbcschema ;
    @Override
    public JdbcSchema space()
        {
        return this.jdbcschema;
        }
    @Override
    public JdbcSchema space(final boolean create)
        {
        if ((create) && (this.jdbcschema == null))
            {
            if (community() != null)
                {
                if (community().space(true) != null)
                    {
/*
                    this.jdbcschema = community().space().schemas().create(
                        this
                        );
 */
                    this.jdbcschema = community().space().schemas().simple();
                    }
                }
            }
        return this.jdbcschema;
        }
    }

