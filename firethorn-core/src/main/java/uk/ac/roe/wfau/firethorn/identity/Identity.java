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
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Public interface for an identity.
 *
 */
public interface Identity
extends Entity
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Identity>
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
    public static interface Factory
    extends Entity.Factory<Identity>
        {

        /**
         * Create a new Identity.
         *
         */
        public Identity create(final Community community, final String name);

        /**
         * Select an Identity.
         *
         */
        public Identity select(final Community community, final String name);

        }

    /**
     * The community this Identity belongs to.
     * 
     */
    public Community community();

    /**
     * User storage builder interface.
     *
     */
    public static interface Builder
        {
        /**
         * Build a physical schema.
         *
         */
        public JdbcSchema create(final Identity identity);

        }

    /**
     * User storage assigned to this Identity.
     * 
     */
    public JdbcSchema store();

    /**
     * User storage assigned to this Identity.
     * 
     */
    public void store(JdbcSchema schema);


    /*
     * 
    Spaces spaces()
    Spaces
        {
        AdqlResource       create(String name)
        Iter<AdqlResource> select()
        Iter<AdqlResource> search(String text)

        AdqlResource       current()

        }

    Queries queries()
    Queries {}
     *
     */
    
    
    
    }

