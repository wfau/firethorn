/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.base;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

/**
 * {@link TreeComponent} implementation.
 *
 */
@Slf4j
@Access(
    AccessType.FIELD
    )
@MappedSuperclass
public abstract class TreeComponentEntity<ComponentType extends TreeComponent>
extends BaseComponentEntity<ComponentType>
implements TreeComponent
    {
    /**
     * Hibernate column mapping.
     *
     */
    public static final String DB_BASE_COL   = "base";
    public static final String DB_PARENT_COL = "parent";

    protected static final String DB_COPY_DEPTH_COL = "copydepth" ;

    /**
     * {@link TreeComponent.EntityFactory} implementation.
     *
     */
    @Repository
    public static abstract class EntityFactory<ComponentType extends TreeComponent>
    extends BaseComponentEntity.EntityFactory<ComponentType>
    implements TreeComponent.EntityFactory<ComponentType>
        {
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected TreeComponentEntity()
        {
        super();
        //log.debug("TreeComponentEntity()");
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected TreeComponentEntity(final String name)
        {
        this(
            CopyDepth.FULL,
            name
            );
        }

    /**
     * Protected constructor, owner defaults to the current actor.
     *
     */
    protected TreeComponentEntity(final CopyDepth depth, final String name)
        {
        super(
            name
            );
        this.depth = depth;
        //log.debug("TreeComponentEntity(CopyDepth, String)");
        //log.debug("  Depth [{}]", depth);
        //log.debug("  Name  [{}]", name);
        }

    @Column(
        name = DB_COPY_DEPTH_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    protected CopyDepth depth = CopyDepth.FULL ;

    @Override
    public CopyDepth depth()
        {
        return this.depth;
        }

    @Override
    public void depth(final CopyDepth type)
        {
        this.depth = type;
        }

    @Override
    public String fullname()
        {
        return namebuilder().toString();
        }
    }
