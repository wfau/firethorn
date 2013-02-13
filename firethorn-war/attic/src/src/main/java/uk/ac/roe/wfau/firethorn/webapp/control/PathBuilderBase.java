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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * A webapp path builder.
 * @todo Add URI handling to create Location headers.
 *
 */
@Slf4j
@Deprecated
public abstract class PathBuilderBase implements PathBuilder
    {

    /**
     * Protected constructor.
     *
     */
    protected PathBuilderBase()
        {
        }

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


    @Override
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

    @Override
    public abstract String getContextPath();

    @Override
    public String redirect(final String path)
        {
        log.debug("PathBuilderBase.redirect(String)");
        log.debug("  Path [{}]", path);
        return new PathImpl(
            REDIRECT_SCHEME,
            path
            ).toString();
        }

    @Override
    public String redirect(final String base, final String path)
        {
        log.debug("PathBuilderBase.redirect(String, String)");
        log.debug("  Base [{}]", base);
        log.debug("  Path [{}]", path);
        return new PathImpl(
            REDIRECT_SCHEME,
            base,
            path
            ).toString();
        }

    @Override
    public String redirect(final String base, final Identifier ident)
        {
        log.debug("PathBuilderBase.redirect(String, Identifier)");
        log.debug("  Base  [{}]", base);
        log.debug("  Ident [{}]", ident);
        return redirect(
            base,
            ident.toString()
            );
        }

    @Override
    public String redirect(final String base, final Entity entity)
        {
        log.debug("PathBuilderBase.redirect(String, Entity)");
        log.debug("  Base   [{}]", base);
        log.debug("  Entity [{}]", entity);
        return redirect(
            base,
            entity.ident()
            );
        }

    @Override
    public Path path(final String path)
        {
        log.debug("PathBuilderBase.path(String)");
        log.debug("  Path [{}]", path);
        return new PathImpl(
            this.getContextPath(),
            this.getServletPath(),
            path
            );
        }

    @Override
    public Path path(final String base, final String path)
        {
        log.debug("PathBuilderBase.path(String, String)");
        log.debug("  Base [{}]", base);
        log.debug("  Path [{}]", path);
        return new PathImpl(
            this.getContextPath(),
            this.getServletPath(),
            base,
            path
            );
        }

    @Override
    public Path path(final String base, final Identifier ident)
        {
        log.debug("PathBuilderBase.path(String, Identifier)");
        log.debug("  Base  [{}]", base);
        log.debug("  Ident [{}]", ident);
        if (base.contains("{ident}"))
            {
            return path(
                base.replaceFirst(
                    "\\{ident\\}",
                    ident.toString()
                    )
                );

            }
        else {
            return path(
                base,
                ident.toString()
                );
            }
        }

    @Override
    public Path path(final String base, final Entity entity)
        {
        log.debug("PathBuilderBase.path(String, Entity)");
        log.debug("  Base   [{}]", base);
        log.debug("  Entity [{}]", entity);
        return path(
            base,
            entity.ident()
            );
        }

    @Override
    public Path file(final String path)
        {
        log.debug("PathBuilderBase.file(String)");
        log.debug("  Path [{}]", path);
        return new PathImpl(
            this.getContextPath(),
            this.getStaticPath(),
            path
            );
        }

    @Override
    public URI location(final String path)
        {
        log.debug("PathBuilderBase.location(String)");
        log.debug("  Path [{}]", path);
        return new PathImpl(
            this.getContextPath(),
            this.getServletPath(),
            path
            ).toUri();
        }

    @Override
    public URI location(final String base, final String path)
        {
        log.debug("PathBuilderBase.location(String, String)");
        log.debug("  Base [{}]", base);
        log.debug("  Path [{}]", path);
        return new PathImpl(
            this.getContextPath(),
            this.getServletPath(),
            base,
            path
            ).toUri();
        }

    @Override
    public URI location(final String base, final Identifier ident)
        {
        log.debug("PathBuilderBase.location(String, Identifier)");
        log.debug("  Base  [{}]", base);
        log.debug("  Ident [{}]", ident);
        return location(
            base,
            ident.toString()
            );
        }

    @Override
    public URI location(final String base, final Entity entity)
        {
        log.debug("PathBuilderBase.location(String, Entity)");
        log.debug("  Base   [{}]", base);
        log.debug("  Entity [{}]", entity);
        return location(
            base,
            entity.ident()
            );
        }

    /**
     * Inner class to represent a path.
     *
     */
    public class PathImpl implements Path
        {

        /**
         * Public constructor.
         *
         */
        public PathImpl(final String base)
            {
            log.debug("PathBuilderBase.Path(String)");
            log.debug("  Base [{}]", base);
            builder = new StringBuilder(
                base
                ) ;
            }

        /**
         * Public constructor.
         *
         */
        public PathImpl(final String base, final String... paths)
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

        @Override
        public void append(final String... paths)
            {
            for(final String path : paths)
                {
                append(
                    path
                    );
                }
            }

        @Override
        public void append(final String path)
            {
            log.debug("PathBuilderBase.Path.append(String)");
            log.debug("  Path [{}]", path);

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

        @Override
        public String toString()
            {
            return builder.toString();
            }

        @Override
        public URI toUri()
            {
            return URI.create(
                this.toString()
                );
            }
        }
    }

