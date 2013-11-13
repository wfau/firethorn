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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Public interface for a Member of a Community.
 *
 */
public interface CommunityMember
extends Identity, Entity, NamedEntity
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<CommunityMember>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Factory interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<CommunityMember>
        {

        /**
         * Create a new CommunityMember.
         *
         */
        public CommunityMember create(final Community community, final String name);

        /**
         * Select a CommunityMember.
         *
         */
        public CommunityMember select(final Community community, final String name);

        }

    /**
     * The parent Community.
     *
     */
    public Community community();

    /**
     *  The storage space for this Identity.
     *
     */
    public JdbcSchema space();

    /**
     *  The storage space for this Identity.
     *
     */
    public JdbcSchema space(final boolean create);

    }

