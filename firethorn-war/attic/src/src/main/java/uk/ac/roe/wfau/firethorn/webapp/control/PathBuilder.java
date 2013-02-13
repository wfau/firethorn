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
package uk.ac.roe.wfau.firethorn.webapp.control;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.tuesday.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder.Path;

@Deprecated
public interface PathBuilder
    {

    public interface Path
        {

        /**
         * Append a set of paths.
         *
         */
        public abstract void append(final String... paths);

        /**
         * Append a path.
         *
         */
        public abstract void append(final String path);

        /**
         * Convert to a String.
         *
         */
        @Override
        public abstract String toString();

        /**
         * Convert to a URI.
         *
         */
        public abstract URI toUri();

        }

    /**
     * URL path delimiter.
     *
     */
    public static final String DELIMITER = "/";
    /**
     * Spring redirect URL scheme.
     *
     */
    public static final String REDIRECT_SCHEME = "redirect:";

    /**
     * Get the request context path.
     *
     */
    public abstract String getContextPath();

    /**
     * Get our Servlet path.
     *
     */
    public abstract String getServletPath();

    /**
     * Create a Spring redirect URI (string).
     *
     */
    public abstract String redirect(final String path);

    /**
     * Create a Spring redirect URI (string).
     *
     */
    public abstract String redirect(final String base, final String path);

    /**
     * Create a Spring redirect URI (string).
     *
     */
    public abstract String redirect(final String base, final Entity entity);

    /**
     * Create a Spring redirect URI (string).
     *
     */
    public abstract String redirect(final String base, final Identifier ident);

    /**
     * Create an internal webapp path.
     *
     */
    public abstract Path path(final String path);

    /**
     * Create an internal webapp path.
     *
     */
    public abstract Path path(final String base, final String path);

    /**
     * Create an internal webapp path.
     *
     */
    public abstract Path path(final String base, final Identifier ident);

    /**
     * Create an internal webapp path.
     *
     */
    public abstract Path path(final String base, final Entity entity);

    /**
     * Create a static file path.
     *
     */
    public abstract Path file(final String path);

    /**
     * Create a location URI.
     *
     */
    public abstract URI location(final String path);

    /**
     * Create a location URI.
     *
     */
    public abstract URI location(final String base, final String path);

    /**
     * Create a location URI.
     *
     */
    public abstract URI location(final String base, final Identifier ident);

    /**
     * Create a location URI.
     *
     */
    public abstract URI location(final String base, final Entity entity);

    }
