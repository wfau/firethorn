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
import org.springframework.http.MediaType;

import uk.ac.roe.wfau.firethorn.config.ConfigProperty;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Abstract base class for Spring MVC controllers.
 *
 */
@Slf4j
public abstract class AbstractController
    {

    /**
     * HTTP content type for JSON.
     * @deprecated Use {@link MediaType.APPLICATION_JSON_VALUE}
     * 
     */
    public static final String JSON_MIME = MediaType.APPLICATION_JSON_VALUE ;

    /**
     * HTTP content type for JSON.
     * @deprecated Use {@link MediaType.APPLICATION_FORM_URLENCODED_VALUE}
     * 
     */
    public static final String FORM_MIME = MediaType.APPLICATION_FORM_URLENCODED_VALUE;

    /**
     * Request property for the copy depth.
     *
     */
    public static final String ADQL_COPY_DEPTH_URN = "urn:adql.copy.depth" ;

    /**
     * URL path for the select method.
     *
     */
    public static final String SELECT_PATH = "select" ;

    /**
     * URL path for the search method.
     *
    public static final String SEARCH_PATH = "search" ;
     */

    /**
     * URL path for the create method.
     *
     */
    public static final String CREATE_PATH = "create" ;

    /**
     * URL path for the import method.
     *
     */
    public static final String IMPORT_PATH = "import" ;

    /**
     * The base URI config property key.
     *
     */
    public static final URI BASE_URI_CONFIG_KEY = URI.create(
        "urn:firethorn.system.base.uri"
        );

    /**
     * Protected constructor.
     * @todo Refactor this to include the path in the constructor.
     *
     */
    protected AbstractController()
        {
        }

    /**
     * Autowired ComponentFactories instance.
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
     * @todo Alternatively, this should come from the URL in the HttpRequest
     *
     */
    public URI base()
        {
        if (this.base == null)
            {
            //
            // TODO wrap this into a config service API.
            final ConfigProperty prop = factories().config().entities().select(
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
     * Create a URI path from a String.
     *
     */
    public Path path(final String string)
        {
        return new PathImpl(
            string
            );
        }
    
    /**
     * Run a Runnable in a Select transaction.
     * 
     */
    public void select(final Runnable runnable)
        {
        log.debug("select(Runnable) ------------");
        factories().spring().transactor().select(
            runnable
            );
        log.debug("-----------------------------");
        }

    /**
     * Run a Runnable in an Update transaction.
     * 
     */
    public void update(final Runnable runnable)
        {
        log.debug("update(Runnable) ------------");
        factories().spring().transactor().update(
            runnable
            );
        log.debug("-----------------------------");
        }
    }

