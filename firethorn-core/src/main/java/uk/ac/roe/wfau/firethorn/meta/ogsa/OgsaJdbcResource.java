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
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<OgsaJdbcResource>
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
         * Select all the resources.
         * @return An {@link Iterable} set of {@link OgsaJdbcResource}(s). 
         *
         */
        public Iterable<OgsaJdbcResource> select();

        /**
         * Select the {@link OgsaJdbcResource}(s) provided by a {@link OgsaService}.
         * @param service The parent service.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         *
         */
        public Iterable<OgsaJdbcResource> select(final OgsaService service);

        /**
         * Create a new {@link OgsaJdbcResource}.
         * @param service The parent service.
         * @param source  The corresponding {@link JdbcResource}.
         * @return A new {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource source);

        /**
         * Create a new {@link OgsaJdbcResource}.
         * @param service The parent service.
         * @param source  The corresponding {@link JdbcResource}.
         * @param name The resource name.
         * @return A new {@link OgsaJdbcResource}.
         *
         */
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource source, final String name);

        /**
         * Select the {@link OgsaJdbcResource}(s) for a {@link JdbcResource}.
         * @param service The parent {@link OgsaService}.
         * @param source  The source {@link JdbcResource}.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         *
         */
        public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source);
        
        }
    
    /**
     * The parent {@link JdbcResource}.
     *  
     */
    public JdbcResource source();

    }
