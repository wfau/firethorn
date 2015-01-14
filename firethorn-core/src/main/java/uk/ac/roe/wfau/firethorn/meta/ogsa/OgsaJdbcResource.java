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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public interface OgsaJdbcResource
    extends OgsaBaseResource
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaJdbcResource>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaJdbcResource>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaJdbcResource>
        {
        /**
         * Select all the {@link OgsaJdbcResource}(s).
         * @return An {@link Iterable} set of {@link OgsaJdbcResource}(s). 
         *
         */
        public Iterable<OgsaJdbcResource> select();

        /**
         * Select the {@link OgsaJdbcResource}(s) for a {@link OgsaService}.
         * @param service The {@link OgsaService} service.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         *
         */
        public Iterable<OgsaJdbcResource> select(final OgsaService service);

        /**
         * Select all the {@link OgsaJdbcResource}(s) for a {@link JdbcResource}.
         * @param source The {@link JdbcResource} resource.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         *
         */
        public Iterable<OgsaJdbcResource> select(final JdbcResource source);

        /**
        * Select the {@link OgsaJdbcResource}(s) for an {@link OgsaService} and {@link JdbcResource}.
        * @param service The {@link OgsaService}.
        * @param source  The {@link JdbcResource}.
        * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
        *
        */
       public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source);

        /**
         * Create a new {@link OgsaJdbcResource} for an {@link OgsaService} and {@link JdbcResource}..
         * @param service The {@link OgsaService}.
         * @param source  The {@link JdbcResource}.
         * @return A new {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource source);

        /**
         * Select the primary {@link OgsaJdbcResource} for a {@link JdbcResource}.
         * @param source The {@link JdbcResource} resource.
         * @return The primary {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource primary(final JdbcResource source);

        /**
         * Select the primary {@link OgsaJdbcResource} for an {@link OgsaService} and {@link JdbcResource}.
         * @param service The {@link OgsaService}.
         * @param source  The {@link JdbcResource}.
         * @return The primary {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource primary(final OgsaService service, final JdbcResource source);

        }
    
    /**
     * The parent {@link JdbcResource}.
     *  
     */
    public JdbcResource source();

    /**
     * Initialise the OGSA-DAI resource, creating a new one if needed.
     * @return The resource status.
     *  
     */
    public Status connect();

    /**
     * Release the OGSA-DAI resource.
     * @return The resource status.
     * 
     */
    public Status release();
    
    }
