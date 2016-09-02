/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 *
 *
 */
public interface OgsaDQPResource
    extends OgsaBaseResource
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaDQPResource>
        {
        }
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaDQPResource>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaDQPResource>
        {
        /**
         * Select all the {@link OgsaDQPResource}(s).
         * @return An {@link Iterable} set of {@link OgsaDQPResource}(s). 
         *
         */
        public Iterable<OgsaDQPResource> select();

        /**
         * Select the {@link OgsaDQPResource}(s) for a {@link OgsaService}.
         * @param service The {@link OgsaService} service.
         * @return An {@link Iterable} list of {@link OgsaDQPResource}(s).
         *
         */
        public Iterable<OgsaDQPResource> select(final OgsaService service);

        /**
         * Create a new {@link OgsaDQPResource} for an {@link OgsaService}.
         * @param service The parent {@link OgsaService}.
         * @return A new {@link OgsaDQPResource}.
         *
         */
        public OgsaDQPResource create(final OgsaService service);

        /**
         * Select the primary {@link OgsaDQPResource} for an {@link OgsaService}.
         * @param service The {@link OgsaService}.
         * @return The primary {@link OgsaDQPResource}.
         *
         */
        public OgsaDQPResource primary(final OgsaService service);

        /**
         * Our primary {@link OgsaDQPResource).
         * 
         */
        public OgsaDQPResource primary();

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends Entity.EntityServices<OgsaDQPResource>
        {
        /**
         * Our {@link OgsaDQPResource.EntityFactory} instance.
         *
         */
        public OgsaDQPResource.EntityFactory entities();
        
        }
    
    /**
     * Initialize the OGSA-DAI resource, creating a new one if needed.
     * @return The resource status.
     *  
     */
    public OgsaStatus init();

    }
