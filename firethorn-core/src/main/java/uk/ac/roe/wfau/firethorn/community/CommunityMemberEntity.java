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
package uk.ac.roe.wfau.firethorn.community;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.AbstractNamedEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
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
    name = CommunityMemberEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList=CommunityMemberEntity.DB_COMMUNITY_COL
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name = "Identity-select-all",
            query = "FROM CommunityMemberEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name = "Identity-select-community.name",
            query = "FROM CommunityMemberEntity WHERE community = :community AND name = :name ORDER BY ident desc"
            )
        }
    )
public class CommunityMemberEntity
extends AbstractNamedEntity
implements CommunityMember, Identity
    {

    /**
     * Hibernate table mapping.
     *
     */
    public static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "CommunityMemberEntity";

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
    extends AbstractEntityFactory<CommunityMember>
    implements CommunityMember.EntityFactory
        {
        @Override
        public Class<?> etype()
            {
            return CommunityMemberEntity.class;
            }

        @Override
        @CreateMethod
        public CommunityMember create(final Community community, final String name)
            {
            log.debug("create(Community, String) [{}][{}]", community.uri(), name);
            final CommunityMember member = this.select(
                community,
                name
                );
            if (member != null)
                {
                return member ;
                }
            else {
                return super.insert(
                    new CommunityMemberEntity(
                        community,
                        name
                        )
                    );
                }
            }

        @Override
        @SelectMethod
        public CommunityMember select(final Community community, final String name)
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
        protected CommunityMember.IdentFactory idents;
        @Override
        public CommunityMember.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected CommunityMember.LinkFactory links;
        @Override
        public CommunityMember.LinkFactory links()
            {
            return this.links;
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected CommunityMemberEntity()
        {
        super();
        }

    /**
     * Create a new IdentityEntity, setting the owner to null.
     *
     */
    protected CommunityMemberEntity(final Community community, final String name)
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

    @Override
    public String link()
        {
        return factories().communities().members().links().link(
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
    }

