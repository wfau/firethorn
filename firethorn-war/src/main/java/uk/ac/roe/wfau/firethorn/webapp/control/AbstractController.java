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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Abstract base class for Spring MVC controllers.
 *
 */
@Slf4j
@Controller
public abstract class AbstractController
    {

    /**
     * HTTP content type for JSON.
     */
    public static final String JSON_MAPPING = "application/json" ;

    /**
     * The base URI config property key.
     *
     */
    public static final URI BASE_URI_CONFIG_KEY = URI.create(
        "urn:firethorn.system.base.uri"
        );

    /**
     * Protected constructor.
     * @param path
     *
     */
    protected AbstractController()
        {
        }

    /**
     * Autowired system services.
     *
     */
    @Autowired
    private ComponentFactories factories;

    /**
     * Our system services.
     *
     */
    public ComponentFactories factories()
        {
        return this.factories;
        }

    /**
     * The base URI (URL) for our webapp.
     *
     */
    private URI base;

    /**
     * The base URI (URL) for our webapp.
     *
     */
    public URI base()
        {
        if (this.base == null)
            {
            //
            // TODO wrap this into a config service API.
            final ConfigProperty prop = factories().config().select(
                BASE_URI_CONFIG_KEY
                );
            if (prop != null)
                {
                this.base = prop.toUri();
                }
            }
        log.debug("base() [{}]", this.base);
        return this.base ;
        }

    /**
     * URI path for this Controller.
     *
     */
    public abstract Path path();

    /**
     * Create a URI Path from a String.
     *
     */
    protected Path path(String path)
        {
        return new PathImpl(
            path
            );
        }
    
    /**
     * URI builder for this Controller.
     *
    public UriBuilder builder(final HttpServletRequest request)
        {
        log.debug("builder() [{}][{}]", request.getRequestURL(), this.base());
        return new HttpUriBuilder(
            request,
            this.base(),
            this.path()
            );
        }
     */
    }

