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
package uk.ac.roe.wfau.firethorn.webapp.paths;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Deprecated
@Slf4j
public class HttpUriBuilder
    extends AbstractUriBuilder
    {

    /**
     * Choose a URI based on a base URI and a HttpRequest.
     * If the baseURI is null, then a new URI is generated form the HttpRequest properties.

     * @param request
     *      The HttpRequest.
     * @param base
     *      The base URI.
     * @return
     *      Either the base URI or a new URI generated from the HttpRequest.
     *
     */
    public static URI choose(final HttpServletRequest request, final URI base)
        {
        log.debug("choose() [{}][{}]", request.getRequestURL(), base);
        if (base != null)
            {
            return base ;
            }
        else {

/*
 * What we should be doing ...
 * Add the context and servlet paths to the path.
 * Base URI is "scheme:host:port/"
 * Path is "/context/servlet/" plus "path"
 * *then* glue them back together ...
 *
 */


            /*
            Path path = new PathImpl(
                request.getContextPath()
                ).append(
                    request.getServletPath()
                    );
             */
            final Path path = new PathImpl(
                request.getContextPath()
                ).append(
                    "/"
                    );

            try {
                URI uri ;
                if (request.getLocalPort() == 80)
                    {
                    uri = new URL(
                        request.getScheme(),
                        request.getServerName(),
                        path.toString()
                        ).toURI();
                    }
                else {
                    uri = new URL(
                        request.getScheme(),
                        request.getServerName(),
                        request.getLocalPort(),
                        path.toString()
                        ).toURI();
                    }
                log.debug("choose() [{}][{}]", request.getRequestURL(), uri);
                return uri ;
                }
            catch (final MalformedURLException ouch)
                {
                log.error("Failed to process request URI [{}]", ouch);
                return null ;
                }
            catch (final URISyntaxException ouch)
                {
                log.error("Failed to process request URI [{}]", ouch);
                return null ;
                }
            }
        }

    /**
     * Public constructor.
     * @param base
     * @param path
     *
     */
    public HttpUriBuilder(final HttpServletRequest request, final URI base, final Path path)
        {
        super(
            choose(
                request,
                base
                ),
            path
            );
        }
    }
