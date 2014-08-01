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

import org.springframework.http.HttpStatus;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public interface OgsaService
    extends NamedEntity
    {

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaService>
        {
        }

    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<OgsaService>
        {
        }
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaService>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaService>
        {

        /**
         * Select all the services.
         * @return An {@link Iterable} set of {@link OgsaService}(s). 
         *
         */
        public Iterable<OgsaService> select();

        /**
         * Create a new service.
         * @return A new {@link OgsaService}.
         *
         */
        public OgsaService create();

        /**
         * Create a new service.
         * @param endpoint The OGSA-DAI web-service endpoint URL.
         * @return A new {@link OgsaService}.
         *
         */
        public OgsaService create(final String endpoint);

        /**
         * Create a new service.
         * @param name The OGSA-DAI web-service name.
         * @param endpoint The OGSA-DAI web-service endpoint URL.
         * @return A new {@link OgsaService}.
         *
         */
        public OgsaService create(final String name, final String endpoint);

        }

    /**
     * The OGSA-DAI service endpoint URL.
     * @return The endpoint URL.
     *
     */
    public String endpoint();

    /**
     * The OGSA-DAI -service version.
     * @return The service version.
     *
     */
    public String version();

    /**
     * Get the service status.
     * @return The service status.
     *
     */
    public HttpStatus http();

    /**
     * Check the service status.
     * @return The service status.
     *
     */
    public HttpStatus ping();

    /**
     * Access to the {@link OgsaBaseResource}(s) provided by this {@link OgsaService}.
     *
     */
    interface Resources
        {
        /**
         * Select the {@link OgsaBaseResource}(s) provided by this {@link OgsaService}.
         * @return An {@link Iterable} list of {@link OgsaBaseResource}(s).
         *
         */
        public Iterable<OgsaBaseResource> select();

        /**
         * Create a new {@link OgsaJdbcResource}.
         * @param source The corresponding {@link JdbcResource}. 
         * @return A new {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource create(final JdbcResource source);

        /**
         * Create a new {@link OgsaIvoaResource}.
         * @param source The corresponding {@link IvoaResource}. 
         * @return A new {@link OgsaIvoaResource}.
         *
         */
        public OgsaIvoaResource create(final IvoaResource source);

        /**
         * Select a list of the {@link OgsaBaseResource}(s) for a {@link JdbcResource}.
         * @return An {@link Iterable} list of {@link OgsaBaseResource}(s).
         *
         */
        public Iterable<OgsaJdbcResource> select(final JdbcResource source);

        /**
         * Select a list of the {@link OgsaBaseResource}(s) for a {@link JdbcResource}.
         * @return An {@link Iterable} list of {@link OgsaBaseResource}(s).
         *
         */
        public Iterable<OgsaIvoaResource> select(final IvoaResource source);
        
        }

    /**
     * Access to the {@link OgsaBaseResource}(s) provided by this {@link OgsaService}.
     *
     */
    public Resources resources();

    /**
     * OGSA-DAI service status.
     *
    public static enum Status
        {
        CREATED(),
        ONLINE(),
        OFFLINE(),
        UNKNOWN();
        }
     */


    }
