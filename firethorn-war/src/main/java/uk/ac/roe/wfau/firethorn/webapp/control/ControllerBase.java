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
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.webapp.paths.HttpUriBuilder;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;

/**
 * Base class for our MVC controllers.
 *
 */
@Slf4j
@Controller
public abstract class ControllerBase
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
     * @param base
     *
    protected ControllerBase(URI base)
        {
        this.base = base ;
        }
     */

    /**
     * Protected constructor.
     * @param path
     *
     */
    protected ControllerBase()
        {
        }

    /**
     * Autowired system services.
     *
     */
    @Autowired
    private Womble womble ;

    /**
     * Our system services.
     *
     */
    public Womble womble()
        {
        return this.womble ;
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
            ConfigProperty prop = womble.config().select(
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
     * URI builder for this Controller.
     *
     */
    public UriBuilder builder(final HttpServletRequest request)
        {
        log.debug("builder() [{}][{}]", request.getRequestURL(), this.base());
        return new HttpUriBuilder(
            request,
            this.base(),
            this.path()
            );
        }
    }

