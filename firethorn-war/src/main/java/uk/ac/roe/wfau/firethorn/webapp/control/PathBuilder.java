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

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * A webapp path builder.
 *
 */
@Slf4j
public class PathBuilder
    {

    /**
     * URL path delimiter.
     *
     */
    public static final String DELIMITER = "/" ;

    /**
     * Public constructor.
     *
     */
    public PathBuilder(HttpServletRequest request)
        {
        log.debug("PathBuilder(HttpServletRequest)");
        log.debug(" Request [{}]", request.getRequestURI());
        log.debug(" Context [{}]", request.getContextPath());
        log.debug(" Servlet [{}]", request.getServletPath());
        this.request = request ;
        }

    /**
     * Our ServletRequest.
     *
     */
    private HttpServletRequest request ;

    /**
     * Access to our ServletRequest.
     *
     */
    public HttpServletRequest request()
        {
        return this.request ;
        }

    /**
     * Spring redirect URL scheme.
     *
     */
    public static final String REDIRECT_SCHEME = "redirect:" ;

    /**
     * The URL path for our Spring dispatcher servlet.
     * 
     */
    public static final String SERVLET_PATH = "" ;

    /**
     * The URL path for our static content.
     * 
     */
    public static final String STATIC_PATH = "static" ;

    /**
     * Get the request context path.
     *
     */
    public String getContextPath()
        {
        return request.getContextPath();
        }

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
     * Create a Spring redirect URL.
     *
     */
    public Path redirect(String path)
        {
        log.debug("PathBuilder.redirect(String)");
        log.debug("  Path [{}]", path);
        return new Path(
            REDIRECT_SCHEME,
            path
            );
        }

    /**
     * Create an internal webapp path.
     *
     */
    public Path path(String path)
        {
        log.debug("PathBuilder.path(String)");
        log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getServletPath(),
            path
            );
        }

    /**
     * Create a static file path.
     *
     */
    public Path file(String path)
        {
        log.debug("PathBuilder.file(String)");
        log.debug("  Path [{}]", path);
        return new Path(
            this.getContextPath(),
            this.getStaticPath(),
            path
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
        public Path(String base)
            {
            log.debug("PathBuilder.Path(String)");
            log.debug("  Base [{}]", base);
            builder = new StringBuilder(
                base
                ) ;
            }

        /**
         * Public constructor.
         *
         */
        public Path(String base, String... paths)
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
        public void append(String... paths)
            {
            for(String path : paths)
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
        public void append(String path)
            {
            log.debug("PathBuilder.Path.append(String)");
            log.debug("  Path [{}]", path);
            if (path.startsWith(DELIMITER))
                {
                builder.append(path);
                }
            else {
                builder.append(DELIMITER);
                builder.append(path);
                }
            }

        /**
         * Convert to a String.
         *
         */
        public String toString()
            {
            return builder.toString();
            }
        }
    }

