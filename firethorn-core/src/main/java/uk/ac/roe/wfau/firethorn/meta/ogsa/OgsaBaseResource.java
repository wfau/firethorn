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
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public interface OgsaBaseResource
    extends NamedEntity
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaBaseResource>
        {
        }

    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<OgsaBaseResource>
        {
        }
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaBaseResource>
        {

        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaBaseResource>
        {
        /**
         * Select all the {@link OgsaBaseResource}(s).
         * @return An {@link Iterable} set of {@link OgsaBaseResource}(s). 
         *
         */
        public Iterable<OgsaBaseResource> select();

        /**
         * Select the {@link OgsaBaseResource}(s) provided by a {@link OgsaService}.
         * @param service The parent service.
         * @return An {@link Iterable} list of {@link OgsaBaseResource}(s).
         *
         */
        public Iterable<OgsaBaseResource> select(final OgsaService service);

        /**
         * Create a new {@link OgsaJdbcResource}.
         * @param service The parent service.
         * @param source  The corresponding {@link JdbcResource}.
         * @return A new {@link OgsaJdbcResource}.
         *
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource source);
         */

        /**
         * Select the {@link OgsaJdbcResource}(s) for a {@link JdbcResource}.
         * @param service The parent {@link OgsaService}.
         * @param source  The source {@link JdbcResource}.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         *
        public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source);
         */

        /**
         * Create a new {@link OgsaIvoaResource}.
         * @param service The parent {@link OgsaService}.
         * @param source  The source {@link IvoaResource}.
         * @return A new {@link OgsaIvoaResource}.
         *
        public OgsaIvoaResource create(final OgsaService service, final IvoaResource source);
         */

        /**
         * Select the {@link OgsaIvoaResource}(s) for a {@link IvoaResource}.
         * @param service The parent {@link OgsaService}.
         * @param source  The source {@link IvoaResource}.
         * @return An {@link Iterable} list of {@link OgsaIvoaResource}(s).
         *
        public Iterable<OgsaIvoaResource> select(final OgsaService service, final IvoaResource source);
         */
        
        }

    /**
     * The parent {@link OgsaService}.
     * @return The {@link OgsaService}.
     *
     */
    public OgsaService service();

    /**
     * The parent {@link BaseResource}.
     * @return The {@link BaseResource}.
     *
     */
    public BaseResource<?> source();

    /**
     * The OGSA-DAI resource identifier.
     * @return The identifier
     *
     */
    public String ogsaid();

    /**
     * OGSA-DAI resource status.
     *
     */
    public static enum Status
        {
        CREATED(),
        ACTIVE(),
        FAILED(),
        UNKNOWN();
        }

    /**
     * Get the resource status.
     * @return The resource status.
     *
     */
    public Status status();

    /**
     * Check the resource status.
     * @return The resource status.
     *
     */
    public Status ping();
    
    }
