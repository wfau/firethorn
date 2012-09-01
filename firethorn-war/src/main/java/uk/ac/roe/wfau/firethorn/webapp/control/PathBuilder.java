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
package uk.ac.roe.wfau.firethorn.webapp.control ;

import java.net.URI;

//import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.common.entity.Entity ;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier ;

/**
 * A webapp path builder.
 * @todo Add URI handling to create Location headers.
 *
 */
//@Slf4j
public abstract class PathBuilder
    {

    /**
     * Protected constructor.
     *
     */
    protected PathBuilder()
        {
        }

    /**
     * URL path delimiter.
     *
     */
    public static final String DELIMITER = "/" ;

    /**
     * Spring redirect URL scheme.
     *
     */
    public static final String REDIRECT_SCHEME = "redirect:" ;

    /**
     * The URL path for our Spring dispatcher servlet.
     * @todo This should be configurable rather than hard coded.
     *
     */
    public static final String SERVLET_PATH = "" ;

    /**
     * The URL path for our static content.
     * @todo This should be configurable rather than hard coded.
     *
     */
    public static final String STATIC_PATH = "static" ;

    /**
     * Get the request context path.
     *
     */
    public abstract String getContextPath();

    /**
     * Get our Servlet path.
     *
     */
    public String getServletPath()
        {
        return SERVLET_PATH;
        }

    /**
     * Get our static content path.
     *
     */
    public String getStaticPath()
        {
        return STATIC_PATH;
        }

    /**
     * Create a Spring redirect String.
     *
     */
    public String redirect(final String path)
        {
        //log.debug("PathBuilder.redirect(String)");
        //log.debug("  Path [{}]", path);
        return new Path(
            REDIRECT_SCHEME,
            path
            ).toString();
        }

    /**
     * Create a Spring redirect String.
     *
     */
    public String redirect(final String base, final String path)
        {
        //log.debug("PathBuilder.redirect(String, String)");
        //log.debug("  Base [{}]", base);
        //log.debug("  Path [{}]", path);
        return new Path(
            REDIRECT_SCHEME,
            base,
            path
            ).toString();
        }

    /**
     * Create a Spring redirect String.
     *
     */
    public String redirect(final String base, final Identifier ident)
        {
        //log.debug("PathBuilder.redirect(String, Identifier)");
        //log.debug("  Base  [{}]", base);
        //log.debug("  Ident [{}]", ident);
        return redirect(
            base,
            ident.toString()
            );
        }

    /**
     * Create a Spring redirect String.
     *
     */
    public String redirect(final String base, final Entity entity)
        {
        //log.debug("PathBuilder.redirect(String, Entity)");
        //log.debug("  Base   [{}]", base);
        //log.debug("  Entity [{}]", entity);
        return redirect(
            base,
            entity.ident()
            );
        }


    /**
     * Create an internal webapp path.
     *
     */
    public Path path(final String path)
        {
        //log.debug("PathBuilder.path(String)");
        //log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getServletPath(),
            path
            );
        }

    /**
     * Create an internal webapp path.
     *
     */
    public Path path(final String base, final String path)
        {
        //log.debug("PathBuilder.path(String, String)");
        //log.debug("  Base [{}]", base);
        //log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getServletPath(),
            base,
            path
            );
        }

    /**
     * Create an internal webapp path.
     *
     */
    public Path path(final String base, final Identifier ident)
        {
        //log.debug("PathBuilder.path(String, Identifier)");
        //log.debug("  Base  [{}]", base);
        //log.debug("  Ident [{}]", ident);
        return path(
            base,
            ident.toString()
            );
        }

    /**
     * Create an internal webapp path.
     *
     */
    public Path path(final String base, final Entity entity)
        {
        //log.debug("PathBuilder.path(String, Entity)");
        //log.debug("  Base   [{}]", base);
        //log.debug("  Entity [{}]", entity);
        return path(
            base,
            entity.ident()
            );
        }

    /**
     * Create a static file path.
     *
     */
    public Path file(final String path)
        {
        //log.debug("PathBuilder.file(String)");
        //log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getStaticPath(),
            path
            );
        }

    /**
     * Create a location URI.
     *
     */
    public URI location(final String path)
        {
        //log.debug("PathBuilder.location(String)");
        //log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getServletPath(),
            path
            ).toUri();
        }

    /**
     * Create a location URI.
     *
     */
    public URI location(final String base, final String path)
        {
        //log.debug("PathBuilder.location(String, String)");
        //log.debug("  Base [{}]", base);
        //log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getServletPath(),
            base,
            path
            ).toUri();
        }

    /**
     * Create a location URI.
     *
     */
    public URI location(final String base, final Identifier ident)
        {
        //log.debug("PathBuilder.location(String, Identifier)");
        //log.debug("  Base  [{}]", base);
        //log.debug("  Ident [{}]", ident);
        return location(
            base,
            ident.toString()
            );
        }

    /**
     * Create a location URI.
     *
     */
    public URI location(final String base, final Entity entity)
        {
        //log.debug("PathBuilder.location(String, Entity)");
        //log.debug("  Base   [{}]", base);
        //log.debug("  Entity [{}]", entity);
        return location(
            base,
            entity.ident()
            );
        }

    /**
     * Inner class to represent a path.
     *
     */
    public class Path
        {

        /**
         * Public constructor.
         *
         */
        public Path(final String base)
            {
            //log.debug("PathBuilder.Path(String)");
            //log.debug("  Base [{}]", base);
            builder = new StringBuilder(
                base
                ) ;
            }

        /**
         * Public constructor.
         *
         */
        public Path(final String base, final String... paths)
            {
            this(
                base
                );
            this.append(
                paths
                );
            }

        /**
         * Internal String builder.
         *
         */
        protected StringBuilder builder ;

        /**
         * Append a set of paths.
         *
         */
        public void append(final String... paths)
            {
            for(final String path : paths)
                {
                append(
                    path
                    );
                }
            }

        /**
         * Append a path.
         *
         */
        public void append(final String path)
            {
            //log.debug("PathBuilder.Path.append(String)");
            //log.debug("  Path [{}]", path);

            final String trim = path.trim() ;

            if (trim.length() > 0)
                {
                if (trim.startsWith(DELIMITER) == false)
                    {
                    builder.append(DELIMITER);
                    }
                builder.append(trim);
                }
            }

        /**
         * Convert to a String.
         *
         */
        @Override
        public String toString()
            {
            return builder.toString();
            }

        /**
         * Convert to a URI.
         *
         */
        public URI toUri()
            {
            return URI.create(
                this.toString()
                );
            }
        }
    }

