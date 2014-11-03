/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.discovery.client;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 *
 *
 */
class DiscoveryServiceBase
    {
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DiscoveryServiceBase.class);

    /**
     * The shared REST service client.
     *
     */
    private static final RestTemplate rest = new RestTemplate() ;
    static {
        rest.setMessageConverters(
            Arrays.asList(
                new HttpMessageConverter<?>[]
                    {
                    new MappingJacksonHttpMessageConverter()
                    }
                )
            );
        }

    public  RestTemplate rest()
        {
        return rest;
        }

    /**
     * Protected constructor.
     *
     */
    protected DiscoveryServiceBase(final String endpoint)
        {
        log.debug("DiscoveryServiceBase(String)");
        log.debug("  Endpoint [" + endpoint + "]");
        this.endpoint = endpoint  ;

        }

    private final String endpoint ;
    public  String endpoint()
        {
        return this.endpoint ;
        }

    public  String endpoint(final String path)
        {
        final StringBuilder builder = new StringBuilder(
            this.endpoint
            );
        if (this.endpoint.endsWith("/"))
            {
            if (path.startsWith("/"))
                {
                builder.append(
                    path.substring(
                        1,
                        path.length()
                        )
                    );
                }
            else {
                builder.append(
                    path
                    );
                }
            }
        else {
            if (path.startsWith("/"))
                {
                builder.append(
                    path
                    );
                }
            else {
                builder.append(
                    "/"
                    );
                builder.append(
                    path
                    );
                }
            }
        return builder.toString();
        }

    /**
     *
     *
    public void debug(final RequestDetails request)
        {
        log.debug("RequestDetails ----");
        log.debug("  Resource ID [" + request.getResourceID() + "]");
        log.debug("  Request resource  [" + request.getRequestResource().toString() + "]");
        log.debug("  Security context  [" + request.getSecurityContext().toString() + "]");
        log.debug("  Activity instance [" + request.getActivityInstanceName().toString() + "]");
        log.debug("----");
        }
     */
    }


